
CC=gcc
CFLAGS=-W -Wall -pedantic -Wcomment
LDFLAGS=
EXEC=entrer sortir

all: $(EXEC)

sortir: sortir.o common.o
	$(CC) -o $@ $^ $(LDFLAGS)

entrer: entrer.o common.o
	$(CC) -o $@ $^ $(LDFLAGS)

%.o: %.c
	$(CC) -o $@ -c $< $(CFLAGS)

clean:
	rm -rf *.o

mrproper: clean
	rm -rf $(EXEC)
