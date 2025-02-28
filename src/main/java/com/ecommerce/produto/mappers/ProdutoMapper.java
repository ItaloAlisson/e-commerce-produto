package com.ecommerce.produto.mappers;

import com.ecommerce.produto.dtos.ProdutoRecordDTO;
import com.ecommerce.produto.models.ProdutoModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    ProdutoModel produtoDTOParaModel(ProdutoRecordDTO produtoDTO);
}
