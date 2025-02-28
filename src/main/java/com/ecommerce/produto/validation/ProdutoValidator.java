package com.ecommerce.produto.validation;

import com.ecommerce.produto.exceptions.ConflictException;
import com.ecommerce.produto.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class ProdutoValidator {

    private final ProdutoRepository produtoRepository;

    public ProdutoValidator(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public void existePorNome(String nome){
        if (produtoRepository.existsByNome(nome)){
            throw new ConflictException("Produto" + nome
                    + " já cadastrado!");
        }
    }
}
