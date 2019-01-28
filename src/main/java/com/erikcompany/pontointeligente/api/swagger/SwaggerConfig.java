package com.erikcompany.pontointeligente.api.swagger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


//URI para ver a documentacao em formato HTML "localhost:8080/swagger-ui.html".



@Configuration                         //Anotacao do Spring, que diz para o Spring que essa eh uma classe de configuracao. Ou seja, uma classe que o Spring vai rodar internamente e fazer algo.
@EnableSwagger2                        //Anotacao da biblioteca do Swagger, que habilita a geracao da documentacao Swagger no meu projeto.
@Profile("profile_de_desenvolvimento") //Defino que o Swagger somente vai gerar a documentacao do projeto e disponibilizar aquela pagina html, se o profile for o que eu defini.
public class SwaggerConfig {

	
	
	@Bean                 //Anotação utilizada em cima dos métodos de uma classe, geralmente marcada com @Configuration, indicando que o Spring sozinho deve invocar esse metodo e gerenciar o objeto retornado por ele. Quando digo gerenciar é que agora este objeto pode ser injetado em qualquer ponto da sua aplicação.
	public Docket api() { //Esse metodo vai ser executado automaticamente pelo Spring. E vai preparar uma pagina html na a uri "localhost:8080/swagger-ui.html".

		
		//Criando um obj "Docket", que eh um objeto que o Spring, internamente vai usar para gerar a pagina html Swagger de documentacao.
		Docket docketASerRetornado = new Docket( DocumentationType.SWAGGER_2 );
		
		
		return docketASerRetornado.select()
				                  .apis(RequestHandlerSelectors.basePackage("com.erikcompany.pontointeligente.api.controllers")) //Obs: Aqui eu tenho que digitar o nome base dos pacotes da minha aplicacao.
				                  .paths(PathSelectors.any()).build()
				                  .apiInfo( apiInfo() );
	}

	
	
	
	//Metodo que que retorna um objeto "ApiInfo". Esse objeto vai conter informacoes que vao aparecer na interface visual do Swagger com o detalhamento dos meus servicos.
	private ApiInfo apiInfo() {
		
		
		//Aqui eu defino o texto que vou querer que aparece la interface visual do Swagger.
	    String  title             = "Documentação com Swagger API - Projeto Ponto Inteligente";
	    String  description       = "Documentação do Erik feita com Swagger do projeto Ponto Inteligente API\n - Lista de endpoints da aplicação com detalhamento";
        String  version           = "1.0";
        String  termsOfServiceUrl = "https://site-do-erik.com/termos";
        String  license           = "Licensa super blaster";
        String  licenseUrl        = "https://site-do-erik.com/licensa";
        Contact contact           = new Contact("Erik Lima", "https://erikcompany", "erik.alves253@gmail.com");

        
		ApiInfo apiInfo = new ApiInfo( title, description, version, termsOfServiceUrl, contact, license, licenseUrl );

		return apiInfo;		
		
	}

}