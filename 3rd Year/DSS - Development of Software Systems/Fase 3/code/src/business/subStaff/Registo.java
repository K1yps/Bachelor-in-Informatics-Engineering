package business.subStaff;

import java.time.LocalDateTime;

public class Registo {
    private final LocalDateTime hora;

    public Registo() {
        this.hora = LocalDateTime.now();
    }

}
