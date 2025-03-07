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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

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
    private ProdutoModelElasticSearch produtoElasticParaPersistenciaAtualizado;

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
        produtoElasticParaPersistenciaAtualizado = produtoElasticParaPersistenciaAtualizado();
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
        verify(elasticSearchRepository).save(produtoElasticParaPersistenciaAtualizado);
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

    @DisplayName(" Quando atualizar dados do produto" +
            "então retornar o produto atualizado")
    @Test
    void quandoAtualizarDadosProduto_EntaoRetornarProdutoAtualizado() {

        when(produtoRepository.findById(UUID.fromString("2c1bd9c6-ecd4-44e4-9f3d-fe54c7a56602")))
                .thenReturn(Optional.ofNullable(produtoDB));
        when(mapper.produtoModelParaModelElasticSearch(any(ProdutoModel.class)))
                .thenReturn(produtoElasticParaPersistenciaAtualizado);
        when(produtoRepository.save(any(ProdutoModel.class))).thenReturn(produtoDBAtualizado);
        when(elasticSearchRepository.save(any(ProdutoModelElasticSearch.class))).thenReturn(
                (produtoElasticParaPersistenciaAtualizado));

        var resultado = produtoService.atualizarDadosProduto(UUID.fromString(
                "2c1bd9c6-ecd4-44e4-9f3d-fe54c7a56602"),produtoDTOAtualizado);

        assertNotNull(resultado);
        verify(produtoRepository).findById(UUID.fromString("2c1bd9c6-ecd4-44e4-9f3d-fe54c7a56602"));
        ArgumentCaptor<ProdutoModel> captor = ArgumentCaptor.forClass(ProdutoModel.class);
        verify(produtoRepository).save(captor.capture());
        ArgumentCaptor<ProdutoModelElasticSearch> captorElastic =
                ArgumentCaptor.forClass(ProdutoModelElasticSearch.class);
        verify(elasticSearchRepository).save(captorElastic.capture());
    }

    @DisplayName("Quando atualizar dados do produto inexistente" +
            "            então lançar ResourceNotFoundException")
    @Test
    void quandoAtualizarDadosProdutoInexistente_EntaoLancarResourceNotFoundException() {

        when(produtoRepository.findById(UUID.fromString("3ab01e6f-da08-4c88-93eb-73ccd94509a7")))
                .thenReturn(Optional.empty());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> produtoService.atualizarDadosProduto(UUID.fromString(
                        "3ab01e6f-da08-4c88-93eb-73ccd94509a7"),produtoDTOAtualizado));

        assertEquals("Produto com o ID " +
                "3ab01e6f-da08-4c88-93eb-73ccd94509a7" +
                " não foi encontrado.", exception.getMessage());
        verify(produtoRepository).findById(UUID.fromString("3ab01e6f-da08-4c88-93eb-73ccd94509a7"));
    }

    @DisplayName(" Deve deletar o produto")
    @Test
    void deveDeletarProduto() {

        when(produtoRepository.findById(UUID.fromString("2c1bd9c6-ecd4-44e4-9f3d-fe54c7a56602")))
                .thenReturn(Optional.ofNullable(produtoDB));
        when(elasticSearchRepository.findById(UUID.fromString("2c1bd9c6-ecd4-44e4-9f3d-fe54c7a56602")))
                .thenReturn(Optional.ofNullable(produtoElasticDB.iterator().next()));

        produtoService.deletarProduto(UUID.fromString(
                "2c1bd9c6-ecd4-44e4-9f3d-fe54c7a56602"));


        verify(produtoRepository).findById(UUID.fromString("2c1bd9c6-ecd4-44e4-9f3d-fe54c7a56602"));
        verify(produtoRepository).delete(produtoDB);
        verify(elasticSearchRepository).findById(UUID.fromString("2c1bd9c6-ecd4-44e4-9f3d-fe54c7a56602"));
        verify(elasticSearchRepository).delete(produtoElasticDB.iterator().next());
    }

    @DisplayName("Quando deletar o produto inexistente" +
            "            então lançar ResourceNotFoundException")
    @Test
    void quandoDeletarProdutoInexistente_EntaoLancarResourceNotFoundException() {

        when(produtoRepository.findById(UUID.fromString("8fc8bb46-2cca-4423-a240-26cf7b373ab5")))
                .thenReturn(Optional.empty());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> produtoService.deletarProduto(UUID.fromString(
                        "8fc8bb46-2cca-4423-a240-26cf7b373ab5")));

        assertEquals("Produto com o ID " +
                "8fc8bb46-2cca-4423-a240-26cf7b373ab5" +
                " não foi encontrado.", exception.getMessage());
        verify(produtoRepository).findById(UUID.fromString("8fc8bb46-2cca-4423-a240-26cf7b373ab5"));
    }



}