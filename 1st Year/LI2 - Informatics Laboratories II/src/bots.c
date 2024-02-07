#include <stdio.h>
#include <stdlib.h>
#include "estado.h"
#include "jogada.h"
#include "bots.h"
//char openingmoves[1401]="2322/2322322411/2322322412/232232241252/2322322412531413253554/2322322413/23223224133121533525544556/23223224133135/23223224133141/232232241342/2322322414/2322322415/23223224152514/232232241542/2322322435/232232243542/23223224355313/2322322435531315142512/2322322435531315142554/2322322435531325141215422103040531/232232243553134212/23223224355354/2322322435535431/232232243553543114/23223224355354316336/23223224355354452536/232232243553544555/2322322455/23223224554125/23223224554225545364/232232245554/23224524/23225424/2324/2342/234253243545/23425324355225/23425324355232/2342532435523222/23425324355241/23425324355245223241/2342532435524522324114/234253243552452232411454/23425324355245223241145412/234253243552452232411454135521463126/234253243552452232411554/23425324355245223241155413556436/23425324355245223241155464/2342532435524522324115546455/2342532435524522324131/2342532435524525/2342532445/23425413/2342541352/234254135253414564553525/2342544532/2342544553/23425445532435255652/2342544553243525566465/23425513/2342554554/23425545542422/2342554554242213/2342554554242213352514051526/2342554554242225/234255455424222532314112202113020414050301/234255455424222535/2342554554242264/23425545542432/23425545542435/23425545542453646352253235/234255455424536463522532351222134115/234255455424536556/23425545542453655632/23425545542453655664/234255455465/2342554554654624";
//char jogadasbot[64]="\0";
int contapecas (ESTADO e){
    int i,j,r=0;
    for (i=0;i<8;i++)
        for(j=0;j<8;j++)
            if (e.grelha[i][j]!=VAZIA) r++;
    return r;
}


int mobilityevaluationaux (ESTADO e) {
    int i,j,r=0;
    for (i=0;i<8;i++)
        for(j=0;j<8;j++)
            if(valida(i,j,e)) r++;
    return r;
}

posicao mobilityevaluation (ESTADO e) {
    int i,j,c=0,min=100;
    ESTADO e2;
    posicao final;
    for (i=0;i<8;i++)
        for(j=0;j<8;j++)
        {
            if(valida(i,j,e)) {
                e2=atualizarestado(i,j,e);
                c=mobilityevaluationaux(e2);
                if (c<min) {min=c;final.x=i;final.y=j;}
            }
        }
    return final;
}

posicao bot1 (ESTADO e) {
    posicao pos[30];
    int i, j, s = 0;
    for (i = 0; i < 8; i++) {
        for (j = 0; j < 8; j++) {
            if (valida(i, j, e)) {
                pos[s].x = i;
                pos[s].y = j;
                s++;
            }
        }
    }
    s=rand()%s;
    return (pos[s]);
}

