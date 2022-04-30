#include <glib.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include "catalogoprodutos.h"


typedef char key[3];


//Parametro de compração das arvores binarias
GCompareDataFunc intcmp (int * a, int * b,gpointer inutilmasexigido){
    if (*a>=*b){
        if(a==b) return 0;
        else return 1;
        }
    else return -1;
}

//Insere um produto no catalogo
int insertProduct(Produtos p,key k,int t0){
    //é nesseçario alocar o inteiro em memoria dinamica para o inserir na arvore
    int * num=malloc(sizeof(int));
    *num=t0;

    //le a chave no dicionario se nao existir cria-a
    GTree * t=g_hash_table_lookup(p,k);
    if (!t) g_hash_table_add(p,g_tree_new(intcmp));
    
    g_tree_insert(t,k,num);
    return 0;    
}

//Testa uma chave e um numero em string de um produto, se ambos forem validos retorna o valor do numero (entre 1000 e 9999)
int testproduto (key k,char num[]){   

    if (!isupper(k[0]) || !isupper(k[1])) return 1;
    
    if (!isdigit(num[4])) return 1;

    int res=0,mul=1;
    for(int i=5;i>=0;i--,mul*=10){
        if (!isdigit(num[i])) return 1;
        res+=(num[i]-'0')*mul;
        }
    
    return res;
}

//Le o ficheiro
int readProducts (Produtos p){
    int i,r=1;
    char buffer[12];
    key k={'\0','\0','\0'};

    FILE *fp=fopen("Produtos.txt","r");
    if (fp==NULL) {
        printf("Erro ao abrir o ficheiro");
        return -1;
        }
    
    for(i=0;fgets(buffer,12,fp);i++) {
        k[0]=buffer[0];
        k[1]=buffer[1];
        r=(testproduto(k,buffer+2));
        if (r>999 && r<10000) insertProduct(p,k,r);    
        else printf("INVALIDO : %s",buffer);
    }

    fclose(fp);
    return i;
}

//Inicializa o Catalogo de produtos
void initProducts (Produtos p){
    p=g_hash_table_new(g_str_hash,g_str_equal);
    readProducts(p);
}

