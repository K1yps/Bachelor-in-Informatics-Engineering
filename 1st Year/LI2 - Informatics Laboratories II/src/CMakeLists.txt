cmake_minimum_required(VERSION 3.13)
project(proj C)

set(CMAKE_C_STANDARD 99)

add_executable(proj main.c estado.h estado.c interpertador.c jogada.c jogada.h interpertador.h FileHandler.c FileHandler.h bots.c bots.h)