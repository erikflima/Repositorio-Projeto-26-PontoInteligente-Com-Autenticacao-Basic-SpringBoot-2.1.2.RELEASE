package com.erikcompany.pontointeligente.api.controllers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.erikcompany.pontointeligente.api.dtos.LancamentoDto;
import com.erikcompany.pontointeligente.api.entities.Funcionario;
import com.erikcompany.pontointeligente.api.entities.Lancamento;
import com.erikcompany.pontointeligente.api.enums.TipoEnum;
import com.erikcompany.pontointeligente.api.services.FuncionarioService;
import com.erikcompany.pontointeligente.api.services.LancamentoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@RunWith(SpringRunner.class) //Aqui eu uso a anotacao da biblioteca JUnit "@RunWith", e digo para o JUnit usar a classe "SpringRunner" da biblioteca "Spring-test"(que eh uma dependencia la no pom.xml) para rodar o teste. Se eu nao fizer isso, o teste nao vai funcionar, pois o JUnit sozinho nao sabe como injetar o objetos.
@SpringBootTest              //Anotacao que diz ao SpringBoot que essa eh uma classe que contem testes do meu projeto.
@ActiveProfiles("test")      //Anotacao do Spring-test que faz com que essa classe utilize o arquivo "application-test.proprities" e nao o arquivo ""application.proprities", caso eu esteja usando alguma constante ou configuracao que esteja dentro do arquivo. E sim, eh so isso ai mesmo!
@AutoConfigureMockMvc        //Anotacao obrigatoria para testar controllers. Isso faz com que o Spring de um contexto web para esse realizar testes.
public class LancamentoControllerTest {

	
	
	@Autowired
	private MockMvc mvc; //Container web que permite realizar requisicoes http para testes. Ele vai fazer o trabalho de chamar os endpoints no meu teste.
	
	
	@MockBean // Anotacao de injecao de dependencia do Mockito. Eu poderia ter usado o "@Autowired" mesmo, mas foi usado Mockito para aprendizado.
	private LancamentoService  lancamentoService;
	
	@MockBean // Anotacao de injecao de dependencia do Mockito. Eu poderia ter usado o "@Autowired" mesmo, mas foi usado Mockito para aprendizado.
	private FuncionarioService funcionarioService;
	
	
	
	private static final String URL_ENDPOINT_ADICIONAR     = "/api/lancamentos/adicionar";
	private static final String URL_ENDPOINT_LISTAR_POR_ID = "/api/lancamentos/listarPorId/";
	private static final String URL_ENDPOINT_ATUALIZAR     = "/api/funcionarios/atualizar/";
	private static final String URL_ENDPOINT_REMOVER       = "/api/lancamentos/remover/";
	
	private static final Long   ID_FUNCIONARIO       = 1L;
	private static final Long   ID_LANCAMENTO        = 1L;
	private static final String TIPO_DO_LANCAMENTO   = TipoEnum.INICIO_TRABALHO.name();
	private static final Date   DATA_DO_LANCAMENTO   = new Date();
	
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //Definindo de data que quero. Obs: Por padrao, esse eh o formato que o MySQL utiliza.
	
	
	
	@Test
	//@WithMockUser
	public void testCadastrarLancamento() throws Exception {
		
		
		//Cria um obj Lancamento para teste.
		Lancamento lancamento = obterDadosLancamento();
		
		
		//Usando o Mochito nesse teste. Le se: Dado o metodo tal, faca que apos a execucao desse metodo, ele retorne tal...
		BDDMockito.given( this.funcionarioService.buscarPorId( Mockito.anyLong() ) )
		.willReturn( Optional.of( new Funcionario() ) );
		
		//Usando o Mochito nesse teste. Le se: Dado o metodo tal, faca que apos a execucao desse metodo, ele retorne tal...
		BDDMockito.given( this.lancamentoService.persistir( Mockito.any(Lancamento.class ) ) )
		.willReturn(lancamento);

		
		
		//Aqui eu vou fazer uma chamada http , como seu eu estivesse usando o postman. Com o objetivo de testar o endpoint.
		mvc.perform( MockMvcRequestBuilders.post(URL_ENDPOINT_ADICIONAR)                                                                          //"mvc.perform"-> Faz o metodo http. "MockMvcRequestBuilders.post" -> Diz a chamada vai ser do tipo POST.
	                                       .content( this.obterJsonParaRequisicaoPost() )                                                         //Aqui eu coloco o json que vai no Body da requisicao do tipo POST.
				                           .contentType( MediaType.APPLICATION_JSON )                                                             //Diz que vou enviar inforcoes em formato JSON
				                           .accept( MediaType.APPLICATION_JSON) )                                                                 //Diz o tipo de retorno que aceito e JSON.
				                           .andExpect( status().isOk() )                                                                          //Digo que espero um status 200-ok no retorno.
				                           .andExpect( jsonPath( "$.conteudoDoResponse.id").value(ID_LANCAMENTO))                                 //Pego o campo "conteudoDoResponse.id" do json de resposta e vejo se ele tem o valor que espero.
				                           .andExpect( jsonPath( "$.conteudoDoResponse.tipo").value(TIPO_DO_LANCAMENTO))                          //Pego o campo "conteudoDoResponse.tipo" do json de resposta e vejo se ele tem o valor que espero.
				                           .andExpect( jsonPath( "$.conteudoDoResponse.data").value( this.dateFormat.format(DATA_DO_LANCAMENTO) ))//Pego o campo "conteudoDoResponse.data" do json de resposta e vejo se ele tem o valor que espero.
			                               .andExpect( jsonPath( "$.conteudoDoResponse.funcionarioId").value(ID_FUNCIONARIO) )                    //Pego o campo "conteudoDoResponse.funcionarioId" do json de resposta e vejo se ele tem o valor que espero.
	                                       .andExpect( jsonPath( "$.errors").isEmpty() );                                                         //Pego o campo "errors" do json de resposta e vejo se ele tem o valor que espero.
	}
	
	
	
	
	
