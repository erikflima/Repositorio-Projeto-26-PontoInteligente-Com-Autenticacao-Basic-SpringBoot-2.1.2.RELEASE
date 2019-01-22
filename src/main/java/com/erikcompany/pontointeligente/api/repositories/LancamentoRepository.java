package com.erikcompany.pontointeligente.api.repositories;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.erikcompany.pontointeligente.api.entities.Lancamento;


/*
O que eu quero eh criar um objeto que tenha metodos que acessa uma tabela no meu banco de dados, blz!
Entao eu crio um interface, que extend que herda de "JpaRepository" da biblioteca Hibernate-JPA.
Essa "JpaRepository" obriga o herdador a implementar um monte de metodos. 
Eh tipo um esqueminha, pq que vai implementar esse monte de metodos, eh o Spring, quando eu usar a anotacao "@Autowired" em alguma parte do codigo.

   Obs: -> JpaRepository<Classe da tabela que quero me conectar, Tipo da chave primaria da tabela> 
*/


public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

	
	
	//Metodo no qual eu defino o conteudo da query.
	@Transactional //Anotacao que diz para o Hibernate que esse metodo eh so de leitura, isso ajuda a melhorar a performance.
    @Query("SELECT lanc FROM Lancamento lanc   WHERE lanc.funcionario.id = :funcionarioId")
    public List<Lancamento> pegaTodosOsLacamentosDoFuncionario ( @Param("funcionarioId") Long funcionarioId );
	
	
	//Metodo no qual eu defino o conteudo da query.
	@Transactional //Anotacao que diz para o Hibernate que esse metodo eh so de leitura, isso ajuda a melhorar a performance.
    @Query("SELECT lanc FROM Lancamento lanc   WHERE lanc.funcionario.id = :funcionarioId")
	Page<Lancamento> pegaTodosOsLacamentosDoFuncionario( @Param("funcionarioId") Long funcionarioId, Pageable pageable ); //Pageable -> Faz com que o resultado da query venham com paginacao. Exemplo, quero que pegar os resultados da segunda ate a vigesima posicao...

	
	

	
	
}