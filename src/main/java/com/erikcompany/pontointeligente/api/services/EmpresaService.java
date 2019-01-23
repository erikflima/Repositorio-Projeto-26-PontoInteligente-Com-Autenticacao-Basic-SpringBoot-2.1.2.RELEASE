package com.erikcompany.pontointeligente.api.services;
import java.util.Optional;
import com.erikcompany.pontointeligente.api.entities.Empresa;


//Essa interface so serve para listar os metodos que a classe "EmpresaServiceImpl" vai precisar ter.
public interface EmpresaService {

	
	/**Anotacao para gerar JavaDoc*
	 * Retorna uma empresa dado um CNPJ.
	 * 
	 * @param cnpj
	 * @return Optional<Empresa>
	 */
	Optional<Empresa> buscarPorCnpj(String cnpj);
	
	
	/**Anotacao para gerar JavaDoc*
	 * Cadastra uma nova empresa na base de dados.
	 * 
	 * @param empresa
	 * @return Empresa
	 */
	Empresa persistir(Empresa empresa);
	
}
