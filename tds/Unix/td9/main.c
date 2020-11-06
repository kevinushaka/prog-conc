#include <pthread.h>
#include <stdio.h>
#include <unistd.h>

#define CAPACITY 5

int space = 0;
void *arrive(void * p_data);
void *depart(void * p_data);
void print_state();
pthread_mutex_t arr_mutex= PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t dep_mutex = PTHREAD_MUTEX_INITIALIZER;

int main(){
    pthread_t arr_tinfo,dep_tinfo;
    int arrivals=pthread_create(&arr_tinfo,NULL,arrive,NULL);
    int departure=pthread_create(&dep_tinfo,NULL,depart,NULL);
    pthread_join(arr_tinfo, NULL);
    pthread_join(dep_tinfo, NULL);
}

void print_state(){
    printf("Space remaining : %d\n",space);
}

void *arrive(void * p_data){
    while(1){
        pthread_mutex_lock(&arr_mutex);
        if(space<0) {pthread_mutex_lock(&dep_mutex);}
        sleep(1);
        if(space<CAPACITY){
            space++; printf("A car arrive.\n");
        }

        print_state();
        pthread_mutex_unlock(&arr_mutex);
        pthread_mutex_unlock(&dep_mutex);
    }
}
void *depart(void * p_data){
    while(1){
        pthread_mutex_lock(&dep_mutex);
        if(space>CAPACITY) {pthread_mutex_lock(&arr_mutex);}
        sleep(2);
        if(space>0){
            space--; 
            printf("A car leave.\n");
        } 
        print_state();
        pthread_mutex_unlock(&dep_mutex);
        pthread_mutex_unlock(&arr_mutex);
    }

}