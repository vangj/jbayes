package com.github.vangj.jbayes.inference.util;

import au.com.bytecode.opencsv.CSVWriter;
import com.github.vangj.jbayes.inference.Graph;

import java.io.IOException;
import java.io.Writer;

/**
 * CSV util.
 */
public class CsvUtil {
  private CsvUtil() {

  }

  public static void saveSamples(Graph graph, Writer writer) throws IOException {
    String[] values = graph.getNodeNames();
    try (CSVWriter csv =
        new CSVWriter(writer,
            CSVWriter.DEFAULT_SEPARATOR,
            CSVWriter.DEFAULT_QUOTE_CHARACTER)) {
      csv.writeNext(graph.getNodeNames());
      graph.getSamples().forEach(line -> csv.writeNext(line));
    }
  }
}
