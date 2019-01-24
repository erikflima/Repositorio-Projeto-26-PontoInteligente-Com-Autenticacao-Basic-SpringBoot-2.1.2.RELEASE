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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.erikcompany.pontointeligente.api.dtos.CadastroPFDto;
import com.erikcompany.pontointeligente.api.entities.Empresa;
import com.erikcompany.pontointeligente.api.entities.Funcionario;
import com.erikcompany.pontointeligente.api.enums.PerfilEnum;
import com.erikcompany.pontointeligente.api.response.ResponsePadronizado;
import com.erikcompany.pontointeligente.api.services.EmpresaService;
import com.erikcompany.pontointeligente.api.services.FuncionarioService;
import com.erikcompany.pontointeligente.api.utils.PasswordUtils;



@RestController                      //Anotacao do Spring que torna essa classe um endpoint.
@RequestMapping("/api/cadastrar-pf") //Anotacao do Spring que uso para definir qual sera o caminho do endpoint.
@CrossOrigin(origins = "*")          //Anotacao do Spring que uso para dizer que esse controller pode receber requisicoes de qualquer origem, ou seja, requisicoes de qualquer dominio(url), mas eu poderia restrigir, e colocar quais domininios podem fazer requisicoes para esse controller.
public class CadastroPFController {

	
	private static final Logger log = LoggerFactory.getLogger(CadastroPFController.class);
	
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private FuncionarioService funcionarioService;

	
	
	
	
	public CadastroPFController() {
	}

	
	
