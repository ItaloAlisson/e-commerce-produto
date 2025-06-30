package com.ecommerce.produto.controllers;

import com.ecommerce.produto.dtos.CategoriaRecordDTO;
import com.ecommerce.produto.dtos.ProdutoRecordDTO;
import com.ecommerce.produto.models.Categoria;
import com.ecommerce.produto.models.ProdutoModel;
import com.ecommerce.produto.models.ProdutoModelElasticSearch;
import com.ecommerce.produto.producers.RabbitMQProducer;
import com.ecommerce.produto.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoController {


    private  ProdutoService produtoService;
    private final  RabbitMQProducer rabbitMQProducer;

    public ProdutoController(ProdutoService produtoService, RabbitMQProducer rabbitMQProducer) {
        this.produtoService = produtoService;
        this.rabbitMQProducer = rabbitMQProducer;
    }

    @PostMapping("/categorias")
    public ResponseEntity<Categoria> registrarCategoria(@RequestBody @Valid CategoriaRecordDTO categoriaDTO){
        var novaCategoria = produtoService.registrarCategoria(categoriaDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(novaCategoria);
    }


    @PostMapping()
    public ResponseEntity<ProdutoModel> registrarProduto(@RequestBody @Valid ProdutoRecordDTO produtoDTO){
        var novoProduto = produtoService.registrarProduto(produtoDTO);
        rabbitMQProducer.enviarProduto(novoProduto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(novoProduto);
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
        var produtoAtualizado = produtoService.atualizarDadosProduto(id,produtoDTO);
        rabbitMQProducer.enviarProduto(produtoAtualizado);
        return ResponseEntity.status(HttpStatus.OK)
                .body(produtoAtualizado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable(value = "id") UUID id) {
        produtoService.deletarProduto(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
