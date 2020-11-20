#include <stdio.h>
#include <stdlib.h>
#include <sys/ipc.h>	// Pour ftok (), IPC_CREAT, IPC_RMID
#include <sys/shm.h> 	// Pour shmget (), shmat (), shmdt (), shmctl ()
#include <sys/sem.h>    // Pour les fonctions IPC des sémaphores

#include "common.h"

/** MESSAGE **/

void abandon(char message[]) {
  perror(message);
  exit(EXIT_FAILURE);
}

/* Generation de la cle a partir de 'chemin_fichier' et identificateur' */
int creer_cle(char *chemin_fichier, int identificateur) {
  key_t cle;
  
  if ((cle = ftok (chemin_fichier, identificateur)) < 0)
    abandon("créer clé");
  
  return cle;
} 

/** MEMOIRE PARTAGEE **/

/* Fonction creant un segment de memoire partagee de 'taille' octets et dont 
 * la cle est construite a partir de 'chemin_fichier' et de 'identificateur'. 
 * Cette fonction retourne l'identificateur du segment.
 */ 
int shm_creation (key_t cle, int taille) {
  int shmid;

  printf("Création d'un segment de mémoire partagée de taille %d\n", taille);
    
  /* Creation d'un segment de memoire partagee de 'taille' octets. */
  if ((shmid = shmget(cle, taille, IPC_CREAT | IPC_EXCL | 0666))<0) {
    /* le segment de mémoire partagé existait déjà, on va le réutiliser */
     if ((shmid = shmget(cle, taille, IPC_CREAT| 0666))<0)
      abandon("créer mémoire partagée"); 

     printf("Id de la mémoire partagée réutilisée %d\n", shmid);
  } else
    printf("Id de la mémoire partagée créée %d\n", shmid);

  return shmid ;
}

/** SEMAPHORE **/

semaphore creer_sem (key_t cle, int val_init) {
  /* création d’un tableau de 1 sémaphore initialisé à val_init */
  semaphore sem;
  int r;

  printf("Création d'un sémaphore initialisé à %d\n", val_init);

  if ((sem = semget (cle, 1, IPC_CREAT | IPC_EXCL | 0666)) < 0) {
    /* le sémaphore existait déjà, on va le réutiliser */
    if ((sem = semget (cle, 1, IPC_CREAT | 0666)) < 0)
      abandon ("creer_sem");

    printf("Id du sémaphore réutilisé %d\n", sem);
  } else {
    printf("Id du sémaphore créé %d\n", sem);
    if ((r = semctl (sem, 0, SETVAL, val_init)) < 0)
      abandon ("Initialisation du sémaphore créé");
  }
  
  return sem;
}

void detruire_sem(semaphore sem) {
  printf("Destruction du sémaphore %d\n", sem);
  
  if ((semctl (sem, 0, IPC_RMID, 0)) != 0)
    abandon("detruire_sem");
}

void changer_sem(semaphore sem, int val) {
  struct sembuf sb[1];
  sb[0].sem_num = 0;
  sb[0].sem_op = val;
  sb[0].sem_flg = 0;

  printf("changer_sem %d %d\n", sem, val);
  if (semop (sem, sb, 1) != 0)
    abandon("changer_sem");

}

void down(semaphore sem) {
  changer_sem(sem, -1);
}

void up(semaphore sem) {
  changer_sem(sem, 1);
} 

