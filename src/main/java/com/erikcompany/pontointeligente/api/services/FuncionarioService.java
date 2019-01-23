package com.erikcompany.pontointeligente.api.services;
import java.util.Optional;

import com.erikcompany.pontointeligente.api.entities.Funcionario;


//Essa interface so serve para listar os metodos que a classe "FuncionarioServiceImpl" vai precisar ter.
public interface FuncionarioService {
	
	/**Anotacao para gerar JavaDoc.
	 * Persiste um funcionário na base de dados.
	 * 
	 * @param funcionario
	 * @return Funcionario
	 */
	Funcionario persistir(Funcionario funcionario);
	
	
	
	/**Anotacao para gerar JavaDoc.
	 * Busca e retorna um funcionário dado um CPF.
	 * 
	 * @param cpf
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorCpf(String cpf);
	
	
	
	/**Anotacao para gerar JavaDoc.
	 * Busca e retorna um funcionário dado um email.
	 * 
	 * @param email
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorEmail(String email);
	
	
	
	/**Anotacao para gerar JavaDoc.
	 * Busca e retorna um funcionário por ID.
	 * 
	 * @param id
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorId(Long id);

}
