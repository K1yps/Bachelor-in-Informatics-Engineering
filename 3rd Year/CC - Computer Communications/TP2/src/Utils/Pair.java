package Utils;

import java.util.Objects;

public class Pair<T, T1> {

    public T fst;
    public T1 snd;

    public Pair() {
    }


    public Pair(T fst, T1 snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public T getFst() {
        return fst;
    }

    public void setFst(T fst) {
        this.fst = fst;
    }

    public T1 getSnd() {
        return snd;
    }

    public void setSnd(T1 snd) {
        this.snd = snd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(fst, pair.fst) && Objects.equals(snd, pair.snd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fst, snd);
    }

}
