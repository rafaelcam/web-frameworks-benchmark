package com.rafaelcam.springbootwebflux.webservice

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class DeveloperResource {

    @Bean
    fun developerRoute(handler: DeveloperResourceHandler) = coRouter {

        accept(MediaType.APPLICATION_JSON).nest {
            "/api/v1/developers".nest {
                GET("", handler::getAll)
                GET("/{id}", handler::getById)
                POST("", handler::create)
                DELETE("/{id}", handler::remove)
            }
        }
    }
}