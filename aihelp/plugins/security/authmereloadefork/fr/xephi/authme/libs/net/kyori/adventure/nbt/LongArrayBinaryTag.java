package fr.xephi.authme.libs.net.kyori.adventure.nbt;

import java.util.PrimitiveIterator.OfLong;
import java.util.function.LongConsumer;
import java.util.stream.LongStream;
import org.jetbrains.annotations.NotNull;

public interface LongArrayBinaryTag extends ArrayBinaryTag, Iterable<Long> {
   @NotNull
   static LongArrayBinaryTag of(@NotNull final long... value) {
      return new LongArrayBinaryTagImpl(value);
   }

   @NotNull
   default BinaryTagType<LongArrayBinaryTag> type() {
      return BinaryTagTypes.LONG_ARRAY;
   }

   @NotNull
   long[] value();

   int size();

   long get(final int index);

   @NotNull
   OfLong iterator();

   @NotNull
   java.util.Spliterator.OfLong spliterator();

   @NotNull
   LongStream stream();

   void forEachLong(@NotNull final LongConsumer action);
}
