package io.realworld

import io.realworld.jwt.ExposeResponseInterceptor
import io.realworld.service.TelegramBot
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.server.ErrorPage
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry




@Configuration
@EnableCaching
@SpringBootApplication
class ApiApplication : WebMvcConfigurerAdapter() {

    override fun addInterceptors(registry: InterceptorRegistry?) {
        registry!!.addInterceptor(exposeResponseInterceptor())
    }

    override fun addCorsMappings(registry: CorsRegistry?) {
        registry!!.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600)
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "OPTIONS", "PUT")
                .allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin",
                        "Access-Control-Request-Method", "Access-Control-Request-Headers")
                .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
                .allowCredentials(true)
                .maxAge(3600)

        super.addCorsMappings(registry)
    }

    @Bean
    fun exposeResponseInterceptor() = ExposeResponseInterceptor()

    @Bean
    fun methodValidationPostProcessor(): MethodValidationPostProcessor {
        val mv = MethodValidationPostProcessor()
        mv.setValidator(validator())
        return mv
    }

    @Bean
    fun validator() = LocalValidatorFactoryBean()
}

fun main(args: Array<String>) {
    SpringApplication.run(ApiApplication::class.java, *args)
}



