#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <unistd.h>
#include "estado.h"
#include "interpertador.h"
#include "FileHandler.h"
#include "jogada.h"
#include "bots.h"


ESTADO newgrid (){
    int n1,n2;
    ESTADO i;
    for (n1=0;n1<8;n1++)
        for (n2=0;n2<8;n2++)
            i.grelha[n1][n2] = VAZIA;
    i.grelha[3][4] = VALOR_O;
    i.grelha[4][3] = VALOR_O;
    i.grelha[3][3] = VALOR_X;
    i.grelha[4][4] = VALOR_X;
    return i;
}

posicao cpecas (ESTADO e){
    posicao res;
    res.x=res.y=0;
    int i,j;
    for (i=0;i<8;i++) {
        for (j=0;j<8;j++) {
            if (e.grelha[i][j]==VALOR_X) res.x++;
            else res.y++;
        }
    }
    return  res;
}

void interpertador(){
    int alpha,beta;
    alpha=-1000;
    beta=1000;
    char ext, c=0;
    int n1, n2;
    posicao a;
    char linha[50];
    char file[255];
    stack game = NULL;
    game=push (newgrid(),NULL);             //define
    game->i.modo=0;                         //estado
    game->i.bot=game->i.peca=AJUDA;         //primitivo
    do {
        if (!(existemjogadas(game->i))){                //mecanismo que verifica se existem jogadas
            game->i.peca=inverte(game->i.peca);         //possiveis e se o jogo terminou
            if (existemjogadas(game->i)){
                if (game->i.peca==VALOR_X)  ext='O'; //está de proposito ao contrario
                else ext='X';                        //pq a peca foi invertida em cima
                printf("Jogador %c obrigado a passar\n",ext);
                goto normalprint;
            }
            else {
                a=cpecas(game->i);
                if (a.x>a.y) printf("\n\n\n\n\n\n\n\n\n\n\n\n\t JOGADOR DAS PECAS BRANCAS GANHA\n\n\n\n");
                else {if (a.x<a.y)printf("\n\n\n\n\n\n\n\n\n\n\n\n\t\t JOGADOR DAS PECAS BRANCAS GANHA\n\n\n\n");
                     else printf("\n\n\n\n\n\n\n\n\n\n\n\n\t\t\t EMPATE\n\n\n\n");}
                push(game->i,game);
                game->i.peca=AJUDA;
                goto fim;
            }
        }
        if (game->i.modo!=0)                             //mecanismo que aciona a jogada dos bots
            if (game->i.peca==game->i.bot) {
                switch (game->i.modo){
                    case 1:
                        a=bot1(game->i);break;
                    case 2:
                        a=bot2(game->i);break;
                    case 3:
                        a=bot3(game->i);break;
                }
                game=push(atualizarestado(a.x,a.y,game->i),game);
                game->i.peca=inverte(game->i.peca);
                printf ("Jogada do computador na posiçao (%d,%d)\n\n",a.x,a.y);
                goto normalprint;
            }

        switch(game->i.peca) {                          //mecanismo de display de jogador atual
            case VALOR_X:
                printf("X>");
                break;
            case VALOR_O:
                printf("O>");
                break;
            default:
                printf("?>");
                break;
        }

        gets(linha);                                    //leitura do comando

        switch (toupper(linha[0])){                     //interpretador em si
            case 'N':                                   //Novo jogo manual
                sscanf(linha,"%c %c",&c,&ext);
                ext=toupper(ext);
                if (ext=='X' || ext=='O'){ //se o comando for valido
                    if (game->i.peca==AJUDA){killstack(game);game=NULL;}//remover estado primitivo
                    game=push(newgrid(),game);
                    game->i.modo=0;
                    if (ext=='X')  game->i.peca=VALOR_X;
                    else game->i.peca=VALOR_O;
                }
                break;

            case 'L':                                  // Load file
            {
                sscanf(linha, "%c %s", &c, file);
               if (CheckFile(file)) {
                   if (game->i.peca==AJUDA){killstack(game);game=NULL;}//remover estado primitivo
                    printf("L  %s\n", file);
                    game=push(LoadFile(file),game);
                }
               else printf("Ficheiro nao existe");
                break;

            }

            case 'E':                                   //Write file
                sscanf(linha, "%c %s", &c, file);
                WriteFile(file,game->i);
                break;
            case 'J':                                   //Joga x y
                sscanf(linha, "%c %d %d",&ext,&n1, &n2);
                if (!(valida(n1,n2,game->i))) printf("Jogada Inválida");
                else {
                    game=push(atualizarestado(n1,n2,game->i),game);
                    game->i.peca=inverte(game->i.peca);
                }
                 break;
            case 'S':                                   //Mostra Jogadas validas
                if (game->i.peca==AJUDA) goto fim;
                printf("\n");
                printa(mostrarjogadas(game->i));
                printf("\n");
                goto fim;
                break;
            case 'H':                                   //Sugerir jogada
                if (game->i.peca==AJUDA) goto fim;
                sugestionprinta(game->i,bot3(game->i));
                goto fim;
                break;
            case 'U':                                   //Desfazer
                if (game->i.modo==0) {
                    if (game->prox==NULL) printf("Limite atingido\n");
                    else game=remtop(game);
                    }
                else{   //Isto é necessario pq em modo automatico a peça X joga sempre 1º independemtemente qual seja a peca do bot
                    if (game->i.bot==VALOR_X){
                        if ((game->prox)->prox == NULL) printf("Limite atingido\n");
                        else game=remtop(remtop(game));
                    }
                    else {
                        if (game->i.modo!=(game->prox)->i.modo) {game=remtop(game);goto normalprint;}
                        if (game->prox==NULL) printf("Limite atingido\n");
                        else game=remtop(remtop(game));
                    }
                }
                break;
            case 'A':                                   //Novo jogo contra computador *I HAVE NO FREINDS MODE*
                sscanf(linha, "%c %c %d", &c, &ext, &n2);
                ext=toupper(ext);
                if (ext=='X' || ext=='O'){ //se o comando for valido
                    if (game->i.peca==AJUDA){killstack(game);game=NULL;}//remover estado primitivo
                    game=push(newgrid(),game);
                    game->i.modo=n2;
                    game->i.peca=VALOR_X;
                    if (ext=='X')  {game->i.bot=VALOR_X;goto fim;}
                    else game->i.bot=VALOR_O;
                }
                break;
            case 'Q':
                printf("Até a proxima\n");
                goto fim;
            default:
                break;
        }

            normalprint:
        if (game->i.peca==AJUDA) goto fim;
        printf("\n");
        printa(game->i);
        printf("\n");
            fim:
                ;   //necessario pq nao me deixa por uma label sem nada á frente ._.
    } while (toupper(linha[0]) != 'Q');
    killstack(game);
}
