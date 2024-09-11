import java.io.IOException;

public class KWIC {
    private InputOutput io;
    private RotationGenerator rotationGenerator;
    private Sorter sorter;

    public KWIC(InputOutput io, RotationGenerator rotationGenerator, Sorter sorter) {
        this.io = io;
        this.rotationGenerator = rotationGenerator;
        this.sorter = sorter;
    }

    public void run(String inputFilePath, String outputFilePath) {
        try {
            String text = io.readInput(inputFilePath);
            String[] rotations = rotationGenerator.generateRotations(text);
            sorter.sort(rotations);
            io.writeOutput(outputFilePath, rotations);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java KWIC <input-file> <output-file>");
            System.exit(1);
        }

        KWIC kwic = new KWIC(new InputOutput(), new RotationGenerator(), new Sorter());
        kwic.run(args[0], args[1]);
    }
}
