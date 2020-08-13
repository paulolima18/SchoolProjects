%{
#include <stdio.h>
#include <glib.h>
#include "NetLang.h"
#include "Utils.h"

ThreadSection thread_section;

GSList * _tempList = NULL;


GString * idBuffer;
GString * commentBuffer;

gint li_closed = 0;
gint li_open = 0;
%}
%x ID USER DATE TIMESTAMP COMMENT TCOMMENT TEXT EMPTYTEXT
%%

<INITIAL>.*"<html>"                                     {
                                                            initThread();
                                                        }

<INITIAL>.*"</html>"                                    {
                                                            printJson();
                                                        }

<INITIAL>.*"</li>"                                      {

                                                            li_closed++;
                                                            if(li_open==li_closed){
                                                                int size = g_slist_length(_tempList);
                                                                for(int i=0; i<size; i++){
                                                                    ThreadSection x = (ThreadSection)g_slist_nth_data (_tempList, i);
                                                                    if(i==0){
                                                                        if(size>1){
                                                                            setRepliesBool(x, TRUE);
                                                                            setNumberReplies(x, size-1);
                                                                        }

                                                                        addThread(x);
                                                                    }
                                                                    else{
                                                                        addReply(listSize()-1, x);
                                                                    }
                                                                }
                                                                g_slist_free(_tempList);
                                                                _tempList = NULL;
                                                            }

                                                            BEGIN INITIAL;
                                                        }
<ID>.*                                                  {
                                                            thread_section = initThread();
                                                            yytext = cleanString(yytext, yyleng, '\"');
                                                            setId(thread_section, yytext);
                                                            idBuffer = g_string_new(yytext);
                                                            BEGIN INITIAL;
                                                        }

<INITIAL>.*"<li class=\"comment\" data-comment-id=\""   {
                                                            li_open++;
                                                            BEGIN ID;
                                                        }

<INITIAL>"<h5 class=\"comment__author\">"[\n\r\t]*      { BEGIN USER; }
<USER>[ ]*"</h5>""\n"*                                  { BEGIN INITIAL; }
<USER><a.*\">                                           { BEGIN TEXT; }
<USER>[ ]*"C"                                           { BEGIN EMPTYTEXT; }

<INITIAL>"<a class=\"comment__permalink\">"             { BEGIN DATE; }
<DATE>.*                                                {
                                                            yytext = cleanString(yytext, yyleng, '<');
                                                            setDate(thread_section, yytext);
                                                            BEGIN INITIAL;
                                                        }

<INITIAL>.*"datetime=\""                                {BEGIN TIMESTAMP;}
<TIMESTAMP>.*                                           {
                                                            yytext = cleanString(yytext, yyleng, '\"');
                                                            setTimeStamp(thread_section, yytext);
                                                            BEGIN INITIAL;
                                                        }

<INITIAL>.*"<p>"[\n\r\t]*                               {
                                                            commentBuffer = g_string_new("");
                                                            BEGIN COMMENT;

                                                        }

<TCOMMENT>.*"</p>".*                                    {
                                                            setComment(thread_section, commentBuffer->str);
                                                            _tempList = g_slist_append (_tempList, (ThreadSection)newThreadSection(thread_section));
                                                            BEGIN INITIAL;
                                                        }
<TCOMMENT>."\n"*                                        {
                                                            if(yytext[0] == '\"') {
                                                                g_string_append_c(commentBuffer,'\\');
                                                                g_string_append_c(commentBuffer,yytext[0]);
                                                            }
                                                            else if(yytext[0] == '\n' || yytext[0] == '\r' || yytext[0] == '\t'){
                                                                g_string_append_c(commentBuffer, ' ');
                                                            }
                                                            else {
                                                                g_string_append_c(commentBuffer,yytext[0]);
                                                            }
                                                        }

<COMMENT>[ ]*                                           { BEGIN TCOMMENT; }


<TEXT>.*\n                                              {
                                                            yytext = cleanString(yytext, yyleng, '<');
                                                            setUser(thread_section, yytext);

                                                            BEGIN USER;
                                                        }
<EMPTYTEXT>.*\n                                         {
                                                            setUser(thread_section, "Conta desactivada por violação das regras de conduta");
                                                            BEGIN USER;
                                                        }

<INITIAL>.|\n                                           {;}

%%

int yywrap(){
    return 1;
}

int main(){
    yylex();
    return 0;
}
