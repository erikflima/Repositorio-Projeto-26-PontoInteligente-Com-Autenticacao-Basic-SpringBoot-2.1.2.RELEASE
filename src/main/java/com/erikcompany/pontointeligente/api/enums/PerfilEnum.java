package com.erikcompany.pontointeligente.api.enums;


/*Enum eh um esquema pra guardar uma lista de valores de constantes. Mas facil criar esse enum do que criar uma tabela no banco de dados so pra guardar isso.

Na real serve so para concentrar constantes em um lugar so e nao sair espalhando constantes em 
varias classes. Ai se  eu precisar usar o valor de alguma constante, basta chamar pelo ENUM.  */
public enum PerfilEnum {
	
	
	//representa os "perfies" de acesso de um usuario aos dados do banco de dados.
	ROLE_ADMIN,   //Representacao do perfil de "administrador" de acesso ao dados do banco de dados.
	ROLE_USUARIO; //Representacao do perfil de "normal" de acesso ao dados do banco de dados.
	
}