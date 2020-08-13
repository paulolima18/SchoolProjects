#ifndef ___NETLANG_H___
#define ___NETLANG_H___

#include <glib.h>


typedef struct threadSection * ThreadSection;

ThreadSection initThread();
ThreadSection newThreadSection(ThreadSection t);

void printJson();

void setId(ThreadSection t, char* str);
void setUser(ThreadSection t, char* str);
void setDate(ThreadSection t, char* str);
void setLikes(ThreadSection t, gint likes);
void setComment(ThreadSection t, char* str);
void setTimeStamp(ThreadSection t, char* str);
void setRepliesBool(ThreadSection t, gboolean hasReplies);
void setNumberReplies(ThreadSection t, gint numberOfReplies);

void addReply(int index, ThreadSection t);
void addThread(ThreadSection value);

int listSize();

#endif
