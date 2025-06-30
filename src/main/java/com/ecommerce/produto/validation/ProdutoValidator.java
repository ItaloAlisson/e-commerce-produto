package com.ecommerce.produto.validation;

import com.ecommerce.produto.exceptions.ConflictException;
import com.ecommerce.produto.repositories.CategoriaRepository;
import com.ecommerce.produto.repositories.ProdutoRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

@Service
public class ProdutoValidator {

    private ProdutoRepository produtoRepository;
    private CategoriaRepository categoriaRepository;

    public ProdutoValidator(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public void existePorNome(String nome){
        if (produtoRepository.existsByNome(nome)){
            throw new ConflictException("Produto " + nome
                    + " já cadastrado!");
        }
    }

    public void existePorTipo(String tipo) {
        if (categoriaRepository.existsByTipo(tipo)){
            throw new ConflictException("Tipo " + tipo
                    + " já cadastrado!");
        }

    }
}
