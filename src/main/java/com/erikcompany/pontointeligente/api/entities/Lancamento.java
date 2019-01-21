package com.erikcompany.pontointeligente.api.entities;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.erikcompany.pontointeligente.api.enums.TipoEnum;



@Entity                                           //Isso eh uma anotacao do Hibernate, no qual estou dizendo que essa classe reprenta um tabela do banco de dados.
@Table(name = "lancamento")                       //Nome da tabela que essa classe representa.
public class Lancamento implements Serializable { //Serializable eh uma frescura do java. Essa implamentacao ajuda a performance do java se internamente ele precisar serializar um objeto dessa classe.
	
	
	private static final long serialVersionUID = 6524560251526772839L;  //Numero gerado pelo Java automaticamente.

	
	
	@Id                                            //Anotacao do Hibernate, no qual estou dizendo que o campo "id" eh o campo chave da tabela.
    @GeneratedValue(strategy=GenerationType.AUTO)  //Anotacao do Hibernate que diz que o campo "id" campo eh Auto-incrementado.E o "Strategy" eh pra dizer como que vai ser esse auto-incremento, que nesse caso vai ser automatico.
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)    //Digo que quero guardar data e a hora no banco.
	@Column(name = "data", nullable = false)
	private Date data;
	
	@Column(name = "descricao", nullable = true)
	private String descricao;
	
	@Column(name = "localizacao", nullable = true)
	private String localizacao;
	
	@Column(name = "data_criacao", nullable = false)
	private Date dataCriacao;
	
	@Column(name = "data_atualizacao", nullable = false)
	private Date dataAtualizacao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo", nullable = false)
	private TipoEnum tipo;
	
                                        // "@ManyToOne"            -> Anotacao do Hibernate, que diz que ha um relacionamento "1:N". Ou seja, "1 FUNCIONARIO pode ter Varios LANCAMENTOS. E 1 LANCAMENTO pertence a 1 FUNCIONARIO.  OBS: Sempre bom olhar o diagrama em desenho para entender 100%.
    @ManyToOne(fetch = FetchType.EAGER) // "fetch=FetchType.EAGER" -> Opcao do Hibernate, Eh pra dizer para hibernate que se eu fizer uma leitura do objeto "LANCAMENTO", tambem deve ser carregado o "FUNCIONARIO" associado. Ou seja, eh o contrario do "FetchType.LAZY".	
    private Funcionario funcionario;  

	

	public Lancamento() {
	}

	
	//-------Getters and Setters-------//
	
	
	public Long getId() {
		
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	//------
	

	public Date getData() {
		
		return data;
	}

	public void setData(Date data) {
		
		this.data = data;
	}

	//------
	

	public String getDescricao() {
		
		return descricao;
	}

	public void setDescricao(String descricao) {
		
		this.descricao = descricao;
	}
	
	//------
	

	public String getLocalizacao() {
		
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
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
	

	public TipoEnum getTipo() {
		
		return tipo;
	}

	public void setTipo(TipoEnum tipo) {
		this.tipo = tipo;
	}

	//------
	

	public Funcionario getFuncionario() {
		
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		
		this.funcionario = funcionario;
	}
	
	
	
	//-------Metodos adicionais------//	
	
	@PreUpdate//Anotacao do Hibernate, que diz para hibernate executar esse metodo antes de realizar um "UPDATE"(que na real eh o metodo persist() ) na tabela.
    public void preUpdate() {
		
        dataAtualizacao = new Date();
    }
     
    @PrePersist//Anotacao do Hibernate, que diz para hibernate executar esse metodo antes de realizar um "INSERT"(que na real eh o metodo persist() ) na tabela.
    public void prePersist() {
    	
        final Date atual = new Date();
        
        dataCriacao     = atual;
        dataAtualizacao = atual;
    }

	//------
    
    
	@Override
	public String toString() {
		
		return "\nLancamento "
				+ "[id             = " + id + ", "
				+ "data            = " + data + ", "
				+ "descricao       = " + descricao + ", "
				+ "localizacao     = " + localizacao
				+ ",dataCriacao    = " + dataCriacao + ", "
				+ "dataAtualizacao = " + dataAtualizacao + ", "
				+ "tipo            = " +tipo+", "
				+ "funcionario     = " + funcionario + "]";
	}

}