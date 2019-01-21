package com.erikcompany.pontointeligente.api.enums;

/*Enum eh um esquema pra guardar uma lista de valores de constantes. Mas facil criar esse enum do que criar uma tabela no banco de dados so pra guardar isso.

Na real serve so para concentrar constantes em um lugar so e nao sair espalhando constantes em 
varias classes. Ai se  eu precisar usar o valor de alguma constante, basta chamar pelo ENUM.  */


public enum TipoEnum {
	
	//Classe que representa um tipo de hora de lancamento que um usuario pode fazer:
	
	INICIO_TRABALHO,
	TERMINO_TRABALHO,
	INICIO_ALMOCO,
	TERMINO_ALMOCO,
	INICIO_PAUSA,
	TERMINO_PAUSA

}