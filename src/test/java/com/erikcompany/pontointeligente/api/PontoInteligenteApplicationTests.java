package com.erikcompany.pontointeligente.api;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


//Essa eh uma classe que estou utilizando para testes


@RunWith(SpringRunner.class) //Aqui eu uso a anotacao da biblioteca JUnit "@RunWith", e digo para o JUnit usar a classe "SpringRunner" da biblioteca "Spring-test"(que eh uma dependencia la no pom.xml) para rodar o teste. Se eu nao fizer isso, o teste nao vai funcionar, pois o JUnit sozinho nao sabe como injetar o objetos.
@SpringBootTest              //Anotacao que diz ao SpringBoot que essa eh uma classe que contem testes do meu projeto.
public class PontoInteligenteApplicationTests {

	
	
	@Value("${parametroDoErikExemplo}")  //Anotacao do Spring: Serve para pegar o valor de uma constante que esta la no arquivo "application-test.proprities"
	private int valor;
	
	
	@Test
	public void testeDoErik() {
		
		System.out.println("\n\nErik - Executando um teste");
		System.out.println( "\nErik - Valor da varivel que peguei do arquivo 'application-test.properties': " +valor );
		
		
	}

}