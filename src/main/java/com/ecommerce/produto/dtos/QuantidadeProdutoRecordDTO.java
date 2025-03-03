package com.ecommerce.produto.dtos;

import jakarta.validation.constraints.NotNull;

public record QuantidadeProdutoRecordDTO(@NotNull(message ="Campo 'quantidade' não pode ser nulo!")
                                         Integer quantidade) {
}
