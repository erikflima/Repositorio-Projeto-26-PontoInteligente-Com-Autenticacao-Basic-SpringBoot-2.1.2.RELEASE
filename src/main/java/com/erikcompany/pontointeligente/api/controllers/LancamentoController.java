package com.erikcompany.pontointeligente.api.controllers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.erikcompany.pontointeligente.api.dtos.LancamentoDto;
import com.erikcompany.pontointeligente.api.entities.Funcionario;
import com.erikcompany.pontointeligente.api.entities.Lancamento;
import com.erikcompany.pontointeligente.api.enums.TipoEnum;
import com.erikcompany.pontointeligente.api.response.ResponsePadronizado;
import com.erikcompany.pontointeligente.api.services.FuncionarioService;
import com.erikcompany.pontointeligente.api.services.LancamentoService;



@RestController                                                        //Anotacao do Spring que torna essa classe um endpoint.
@RequestMapping(value="/api/lancamentos", produces="application/json") //Anotacao do Spring que uso para definir qual sera o caminho do endpoint. Digo que recebe json e produso json.
@CrossOrigin(origins = "*")                                            //Anotacao do Spring que uso para dizer que esse controller pode receber requisicoes de qualquer origem, ou seja, requisicoes de qualquer dominio(url), mas eu poderia restrigir, e colocar quais domininios podem fazer requisicoes para esse controller.
public class LancamentoController {

	
	
	private static final Logger log = LoggerFactory.getLogger(LancamentoController.class);
	
	//Definindo de data que quero. Obs: Por padrao, esse eh o formato que o MySQL utiliza.
	private final SimpleDateFormat formatoDaData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	
	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private FuncionarioService funcionarioService;
	
	
	@Value("${paginacao.qtd_de_resultados_que_desejo}") //Anotacao do Spring: Serve para pegar o valor de uma constante que esta la no arquivo "application.proprities
	private int qtdDeResultadosQueDesejo;

	
	
	public LancamentoController() {
	}

	
	
	/**Anotacao para gerar JavaDoc*
	 * Retorna a listagem de lançamentos de um funcionário.
	 * 
	 * @param funcionarioId
	 * @return ResponseEntity<Response<LancamentoDto>>
	 */
    //@GetMapping                    -> Anotacao do Spring que defino qual verbo quero utilizar. Poderia ser "@GetMapping" or "@DeleteMapping" or "@PutMapping" or "@PatchMapping".
	//{funcionarioId}                -> Definindo um atributo que recebo atraves da url da requisicao.
	//@PathVariable("funcionarioId") -> Definindo um atributo que recebo atraves da url da requisicao.
	//@RequestParam                  -> Anotacao que serve para definir os parametros opcionais que recebo no url com aquele esquema "?nomeDoParametro=erik". Posso tambem definir o valor padrao que o parametro vai ter caso eu nao receba ele na url.
	@GetMapping(value = "/funcionario/listarPorFuncionarioId/{funcionarioId}")
	public ResponseEntity< ResponsePadronizado< Page<LancamentoDto> > > listarPorFuncionarioId(	@PathVariable("funcionarioId") Long funcionarioId,
			                                                                                    @RequestParam(value = "posicaoInicial",     defaultValue = "0")    int    posicaoInicial,
			                                                                                    @RequestParam(value = "ordenarPor",         defaultValue = "id")   String ordenarPor,
			                                                                                    @RequestParam(value = "direcaoDaOrdenacao", defaultValue = "DESC") String direcaoDaOrdenacao ){
		
		
		log.info("Buscando lançamentos por ID do funcionário: {}, página: {}", funcionarioId, posicaoInicial);
		
		
		//Preparando o objeto de resposta padronizada que criei.
		ResponsePadronizado< Page<LancamentoDto> > responsePadronizado = new ResponsePadronizado< Page<LancamentoDto> >();

		
        // Aqui eu defino  os parametros do select que vou fazer. Faco isso usando um obj do tipo "PageRequest" do proprio Spring.
		PageRequest parametrosParaAQuery = PageRequest.of( posicaoInicial, qtdDeResultadosQueDesejo, Sort.by(ordenarPor) );
		

	    //Faco o select passando os parametros para a query.
		Page<Lancamento> lancamentosEncontrados = this.lancamentoService.buscarPorFuncionarioId( funcionarioId, parametrosParaAQuery );
		
		
		
		//Converto os lancamentosEncontrados do tipo "Page<Lancamento>" para o tipo "Page<LancamentoDto>" para poder colocar na resposta.
		//Le se: Para cada lancamentoEncontrado, converta-o para lacamentoDto.
		Page<LancamentoDto> lancamentosDto = lancamentosEncontrados.map( lancamento -> this.converterLancamentoDto(lancamento) );

		
		responsePadronizado.setConteudoDoResponse(lancamentosDto);
		
		
		/*Aqui eu retorno uma instancia da classe "ResponseEntity". O metodo ok(), ja deixa esse objeto com status code +200-Ok.
		 Esse objeto ja tem um monte de atributos que uma resposta de servico tem, como por exemplo o campo "statusCode".
		 Entao eu uso esse objeto como reposta. Ai o Spring vai transformar esse objeto "ResponseEntity" em um json e retornar.
		*/
		return ResponseEntity.ok().body(responsePadronizado);
	}

	
	
	
	
