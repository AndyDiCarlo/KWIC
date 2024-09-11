
public class RotationGenerator {
    public String[] generateRotations(String text) {
        String[] words = text.toLowerCase().split("\\s+");
        int wordCount = words.length;
        String[] rotations = new String[wordCount];

        for (int i = 0; i < wordCount; i++) {
            StringBuilder rotation = new StringBuilder();
            for (int j = 0; j < wordCount; j++) {
                rotation.append(words[(i + j) % wordCount]).append(" ");
            }
            rotations[i] = rotation.toString().trim();
        }

        return rotations;
    }
}
