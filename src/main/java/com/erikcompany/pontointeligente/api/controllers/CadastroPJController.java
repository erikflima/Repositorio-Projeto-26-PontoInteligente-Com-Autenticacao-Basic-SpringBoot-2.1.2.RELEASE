package com.erikcompany.pontointeligente.api.controllers;
import java.security.NoSuchAlgorithmException;
import java.util.List;
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
import com.erikcompany.pontointeligente.api.dtos.CadastroPJDto;
import com.erikcompany.pontointeligente.api.entities.Empresa;
import com.erikcompany.pontointeligente.api.entities.Funcionario;
import com.erikcompany.pontointeligente.api.enums.PerfilEnum;
import com.erikcompany.pontointeligente.api.response.ResponsePadronizado;
import com.erikcompany.pontointeligente.api.services.EmpresaService;
import com.erikcompany.pontointeligente.api.services.FuncionarioService;
import com.erikcompany.pontointeligente.api.utils.PasswordUtils;


@RestController                                                         //Anotacao do Spring que torna essa classe um endpoint.
@RequestMapping(value="/api/cadastrar-pj", produces="application/json") //Anotacao do Spring que uso para definir qual sera o caminho do endpoint. Digo que recebe json e produso json.
@CrossOrigin(origins = "*")                                             //Anotacao do Spring que uso para dizer que esse controller pode receber requisicoes de qualquer origem, ou seja, requisicoes de qualquer dominio(url), mas eu poderia restrigir, e colocar quais domininios podem fazer requisicoes para esse controller.
public class CadastroPJController {

	
	private static final Logger log = LoggerFactory.getLogger( CadastroPJController.class );

	
	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private EmpresaService empresaService;

	
	
	public CadastroPJController() {
	}

	
	
	/**Anotacao para gerar JavaDoc*
	 * Cadastra uma pessoa jurídica no sistema.
	 * 
	 * @param cadastroPJDto
	 * @param resultadoDaValidacao
	 * @return ResponseEntity<Response<CadastroPJDto>>
	 * @throws NoSuchAlgorithmException
	 */
    //@PostMapping  -> Anotacao do Spring que defino qual verbo quero utilizar. Poderia ser "@GetMapping" or "@DeleteMapping" or "@PutMapping" or "@PatchMapping".
    //@RequestBody  -> Essa anotacao faz com que ao receber a requisicao, o que estiver no body da requisicao vai ser jogado dentro do objeto "empresaDtoRecebida", pra que eu posso manipular esse objeto depois. 
    //@Valid        -> Faz com que o parametro seja validado automaticamente pelo hibernate validator(que sao anotacoes), baseado nas anotacoes de validacao que estao na classe do paramentro.
    //BindingResult -> Trabalha junto com a anotacao @Valid. Se o hibernate validator(que sao anotacoes) realizar as validacoes e achar algum erro, ele vai se encarregar de me passar um objeto "BindingResult" ai dentro do metodo. Se nao tiver nenhum erro, o objeto "BindingResult" fica vazio.
	@PostMapping
	public ResponseEntity< ResponsePadronizado<CadastroPJDto> > cadastrar( @Valid @RequestBody CadastroPJDto cadastroPJDto,
			                                                                                   BindingResult resultadoDaValidacao ) throws NoSuchAlgorithmException {
		
		
		log.info("Cadastrando PJ: {}", cadastroPJDto.toString());
		
		
		//Preparando o objeto de resposta padronizada que criei.
		ResponsePadronizado<CadastroPJDto> responsePadronizado = new ResponsePadronizado<CadastroPJDto>();


		
		//Verifico se a empresa e o funcionario que vieram na entrada, ja existem no banco de dados.
		validarDadosExistentes( cadastroPJDto, resultadoDaValidacao );
		
		
		Empresa     empresa     = this.converterDtoParaEmpresa( cadastroPJDto );
		Funcionario funcionario = this.converterDtoParaFuncionario( cadastroPJDto, resultadoDaValidacao) ;

		
		
		
		//Verificando a validacao dos dados de entrada feita automaticamente pelo Hibernate, com base nas anotacoes feitas na classe "CadastroPJDto".
		if ( resultadoDaValidacao.hasErrors() ) {
			
			
			log.error("Erro validando dados de cadastro PJ: {}", resultadoDaValidacao.getAllErrors() );
			
			
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
			return ResponseEntity.badRequest().body(responsePadronizado);
		}

		
		
		//Persisto a Empresa e Funcionario no banco.
		empresaService.persistir(empresa);
		
		funcionario.setEmpresa(empresa);
		
		funcionarioService.persistir(funcionario);

		
		
		responsePadronizado.setConteudoDoResponse( this.converterCadastroPJDto(funcionario) );
		
		/*Aqui eu retorno uma instancia da classe "ResponseEntity". O metodo ok(), ja deixa esse objeto com status code +200-Ok.
		 Esse objeto ja tem um monte de atributos que uma resposta de servico tem, como por exemplo o campo "statusCode".
		 Entao eu uso esse objeto como reposta. Ai o Spring vai transformar esse objeto "ResponseEntity" em um json e retornar.
		*/
		return ResponseEntity.ok().body(responsePadronizado);
	}

	
	
	
	/**Anotacao para gerar JavaDoc*
	 * Verifica se a empresa ou funcionário já existem na base de dados.
	 * 
	 * @param cadastroPJDto
	 * @param resultadoDaValidacao
	 */
	private void validarDadosExistentes( CadastroPJDto cadastroPJDto, BindingResult resultadoDaValidacao ) {
		
		
		empresaService.buscarPorCnpj( cadastroPJDto.getCnpj() )
				.ifPresent( empresaEncontrada -> resultadoDaValidacao.addError( new ObjectError("empresa", "Empresa já existente.") ) );

		funcionarioService.buscarPorCpf(cadastroPJDto.getCpf())
				.ifPresent( funcionarioEncontrado -> resultadoDaValidacao.addError( new ObjectError("funcionario", "CPF já existente.") ) );

		funcionarioService.buscarPorEmail(cadastroPJDto.getEmail())
				.ifPresent( funcionarioEncontrado -> resultadoDaValidacao.addError( new ObjectError("funcionario", "Email já existente.") ) );
	}

	
	
	
	/**Anotacao para gerar JavaDoc*
	 * Converte os dados do DTO para empresa.
	 * 
	 * @param cadastroPJDto
	 * @return Empresa
	 */
	private Empresa converterDtoParaEmpresa( CadastroPJDto cadastroPJDto ) {
		
		Empresa empresa = new Empresa();
		
		empresa.setCnpj( cadastroPJDto.getCnpj() );
		empresa.setRazaoSocial( cadastroPJDto.getRazaoSocial() );

		return empresa;
	}

	
	
