package main.java.com.example.utilities;

public class ProgressBar {

    private static final char incomplete = '░'; // U+2591 Unicode Character
    private static final char complete = '█'; // U+2588 Unicode Character

    private final String message;
    private final int size;
    private final long length;
    private final float multiplyFactor;

    public ProgressBar (String message, int size, long length) {
        this.message = message;
        this.size = size;
        this.length = length;
        this.multiplyFactor = (float) size / length;
    }

    public void update(long currentData) {
        int charCount = (int) ((float) currentData * this.multiplyFactor);
        int percent = (int) (((float) currentData / length) * 100);
        String progressBar = "\r" + message + ":  " + percent + "% | " + String.valueOf(complete).repeat(charCount)
                + String.valueOf(incomplete).repeat(size - charCount)
                + " | (" + (currentData / 1000) + " KB /" + " " +(length / 1000) + " KB)";

        System.out.print(progressBar);
    }
}
