EXEC := public2NetLang

CXX := gcc
FLAGS := -g -Ofast
GLIB := `pkg-config --cflags --libs glib-2.0`
INCD := -I ../include

all:
	@cd src/ && $(MAKE)

clean:
	rm public2NetLang
	@cd src/ && $(MAKE) clean

run:
	public2NetLang < $(dir)
