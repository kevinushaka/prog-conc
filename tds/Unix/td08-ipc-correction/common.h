#include <stdio.h>
#include <sys/ipc.h>	// Pour ftok (), IPC_CREAT, IPC_RMID
#include <sys/shm.h> 	// Pour shmget (), shmat (), shmdt (), shmctl ()
#include <sys/sem.h>    // Pour les fonctions IPC des sémaphores

/** MESSAGE **/
void abandon(char message[]);

/* Generation de la cle a partir de 'chemin_fichier' et identificateur' */
int creer_cle(char *chemin_fichier, int identificateur);
  
/** MEMOIRE PARTAGEE **/

/* Fonction creant un segment de memoire partagee de 'taille' octets et dont 
 * la cle est construite a partir de 'chemin_fichier' et de 'identificateur'. 
 * Cette fonction retourne l'identificateur du segment.
 */ 
int shm_creation (key_t cle, int taille);

/** SEMAPHORE **/
typedef int semaphore;

semaphore creer_sem (key_t key, int val_init);
void detruire_sem(semaphore sem);

void down(semaphore sem);
void up(semaphore sem);
