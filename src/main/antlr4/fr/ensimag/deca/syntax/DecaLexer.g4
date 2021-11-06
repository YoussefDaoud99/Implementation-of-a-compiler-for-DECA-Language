lexer grammar DecaLexer;

options {
   language=Java;
   
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

// Deca lexer rules.

 // Les mots résérvés
 PRINT: 'print';
 PRINTLN: 'println';
 PRINTLNX: 'printlnx';
 PRINTX: 'printx';
 ASM: 'asm';	
 CLASS: 'class';
 EXTENDS: 'extends';
 ELSE: 'else';
 FALSE: 'false';
 IF: 'if';
 INSTANCEOF: 'instanceof';
 NEW: 'new';
 NULL:'null';
 READINT: 'readInt';
 READFLOAT: 'readFloat';
 PROTECTED: 'protected';
 RETURN: 'return';
 THIS: 'this';
 TRUE: 'true';
 WHILE: 'while';

//Lettres
fragment LETTRE: ('a'..'z'|'A'..'Z');

 //Littéraux entiers
fragment DIGIT: '0'..'9';
fragment POSITIVE_DIGIT: '1'..'9';
INT: '0' | POSITIVE_DIGIT DIGIT*;

 // Symboles spéciaux
 SEMI: ';';
 OPARENT: '(';
 CPARENT: ')';
 OBRACE: '{';
 CBRACE: '}';
 COMMA: ',';
 SLASH: '/';
 TIMES: '*';
 LT: '<';
 GT: '>';
 EQUALS: '=';
 PLUS: '+';
 MINUS: '-';
 PERCENT: '%';
 DOT: '.';
 EXCLAM: '!';
 EQEQ: '==';
 NEQ: '!=';
 GEQ: '>=';
 LEQ: '<=';
 AND: '&&';
 OR: '||';
 


// Littéraux flottants
fragment NUM: DIGIT+;
fragment SIGN: ('+' | '-')?;
fragment EXP: ('E' | 'e') SIGN NUM;
fragment DEC: NUM '.' NUM;
fragment FLOATDEC: (DEC | DEC EXP)('F' | 'f')?;
fragment DIGITHEX: '0'..'9' | 'A'..'F' | 'a'..'f';
fragment NUMHEX: DIGITHEX+;
fragment FLOATHEX: ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' )?;
FLOAT: FLOATDEC | FLOATHEX;

// Identificateur se place après les mots reservés et l'exponontielle
IDENT: (LETTRE | '$' | '_')(LETTRE | DIGIT | '$' | '_')*;

 
 // chaines de caracteres
 fragment STRING_CAR: ~('"' | '\\' | '\n');

 STRING: '"' (STRING_CAR | '\\"' | '\\\\')* '"'
 {
 	String s = getText();
 	setText(s.substring(1, s.length()-1).replace("\\\"", "\"").replace("\\\\", "\\")); 
 };
 MULTI_LINE_STRING: '"' (STRING_CAR | '\n' | '\\"' | '\\\\')* '"'
 {
 	String s = getText();
 	setText(s.substring(1, s.length()-1).replace("\\\"", "\"").replace("\\\\", "\\")); 
 };
 

 
 // Commentaires
 fragment COMMENT: '/*' .*? '*/';
 fragment LINE_COMMENT: '//' ~('\n')* ('\n' | EOF);
 
 //Séparateurs
 SEPARATEUR: (COMMENT | LINE_COMMENT | ' ' | '\n' | '\r' | '\t')
 {
 	skip();
 };
 
 // inclusion de fichier
 
 fragment FILENAME: (LETTRE | DIGIT | '.' | '-' | '_')+;
 INCLUDE: '#include' (' ')* '"' FILENAME '"'
 {
 	doInclude(getText());
 };
 
 // erreurs lexicales
 BADSTRING : '"' ( STRING_CAR | '\\"' | '\\\\' )*
 {
  Unterminated_String(); 
 } ;
 BADINCLUDE : '#include' (' ')* STRING
{ 
  Bad_Include();
};

BADCOMMENT: '/*' .*? 
{
	Bad_Comment();
};

DEFAULT : . { Illegal_Character();} ;

