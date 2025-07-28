package com.jose.demoia.actriz.infrastructure.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
@Tag(name = "Archivos", description = "API para servir archivos estáticos (imágenes)")
public class FileController {

    @Value("${app.file.upload-dir:uploads}")
    private String uploadDir;

    @GetMapping("/{categoria}/{nombreArchivo:.+}")
    @Operation(summary = "Servir archivo", description = "Sirve archivos estáticos como imágenes")
    public ResponseEntity<Resource> servirArchivo(
            @PathVariable String categoria,
            @PathVariable String nombreArchivo) {

        try {
            Path rutaArchivo = Paths.get(uploadDir).resolve(categoria).resolve(nombreArchivo);
            Resource resource = new UrlResource(rutaArchivo.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = determinarTipoContenido(nombreArchivo);

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nombreArchivo + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String determinarTipoContenido(String nombreArchivo) {
        String extension = nombreArchivo.toLowerCase();
        if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (extension.endsWith(".png")) {
            return "image/png";
        } else if (extension.endsWith(".gif")) {
            return "image/gif";
        } else if (extension.endsWith(".webp")) {
            return "image/webp";
        }
        return "application/octet-stream";
    }
}
