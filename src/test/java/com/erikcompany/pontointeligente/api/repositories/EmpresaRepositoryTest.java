package com.erikcompany.pontointeligente.api.repositories;
import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.erikcompany.pontointeligente.api.entities.Empresa;

//Essa eh uma classe que estou utilizando para testes



@RunWith(SpringRunner.class) //Aqui eu uso a anotacao da biblioteca JUnit "@RunWith", e digo para o JUnit usar a classe "SpringRunner" da biblioteca "Spring-test"(que eh uma dependencia la no pom.xml) para rodar o teste. Se eu nao fizer isso, o teste nao vai funcionar, pois o JUnit sozinho nao sabe como injetar o objetos.
@SpringBootTest              //Anotacao que diz ao SpringBoot que essa eh uma classe que contem testes do meu projeto.
@ActiveProfiles("test")      //Anotacao do Spring-test que faz com que essa classe utilize o arquivo "application-test.proprities" e nao o arquivo ""application.proprities", caso eu esteja usando alguma constante ou configuracao que esteja dentro do arquivo. E sim, eh so isso ai mesmo!
public class EmpresaRepositoryTest {
	
	
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	private static final String CNPJ = "51463645000100";

	
	
	@Before 
	public void antesDeTestar() throws Exception {
		
		//Criando uma empresa para teste.
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa de exemplo");
		empresa.setCnpj(CNPJ);
		
		//Salvo uma empresa na tabela "Empresa", no banco H2. Se nao existir a tabela, o Hibernate automaticamente a cria.
		this.empresaRepository.save(empresa);
	}
	

	
	@Test
	public void testBuscarPorCnpj() {
		
		Empresa empresa = this.empresaRepository.findByCnpj(CNPJ);
		
		assertEquals( CNPJ, empresa.getCnpj() );
	}
	
	
	
	@After
    public final void depoisDeTestar() { 
		
		//Deletando as linhas do banco de dados em memoria.
		this.empresaRepository.deleteAll();
	}

	
}