	/**Anotacao para gerar JavaDoc*
	 * Cadastra um funcionário pessoa física no sistema.
	 * 
	 * @param cadastroPFDto
	 * @param resultadoDaValidacao
	 * @return ResponseEntity<Response<CadastroPFDto>>
	 * @throws NoSuchAlgorithmException
	 */
    //@PostMapping  -> Anotacao do Spring que defino qual verbo quero utilizar. Poderia ser "@GetMapping" or "@DeleteMapping" or "@PutMapping" or "@PatchMapping".
    //@RequestBody  -> Essa anotacao faz com que ao receber a requisicao, o que estiver no body da requisicao vai ser jogado dentro do objeto "empresaDtoRecebida", pra que eu posso manipular esse objeto depois. 
    //@Valid        -> Faz com que o parametro seja validado automaticamente pelo hibernate validator(que sao anotacoes), baseado nas anotacoes de validacao que estao na classe do paramentro.
    //BindingResult -> Trabalha junto com a anotacao @Valid. Se o hibernate validator(que sao anotacoes) realizar as validacoes e achar algum erro, ele vai se encarregar de me passar um objeto "BindingResult" ai dentro do metodo. Se nao tiver nenhum erro, o objeto "BindingResult" fica vazio.
	@PostMapping
	public ResponseEntity< ResponsePadronizado<CadastroPFDto> > cadastrar( @Valid @RequestBody CadastroPFDto cadastroPFDto,
			                                                                                   BindingResult resultadoDaValidacao ) throws NoSuchAlgorithmException {
		
		
		log.info("Cadastrando PF: {}", cadastroPFDto.toString() );
		
		
		//Preparando o objeto de resposta padronizada que criei.
		ResponsePadronizado<CadastroPFDto> responsePadronizado = new ResponsePadronizado<CadastroPFDto>();

		
		//Verifico se a empresa e o funcionario que vieram na entrada, ja existem no banco de dados.
		validarDadosExistentes( cadastroPFDto, resultadoDaValidacao );
		
		
		Funcionario funcionario = this.converterDtoParaFuncionario( cadastroPFDto, resultadoDaValidacao );

		
		
		//Verificando a validacao dos dados de entrada feita automaticamente pelo Hibernate, com base nas anotacoes feitas na classe "cadastroPFDto".
		if ( resultadoDaValidacao.hasErrors() ) {
			
			log.error( "Erro validando dados de cadastro PF: {}", resultadoDaValidacao.getAllErrors() );
			
			
			//A variavel "resultadoDaValidacao" eh enviado pela validacao que o Spring fez, baseado nas anotacoes de validacao que estao la na classe "cadastroPFDto".
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
		
		
		
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj( cadastroPFDto.getCnpj() );
		
		empresa.ifPresent( emp -> funcionario.setEmpresa(emp) );
		
		
		this.funcionarioService.persistir(funcionario);

		
		responsePadronizado.setConteudoDoResponse( this.converterCadastroPFDto( funcionario) );
		
		
		
		/*Aqui eu retorno uma instancia da classe "ResponseEntity". O metodo ok(), ja deixa esse objeto com status code +200-Ok.
		 Esse objeto ja tem um monte de atributos que uma resposta de servico tem, como por exemplo o campo "statusCode".
		 Entao eu uso esse objeto como reposta. Ai o Spring vai transformar esse objeto "ResponseEntity" em um json e retornar.
		*/
		return ResponseEntity.ok().body(responsePadronizado);
	}

	
	
	
	
	/***Anotacao para gerar JavaDoc*
	 * Verifica se a empresa está cadastrada e se o funcionário não existe na base de dados.
	 * 
	 * @param cadastroPFDto
	 * @param resultadoDaValidacao
	 */
	private void validarDadosExistentes(CadastroPFDto cadastroPFDto, BindingResult resultadoDaValidacao) {
		
		
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj( cadastroPFDto.getCnpj() );
		
		
		//Se a empresa nao exisitir no banco de dados.
		if ( !empresa.isPresent() ) {
			
			resultadoDaValidacao.addError(new ObjectError("empresa", "Empresa não cadastrada."));
		}
		
		
		this.funcionarioService.buscarPorCpf( cadastroPFDto.getCpf() )
			.ifPresent(func -> resultadoDaValidacao.addError( new ObjectError("funcionario", "CPF já existente.") ) );

		
		
		this.funcionarioService.buscarPorEmail( cadastroPFDto.getEmail() )
			.ifPresent(func -> resultadoDaValidacao.addError( new ObjectError("funcionario", "Email já existente.") ) );
	}

	
	
	
	
	/**Anotacao para gerar JavaDoc*
	 * Converte os dados do DTO para funcionário.
	 * 
	 * @param cadastroPFDto
	 * @param resultadoDaValidacao
	 * @return Funcionario
	 * @throws NoSuchAlgorithmException
	 */
	private Funcionario converterDtoParaFuncionario( CadastroPFDto cadastroPFDto, BindingResult resultadoDaValidacao ) throws NoSuchAlgorithmException {
		
		Funcionario funcionario = new Funcionario();
		
		funcionario.setNome( cadastroPFDto.getNome() );
		funcionario.setEmail( cadastroPFDto.getEmail() );
		funcionario.setCpf( cadastroPFDto.getCpf() );
		funcionario.setPerfil( PerfilEnum.ROLE_USUARIO );
		funcionario.setSenha( PasswordUtils.gerarBCrypt( cadastroPFDto.getSenha() ) ); //Gerar a senha com criptografia.
		
		
		//Verificar os campos nao obrigatorios.
		cadastroPFDto.getQtdHorasAlmoco()
				.ifPresent( qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco( Float.valueOf(qtdHorasAlmoco) ) );
		
		cadastroPFDto.getQtdHorasTrabalhoDia()
				.ifPresent( qtdHorasTrabDia -> funcionario.setQtdHorasTrabalhoDia( Float.valueOf( qtdHorasTrabDia ) ) );
		
		cadastroPFDto.getValorHora()
		        .ifPresent(valorHora -> funcionario.setValorHora( new BigDecimal( valorHora ) ) );

		return funcionario;
	}

	
	
	
	
	/**Anotacao para gerar JavaDoc*
	 * Popula o DTO de cadastro com os dados do funcionário e empresa.
	 * 
	 * @param funcionario
	 * @return CadastroPFDto
	 */
	private CadastroPFDto converterCadastroPFDto(Funcionario funcionario) {
		
		
		CadastroPFDto cadastroPFDto = new CadastroPFDto();
		
		cadastroPFDto.setId(    funcionario.getId() );
		cadastroPFDto.setNome(  funcionario.getNome() );
		cadastroPFDto.setEmail( funcionario.getEmail() );
		cadastroPFDto.setCpf(   funcionario.getCpf() );
		cadastroPFDto.setCnpj(  funcionario.getEmpresa().getCnpj() );
		
		
		//Verificar os campos nao obrigatorios.
		funcionario.getQtdHorasAlmocoOpt()
		        .ifPresent(qtdHorasAlmoco -> cadastroPFDto.setQtdHorasAlmoco( Optional.of( Float.toString(qtdHorasAlmoco) ) ) );
		
		funcionario.getQtdHorasTrabalhoDiaOpt()
		         .ifPresent( qtdHorasTrabDia -> cadastroPFDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabDia))));
		
		funcionario.getValorHoraOpt()
				.ifPresent(valorHora -> cadastroPFDto.setValorHora(Optional.of(valorHora.toString())));

		
		return cadastroPFDto;
	}
	
}