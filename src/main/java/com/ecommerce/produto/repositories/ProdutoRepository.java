package com.ecommerce.produto.repositories;

import com.ecommerce.produto.models.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoModel, UUID> {
    boolean existsByNome(String nome);
}
