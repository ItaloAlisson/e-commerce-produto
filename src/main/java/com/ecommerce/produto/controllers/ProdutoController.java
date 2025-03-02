package com.ecommerce.produto.controllers;

import com.ecommerce.produto.dtos.PrecoRecordDTO;
import com.ecommerce.produto.dtos.ProdutoRecordDTO;
import com.ecommerce.produto.models.ProdutoModel;
import com.ecommerce.produto.models.ProdutoModelElasticSearch;
import com.ecommerce.produto.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @GetMapping("/{nome}")
    public ResponseEntity<ProdutoModelElasticSearch> buscarProdutoPorNome(@PathVariable(value = "nome") String nome){
        return ResponseEntity.status(HttpStatus.OK)
                .body(produtoService.buscarProdutoPorNome(nome));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoModel> atualizarDadosProduto(@PathVariable(value = "id") UUID id,
                                                      @RequestBody @Valid ProdutoRecordDTO produtoDTO){
        return ResponseEntity.status(HttpStatus.OK)
                .body(produtoService.atualizarDadosProduto(id,produtoDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> atualizarPrecoProduto(@PathVariable(value = "id") UUID id,
                                                      @RequestBody @Valid PrecoRecordDTO produtoprecoDTO){
        produtoService.atualizarPrecoProduto(id,produtoprecoDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
