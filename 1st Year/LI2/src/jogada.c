#include <stdio.h>
#include "estado.h"
#include "jogada.h"
VALOR inverte(VALOR a) // a funcao inverte vê qual é a peça contrária à peça recebida pela função
{
    if (a==VAZIA) return VAZIA; else
    if (a==VALOR_O) return VALOR_X; else
    if (a==VALOR_X) return VALOR_O; else return AJUDA;
}

int auxvalida(int x,int y,ESTADO e)
{
    int l=x,c=y;
    VALOR k=inverte(e.peca);
    if(e.grelha[x][y-1]==k) {
        c--;
        while (e.grelha[l][c]==k && c>0) c--;
        if (e.grelha[l][c]==e.peca) return 1; else {l=x;c=y;} } // o vizinho esta na esquerda
    if(e.grelha[x][y+1]==k) {
        c++;
        while (e.grelha[l][c]==k && c<8) c++;
        if (e.grelha[l][c]==e.peca) return 1; else {l=x;c=y;} } // o vizinho esta na direita
    if(e.grelha[x-1][y-1]==k){
        c--;l--;
        while (e.grelha[l][c]==k && c>0 && l>0) {c--;l--;}
        if (e.grelha[l][c]==e.peca) return 1; else {l=x;c=y;} } // o vizinho esta no canto esquerdo superior
    if(e.grelha[x-1][y]==k) {
        l--;
        while (e.grelha[l][c]==k && l>0) l--;
        if (e.grelha[l][c]==e.peca) return 1; else {l=x;c=y;} } // o vizinho esta em cima
    if(e.grelha[x-1][y+1]==k){
        c++;l--;
        while (e.grelha[l][c]==k && c<8 && l>0) {c++;l--;}
        if (e.grelha[l][c]==e.peca) return 1; else {l=x;c=y;} } // o vizinho esta no canto direito superior
    if(e.grelha[x+1][y-1]==k){
        c--;l++;
        while (e.grelha[l][c]==k && c>0 && l<8) {c--;l++;}
        if (e.grelha[l][c]==e.peca) return 1; else {l=x;c=y;} } // o vizinho esta no canto esquerdo inferior
    if(e.grelha[x+1][y]==k) {
        l++;
        while (e.grelha[l][c]==k && l<8) l++;
        if (e.grelha[l][c]==e.peca) return 1; else {l=x;c=y;} } // o vizinho esta em baixo
    if(e.grelha[x+1][y+1]==k){
        c++;l++;
        while (e.grelha[l][c]==k && c<8 && l<8) {c++;l++;};
        if (e.grelha[l][c]==e.peca) return 1; else {l=x;c=y;} } // o vizinho esta no canto direito inferior
    return 0;
}

int valida(int x,int y,ESTADO e) {
    if (e.grelha[x][y]==VAZIA) {
        if (auxvalida(x,y,e) == 1) return 1;
    }
    return 0;
}


ESTADO atualizarestado(int x,int y,ESTADO e) {
        int l, c;
        VALOR k = inverte(e.peca);
        if (e.grelha[x][y - 1] == k) {
            l=x;c=y;
            c--;
            while (e.grelha[l][c] == k && c > 0) c--;
            if (e.grelha[l][c] == e.peca) {
                while (c < y){e.grelha[l][c]=e.peca; c++;}
            }
        }
        if (e.grelha[x][y + 1] == k) {
            l=x;c=y;
            c++;
            while (e.grelha[l][c] == k && c < 8) c++;
            if (e.grelha[l][c] == e.peca) {
                while (c > y){e.grelha[l][c]=e.peca; c--;}
            }
        }
        if (e.grelha[x-1][y] == k) {
            l=x;c=y;
            l--;
            while (e.grelha[l][c] == k && l > 0) l--;
            if (e.grelha[l][c] == e.peca) {
                while (l < x){e.grelha[l][c]=e.peca; l++;}
            }
        }
        if (e.grelha[x+1][y] == k) {
            l=x;c=y;
            l++;
            while (e.grelha[l][c] == k && l<8) l++;
            if (e.grelha[l][c] == e.peca) {
                while (l > x){e.grelha[l][c]=e.peca; l--;}
            }
        }
        if (e.grelha[x-1][y - 1] == k) {
            l=x;c=y;
            c--;l--;
            while (e.grelha[l][c] == k && c > 0 && l>0) {c--;l--;}
            if (e.grelha[l][c] == e.peca) {
                while (l < x && c<y){e.grelha[l][c]=e.peca; l++,c++;}
            }
        }
        if (e.grelha[x-1][y + 1] == k) {
            l=x;c=y;
            l--;c++;
            while (e.grelha[l][c] == k && l>0 && c < 8) {l--;c++;}
            if (e.grelha[l][c] == e.peca) {
                while (l < x && c>y){e.grelha[l][c]=e.peca; l++,c--;}
            }
        }
        if (e.grelha[x+1][y - 1] == k) {
            l=x;c=y;
            c--;l++;
            while (e.grelha[l][c] == k && c > 0 && l<8) {c--;l++;}
            if (e.grelha[l][c] == e.peca) {
                while (l > x && c<y){e.grelha[l][c]=e.peca; l--,c++;}
            }
        }
        if (e.grelha[x+1][y + 1] == k) {
            l=x;c=y;
            l++;c++;
            while (e.grelha[l][c] == k && c<8 && l<8) {l++;c++;};
            if (e.grelha[l][c] == e.peca) {
                while (l > x && c>y){e.grelha[l][c]=e.peca; l--,c--;}
            }
        }
    e.grelha[x][y]=e.peca;
    return e;
}

ESTADO mostrarjogadas(ESTADO e){
    int i,j;
    for (i=0;i<8;i++) {
        for (j=0;j<8;j++) {
            if (valida(i,j,e)) e.grelha[i][j]=AJUDA;
        }
    }
    return e;
}

int existemjogadas (ESTADO e){
    int i,j;
    if (e.peca!=VALOR_O && e.peca!=VALOR_X) return 1;
    for (i=0;i<8;i++) {
        for (j=0;j<8;j++) {
            if (valida(i,j,e)) return 1;
        }
    }
    return 0;
}