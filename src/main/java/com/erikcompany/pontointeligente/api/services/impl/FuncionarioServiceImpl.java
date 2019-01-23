package com.erikcompany.pontointeligente.api.services.impl;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erikcompany.pontointeligente.api.entities.Funcionario;
import com.erikcompany.pontointeligente.api.repositories.FuncionarioRepository;
import com.erikcompany.pontointeligente.api.services.FuncionarioService;


@Service //Anotacao do Spring que transforma essa classe em um "service". Ou seja, agora essa classe pode ser injetada em outros lugares.
public class FuncionarioServiceImpl implements FuncionarioService {
	
	
	
	private static final Logger log = LoggerFactory.getLogger(FuncionarioServiceImpl.class);

	
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	
	
	public Funcionario persistir( Funcionario funcionario ) {
		
		log.info("Persistindo funcionário: {}", funcionario);
		
		return funcionarioRepository.save( funcionario );
	}
	
	
	
	public Optional<Funcionario> buscarPorCpf(String cpf) {
		
		log.info("Buscando funcionário pelo CPF {}", cpf);
		
		return Optional.ofNullable( funcionarioRepository.findByCpf(cpf) );
	}
	
	
	
	public Optional<Funcionario> buscarPorEmail(String email) {
		
		log.info("Buscando funcionário pelo email {}", email);
		
		return Optional.ofNullable( funcionarioRepository.findByEmail(email));
	}
	
    
	@Override
	public Optional<Funcionario> buscarPorId(Long id) {
		
	  log.info("Buscando funcionario por ID {}", id);
	  
	  return this.funcionarioRepository.findById(id);
	} 

}