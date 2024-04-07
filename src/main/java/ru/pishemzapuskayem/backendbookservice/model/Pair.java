package ru.pishemzapuskayem.backendbookservice.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Pair<T, T2> {
    private final T first;
    private final T2 second;

    public Pair(T first, T2 second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) &&
            Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    public static String generatePairKey(Pair<?, ?> pair) {
        int hash1 = pair.getFirst().hashCode();
        int hash2 = pair.getSecond().hashCode();
        return hash1 < hash2 ? hash1 + ":" + hash2 : hash2 + ":" + hash1;
    }
}
