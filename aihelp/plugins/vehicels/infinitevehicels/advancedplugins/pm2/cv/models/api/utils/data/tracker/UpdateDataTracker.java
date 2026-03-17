package advancedplugins.pm2.cv.models.api.utils.data.tracker;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class UpdateDataTracker<T> extends DataTracker<T> {
   private final BiConsumer<T, T> setter;
   private final Function<T, T> supplier;

   public UpdateDataTracker(BiConsumer<T, T> var1) {
      this.setter = var1;
      this.supplier = (var0) -> {
         return var0;
      };
   }

   public UpdateDataTracker(T var1, BiConsumer<T, T> var2) {
      super(var1);
      this.setter = var2;
      this.supplier = (var0) -> {
         return var0;
      };
   }

   public UpdateDataTracker(T var1, BiConsumer<T, T> var2, Function<T, T> var3) {
      super(var1);
      this.setter = var2;
      this.supplier = var3;
   }

   public void set(T var1) {
      this.set(var1, (Runnable)null);
   }

   public void set(T var1, Runnable var2) {
      if (!this.protectAgainstChanges) {
         if (this.value == null || !this.value.equals(var1)) {
            this.setter.accept(this.value, var1);
            this.isDirty = true;
            if (var2 != null) {
               var2.run();
            }
         }

      }
   }

   public T get() {
      return this.supplier.apply(this.value);
   }
}
