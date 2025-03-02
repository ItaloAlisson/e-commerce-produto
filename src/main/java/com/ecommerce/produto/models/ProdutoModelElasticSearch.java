package com.ecommerce.produto.models;

import com.ecommerce.produto.enums.CategoriaEnum;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Document(indexName = "produtos")
public class ProdutoModelElasticSearch {

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
    @Field(type = FieldType.Date)
    private Instant dataRegistro;


}
