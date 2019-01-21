package com.erikcompany.pontointeligente.api.entities;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;



@Entity                                          //Isso eh uma anotacao do Hibernate, no qual estou dizendo que essa classe reprenta um tabela do banco de dados.
@Table(name = "empresa")                         //Nome da tabela que essa classe representa.
public class Empresa implements Serializable {   //Serializable eh uma frescura do java. Essa implamentacao ajuda a performance do java se internamente ele precisar serializar um objeto dessa classe.

	
	private static final long serialVersionUID = 3960436649365666213L; //Numero gerado pelo Java automaticamente.
	
	
	
	@Id                                             //Anotacao do Hibernate, no qual estou dizendo que o campo "id" eh o campo chave da tabela.
    @GeneratedValue(strategy=GenerationType.AUTO)   //Anotacao do Hibernate que diz que o campo "id" campo eh Auto-incrementado.E o "Strategy" eh pra dizer como que vai ser esse auto-incremento, que nesse caso vai ser de um em um.	
	private Long id;
	
	@Column(name = "razao_social", nullable = false)
	private String  razaoSocial;
	
	@Column(name = "cnpj", nullable = false)
	private String  cnpj;
		
	@Column(name = "data_criacao", nullable = false)
	private Date   dataCriacao;

	@Column(name = "data_atualizacao", nullable = false)	
	private Date   dataAtualizacao;

	
    // "OneToMany"               -> Anotacao do Hibernate, que diz que ha um relaiconamento "1:N". Ou seja, "1 EMPRESA pode ter Varios funcionarios. E 1 FUNCIONARIO pertence a 1 empresa.  OBS: Sempre bom olhar o diagrama em desenho para entender 100%.
    // "fetch=FetchType.EAGER"   -> Opcao do Hibernate, Eh pra dizer para hibernate que se eu fizer uma leitura do objeto "EMPRESA", tambem deve ser carregado os "FUNCIOANRIOS" associados a ela. Ou seja, eh o contrario do "FetchType.LAZY".
    // "cascade=CascadeType.ALL" -> Opcao do Hibernate, Eh pra dizer para o hibernate que se eu deletar um objeto "EMPRESA" do banco de dados, entao vai ser deletado automaticamente os "FUNCIONARIOS" daquela "EMPRESA.
	@OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL)	
	private List<Funcionario> funcionarios;
	
	
	
	public Empresa() {
	}

	

	//-----------Getters and Setters-------------//
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	

	public Date getDataCriacao() {
		
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		
		this.dataCriacao = dataCriacao;
	}

	//------
	

	public Date getDataAtualizacao() {
		
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		
		this.dataAtualizacao = dataAtualizacao;
	}

	//------
	

	public List<Funcionario> getFuncionarios() {
		
		return funcionarios;
	}

	
	public void setFuncionarios(List<Funcionario> funcionarios) {
		
		this.funcionarios = funcionarios;
	}
	
	//------
	
	@PreUpdate  //Anotacao do Hibernate, que diz para hibernate executar esse metodo antes de realizar um "UPDATE"(que na real eh o metodo persist() ) na tabela.
    public void preUpdate() {
		
        dataAtualizacao = new Date();
    }
    
	
	
    @PrePersist  //Anotacao do Hibernate, que diz para hibernate executar esse metodo antes de realizar um "INSERT"(que na real eh o metodo persist() ) na tabela.
    public void prePersist() {
    	
        final Date atual = new Date();
        dataCriacao      = atual;
        dataAtualizacao  = atual;
    }

	//------
    
	@Override
	public String toString() {
		
		return "\n Imprimindo o objeto Empresa: \n"       +
			   " > id = "              +id+ "\n"          +
			   " > razaoSocial = "     +razaoSocial+ "\n" +
			   " > cnpj = "            +cnpj+ "\n"        +
			   " > dataCriacao = "     +dataCriacao+"\n"  +
			   " > dataAtualizacao = " +dataAtualizacao+ "\n\n";
	}
	
}