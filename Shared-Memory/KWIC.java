import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

// Run code with "java KWIC input.txt output.txt"
public class KWIC {
    private char[] textData; // Shared memory: text data stored as characters
    private char[][] rotations; // Stores all rotations as character arrays

    public KWIC(String filePath) throws IOException {
        textData = kwicInput(filePath); // Read as char array
        int wordCount = countWords(textData);
        rotations = new char[wordCount][];
        generateRotations(); // Generate the rotations
        sortRotations(); // Sort the rotations alphabetically
    }

    // Read file character by character and store in a char array
    private char[] kwicInput(String filePath) throws IOException {
        CharArrayWriter charWriter = new CharArrayWriter();
        try (FileReader reader = new FileReader(filePath)) {
            int ch;
            while ((ch = reader.read()) != -1) {
                charWriter.write(Character.toLowerCase(ch)); // Convert to lowercase while reading
            }
        }
        return charWriter.toCharArray();
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
        char[] originalText = textData;
        char[][] words = splitWords(originalText);
        int wordCount = words.length;

        // Generate all rotations
        for (int i = 0; i < wordCount; i++) {
            char[] rotation = new char[originalText.length];
            int position = 0;
            for (int j = 0; j < wordCount; j++) {
                char[] word = words[(i + j) % wordCount];
                System.arraycopy(word, 0, rotation, position, word.length);
                position += word.length;
                if (j < wordCount - 1) {
                    rotation[position++] = ' '; // Add space between words
                }
            }
            rotations[i] = Arrays.copyOf(rotation, position); // Trim to correct size
        }
    }

    private char[][] splitWords(char[] text) {
        CharArrayWriter wordWriter = new CharArrayWriter();
        CharArrayWriter[] wordsTemp = new CharArrayWriter[text.length]; // Temp storage
        int wordIndex = 0;
        boolean inWord = false;

        for (char c : text) {
            if (Character.isWhitespace(c)) {
                if (inWord) {
                    wordsTemp[wordIndex] = new CharArrayWriter();
                    wordsTemp[wordIndex].write(wordWriter.toCharArray(), 0, wordWriter.size());
                    wordWriter.reset();
                    wordIndex++;
                    inWord = false;
                }
            } else {
                wordWriter.write(c);
                inWord = true;
            }
        }
        if (inWord) { // Handle the last word
            wordsTemp[wordIndex] = new CharArrayWriter();
            wordsTemp[wordIndex].write(wordWriter.toCharArray(), 0, wordWriter.size());
            wordIndex++;
        }

        // Convert to fixed array size
        char[][] words = new char[wordIndex][];
        for (int i = 0; i < wordIndex; i++) {
            words[i] = wordsTemp[i].toCharArray();
        }
        return words;
    }

    private void sortRotations() {
        Arrays.sort(rotations, (a, b) -> compareCharArrays(a, b));
    }
    
    private int compareCharArrays(char[] a, char[] b) {
        int length = Math.min(a.length, b.length);
        for (int i = 0; i < length; i++) {
            if (a[i] != b[i]) {
                return a[i] - b[i];
            }
        }
        return a.length - b.length; // If all characters are equal, shorter array comes first
    }

    public void outputRotations(String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (char[] rotation : rotations) {
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
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
