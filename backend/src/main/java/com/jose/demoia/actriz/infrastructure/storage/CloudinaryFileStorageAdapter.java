package com.jose.demoia.actriz.infrastructure.storage;

import com.jose.demoia.actriz.domain.ports.out.FileStoragePort;
import com.jose.demoia.common.infrastructure.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Primary // Esta anotación hace que Spring use esta implementación en lugar de LocalFileStorageAdapter
public class CloudinaryFileStorageAdapter implements FileStoragePort {

    @Autowired
    private CloudinaryService cloudinaryService;

    private static final List<String> TIPOS_IMAGEN_PERMITIDOS = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    private static final long TAMAÑO_MAXIMO = 5 * 1024 * 1024; // 5MB

    @Override
    public String guardarImagen(MultipartFile file, String categoria) {
        try {
            // Validar archivo
            validarArchivo(file);

            // Definir carpeta en Cloudinary basada en la categoría
            String folder = "demo-ia/" + categoria;

            // Subir a Cloudinary
            return cloudinaryService.uploadImage(file, folder);

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar imagen en Cloudinary: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarImagen(String imageUrl) {
        try {
            if (imageUrl != null && imageUrl.contains("cloudinary.com")) {
                String publicId = cloudinaryService.extractPublicIdFromUrl(imageUrl);
                if (publicId != null) {
                    cloudinaryService.deleteImage("demo-ia/" + publicId);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar imagen de Cloudinary: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean esImagenValida(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        if (file.getSize() > TAMAÑO_MAXIMO) {
            return false;
        }

        String contentType = file.getContentType();
        return contentType != null && TIPOS_IMAGEN_PERMITIDOS.contains(contentType.toLowerCase());
    }

    private void validarArchivo(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        if (file.getSize() > TAMAÑO_MAXIMO) {
            throw new IllegalArgumentException("El archivo es demasiado grande. Tamaño máximo: 5MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !TIPOS_IMAGEN_PERMITIDOS.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Tipo de archivo no permitido. Solo se permiten: " +
                String.join(", ", TIPOS_IMAGEN_PERMITIDOS));
        }
    }
}
