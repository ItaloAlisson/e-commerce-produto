package com.ecommerce.produto.services;

import com.ecommerce.produto.controllers.ProdutoController;
import com.ecommerce.produto.repositories.ProdutoElasticSearchRepository;
import com.ecommerce.produto.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoElasticSearchRepository elasticSearchRepository;

    public ProdutoService(ProdutoRepository produtoRepository, ProdutoElasticSearchRepository elasticSearchRepository) {
        this.produtoRepository = produtoRepository;
        this.elasticSearchRepository = elasticSearchRepository;
    }
}
