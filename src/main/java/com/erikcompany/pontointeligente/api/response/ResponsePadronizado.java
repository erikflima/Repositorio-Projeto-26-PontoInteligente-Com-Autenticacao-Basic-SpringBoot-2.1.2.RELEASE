package com.erikcompany.pontointeligente.api.response;
import java.util.ArrayList;
import java.util.List;



//-----Classe que serve para ser a resposta de meus servicoes rest. Serve como uma resposta padrao.-----//
public class ResponsePadronizado<ClasseRecebida> {

	
	private ClasseRecebida conteudoDoResponse;
	private List<String>   errors;

	
	public ResponsePadronizado(){
	}

	
	//-------------------------Getters and Setters----------------------//	

	public ClasseRecebida getConteudoDoResponse() {
		
		return conteudoDoResponse;
	}

	public void setConteudoDoResponse(ClasseRecebida conteudoDoResponse) {
		
		this.conteudoDoResponse = conteudoDoResponse;
	}
	
	
	
	
	
	public List<String> getErrors() {
		
		if (this.errors == null) {
			
			//Nao retorna "null", e sim um objeto vazio.
			this.errors = new ArrayList<String>();
		}
		
		return errors;
	}

	public void setErrors( List<String> errors ) {
		
		this.errors = errors;
	}

}