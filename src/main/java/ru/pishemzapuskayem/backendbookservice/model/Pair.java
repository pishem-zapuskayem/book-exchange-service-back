package ru.pishemzapuskayem.backendbookservice.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Pair<T, T2> {
    private final T first;
    private final T2 second;
    private final String pairKey;

    public Pair(T first, T2 second) {
        this.first = first;
        this.second = second;
        this.pairKey = generatePairKey();
    }

    public String generatePairKey() {
        int hash1 = first.hashCode();
        int hash2 = second.hashCode();
        return hash1 < hash2 ? hash1 + ":" + hash2 : hash2 + ":" + hash1;
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
}
