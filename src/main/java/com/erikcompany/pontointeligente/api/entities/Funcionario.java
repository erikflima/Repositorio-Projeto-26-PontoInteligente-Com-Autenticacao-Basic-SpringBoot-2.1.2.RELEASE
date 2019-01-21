package com.erikcompany.pontointeligente.api.entities;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.erikcompany.pontointeligente.api.enums.PerfilEnum;


@Entity                                            //Isso eh uma anotacao do Hibernate, no qual estou dizendo que essa classe reprenta um tabela do banco de dados.
@Table(name = "funcionario")                       //Nome da tabela que essa classe representa.
public class Funcionario implements Serializable { //Serializable eh uma frescura do java. Essa implamentacao ajuda a performance do java se internamente ele precisar serializar um objeto dessa classe.

	
	private static final long serialVersionUID = -5754246207015712518L; //Numero gerado pelo Java automaticamente.
	
	
	
	@Id                                            //Anotacao do Hibernate, no qual estou dizendo que o campo "id" eh o campo chave da tabela.
    @GeneratedValue(strategy=GenerationType.AUTO)  //Anotacao do Hibernate que diz que o campo "id" campo eh Auto-incrementado.E o "Strategy" eh pra dizer como que vai ser esse auto-incremento, que nesse caso vai ser automatico.
	private Long   id;
	
	@Column(name = "nome", nullable = false)
	private String nome;
	
	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name = "senha", nullable = false)
	private String senha;
	
	@Column(name = "cpf", nullable = false)
	private String cpf;
	
	@Column(name = "valor_hora", nullable = true)	
	private BigDecimal valorHora;
	
	@Column(name = "qtd_horas_trabalho_dia", nullable = true)
	private Float   qtdHorasTrabalhoDia;
	
	@Column(name = "qtd_horas_almoco", nullable = true)
	private Float   qtdHorasAlmoco;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "perfil", nullable = false)
	private PerfilEnum perfil;
	
	
	@Column(name = "data_criacao", nullable = false)
	private Date dataCriacao;
	
	@Column(name = "data_atualizacao", nullable = false)
	private Date dataAtualizacao;
	
	
                                        // "@ManyToOne"            -> Anotacao do Hibernate, que diz que ha um relaiconamento "1:N". Ou seja, "1 EMPRESA pode ter Varios funcionarios. E 1 FUNCIONARIO pertence a 1 empresa. OBS: Sempre bom olhar o diagrama em desenho para entender 100%.
    @ManyToOne(fetch = FetchType.EAGER) // "fetch=FetchType.EAGER" -> Opcao do Hibernate, Eh pra dizer para hibernate que se eu fizer uma leitura do objeto "FUNCIONARIO", tambem deve ser carregado a "EMPRESA" associado a ele. Ou seja, eh o contrario do "FetchType.LAZY".	
	private Empresa  empresa;
    
    
    // "OneToMany"               -> Anotacao do Hibernate, que diz que ha um relaiconamento "1:N". Ou seja, "1 FUNCIONARIO pode ter Varios LANCAMENTOS. E 1 LANCAMENTO pertence a 1 FUNCIONARIO.  OBS: Sempre bom olhar o diagrama em desenho para entender 100%.
    // "fetch=FetchType.EAGER"   -> Opcao do Hibernate, Eh pra dizer para hibernate que se eu fizer uma leitura do objeto "FUNCIONARIO", tambem deve ser carregado os "LANCAMENTOS" associados a ele. Ou seja, eh o contrario do "FetchType.LAZY".
    // "cascade=CascadeType.ALL" -> Opcao do Hibernate, Eh pra dizer para o hibernate que se eu deletar um objeto "FUNCIONARIO" do banco de dados, entao vai ser deletado automaticamente os "LANCAMENTOS" daquele "FUNCIONARIO".    
	@OneToMany(mappedBy = "funcionario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Lancamento> lancamentos;
	
	
	
	
	public Funcionario() {
	}

	
	//---------------Metodos adicionais-----------------//
	
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

	public void setNome(String nome) {
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
	
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	//------	

	public BigDecimal getValorHora() {
		return valorHora;
	}
	
	public void setValorHora(BigDecimal valorHora) {
		
		this.valorHora = valorHora;
	}
	
	
	@Transient //Anotacao que serve para dizer ao Hibernate, que esse metodo nao tem nada a ver com o relacionamento da classe e tabela. Esse metodo esta aqui por alguma outra razao particular minha.
	public Optional<BigDecimal> getValorHoraOpt() {
		
		return Optional.ofNullable(valorHora);
	}



	//------
	
	public Float getQtdHorasTrabalhoDia() {
		
		return qtdHorasTrabalhoDia;
	}
	
	
	@Transient //Anotacao que serve para dizer ao Hibernate, que esse metodo nao tem nada a ver com o relacionamento da classe e tabela. Esse metodo esta aqui por alguma outra razao particular minha.
	public Optional<Float> getQtdHorasTrabalhoDiaOpt() {
		
		return Optional.ofNullable(qtdHorasTrabalhoDia);
	}

	
	public void setQtdHorasTrabalhoDia(Float qtdHorasTrabalhoDia) {
		
		this.qtdHorasTrabalhoDia = qtdHorasTrabalhoDia;
	}

	//------
	
	public Float getQtdHorasAlmoco() {
		return qtdHorasAlmoco;
	}
	
	
	@Transient //Anotacao que serve para dizer ao Hibernate, que esse metodo nao tem nada a ver com o relacionamento da classe e tabela. Esse metodo esta aqui por alguma outra razao particular minha.
	public Optional<Float> getQtdHorasAlmocoOpt() {
		
		return Optional.ofNullable(qtdHorasAlmoco);
	}

	
	public void setQtdHorasAlmoco(Float qtdHorasAlmoco) {
		
		this.qtdHorasAlmoco = qtdHorasAlmoco;
	}

	//------
	

	public PerfilEnum getPerfil() {
		
		return perfil;
	}

	public void setPerfil(PerfilEnum perfil) {
		this.perfil = perfil;
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
	

	public String getSenha() {
		
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}

	//------
	
	public Empresa getEmpresa() {
		
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	//------
	
	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	
	
	
	//----------Metodos adicionais--------------//
	
	
	@PreUpdate //Anotacao do Hibernate, que diz para hibernate executar esse metodo antes de realizar um "UPDATE"(que na real eh o metodo persist() ) na tabela.
    public void preUpdate() {
		
        dataAtualizacao = new Date();
    }
     
    @PrePersist //Anotacao do Hibernate, que diz para hibernate executar esse metodo antes de realizar um "INSERT"(que na real eh o metodo persist() ) na tabela.
    public void prePersist() {
    	
        final Date atual = new Date();
        dataCriacao      = atual;
        dataAtualizacao  = atual;
    }

	//------
    
	@Override
	public String toString() {

		return "Funcionario:"
				+ "[id                 = " + id+ ", "
				+ "nome                = " + nome+ ","
				+ "email               = " + email+ ", "
			    + "senha               = " + senha+ ", "
				+ "cpf                 = " + cpf+ ", "
				+ "valorHora           = " + valorHora+ ", "
				+ "qtdHorasTrabalhoDia = " + qtdHorasTrabalhoDia+ ", "
				+ "qtdHorasAlmoco      = " + qtdHorasAlmoco+ ", "
				+ "perfil              = " + perfil+ ","
				+ "dataCriacao         = " + dataCriacao+ ", "
				+ "dataAtualizacao     = " + dataAtualizacao+ ","
				+ "empresa             = " + empresa+ "]";
	}

}