	/**Anotacao para gerar JavaDoc*
	 * Converte os dados do DTO para funcionário.
	 * 
	 * @param cadastroPJDto
	 * @param result
	 * @return Funcionario
	 * @throws NoSuchAlgorithmException
	*/
	private Funcionario converterDtoParaFuncionario( CadastroPJDto cadastroPJDto, 
			                                         BindingResult result        ) throws NoSuchAlgorithmException {

		Funcionario funcionario = new Funcionario();
		
		funcionario.setNome(cadastroPJDto.getNome());
		funcionario.setEmail(cadastroPJDto.getEmail());
		funcionario.setCpf(cadastroPJDto.getCpf());
		funcionario.setPerfil( PerfilEnum.ROLE_ADMIN ); //Perfil de admnistrador
		funcionario.setSenha( PasswordUtils.gerarBCrypt( cadastroPJDto.getSenha() ) ); //Definir a senha e criptografar.

		return funcionario;
	}

	
	
	
	/**Anotacao para gerar JavaDoc*
	 * Popula o DTO de cadastro com os dados do funcionário e empresa.
	 * 
	 * @param funcionario
	 * @return CadastroPJDto
	 */
	private CadastroPJDto converterCadastroPJDto(Funcionario funcionario) {
		
		
		CadastroPJDto cadastroPJDto = new CadastroPJDto();
		
		cadastroPJDto.setId( funcionario.getId() );
		cadastroPJDto.setNome( funcionario.getNome() );
		cadastroPJDto.setEmail( funcionario.getEmail() );
		cadastroPJDto.setCpf( funcionario.getCpf() );
		cadastroPJDto.setRazaoSocial( funcionario.getEmpresa().getRazaoSocial() );
		cadastroPJDto.setCnpj( funcionario.getEmpresa().getCnpj() );

		return cadastroPJDto;
	}

}