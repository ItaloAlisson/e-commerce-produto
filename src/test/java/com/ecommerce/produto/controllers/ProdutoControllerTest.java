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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.ecommerce.produto.TestDataFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private ProdutoModel produtoDB;
    private ProdutoModelElasticSearch produtoElasticDB;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        produtoDTO = produtoDTO();
        produtoDB = produtoDB();
        produtoElasticDB = produtoElasticDB();
    }

    @DisplayName(" Quando registrar um produto, " +
            "então retornar produto registrado com http status 201")
    @Test
    void quandoRegistrarProduto_EntaoRetornarProdutoRegistradoComHttpStatus201() throws Exception {

        when(produtoService.registrarProduto(any(ProdutoRecordDTO.class))).thenReturn(produtoDB);

        ResultActions resultado = mock.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(produtoDTO)));

        resultado.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Moto G75"))
                .andExpect(jsonPath("$.marca").value("Motorola"))
                .andExpect(jsonPath("$.preco").value("1600"))
                .andExpect(jsonPath("$.quantidade").value("420"))
                .andExpect(jsonPath("$.categoria").value("TECNOLOGIA"))
                .andExpect(jsonPath("$.descricao").value("Primeiro moto g " +
                        "com ultrarrêsistencia, O poder da IA e a câmera Sony - LYTIA 600, 5 anos de atualização de" +
                        " Android, Som Dolby Atmos e tela Full HD+ de 6,8” com superbrilho e Smart Water Touch ." +
                        " Velocidade e eficiência do novo Snapdragon."));
    }
}