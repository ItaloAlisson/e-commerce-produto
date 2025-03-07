package com.ecommerce.produto.repositories;

import com.ecommerce.produto.models.ProdutoModelElasticSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface ProdutoElasticSearchRepository extends ElasticsearchRepository<ProdutoModelElasticSearch, UUID> {
    Optional<ProdutoModelElasticSearch> findByNome(String nome);
}
