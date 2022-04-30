#include <stdio.h>
#include <string.h>
#include <io.h>
#include <unistd.h>
#include "estado.h"
#include "jogada.h"
#include "FileHandler.h"


ESTADO LoadFile(char *path){
    ESTADO e;
    char File[1024] = "";
    char line[512];
    int j = 0, c = 6;
    FILE *f = fopen(path, "r+");

    if(f != NULL) {
        while (fgets(line, sizeof line, f) != NULL) {
            line[strlen(line) - 1] = '|';
            strcat(File, line);
        }
    }

    if(File[0] == 'M') e.modo = 0;
    else e.modo = File[4]-'0';

    if(File[2] == 'X') e.peca = VALOR_X;
    else e.peca = VALOR_O;

    e.bot=inverte(e.peca); //funciona pq so o jogador pode guardar o estado nao o bot

    for (int i = 0; i < 8; i++) {
        while (File[c] != '|') {
            if(File[c] == 'X') { e.grelha[i][j] = VALOR_X; j++; c++; }
            else if(File[c] == 'O') { e.grelha[i][j] = VALOR_O; j++; c++; }
            else if(File[c] == '-') { e.grelha[i][j] = VAZIA; j++; c++; }
            else c++;
        }
        j = 0;
        c++;
    }

    fclose(f);

    return e;
}

void WriteFile(char *path, ESTADO e){
    char p, m, b = ' ';

    if(e.modo == 0) {m = 'M';}
    else if(e.modo == 1) { m = 'A'; b = '1'; }
    else if(e.modo == 2) { m = 'A'; b = '2'; }
    else { m = 'A'; b = '3'; }
    
    FILE *f = fopen(path, "w+");
    
    if(f != NULL) { //Check if File is created
        if (e.peca == VALOR_X) p = 'X'; //Check peca
        else p = 'O';

        fprintf(f, "%c %c %c\n", m, p, b); //Write Mode and peca to file

        for (int i = 0; i < 8; i++) { //Loop to Write Board Line by Line
            for (int j = 0; j < 8; j++) {
                if (e.grelha[i][j] == VALOR_X) fprintf(f, "X ");
                else if (e.grelha[i][j] == VALOR_O) fprintf(f, "O ");
                else fprintf(f, "- ");
            }
            fprintf(f, "\n");
        }
    } else printf("Invalid File Path or Unable to Creat File"); //Error message


    fclose(f);
}

int CheckFile(char* path){
    if(access(path, F_OK) != -1) return 1;
    else return 0;

}
