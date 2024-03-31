package Domain;

import java.util.Objects;

import org.apache.commons.lang3.RandomUtils;


public class GenerableLong implements IGenerable<GenerableLong> {

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
        return new GenerableLong(RandomUtils.nextLong(Long.MIN_VALUE, Long.MAX_VALUE));
    }
}
