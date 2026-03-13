package com.volmit.iris.util.hunk.view;

import com.volmit.iris.util.hunk.Hunk;
import java.util.function.Function;

public class FunctionalHunkView<R, T> implements Hunk<T> {
   private final Hunk<R> src;
   private final Function<R, T> converter;
   private final Function<T, R> backConverter;

   public FunctionalHunkView(Hunk<R> src, Function<R, T> converter, Function<T, R> backConverter) {
      this.src = var1;
      this.converter = var2;
      this.backConverter = var3;
   }

   public void setRaw(int x, int y, int z, T t) {
      if (this.backConverter == null) {
         throw new UnsupportedOperationException("You cannot writeNodeData to this hunk (Read Only)");
      } else {
         this.src.setRaw(var1, var2, var3, this.backConverter.apply(var4));
      }
   }

   public T getRaw(int x, int y, int z) {
      if (this.converter == null) {
         throw new UnsupportedOperationException("You cannot read this hunk (Write Only)");
      } else {
         return this.converter.apply(this.src.getRaw(var1, var2, var3));
      }
   }

   public int getWidth() {
      return this.src.getWidth();
   }

   public int getDepth() {
      return this.src.getDepth();
   }

   public int getHeight() {
      return this.src.getHeight();
   }

   public Hunk<T> getSource() {
      throw new UnsupportedOperationException("You cannot read this hunk's source because it's a different type.");
   }
}
