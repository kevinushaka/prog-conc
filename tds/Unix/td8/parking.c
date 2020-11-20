#include <stdio.h> // Pourprintf ()
#include <stdlib.h>// Pourexit(), NULL
#include <unistd.h> //Pourpause ()
#include <fcntl.h> // Pouropen (), O_CREAT O_WRONLY
#include <signal.h>// Poursignal ()
#include<sys/types.h>// Pourkey_t
#include<sys/ipc.h>// Pourftok(), IPC_CREAT , IPC_RMID
#include <sys/shm.h> // Pourshmget (), shmat (), shmdt (), shmctl ()
#include <sys/sem.h>

#define SHM_CHEMIN "./"
#define SHM_ID 42

int shmid; // Identification du segment
int* shmadr; // Adresse d’attachement du segment

//Fonction executee a la receptiondu signal SIGINT
void traitant_sigint(int numero_signal) { 
    shmdt (shmadr); 
    // Detachement du segment
    shmctl(shmid , IPC_RMID, NULL); // Destruction du segment
    exit (0); 
}

int shm_creation( char * chemin_fichier , int identificateur , int taille ) {
    int fd, shmid;
    key_t cle ;
    //Generation de la cle a partir de ’chemin_fichier’ et identificateur’
    cle = ftok (chemin_fichier, identificateur); 
    //Creation d’un segment de memoire partagee de ’ taille ’ octets .
    shmid = shmget(cle, taille, IPC_CREAT|0666); 
    return shmid ;
} 

typedef int semaphore;
void abandon(char message[]) { 
    perror(message); 
    exit(EXIT_FAILURE); 
} 
semaphore creer_sem(key_t key, int val_init) {
    /* création d’un tableau de 1sémaphore initialisé à val_init*/
    semaphore sem;int r;
    if (sem = semget(key, 1, IPC_CREAT | 0666) < 0) abandon ("creer_sem");
    if (r = semctl(sem,0, SETVAL, val_init)< 0){
        abandon ("initialisation sémaphore");
    }
        return sem;
}
void detruire_sem(semaphore sem){ 
    if (semctl(sem, 0, IPC_RMID, 0)!= 0) 
        abandon("detruire_sem");
}

void changer_sem(semaphore sem, int val) {
    struct sembuf sb[1];
    sb[0].sem_num = 0;
    sb[0].sem_op = val;
    sb[0].sem_flg = 0;
    if (semop(sem, sb, 1)!= 0) {abandon("changer_sem");}
}

void down(semaphore sem){ changer_sem(sem, -1);}
void up(semaphore sem) { changer_sem(sem, 1);}

void Printf(char * msg){printf("%s",msg);}
semaphore entry;
int borne_1();
int borne_2();



int main() {
    //Creation du segment de memoire partagee de taille un entier.
    shmid = shm_creation (SHM_CHEMIN, SHM_ID, sizeof(int)); 
    //Attachement du segment et recuperationde son adresse. 
    shmadr = (int*)shmat(shmid, NULL, 0); 
    //Initialisation a 20 de l’entier contenu dans le segment.
    *shmadr = 20; 
    printf("Parking : Identificateur=%d, Places=%d.\n", shmid, *shmadr); 
    entry=creer_sem(shmid,10);
    int borne1= borne_1();
    int borne2= borne_2();
    signal(SIGINT, traitant_sigint);
    //waitpid(borne1);
    //waitpid(borne2);
    //Deroutementde SIGINT et endormissement jusqu’a reception d’un signal. 
    pause (); 
    return 0;
}

int  borne_1 (){
    int pid =fork();
    printf("PID : %d", &pid);
    if(pid!=0){
         printf("Demande acceptée./n");
        while(1){
            
            if(*shmadr>0){
                Printf("Demande acceptée./n");
                sleep(2);
                changer_sem(entry,--(*shmadr));
                printf("Nombre de place : %d",&shmadr);
            }else{
                Printf("Pas de place./n");
                sleep(1);
            }
        }
        exit(0);
    }
    return pid;
}
int  borne_2(){
    int pid =fork();
    if(pid==0){
        while(1){

            if(shmadr>0){
                Printf("Demande acceptée./n");
                changer_sem(entry,--(*shmadr));
                printf("Nombre de place : %d",&shmadr);
                sleep(1);
            }else{
                Printf("Pas de place./n");
                sleep(1);
            }
        }
        exit(0);
    }
    return pid;
}