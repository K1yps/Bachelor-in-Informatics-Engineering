CC=gcc
CFLAGS= -Wall $(pkg-config --libs glib-2.0)

manager: main.o
	$(CC) main.o -o manager

main.o: main.c catalogoclientes.o catalogoprodutos.o

catalogoclientes.o: catalogoclientes.c catalogoclientes.h

catalogoprodutos.o: catalogoprodutos.c catalogoprodutos.h

clean:
	rm *.o manager
