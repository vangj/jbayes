package com.github.vangj.jbayes.inf.prob.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.vangj.jbayes.inf.prob.Graph;
import com.github.vangj.jbayes.inf.prob.json.JsonGraph;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.commons.io.IOUtils;

/**
 * JSON util.
 */
public class JsonUtil {

  private static final ObjectMapper _mapper = new ObjectMapper();

  static {
    _mapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  private JsonUtil() {

  }

  /**
   * Serializes Graph.
   *
   * @param g  Graph.
   * @param os OutputStream.
   * @throws IOException For any IO errors.
   */
  public static void serialize(Graph g, OutputStream os) throws IOException {
    IOUtils.write(toJson(new JsonGraph(g)), os, "UTF-8");
  }

  /**
   * Deserializes back to Graph.
   *
   * @param is InputStream.
   * @return Graph.
   * @throws IOException For any IO errors.
   */
  public static Graph deserialize(InputStream is) throws IOException {
    return ((JsonGraph) fromJson(IOUtils.toString(is, StandardCharsets.UTF_8), JsonGraph.class)).toGraph();
  }

  /**
   * Converts JSON to object based on type reference.
   *
   * @param type TypeReference.
   * @param json JSON.
   * @param <T>  Type.
   * @return Object.
   * @throws IOException If any errors occur.
   */
  public static <T> T fromJson(final TypeReference<T> type, final String json) throws IOException {
    T data = _mapper.readValue(json, type);
    return data;
  }

  /**
   * Converts Map to object the specified clazz.
   *
   * @param map   Map.
   * @param clazz Clazz.
   * @return Object.
   */
  public static Object toObject(Map<String, Object> map, Class<?> clazz) {
    return _mapper.convertValue(map, clazz);
  }

  /**
   * Converts JSON to clazz.
   *
   * @param json  JSON.
   * @param clazz Clazz to deserialize object instance from JSON.
   * @return Object.
   */
  public static Object fromJson(String json, Class<?> clazz) {
    try {
      return _mapper.readValue(json, clazz);
    } catch (Exception e) {
      throw new RuntimeException("Cannot deserialize JSON to object", e);
    }
  }

  /**
   * Converts an object to JSON.
   *
   * @param o Object.
   * @return JSON.
   */
  public static String toJson(Object o) {
    try {
      StringWriter w = new StringWriter();
      _mapper.writeValue(w, o);
      return w.toString();
    } catch (Exception e) {
      throw new RuntimeException("Cannot serialize object as JSON", e);
    }
  }
}
