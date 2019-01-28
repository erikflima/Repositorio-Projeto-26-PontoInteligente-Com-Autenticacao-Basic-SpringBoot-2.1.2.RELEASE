package com.erikcompany.pontointeligente.api.controllers;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erikcompany.pontointeligente.api.dtos.FuncionarioDto;
import com.erikcompany.pontointeligente.api.entities.Funcionario;
import com.erikcompany.pontointeligente.api.response.ResponsePadronizado;
import com.erikcompany.pontointeligente.api.services.FuncionarioService;
import com.erikcompany.pontointeligente.api.utils.PasswordUtils;


@RestController                                                         //Anotacao do Spring que torna essa classe um endpoint.
@RequestMapping(value="/api/funcionarios", produces="application/json") //Anotacao do Spring que uso para definir qual sera o caminho do endpoint. Digo que recebe json e produzo json.
@CrossOrigin(origins = "*")                                             //Anotacao do Spring que uso para dizer que esse controller pode receber requisicoes de qualquer origem, ou seja, requisicoes de qualquer dominio(url), mas eu poderia restrigir, e colocar quais domininios podem fazer requisicoes para esse controller.
public class FuncionarioController {

	
	
	private static final Logger log = LoggerFactory.getLogger(FuncionarioController.class);

	
	@Autowired
	private FuncionarioService funcionarioService;

	
	
	public FuncionarioController() {
	}

	
	
