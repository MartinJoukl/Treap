package org.example;

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
            System.out.println("1) Vizuální test\n2) Proveď test a sesbírej statistiky \n3) Ukonči");
            int operace = sc.nextInt();
            switch (operace) {
                case 1 -> tester.performVisualTest();
                case 2 -> {
                    try {
                        tester.performTestAndCollectStats();
                    } catch (IOException e) {
                        System.out.println("Nepodařilo se načíst vstupní soubor s prvky... možná ho bude nutné vytvořit.");
                    }
                }
                case 3 -> exit = true;
            }
        }

        tester.performVisualTest();

    }

    /*
    private static void handleNewElementsGeneration(TreapTester tester) {
        boolean shouldContinue = true;
        boolean force = false;
        while (shouldContinue) {
            try {
                tester.generateRandomTestElements(force);
                System.out.println("Nové prvky byly vytvořeny");
                shouldContinue = false;
            } catch (IOException e) {
                System.out.println("Vytváření selhalo... možná již existuje... Zkusit znovu s možným přepsáním? Y/N");
                char a;
                Scanner lineScanner = new Scanner(System.in);
                do {
                    a = lineScanner.nextLine().toUpperCase().charAt(0);
                    force = true;
                } while (a != 'Y' && a != 'N');
                if (a == 'N') {
                    force = false;
                    shouldContinue = false;
                    System.out.println("Nové prvky nebyly vytvořeny...");
                }
            }
        }
    }

     */
}