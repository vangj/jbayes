package com.github.vangj.jbayes.inf.exact.graph.lpd;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PotentialEntryTest {

  @Test
  public void testMatch() {
    PotentialEntry ace = new PotentialEntry()
        .add("a", "on")
        .add("c", "on")
        .add("e", "on")
        .setValue(1.0d);

    assertTrue(ace.match(new PotentialEntry().add("c", "on").add("e", "on")));
    assertFalse(ace.match(new PotentialEntry().add("c", "on").add("e", "off")));
  }
}
