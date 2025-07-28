package com.jose.demoia.actriz.domain.ports.out;

import org.springframework.web.multipart.MultipartFile;

public interface FileStoragePort {

    /**
     * Guarda un archivo de imagen y retorna la URL donde se puede acceder
     * @param file el archivo a guardar
     * @param categoria la categoría del archivo (actriz, escena, etc.)
     * @return la URL del archivo guardado
     */
    String guardarImagen(MultipartFile file, String categoria);

    /**
     * Elimina un archivo por su URL
     * @param imageUrl la URL del archivo a eliminar
     */
    void eliminarImagen(String imageUrl);

    /**
     * Valida si el archivo es una imagen válida
     * @param file el archivo a validar
     * @return true si es una imagen válida, false en caso contrario
     */
    boolean esImagenValida(MultipartFile file);
}
