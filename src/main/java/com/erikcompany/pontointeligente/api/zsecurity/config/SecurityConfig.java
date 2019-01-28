package com.erikcompany.pontointeligente.api.zsecurity.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;




/*Essa classe serve para definir as configuracoes do SpringSecurity no projeto. 
  Aqui eu defino:
   - Quais uri's sao de livre acesso.
   - Quais uri's precisam de autorizacao para ser acessada.
   - Outras coisas.
*/
@Configuration       //Anotacao do Spring, que diz para o Spring que essa eh uma classe de configuracao. Ou seja, uma classe que o Spring vai rodar internamente e fazer algo.
@EnableWebSecurity   //Habilita o Spring security na minha aplicacao. Ou seja, vai comecar a validar todas as requisicoes.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	//1º METODO A SER EXECUTADO. O proprio SpringBoot chama esse metodo sozinho, para configuracao da aplicacao.
	//Aqui eu defino os usuarios e senhas para acessar a aplicacacao. E tambem defino o perfil de permissao de cada usuario.
	@SuppressWarnings("deprecation")
	@Override 
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

			
		/*
		  De onde eu vou validar o "usuario" e "senha" de quem fez a requisicao.
		  Posso dizer que eh no banco de dados. Mas por enquanto vou deixar em memoria, pois tambem colocar isso ai no banco ainda rs.
	    */
		auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())//Defino o enconder que o Spring vai usar. Esse eh o padrao mais simples. Esta com deprecate, mais ainda funciona, se quiser melhorar entao devo procurar a solucao na internet.
		                             .withUser("admin")                                 //Nome do usuario que quero validar.
		                             .password("admin")                                 //Senha do usuario que quero validar.
		                             .roles("ADMIN");                                   //Perfil de Permissoes. Eu que inventei esse nome "ADMIN", mas poderia ser qualquer nome.
		

		auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())//Defino o enconder que o Spring vai usar. Esse eh o padrao mais simples. Esta com deprecate, mais ainda funciona, se quiser melhorar entao devo procurar a solucao na internet.
		                             .withUser("erik")                                  //Nome do usuario que quero validar.
                                     .password("erik")                                  //Senha do usuario que quero validar.
                                     .roles("NORMAL");                                  //Perfil de Permissoes. Eu que inventei esse nome "NORMAL", mas poderia ser qualquer nome.
	}
	
	

	//2º METODO A SER EXECUTADO. O proprio SpringBoot chama esse metodo sozinho, para configuracao da aplicacao.
    //Esse metodo vai configurar as questoes de permissao de acesso as uri's.
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		
		http.authorizeRequests()
	                            .antMatchers("/api/cadastrar-pj").hasAnyRole("NORMAL", "ADMIN")                                 //Todas as request que forem feitas para a uri tal, precisa passar pela autenticacao. E somente usuarios com a role "NORMAL" ou "ADMIN" podem acessar essa uri.
	                            .antMatchers("/api/cadastrar-pf").hasAnyRole("NORMAL", "ADMIN")                                 //Todas as request que forem feitas para a uri tal, precisa passar pela autenticacao. E somente usuarios com a role "NORMAL" ou "ADMIN" podem acessar essa uri.
	                            .antMatchers("/api/empresas/cnpj/{cnpj}").permitAll()                                           //Todas as request que forem feitas para as uri's tal, nao precisazam passar pela autenticacao.                       
	                            .antMatchers("/api/funcionarios/atualizar/{id}").hasAnyRole("NORMAL", "ADMIN")                  //Todas as request que forem feitas para a uri tal, precisa passar pela autenticacao. E somente usuarios com a role "NORMAL" ou "ADMIN" podem acessar essa uri.
	                            .antMatchers("/api/lancamentos/funcionario/listarPorFuncionarioId/{funcionarioId}").permitAll() //Todas as request que forem feitas para as uri's tal, nao precisazam passar pela autenticacao.
	                            .antMatchers("/api/lancamentos/listarPorId/{id}").permitAll()                                   //Todas as request que forem feitas para as uri's tal, nao precisazam passar pela autenticacao.                               
	                            .antMatchers("/api/lancamentos/adicionar").hasAnyRole("NORMAL", "ADMIN")                        //Todas as request que forem feitas para a uri tal, precisa passar pela autenticacao. E somente usuarios com a role "NORMAL" ou "ADMIN" podem acessar essa uri.
	                            .antMatchers("/api/lancamentos/atualizar/{id}").hasAnyRole("NORMAL", "ADMIN")                   //Todas as request que forem feitas para a uri tal, precisa passar pela autenticacao. E somente usuarios com a role "NORMAL" ou "ADMIN" podem acessar essa uri.
	                            .antMatchers("/api/lancamentos/remover/{id}").hasRole("ADMIN")                                  //Todas as request que forem feitas para a uri tal, precisa passar pela autenticacao. E somente usuarios com a role "ADMIN" podem acessar essa uri.
	                            .antMatchers("/swagger-ui.html#").permitAll()                                                   //Todas as request que forem feitas para a uri tal, precisa passar pela autenticacao. E somente usuarios com a role "ADMIN" podem acessar essa uri.
	                            
	                            
	                            
	                            .anyRequest().authenticated()                                                  //Digo que requests para qualquer outro uri, deverao ser validadas pelo Spring Security
								.and()
								.httpBasic()                                                                  //Aqui eu digo o tipo de autenticacao que quero fazer. Nese caso eh do tipo "Basic".
		                        .and()
		                        .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS ) //Digo que todas as requisicoes sao Stateless, ou seja, nao persiste nenhuma informacao na sessao.
		                        .and()                                    
		                        .csrf().disable();                                                            //Desabilitando o csrf. Disabilita o recurso de CSRF (Cross-site request forgery), que eh uma protecao de segurança via tokens utilizados em formularios, mas que nao se faz necessario para um modelo de APIs. Tem haver com colocar mais seguranca contra SQLInjections.
		
								http.headers().cacheControl();                                                //Adiciona recursos de cache padrao do Spring para as requisicoes, que ajudam a otimizar as requisicoes com cache de alguns recursos no lado do cliente.
	}		
	
}//class