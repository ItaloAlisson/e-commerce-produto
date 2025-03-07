package com.ecommerce.produto.validation;

import com.ecommerce.produto.dtos.ProdutoRecordDTO;
import com.ecommerce.produto.exceptions.ConflictException;
import com.ecommerce.produto.repositories.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static com.ecommerce.produto.TestDataFactory.produtoDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoValidatorTest {

    @InjectMocks
    ProdutoValidator produtoValidator;

    @Mock
    ProdutoRepository produtoRepository;

    private ProdutoRecordDTO produtoDTO;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        produtoDTO = produtoDTO();
    }

    @DisplayName(" Deve validar o produto com nome inexistente")
    @Test
    void deveValidarProdutoNomeInexistente() {

        when(produtoRepository.existsByNome("Iphone 16")).thenReturn(false);

        produtoValidator.existePorNome("Iphone 16");

        verify(produtoRepository).existsByNome("Iphone 16");
    }

    @DisplayName(" Quando validar o produto com nome existente" +
            "então lançar ConflictException")
    @Test
    void quandoValidarProdutoNomeExistente_EntaoLancarConflictException() {

        when(produtoRepository.existsByNome("Moto G75")).thenReturn(true);

        var exception = assertThrows(ConflictException.class,
                () -> produtoValidator.existePorNome("Moto G75"));

        assertEquals("Produto Moto G75 já cadastrado!", exception.getMessage());
        verify(produtoRepository).existsByNome("Moto G75");
    }


}