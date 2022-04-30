package business.subStock;

import java.util.AbstractMap;
import java.util.Map.Entry;

public class EmArmazenamento implements EstadoPalete {
    private final int prateleira;
    private final String zona;

    public EmArmazenamento(String zona, int prateleira) {
        this.prateleira = prateleira;
        this.zona = zona;
    }

    public int getPrateleira() {
        return prateleira;
    }

    public String getZona() {
        return zona;
    }

    public Entry<String, Integer> getLocation() {
        return new AbstractMap.SimpleEntry<>(zona, prateleira);
    }

}

