package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.PrimitiveIterator.OfInt;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public interface IntArrayBinaryTag extends ArrayBinaryTag, Iterable<Integer> {
   @NotNull
   static IntArrayBinaryTag intArrayBinaryTag(@NotNull final int... value) {
      return new IntArrayBinaryTagImpl(value);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
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
