package com.erikcompany.pontointeligente.api.services.impl;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.erikcompany.pontointeligente.api.entities.Lancamento;
import com.erikcompany.pontointeligente.api.repositories.LancamentoRepository;
import com.erikcompany.pontointeligente.api.services.LancamentoService;



@Service  //Anotacao do Spring que transforma essa classe em um "service". Ou seja, agora essa classe pode ser injetada em outros lugares.
public class LancamentoServiceImpl implements LancamentoService {

	
	private static final Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);

	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	

	
	@Cacheable("cacheDoErik") //Aqui eu ativo o uso de cache nesse metodo. As configuracoes estao no arquivo ehcache.xml na pasta src/main/resources. Ai, quando esse metodo for chamado, o resultado dele vai ser armazenado na memoria do cache, e a proxima chamada que for feita, vai ser retornado o valor salvo no cache, melhorando assim a performance e rapidez da aplicacao.
	public Optional<Lancamento> buscarPorId(Long id) {
	
		log.info("Buscando um lançamento pelo ID {}", id);
		
		return lancamentoRepository.findById(id);
	}
	
	
	
	public Page<Lancamento> buscarPorFuncionarioId( Long funcionarioId, PageRequest pageRequest ) {
	
		log.info("Buscando lançamentos para o funcionário ID {}", funcionarioId);
		
		return lancamentoRepository.findByFuncionarioId( funcionarioId, pageRequest );
	}
	
	
	
	@CachePut("cacheDoErik") //Aqui eu ativo o uso de cache nesse metodo. As configuracoes estao no arquivo ehcache.xml na pasta src/main/resources. Ai, quando esse metodo for chamado, ele vai atualizar a informacao de cache atual que esta em memoria. Posso ver o video "25.Aumentando a performance com o EhCache" para relembrar melhor.
	public Lancamento persistir(Lancamento lancamento) {
		
		log.info("Persistindo o lançamento: {}", lancamento);
		
		return lancamentoRepository.save(lancamento);
	}
	
	
	
	public void remover(Long id) {

		log.info("Removendo o lançamento ID {}", id);
		
		this.lancamentoRepository.deleteById(id);
	}

}