package com.erikcompany.pontointeligente.api.repositories;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.erikcompany.pontointeligente.api.entities.Empresa;
import com.erikcompany.pontointeligente.api.entities.Funcionario;
import com.erikcompany.pontointeligente.api.enums.PerfilEnum;
import com.erikcompany.pontointeligente.api.utils.PasswordUtils;

//Essa eh uma classe que estou utilizando para testes


@RunWith(SpringRunner.class) //Aqui eu uso a anotacao da biblioteca JUnit "@RunWith", e digo para o JUnit usar a classe "SpringRunner" da biblioteca "Spring-test"(que eh uma dependencia la no pom.xml) para rodar o teste. Se eu nao fizer isso, o teste nao vai funcionar, pois o JUnit sozinho nao sabe como injetar o objetos.
@SpringBootTest              //Anotacao que diz ao SpringBoot que essa eh uma classe que contem testes do meu projeto.
@ActiveProfiles("test")      //Anotacao do Spring-test que faz com que essa classe utilize o arquivo "application-test.proprities" e nao o arquivo ""application.proprities", caso eu esteja usando alguma constante ou configuracao que esteja dentro do arquivo. E sim, eh so isso ai mesmo!
public class FuncionarioRepositoryTest {

	
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;

	
	@Autowired
	private EmpresaRepository empresaRepository;

	
	
	private static final String EMAIL = "email@email.com";
	private static final String CPF   = "24291173474";

	
	
	@Before //Preparo o banco com 1 empresa e um funcionario.
	public void antesDeTestar() throws Exception {
		
		
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa de exemplo");
		empresa.setCnpj("51463645000100");
		this.empresaRepository.save( empresa ); //Salvo uma empresa na tabela "Empresa", no banco H2. Se nao existir a tabela, o Hibernate automaticamente a cria.
		
		
		Funcionario funcionario = new Funcionario();
		funcionario.setNome("Fulano de Tal");
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		funcionario.setCpf(CPF);
		funcionario.setEmail(EMAIL);
		funcionario.setEmpresa(empresa);
		this.funcionarioRepository.save( funcionario ); //Salvo um funcionario na tabela "Funcionario", no banco H2. Se nao existir a tabela, o Hibernate automaticamente a cria.
	}

	


	@Test
	public void testBuscarFuncionarioPorEmail() {
		
		Funcionario funcionario = this.funcionarioRepository.findByEmail(EMAIL);

		assertEquals( EMAIL, funcionario.getEmail() );
	}

	
	
	@Test
	public void testBuscarFuncionarioPorCpf() {
		
		Funcionario funcionario = this.funcionarioRepository.findByCpf(CPF);

		assertEquals( CPF, funcionario.getCpf() );
	}

	
	
	@Test
	public void testBuscarFuncionarioPorEmailECpf() {
		
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail( CPF, EMAIL );

		assertNotNull(funcionario);
	}

	
	
	@Test
	public void testBuscarFuncionarioPorEmailOuCpfParaEmailInvalido() {
		
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail( CPF, "email@invalido.com" );

		assertNotNull(funcionario);
	}

	
	
	@Test
	public void testBuscarFuncionarioPorEmailECpfParaCpfInvalido() {
		
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail( "12345678901", EMAIL );

		assertNotNull(funcionario);
	}

	
	
	@After
	public final void depoisDeTestar() {
		
		this.empresaRepository.deleteAll();
	}
	
}