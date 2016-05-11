package net.jbayes;

import java.util.*;

public class RandomUtil {

  private static final Random R = new Random(37L);

  private RandomUtil() {

  }

  public static double nextDouble() {
    return R.nextDouble();
  }
}
