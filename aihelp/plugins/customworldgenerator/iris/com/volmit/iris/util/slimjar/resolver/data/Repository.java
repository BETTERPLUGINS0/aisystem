package com.volmit.iris.util.slimjar.resolver.data;

import com.volmit.iris.util.slimjar.util.Connections;
import com.volmit.iris.util.slimjar.util.Serialization;
import java.io.DataInput;
import java.io.DataOutput;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Repository(@NotNull URL url) {
   @NotNull
   public static final String CENTRAL_URL = "https://repo1.maven.org/maven2/";
   private static Repository centralInstance;

   public Repository(@NotNull URL url) {
      this.url = var1;
   }

   @NotNull
   public static Repository central() {
      if (centralInstance == null) {
         try {
            centralInstance = new Repository(Connections.newURL("https://repo1.maven.org/maven2/"));
         } catch (MalformedURLException var1) {
         }
      }

      return centralInstance;
   }

   public static Repository read(@NotNull DataInput var0) {
      return new Repository(Serialization.readURL(var0));
   }

   public void write(@NotNull DataOutput var1) {
      Serialization.writeURL(this.url, var1);
   }

   @Contract(
      pure = true
   )
   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof Repository) {
         Repository var2 = (Repository)var1;
         return Objects.equals(this.url, var2.url);
      } else {
         return false;
      }
   }

   @Contract(
      pure = true
   )
   @NotNull
   public String toString() {
      return "Repository{, url='" + String.valueOf(this.url) + "'}";
   }

   @NotNull
   public URL url() {
      return this.url;
   }
}
