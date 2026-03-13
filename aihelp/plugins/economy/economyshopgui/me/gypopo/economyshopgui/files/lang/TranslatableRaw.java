package me.gypopo.economyshopgui.files.lang;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.util.ChatUtil;

public class TranslatableRaw implements Translatable {
   private final String s;

   public TranslatableRaw(String s) {
      this.s = ChatUtil.formatColors(s);
   }

   public Translatable replace(String ph, String value) {
      return new TranslatableRaw(this.s.replace(ph, value));
   }

   public Translatable replace(String ph, Translatable value) {
      return new TranslatableRaw(this.s.replace(ph, (String)value.get()));
   }

   public String getLegacy() {
      return this.s;
   }

   public String toString() {
      return this.s;
   }

   public Object get() {
      return this.s;
   }

   public List<Translatable> getLines() {
      return (List)Arrays.stream(this.s.split("\n")).map(TranslatableRaw::new).collect(Collectors.toList());
   }

   public Translatable.Builder builder() {
      return new TranslatableRaw.StringBuilder(this.s);
   }

   public class StringBuilder implements Translatable.Builder {
      private final java.lang.StringBuilder builder = new java.lang.StringBuilder();

      public StringBuilder(String param2) {
         this.builder.append(base);
      }

      public Translatable.Builder append(Translatable s) {
         this.builder.append(s.get());
         return this;
      }

      public Translatable.Builder append(String s) {
         this.builder.append(s);
         return this;
      }

      public Translatable.Builder color(char c) {
         this.builder.append("§").append(c);
         return this;
      }

      public Translatable build() {
         return new TranslatableRaw(this.builder.toString());
      }
   }
}
