package com.nisovin.shopkeepers.util.java;

import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;

public class Box<T> {
   @Nullable
   private T value;

   public Box() {
   }

   public Box(@Nullable T value) {
      this.value = value;
   }

   @Nullable
   public T getValue() {
      return this.value;
   }

   public void setValue(@Nullable T value) {
      this.value = value;
   }

   public int hashCode() {
      return Objects.hashCode(this.value);
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof Box)) {
         return false;
      } else {
         Box<?> other = (Box)obj;
         return Objects.equals(this.value, other.value);
      }
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Box [value=");
      builder.append(this.value);
      builder.append("]");
      return builder.toString();
   }
}
