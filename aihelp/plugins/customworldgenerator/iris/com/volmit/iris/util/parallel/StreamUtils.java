package com.volmit.iris.util.parallel;

import com.volmit.iris.util.math.Position2;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;

public class StreamUtils {
   public static Stream<Position2> streamRadius(int x, int z, int radius) {
      return streamRadius(var0, var1, var2, var2);
   }

   public static Stream<Position2> streamRadius(int x, int z, int radiusX, int radiusZ) {
      return IntStream.rangeClosed(-var2, var2).mapToObj((var3x) -> {
         return IntStream.rangeClosed(-var3, var3).mapToObj((var3xx) -> {
            return new Position2(var0 + var3x, var1 + var3xx);
         });
      }).flatMap(Function.identity());
   }

   public static <T, M> void forEach(Stream<T> stream, Function<T, Stream<M>> mapper, Consumer<M> consumer, @Nullable MultiBurst burst) {
      forEach(var0.flatMap(var1), var2, var3);
   }

   public static <T> void forEach(Stream<T> stream, Consumer<T> task, @Nullable MultiBurst burst) {
      try {
         if (var2 == null) {
            var0.forEach(var1);
         } else {
            List var3 = var0.toList();
            BurstExecutor var4 = var2.burst(var3.size());
            var3.forEach((var2x) -> {
               var4.queue(() -> {
                  var1.accept(var2x);
               });
            });
            var4.complete();
         }

      } catch (Throwable var5) {
         throw var5;
      }
   }
}
