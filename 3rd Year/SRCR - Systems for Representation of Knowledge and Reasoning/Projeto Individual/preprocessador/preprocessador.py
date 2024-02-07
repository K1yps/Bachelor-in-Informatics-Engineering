import ply.yacc as yacc
from analisador_lexico import tokens
import grafo as graph
import sys

nodos = {}
nomesParaIds = {}
cruzamentos = {}
parsedRelations = []
listaArestas = []


# Gramatica:

def p_LINHA(p):
    """LINHA : OBJETO
             | LEGENDA"""
    p[0] = p[1]


def p_Legenda(p):
    """LEGENDA : LEGENDA ';'
               | LEGENDA STRING"""
    p[0] = p[1] + p[2]


def p_Legenda_empty(p):
    """LEGENDA : """
    p[0] = ''


def p_OBJETO(p):
    """OBJETO : FNUM ';' FNUM ';' NUM ';' STRING ';' PLocal ';' STRING ';' STRING ';' NUM ';' NUM ';' NUM """
    handleObjeto(float(p[1]), float(p[3]), p[9], p[11], int(p[19]))


def p_PLocal_segmentado(p):
    """PLocal : NUM ':' STRING Segmento"""
    extra, x = p[4]
    if extra:
        p[3] = p[3] + extra
    p[0] = (int(p[1]), p[3], x)


def p_Segmento(p):
    """Segmento :  '(' STRING HousesNDirections ':' Rua '-' Rua ')' """
    p[0] = (None, (p[5], p[7]))


def p_Segmento_Extra(p):
    """Segmento :  '(' STRING FimInfoRua '(' STRING HousesNDirections ':' Rua '-' Rua ')' """
    p[0] = (p[1] + p[2] + p[3], (p[8], p[10]))


def p_Segmento_Extra_fir(p):
    """Segmento :  '(' STRING FimInfoRua  """
    p[0] = (p[1] + p[2] + p[3], None)


def p_Segmento_empty(p):
    """Segmento : """
    p[0] = (None, None)


def p_Rua_empty(p):
    """Rua : STRING"""
    p[0] = p[1]


def p_Rua(p):
    """Rua : STRING '(' STRING FimInfoRua"""
    p[0] = p[1] + p[2] + p[3] + p[4]


def p_FimInfoRua_Nome(p):
    """FimInfoRua : ')' """
    p[0] = p[1]


def p_FimInfoRua_NomeENum(p):
    """FimInfoRua :  ',' NUM ')'"""
    p[0] = p[1] + p[2] + p[3]


def p_PLocal_virgulaString(p):
    """PLocal : NUM ':' STRING ',' STRING FatoresInuteis"""
    p[0] = (int(p[1]), p[3] + p[4] + p[5], None)


def p_PLocal_virgulaFatores(p):
    """PLocal : NUM ':' STRING ',' FatoresInuteis """
    p[0] = (int(p[1]), p[3], None)


def p_FatoresInuteis(p):
    """FatoresInuteis : FatoresInuteis Fator
                      | """


def p_Factor(p):
    """Fator : '-'
             | NUM
             | '/'
             | '('
             | ')'
             | STRING
             | ','
     """
    return p[1]


n = 1  # Line number


def p_error(p):
    print('Syntax error! Linha ', n, '  --> ', p)


parser = yacc.yacc()


# Processamento de Arestas :


def mkaresta(id1, id2):
    return graph.aresta(id1, id2, nodos[id1].distanciaEucladiana(nodos[id2]))


simplificar = False


def handleObjeto(latitude, longitude, PontoLocal, tipoResiduo, capacidadeTotal):
    id_, rua, segmento = PontoLocal
    if id_ in nodos:
        nodos[id_].append(capacidadeTotal, tipoResiduo)
    else:
        NovoObjeto = graph.pontoRecolha(id_, latitude, longitude, rua, capacidadeTotal, tipoResiduo)

        foiInserido = False

        if rua in nomesParaIds:
            if simplificar:
                id_ = nomesParaIds[rua][0]
                nodos[id_].mergeWith(NovoObjeto)
            else:
                nodos[id_] = NovoObjeto
                foiInserido = True
                for i in nomesParaIds[rua]:
                    listaArestas.append(mkaresta(i, id_))
                addIfAbstent(nomesParaIds[rua], id_)
        else:
            nomesParaIds[rua] = [id_]
            nodos[id_] = NovoObjeto
            foiInserido = True

        temVizinhos = False
        if segmento:
            x, y = segmento
            for count in range(2):
                if x is not None:
                    temVizinhos = True
                    if x not in cruzamentos:
                        cruzamentos[x] = [id_]
                    elif id_ not in cruzamentos[x]:
                        cruzamentos[x].append(id_)
                x = y
        if foiInserido:
            parsedRelations.append((id_, temVizinhos))


def addIfAbstent(lista, elemento):
    if lista is None:
        return [elemento]
    if elemento not in lista:
        lista.append(elemento)
    return lista


# MAIN

# Simplificar ou nao?

if 'simplicado' in sys.argv or '-s' in sys.argv:
    simplificar = True

# Ler e fazer Parse

with open('dataset.csv', encoding='UTF8') as file:
    linha = file.readline()
    print("Line ignored: ", linha)
    while linha:
        n += 1
        linha = file.readline()
        parser.parse(linha)

# Processar arestas de cruzamentos

for cruz in cruzamentos:
    if cruz in nomesParaIds:
        idx = nomesParaIds[cruz][0]
        for obj in cruzamentos[cruz]:
            if obj != idx:
                listaArestas.append(mkaresta(idx, obj))

# Processar arestas de nodos sem vizinhos diretos

count = len(parsedRelations)
curr = 0
old, conected = parsedRelations[count - 1]

while curr < count:
    obj, conected = parsedRelations[curr]
    if not conected:
        if obj is not old:
            listaArestas.append(mkaresta(obj, old))
        next, b = parsedRelations[(curr + 1) % count]
        if b and obj is not next:
            ares = mkaresta(obj, next)
    old = obj
    curr += 1

with open("nodos.csv", "w", encoding="UTF8") as file:
    for node in nodos:
        file.write(nodos[node].csvline())

with open("arestas.csv", "w", encoding="UTF8") as file:
    for ars in listaArestas:
        file.write(ars.csvline())
