//
// Created by pja on 28/02/2019.
//
#include <stdio.h>
#include <stdlib.h>
#include "estado.h"


// exemplo de uma função para imprimir o estado (Tabuleiro)
void printa(ESTADO e)
{
    char c = ' ';

    printf("  0 1 2 3 4 5 6 7\n");
    for (int i = 0; i < 8; i++) {
        printf("%d ",i);
        for (int j = 0; j < 8; j++) {
            switch (e.grelha[i][j]) {
                case VALOR_O: {
                    c = 'O';
                    break;
                }
                case VALOR_X: {
                    c = 'X';
                    break;
                }
                case VAZIA: {
                    c = '-';
                    break;
                }
                case AJUDA:{
                    c='.';
                    break;
                    }
            }
            printf("%c ", c);

        }
        printf("\n");
    }

}

stack push (ESTADO e,stack agr){ //adiciona o estado e á stack e devolve o apontador da nova stack
    stack novo=malloc(sizeof(bloco));
    novo->i=e;
    novo->prox=agr;
    return novo;
}

stack remtop (stack agr){ //remove o estado no topo da stack e devolve o apontador atualizado da stack
    stack temp = agr;
    if (agr!=NULL) {
        agr = agr->prox;
        free (temp);
    }
    return agr;
}

void killstack (stack agr){
    stack temp;
    while (agr!=NULL) {
        temp = agr;
        agr = agr->prox;
        free (temp);
    }
}


void sugestionprinta(ESTADO e,posicao a)
{
    char c = ' ';
    e.grelha[a.x][a.y]=AJUDA;
    printf("  0 1 2 3 4 5 6 7\n");
    for (int i = 0; i < 8; i++) {
        printf("%d ",i);
        for (int j = 0; j < 8; j++) {
            switch (e.grelha[i][j]) {
                case VALOR_O: {
                    c = 'O';
                    break;
                }
                case VALOR_X: {
                    c = 'X';
                    break;
                }
                case VAZIA: {
                    c = '-';
                    break;
                }
                case AJUDA:{
                    c='?';
                    break;
                }
            }
            printf("%c ", c);

        }
        printf("\n");
    }

}