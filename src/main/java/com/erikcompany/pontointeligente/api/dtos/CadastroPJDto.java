package com.erikcompany.pontointeligente.api.dtos;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


//-----Classe DTO responsavel por guardar os dados que chegaram pela requisicao-----//

public class CadastroPJDto {
	
	
	private Long id;
	
	
	@NotEmpty(message = "Nome não pode ser vazio.")                                     //Anotacao de validacao automatica do hibernate.
	@Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.") //Anotacao de validacao automatica do hibernate.
	private String nome;
	
	
	@NotEmpty(message = "Email não pode ser vazio.")                                     //Anotacao de validacao automatica do hibernate.
	@Length(min = 5, max = 200, message = "Email deve conter entre 5 e 200 caracteres.") //Anotacao de validacao automatica do hibernate.
	@Email(message="Email inválido.")                                                    //Anotacao de validacao automatica do hibernate.
	private String email;

	
	@NotEmpty(message = "Senha não pode ser vazia.") //Anotacao de validacao automatica do hibernate.
	private String senha;
	
	
	@NotEmpty(message = "CPF não pode ser vazio.") //Anotacao de validacao automatica do hibernate.
	@CPF(message="CPF inválido")                   //Anotacao de validacao automatica do hibernate.
	private String cpf;
	
	
	@NotEmpty(message = "Razão social não pode ser vazio.")                                      //Anotacao de validacao automatica do hibernate.
	@Length(min = 5, max = 200, message = "Razão social deve conter entre 5 e 200 caracteres.")  //Anotacao de validacao automatica do hibernate.
	private String razaoSocial;
	
	
	@NotEmpty(message = "CNPJ não pode ser vazio.") //Anotacao de validacao automatica do hibernate.
	@CNPJ(message="CNPJ inválido.")                 //Anotacao de validacao automatica do hibernate.
	private String cnpj;

	
	
	
	public CadastroPJDto() {
	}

	
	
	//-------------------------Getters and Setters----------------------//		
	
	public Long getId() {
		
		return id;
	}

	public void setId(Long id) {
		
		this.id = id;
	}

	//------
	

	public String getNome() {
		
		return nome;
	}

	public void setNome( String nome ) {
		
		this.nome = nome;
	}

	
	//------	
	

	public String getEmail() {
		
		return email;
	}

	public void setEmail(String email) {
		
		this.email = email;
	}

	
	//------
	

	public String getSenha(){
		
		return senha;
	}

	public void setSenha(String senha){
		
		this.senha = senha;
	}

	
	//------
	

	public String getCpf() {
		
		return cpf;
	}

	public void setCpf(String cpf) {
		
		this.cpf = cpf;
	}

	
	//------
	
	
	public String getRazaoSocial() {
		
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		
		this.razaoSocial = razaoSocial;
	}

	
	//------

	public String getCnpj() {
		
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		
		this.cnpj = cnpj;
	}

	
	//------
	
	@Override
	public String toString() {
		return "CadastroPJDto ["
				+ "id = " + id + ", "
			    + "nome = " + nome + ", "
			    + "email = " + email + ", "
			    + "senha = " + senha + ", "
			    + "cpf= " + cpf+ ", "
			    + "razaoSocial = " + razaoSocial + ", "
			    + "cnpj =" + cnpj + 
			    "]";
	}

}