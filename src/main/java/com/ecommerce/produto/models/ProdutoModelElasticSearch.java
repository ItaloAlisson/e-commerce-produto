package com.ecommerce.produto.models;

import com.ecommerce.produto.enums.CategoriaEnum;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "produtos")
public class ProdutoModelElasticSearch implements Serializable {

    private static final long serialVersionUID = 2L;

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
    @Field(type = FieldType.Keyword)
    private CategoriaEnum categoria;
    @Field(type = FieldType.Text)
    private String descricao;



}
