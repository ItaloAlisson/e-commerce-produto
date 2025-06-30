package com.ecommerce.produto.repositories;

import com.ecommerce.produto.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria,Integer> {
    boolean existsByTipo(String tipo);
}
