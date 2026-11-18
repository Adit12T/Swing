package io.github.adit12t.swing;

import java.util.ArrayList;
import java.util.List;

/**
 * Searches for runs of consecutive samples that meet a condition for at least {@code winLength}
 * samples. All searches are single-pass, O(n) in the size of the searched range.
 * Indexes are inclusive and zero-based; "not found" is -1.
 */
public final class SwingAnalyzer {

    public record Range(int start, int end) {
        public int length() {
            return end - start + 1;
        }
    }

    private SwingAnalyzer() {
    }

    /** First index from indexBegin to indexEnd where data stays above threshold for winLength samples in a row. */
    public static int searchContinuityAboveValue(double[] data, int indexBegin, int indexEnd,
                                                 double threshold, int winLength) {
        checkForward(data.length, indexBegin, indexEnd, winLength);
        int run = 0;
        for (int i = indexBegin; i <= indexEnd; i++) {
            run = data[i] > threshold ? run + 1 : 0;
            if (run == winLength) {
                return i - winLength + 1;
            }
        }
        return -1;
    }

    /**
     * Searches backwards from indexBegin down to indexEnd (indexBegin >= indexEnd) for winLength samples
     * in a row strictly between thresholdLo and thresholdHi. Returns the index where that run starts,
     * which is the highest index of the run, since the search moves backwards.
     */
    public static int backSearchContinuityWithinRange(double[] data, int indexBegin, int indexEnd,
                                                      double thresholdLo, double thresholdHi, int winLength) {
        checkBounds(data.length, indexEnd, indexBegin, winLength);
        checkRange(thresholdLo, thresholdHi);
        int run = 0;
        for (int i = indexBegin; i >= indexEnd; i--) {
            run = data[i] > thresholdLo && data[i] < thresholdHi ? run + 1 : 0;
            if (run == winLength) {
                return i + winLength - 1;
            }
        }
        return -1;
    }

    /** First index where data1 is above threshold1 and data2 above threshold2 for winLength samples in a row. */
    public static int searchContinuityAboveValueTwoSignals(double[] data1, double[] data2, int indexBegin, int indexEnd,
                                                           double threshold1, double threshold2, int winLength) {
        if (data1.length != data2.length) {
            throw new IllegalArgumentException("Both signals must have the same length");
        }
        checkForward(data1.length, indexBegin, indexEnd, winLength);
        int run = 0;
        for (int i = indexBegin; i <= indexEnd; i++) {
            run = data1[i] > threshold1 && data2[i] > threshold2 ? run + 1 : 0;
            if (run == winLength) {
                return i - winLength + 1;
            }
        }
        return -1;
    }

    /** Every maximal run of at least winLength samples strictly between thresholdLo and thresholdHi. */
    public static List<Range> searchMultiContinuityWithinRange(double[] data, int indexBegin, int indexEnd,
                                                               double thresholdLo, double thresholdHi, int winLength) {
        checkForward(data.length, indexBegin, indexEnd, winLength);
        checkRange(thresholdLo, thresholdHi);
        List<Range> ranges = new ArrayList<>();
        int start = -1;
        for (int i = indexBegin; i <= indexEnd + 1; i++) {
            boolean inRange = i <= indexEnd && data[i] > thresholdLo && data[i] < thresholdHi;
            if (inRange && start < 0) {
                start = i;
            } else if (!inRange && start >= 0) {
                if (i - start >= winLength) {
                    ranges.add(new Range(start, i - 1));
                }
                start = -1;
            }
        }
        return List.copyOf(ranges);
    }

    private static void checkForward(int length, int begin, int end, int winLength) {
        checkBounds(length, begin, end, winLength);
    }

    private static void checkBounds(int length, int low, int high, int winLength) {
        if (low < 0 || high >= length || low > high) {
            throw new IllegalArgumentException("Index range [" + low + ", " + high + "] is outside 0.." + (length - 1));
        }
        if (winLength < 1) {
            throw new IllegalArgumentException("winLength must be at least 1");
        }
    }

    private static void checkRange(double lo, double hi) {
        if (!(lo < hi)) {
            throw new IllegalArgumentException("thresholdLo must be below thresholdHi");
        }
    }
}
