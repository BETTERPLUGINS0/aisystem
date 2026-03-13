package com.volmit.iris.util.slimjar.resolver;

import com.volmit.iris.util.slimjar.resolver.data.Repository;
import com.volmit.iris.util.slimjar.util.Serialization;
import java.io.DataInput;
import java.io.DataOutput;
import java.net.URL;
import java.util.Objects;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ResolutionResult {
   @NotNull
   private final Repository repository;
   @NotNull
   private final URL dependencyURL;
   @Nullable
   private final URL checksumURL;
   private final boolean aggregator;
   private transient boolean checked;

   @Contract(
      pure = true
   )
   public ResolutionResult(@NotNull Repository var1, @NotNull URL var2, @Nullable URL var3, boolean var4, boolean var5) {
      this.repository = var1;
      this.dependencyURL = var2;
      this.checksumURL = var3;
      this.aggregator = var4;
      this.checked = var5;
      if (!var4) {
         Objects.requireNonNull(var2, "Resolved URL must not be null for non-aggregator dependencies");
      }

   }

   @NotNull
   @Contract(
      pure = true
   )
   public static ResolutionResult read(@NotNull DataInput var0) {
      return new ResolutionResult(Repository.read(var0), Serialization.readURL(var0), var0.readBoolean() ? Serialization.readURL(var0) : null, var0.readBoolean(), false);
   }

   public void write(@NotNull DataOutput var1) {
      this.repository.write(var1);
      Serialization.writeURL(this.dependencyURL, var1);
      if (this.checksumURL == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         Serialization.writeURL(this.checksumURL, var1);
      }

      var1.writeBoolean(this.aggregator);
   }

   @NotNull
   public Repository repository() {
      return this.repository;
   }

   @NotNull
   public URL dependencyURL() {
      return this.dependencyURL;
   }

   @Nullable
   public URL checksumURL() {
      return this.checksumURL;
   }

   public boolean aggregator() {
      return this.aggregator;
   }

   public boolean checked() {
      return this.checked;
   }

   public void setChecked() {
      this.checked = true;
   }

   @Contract(
      pure = true
   )
   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ResolutionResult)) {
         return false;
      } else {
         ResolutionResult var2 = (ResolutionResult)var1;
         return Objects.equals(this.dependencyURL.toString(), var2.dependencyURL.toString()) && Objects.equals(this.checksumURL != null ? this.checksumURL.toString() : null, var2.checksumURL != null ? var2.checksumURL.toString() : null) && this.aggregator == var2.aggregator && this.checked == var2.checked;
      }
   }

   @Contract(
      pure = true
   )
   public int hashCode() {
      return Objects.hash(new Object[]{this.dependencyURL.toString(), this.checksumURL != null ? this.checksumURL.toString() : null, this.aggregator, this.checked});
   }
}