posicao bot2 (ESTADO e) {
    posicao pos[30];
    posicao quarentena;
    int i, j, s = 0;
    for (i = 0; i < 8; i++){
        for (j = 0; j < 8; j++) {
            if (valida(i, j, e)) {
                if ((i == 1 && j == 1) || (i == 6 && j == 6) || (i == 1 && j == 6) || (i == 6 && j == 1)) {
                    quarentena.x = i;
                    quarentena.y = j;
                } else {
                    pos[s].x = i;
                    pos[s].y = j;
                    if ((i == 0 && j == 0) || (i == 7 && j == 7) || (i == 0 && j == 7) || (i == 7 && j == 0))
                        return (pos[s]);
                    s++;
                }
            }
        }
    }
        if (s == 0) return quarentena;
        else {
            for (i = 0; i < s; i++) {
                if ((pos[i].x == 0 && pos[i].y == 2) || (pos[i].x == 0 && pos[i].y == 5) ||
                    (pos[i].x == 2 && pos[i].y == 0) || (pos[i].x == 2 && pos[i].y == 7) ||
                    (pos[i].x == 5 && pos[i].y == 0) || (pos[i].x == 7 && pos[i].y == 2) ||
                    (pos[i].x == 7 && pos[i].y == 7) || (pos[i].x == 5 && pos[i].y == 7))
                    return pos[i];
            }
            for (i = 0; i < s; i++) {
                if ((pos[i].x == 0 && pos[i].y == 3) || (pos[i].x == 0 && pos[i].y == 4) ||
                    (pos[i].x == 2 && pos[i].y == 2) || (pos[i].x == 2 && pos[i].y == 5) ||
                    (pos[i].x == 5 && pos[i].y == 2) || (pos[i].x == 5 && pos[i].y == 5) ||
                    (pos[i].x == 7 && pos[i].y == 3) || (pos[i].x == 7 && pos[i].y == 4) ||
                    (pos[i].x == 3 && pos[i].y == 0) || (pos[i].x == 3 && pos[i].y == 7) ||
                    (pos[i].x == 4 && pos[i].y == 0) || (pos[i].x == 4 && pos[i].y == 7))
                    return pos[i];
            }
            for (i = 0; i < s; i++) {
                if ((pos[i].x == 2 && pos[i].y == 3) || (pos[i].x == 2 && pos[i].y == 4) ||
                    (pos[i].x == 3 && pos[i].y == 2) || (pos[i].x == 3 && pos[i].y == 5) ||
                    (pos[i].x == 4 && pos[i].y == 2) || (pos[i].x == 4 && pos[i].y == 5) ||
                    (pos[i].x == 5 && pos[i].y == 3) || (pos[i].x == 5 && pos[i].y == 4))
                    return pos[i];
            }
            for (i = 0; i < s; i++) {
                if ((pos[i].x == 1 && pos[i].y == 3) || (pos[i].x == 1 && pos[i].y == 4) ||
                    (pos[i].x == 3 && pos[i].y == 1) || (pos[i].x == 3 && pos[i].y == 6) ||
                    (pos[i].x == 4 && pos[i].y == 1) || (pos[i].x == 4 && pos[i].y == 6) ||
                    (pos[i].x == 6 && pos[i].y == 3) || (pos[i].x == 6 && pos[i].y == 4))
                    return pos[i];
            }
            for (i = 0; i < s; i++) {
                if ((pos[i].x == 1 && pos[i].y == 2) || (pos[i].x == 1 && pos[i].y == 5) ||
                    (pos[i].x == 2 && pos[i].y == 1) || (pos[i].x == 2 && pos[i].y == 6) ||
                    (pos[i].x == 5 && pos[i].y == 1) || (pos[i].x == 5 && pos[i].y == 6) ||
                    (pos[i].x == 6 && pos[i].y == 2) || (pos[i].x == 6 && pos[i].y == 5))
                    return pos[i];
            }
            for (i = 0; i < s; i++) {
                if ((pos[i].x == 0 && pos[i].y == 1) || (pos[i].x == 0 && pos[i].y == 6) ||
                    (pos[i].x == 1 && pos[i].y == 0) || (pos[i].x == 1 && pos[i].y == 7) ||
                    (pos[i].x == 6 && pos[i].y == 1) || (pos[i].x == 6 && pos[i].y == 7) ||
                    (pos[i].x == 7 && pos[i].y == 1) || (pos[i].x == 7 && pos[i].y == 6))
                    return pos[i];
            }
        }
    return pos[i];
}

posicao bot3 (ESTADO e) {
    posicao pos[30];
    posicao quarentena;
    int i, j, s = 0;
    if (contapecas(e)<14) {
        return(mobilityevaluation(e));
    } else
    for (i = 0; i < 8; i++){
        for (j = 0; j < 8; j++) {
            if (valida(i, j, e)) {
                if ((i == 1 && j == 1) || (i == 6 && j == 6) || (i == 1 && j == 6) || (i == 6 && j == 1)) {
                    quarentena.x = i;
                    quarentena.y = j;
                } else {
                    pos[s].x = i;
                    pos[s].y = j;
                    if ((i == 0 && j == 0) || (i == 7 && j == 7) || (i == 0 && j == 7) || (i == 7 && j == 0))
                        return (pos[s]);
                    s++;
                }
            }
        }
    }
    if (s == 0) return quarentena;
    else {
        for (i = 0; i < s; i++) {
            if ((pos[i].x == 0 && pos[i].y == 2) || (pos[i].x == 0 && pos[i].y == 5) ||
                (pos[i].x == 2 && pos[i].y == 0) || (pos[i].x == 2 && pos[i].y == 7) ||
                (pos[i].x == 5 && pos[i].y == 0) || (pos[i].x == 7 && pos[i].y == 2) ||
                (pos[i].x == 7 && pos[i].y == 7) || (pos[i].x == 5 && pos[i].y == 7))
                return pos[i];
        }
        for (i = 0; i < s; i++) {
            if ((pos[i].x == 0 && pos[i].y == 3) || (pos[i].x == 0 && pos[i].y == 4) ||
                (pos[i].x == 2 && pos[i].y == 2) || (pos[i].x == 2 && pos[i].y == 5) ||
                (pos[i].x == 5 && pos[i].y == 2) || (pos[i].x == 5 && pos[i].y == 5) ||
                (pos[i].x == 7 && pos[i].y == 3) || (pos[i].x == 7 && pos[i].y == 4) ||
                (pos[i].x == 3 && pos[i].y == 0) || (pos[i].x == 3 && pos[i].y == 7) ||
                (pos[i].x == 4 && pos[i].y == 0) || (pos[i].x == 4 && pos[i].y == 7))
                return pos[i];
        }
        for (i = 0; i < s; i++) {
            if ((pos[i].x == 2 && pos[i].y == 3) || (pos[i].x == 2 && pos[i].y == 4) ||
                (pos[i].x == 3 && pos[i].y == 2) || (pos[i].x == 3 && pos[i].y == 5) ||
                (pos[i].x == 4 && pos[i].y == 2) || (pos[i].x == 4 && pos[i].y == 5) ||
                (pos[i].x == 5 && pos[i].y == 3) || (pos[i].x == 5 && pos[i].y == 4))
                return pos[i];
        }
        for (i = 0; i < s; i++) {
            if ((pos[i].x == 1 && pos[i].y == 3) || (pos[i].x == 1 && pos[i].y == 4) ||
                (pos[i].x == 3 && pos[i].y == 1) || (pos[i].x == 3 && pos[i].y == 6) ||
                (pos[i].x == 4 && pos[i].y == 1) || (pos[i].x == 4 && pos[i].y == 6) ||
                (pos[i].x == 6 && pos[i].y == 3) || (pos[i].x == 6 && pos[i].y == 4))
                return pos[i];
        }
        for (i = 0; i < s; i++) {
            if ((pos[i].x == 1 && pos[i].y == 2) || (pos[i].x == 1 && pos[i].y == 5) ||
                (pos[i].x == 2 && pos[i].y == 1) || (pos[i].x == 2 && pos[i].y == 6) ||
                (pos[i].x == 5 && pos[i].y == 1) || (pos[i].x == 5 && pos[i].y == 6) ||
                (pos[i].x == 6 && pos[i].y == 2) || (pos[i].x == 6 && pos[i].y == 5))
                return pos[i];
        }
        for (i = 0; i < s; i++) {
            if ((pos[i].x == 0 && pos[i].y == 1) || (pos[i].x == 0 && pos[i].y == 6) ||
                (pos[i].x == 1 && pos[i].y == 0) || (pos[i].x == 1 && pos[i].y == 7) ||
                (pos[i].x == 6 && pos[i].y == 1) || (pos[i].x == 6 && pos[i].y == 7) ||
                (pos[i].x == 7 && pos[i].y == 1) || (pos[i].x == 7 && pos[i].y == 6))
                return pos[i];
        }
    }
    return pos[i];
}






