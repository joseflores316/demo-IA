package com.jose.demoia.actriz.infrastructure.storage;

import com.jose.demoia.actriz.domain.ports.out.FileStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Primary // Esta anotación hace que Spring use esta implementación en lugar de CloudinaryFileStorageAdapter
public class LocalVolumeFileStorageAdapter implements FileStoragePort {

    @Value("${app.upload.dir:/app/uploads}")
    private String uploadDir;

    @Value("${app.base.url:http://localhost:8080}")
    private String baseUrl;

    private static final List<String> TIPOS_IMAGEN_PERMITIDOS = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    private static final long TAMAÑO_MAXIMO = 5 * 1024 * 1024; // 5MB

    @Override
    public String guardarImagen(MultipartFile file, String categoria) {
        try {
            // Validar archivo
            validarArchivo(file);

            // Crear directorio si no existe
            Path categoriaPath = Paths.get(uploadDir, categoria);
            Files.createDirectories(categoriaPath);

            // Generar nombre único para el archivo
            String extension = getFileExtension(file.getOriginalFilename());
            String nombreArchivo = UUID.randomUUID().toString() + extension;

            // Ruta completa del archivo
            Path rutaArchivo = categoriaPath.resolve(nombreArchivo);

            // Guardar el archivo
            Files.copy(file.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

            // Retornar URL accesible
            return baseUrl + "/api/files/" + categoria + "/" + nombreArchivo;

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar imagen en volumen local: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarImagen(String imageUrl) {
        try {
            if (imageUrl != null && imageUrl.contains(baseUrl)) {
                // Extraer la ruta relativa de la URL
                String relativePath = imageUrl.replace(baseUrl + "/api/files/", "");
                Path rutaArchivo = Paths.get(uploadDir, relativePath);

                if (Files.exists(rutaArchivo)) {
                    Files.delete(rutaArchivo);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar imagen del volumen local: " + e.getMessage(), e);
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
        if (!esImagenValida(file)) {
            throw new IllegalArgumentException("Archivo no válido. Debe ser una imagen menor a 5MB.");
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return ".jpg";
        }

        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return ".jpg";
        }

        return filename.substring(lastDotIndex);
    }
}
