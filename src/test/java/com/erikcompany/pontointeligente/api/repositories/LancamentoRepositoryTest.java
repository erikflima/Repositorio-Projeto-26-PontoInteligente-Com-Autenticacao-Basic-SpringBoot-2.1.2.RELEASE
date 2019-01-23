package com.erikcompany.pontointeligente.api.repositories;
import static org.junit.Assert.assertEquals;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.erikcompany.pontointeligente.api.entities.Empresa;
import com.erikcompany.pontointeligente.api.entities.Funcionario;
import com.erikcompany.pontointeligente.api.entities.Lancamento;
import com.erikcompany.pontointeligente.api.enums.PerfilEnum;
import com.erikcompany.pontointeligente.api.enums.TipoEnum;
import com.erikcompany.pontointeligente.api.utils.PasswordUtils;



@RunWith(SpringRunner.class) //Aqui eu uso a anotacao da biblioteca JUnit "@RunWith", e digo para o JUnit usar a classe "SpringRunner" da biblioteca "Spring-test"(que eh uma dependencia la no pom.xml) para rodar o teste. Se eu nao fizer isso, o teste nao vai funcionar, pois o JUnit sozinho nao sabe como injetar o objetos.
@SpringBootTest              //Anotacao que diz ao SpringBoot que essa eh uma classe que contem testes do meu projeto.
@ActiveProfiles("test")      //Anotacao do Spring-test que faz com que essa classe utilize o arquivo "application-test.proprities" e nao o arquivo ""application.proprities", caso eu esteja usando alguma constante ou configuracao que esteja dentro do arquivo. E sim, eh so isso ai mesmo!
public class LancamentoRepositoryTest {
	
	
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	
	private Long funcionarioId;

	
	
	@Before //Preparo o banco com 1 empresa, e 1 funcionario que tem 2 lancamentos.
	public void antesDoTest() throws Exception {
		
		
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa de exemplo");
		empresa.setCnpj("51463645000100");
		empresaRepository.save( empresa );  //Salvo uma empresa na tabela "Empresa", no banco H2. Se nao existir a tabela, o Hibernate automaticamente a cria.
		
	
		Funcionario funcionario = new Funcionario();
		funcionario.setNome("Fulano de Tal");
		funcionario.setPerfil( PerfilEnum.ROLE_USUARIO );
		funcionario.setSenha( PasswordUtils.gerarBCrypt("123456") );
		funcionario.setCpf("24291173474");
		funcionario.setEmail("email@email.com");
		funcionario.setEmpresa(empresa);
		funcionarioRepository.save( funcionario );  //Salvo um funcionario na tabela "Funcionario", no banco H2. Se nao existir a tabela, o Hibernate automaticamente a cria.
		
		
		Lancamento lancameto1 = new Lancamento();
		lancameto1.setData( new Date() );
		lancameto1.setTipo( TipoEnum.INICIO_ALMOCO );
		lancameto1.setFuncionario( funcionario );
		lancamentoRepository.save( lancameto1 );  //Salvo um lancamento na tabela "Lancamento", no banco H2. Se nao existir a tabela, o Hibernate automaticamente a cria.

		Lancamento lancameto2 = new Lancamento();
		lancameto2.setData( new Date() );
		lancameto2.setTipo( TipoEnum.TERMINO_TRABALHO );
		lancameto2.setFuncionario( funcionario );
		lancamentoRepository.save( lancameto2 );  //Salvo um lancamento na tabela "Lancamento", no banco H2. Se nao existir a tabela, o Hibernate automaticamente a cria.		

		
		funcionarioId = funcionario.getId();
	}

	
	
	@Test
	public void testBuscarLancamentosPorFuncionarioId() {
		
		List<Lancamento> lancamentos = this.lancamentoRepository.pegaTodosOsLacamentosDoFuncionario( funcionarioId );
		
		assertEquals( 2, lancamentos.size() );
	}
	
	
	
	@Test
	public void testBuscarLancamentosPorFuncionarioIdPaginado() {
		
		
		//Definindo que quero extrair somente os 10 primeiros resultados do select.
		PageRequest quantidadeDeResultadosQueDesejo = PageRequest.of(0, 10);
		
		
		//Select dos lancamentos.
		Page<Lancamento> lancamentos = lancamentoRepository.pegaTodosOsLacamentosDoFuncionario( funcionarioId, quantidadeDeResultadosQueDesejo );
		
		
		assertEquals(2, lancamentos.getTotalElements());
	}
	
	
	
	@After
	public void depoisDoTeste() throws Exception {
		
		this.empresaRepository.deleteAll();
	}

}