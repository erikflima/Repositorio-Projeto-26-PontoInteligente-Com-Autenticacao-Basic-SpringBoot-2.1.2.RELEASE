package com.erikcompany.pontointeligente.api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


//Classe principal que de fato executa o projeto.


@SpringBootApplication //Anotacao do Spring Boot que faz tudo acontecer. Inicializa o Spring Boot, componentes, entre outras coisas.
@EnableCaching         //Habilita o uso de Cache da biblioteca EhCache na aplicaçao.
public class PontoInteligenteApplication {
	

	public static void main(String[] args) {
		
		System.out.println("\n\nErik - Executando o metodo main e apos isso o metodo SpringApplication.run, que roda a aplicacao literalmente!\n\n");

		SpringApplication.run(PontoInteligenteApplication.class, args);
		
		System.out.println("\n\nErik - Finalizando a execucao do metodo main \n\n");			
	}

}