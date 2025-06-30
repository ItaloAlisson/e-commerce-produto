package com.ecommerce.produto.dtos;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRecordDTO(@NotBlank(message ="Campo 'tipo' não pode estar vazio!")
        String tipo) {

}
