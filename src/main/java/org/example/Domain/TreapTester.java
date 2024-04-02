package org.example.Domain;

import org.example.DataStructure.Treap;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.example.Domain.GenerableLong.generatedMaxValue;

public class TreapTester {
    private Treap<String, Municipality, GenerableLong> userTreap;

    public TreapTester(){
        this.userTreap = new Treap<>(new GenerableLong());
    }

    public void resetTreap() {
        userTreap = new Treap<>(new GenerableLong());
    }

    public void performVisualTest() {
        Random random = new SecureRandom();
        userTreap = new Treap<>(new GenerableLong());
        List<Municipality> municipalityList = Persistence.loadFromFile("testMunicipalities.txt");
        System.out.println("Načtena data: " + Arrays.toString(municipalityList.stream().map((Municipality::getNazevObce)).toArray()));
        long previousMaxValue = generatedMaxValue;
        generatedMaxValue = 9999;
        System.out.println("Maximální haldová priorita byla snížena na " + generatedMaxValue);
        System.out.println("Testuji vkládání");
        System.out.println("\n");
        for (Municipality municipality : municipalityList) {
            userTreap.insert(municipality.getNazevObce(), municipality);
            System.out.println(userTreap);
            System.out.println("\n");
        }
        System.out.println("Mažu dvě náhodné obce...");
        List<Municipality> selectionPool = new ArrayList<>(municipalityList);
        Municipality firstToDelete = selectionPool.remove(random.nextInt(selectionPool.size() - 1));
        Municipality secondToDelete = selectionPool.remove(random.nextInt(selectionPool.size() - 1));
        System.out.println("Bude smazána obec: " + firstToDelete.getNazevObce());
        userTreap.delete(firstToDelete.getNazevObce());
        System.out.println(userTreap.toString());
        System.out.print("\nBude smazána obec: " + secondToDelete.getNazevObce());
        userTreap.delete(secondToDelete.getNazevObce());
        System.out.println(userTreap.toString());
        System.out.println("\nTest hledání ");
        Municipality toFind = selectionPool.remove(random.nextInt(selectionPool.size() - 1));
        System.out.println("Hledám obec: " + toFind.getNazevObce());
        Municipality foundMunicipality = userTreap.find(toFind.getNazevObce());
        System.out.println(userTreap.toString());
        System.out.println("Byla nalezena obec: " + foundMunicipality.getNazevObce() + " počet obyvatel: " + foundMunicipality.getInhabitants() + "\n");
        generatedMaxValue = previousMaxValue;
    }

    public List<Municipality> generateRandomTestElements() {
        Random random = new SecureRandom();
        List<Municipality> randomMunicipalities = new ArrayList<>();
        char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        for (int i = 0; i < 1023; i++) {
            int keyLength = random.nextInt(3, 16);
            StringBuilder muniNameBuilder = new StringBuilder(keyLength);
            for (int j = 0; j < keyLength; j++) {
                muniNameBuilder.append(alphabet[random.nextInt(0, alphabet.length)]);
            }
            randomMunicipalities.add(new Municipality(muniNameBuilder.toString(), random.nextInt(0, 999_999)));
        }
        return randomMunicipalities;
    }

    public void performTestAndCollectStats() throws IOException {
        if (generatedMaxValue != GenerableLong.INITIAL_MAX_VALUE) {
            toggleReduceKeySpace();
        }

        double[] heights = new double[10_000];
        double[] comulativeAvg = new double[10_000];
        double maxHeigth;
        double avgHeight;
        double minHeight;
        double mode;
        Persistence.deleteFolder("randomTests");
        for (int i = 0; i < heights.length; i++) {
            if ((i + 1) % 200 == 0) {
                System.out.println("Pokus číslo: " + i);
            }
            Treap<String, Municipality, GenerableLong> testedTreap = new Treap<>(new GenerableLong());
            List<Municipality> municipalityList = generateRandomTestElements();
            Persistence.saveMunicipalitiesToFile(municipalityList, "randomTests/inputs/randomGenerated1023Municipalities" + i + ".txt", true);
            for (Municipality municipality : municipalityList) {
                testedTreap.insert(municipality.getNazevObce(), municipality);
            }
            heights[i] = testedTreap.getTreeDepth();
        }
        maxHeigth = Arrays.stream(heights).max().getAsDouble();
        avgHeight = Arrays.stream(heights).average().getAsDouble();
        minHeight = Arrays.stream(heights).min().getAsDouble();
        mode = calculateMode(heights);

        double cumulativeSum = 0;
        for (int i = 0; i < heights.length; i++) {
            cumulativeSum += heights[i];
            comulativeAvg[i] = cumulativeSum / (i + 1);
        }
        String statString = "";
        statString += "Průměrná výška: " + avgHeight + "\n";
        statString += "Maximální výška: " + maxHeigth + "\n";
        statString += "Minimální výška: " + minHeight + "\n";
        statString += "Modus: " + mode + "\n";
        statString += "Komulativní průměry: " + Arrays.toString(comulativeAvg) + "\n";
        Persistence.saveStringToFile(statString, "randomTests/stats.txt", true);
    }

    private double calculateMode(double[] heights) {
        // Collect elements into a map with their frequencies
        Map<Double, Long> frequencyMap = Arrays.stream(heights)
                .boxed()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map.Entry<Double, Long> modeEntry = frequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(IllegalStateException::new);

        return modeEntry.getKey();
    }

    public String getTreapAsString() {
        return userTreap.toString();
    }

    public void addToTreap(String municipalityName, int inhabitants) {
        Municipality municipality = new Municipality(municipalityName, inhabitants);
        userTreap.insert(municipality.getNazevObce(), municipality);
    }

    public void toggleReduceKeySpace() {
        if (generatedMaxValue == GenerableLong.INITIAL_MAX_VALUE) {
            generatedMaxValue = 9999;
        } else {
            generatedMaxValue = GenerableLong.INITIAL_MAX_VALUE;
        }
    }

    public void deleteFromTreap(String municipalityName) {
        userTreap.delete(municipalityName);
    }

    public Municipality findElementInHeap(String municipalityName) {
        return userTreap.find(municipalityName);
    }
}
