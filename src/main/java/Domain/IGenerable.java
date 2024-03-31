package Domain;

public interface IGenerable<T> extends Comparable<T> {
    public T generate();
}
