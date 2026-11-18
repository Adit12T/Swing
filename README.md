# Swing Analyzer

[![CI](https://github.com/Adit12T/Swing/actions/workflows/ci.yml/badge.svg)](https://github.com/Adit12T/Swing/actions/workflows/ci.yml)

A small **Java 21** library and command-line tool that finds patterns in baseball-swing sensor data:
stretches where a signal stays above a threshold, or inside a range, for a minimum number of samples.

The data comes from a bat-mounted sensor that records a timestamp, acceleration (`ax`, `ay`, `az`) and
rotation rate (`wx`, `wy`, `wz`) for each sample. A sample swing with 1,276 readings is in
[`data/latestSwing.csv`](data/latestSwing.csv).

## Searches

All indexes are zero-based and inclusive; a search that finds nothing returns `-1`.

| Method | Finds |
|---|---|
| `searchContinuityAboveValue(data, begin, end, threshold, win)` | The first index where `data` stays above `threshold` for `win` samples in a row |
| `backSearchContinuityWithinRange(data, begin, end, lo, hi, win)` | Searching backwards from `begin` to `end`, the first run of `win` samples strictly between `lo` and `hi` |
| `searchContinuityAboveValueTwoSignals(d1, d2, begin, end, t1, t2, win)` | The first index where both signals are above their thresholds for `win` samples |
| `searchMultiContinuityWithinRange(data, begin, end, lo, hi, win)` | Every run of at least `win` samples between `lo` and `hi`, as start/end pairs |

Each search makes a single pass over the range, so it runs in linear time, and every input is checked
(range inside the data, `win` at least 1, `lo` below `hi`, equal-length signals).

## Command line

```bash
mvn -q package
mvn -q exec:java -Dexec.args="above data/latestSwing.csv az 0 5"
# Index 163 (t=203536)

mvn -q exec:java -Dexec.args="multi data/latestSwing.csv wz -5 5 20"
mvn -q exec:java -Dexec.args="two data/latestSwing.csv ax wz 1 100 3"
mvn -q exec:java -Dexec.args="back-range data/latestSwing.csv ay -1 1 10"
```

Bad CSV rows are reported with their line number instead of failing silently.

## What changed from the original

I first wrote this in 2017 as a NetBeans project. That version never ran correctly: its result arrays
were `null`, so every search crashed; it printed values instead of returning where the run started; and
it didn't check that the samples were consecutive. Version 2 rewrites the four searches so they return
the index (or ranges), adds a CSV loader and a command line, and covers each one with tests.

## Tests

```bash
mvn verify
```

## License

Copyright © 2026 Aditya Tripathi. All rights reserved. The code is published for viewing only; see
[LICENSE](LICENSE).
