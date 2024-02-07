import math


def strSplitBy(dict, regex):
    res = ""
    for i in dict:
        res = res + str(i) + "=" + str(dict[i]) + regex
    return res[:-1]


class pontoRecolha:
    collapseCount = 1
    id_ = latitude = longitude = rua = residuos = None

    def __init__(self, id_, latitude, longitude, rua, capacidade, residuo):
        self.id_ = id_
        self.latitude = latitude
        self.longitude = longitude
        self.rua = rua
        self.residuos = {residuo: capacidade}

    def append(self, capacidade, tipoResiduo):
        if tipoResiduo in self.residuos:
            self.residuos[tipoResiduo] += capacidade
        else:
            self.residuos[tipoResiduo] = capacidade

    def distanciaRealKm(self, outro):
        lat1 = math.radians(self.latitude)
        lon1 = math.radians(self.longitude)
        lat2 = math.radians(outro.latitude)
        lon2 = math.radians(outro.longitude)
        dlon = lon2 - lon1
        dlat = lat2 - lat1
        a = math.sin(dlat / 2) ** 2 + math.cos(lat1) * math.cos(lat2) * math.sin(dlon / 2) ** 2
        c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
        return 6373.0 * c  # Raio da terra * c

    def distanciaEucladiana(self, outro):
        return math.sqrt(pow(self.latitude - outro.latitude, 2) + pow(self.longitude - outro.longitude, 2))

    def mergeWith(self, outro):
        lat = self.latitude * self.collapseCount
        long = self.longitude * self.collapseCount
        self.collapseCount += 1
        self.longitude = (lat + outro.latitude) / self.collapseCount
        self.longitude = (long + outro.longitude) / self.collapseCount
        self.appendResiduos(self.residuos)

    def __str__(self) -> str:
        return "PontoRecolha\n\tid:" + str(self.id_) + "\t rua:'" + str(self.rua) + "'\n\tlat/long:\t" + str(
            self.latitude) + "\t" + str(self.longitude) + "\n\tresiduos:\t" + str(self.residuos)

    def csvline(self):
        return str(self.id_) + ";" + str(self.rua) + ";" + str(self.latitude) + ";" + str(
            self.longitude) + ";" + strSplitBy(self.residuos, '|') + "\n"

    def appendResiduos(self, residuos):
        if residuos:
            for nome in residuos:
                capacidade = residuos[nome]
                self.append(capacidade, nome)


class aresta:
    inicio = fim = custo = None

    def __init__(self, inicio, fim, custo):
        self.inicio = inicio
        self.fim = fim
        self.custo = custo

    def __str__(self):
        return "I:" + str(self.inicio) + " D: " + str(self.fim) + " Custo: " + str(self.custo)

    def csvline(self):
        return str(self.inicio) + ";" + str(self.fim) + ";" + str(self.custo) + "\n"
