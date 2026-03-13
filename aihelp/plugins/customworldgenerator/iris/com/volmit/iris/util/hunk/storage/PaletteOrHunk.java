package com.volmit.iris.util.hunk.storage;

import com.volmit.iris.util.function.Consumer4;
import com.volmit.iris.util.function.Consumer4IO;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.hunk.bits.DataContainer;
import com.volmit.iris.util.hunk.bits.Writable;
import java.util.function.Supplier;

public abstract class PaletteOrHunk<T> extends StorageHunk<T> implements Hunk<T>, Writable<T> {
   private final Hunk<T> hunk;

   public PaletteOrHunk(int width, int height, int depth, boolean allow, Supplier<Hunk<T>> factory) {
      super(var1, var2, var3);
      this.hunk = (Hunk)(var4 && var1 * var2 * var3 <= 4096 ? new PaletteHunk(var1, var2, var3, this) : (Hunk)var5.get());
   }

   public DataContainer<T> palette() {
      return this.isPalette() ? ((PaletteHunk)this.hunk).getData() : null;
   }

   public boolean isPalette() {
      return this.hunk instanceof PaletteHunk;
   }

   public void setPalette(DataContainer<T> c) {
      if (this.isPalette()) {
         ((PaletteHunk)this.hunk).setPalette(var1);
      }

   }

   public void setRaw(int x, int y, int z, T t) {
      this.hunk.setRaw(var1, var2, var3, var4);
   }

   public T getRaw(int x, int y, int z) {
      return this.hunk.getRaw(var1, var2, var3);
   }

   public int getEntryCount() {
      return this.hunk.getEntryCount();
   }

   public boolean isMapped() {
      return this.hunk.isMapped();
   }

   public boolean isEmpty() {
      return this.hunk.isMapped();
   }

   public synchronized Hunk<T> iterateSync(Consumer4<Integer, Integer, Integer, T> c) {
      this.hunk.iterateSync(var1);
      return this;
   }

   public synchronized Hunk<T> iterateSyncIO(Consumer4IO<Integer, Integer, Integer, T> c) {
      this.hunk.iterateSyncIO(var1);
      return this;
   }

   public void empty(T b) {
      this.hunk.empty(var1);
   }
}
