package com.ecommerce.produto.models;

import co.elastic.clients.util.DateTime;
import com.ecommerce.produto.enums.CategoriaEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity()
@Table(name = "PRODUTOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoModel implements Serializable {

    private static final long serialversionUID = 3L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String marca;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;
    @Column(nullable = false)
    private Integer quantidade;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaEnum categoria;
    @Column(columnDefinition = "TEXT")
    private String descricao;

    public ProdutoModel(String nome, String marca, BigDecimal preco, Integer quantidade, CategoriaEnum categoria, String descricao) {
        this.nome = nome;
        this.marca = marca;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoria = categoria;
        this.descricao = descricao;
    }
}
