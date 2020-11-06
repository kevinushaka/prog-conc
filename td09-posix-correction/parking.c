#include <stdio.h> 	// Pour printf ()
#include <stdlib.h>	// Pour exit(), NULL
#include <unistd.h> 	// Pour pause ()
#include <pthread.h>    // Pour les threads posix

# define NB_PLACES (20)
int places_vides = NB_PLACES; // partagé entre les deux threads, contient le nombre de places vides

// Le verrou associé à la variable condition plein
static pthread_mutex_t mutex_plein = PTHREAD_MUTEX_INITIALIZER;
// La variable condition parking plein
static pthread_cond_t cond_plein = PTHREAD_COND_INITIALIZER;

// Le verrou associé à la variable condition vide
static pthread_mutex_t mutex_vide = PTHREAD_MUTEX_INITIALIZER;
// La variable condition parking plein
static pthread_cond_t cond_vide = PTHREAD_COND_INITIALIZER;

void *entrer(void *arg) {
  int *nb_places_vides_addr = (int *) arg;

  printf("Début de la thread arrive\n");
  while(1){
    // On rentre dans la section critique
    pthread_mutex_lock(&mutex_plein);
    while (*nb_places_vides_addr<=0) {
      // Il ne reste plus de place libre
      // On attend qu'une place libre se libère
      pthread_cond_wait(&cond_plein, &mutex_plein);
    }

    // Il reste au moins une place vide
    // On la consomme
    *nb_places_vides_addr = *nb_places_vides_addr-1;
    
    printf("Nombre de place vide restante %d\n", *nb_places_vides_addr);

    // On reveille les threads potentiellement en attente d'une place pleine
    pthread_cond_broadcast(&cond_vide);
    
    // On sort de la section critique
    pthread_mutex_unlock(&mutex_plein);
    
    // On attend un peu avant de recommencer
    sleep(1); // on se bloque, hors section critique
  }
}

void *sortir(void *arg) {
  int *nb_places_vides_addr = (int *) arg;

  printf("Début de la thread départ\n");
  while(1){
    // On rentre dans la section critique
    pthread_mutex_lock(&mutex_vide);
    while (*nb_places_vides_addr>=NB_PLACES) {
      // Toutes les places sont vides
      // On attend qu'une place soit occupée
       pthread_cond_wait(&cond_vide, &mutex_vide);
    }

    // Il y a au moins un place prise
    // On la libère
    *nb_places_vides_addr = *nb_places_vides_addr+1;
    
    printf("Nombre de place vide restante %d\n", *nb_places_vides_addr);

    // On reveille les threads potentiellement en attente d'une place vide
    pthread_cond_broadcast(&cond_plein);

    // On sort de la section critique
    pthread_mutex_unlock(&mutex_vide);

    /* On attend un peu avant de recommencer */
    if (*nb_places_vides_addr % 3==0) // possible hors section critique car lecture non suivie d'une modification
      sleep(1); // on se bloque, hors section critique
  }
}

int main() {
  pthread_t th1, th2, th3, th4;
  
  // Création des deux threads
  pthread_create(&th1, NULL, entrer, &places_vides);
  pthread_create(&th2, NULL, entrer, &places_vides);
  pthread_create(&th3, NULL, entrer, &places_vides);
  pthread_create(&th4, NULL, sortir, &places_vides);

  // On attend la terminaison des deux threads
  pthread_join(th1, NULL);
  pthread_join(th2, NULL);
  pthread_join(th3, NULL);
  pthread_join(th4, NULL);

  exit(0);
}
