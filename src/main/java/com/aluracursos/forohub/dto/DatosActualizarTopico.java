package com.aluracursos.forohub.dto;

import jakarta.validation.constraints.NotNull;

public record DatosActualizarTopico(
        @NotNull
        Long id,
        String titulo,
        String mensaje,
        String status,
        Long autorId,
        Long cursoId
) {
}