	/**Anotacao para gerar JavaDoc*
	 * Atualiza os dados de um funcionário.
	 * 
	 * @param id
	 * @param funcionarioDto
	 * @param resultadoDaValidacao
	 * @return ResponseEntity<Response<FuncionarioDto>>
	 * @throws NoSuchAlgorithmException
	 */
    //@PutMapping  -> Anotacao do Spring que defino qual verbo quero utilizar. Poderia ser "@GetMapping" or "@DeleteMapping" or "@PutMapping" or "@PatchMapping".
    //@RequestBody  -> Essa anotacao faz com que ao receber a requisicao, o que estiver no body da requisicao vai ser jogado dentro do objeto "empresaDtoRecebida", pra que eu posso manipular esse objeto depois. 
    //@Valid        -> Faz com que o parametro seja validado automaticamente pelo hibernate validator(que sao anotacoes), baseado nas anotacoes de validacao que estao na classe do paramentro.
    //BindingResult -> Trabalha junto com a anotacao @Valid. Se o hibernate validator(que sao anotacoes) realizar as validacoes e achar algum erro, ele vai se encarregar de me passar um objeto "BindingResult" ai dentro do metodo. Se nao tiver nenhum erro, o objeto "BindingResult" fica vazio.
	//OBS: Usar o verbo "PUT" sem que for um endpoint de atualizacao.
	@PutMapping(value = "atualizar/{id}")  
	public ResponseEntity<ResponsePadronizado< FuncionarioDto> > atualizar( @PathVariable("id") Long id,
			                                                                @Valid @RequestBody FuncionarioDto funcionarioDto, 
			                                                                                    BindingResult  resultadoDaValidacao ) throws NoSuchAlgorithmException {
		
		
		log.info("Atualizando funcionário: {}", funcionarioDto.toString() );
		
		
		//Preparando o objeto de resposta padronizada que criei.
		ResponsePadronizado<FuncionarioDto> responsePadronizado = new ResponsePadronizado<FuncionarioDto>();

		
		//Verificar se o funcionario ja existe no banco.
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(id);
		
		
		//Se o funcionario nao existe no banco.
		if ( !funcionario.isPresent() ) {
		
			resultadoDaValidacao.addError( new ObjectError("funcionario", "Funcionário não encontrado.") );
		}

		
		this.atualizarDadosFuncionario( funcionario.get(), funcionarioDto, resultadoDaValidacao );

		
		//Verificando a validacao dos dados de entrada feita automaticamente pelo Hibernate, com base nas anotacoes feitas na classe "FuncionarioDto".
		if ( resultadoDaValidacao.hasErrors() ) {
			
			log.error("Erro validando funcionário: {}", resultadoDaValidacao.getAllErrors());
			
			//A variavel "resultadoDaValidacao" eh enviado pela validacao que o Spring fez, baseado nas anotacoes de validacao que estao la na classe "CadastroPJDto".
			//Entao, pego a lista de erros dessa variavel.
			List<ObjectError> listaDeErros = resultadoDaValidacao.getAllErrors();
			
			
			for( ObjectError auxiliar : listaDeErros  ){
				
				//Pego a mensagem de erro da posicao atual.
				String mensagemDeErroExtraida = auxiliar.getDefaultMessage();
						
				responsePadronizado.getErrors().add(mensagemDeErroExtraida);
			}			
			
			
			/*Aqui eu retorno uma instancia da classe "ResponseEntity". O metodo badRequest(), ja deixa esse objeto com status code +400-Bad Request.
			 Esse objeto ja tem um monte de atributos que uma resposta de servico tem, como por exemplo o campo "statusCode".
			 Entao eu uso esse objeto como reposta. Ai o Spring vai transformar esse objeto "ResponseEntity" em um json e retornar.
			*/			
			return ResponseEntity.badRequest().body( responsePadronizado );
		}

		
		//Atualizo as informacoes do funcionario no banco. 
		this.funcionarioService.persistir( funcionario.get() );
		
		
		//Converto o funcionario em funcionarioDTO para retornar na resposta.
		FuncionarioDto FuncionarioDtoASerRetornado = this.converterParaFuncionarioDto( funcionario.get() );
		
		
		responsePadronizado.setConteudoDoResponse( FuncionarioDtoASerRetornado );

		
		/*Aqui eu retorno uma instancia da classe "ResponseEntity". O metodo ok(), ja deixa esse objeto com status code +200-Ok.
		 Esse objeto ja tem um monte de atributos que uma resposta de servico tem, como por exemplo o campo "statusCode".
		 Entao eu uso esse objeto como reposta. Ai o Spring vai transformar esse objeto "ResponseEntity" em um json e retornar.
		*/
		return ResponseEntity.ok().body( responsePadronizado );
	}

	
	
	
	/**Anotacao para gerar JavaDoc*
	 * Atualiza os dados do funcionário com base nos dados encontrados no DTO.
	 * 
	 * @param funcionario
	 * @param funcionarioDto
	 * @param resultadoDaValidacao
	 * @throws NoSuchAlgorithmException
	 */
	private void atualizarDadosFuncionario( Funcionario    funcionario, 
			                                FuncionarioDto funcionarioDto, 
			                                BindingResult  resultadoDaValidacao ) throws NoSuchAlgorithmException {
		
		
		//Atualiza o nome do funcionario.
		funcionario.setNome( funcionarioDto.getNome() );

		
		//Verifica se o email recebido no DTO eh diferente do email que o funcionario ja tem. Se for diferentes, estao o atualizo com o email que veio no DTo.
		if ( !funcionario.getEmail().equals( funcionarioDto.getEmail() ) ) {
			
			
			//Verifico se alguem no banco ja esta usando esse email.
			//LE se: Busco um funcionario pelo email, se a busca der certo, entao adiciono um erro na variavel "resultadoDaValidacao".
			this.funcionarioService.buscarPorEmail( funcionarioDto.getEmail() )
					.ifPresent(funcionarioEncontrado -> resultadoDaValidacao.addError( new ObjectError("email", "Email já existente.") ) );
			
			funcionario.setEmail( funcionarioDto.getEmail() );
		}

		
		//Aqui eu verifico se o DTO veio com o campo "qtdHorasAlmoco" preenchido, pois se trada de um opcional.
		funcionario.setQtdHorasAlmoco(null);
		funcionarioDto.getQtdHorasAlmoco()
				.ifPresent( qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco( Float.valueOf(qtdHorasAlmoco) ) );

		
		//Aqui eu verifico se o DTO veio com o campo "qtdHorasTrabDia" preenchido, pois se trada de um opcional.
		funcionario.setQtdHorasTrabalhoDia(null);
		funcionarioDto.getQtdHorasTrabalhoDia()
				.ifPresent( qtdHorasTrabDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf( qtdHorasTrabDia) ) );

		
		//Aqui eu verifico se o DTO veio com o campo "valorHora" preenchido, pois se trada de um opcional.
		funcionario.setValorHora(null);
		funcionarioDto.getValorHora()
		        .ifPresent(valorHora -> funcionario.setValorHora( new BigDecimal(valorHora) ) );

		
		//Aqui verifico o campo "senha" veio no DTO, se sim, significa que devo atualizar a senha.
		if ( funcionarioDto.getSenha().isPresent() ) {
			
			funcionario.setSenha( PasswordUtils.gerarBCrypt( funcionarioDto.getSenha().get() ) );
		}
	}

	
	
	
	/**Anotacao para gerar JavaDoc*
	 * Retorna um DTO com os dados de um funcionário.
	 * 
	 * @param funcionario
	 * @return FuncionarioDto
	 */
	private FuncionarioDto converterParaFuncionarioDto( Funcionario funcionario ) {
		
		FuncionarioDto funcionarioDto = new FuncionarioDto();
		
		funcionarioDto.setId( funcionario.getId() );
		funcionarioDto.setEmail( funcionario.getEmail() );
		funcionarioDto.setNome( funcionario.getNome() );
		
		funcionario.getQtdHorasAlmocoOpt().ifPresent(
				qtdHorasAlmoco -> funcionarioDto.setQtdHorasAlmoco( Optional.of(Float.toString(qtdHorasAlmoco) ) ) );
		
		funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(
				qtdHorasTrabDia -> funcionarioDto.setQtdHorasTrabalhoDia( Optional.of(Float.toString(qtdHorasTrabDia ) ) ) );
		
		funcionario.getValorHoraOpt()
				.ifPresent(valorHora -> funcionarioDto.setValorHora( Optional.of(valorHora.toString() ) ) );

		return funcionarioDto;
	}

}