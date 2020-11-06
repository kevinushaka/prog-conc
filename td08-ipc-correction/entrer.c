#include <stdio.h> 	// Pour printf ()
#include <stdlib.h>	// Pour exit(), NULL
#include <unistd.h> 	// Pour pause ()
#include <fcntl.h> 	// Pour open (), O_CREAT O_WRONLY
#include <signal.h>	// Pour signal ()
#include <sys/types.h>	// Pour key_t
#include <stdio.h>
#include "common.h"

int shmid; 		// Identification du segment
int *shmadr; 		// Adresse d'attachement du segment
semaphore vide;         // sémaphore initialisé au nombres de places vide
semaphore plein;        // sémaphore initialisé au nombre de places pleine
semaphore mutex;        // sémaphore mutex pour section critique

# define NB_PLACES (20)

/* Fonction executee a la reception du signal SIGINT */
void traitant_sigint() {
  struct shmid_ds buf ;

  printf("\n");
  printf("Détachement du bloc de mémoire partagée\n");
  shmdt (shmadr); 			// Detachement du segment

  /* On recherche des informations sur le segment partagé */
  if (shmctl(shmid, IPC_STAT, &buf) == -1)
    abandon("Erreur lors de l'utilsation de shmctl") ;
  
  if (buf.shm_nattch==0) {
    /* Plus personne n'utilise la mémoire partagée, on va la détruire */
    
    printf("Destruction du bloc de mémoire partagée %d\n", shmid);
    shmctl (shmid, IPC_RMID, NULL);
    
    /* La destruction du segment a eut lieu, on peut détruire les sémaphores */
    printf("Destruction des sémaphores\n");
    detruire_sem(vide);
    detruire_sem(plein);
    detruire_sem(mutex);
  } else {
    printf("Reste %d utilisateur(s) de la mémoire partagée %d\n", buf.shm_nattch, shmid);
  }
  exit (0); 
} 

int main() {
  /* Creation de la clé */
  key_t cle = creer_cle("./common.h", 10);
  printf("La clé vaut %d\n", cle);
  
  /* Creation du segment de memoire partagee de taille un entier. */
  shmid = shm_creation(cle, sizeof(int)); 
  
  /* Attachement du segment et recuperation de son adresse. */
  shmadr = (int *) shmat(shmid, NULL, 0); 
  
 /* Création de deux sémaphores :
     un sémaphore initialisé au nombres de places vide
     un sémaphore initialisé au nombre de places pleine
  */
  vide = creer_sem(cle, NB_PLACES);
  plein = creer_sem(cle+1, 0); // Un sémaphore étant identifié par sa clé, on ne peut pas prendre la même
  mutex = creer_sem(cle+2, 1); // Un sémaphore étant identifié par sa clé, on ne peut pas prendre la même
  
  /* Deroutement de SIGINT et endormissement jusqu'a reception d'un signal. */
  printf ("Création du traitant SIGINT\n"); 
  signal (SIGINT, traitant_sigint);

  /* C'est le processus entre qui initialise la mémoire partage,
     donc sort doit attendre que l'initialisation est été faite.
     On utilise pour cela le sémaphore : plein
  */

  /* Initialisation a 20 de l'entier contenu dans le segment. */
  *shmadr = NB_PLACES; 
  printf ("parking : identificateur=%d, places=%d.\n", shmid, *shmadr); 
  up(plein);
 
  /* On ne fait que rentrer : on consomme donc une place vide */
  printf("Nombre de place vide restante %d\n", *shmadr); // lecture non suivie de test ou de modification, la section critique n'est pas nécessaire.
  while (1) {
    down(vide); // On entre, il y a une place vide de moins
    up(plein); // On est rentré, il y a une place pleine de plus

    down(mutex); // On va modifier le nombre de place, il faut donc être en section critique
    *shmadr = (*shmadr)-1;
    printf("Nombre de place vide restante %d\n", *shmadr);
    up(mutex);

    /* On attend un peu avant de recommencer */
    sleep(1); // on se bloque, hors section critique
  } 
  
}