	/**Anotacao para gerar JavaDoc*
	 * Retorna um lançamento por ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<LancamentoDto>>
	 */
    //@GetMapping   -> Anotacao do Spring que defino qual verbo quero utilizar. Poderia ser "@GetMapping" or "@DeleteMapping" or "@PutMapping" or "@PatchMapping".
	//@PathVariable -> Definindo um atributo que recebo atraves da url da requisicao.
	//{id}          -> Definindo um atributo que recebo atraves da url da requisicao.
	@GetMapping(value = "/listarPorId/{id}") 
	public ResponseEntity< ResponsePadronizado<LancamentoDto> > listarPorId( @PathVariable("id") Long id) {
		
		
		log.info("Buscando lançamento por ID: {}", id);
		
		
		//Preparando o objeto de resposta padronizada que criei.
		ResponsePadronizado<LancamentoDto> responsePadronizado = new ResponsePadronizado<LancamentoDto>();
		
		//Faco o select no banco.
		Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);

		
		
		if ( !lancamento.isPresent() ){
		
			log.info("Lançamento não encontrado para o ID: {}", id);
			
			
			responsePadronizado.getErrors().add("Lançamento não encontrado para o id " + id);
			
			
			/*Aqui eu retorno uma instancia da classe "ResponseEntity". O metodo badRequest(), ja deixa esse objeto com status code +400-Bad Request.
			 Esse objeto ja tem um monte de atributos que uma resposta de servico tem, como por exemplo o campo "statusCode".
			 Entao eu uso esse objeto como reposta. Ai o Spring vai transformar esse objeto "ResponseEntity" em um json e retornar.
			*/			
			return ResponseEntity.badRequest().body( responsePadronizado );
		}

		
		responsePadronizado.setConteudoDoResponse( this.converterLancamentoDto(lancamento.get() ) );
		
		
		/*Aqui eu retorno uma instancia da classe "ResponseEntity". O metodo ok(), ja deixa esse objeto com status code +200-Ok.
		 Esse objeto ja tem um monte de atributos que uma resposta de servico tem, como por exemplo o campo "statusCode".
		 Entao eu uso esse objeto como reposta. Ai o Spring vai transformar esse objeto "ResponseEntity" em um json e retornar.
		*/
		return ResponseEntity.ok().body(responsePadronizado);
	}

	
	
	
	
	/**Anotacao para gerar JavaDoc*
	 * Adiciona um novo lançamento.
	 * 
	 * @param lancamento
	 * @param resultadoDaValidacao
	 * @return ResponseEntity<Response<LancamentoDto>>
	 * @throws ParseException 
	 */
    //@PostMapping  -> Anotacao do Spring que defino qual verbo quero utilizar. Poderia ser "@GetMapping" or "@DeleteMapping" or "@PutMapping" or "@PatchMapping".
    //Obs: Sempre que o objetivo do endpoint for um "insert", eu devo usar o metodo "POST" por padrao e boa pratica.
	//@RequestBody  -> Essa anotacao faz com que ao receber a requisicao, o que estiver no body da requisicao vai ser jogado dentro do objeto "empresaDtoRecebida", pra que eu posso manipular esse objeto depois. 
    //@Valid        -> Faz com que o parametro seja validado automaticamente pelo hibernate validator(que sao anotacoes), baseado nas anotacoes de validacao que estao na classe do paramentro.
    //BindingResult -> Trabalha junto com a anotacao @Valid. Se o hibernate validator(que sao anotacoes) realizar as validacoes e achar algum erro, ele vai se encarregar de me passar um objeto "BindingResult" ai dentro do metodo. Se nao tiver nenhum erro, o objeto "BindingResult" fica vazio.
	@PostMapping(value = "/adicionar") 
	public ResponseEntity<ResponsePadronizado<LancamentoDto> > adicionar( @Valid @RequestBody LancamentoDto lancamentoDto,
			                                                                                  BindingResult resultadoDaValidacao ) throws ParseException {
		
		
		log.info("Adicionando lançamento: {}", lancamentoDto.toString() );
		
		
		//Preparando o objeto de resposta padronizada que criei.
		ResponsePadronizado<LancamentoDto> responsePadronizado = new ResponsePadronizado<LancamentoDto>();
		
		
		//Executar minhas validacoes especificas.
		validarFuncionario(lancamentoDto, resultadoDaValidacao);
		
		
		//Converto o "lancamentoDto" para o tipo "Lancamento", que eh o tipo que vai ser inserido no banco.
		Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, resultadoDaValidacao);

		
		
		if ( resultadoDaValidacao.hasErrors() ) {
			
			log.error("Erro validando lançamento: {}", resultadoDaValidacao.getAllErrors() );
			
			
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

		
		//Salvo de fato o lancamento no banco.
		lancamento = this.lancamentoService.persistir(lancamento);
		
		
		responsePadronizado.setConteudoDoResponse( this.converterLancamentoDto(lancamento) );
		
		
		/*Aqui eu retorno uma instancia da classe "ResponseEntity". O metodo ok(), ja deixa esse objeto com status code +200-Ok.
		 Esse objeto ja tem um monte de atributos que uma resposta de servico tem, como por exemplo o campo "statusCode".
		 Entao eu uso esse objeto como reposta. Ai o Spring vai transformar esse objeto "ResponseEntity" em um json e retornar.
		*/
		return ResponseEntity.ok().body(responsePadronizado);
	}

	
	
	
	
	/**Anotacao para gerar JavaDoc*
	 * Atualiza os dados de um lançamento.
	 * 
	 * @param id
	 * @param lancamentoDto
	 * @return ResponseEntity<Response<Lancamento>>
	 * @throws ParseException 
	 */
    //@PutMapping   -> Anotacao do Spring que defino qual verbo quero utilizar. Poderia ser "@GetMapping" or "@DeleteMapping" or "@PutMapping" or "@PatchMapping".
    //@RequestBody  -> Essa anotacao faz com que ao receber a requisicao, o que estiver no body da requisicao vai ser jogado dentro do objeto "empresaDtoRecebida", pra que eu posso manipular esse objeto depois. 
    //@Valid        -> Faz com que o parametro seja validado automaticamente pelo hibernate validator(que sao anotacoes), baseado nas anotacoes de validacao que estao na classe do paramentro.
    //BindingResult -> Trabalha junto com a anotacao @Valid. Se o hibernate validator(que sao anotacoes) realizar as validacoes e achar algum erro, ele vai se encarregar de me passar um objeto "BindingResult" ai dentro do metodo. Se nao tiver nenhum erro, o objeto "BindingResult" fica vazio.
	//@PathVariable -> Definindo um atributo que recebo atraves da url da requisicao.
	//{id}          -> Definindo um atributo que recebo atraves da url da requisicao.
	//OBS: Usar o verbo "PUT" sem que for um endpoint de atualizacao.
	@PutMapping(value = "/atualizar/{id}")
	public ResponseEntity< ResponsePadronizado<LancamentoDto> > atualizar( @PathVariable("id") Long id,
			                                                               @Valid @RequestBody LancamentoDto lancamentoDto, 
			                                                                                   BindingResult resultadoDaValidacao ) throws ParseException {
		
		
		log.info("Atualizando lançamento: {}", lancamentoDto.toString() );
		
		
		//Preparando o objeto de resposta padronizada que criei.
		ResponsePadronizado<LancamentoDto> responsePadronizado = new ResponsePadronizado<LancamentoDto>();
		
		
		//Executar minhas validacoes especificas.
		validarFuncionario(lancamentoDto, resultadoDaValidacao);
		
		
		//Defino o lancamento com o "id" do funcionario.
		lancamentoDto.setId( Optional.of(id) );
		
		
		//Converto o "lancamentoDto" para o tipo "Lancamento", que eh o tipo que vai ser atualizado no banco.
		Lancamento lancamento = this.converterDtoParaLancamento( lancamentoDto, resultadoDaValidacao );

		
		
		if ( resultadoDaValidacao.hasErrors() ){
		
			
			log.error("Erro validando lançamento: {}", resultadoDaValidacao.getAllErrors());
			
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

		
		lancamento = this.lancamentoService.persistir(lancamento);
		
		
		responsePadronizado.setConteudoDoResponse( this.converterLancamentoDto(lancamento) );
		
		
		/*Aqui eu retorno uma instancia da classe "ResponseEntity". O metodo ok(), ja deixa esse objeto com status code +200-Ok.
		 Esse objeto ja tem um monte de atributos que uma resposta de servico tem, como por exemplo o campo "statusCode".
		 Entao eu uso esse objeto como reposta. Ai o Spring vai transformar esse objeto "ResponseEntity" em um json e retornar.
		*/
		return ResponseEntity.ok().body( responsePadronizado );
	}

	
	
	
	/**Anotacao para gerar JavaDoc*
	 * Remove um lançamento por ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<Lancamento>>
	 */
    //@DeleteMapping                       -> Anotacao do Spring que defino qual verbo quero utilizar. Poderia ser "@GetMapping" or "@DeleteMapping" or "@PutMapping" or "@PatchMapping".
	//@PathVariable                        -> Definindo um atributo que recebo atraves da url da requisicao.
	//{id}                                 -> Definindo um atributo que recebo atraves da url da requisicao.
	//@PreAuthorize("hasAnyRole('ADMIN')") -> Aqui digo "somente quem tem perfil 'ADMIN', pode acessar esse metodo". Fazendo assim que a requisicao que chegar ate aqui, passe pelos filtros que criei do Spring Security. 
	@DeleteMapping(value = "/remover/{id}")
	public ResponseEntity< ResponsePadronizado<String> > remover( @PathVariable("id") Long id) {
		
		
		log.info("Removendo lançamento: {}", id);
		
		
		//Preparando o objeto de resposta padronizada que criei.
		ResponsePadronizado<String> responsePadronizado = new ResponsePadronizado<String>();
		
		
		Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);

		
		if ( !lancamento.isPresent() ){
		
			log.info("Erro ao remover devido ao lançamento ID: {} ser inválido.", id);
			
			responsePadronizado.getErrors().add("Erro ao remover lançamento. Registro não encontrado para o id " + id);
			
			
			/*Aqui eu retorno uma instancia da classe "ResponseEntity". O metodo badRequest(), ja deixa esse objeto com status code +400-Bad Request.
			 Esse objeto ja tem um monte de atributos que uma resposta de servico tem, como por exemplo o campo "statusCode".
			 Entao eu uso esse objeto como reposta. Ai o Spring vai transformar esse objeto "ResponseEntity" em um json e retornar.
			*/			
			return ResponseEntity.badRequest().body( responsePadronizado );
		}

		//Remove o lancamento de fato do banco.
		this.lancamentoService.remover(id);
		
		
		/*Aqui eu retorno uma instancia da classe "ResponseEntity". O metodo ok(), ja deixa esse objeto com status code +200-Ok.
		 Esse objeto ja tem um monte de atributos que uma resposta de servico tem, como por exemplo o campo "statusCode".
		 Entao eu uso esse objeto como reposta. Ai o Spring vai transformar esse objeto "ResponseEntity" em um json e retornar.
		*/
		return ResponseEntity.ok( new ResponsePadronizado<String>() );
	}

	
	
	
	
	
	/**Anotacao para gerar JavaDoc*
	 * Valida um funcionário, verificando se ele é existente e válido no
	 * sistema.
	 * 
	 * @param lancamentoDto
	 * @param resultadoDaValidacao
	 */
	private void validarFuncionario( LancamentoDto lancamentoDto, BindingResult resultadoDaValidacao ) {
		
		
		//OBS: Verifico se tem um funcionario antes de inserir o lancamento. Pois nao tem como inserir um lancamento se nao tem funcionario pra ele.
		
		
		//Verifico se foi recebido o id do funcionario.
		if ( lancamentoDto.getFuncionarioId() == null ) {
			
			resultadoDaValidacao.addError( new ObjectError("funcionario", "Funcionário não informado.") );
			
			return;
		}

		
		
		log.info("Validando funcionário id {}: ", lancamentoDto.getFuncionarioId() );
		
		
		//Verifico se o funcionario existe no banco.
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId( lancamentoDto.getFuncionarioId() );
		
		
		if ( !funcionario.isPresent() ) {
		
			resultadoDaValidacao.addError( new ObjectError("funcionario", "Funcionário não encontrado. ID inexistente.") );
		}
		
	}

	
	
	/**Anotacao para gerar JavaDoc*
	 * Converte uma entidade lançamento para seu respectivo DTO.
	 * 
	 * @param lancamento
	 * @return LancamentoDto
	 */
	private LancamentoDto converterLancamentoDto( Lancamento lancamento ){
		
		LancamentoDto lancamentoDto = new LancamentoDto();
		
		lancamentoDto.setId( Optional.of(lancamento.getId() ) );
		lancamentoDto.setData( this.formatoDaData.format(lancamento.getData() ) ); //Formato a data no formato que eu escolhi.
		lancamentoDto.setTipo( lancamento.getTipo().toString() );
		lancamentoDto.setDescricao( lancamento.getDescricao() );
		lancamentoDto.setLocalizacao( lancamento.getLocalizacao());
		lancamentoDto.setFuncionarioId( lancamento.getFuncionario().getId() );

		return lancamentoDto;
	}

	
	
	/**Anotacao para gerar JavaDoc*
	 * Converte um LancamentoDto para uma entidade Lancamento.
	 * 
	 * @param lancamentoDto
	 * @param resultadoDaValidacao
	 * @return Lancamento
	 * @throws ParseException 
	 */
	private Lancamento converterDtoParaLancamento( LancamentoDto lancamentoDto, BindingResult resultadoDaValidacao ) throws ParseException {
		
		
		Lancamento lancamentoASerRetornado = new Lancamento();

		
		
		//Verifica o campo "id" do "lancamentoDto"
		if ( lancamentoDto.getId().isPresent() ){
			
			Optional<Lancamento> lancamentoEncontrado = this.lancamentoService.buscarPorId( lancamentoDto.getId().get() );
			
			
			if ( lancamentoEncontrado.isPresent() ) {
			
				//Pegao o lancamento de fato.
				lancamentoASerRetornado = lancamentoEncontrado.get();
				
			} else {
				
				resultadoDaValidacao.addError( new ObjectError("lancamento", "Lançamento não encontrado.") );
			}
			
			
		} else { //Se o lancamentoDto nao tiver o campo "id" preenchido.
			
			lancamentoASerRetornado.setFuncionario( new Funcionario() );
			lancamentoASerRetornado.getFuncionario().setId( lancamentoDto.getFuncionarioId() );
		}

		
		//-------Faco o set dos campos------/
		lancamentoASerRetornado.setDescricao( lancamentoDto.getDescricao() );
		lancamentoASerRetornado.setLocalizacao( lancamentoDto.getLocalizacao() );
		lancamentoASerRetornado.setData( this.formatoDaData.parse( lancamentoDto.getData() ) );

		
		//Verifico se o valor do enum que veio no DTO eh realmente valido.
		if ( EnumUtils.isValidEnum( TipoEnum.class, lancamentoDto.getTipo() ) ) {
		
			lancamentoASerRetornado.setTipo(TipoEnum.valueOf( lancamentoDto.getTipo() ) );
			
		} else {
			
			resultadoDaValidacao.addError( new ObjectError("tipo", "Tipo inválido.") );
		}

		
		
		return lancamentoASerRetornado;
	}

}