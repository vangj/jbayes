package com.github.vangj.jbayes.inference;

import java.util.Random;

/**
 * Util class to generate random numbers.
 */
public class RandomUtil {

  private static final Random R = new Random(37L);

  private RandomUtil() {

  }

  public static double nextDouble() {
    return R.nextDouble();
  }
}
