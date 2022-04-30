#include <glib.h>
#include <stdio.h>
#include "catalogoclientes.h"



//Estrutura do catalogo de clientes
 struct clientes {
    GTree * array[26];
};


//Compara 2 inteiros e retorna -1, 0 ou 1 se o primeiro for menor,igual ou maior repetidamente
int cmpint (int a,int b){
    if(a>=b){
        if (a==b)return 0;
        else return 1;}
    return 0;
}

//Funçao que inicia o catalogo dos Clientes
//Assume que o parameto passado é invalido e escreve o resultado por cima do mesmo
void initClients(struct clientes * new){
    new=calloc(26,sizeof(GTree *));

}