	@Test
	//@WithMockUser
	public void testCadastrarLancamentoFuncionarioIdInvalido() throws Exception {
		
		
		//Usando o Mochito nesse teste. Le se: Dado o metodo tal, faca que apos a execucao desse metodo, ele retorne tal...
		BDDMockito.given( this.funcionarioService.buscarPorId( Mockito.anyLong() ))
		    .willReturn( Optional.empty() );

		
		//Coleto o campo "errors" que vem no json de resposta da requisicao, e digo que o conteudo tem que ser "Empresa não encontrada para o CNPJ " + CNPJ".
		mvc.perform( MockMvcRequestBuilders.post(URL_ENDPOINT_ADICIONAR)                                                         //"mvc.perform"-> Faz o metodo http. "MockMvcRequestBuilders.post" -> Diz a chamada vai ser do tipo POST.
				                           .content( this.obterJsonParaRequisicaoPost() )                                        //Aqui eu coloco o json que vai no Body da requisicao do tipo POST.
				                           .contentType( MediaType.APPLICATION_JSON)                                             //Diz o tipo de retorno que aceito e JSON.
				                           .accept( MediaType.APPLICATION_JSON))                                                 //Diz o tipo de retorno que aceito e JSON.
				                           .andExpect( status().isBadRequest() )                                                 //Digo que espero um status 400 no retorno
				                           .andExpect( jsonPath("$.errors").value("Funcionário não encontrado. ID inexistente."))//Pego o campo "errors" do json de resposta e vejo se ele tem o valor que espero.
				                           .andExpect( jsonPath("$.conteudoDoResponse").isEmpty());                              //Pego o campo "conteudoDoResponse" do json de resposta e digo que espero que ele esteja vazio.
	}
	
	
	
	
	
	@Test
	//@WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
	public void testRemoverLancamento() throws Exception {
		
		
		//Usando o Mochito nesse teste. Le se: Dado o metodo tal, faca que apos a execucao desse metodo, ele retorne tal...
		BDDMockito.given( this.lancamentoService.buscarPorId( Mockito.anyLong() ) )
		    .willReturn( Optional.of(new Lancamento()) );

		
		mvc.perform( MockMvcRequestBuilders.delete( URL_ENDPOINT_REMOVER + ID_LANCAMENTO ) //"mvc.perform"-> Faz o metodo http. "MockMvcRequestBuilders.post" -> Diz a chamada vai ser do tipo DELETE.
				                           .accept( MediaType.APPLICATION_JSON ) )         //Diz o tipo de retorno que aceito e JSON.
			                               .andExpect( status().isOk() );                  //Digo que espero um status 200-ok no retorno.
	}
	
	
	
	/*
	@Test
	@WithMockUser
	public void testRemoverLancamentoAcessoNegado() throws Exception {
		
		
		BDDMockito.given( this.lancamentoService.buscarPorId(Mockito.anyLong() ) )
		   .willReturn( Optional.of( new Lancamento()) );

		
		mvc.perform( MockMvcRequestBuilders.delete( URL_ENDPOINT_REMOVER + ID_LANCAMENTO) //"mvc.perform"-> Faz o metodo http. "MockMvcRequestBuilders.post" -> Diz a chamada vai ser do tipo GET
				                           .accept( MediaType.APPLICATION_JSON))          //Diz o tipo de retorno que aceito e JSON.
				                           .andExpect( status().isForbidden() );          //Digo que espero um status 403-FORBIDDEN no retorno.
	}
    */
	
	
	//Cria uma String em formato JSON.
	private String obterJsonParaRequisicaoPost() throws JsonProcessingException {
		
		
		//Crio um LancamentoDto que vou preencher, e depois transforma-lo em um JSON
		LancamentoDto lancamentoDto = new LancamentoDto();
		
		lancamentoDto.setId(null);
		lancamentoDto.setData(this.dateFormat.format(DATA_DO_LANCAMENTO));
		lancamentoDto.setTipo(TIPO_DO_LANCAMENTO);
		lancamentoDto.setFuncionarioId(ID_FUNCIONARIO);
		
		//Objeto que serve para converter objeto em um JSON
		ObjectMapper mapper = new ObjectMapper();
		
		
		String lancamentoEmFormatoJSON = mapper.writeValueAsString(lancamentoDto);
		
		return lancamentoEmFormatoJSON;
	}
	
	
	

	//Cria um obj Lancamento com dados genericos para o teste.
	private Lancamento obterDadosLancamento() {
		
		Lancamento lancamento = new Lancamento();
		
		lancamento.setId(ID_LANCAMENTO);
		lancamento.setData(DATA_DO_LANCAMENTO);
		lancamento.setTipo(TipoEnum.valueOf(TIPO_DO_LANCAMENTO));
		lancamento.setFuncionario(new Funcionario());
		lancamento.getFuncionario().setId(ID_FUNCIONARIO);
		
		return lancamento;
	}	

}