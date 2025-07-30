package com.jose.demoia.common.infrastructure;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    private Cloudinary cloudinary;

    @PostConstruct
    public void init() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    public String uploadImage(MultipartFile file, String folder) throws IOException {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "image"
                    )
            );
            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            throw new IOException("Error uploading image to Cloudinary: " + e.getMessage(), e);
        }
    }

    public void deleteImage(String publicId) throws IOException {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new IOException("Error deleting image from Cloudinary: " + e.getMessage(), e);
        }
    }

    public String extractPublicIdFromUrl(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
            return null;
        }

        try {
            // Extraer el public_id de la URL de Cloudinary
            // URL típica: https://res.cloudinary.com/cloud-name/image/upload/v1234567890/folder/public-id.jpg
            String[] parts = imageUrl.split("/");
            for (int i = 0; i < parts.length; i++) {
                if ("upload".equals(parts[i]) && i + 1 < parts.length) {
                    // Saltar la versión si existe (v1234567890)
                    int startIndex = i + 1;
                    if (parts[startIndex].startsWith("v") && parts[startIndex].length() > 1) {
                        startIndex++;
                    }

                    // Construir el public_id desde la carpeta hasta el nombre del archivo
                    StringBuilder publicId = new StringBuilder();
                    for (int j = startIndex; j < parts.length; j++) {
                        if (j > startIndex) {
                            publicId.append("/");
                        }
                        String part = parts[j];
                        // Quitar la extensión del último elemento
                        if (j == parts.length - 1 && part.contains(".")) {
                            part = part.substring(0, part.lastIndexOf('.'));
                        }
                        publicId.append(part);
                    }
                    return publicId.toString();
                }
            }
        } catch (Exception e) {
            // Log del error pero no fallar
            System.err.println("Error extracting public_id from URL: " + imageUrl + " - " + e.getMessage());
        }
        return null;
    }
}
