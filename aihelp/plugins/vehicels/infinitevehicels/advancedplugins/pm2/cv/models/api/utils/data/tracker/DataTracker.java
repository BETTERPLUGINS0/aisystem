package advancedplugins.pm2.cv.models.api.utils.data.tracker;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import lombok.Generated;

public class DataTracker<T> {
   protected final BiPredicate<T, T> equal;
   protected boolean isDirty;
   protected T value;
   protected boolean protectAgainstChanges;

   public DataTracker() {
      this.equal = Object::equals;
   }

   public DataTracker(T var1) {
      this.value = var1;
      this.equal = Object::equals;
   }

   public DataTracker(BiPredicate<T, T> var1) {
      this.equal = var1;
   }

   public void markDirty() {
      this.isDirty = true;
   }

   public void clearDirty() {
      this.isDirty = false;
   }

   public void ifDirty(Consumer<T> var1) {
      if (this.isDirty) {
         var1.accept(this.get());
      }

   }

   public void ifDirty(Consumer<T> var1, boolean var2) {
      if (this.isDirty || var2) {
         var1.accept(this.get());
      }

   }

   public void set(T var1) {
      this.set(var1, (Runnable)null);
   }

   public void set(T var1, Runnable var2) {
      if (!this.protectAgainstChanges) {
         if (this.value == null || !this.equal.test(this.value, var1)) {
            this.value = var1;
            this.isDirty = true;
            if (var2 != null) {
               var2.run();
            }
         }

      }
   }

   public T get() {
      return this.value;
   }

   public boolean isDirty() {
      return this.isDirty;
   }

   @Generated
   public void setProtectAgainstChanges(boolean var1) {
      this.protectAgainstChanges = var1;
   }

   @Generated
   public boolean isProtectAgainstChanges() {
      return this.protectAgainstChanges;
   }
}
