package org.example.Domain;

public interface IGenerable<T> extends Comparable<T> {
    public T generate();
}
