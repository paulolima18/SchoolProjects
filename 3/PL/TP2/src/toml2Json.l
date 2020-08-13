%{
#include "y.tab.h"
void yyerror(char* s);

char* change(char* s){
    for(int i=0; s[i];i++){
        if(s[i] == '\n')
            s[i] = ' ';
        if(s[i] == '\\')
            s[i] = '/';
        if(s[i] == '"' || s[i] == '\'')
            s[i] = ' ';
    }
    return s;
}

%}
hex 0x[a-zA-Z_]+
octal 0o[0-7]+
binary 0b[0-1]+
integer [\+\-]?[0-9]+
float [\+\-]?[0-9]+\.[0-9]+
exp ({integer}|{float})(e|E){integer}
inf [\+\-]?inf
nan [\+\-]?nan
separators ([0-9]+_|[0-9]+)+\.([0-9]+_|[0-9]+)+
number ({integer}|{hex}|{octal}|{float}|{binary}|{inf}|{exp}|{nan}|{separators})

time ([0-2][0-9]:[0-5][0-9]:[0-5][0-9])?(\.[0-9]*|[A-Z])?
date [0-9]{4}-[0-3][0-9]-[0-3][0-9]
dateTime {date}T{time}(\-[0-2][0-9]:[0-5][0-9])?
ip [0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}
text [ ]*\"([0-9a-zA-Z :$<>\*\-\'\.\\]|(\\\"))+\"
textAsp [ ]*\'([0-9a-zA-Z :\"$<>\*\-\.\\]|(\\\'))+\'
multString (\"\"\"([^\"]|\n)*\"\"\")|('''([^\']|\n)*''')
bool (true|false)
list \[([0-9]|{bool}|{text}|{ip}|(\n)?|[ \.,\[\]])*\]
variable [a-zA-Z_]+[a-zA-Z_0-9]*
dict \{([0-9]|{bool}|{number}|{text}|{ip}|{variable}|(\n)?|[ \.=,\[\]])*\}
%option noyywrap yylineno
%%

[ \\\t\r]                  { ; }
\n/([^a-zA-Z\[\]\" ]|\n)   { yylval.string = strdup(yytext); return EMPTYLINE; }
{multString}               { yylval.string = change(strdup(yytext)); return DATE; }
#.*                        { ; }
{dateTime}                 { yylval.string = strdup(yytext); return DATE; }
{bool}                     { yylval.boolean = strdup(yytext); return BOOLEAN; }
{ip}                       { yylval.string = strdup(yytext); return IP; }
{number}                   { yylval.number = strdup(yytext); return NUMBER; }
[0-9a-zA-Z]+\.[0-9a-zA-Z]+ { yylval.string = strdup(yytext); return SUBSET; }
{variable}                 { yylval.string = strdup(yytext); return VARIABLE; }
{text}                     { yylval.string = strdup(yytext); return TEXT; }
{textAsp}                  { yylval.string = change(strdup(yytext)); return IP; }
{list}                     { yylval.list = strdup(yytext); return LIST; }
{dict}                     { yylval.list = strdup(yytext); return DICT; }
{time}                     { yylval.string = strdup(yytext); return TIME; }
{date}                     { yylval.string = strdup(yytext); return DATE; }
[=\[\]\{\}]                { return yytext[0]; }
.|\n                       { ; }
<<EOF>>                    { return END_OF_FILE; }

%%