package io.github.adit12t.swing;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * One swing recorded by a bat sensor: a timestamp plus accelerometer (ax, ay, az) and
 * gyroscope (wx, wy, wz) readings per sample. Columns are stored as arrays for fast scanning.
 */
public record SwingData(long[] timestamps, double[] ax, double[] ay, double[] az,
                        double[] wx, double[] wy, double[] wz) {

    public enum Column { AX, AY, AZ, WX, WY, WZ }

    public int size() {
        return timestamps.length;
    }

    public double[] column(Column column) {
        return switch (column) {
            case AX -> ax;
            case AY -> ay;
            case AZ -> az;
            case WX -> wx;
            case WY -> wy;
            case WZ -> wz;
        };
    }

    public static Column parseColumn(String name) {
        try {
            return Column.valueOf(name.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown column '" + name + "'. Use ax, ay, az, wx, wy or wz.");
        }
    }

    /** Reads "timestamp,ax,ay,az,wx,wy,wz" rows; a header row is optional. Bad rows report their line number. */
    public static SwingData readCsv(Path file) {
        List<String> lines;
        try {
            lines = Files.readAllLines(file);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read " + file, e);
        }
        List<double[]> rows = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty() || (i == 0 && Character.isLetter(line.charAt(0)))) {
                continue;
            }
            String[] parts = line.split(",");
            if (parts.length != 7) {
                throw new IllegalArgumentException("Line " + (i + 1) + ": expected 7 values, found " + parts.length);
            }
            double[] row = new double[7];
            for (int c = 0; c < 7; c++) {
                try {
                    row[c] = Double.parseDouble(parts[c].trim());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Line " + (i + 1) + ": '" + parts[c] + "' is not a number");
                }
                if (!Double.isFinite(row[c])) {
                    throw new IllegalArgumentException("Line " + (i + 1) + ": values must be finite");
                }
            }
            rows.add(row);
        }
        int n = rows.size();
        long[] t = new long[n];
        double[][] cols = new double[6][n];
        for (int r = 0; r < n; r++) {
            t[r] = (long) rows.get(r)[0];
            for (int c = 0; c < 6; c++) {
                cols[c][r] = rows.get(r)[c + 1];
            }
        }
        return new SwingData(t, cols[0], cols[1], cols[2], cols[3], cols[4], cols[5]);
    }
}
