package com.erikcompany.pontointeligente.api.controllers;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.erikcompany.pontointeligente.api.entities.Empresa;
import com.erikcompany.pontointeligente.api.services.EmpresaService;



@RunWith(SpringRunner.class) //Aqui eu uso a anotacao da biblioteca JUnit "@RunWith", e digo para o JUnit usar a classe "SpringRunner" da biblioteca "Spring-test"(que eh uma dependencia la no pom.xml) para rodar o teste. Se eu nao fizer isso, o teste nao vai funcionar, pois o JUnit sozinho nao sabe como injetar o objetos.
@SpringBootTest              //Anotacao que diz ao SpringBoot que essa eh uma classe que contem testes do meu projeto.
@ActiveProfiles("test")      //Anotacao do Spring-test que faz com que essa classe utilize o arquivo "application-test.proprities" e nao o arquivo ""application.proprities", caso eu esteja usando alguma constante ou configuracao que esteja dentro do arquivo. E sim, eh so isso ai mesmo!
@AutoConfigureMockMvc        //Anotacao obrigatoria para testar controllers. Isso faz com que o Spring de um contexto web para esse realizar testes.
public class EmpresaControllerTest {

	
	@Autowired 
	private MockMvc mvc; //Container web que permite realizar requisicoes http para testes. Ele vai fazer o trabalho de chamar os endpoints no meu teste.

	
	@MockBean // Anotacao de injecao de dependencia do Mockito. Eu poderia ter usado o "@Autowired" mesmo, mas foi usado Mockito para aprendizado.
	private EmpresaService empresaService;

		
	private static final String BUSCAR_EMPRESA_CNPJ_URL = "/api/empresas/cnpj/";
	private static final String RAZAO_SOCIAL = "Empresa XYZ";
	private static final Long   ID           = Long.valueOf(1);
	private static final String CNPJ         = "82198127000120";


	
	
	@Test
	@WithMockUser
	public void testBuscarEmpresaCnpjInvalido() throws Exception {
		
		//Usando o Mochito nesse teste. Le se: Dado o metodo tal, faca que apos a execucao desse metodo, ele retorne tal...
		BDDMockito.given( this.empresaService.buscarPorCnpj( Mockito.anyString() ) )
		        .willReturn( Optional.empty() );

		
		//Aqui eu vou fazer uma chamada http , como seu eu estivesse usando o postman. Com o objetivo de testar o endpoint.
		mvc.perform( MockMvcRequestBuilders.get( BUSCAR_EMPRESA_CNPJ_URL + CNPJ )   //"perform"-> Faz o metodo http. "MockMvcRequestBuilders.get" -> Diz a chamada vai ser do tipo GET
				                           .accept( MediaType.APPLICATION_JSON ) )  //"Diz o tipo de retorno que aceito e JSON.
				                           .andExpect( status().isBadRequest() )    // Digo que espero um status 400 no retorno
				                           .andExpect( jsonPath("$.errors").value("Empresa não encontrada para o CNPJ " + CNPJ) ); //Coleto o campo "errors" que vem no json de resposta da requisicao, e digo que o conteudo tem que ser "Empresa não encontrada para o CNPJ " + CNPJ".
	}

	
	
	/* REVISAR ESSE PARTE ACOMPANHANDO O VIDEO 19. Criando o controller de empresas
	@Test
	@WithMockUser
	public void testBuscarEmpresaCnpjValido() throws Exception {
		
		
		//Usando o Mochito nesse teste. Le se: Dado o metodo tal, faca que apos a execucao desse metodo, ele retorne tal...
		BDDMockito.given( this.empresaService.buscarPorCnpj( Mockito.anyString() ) )
				.willReturn( Optional.of( this.obterDadosEmpresa() ) );

		
		
		mvc.perform(MockMvcRequestBuilders.get( BUSCAR_EMPRESA_CNPJ_URL + CNPJ )
				   .accept( MediaType.APPLICATION_JSON ) )
				   .andExpect( status().isOk() )
				   .andExpect( jsonPath( "$.data.id" ).value(ID) )
				   .andExpect( jsonPath( "$.data.razaoSocial", equalTo( RAZAO_SOCIAL ) ) )
				   .andExpect( jsonPath( "$.data.cnpj", equalTo(CNPJ) ) )
				   .andExpect( jsonPath( "$.errors" ).isEmpty() );
	}
   */
	
	
	private Empresa obterDadosEmpresa() {
		
		Empresa empresa = new Empresa();
		empresa.setId(ID);
		empresa.setRazaoSocial(RAZAO_SOCIAL);
		empresa.setCnpj(CNPJ);
		
		return empresa;
	}

}