package com.ecommerce.produto.services;

import com.ecommerce.produto.dtos.PrecoProdutoRecordDTO;
import com.ecommerce.produto.dtos.ProdutoRecordDTO;
import com.ecommerce.produto.dtos.QuantidadeProdutoRecordDTO;
import com.ecommerce.produto.exceptions.ResourceNotFoundException;
import com.ecommerce.produto.mappers.ProdutoMapper;
import com.ecommerce.produto.models.ProdutoModel;
import com.ecommerce.produto.models.ProdutoModelElasticSearch;
import com.ecommerce.produto.repositories.ProdutoElasticSearchRepository;
import com.ecommerce.produto.repositories.ProdutoRepository;
import com.ecommerce.produto.validation.ProdutoValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @CacheEvict(value = "produtos", allEntries = true)
    @Transactional
    public ProdutoModel registrarProduto(ProdutoRecordDTO produtoDTO) {
        produtoValidator.existePorNome(produtoDTO.nome());
        var novoProduto = produtoMapper.produtoDTOParaProdutoModel(produtoDTO);
        novoProduto = produtoRepository.save(novoProduto);
        var novoProdutoElastic = produtoMapper.produtoModelParaModelElasticSearch(novoProduto);
        novoProdutoElastic.setDataRegistro(novoProduto.getDataRegistro().toInstant(ZoneOffset.UTC));
        elasticSearchRepository.save(novoProdutoElastic);
        return novoProduto;
    }

    @Cacheable(value = "produtos")
    public Iterable<ProdutoModelElasticSearch> buscarProdutos() {
        return elasticSearchRepository.findAll();
    }

    @Cacheable(value = "produtos", key = "#nome")
    public ProdutoModelElasticSearch buscarProdutoPorNome(String nome) {
        return elasticSearchRepository.findByNome(nome)
                .orElseThrow(()->
                        new ResourceNotFoundException("Produto com o nome " + nome
                                + " não foi encontrado."));
    }

    @CacheEvict(value = "produtos", allEntries = true)
    @Transactional
    public ProdutoModel atualizarDadosProduto(UUID id,ProdutoRecordDTO produtoDTO) {
        var produto = produtoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Produto com o ID " + id
                        + " não foi encontrado."));

        BeanUtils.copyProperties(produtoDTO,produto,"id");
        var produtoElastic = produtoMapper.produtoModelParaModelElasticSearch(produto);
        produtoElastic.setDataRegistro(produto.getDataRegistro().toInstant(ZoneOffset.UTC));
        elasticSearchRepository.save(produtoElastic);
        return produtoRepository.save(produto);
    }

    @CacheEvict(value = "produtos", allEntries = true)
    @Transactional
    public void atualizarPrecoProduto(UUID id, PrecoProdutoRecordDTO precoProdutoDTO) {
        var produto = produtoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Produto com o ID " + id
                        + " não foi encontrado."));

        produto.setPreco(precoProdutoDTO.preco());
        var produtoElastic = produtoMapper.produtoModelParaModelElasticSearch(produto);
        produtoElastic.setDataRegistro(produto.getDataRegistro().toInstant(ZoneOffset.UTC));
        elasticSearchRepository.save(produtoElastic);
        produtoRepository.save(produto);
    }

    @CacheEvict(value = "produtos", allEntries = true)
    @Transactional
    public void atualizarQuantidadeProduto(UUID id, QuantidadeProdutoRecordDTO quantidadeProdutoDTO) {
        var produto = produtoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Produto com o ID " + id
                        + " não foi encontrado."));

        produto.setQuantidade(quantidadeProdutoDTO.quantidade());
        var produtoElastic = produtoMapper.produtoModelParaModelElasticSearch(produto);
        produtoElastic.setDataRegistro(produto.getDataRegistro().toInstant(ZoneOffset.UTC));
        elasticSearchRepository.save(produtoElastic);
        produtoRepository.save(produto);
    }

    @CacheEvict(value = "produtos", allEntries = true)
    @Transactional
    public void deletarProduto(UUID id) {
        var produto = produtoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Produto com o ID " + id
                        + " não foi encontrado."));

        var produtoElastic = produtoMapper.produtoModelParaModelElasticSearch(produto);
        elasticSearchRepository.delete(produtoElastic);
        produtoRepository.delete(produto);
    }
}
