package me.PM2.infinitevehicles.xseries.reflection.asm;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

final class ArrayInsnGenerator {
   private final GeneratorAdapter mv;
   private final int length;
   private int index = 0;
   private final int storeInsn;

   public ArrayInsnGenerator(GeneratorAdapter var1, Class<?> var2, int var3) {
      if (var2.getComponentType() != null) {
         throw new IllegalArgumentException("The raw array element type must be given, not the array type itself: " + var2);
      } else {
         this.mv = var1;
         this.length = var3;
         Type var4 = Type.getType(var2);
         this.storeInsn = var2 == Object.class ? -1 : var4.getOpcode(79);
         var1.push(var3);
         var1.newArray(var4);
      }
   }

   private boolean isDynamicStoreInsn() {
      return this.storeInsn == -1;
   }

   public void add(Runnable var1) {
      if (this.isDynamicStoreInsn()) {
         throw new IllegalStateException("Must provide the type of stored object since this is a dynamic type array");
      } else {
         this.add(var1, this.storeInsn);
      }
   }

   public void add(Type var1, Runnable var2) {
      this.add(var2, var1.getOpcode(79));
   }

   private void add(Runnable var1, int var2) {
      if (this.index >= this.length) {
         throw new IllegalStateException("Array is already full, at index " + this.index);
      } else {
         this.mv.visitInsn(89);
         this.mv.push(this.index++);
         var1.run();
         this.mv.visitInsn(var2);
      }
   }
}
