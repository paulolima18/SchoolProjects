%{
#include <stdio.h>
int yylex();
void yyerror(char* s);
%}
%union { char* string; char* number; char* boolean; char* list; }
%token VARIABLE TEXT DATE TIME EMPTYLINE IP NUMBER BOOLEAN LIST DICT SUBSET END_OF_FILE
%type <string> VARIABLE TEXT DATE TIME EMPTYLINE IP SUBSET
%type <boolean> BOOLEAN
%type <list> LIST DICT
%type <number> NUMBER
%%

Start: { printf("{\n"); } TomlParse { printf("}\n"); }
     ;
     
TomlParse: 
         | Item TomlParse
         ;

Item: '[' VARIABLE ']' { printf("\t\"%s\":{\n",$2); } Group
    | Equals { printf("\n"); } END_OF_FILE
    | Equals { printf(",\n"); }
    | EMPTYLINE
    ;

Group: '[' SUBSET ']' { printf("\t\"%s\":{\n",$2); } SubGroup
     | Equals { printf(",\n"); } Group
     | Equals { printf("\n\t}\n"); } END_OF_FILE
     | Equals { printf("\n\t},\n"); }  EMPTYLINE
     ;

SubGroup: '[' SUBSET ']' { printf("\t\"%s\":{\n",$2); } SubGroup
        | Equals { printf(",\n"); } SubGroup
        | Equals { printf("\n\t}\n"); } END_OF_FILE { printf("\t}\n"); }
        | Equals { printf("\n\t}"); } EMPTYLINE IsOrNot
        ;

IsOrNot: '[' SUBSET ']' { printf(",\n\t\"%s\":{\n",$2); } SubGroup
       | EMPTYLINE
       | '[' VARIABLE ']' { printf("\n},\n\t\"%s\":{\n",$2); } Group
       ;

Equals: VARIABLE '=' TEXT    { printf("\t\"%s\":%s",$1,$3); }
      | VARIABLE '=' IP      { printf("\t\"%s\":\"%s\"",$1,$3); }
      | VARIABLE '=' DATE    { printf("\t\"%s\":\"%s\"",$1,$3); }
      | VARIABLE '=' TIME    { printf("\t\"%s\":\"%s\"",$1,$3); }
      | VARIABLE '=' BOOLEAN { printf("\t\"%s\":%s",$1,$3); } 
      | VARIABLE '=' LIST    { printf("\t\"%s\":%s",$1,$3); } 
      | VARIABLE '=' NUMBER  { printf("\t\"%s\":\"%s\"",$1,$3); } 
      | VARIABLE '=' DICT    { printf("\t\"%s\":\"%s\"",$1,$3); }
      ;
%%

int main(){
   yyparse();
   return 0;
}

void yyerror(char* s){
   extern int yylineno;
   extern char* yytext;
   fprintf(stderr, "Linha %d: %s (%s)\n", yylineno, s, yytext);
}
