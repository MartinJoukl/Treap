package org.example.Domain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Persistence {
    public static List<Municipality> loadFromFile(String file) {
        List<Municipality> loadedMunicipalities = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitLines = line.split(" ");
                int inhabitants = Integer.parseInt(splitLines[splitLines.length - 1]);
                StringBuilder municipalityName = new StringBuilder();
                for (int i = 0; i < splitLines.length - 2; i++) {
                    municipalityName.append(splitLines[i]);
                }

                loadedMunicipalities.add(new Municipality(municipalityName.toString(), inhabitants));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return loadedMunicipalities;
    }

    public static void saveToFile(List<Municipality> municipalities, String file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Municipality municipality : municipalities) {
                writer.write(municipality.getNazevObce() + " " + municipality.getInhabitants());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
