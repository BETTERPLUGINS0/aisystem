package com.volmit.iris.util.slimjar.resolver.data;

import com.volmit.iris.util.slimjar.util.Serialization;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Serializable;
import java.net.URL;
import java.util.Objects;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Mirror(@NotNull URL mirroring, @NotNull URL original) implements Serializable {
   private static final long serialVersionUID = 1L;

   public Mirror(@NotNull URL mirroring, @NotNull URL original) {
      this.mirroring = var1;
      this.original = var2;
   }

   @NotNull
   @Contract(
      pure = true
   )
   public static Mirror read(@NotNull DataInput var0) {
      return new Mirror(Serialization.readURL(var0), Serialization.readURL(var0));
   }

   public void write(@NotNull DataOutput var1) {
      Serialization.writeURL(this.mirroring, var1);
      Serialization.writeURL(this.original, var1);
   }

   @Contract(
      pure = true
   )
   @NotNull
   public String toString() {
      String var10000 = String.valueOf(this.mirroring);
      return "Mirror{mirroring=" + var10000 + ", original=" + String.valueOf(this.original) + "}";
   }

   @Contract(
      value = "null -> false",
      pure = true
   )
   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Mirror)) {
         return false;
      } else {
         Mirror var2 = (Mirror)var1;
         return Objects.equals(this.mirroring, var2.mirroring) && Objects.equals(this.original, var2.original);
      }
   }

   @NotNull
   public URL mirroring() {
      return this.mirroring;
   }

   @NotNull
   public URL original() {
      return this.original;
   }
}
