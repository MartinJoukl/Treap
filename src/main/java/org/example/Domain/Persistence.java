package org.example.Domain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
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
                for (int i = 0; i < splitLines.length - 1; i++) {
                    municipalityName.append(splitLines[i]);
                }

                loadedMunicipalities.add(new Municipality(municipalityName.toString(), inhabitants));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return loadedMunicipalities;
    }

    public static void saveMunicipalitiesToFile(List<Municipality> municipalities, String file, boolean force) throws IOException {
        if (!force && Files.exists(Path.of(file))) {
            throw new IOException();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Municipality municipality : municipalities) {
                writer.write(municipality.getNazevObce() + " " + municipality.getInhabitants());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteFolder(String directoryName) throws IOException {
        Path pathToBeDeleted = Path.of(directoryName);

        Files.walk(pathToBeDeleted)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public static void saveStringToFile(String stringToSave, String file, boolean force) throws IOException {
        if (!force && Files.exists(Path.of(file))) {
            throw new IOException();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(stringToSave);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