//TENTATIVA DE ALPHA BETA PRUNING E PATTERN EVALUATION

/*


posicao patternevaluationaux (ESTADO e) {
    posicao final;
    int i,j,n=0;
    int p=contapecas(e);
    if (p<25) {      //a maior openingmove tem 21 jogadas(21 pecas + 4 iniciais=25. Casos em que o bot vai jogar com opening moves
        if (p==4) {final.x=2;final.y=3;return final;} else
        if (p==5) {final.x=2;final.y=2;return final;} else  {
            n=encontratabuleiro(jogadas);
            if (jogadasbot[n]!='\0' && jogadasbot[n+1]!='/') {final.x=jogadasbot[n+1];final.y=jogadasbot[n+2];return final;} else
                final=mobilityevaluation(e);

        }

    }
    return final;
}




int patternevaluation (ESTADO e) {

}

 */
/*
int fimdejogo(ESTADO e) {
    if (!(existemjogadas(e))) {                //mecanismo que verifica se existem jogadas
        e.peca = inverte(e.peca);         //possiveis e se o jogo terminou
        if (!(existemjogadas(e))) return 0;
    }
    return 1;
}

int avaliatabuleiro(ESTADO e) {
    int r;
    r=(rand()%200)-100; //para ser possivel fazer testes simpllesmente cria um valor aliatorio
    return r;
}

int max(int a,int b) {
    if (a>b) return a; else return b;
}

int min(int a,int b) {
    if (a<b) return a; else return b;
}

int alphabeta(ESTADO e, int depth, int *a, int *b, int maximizingPlayer,posicao *jogada) {
    ESTADO e2;
    int value=0,i,j;
    if (depth == 0 ||  fimdejogo(e))
        return (avaliatabuleiro(e)); else
    if (maximizingPlayer) {
        value = -1000;
        for (i = 0; i < 8; i++)
            for (j = 0; j < 8; j++) {
                if (valida(i, j, e)) {  //each child of node do
                    e2 = (atualizarestado(i, j, e));
                    value = max(value, alphabeta(e2, depth - 1, a, b, 0,jogada));
                    *a = max(*a, value);
                    if (*a >= *b) {
                        (*jogada).x=i;
                        (*jogada).y=j;
                        goto fim1;
                    }
                }
            }
        fim1:
        return value;
    }
    else {
        value=+1000;
        for (i = 0; i < 8; i++)
            for (j = 0; j < 8; j++) {
                if (valida(i, j, e)) {  //each child of node do
                    e2 = (atualizarestado(i, j, e));
                    value = min(value, alphabeta(e2, depth - 1, a, b, 1,jogada));
                    *b = min(*b, value);
                    if (*a >= *b)
                        goto fim2;
                }
            }
        fim2:
        return value;
    }
}
 */