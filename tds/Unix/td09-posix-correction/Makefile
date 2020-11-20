
CC=gcc
CFLAGS=-W -Wall -pedantic -Wcomment
LDFLAGS=
EXEC=parking

all: $(EXEC)

parking: parking.o
	$(CC) -o $@ $^ $(LDFLAGS)

%.o: %.c
	$(CC) -o $@ -c $< $(CFLAGS)

clean:
	rm -rf *.o

mrproper: clean
	rm -rf $(EXEC)
