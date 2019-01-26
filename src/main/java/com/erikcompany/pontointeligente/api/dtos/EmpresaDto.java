package com.erikcompany.pontointeligente.api.dtos;


//-----Classe DTO responsavel por guardar os dados que chegaram pela requisicao-----//
public class EmpresaDto {
	
	
	private Long   id;
	private String razaoSocial;
	private String cnpj;

	
	public EmpresaDto() {
	}

	
	//-------------------------Getters and Setters----------------------//	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	//----
	
	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	
	//----
	
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	//----
	
	@Override
	public String toString() {
		
		return "EmpresaDto [id=" + id + ", razaoSocial=" + razaoSocial + ", cnpj=" + cnpj + "]";
	}

}
