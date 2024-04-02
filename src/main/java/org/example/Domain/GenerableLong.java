package org.example.Domain;

import java.util.Objects;

import org.apache.commons.lang3.RandomUtils;


public class GenerableLong implements IGenerable<GenerableLong> {
    public static final long INITIAL_MAX_VALUE = Long.MAX_VALUE;
    public static long generatedMaxValue = Long.MAX_VALUE;
    private final long value;

    public GenerableLong(long value) {
        this.value = value;
    }

    public GenerableLong() {
        this.value = generate().value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenerableLong that = (GenerableLong) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(GenerableLong otherLong) {
        return Long.compare(value, otherLong.value);
    }
    public GenerableLong generate() {
        return new GenerableLong(RandomUtils.nextLong(0, generatedMaxValue));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
