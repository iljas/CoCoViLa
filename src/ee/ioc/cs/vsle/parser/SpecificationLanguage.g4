grammar SpecificationLanguage;


options {
  language = Java;
}

@members{
	
}


metaInterfase
    :	'specification' IDENTIFIER (superMetaInterface)?
    	'{' specification '}'
	;
	
superMetaInterface
	:	'super' classType (',' classType)*
	;
	
specification
    :	((variableDeclaration /*| constantDeclaration */ | variableAssignment 
    		| axiom | goal | aliasDeclaration | aliasDefinition | equation /*binding*/ ) ';' )*
    ;
	
variableDeclaration
	:	variableModifier? type variableDeclarator (',' variableDeclarator)*
	;
	
variableModifier
	:	'static'	# staticVariable
	|	'const'		# constantVariable
	;

variableDeclarator
	:	IDENTIFIER ('=' variableInitializer)?	# variableDeclaratorInitializer
	|	IDENTIFIER ('=' variableAssigner)		# variableDeclaratorAssigner 
	;

//constantDeclaration
//	:	'const' type constantDeclarator (',' constantDeclarator)*
//	;
//	
//constantDeclarator
//	:	IDENTIFIER '=' variableInitializer
//	;

variableAssignment
	:	variableIdentifier '=' variableAssigner
	;
	
axiom
	:	( inputVariables = variableIdentifierList | subtaskList | (subtaskList ',' inputVariables = variableIdentifierList) ) '->' outputVariables = variableIdentifierList (',' exceptionList)? '{' method = IDENTIFIER '}'
	;
	
//simpleAxiom
//	:	variableIdentifierList? '->' variableIdentifier
//	;
//
//subtaskAxiom
//	:	subtask (',' subtask)* (',' variableIdentifierList)? '->' variableIdentifierList //Is list OK?
//	
//	;
	
subtask
	:	'[' (context = classType '|-')? inputVariables = variableIdentifierList '->' outputVariables = variableIdentifierList ']'
	;
	
subtaskList
	:	subtask (',' subtask)*
	;

exceptionList
	:	'(' classType ')' (',' '(' classType ')')*
	;
	
goal
	:	inputVariables = variableIdentifierList? '->' outputVariables = variableIdentifierList	
	;
	
aliasDeclaration
	:	'alias' ('(' type')')? IDENTIFIER ('=' aliasStructure)?
	;
	
aliasStructure
	:	'(' ( variableAlias=variableIdentifierList | '*.'wildcardAlias=IDENTIFIER )')'
	;

aliasDefinition
	:	variableIdentifier '=' aliasStructure
	;

type
    :   (classType | primitiveType) ('[' ']')*
    ;

classType
    :   IDENTIFIER ('.' IDENTIFIER )*
    ;

primitiveType
    :   'boolean'
    |   'char'
    |   'byte'
    |   'short'
    |   'int'
    |   'long'
    |   'float'
    |   'double'
    ;

equation
	:	left = expression '=' right = expression
	;

expression
	:	term
	|	'(' expression ')'
	|	'-' expression
	|	IDENTIFIER '(' expression ')'
	|	left = expression op=('*' | '/') right = expression
	|	left = expression op=('+' | '-') right = expression
    |   expression '^' expression
	;
	
term
    :	NUMBER
    |	variableIdentifier
    ;

array
    :   '{' (inArrayVariableAssigner (',' inArrayVariableAssigner)* )? '}'
    ;
    
inArrayVariableAssigner
	:	variableAssigner | variableInitializer
	;

variableAssigner
    :   array
    |   'new' classType '(' expression (',' expression)* ')'
    |	STRING
    |	'true'
    |	'false'
    ;
    
variableInitializer
    :   array
    |   expression
    ;

variableIdentifier
	:	IDENTIFIER ('.' IDENTIFIER )* ALIAS_ELEMENT_REF*  ('.' variableIdentifier)?
	;
	
//aliasElementRef
//	:	'.' ALIAS_ELEMENT_REF
//	;
	
variableIdentifierList
	:	variableIdentifier (',' variableIdentifier )*
	;

NUMBER
    : INTEGER ('.' INTEGER)?
    ;
	
INTEGER
    : '0'..'9'+
    ;
	
STRING :	'"' (ESC|.)*? '"' ;
fragment ESC :	'\\"' | '\\\\' ;

IDENTIFIER
	:	LETTER LETTER_OR_DNUMBER*
	;
fragment LETTER : [a-zA-Z$_];
fragment LETTER_OR_DNUMBER : [a-zA-Z0-9$_];

ALIAS_ELEMENT_REF
	:	'.' NUMBER+
	;

WS : [ \t\r\n]+ -> skip ;
COMMENT : '//' .*? '\r'? '\n' -> skip;
BLOCK_COMMENT : '/*' .*? '*/' -> skip;

JAVA_BEFORE_SPEC : .*? '/*@' -> skip;
JAVA_AFTER_SPEC : '@*/' .*? -> skip;   	