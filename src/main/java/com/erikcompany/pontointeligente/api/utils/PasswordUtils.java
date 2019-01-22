package com.erikcompany.pontointeligente.api.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;




public class PasswordUtils {

	private static final Logger log = LoggerFactory.getLogger(PasswordUtils.class);

	
	public PasswordUtils() {
	}

	
	
	/*Gera um hash utilizando o BCrypt.*/
	public static String gerarBCrypt(String senhaRecebidaSemCriptografia) {
		
		
		if (senhaRecebidaSemCriptografia == null) {
			
			return senhaRecebidaSemCriptografia;
		}

		
		log.info("Gerando hash com o BCrypt.");
		
		//Objeto que serve para fazer criptografia.
		BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
		
		
		//Criptografando a senha e transformando ela em um hash.
		String senhaCriptografadaEmFormatoHash = bCryptEncoder.encode(senhaRecebidaSemCriptografia);
		
		
		return senhaCriptografadaEmFormatoHash;
	}

}