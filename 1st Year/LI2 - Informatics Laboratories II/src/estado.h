//
// Created by pja on 27/02/2019.
//



#ifndef PROJ_ESTADO_H
#define PROJ_ESTADO_H



/**
estado.h
Definição do estado i.e. tabuleiro. Representação matricial do tabuleiro.
*/


// definição de valores possiveis no tabuleiro
typedef enum {VAZIA, VALOR_X, VALOR_O,AJUDA} VALOR;

/**
Estrutura que armazena o estado do jogo
*/
typedef struct estado {
    VALOR peca,bot; // peça do jogador que vai jogar!
    VALOR grelha[8][8];
    char modo; // modo em que se está a jogar! 0-> manual, 1,2,3-> contra computador
} ESTADO;


typedef struct sEstado {
    ESTADO i;
    struct  sEstado * prox;
} bloco;

typedef bloco * stack;

typedef struct coordenada{ //coordernadas da grelha
    int x,y;
}posicao;

void printa(ESTADO);
stack push (ESTADO e,stack agr);
stack remtop (stack agr);
void killstack (stack agr);
void sugestionprinta(ESTADO e,posicao a);

#endif //PROJ_ESTADO_H