package com.jose.demoia.actriz.infrastructure.web;

import com.jose.demoia.actriz.domain.model.Escena;
import com.jose.demoia.actriz.domain.ports.in.EscenaUseCase;
import com.jose.demoia.actriz.domain.ports.out.FileStoragePort;
import com.jose.demoia.actriz.infrastructure.web.dto.EscenaRequestDto;
import com.jose.demoia.actriz.infrastructure.web.dto.EscenaResponseDto;
import com.jose.demoia.actriz.infrastructure.web.mapper.EscenaMapper;
import com.jose.demoia.actriz.application.EscenaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/escenas")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class EscenaController {

    private final EscenaUseCase escenaUseCase;
    private final EscenaService escenaService; // Acceso directo al servicio para funcionalidades específicas
    private final EscenaMapper escenaMapper;
    private final FileStoragePort fileStoragePort;
    private final ObjectMapper objectMapper;

    public EscenaController(EscenaUseCase escenaUseCase, EscenaService escenaService,
                           EscenaMapper escenaMapper, FileStoragePort fileStoragePort,
                           ObjectMapper objectMapper) {
        this.escenaUseCase = escenaUseCase;
        this.escenaService = escenaService;
        this.escenaMapper = escenaMapper;
        this.fileStoragePort = fileStoragePort;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<EscenaResponseDto> crearEscena(@Valid @RequestBody EscenaRequestDto requestDto) {
        try {
            Escena escena = escenaMapper.toEntity(requestDto);

            // Preparar información de actrices si se proporcionaron
            List<EscenaService.ActrizEscenaInfo> actricesInfo = null;
            if (requestDto.getActrices() != null && !requestDto.getActrices().isEmpty()) {
                actricesInfo = requestDto.getActrices().stream()
                    .map(dto -> new EscenaService.ActrizEscenaInfo(dto.getActrizId(), dto.getPapel()))
                    .collect(Collectors.toList());
            }

            // Crear escena con actrices asociadas
            Escena nuevaEscena;
            if (actricesInfo != null && !actricesInfo.isEmpty()) {
                nuevaEscena = escenaService.crearEscenaConActrices(escena, actricesInfo);
            } else {
                nuevaEscena = escenaUseCase.crearEscena(escena);
            }

            EscenaResponseDto responseDto = escenaMapper.toResponseDto(nuevaEscena);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/con-imagen", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EscenaResponseDto> crearEscenaConImagen(
            @RequestPart("escena") String escenaJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        try {
            System.out.println("Recibiendo solicitud con escenaJson: " + escenaJson);

            EscenaRequestDto requestDto = objectMapper.readValue(escenaJson, EscenaRequestDto.class);
            System.out.println("DTO parseado correctamente: " + requestDto);

            // Si hay una imagen, guardarla y asignar la URL
            if (imagen != null && !imagen.isEmpty()) {
                System.out.println("Guardando imagen: " + imagen.getOriginalFilename());
                String imagenUrl = fileStoragePort.guardarImagen(imagen, "escenas");
                requestDto.setImagenUrl(imagenUrl);
                System.out.println("Imagen guardada en: " + imagenUrl);
            }

            Escena escena = escenaMapper.toEntity(requestDto);
            System.out.println("Entity creada: " + escena);

            // Preparar información de actrices si se proporcionaron
            List<EscenaService.ActrizEscenaInfo> actricesInfo = null;
            if (requestDto.getActrices() != null && !requestDto.getActrices().isEmpty()) {
                actricesInfo = requestDto.getActrices().stream()
                    .map(dto -> new EscenaService.ActrizEscenaInfo(dto.getActrizId(), dto.getPapel()))
                    .collect(Collectors.toList());
                System.out.println("Actrices info preparada: " + actricesInfo.size() + " actrices");
            }

            // Crear escena con actrices asociadas
            Escena nuevaEscena;
            if (actricesInfo != null && !actricesInfo.isEmpty()) {
                System.out.println("Creando escena con actrices");
                nuevaEscena = escenaService.crearEscenaConActrices(escena, actricesInfo);
            } else {
                System.out.println("Creando escena sin actrices");
                nuevaEscena = escenaUseCase.crearEscena(escena);
            }

            System.out.println("Escena creada con ID: " + nuevaEscena.getId());
            EscenaResponseDto responseDto = escenaMapper.toResponseDto(nuevaEscena);
            System.out.println("Response DTO creado");

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

        } catch (Exception e) {
            System.err.println("Error al crear escena con imagen: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EscenaResponseDto> obtenerEscenaPorId(@PathVariable Long id) {
        Optional<Escena> escena = escenaUseCase.obtenerEscenaPorId(id);
        return escena.map(escenaMapper::toResponseDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<EscenaResponseDto>> obtenerTodasLasEscenas() {
        List<Escena> escenas = escenaUseCase.obtenerTodasLasEscenas();
        List<EscenaResponseDto> responseDtos = escenas.stream()
                .map(escenaMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/actriz/{actrizId}")
    public ResponseEntity<List<EscenaResponseDto>> obtenerEscenasPorActriz(@PathVariable Long actrizId) {
        List<Escena> escenas = escenaService.obtenerEscenasPorActriz(actrizId);
        List<EscenaResponseDto> responseDtos = escenas.stream()
                .map(escenaMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EscenaResponseDto> actualizarEscena(@PathVariable Long id,
                                                            @Valid @RequestBody EscenaRequestDto requestDto) {
        Optional<Escena> escenaExistente = escenaUseCase.obtenerEscenaPorId(id);
        if (escenaExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Escena escena = escenaExistente.get();
        escenaMapper.updateEntityFromDto(requestDto, escena);
        Escena escenaActualizada = escenaUseCase.crearEscena(escena);
        EscenaResponseDto responseDto = escenaMapper.toResponseDto(escenaActualizada);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEscena(@PathVariable Long id) {
        Optional<Escena> escena = escenaUseCase.obtenerEscenaPorId(id);
        if (escena.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Eliminar imagen asociada si existe
        if (escena.get().getImagenUrl() != null) {
            fileStoragePort.eliminarImagen(escena.get().getImagenUrl());
        }

        escenaUseCase.eliminarEscena(id);
        return ResponseEntity.noContent().build();
    }
}
