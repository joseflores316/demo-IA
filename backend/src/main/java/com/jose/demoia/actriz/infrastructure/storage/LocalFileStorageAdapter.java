package com.jose.demoia.actriz.infrastructure.storage;

import com.jose.demoia.actriz.domain.ports.out.FileStoragePort;
import org.springframework.beans.factory.annotation.Value;
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
public class LocalFileStorageAdapter implements FileStoragePort {

    @Value("${app.file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${app.file.base-url:http://localhost:8080}")
    private String baseUrl;

    private static final List<String> TIPOS_IMAGEN_PERMITIDOS = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    private static final long TAMAÑO_MAXIMO = 5 * 1024 * 1024; // 5MB

    @Override
    public String guardarImagen(MultipartFile file, String categoria) {
        if (!esImagenValida(file)) {
            throw new IllegalArgumentException("El archivo no es una imagen válida");
        }

        try {
            // Crear directorio si no existe
            Path directorioCategoria = Paths.get(uploadDir, categoria);
            Files.createDirectories(directorioCategoria);

            // Generar nombre único para el archivo
            String extension = obtenerExtension(file.getOriginalFilename());
            String nombreArchivo = UUID.randomUUID().toString() + "." + extension;
            Path rutaArchivo = directorioCategoria.resolve(nombreArchivo);

            // Guardar archivo
            Files.copy(file.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

            // Retornar URL accesible
            return baseUrl + "/api/files/" + categoria + "/" + nombreArchivo;

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarImagen(String imageUrl) {
        if (imageUrl == null || !imageUrl.startsWith(baseUrl)) {
            return;
        }

        try {
            // Extraer la ruta relativa de la URL
            String rutaRelativa = imageUrl.substring(baseUrl.length() + "/api/files/".length());
            Path rutaArchivo = Paths.get(uploadDir, rutaRelativa);

            if (Files.exists(rutaArchivo)) {
                Files.delete(rutaArchivo);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar el archivo: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean esImagenValida(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // Validar tipo de contenido
        if (!TIPOS_IMAGEN_PERMITIDOS.contains(file.getContentType())) {
            return false;
        }

        // Validar tamaño
        if (file.getSize() > TAMAÑO_MAXIMO) {
            return false;
        }

        return true;
    }

    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            return "jpg";
        }
        return nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1).toLowerCase();
    }
}
