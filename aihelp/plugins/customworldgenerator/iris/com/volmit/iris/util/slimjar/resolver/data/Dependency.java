package com.volmit.iris.util.slimjar.resolver.data;

import com.volmit.iris.util.slimjar.exceptions.NotComparableDependencyException;
import com.volmit.iris.util.slimjar.util.Serialization;
import java.io.DataInput;
import java.io.DataOutput;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Dependency(@NotNull String groupId, @NotNull String artifactId, @NotNull String version, @Nullable String snapshotId, @NotNull Collection<Dependency> transitive) implements Comparable<Dependency> {
   public Dependency(@NotNull String groupId, @NotNull String artifactId, @NotNull String version, @Nullable String snapshotId, @NotNull Collection<Dependency> transitive) {
      this.groupId = var1;
      this.artifactId = var2;
      this.version = var3;
      this.snapshotId = var4;
      this.transitive = var5;
   }

   @NotNull
   @Contract(
      pure = true
   )
   public static Dependency read(@NotNull DataInput var0) {
      return new Dependency(var0.readUTF(), var0.readUTF(), var0.readUTF(), var0.readBoolean() ? var0.readUTF() : null, Serialization.readList(var0, Dependency::read));
   }

   public boolean isSnapshot() {
      return this.snapshotId != null && !this.snapshotId.isEmpty();
   }

   public Optional<String> snapshot() {
      return this.isSnapshot() ? Optional.ofNullable(this.snapshotId) : Optional.empty();
   }

   public void write(@NotNull DataOutput var1) {
      var1.writeUTF(this.groupId);
      var1.writeUTF(this.artifactId);
      var1.writeUTF(this.version);
      if (this.snapshotId == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         var1.writeUTF(this.snapshotId);
      }

      Serialization.writeList(this.transitive, var1, Dependency::write);
   }

   @Contract(
      pure = true
   )
   @NotNull
   public String toString() {
      String var1 = this.isSnapshot() ? ":" + this.snapshotId() : "";
      String var10000 = this.groupId();
      return var10000 + ":" + this.artifactId() + ":" + this.version() + var1;
   }

   @Contract(
      value = "null -> false",
      pure = true
   )
   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Dependency)) {
         return false;
      } else {
         Dependency var2 = (Dependency)var1;
         return this.groupId.equals(var2.groupId) && this.artifactId.equals(var2.artifactId) && this.version.equals(var2.version);
      }
   }

   @Contract(
      pure = true
   )
   public int hashCode() {
      return Objects.hash(new Object[]{this.groupId, this.artifactId, this.version});
   }

   @Contract(
      pure = true
   )
   public int compareTo(@NotNull Dependency var1) {
      if (!this.equals(var1)) {
         throw new NotComparableDependencyException(this, var1);
      } else {
         return this.version().compareTo(var1.version());
      }
   }

   @NotNull
   public String groupId() {
      return this.groupId;
   }

   @NotNull
   public String artifactId() {
      return this.artifactId;
   }

   @NotNull
   public String version() {
      return this.version;
   }

   @Nullable
   public String snapshotId() {
      return this.snapshotId;
   }

   @NotNull
   public Collection<Dependency> transitive() {
      return this.transitive;
   }
}
