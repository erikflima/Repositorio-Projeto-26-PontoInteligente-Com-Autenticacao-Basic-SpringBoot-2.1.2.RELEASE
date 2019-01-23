package com.erikcompany.pontointeligente.api.services.impl;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erikcompany.pontointeligente.api.entities.Empresa;
import com.erikcompany.pontointeligente.api.repositories.EmpresaRepository;
import com.erikcompany.pontointeligente.api.services.EmpresaService;



@Service //Anotacao do Spring que transforma essa classe em um "service". Ou seja, agora essa classe pode ser injetada em outros lugares.
public class EmpresaServiceImpl implements EmpresaService {

	
	private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);

	
	@Autowired
	private EmpresaRepository empresaRepository;

	
	
	
	@Override
	public Optional<Empresa> buscarPorCnpj(String cnpj) {
		
		log.info("Buscando uma empresa para o CNPJ {}", cnpj);
		
		return Optional.ofNullable( empresaRepository.findByCnpj(cnpj) );
	}

	
	
	@Override
	public Empresa persistir(Empresa empresa) {
	
		log.info("Persistindo empresa: {}", empresa);
		
		return empresaRepository.save(empresa);
	}

}