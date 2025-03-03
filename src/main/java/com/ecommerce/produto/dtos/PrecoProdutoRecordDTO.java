package com.ecommerce.produto.dtos;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PrecoProdutoRecordDTO(@NotNull(message ="Campo 'preco' não pode ser nulo!")
                             BigDecimal preco) {
}
