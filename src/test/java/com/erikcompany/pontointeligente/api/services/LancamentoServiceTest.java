package com.erikcompany.pontointeligente.api.services;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.erikcompany.pontointeligente.api.entities.Lancamento;
import com.erikcompany.pontointeligente.api.repositories.LancamentoRepository;



@RunWith(SpringRunner.class) //Aqui eu uso a anotacao da biblioteca JUnit "@RunWith", e digo para o JUnit usar a classe "SpringRunner" da biblioteca "Spring-test"(que eh uma dependencia la no pom.xml) para rodar o teste. Se eu nao fizer isso, o teste nao vai funcionar, pois o JUnit sozinho nao sabe como injetar o objetos.
@SpringBootTest              //Anotacao que diz ao SpringBoot que essa eh uma classe que contem testes do meu projeto.
@ActiveProfiles("test")      //Anotacao do Spring-test que faz com que essa classe utilize o arquivo "application-test.proprities" e nao o arquivo ""application.proprities", caso eu esteja usando alguma constante ou configuracao que esteja dentro do arquivo. E sim, eh so isso ai mesmo!
public class LancamentoServiceTest {

	
	
	@MockBean // Anotacao de injecao de dependencia do Mockito. Eu poderia ter usado o "@Autowired" mesmo, mas foi usado Mockito para aprendizado.
	private LancamentoRepository lancamentoRepository;

	
	@Autowired
	private LancamentoService lancamentoService;

	
	@Before
	public void setUp() throws Exception {
	
		
		//Usando o Mochito nesse teste. Le se: Dado o metodo "findById", faca que apos a execucao desse metodo, ele retorne tal...
		BDDMockito.given( lancamentoRepository.findById( Mockito.anyLong() ) ).willReturn( Optional.of(new Lancamento()) );
		
		
		//Usando o Mochito nesse teste. Le se: Dado o metodo tal, faca que apos a execucao desse metodo, ele retorne tal...
		BDDMockito.given( lancamentoRepository.save(Mockito.any( Lancamento.class) ) ).willReturn( new Lancamento() );

		//Usando o Mochito nesse teste. Le se: Dado o metodo tal, faca que apos a execucao desse metodo, ele retorne tal...
		//Obs: "PageImpl<Lancamento>" eh o mesmo que colocar "Page<Lancamento>". Mas tem que ser assim mesmo.
		BDDMockito.given( lancamentoRepository.findByFuncionarioId( Mockito.anyLong(), Mockito.any( PageRequest.class ) ) )
		.willReturn( new PageImpl<Lancamento>( new ArrayList<Lancamento>() ) );
	
		//Nao faca nada quando 
		doNothing().when(lancamentoRepository).delete( Mockito.any( Lancamento.class ) );
	
	}
	
	

	@Test
	public void testBuscarLancamentoPorFuncionarioId() {

		
		//Dizendo que quero selecionar somente os 10 primeiros resultados da selecao. Da posicao "0" ate a posicao "10".
		PageRequest quantidadeDeLinhasASelecionar = PageRequest.of(0, 10);
		
		Page<Lancamento> lancamento = lancamentoService.buscarPorFuncionarioId( 1L, quantidadeDeLinhasASelecionar );

		assertNotNull(lancamento);
	}

	
	
	@Test
	public void testBuscarLancamentoPorId() {
		
		Optional<Lancamento> lancamento = lancamentoService.buscarPorId(1L);

		assertTrue(lancamento.isPresent());
	}

	
	
	@Test
	public void testPersistirLancamento() {
		
		Lancamento lancamento = lancamentoService.persistir( new Lancamento() );
		
		assertNotNull(lancamento);
	}
	
	
	@Test
	 public void testRemoverLancamentoPorId() {
		
		lancamentoService.remover(1L);
	 
		verify(this.lancamentoRepository, times(1)).deleteById(1L);
	 
	 }

	
}