package ac.grim.grimac.shaded.fastutil.longs;

public abstract class AbstractLongSpliterator implements LongSpliterator {
   protected AbstractLongSpliterator() {
   }

   public final boolean tryAdvance(LongConsumer action) {
      return this.tryAdvance(action);
   }

   public final void forEachRemaining(LongConsumer action) {
      this.forEachRemaining(action);
   }
}
