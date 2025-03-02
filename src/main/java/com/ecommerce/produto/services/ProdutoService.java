package com.ecommerce.produto.services;

import com.ecommerce.produto.dtos.ProdutoRecordDTO;
import com.ecommerce.produto.exceptions.ResourceNotFoundException;
import com.ecommerce.produto.mappers.ProdutoMapper;
import com.ecommerce.produto.models.ProdutoModel;
import com.ecommerce.produto.models.ProdutoModelElasticSearch;
import com.ecommerce.produto.repositories.ProdutoElasticSearchRepository;
import com.ecommerce.produto.repositories.ProdutoRepository;
import com.ecommerce.produto.validation.ProdutoValidator;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class ProdutoService {

    private final   ProdutoRepository produtoRepository;
    private final ProdutoElasticSearchRepository elasticSearchRepository;
    private final ProdutoValidator produtoValidator;
    private final ProdutoMapper produtoMapper;

    public ProdutoService(ProdutoRepository produtoRepository, ProdutoElasticSearchRepository elasticSearchRepository, ProdutoValidator produtoValidator, ProdutoMapper produtoMapper) {
        this.produtoRepository = produtoRepository;
        this.elasticSearchRepository = elasticSearchRepository;
        this.produtoValidator = produtoValidator;
        this.produtoMapper = produtoMapper;
    }


    public ProdutoModel registrarProduto(ProdutoRecordDTO produtoDTO) {
        produtoValidator.existePorNome(produtoDTO.nome());
        var novoProduto = produtoMapper.produtoDTOParaProdutoModel(produtoDTO);
        novoProduto = produtoRepository.save(novoProduto);
        var novoProdutoElastic = produtoMapper.produtoModelParaModelElasticSearch(novoProduto);
        novoProdutoElastic.setDataRegistro(novoProduto.getDataRegistro().toInstant(ZoneOffset.UTC));
        elasticSearchRepository.save(novoProdutoElastic);
        return novoProduto;
    }

    public Iterable<ProdutoModelElasticSearch> buscarProdutos() {
        return elasticSearchRepository.findAll();
    }

    public ProdutoModelElasticSearch buscarProdutoPorNome(String nome) {
        return elasticSearchRepository.findByNome(nome)
                .orElseThrow(()->
                        new ResourceNotFoundException("Produto com o nome " + nome
                                + " não foi encontrado."));
    }

    public ProdutoModel atualizarDadosProduto(UUID id, @Valid ProdutoRecordDTO produtoDTO) {
        var produto = produtoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Produto com o ID " + id
                        + " não foi encontrado."));

        BeanUtils.copyProperties(produtoDTO,produto,"id");
        return produtoRepository.save(produto);
    }
}
