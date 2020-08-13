#include "NetLang.h"
#include <stdio.h>
#include <glib.h>

/* Private functions declarations */

void printThread(ThreadSection t, int ident);
void printThreadFinal(ThreadSection t, int ident);

void printBool(gboolean boolean, int ident);
void printInt(int integer, char* name, int ident);
void printString(char* string, char* name, int ident);
void printText(char* string, int ident);

GSList * threadSList = NULL;

struct threadSection {
    GString * id;
    GString * user;
    GString * date;
    GString * timestamp;
    GString * commentText;
    gint likes;
    gboolean hasReplies;
    gint numberOfReplies;
    GSList * replies;
};

ThreadSection initThread(){
    ThreadSection thread_section = (ThreadSection)g_malloc(sizeof(struct threadSection));

    thread_section->id = g_string_new("");
    thread_section->user = g_string_new("");
    thread_section->date = g_string_new("");
    thread_section->timestamp = g_string_new("");
    thread_section->commentText = g_string_new("");
    thread_section->likes = 0;
    thread_section->hasReplies = FALSE;
    thread_section->numberOfReplies = 0;
    thread_section->replies = NULL;

    return thread_section;
}

ThreadSection newThreadSection(ThreadSection t){
    ThreadSection thread_section = (ThreadSection)g_malloc(sizeof(struct threadSection));

    thread_section->id = g_string_new((t->id)->str);
    thread_section->user = g_string_new((t->user)->str);
    thread_section->date = g_string_new((t->date)->str);
    thread_section->timestamp = g_string_new((t->timestamp)->str);
    thread_section->commentText = g_string_new((t->commentText)->str);
    thread_section->likes = t->likes;
    thread_section->hasReplies = t->hasReplies;
    thread_section->numberOfReplies = t->numberOfReplies;
    thread_section->replies = NULL;

    return thread_section;
}

void printJson(){
    int size = g_slist_length(threadSList);
    printf("{\"commentThread\": [\n");
    int initial_identation = 1;

    for(int i=0;i<size;i++){
        ThreadSection _thread = (ThreadSection)g_slist_nth_data (threadSList, i);
        if(i==size-1)
            printThreadFinal(_thread,initial_identation);
        else
            printThread(_thread, initial_identation);
    }
    printf("]}");
}

void printThreadFinal(ThreadSection t, int ident){

    printText("  {\n", ident);
    printString((t->id)->str, "id", ident);
    printString((t->user)->str, "user", ident);
    printString((t->date)->str, "date", ident);
    printString((t->timestamp)->str, "timestamp", ident);
    printString((t->commentText)->str, "commentText", ident);
    printInt(t->likes, "likes", ident);
    printBool(t->hasReplies, ident);
    printInt(t->numberOfReplies, "numberOfReplies", ident);
    printText("\"replies\": [ ", ident);

    int size = g_slist_length(t->replies);
    for(int i=0;i<size;i++){
        ThreadSection _thread = (ThreadSection)g_slist_nth_data (t->replies, i);

        if(i==size-1)
            printThreadFinal(_thread,ident+1);
        else
            printThread(_thread,ident+1);
    }
    printf("]\n");/*t->replies*/
    printf("    }\n\n\n");
}

void printThread(ThreadSection t,int ident){

    printText("{\n", ident);
    printString((t->id)->str, "id", ident);
    printString((t->user)->str, "user", ident);
    printString((t->date)->str, "date", ident);
    printString((t->timestamp)->str, "timestamp", ident);
    printString((t->commentText)->str, "commentText", ident);
    printInt(t->likes, "likes", ident);

    if(t->hasReplies == FALSE)
        printf("    \"hasReplies\": false,\n");
    else
        printf("    \"hasReplies\": true,\n");

    printInt(t->numberOfReplies, "numberOfReplies", ident);
    printText("\"replies\": [ ", ident);
    int size = g_slist_length(t->replies);
    for(int i=0;i<size;i++){
        ThreadSection _thread = (ThreadSection)g_slist_nth_data (t->replies, i);
        if(i==size-1)
            printThreadFinal(_thread,ident+1);
        else
            printThread(_thread,ident+1);
    }
    printf("]\n");/*t->replies*/
    printf("    },\n\n");
}

void setId(ThreadSection t, char* str){
    g_string_append(t->id,str);
}

void setUser(ThreadSection t, char* str){
    g_string_append(t->user,str);
}

void setDate(ThreadSection t, char* str){
    g_string_append(t->date,str);
}

void setTimeStamp(ThreadSection t, char* str){
    g_string_append(t->timestamp,str);
}

void setComment(ThreadSection t, char* str){
    g_string_append(t->commentText,str);
}

void setLikes(ThreadSection t, gint likes){
    t->likes = likes;
}

void setRepliesBool(ThreadSection t, gboolean hasReplies){
    t->hasReplies = hasReplies;
}

void setNumberReplies(ThreadSection t, gint numberOfReplies){
    t->numberOfReplies = numberOfReplies;
}

void addReply(int index, ThreadSection t){
    ThreadSection x = (ThreadSection)g_slist_nth_data (threadSList, index);
    x->replies = g_slist_append (x->replies, (ThreadSection)newThreadSection(t));
}

void addThread(ThreadSection value){
    threadSList = g_slist_append (threadSList, (ThreadSection)newThreadSection(value));
}

int listSize(){
    return g_slist_length(threadSList);
}


void printString(char* string, char* name, int ident){
    while(ident > 0){
        printf("    ");
        ident--;
    }
    printf("\"%s\": \"%s\",\n", name, string);
}

void printInt(int integer, char* name, int ident){
    while(ident > 0){
        printf("    ");
        ident--;
    }
    printf("\"%s\": \"%d\",\n", name, integer);
}

void printBool(gboolean boolean, int ident){
    while(ident > 0){
        printf("    ");
        ident--;
    }
    if(boolean == FALSE)
        printf("\"hasReplies\": false,\n");
    else
        printf("\"hasReplies\": true,\n");
}

void printText(char* string, int ident){
    while(ident > 0){
        printf("    ");
        ident--;
    }
    printf("%s",string);
}
