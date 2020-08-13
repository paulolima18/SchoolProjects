EXEC := public2NetLang

CXX := gcc
FLAGS := -g -Ofast
GLIB := `pkg-config --cflags --libs glib-2.0`
INCD := -I ../include
OBJECTS:=$(filter-out lex.yy.c,$(wildcard *.c))

all:
	@flex transformer.l
	$(CXX) -o ../$(EXEC) $(OBJECTS) lex.yy.c $(FLAGS) $(GLIB) $(INCD)

clean:
	rm -rf lex.yy.c


run:
	public2NetLang < ../resources/Publico_extraction_portuguese_comments_4.html
