package com.ecommerce.produto.controllers;

import com.ecommerce.produto.dtos.ProdutoRecordDTO;
import com.ecommerce.produto.models.ProdutoModel;
import com.ecommerce.produto.models.ProdutoModelElasticSearch;
import com.ecommerce.produto.producers.RabbitMQProducer;
import com.ecommerce.produto.services.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.UUID;

import static com.ecommerce.produto.TestDataFactory.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ProdutoControllerTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private ProdutoService produtoService;

    @MockitoBean
    private RabbitMQProducer rabbitMQProducer;

    private ProdutoRecordDTO produtoDTO;
    private ProdutoRecordDTO produtoDTOAtualizado;
    private ProdutoModel produtoDB;
    private ProdutoModel produtoDBAtualizado;
    private Iterable<ProdutoModelElasticSearch> produtoElasticDB;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        produtoDTO = produtoDTO();
        produtoDTOAtualizado = produtoDTOAtualizado();
        produtoDB = produtoDB();
        produtoDBAtualizado = produtoDBAtualizado();
        produtoElasticDB = produtoElasticDB();
    }

    @DisplayName(" Quando registrar um produto, " +
            "então retornar produto registrado com http status 201")
    @Test
    void quandoRegistrarProduto_EntaoRetornarProdutoRegistradoComHttpStatus201() throws Exception {

        when(produtoService.registrarProduto(any(ProdutoRecordDTO.class)))
                .thenReturn(produtoDB);

        ResultActions resultado = mock.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(produtoDTO)));

        resultado.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("2c1bd9c6-ecd4-44e4-9f3d-fe54c7a56602"))
                .andExpect(jsonPath("$.nome").value("Moto G75"))
                .andExpect(jsonPath("$.marca").value("Motorola"))
                .andExpect(jsonPath("$.preco").value("1600"))
                .andExpect(jsonPath("$.quantidade").value("420"))
                .andExpect(jsonPath("$.categoria").value("TECNOLOGIA"))
                .andExpect(jsonPath("$.descricao").value("Primeiro moto g " +
                        "com ultrarresistência, O poder da IA e a câmera Sony - LYTIA 600, 5 anos de atualização de" +
                        " Android, Som Dolby Atmos e tela Full HD+ de 6,8” com superbrilho e Smart Water Touch ." +
                        " Velocidade e eficiência do novo Snapdragon."));
    }

    @DisplayName(" Quando buscar todos os produtos, " +
            "então retornar produtos com http status 200")
    @Test
    void quandoBuscarTodosProdutos_EntaoRetornarProdutosComHttpStatus200() throws Exception {

        when(produtoService.buscarProdutos())
                .thenReturn(produtoElasticDB);

        ResultActions resultado = mock.perform(get("/produtos")
                .contentType(MediaType.APPLICATION_JSON));

        resultado.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("2c1bd9c6-ecd4-44e4-9f3d-fe54c7a56602"))
                .andExpect(jsonPath("$[0].nome").value("Moto G75"))
                .andExpect(jsonPath("$[0].marca").value("Motorola"))
                .andExpect(jsonPath("$[0].preco").value("1600.0"))
                .andExpect(jsonPath("$[0].quantidade").value("420"))
                .andExpect(jsonPath("$[0].categoria").value("TECNOLOGIA"))
                .andExpect(jsonPath("$[0].descricao").value("Primeiro moto g com ultrarresistência, " +
                        "O poder da IA e a câmera Sony - LYTIA 600, 5 anos de atualização de Android, " +
                        "Som Dolby Atmos e tela Full HD+ de 6,8” com superbrilho e Smart Water Touch. " +
                        "Velocidade e eficiência do novo Snapdragon."));
    }

    @DisplayName(" Quando buscar um produto por nome, " +
            "então retornar produto com http status 200")
    @Test
    void quandoBuscarProdutoPorNome_EntaoRetornarProdutoComHttpStatus200() throws Exception {

        when(produtoService.buscarProdutoPorNome("Moto G75"))
                .thenReturn(produtoElasticDB.iterator().next());

        ResultActions resultado = mock.perform(get("/produtos/Moto G75")
                .contentType(MediaType.APPLICATION_JSON));

        resultado.andDo(print())
                .andExpect(jsonPath("$.id").value("2c1bd9c6-ecd4-44e4-9f3d-fe54c7a56602"))
                .andExpect(jsonPath("$.nome").value("Moto G75"))
                .andExpect(jsonPath("$.marca").value("Motorola"))
                .andExpect(jsonPath("$.preco").value("1600.0"))
                .andExpect(jsonPath("$.quantidade").value("420"))
                .andExpect(jsonPath("$.categoria").value("TECNOLOGIA"))
                .andExpect(jsonPath("$.descricao").value("Primeiro moto g com ultrarresistência, " +
                        "O poder da IA e a câmera Sony - LYTIA 600, 5 anos de atualização de Android, " +
                        "Som Dolby Atmos e tela Full HD+ de 6,8” com superbrilho e Smart Water Touch. " +
                        "Velocidade e eficiência do novo Snapdragon."));
    }

    @DisplayName(" Quando atualizar dados do produto, " +
            "então retornar produto atualizado com http status 200")
    @Test
    void quandoAtualizarDadosProduto_EntaoRetornarProdutoAtualizadoComHttpStatus200() throws Exception {

        when(produtoService.atualizarDadosProduto(any(UUID.class),
                any(ProdutoRecordDTO.class))).thenReturn(produtoDBAtualizado);

        ResultActions resultado = mock.perform(
                put("/produtos/2c1bd9c6-ecd4-44e4-9f3d-fe54c7a56602")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(produtoDTOAtualizado)));

        resultado.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2c1bd9c6-ecd4-44e4-9f3d-fe54c7a56602"))
                .andExpect(jsonPath("$.nome").value("Moto G75"))
                .andExpect(jsonPath("$.marca").value("Motorola"))
                .andExpect(jsonPath("$.preco").value("2000"))
                .andExpect(jsonPath("$.quantidade").value("320"))
                .andExpect(jsonPath("$.categoria").value("TECNOLOGIA"))
                .andExpect(jsonPath("$.descricao").value("Primeiro moto g " +
                        "com ultrarresistência, O poder da IA e a câmera Sony - LYTIA 600, 5 anos de atualização de" +
                        " Android, Som Dolby Atmos e tela Full HD+ de 6,8” com superbrilho e Smart Water Touch ." +
                        " Velocidade e eficiência do novo Snapdragon."));
    }





}