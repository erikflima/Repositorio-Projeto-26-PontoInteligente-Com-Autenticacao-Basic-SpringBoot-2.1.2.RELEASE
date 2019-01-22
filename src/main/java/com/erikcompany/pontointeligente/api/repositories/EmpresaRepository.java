package com.erikcompany.pontointeligente.api.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.erikcompany.pontointeligente.api.entities.Empresa;


/*
O que eu quero eh criar um objeto que tenha metodos que acessa uma tabela no meu banco de dados, blz!
Entao eu crio um interface, que extend que herda de "JpaRepository" da biblioteca Hibernate-JPA.
Essa "JpaRepository" obriga o herdador a implementar um monte de metodos. 
Eh tipo um esqueminha, pq que vai implementar esse monte de metodos, eh o Spring, quando eu usar a anotacao "@Autowired" em alguma parte do codigo.

   Obs: -> JpaRepository<Classe da tabela que quero me conectar, Tipo da chave primaria da tabela> 
*/
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
	
	
	//@Transcional-> Anotacao que diz para o Hibernate que esse metodo eh so de leitura, isso ajuda a melhorar a performance.
	@Transactional(readOnly = true)
	Empresa findByCnpj(String cnpj);

}