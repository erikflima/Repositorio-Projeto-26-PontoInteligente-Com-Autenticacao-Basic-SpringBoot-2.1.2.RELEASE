package com.erikcompany.pontointeligente.api.utils;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class) //Aqui eu uso a anotacao da biblioteca JUnit "@RunWith", e digo para o JUnit usar a classe "SpringRunner" da biblioteca "Spring-test"(que eh uma dependencia la no pom.xml) para rodar o teste. Se eu nao fizer isso, o teste nao vai funcionar, pois o JUnit sozinho nao sabe como injetar o objetos.
@SpringBootTest              //Anotacao que diz ao SpringBoot que essa eh uma classe que contem testes do meu projeto.
@ActiveProfiles("test")      //Anotacao do Spring-test que faz com que essa classe utilize o arquivo "application-test.proprities" e nao o arquivo ""application.proprities", caso eu esteja usando alguma constante ou configuracao que esteja dentro do arquivo. E sim, eh so isso ai mesmo!
public class PasswordUtilsTest {
	
	
	private static final String SENHA_SEM_CRIPTOGRAFIA = "123456";
	
	
	//Objeto que serve para fazer a criptografia e descriptografia. 
	private final BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();

	
	
	@Test
	public void testSenhaNula() throws Exception {
		
		assertNull( PasswordUtils.gerarBCrypt(null) );
	}
	
	
	
	@Test
	public void testGerarHashSenha() throws Exception {
		
		
		//Criptografando a senha em formato hash.
		String senhaCriptografadaEmFormatoHash = PasswordUtils.gerarBCrypt( SENHA_SEM_CRIPTOGRAFIA );
		
		
		assertTrue( bCryptEncoder.matches( SENHA_SEM_CRIPTOGRAFIA, senhaCriptografadaEmFormatoHash) );
	}

}
