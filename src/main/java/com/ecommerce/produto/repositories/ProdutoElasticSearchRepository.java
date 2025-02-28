package com.ecommerce.produto.repositories;

import com.ecommerce.produto.models.ProdutoModelElasticSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface ProdutoElasticSearchRepository extends ElasticsearchRepository<ProdutoModelElasticSearch, UUID> {
}
