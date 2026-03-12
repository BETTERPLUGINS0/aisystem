package fr.xephi.authme.libs.org.postgresql.util.internal;

import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.dataflow.qual.Pure;

public class Nullness {
   @Pure
   @EnsuresNonNull({"#1"})
   public static <T> T castNonNull(T ref) {
      assert ref != null : "Misuse of castNonNull: called with a null argument";

      return ref;
   }

   @Pure
   @EnsuresNonNull({"#1"})
   public static <T> T castNonNull(T ref, String message) {
      assert ref != null : "Misuse of castNonNull: called with a null argument " + message;

      return ref;
   }
}
