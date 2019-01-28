package com.erikcompany.pontointeligente.api.controllers;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erikcompany.pontointeligente.api.dtos.EmpresaDto;
import com.erikcompany.pontointeligente.api.entities.Empresa;
import com.erikcompany.pontointeligente.api.response.ResponsePadronizado;
import com.erikcompany.pontointeligente.api.services.EmpresaService;


@RestController                  //Anotacao do Spring que torna essa classe um endpoint.
@RequestMapping("/api/empresas") //Anotacao do Spring que uso para definir qual sera o caminho do endpoint.
@CrossOrigin(origins = "*")      //Anotacao do Spring que uso para dizer que esse controller pode receber requisicoes de qualquer origem, ou seja, requisicoes de qualquer dominio(url), mas eu poderia restrigir, e colocar quais domininios podem fazer requisicoes para esse controller.
public class EmpresaController {

	
	private static final Logger log = LoggerFactory.getLogger( EmpresaController.class );

	
	@Autowired
	private EmpresaService empresaService;

	
	
	public EmpresaController() {
	}

	
	
	/**Anotacao para gerar JavaDoc*
	 * Retorna uma empresa dado um CNPJ.
	 * 
	 * @param cnpj
	 * @return ResponseEntity<Response<EmpresaDto>>
	 */
	//@GetMapping           -> Anotacao do Spring que defino qual verbo quero utilizar. Poderia ser "@PostMapping" or "@DeleteMapping" or "@PutMapping" or "@PatchMapping".	
	//{cnpj}")              -> Definindo um atributo que recebo atraves da url da requisicao.
	//@PathVariable("cnpj") -> Definindo um atributo que recebo atraves da url da requisicao.
	@GetMapping(value = "/cnpj/{cnpj}") 
	public ResponseEntity< ResponsePadronizado<EmpresaDto> > buscarPorCnpj( @PathVariable("cnpj") String cnpj ) {
		
		
		log.info("Buscando empresa por CNPJ: {}", cnpj);

		
		//Preparando o objeto de resposta padronizada que criei.
		ResponsePadronizado<EmpresaDto> responsePadronizado = new ResponsePadronizado<EmpresaDto>();
		
		
		//Verificar se a empresa ja existe no banco.
		Optional<Empresa> empresa = empresaService.buscarPorCnpj(cnpj);

		
		
		if ( !empresa.isPresent() ) {
			
			log.info("Empresa não encontrada para o CNPJ: {}", cnpj);
			
			responsePadronizado.getErrors().add("Empresa não encontrada para o CNPJ " + cnpj);
		
			
			/*Aqui eu retorno uma instancia da classe "ResponseEntity". O metodo badRequest(), ja deixa esse objeto com status code +400-Bad Request.
			 Esse objeto ja tem um monte de atributos que uma resposta de servico tem, como por exemplo o campo "statusCode".
			 Entao eu uso esse objeto como reposta. Ai o Spring vai transformar esse objeto "ResponseEntity" em um json e retornar.
			*/			
			return ResponseEntity.badRequest().body( responsePadronizado );
		}

		
		responsePadronizado.setConteudoDoResponse( this.converterEmpresaDto( empresa.get() ) );
		
		
		/*Aqui eu retorno uma instancia da classe "ResponseEntity". O metodo ok(), ja deixa esse objeto com status code +200-Ok.
		 Esse objeto ja tem um monte de atributos que uma resposta de servico tem, como por exemplo o campo "statusCode".
		 Entao eu uso esse objeto como reposta. Ai o Spring vai transformar esse objeto "ResponseEntity" em um json e retornar.
		*/
		return ResponseEntity.ok().body(responsePadronizado);
	}

	
	
	
	/**Anotacao para gerar JavaDoc*
	 * Popula um DTO com os dados de uma empresa.
	 * 
	 * @param empresa
	 * @return EmpresaDto
	 */
	private EmpresaDto converterEmpresaDto( Empresa empresa ) {
		
		EmpresaDto empresaDto = new EmpresaDto();
		
		empresaDto.setId( empresa.getId() );
		empresaDto.setCnpj( empresa.getCnpj() );
		empresaDto.setRazaoSocial( empresa.getRazaoSocial() );

		return empresaDto;
	}

}