package com.erikcompany.pontointeligente.api.services;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.erikcompany.pontointeligente.api.entities.Lancamento;



public interface LancamentoService {

	
	/**Anotacao para gerar JavaDoc.
	 * Retorna uma lista paginada de lançamentos de um determinado funcionário.
	 * 
	 * @param funcionarioId
	 * @param pageRequest
	 * @return Page<Lancamento>
	 */
	Page<Lancamento> buscarPorFuncionarioId( Long funcionarioId, PageRequest pageRequest );
	
	
	
	/**Anotacao para gerar JavaDoc.
	 * Retorna um lançamento por ID.
	 * 
	 * @param id
	 * @return Optional<Lancamento>
	 */
	Optional<Lancamento> buscarPorId(Long id);
	
	
	
	/**Anotacao para gerar JavaDoc.
	 * Persiste um lançamento na base de dados.
	 * 
	 * @param lancamento
	 * @return Lancamento
	 */
	Lancamento persistir( Lancamento lancamento );
	

	
	/**Anotacao para gerar JavaDoc.
	 * Remove um lançamento da base de dados.
	 * 
	 * @param id
	 */
	void remover( Long id );
	
}