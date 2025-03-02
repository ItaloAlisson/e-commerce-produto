package com.ecommerce.produto.controllers;

import co.elastic.clients.elasticsearch.ml.Page;
import com.ecommerce.produto.dtos.ProdutoRecordDTO;
import com.ecommerce.produto.models.ProdutoModel;
import com.ecommerce.produto.models.ProdutoModelElasticSearch;
import com.ecommerce.produto.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }


    @PostMapping()
    public ResponseEntity<ProdutoModel> registrarProduto(@RequestBody @Valid ProdutoRecordDTO produtoDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(produtoService.registrarProduto(produtoDTO));
    }

    @GetMapping()
    public ResponseEntity<Iterable<ProdutoModelElasticSearch>> buscarProdutos(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(produtoService.buscarProdutos());
    }



}
