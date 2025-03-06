package com.ecommerce.produto;

import com.ecommerce.produto.dtos.ProdutoRecordDTO;
import com.ecommerce.produto.enums.CategoriaEnum;
import com.ecommerce.produto.models.ProdutoModel;
import com.ecommerce.produto.models.ProdutoModelElasticSearch;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

public class TestDataFactory {

    public static ProdutoRecordDTO produtoDTO(){
        return new ProdutoRecordDTO("Moto G75","Motorola", BigDecimal.valueOf(1600), 420,
                CategoriaEnum.TECNOLOGIA,"Primeiro moto g com ultrarresistência," +
                " O poder da IA e a câmera Sony - LYTIA 600, 5 anos de atualização de Android, Som Dolby Atmos e tela "+
                "Full HD+ de 6,8” com superbrilho e Smart Water Touch . Velocidade e eficiência do novo Snapdragon.");
    }

    public static ProdutoRecordDTO produtoDTOAtualizado(){
        return new ProdutoRecordDTO("Moto G75","Motorola", BigDecimal.valueOf(200), 320,
                CategoriaEnum.TECNOLOGIA,"Primeiro moto g com ultrarresistência," +
                " O poder da IA e a câmera Sony - LYTIA 600, 5 anos de atualização de Android, Som Dolby Atmos e tela "+
                "Full HD+ de 6,8” com superbrilho e Smart Water Touch . Velocidade e eficiência do novo Snapdragon.");
    }


    public static ProdutoModel produtoParaPersistencia(){
        return new ProdutoModel("Moto G75","Motorola",BigDecimal.valueOf(1600),420,
                CategoriaEnum.TECNOLOGIA,"Primeiro moto g com ultrarresistência," +
                " O poder da IA e a câmera Sony - LYTIA 600, 5 anos de atualização de Android, Som Dolby Atmos e tela "+
                "Full HD+ de 6,8” com superbrilho e Smart Water Touch . Velocidade e eficiência do novo Snapdragon.");

    }
    public static ProdutoModelElasticSearch produtoElasticParaPersistencia(){
        return new ProdutoModelElasticSearch(UUID.fromString("2c1bd9c6-ecd4-44e4-9f3d-fe54c7a56602")
                ,"Moto G75","Motorola",1600.00,420,
                CategoriaEnum.TECNOLOGIA,"Primeiro moto g com ultrarresistência," +
                " O poder da IA e a câmera Sony - LYTIA 600, 5 anos de atualização de Android, Som Dolby Atmos e tela "+
                "Full HD+ de 6,8” com superbrilho e Smart Water Touch . Velocidade e eficiência do novo Snapdragon.");

    }


    public static ProdutoModel produtoDB(){
        return new ProdutoModel(UUID.fromString("2c1bd9c6-ecd4-44e4-9f3d-fe54c7a56602"),"Moto G75",
                "Motorola",BigDecimal.valueOf(1600),420,CategoriaEnum.TECNOLOGIA,
                "Primeiro moto g com ultrarresistência," +
                        " O poder da IA e a câmera Sony - LYTIA 600, 5 anos de atualização de Android, Som Dolby Atmos e tela "+
                        "Full HD+ de 6,8” com superbrilho e Smart Water Touch . Velocidade e eficiência do novo Snapdragon.");
    }
    public static ProdutoModel produtoDBAtualizado(){
        return new ProdutoModel(UUID.fromString("2c1bd9c6-ecd4-44e4-9f3d-fe54c7a56602"),"Moto G75",
                "Motorola",BigDecimal.valueOf(2000),320,CategoriaEnum.TECNOLOGIA,
                "Primeiro moto g com ultrarresistência," +
                        " O poder da IA e a câmera Sony - LYTIA 600, 5 anos de atualização de Android, Som Dolby Atmos e tela "+
                        "Full HD+ de 6,8” com superbrilho e Smart Water Touch . Velocidade e eficiência do novo Snapdragon.");
    }

    public static Iterable<ProdutoModelElasticSearch> produtoElasticDB(){
        return Arrays.asList(
                new ProdutoModelElasticSearch(
                        UUID.fromString("2c1bd9c6-ecd4-44e4-9f3d-fe54c7a56602"),
                        "Moto G75",
                        "Motorola",
                        1600.00,
                        420,
                        CategoriaEnum.TECNOLOGIA,
                        "Primeiro moto g com ultrarresistência, " +
                                "O poder da IA e a câmera Sony - LYTIA 600, 5 anos de atualização de Android, " +
                                "Som Dolby Atmos e tela Full HD+ de 6,8” com superbrilho e Smart Water Touch. " +
                                "Velocidade e eficiência do novo Snapdragon."
                )
        );
    }

}
