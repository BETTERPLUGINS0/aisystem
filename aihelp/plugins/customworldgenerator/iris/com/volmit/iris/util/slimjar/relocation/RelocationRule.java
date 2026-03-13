package com.volmit.iris.util.slimjar.relocation;

import com.volmit.iris.util.slimjar.util.Serialization;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record RelocationRule(@NotNull String originalPackagePattern, @NotNull String relocatedPackagePattern, @NotNull Collection<String> exclusions, @NotNull Collection<String> inclusions) implements Serializable {
   private static final long serialVersionUID = 1L;

   public RelocationRule(@NotNull String var1, @NotNull String var2) {
      this(var1, var2, Collections.emptyList(), Collections.emptyList());
   }

   public RelocationRule(@NotNull String originalPackagePattern, @NotNull String relocatedPackagePattern, @NotNull Collection<String> exclusions, @NotNull Collection<String> inclusions) {
      this.originalPackagePattern = var1;
      this.relocatedPackagePattern = var2;
      this.exclusions = var3;
      this.inclusions = var4;
   }

   @NotNull
   @Contract(
      pure = true
   )
   public static RelocationRule read(@NotNull DataInput var0) {
      return new RelocationRule(var0.readUTF(), var0.readUTF(), Serialization.readList(var0, DataInput::readUTF), Serialization.readList(var0, DataInput::readUTF));
   }

   public void write(@NotNull DataOutput var1) {
      var1.writeUTF(this.originalPackagePattern);
      var1.writeUTF(this.relocatedPackagePattern);
      Serialization.writeList(this.exclusions, var1, (var0, var1x) -> {
         var1x.writeUTF(var0);
      });
      Serialization.writeList(this.inclusions, var1, (var0, var1x) -> {
         var1x.writeUTF(var0);
      });
   }

   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof RelocationRule)) {
         return false;
      } else {
         RelocationRule var2 = (RelocationRule)var1;
         return this.originalPackagePattern.equals(var2.originalPackagePattern) && this.relocatedPackagePattern.equals(var2.relocatedPackagePattern);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.originalPackagePattern, this.relocatedPackagePattern});
   }

   @NotNull
   public String toString() {
      return "RelocationRule{originalPackagePattern='" + this.originalPackagePattern + "', relocatedPackagePattern='" + this.relocatedPackagePattern + "'}";
   }

   @NotNull
   public String originalPackagePattern() {
      return this.originalPackagePattern;
   }

   @NotNull
   public String relocatedPackagePattern() {
      return this.relocatedPackagePattern;
   }

   @NotNull
   public Collection<String> exclusions() {
      return this.exclusions;
   }

   @NotNull
   public Collection<String> inclusions() {
      return this.inclusions;
   }
}
