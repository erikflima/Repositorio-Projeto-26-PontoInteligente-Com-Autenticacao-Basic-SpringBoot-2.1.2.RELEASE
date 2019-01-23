package com.erikcompany.pontointeligente.api.services;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.erikcompany.pontointeligente.api.entities.Funcionario;
import com.erikcompany.pontointeligente.api.repositories.FuncionarioRepository;



@RunWith(SpringRunner.class) //Aqui eu uso a anotacao da biblioteca JUnit "@RunWith", e digo para o JUnit usar a classe "SpringRunner" da biblioteca "Spring-test"(que eh uma dependencia la no pom.xml) para rodar o teste. Se eu nao fizer isso, o teste nao vai funcionar, pois o JUnit sozinho nao sabe como injetar o objetos.
@SpringBootTest              //Anotacao que diz ao SpringBoot que essa eh uma classe que contem testes do meu projeto.
@ActiveProfiles("test")      //Anotacao do Spring-test que faz com que essa classe utilize o arquivo "application-test.proprities" e nao o arquivo ""application.proprities", caso eu esteja usando alguma constante ou configuracao que esteja dentro do arquivo. E sim, eh so isso ai mesmo!
public class FuncionarioServiceTest {

	
	
	@MockBean // Anotacao de injecao de dependencia do Mockito. Eu poderia ter usado o "@Autowired" mesmo, mas foi usado Mockito para aprendizado.
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private FuncionarioService    funcionarioService;

	
	
	
	@Before
	public void setUp() throws Exception {
		
		//Usando o Mochito nesse teste. Le se: Dado o metodo tal, faca que apos a execucao desse metodo, ele retorne tal...
	    BDDMockito.given( funcionarioRepository.save( Mockito.any(Funcionario.class) ) ).willReturn( new Funcionario() );
	    
	    //Usando o Mochito nesse teste. Le se: Dado o metodo tal, faca que apos a execucao desse metodo, ele retorne tal...
	    BDDMockito.given( funcionarioRepository.findById( Mockito.anyLong()) ).willReturn( Optional.of(new Funcionario()) ); 
		
	    //Usando o Mochito nesse teste. Le se: Dado o metodo tal, faca que apos a execucao desse metodo, ele retorne tal...
	    BDDMockito.given( funcionarioRepository.findByEmail( Mockito.anyString()) ).willReturn( new Funcionario() );
		
	    //Usando o Mochito nesse teste. Le se: Dado o metodo tal, faca que apos a execucao desse metodo, ele retorne tal...
	    BDDMockito.given( funcionarioRepository.findByCpf( Mockito.anyString()) ).willReturn( new Funcionario() );
	}

	
	
	@Test
	public void testPersistirFuncionario() {
		
		Funcionario funcionario = this.funcionarioService.persistir(new Funcionario());

		assertNotNull(funcionario);
	}

	
	
	@Test
	public void testBuscarFuncionarioPorId() {
		
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(1L);

		assertTrue(funcionario.isPresent());
	}

	
	
	@Test
	public void testBuscarFuncionarioPorEmail() {
		
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorEmail("email@email.com");

		assertTrue(funcionario.isPresent());
	}

	
	
	@Test
	public void testBuscarFuncionarioPorCpf() {
		
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorCpf("24291173474");

		assertTrue( funcionario.isPresent() );
	}

}