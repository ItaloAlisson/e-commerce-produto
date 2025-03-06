package com.ecommerce.produto.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private static final String NOME_EXCHANGE = "amq.direct";

    @Value("${spring.rabbitmq.queue}")
    private String fila;

    @Bean
    public Queue FilaProdutos(){
        return new Queue(fila,true,false,false);
    }

    @Bean
    public DirectExchange trocaDireta(){
        return new DirectExchange(NOME_EXCHANGE);
    }

    @Bean
    public Binding vincularFilaProduto(Queue filaProdutos,DirectExchange trocaDireta){
        return BindingBuilder.bind(filaProdutos).to(trocaDireta).with(fila);
    }

}
