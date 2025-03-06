package com.ecommerce.produto.services;

import com.ecommerce.produto.dtos.ProdutoRecordDTO;
import com.ecommerce.produto.exceptions.ConflictException;
import com.ecommerce.produto.exceptions.ResourceNotFoundException;
import com.ecommerce.produto.mappers.ProdutoMapper;
import com.ecommerce.produto.models.ProdutoModel;
import com.ecommerce.produto.models.ProdutoModelElasticSearch;
import com.ecommerce.produto.repositories.ProdutoElasticSearchRepository;
import com.ecommerce.produto.repositories.ProdutoRepository;
import com.ecommerce.produto.validation.ProdutoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.ecommerce.produto.TestDataFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @InjectMocks
    ProdutoService produtoService;
    @Mock
    ProdutoRepository produtoRepository;
    @Mock
    ProdutoElasticSearchRepository elasticSearchRepository;
    @Mock
    ProdutoValidator validator;
    @Mock
    ProdutoMapper mapper;

    private ProdutoRecordDTO produtoDTO;
    private ProdutoRecordDTO produtoDTOAtualizado;
    private ProdutoModel produtoParaPersistencia;
    private ProdutoModel produtoDB;
    private ProdutoModel produtoDBAtualizado;
    private Iterable<ProdutoModelElasticSearch> produtoElasticDB;
    private ProdutoModelElasticSearch produtoElasticParaPersistencia;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        produtoDTO = produtoDTO();
        produtoDTOAtualizado = produtoDTOAtualizado();
        produtoParaPersistencia = produtoParaPersistencia();
        produtoDB = produtoDB();
        produtoDBAtualizado = produtoDBAtualizado();
        produtoElasticDB = produtoElasticDB();
        produtoElasticParaPersistencia= produtoElasticParaPersistencia();
    }

    @DisplayName(" Quando registrar o produto" +
            "então retornar o produto registrado")
    @Test
    void quandoRegistrarProduto_EntaoRetornarProdutoRegistrado() {

        doNothing().when(validator).existePorNome(produtoDTO.nome());
        when(mapper.produtoDTOParaProdutoModel(produtoDTO)).thenReturn(produtoParaPersistencia);
        when(mapper.produtoModelParaModelElasticSearch(produtoDB))
                .thenReturn(produtoElasticParaPersistencia);
        when(produtoRepository.save(produtoParaPersistencia)).thenReturn(produtoDB);
        when(elasticSearchRepository.save(produtoElasticParaPersistencia)).thenReturn(
                (produtoElasticDB.iterator().next()));

        var resultado = produtoService.registrarProduto(produtoDTO);

        assertNotNull(resultado);
        verify(validator).existePorNome(produtoDTO.nome());
        verify(produtoRepository).save(produtoParaPersistencia);
    }

    @DisplayName(" Quando registrar o produto existente" +
            "então lançar ConflictException")
    @Test
    void quandoRegistrarProdutoExistente_EntaoLancarConflictException() {

        doThrow(new ConflictException("Produto " +
                produtoDTO.nome() + " já cadastrado!"))
                .when(validator).existePorNome(produtoDTO.nome());

        var exception = assertThrows(ConflictException.class,
                () -> produtoService.registrarProduto(produtoDTO));

        assertEquals("Produto " +
                produtoDTO.nome()
                + " já cadastrado!", exception.getMessage());
        verify(validator).existePorNome(produtoDTO.nome());
    }

    @DisplayName("Quando buscar todos os produtos " +
            "            então retornar produtos")
    @Test
    void quandoBuscarTodosProdutos_EntaoRetornarProdutos() {

        when(elasticSearchRepository.findAll()).thenReturn(produtoElasticDB);

        var resultado = produtoService.buscarProdutos();

        assertNotNull(resultado);
        verify(elasticSearchRepository).findAll();
    }

    @DisplayName("Quando buscar um produto por nome" +
            "            então retornar produto")
    @Test
    void quandoBuscarProdutoPorNome_EntaoRetornarProduto() {

        when(elasticSearchRepository.findByNome("Moto G75")).thenReturn(Optional.ofNullable(
                produtoElasticDB.iterator().next()));

        var resultado = produtoService.buscarProdutoPorNome("Moto G75");

        assertNotNull(resultado);
        assertEquals("Moto G75", resultado.getNome());
        verify(elasticSearchRepository).findByNome("Moto G75");
    }

    @DisplayName("Quando buscar um produto por nome inexistente" +
            "            então lançar ResourceNotFoundException")
    @Test
    void quandoBuscarProdutoPorNomeInexistente_EntaoLancarResourceNotFoundException() {

        when(elasticSearchRepository.findByNome("Iphone 16")).thenReturn(Optional.empty());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> produtoService.buscarProdutoPorNome("Iphone 16"));

        assertEquals("Produto " +
                "Iphone 16" +
                " não foi encontrado.", exception.getMessage());
        verify(elasticSearchRepository).findByNome("Iphone 16");
    }



}