package org.example;

import org.example.DataStructure.Treap;
import org.example.Domain.Municipality;
import org.example.Domain.Persistence;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Treap<String, Integer> treap = new Treap<String, Integer>();

        treap.insert("H", 10);
        treap.insert("A", 20);
        treap.insert("Z", 30);
        treap.insert("G", 40);
        treap.delete("A");
        treap.delete("Z");
        treap.delete("H");

        treap.insert("B", 50);
        treap.insert("E", 50);
        treap.insert("T", 50);
        treap.insert("A", 50);
        treap.insert("XX", 50);
        treap.insert("XY", 50);
        treap.insert("XZ", 50);


        System.out.println(treap);
        List<Municipality> municipalities = Persistence.loadFromFile("testMunicipalities.txt");
        Persistence.saveToFile(null,"");
        System.out.println("a");
    }
}