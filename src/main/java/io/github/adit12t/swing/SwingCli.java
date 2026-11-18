package io.github.adit12t.swing;

import io.github.adit12t.swing.SwingAnalyzer.Range;
import io.github.adit12t.swing.SwingData.Column;

import java.nio.file.Path;
import java.util.List;

/**
 * Command-line entry point.
 * <pre>
 *   above      FILE COLUMN THRESHOLD WIN [BEGIN END]
 *   back-range FILE COLUMN LO HI WIN [BEGIN END]       (BEGIN >= END)
 *   two        FILE COL1 COL2 T1 T2 WIN [BEGIN END]
 *   multi      FILE COLUMN LO HI WIN [BEGIN END]
 * </pre>
 */
public final class SwingCli {

    private SwingCli() {
    }

    public static void main(String[] args) {
        try {
            System.out.println(run(args));
        } catch (IllegalArgumentException | java.io.UncheckedIOException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println(usage());
            System.exit(1);
        }
    }

    static String run(String[] a) {
        if (a.length < 2) {
            throw new IllegalArgumentException("Not enough arguments");
        }
        SwingData data = SwingData.readCsv(Path.of(a[1]));
        int last = data.size() - 1;
        return switch (a[0]) {
            case "above" -> {
                need(a, 5);
                int[] r = range(a, 5, 0, last);
                yield describe(SwingAnalyzer.searchContinuityAboveValue(col(data, a[2]), r[0], r[1], num(a[3]), integer(a[4])), data);
            }
            case "back-range" -> {
                need(a, 6);
                int[] r = range(a, 6, last, 0);
                yield describe(SwingAnalyzer.backSearchContinuityWithinRange(col(data, a[2]), r[0], r[1], num(a[3]), num(a[4]), integer(a[5])), data);
            }
            case "two" -> {
                need(a, 7);
                int[] r = range(a, 7, 0, last);
                yield describe(SwingAnalyzer.searchContinuityAboveValueTwoSignals(col(data, a[2]), col(data, a[3]),
                        r[0], r[1], num(a[4]), num(a[5]), integer(a[6])), data);
            }
            case "multi" -> {
                need(a, 6);
                int[] r = range(a, 6, 0, last);
                List<Range> ranges = SwingAnalyzer.searchMultiContinuityWithinRange(col(data, a[2]), r[0], r[1],
                        num(a[3]), num(a[4]), integer(a[5]));
                yield ranges.isEmpty() ? "No matching ranges." : ranges.stream()
                        .map(x -> "samples " + x.start() + "-" + x.end() + " (t=" + data.timestamps()[x.start()]
                                + ".." + data.timestamps()[x.end()] + ")")
                        .reduce((p, q) -> p + System.lineSeparator() + q).orElseThrow();
            }
            default -> throw new IllegalArgumentException("Unknown command '" + a[0] + "'");
        };
    }

    private static String describe(int index, SwingData data) {
        return index < 0 ? "Not found." : "Index " + index + " (t=" + data.timestamps()[index] + ")";
    }

    private static double[] col(SwingData d, String name) {
        Column column = SwingData.parseColumn(name);
        return d.column(column);
    }

    private static void need(String[] a, int n) {
        if (a.length != n && a.length != n + 2) {
            throw new IllegalArgumentException("Wrong number of arguments for " + a[0]);
        }
    }

    private static int[] range(String[] a, int at, int defBegin, int defEnd) {
        return a.length >= at + 2 ? new int[]{integer(a[at]), integer(a[at + 1])} : new int[]{defBegin, defEnd};
    }

    private static double num(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + s + "' is not a number");
        }
    }

    private static int integer(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + s + "' is not a whole number");
        }
    }

    static String usage() {
        return """
                Usage:
                  above      FILE COLUMN THRESHOLD WIN [BEGIN END]
                  back-range FILE COLUMN LO HI WIN [BEGIN END]   (searches from BEGIN down to END)
                  two        FILE COL1 COL2 T1 T2 WIN [BEGIN END]
                  multi      FILE COLUMN LO HI WIN [BEGIN END]
                Columns: ax, ay, az, wx, wy, wz""";
    }
}
