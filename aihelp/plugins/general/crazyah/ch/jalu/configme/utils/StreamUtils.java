package ch.jalu.configme.utils;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class StreamUtils {
   private StreamUtils() {
   }

   public static <T> Stream<T> repeat(T element, int numberOfTimes) {
      return IntStream.range(0, numberOfTimes).mapToObj((i) -> {
         return element;
      });
   }
}
