package com.ecommerce.produto.dtos;

import com.ecommerce.produto.enums.CategoriaEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoRecordDTO(@NotBlank(message ="Campo 'nome' não pode estar vazio!")
                               String nome,
                               @NotBlank(message ="Campo 'marca' não pode estar vazio!")
                               String marca,
                               @NotNull(message ="Campo 'preco' não pode ser nulo!")
                               BigDecimal preco,
                               @NotNull(message ="Campo 'quantidade' não pode ser nulo!")
                               Integer quantidade,
                               @Enumerated(EnumType.STRING)
                               @NotNull(message ="Campo 'categoria' não pode ser nulo!")
                               CategoriaEnum categoria,
                               String descricao) {
}
