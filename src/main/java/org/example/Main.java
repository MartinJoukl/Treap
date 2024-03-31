package org.example;

import DataStructure.Treap;
import Domain.GenerableLong;

public class Main {
    public static void main(String[] args) {
        Treap<String, Integer> treap = new Treap<String, Integer>();

        treap.insert("H", 10);
        treap.insert("A", 10);
        treap.insert("Z", 10);
        treap.insert("G", 10);


        System.out.println(treap);
    }
}