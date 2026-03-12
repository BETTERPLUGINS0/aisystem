package fr.xephi.authme.libs.net.kyori.adventure.nbt;

import java.util.PrimitiveIterator.OfInt;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import org.jetbrains.annotations.NotNull;

public interface IntArrayBinaryTag extends ArrayBinaryTag, Iterable<Integer> {
   @NotNull
   static IntArrayBinaryTag of(@NotNull final int... value) {
      return new IntArrayBinaryTagImpl(value);
   }

   @NotNull
   default BinaryTagType<IntArrayBinaryTag> type() {
      return BinaryTagTypes.INT_ARRAY;
   }

   @NotNull
   int[] value();

   int size();

   int get(final int index);

   @NotNull
   OfInt iterator();

   @NotNull
   java.util.Spliterator.OfInt spliterator();

   @NotNull
   IntStream stream();

   void forEachInt(@NotNull final IntConsumer action);
}
