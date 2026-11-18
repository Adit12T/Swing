package io.github.adit12t.swing;

import io.github.adit12t.swing.SwingAnalyzer.Range;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SwingAnalyzerTest {

    private static final double[] DATA = {0, 5, 6, 7, 1, 8, 9, 10, 11, 2};

    @Test
    void findsTheFirstRunAboveAThreshold() {
        assertEquals(1, SwingAnalyzer.searchContinuityAboveValue(DATA, 0, 9, 4, 3));
        assertEquals(5, SwingAnalyzer.searchContinuityAboveValue(DATA, 0, 9, 4, 4));
        assertEquals(-1, SwingAnalyzer.searchContinuityAboveValue(DATA, 0, 9, 4, 5));
        assertEquals(-1, SwingAnalyzer.searchContinuityAboveValue(DATA, 0, 2, 4, 3), "run must fit inside the range");
    }

    @Test
    void thresholdIsStrict() {
        assertEquals(-1, SwingAnalyzer.searchContinuityAboveValue(new double[]{4, 4, 4}, 0, 2, 4, 1));
    }

    @Test
    void searchesBackwardsWithinARange() {
        // From index 9 down to 0, values strictly between 4 and 10: 9,8 (idx 6,5) then break at 4 (=1).
        assertEquals(6, SwingAnalyzer.backSearchContinuityWithinRange(DATA, 9, 0, 4, 10, 2));
        assertEquals(3, SwingAnalyzer.backSearchContinuityWithinRange(DATA, 3, 0, 4, 10, 3));
        assertEquals(-1, SwingAnalyzer.backSearchContinuityWithinRange(DATA, 9, 0, 4, 10, 4));
    }

    @Test
    void requiresBothSignalsAtOnce() {
        double[] other = {9, 9, 0, 9, 9, 9, 9, 9, 0, 0};
        assertEquals(5, SwingAnalyzer.searchContinuityAboveValueTwoSignals(DATA, other, 0, 9, 4, 4, 3));
        assertEquals(-1, SwingAnalyzer.searchContinuityAboveValueTwoSignals(DATA, other, 0, 9, 4, 4, 4));
    }

    @Test
    void listsEveryLongEnoughRange() {
        assertEquals(List.of(new Range(1, 3), new Range(5, 6)),
                SwingAnalyzer.searchMultiContinuityWithinRange(DATA, 0, 9, 4, 10, 2));
        assertEquals(List.of(new Range(1, 3)), SwingAnalyzer.searchMultiContinuityWithinRange(DATA, 0, 9, 4, 10, 3));
        assertEquals(List.of(new Range(7, 8)), SwingAnalyzer.searchMultiContinuityWithinRange(DATA, 7, 8, 9, 20, 1),
                "a run touching the end of the range counts");
    }

    @Test
    void rejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> SwingAnalyzer.searchContinuityAboveValue(DATA, 0, 10, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> SwingAnalyzer.searchContinuityAboveValue(DATA, 5, 2, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> SwingAnalyzer.searchContinuityAboveValue(DATA, 0, 9, 1, 0));
        assertThrows(IllegalArgumentException.class, () -> SwingAnalyzer.backSearchContinuityWithinRange(DATA, 9, 0, 5, 5, 1));
        assertThrows(IllegalArgumentException.class,
                () -> SwingAnalyzer.searchContinuityAboveValueTwoSignals(DATA, new double[3], 0, 2, 1, 1, 1));
    }

    @Test
    void loadsTheSampleSwingAndFindsTheImpact() {
        SwingData swing = SwingData.readCsv(Path.of("data/latestSwing.csv"));
        assertEquals(1276, swing.size());
        int index = SwingAnalyzer.searchContinuityAboveValue(swing.az(), 0, swing.size() - 1, 0, 5);
        assertEquals(true, index >= 0, "the bat's z-acceleration turns positive during the swing");
    }

    @Test
    void reportsTheLineOfABadRow() throws Exception {
        Path file = Files.createTempFile("swing", ".csv");
        Files.writeString(file, "timestamp,ax,ay,az,wx,wy,wz\n0,1,2,3,4,5,6\n1,1,2,x,4,5,6\n");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> SwingData.readCsv(file));
        assertEquals("Line 3: 'x' is not a number", e.getMessage());
        Files.delete(file);
    }

    @Test
    void cliPrintsResults() {
        assertEquals("No matching ranges.", SwingCli.run(new String[]{"multi", "data/latestSwing.csv", "ax", "1000", "2000", "1"}));
        assertEquals(true, SwingCli.run(new String[]{"above", "data/latestSwing.csv", "az", "0", "5"}).startsWith("Index "));
    }
}
