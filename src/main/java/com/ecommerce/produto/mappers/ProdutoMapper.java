package com.ecommerce.produto.mappers;

import com.ecommerce.produto.dtos.ProdutoRecordDTO;
import com.ecommerce.produto.models.ProdutoModel;
import com.ecommerce.produto.models.ProdutoModelElasticSearch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    ProdutoModel produtoDTOParaProdutoModel(ProdutoRecordDTO produtoDTO);
    @Mapping(target = "dataRegistro", ignore = true)
    ProdutoModelElasticSearch produtoModelParaModelElasticSearch(ProdutoModel produtoModel);
}
