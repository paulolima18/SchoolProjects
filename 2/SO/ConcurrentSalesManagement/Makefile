all: pm ag ss sc

pm: PM/product_maintenance.c
	gcc -Wall -O2 -Wextra -o pm PM/product_maintenance.c basics.c stack.c

ag: AG/aggregator.c
	gcc -Wall -O2 -Wextra -o ag AG/aggregator.c basics.c

ss: SS/sales_server.c
	gcc -Wall -O2 -Wextra -o ss SS/sales_server.c basics.c stack.c

sc: SC/sales_client.c
	gcc -Wall -O2 -Wextra -o sc SC/sales_client.c basics.c

files:
	rm files/* tmp/*

exec:
	rm pm ag ss sc

clean:
	rm files/* tmp/* pm ag ss sc
