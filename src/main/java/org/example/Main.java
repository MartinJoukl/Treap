package org.example;

import org.example.Domain.Municipality;
import org.example.Domain.TreapTester;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TreapTester tester = new TreapTester();
        boolean exit = false;
        Scanner sc = new Scanner(System.in);

        while (!exit) {
            System.out.println("Zadejte číslo operace:");
            System.out.println("""
                    1) Vizuální test
                    2) Proveď test a sesbírej statistiky
                    3) Resetuj treap
                    4) Vypiš treap
                    5) Vlož obec do treap
                    6) Smaž obec
                    7) Najdi obec v treap
                    8) Přepni omezení velikosti haldových klíčů
                    9) Konec
                    """);
            int operace = sc.nextInt();
            switch (operace) {
                case 1 -> tester.performVisualTest();
                case 2 -> {
                    try {
                        tester.performTestAndCollectStats();
                    } catch (IOException e) {
                        System.out.println("Nepodařilo se provést operaci se složkou. Chyba: " + e);
                    }
                }
                case 3 -> tester.resetTreap();
                case 4 -> System.out.println(tester.getTreapAsString());
                case 5 -> {
                    Scanner intScanner = new Scanner(System.in);
                    Scanner stringScanner = new Scanner(System.in);
                    System.out.println("Zadejte jméno obce");
                    String municipalityName = stringScanner.nextLine();
                    System.out.println("Zadejte počet obyvatel");
                    int inhabitants = intScanner.nextInt();
                    tester.addToTreap(municipalityName, inhabitants);
                }
                case 6 -> {
                    Scanner stringScanner = new Scanner(System.in);
                    System.out.println("Zadejte jméno obce");
                    String municipalityName = stringScanner.nextLine();
                    tester.deleteFromTreap(municipalityName);
                    System.out.println("Obec smazána");
                }
                case 7 -> {
                    Scanner stringScanner = new Scanner(System.in);
                    System.out.println("Zadejte jméno obce");
                    String municipalityName = stringScanner.nextLine();
                    Municipality municipality = tester.findElementInHeap(municipalityName);
                    System.out.println("Obec " + municipality.getNazevObce() + " počet obyvatel: " + municipality.getInhabitants());
                }
                case 8 -> {
                    tester.toggleReduceKeySpace();
                    System.out.println("Nový rozsah klíčů haldy: " + TreapTester.generatedMaxValue);
                }
                case 9 -> exit = true;
            }
        }
    }
}