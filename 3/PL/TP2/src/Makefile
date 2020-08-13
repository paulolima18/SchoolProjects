tomlJson: toml2Json.l toml2Json.y
	flex toml2Json.l
	yacc -d toml2Json.y
	cc lex.yy.c y.tab.c -lm -o ../toml2Json

clean:
	rm tomlJson y.tab.* lex.yy.c
