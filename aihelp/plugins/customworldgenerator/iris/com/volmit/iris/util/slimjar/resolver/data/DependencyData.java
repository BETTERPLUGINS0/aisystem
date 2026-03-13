package com.volmit.iris.util.slimjar.resolver.data;

import com.volmit.iris.util.slimjar.relocation.RelocationRule;
import com.volmit.iris.util.slimjar.util.Serialization;
import java.io.DataInput;
import java.io.DataOutput;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record DependencyData(@NotNull Collection<Mirror> mirrors, @NotNull Collection<Repository> repositories, @NotNull Collection<Dependency> dependencies, @NotNull Collection<RelocationRule> relocations) {
   @Contract(
      pure = true
   )
   public DependencyData(@NotNull final Collection<Mirror> mirrors, @NotNull final Collection<Repository> repositories, @NotNull final Collection<Dependency> dependencies, @NotNull final Collection<RelocationRule> relocations) {
      this.mirrors = Collections.unmodifiableCollection(var1);
      this.repositories = Collections.unmodifiableCollection(var2);
      this.dependencies = Collections.unmodifiableCollection(var3);
      this.relocations = Collections.unmodifiableCollection(var4);
   }

   @NotNull
   @Contract(
      pure = true
   )
   public static DependencyData read(@NotNull DataInput var0) {
      return new DependencyData(Serialization.readList(var0, Mirror::read), Serialization.readList(var0, Repository::read), Serialization.readList(var0, Dependency::read), Serialization.readList(var0, RelocationRule::read));
   }

   public void write(@NotNull DataOutput var1) {
      Serialization.writeList(this.mirrors, var1, Mirror::write);
      Serialization.writeList(this.repositories, var1, Repository::write);
      Serialization.writeList(this.dependencies, var1, Dependency::write);
      Serialization.writeList(this.relocations, var1, RelocationRule::write);
   }

   @Contract(
      value = "null -> false",
      pure = true
   )
   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof DependencyData)) {
         return false;
      } else {
         DependencyData var2 = (DependencyData)var1;
         return this.isCollectionEqual(this.repositories, var2.repositories) && this.isCollectionEqual(this.dependencies, var2.dependencies) && this.isCollectionEqual(this.relocations, var2.relocations);
      }
   }

   @Contract(
      pure = true
   )
   private <T> boolean isCollectionEqual(@NotNull Collection<T> var1, @NotNull Collection<T> var2) {
      return var1.containsAll(var2) && var2.containsAll(var1);
   }

   @Contract(
      pure = true
   )
   public int hashCode() {
      return Objects.hash(new Object[]{this.repositories, this.dependencies, this.relocations});
   }

   @Contract(
      pure = true
   )
   @NotNull
   public String toString() {
      String var10000 = String.valueOf(this.mirrors);
      return "DependencyData{mirrors=" + var10000 + ", repositories=" + String.valueOf(this.repositories) + ", dependencies=" + String.valueOf(this.dependencies) + ", relocations=" + String.valueOf(this.relocations) + "}";
   }

   @NotNull
   public Collection<Mirror> mirrors() {
      return this.mirrors;
   }

   @NotNull
   public Collection<Repository> repositories() {
      return this.repositories;
   }

   @NotNull
   public Collection<Dependency> dependencies() {
      return this.dependencies;
   }

   @NotNull
   public Collection<RelocationRule> relocations() {
      return this.relocations;
   }
}
