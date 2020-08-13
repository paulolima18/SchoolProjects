#include "Utils.h"

char * cleanString(char* yytext, int len, char endpoint){
    int end = 0;
    while(yytext[len-end] != endpoint)
        end++;
    yytext[len-end]='\0';
    return yytext;
}
