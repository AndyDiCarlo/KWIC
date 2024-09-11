import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

// Run code with "java KWIC input.txt output.txt"

public class KWIC {
    private char[] textData; // Shared memory: text data stored as characters
    private String[] rotations; // Stores all rotations of the sentence

    public KWIC(String filePath) throws IOException {
        String text = kwicInput(filePath);
        textData = text.toLowerCase().toCharArray(); // Convert to lowercase
        int wordCount = countWords(textData);
        rotations = new String[wordCount];
        generateRotations();
        Arrays.sort(rotations); // Sort after generating rotations
    }

    private String kwicInput(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(" ");
            }
        }
        return content.toString().trim();
    }

    private int countWords(char[] text) {
        int count = 0;
        boolean inWord = false;
        for (char c : text) {
            if (Character.isWhitespace(c)) {
                if (inWord) {
                    count++;
                    inWord = false;
                }
            } else {
                inWord = true;
            }
        }
        return inWord ? count + 1 : count;
    }

    private void generateRotations() {
        String originalText = new String(textData);
        String[] words = originalText.split("\\s+");
        int wordCount = words.length;

        // Generate all rotations
        for (int i = 0; i < wordCount; i++) {
            StringBuilder rotation = new StringBuilder();
            for (int j = 0; j < wordCount; j++) {
                rotation.append(words[(i + j) % wordCount]).append(" ");
            }
            rotations[i] = rotation.toString().trim();
        }
    }

    public void outputRotations(String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (String rotation : rotations) {
                writer.write(rotation);
                writer.newLine();
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java KWIC <inputFilePath> <outputFilePath>");
            return;
        }

        try {
            KWIC kwic = new KWIC(args[0]);
            kwic.outputRotations(args[1]);
            System.out.println("Rotations written to " + args[1]);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
