package com.ecommerce.produto.models;

import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.UUID;

@Document(indexName = "produtos")
public class ProdutoModelElasticSearch implements Serializable {

    private static final long serialVersionUID = 4L;

    @Id
    @Field(type = FieldType.Keyword)
    private UUID id;
    @Field(type = FieldType.Text)
    private String nome;
    @Field(type = FieldType.Text)
    private String marca;
    @Field(type = FieldType.Double)
    private Double preco;
    @Field(type = FieldType.Integer)
    private Integer quantidade;
    @Field(type = FieldType.Object)
    private Categoria categoria;
    @Field(type = FieldType.Text)
    private String descricao;

    public ProdutoModelElasticSearch() {
    }

    public ProdutoModelElasticSearch(UUID id, String nome, String marca, Double preco, Integer quantidade, Categoria categoria, String descricao) {
        this.id = id;
        this.nome = nome;
        this.marca = marca;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoria = categoria;
        this.descricao = descricao;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
