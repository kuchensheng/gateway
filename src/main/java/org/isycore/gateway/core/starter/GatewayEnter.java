package org.isycore.gateway.core.starter;

import org.isycore.gateway.core.handlers.GatewayServerHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.isycore.gateway"})
public class GatewayEnter {



    public static void main(String[] args) {
        SpringApplication.run(GatewayEnter.class);
    }

    @Bean
    public GatewayServerHandler gatewayServerHandler(){
        return new GatewayServerHandler();
    }

}
