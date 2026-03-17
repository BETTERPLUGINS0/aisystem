package me.PM2.infinitevehicles.libby.relocation;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

public class Relocation {
   private final String pattern;
   private final String relocatedPattern;
   private final Collection<String> includes;
   private final Collection<String> excludes;

   public Relocation(String pattern, String relocatedPattern, Collection<String> includes, Collection<String> excludes) {
      this.pattern = ((String)Objects.requireNonNull(var1, "pattern")).replace("{}", ".");
      this.relocatedPattern = ((String)Objects.requireNonNull(var2, "relocatedPattern")).replace("{}", ".");
      this.includes = var3 != null ? Collections.unmodifiableList(new LinkedList(var3)) : Collections.emptyList();
      this.excludes = var4 != null ? Collections.unmodifiableList(new LinkedList(var4)) : Collections.emptyList();
   }

   public Relocation(String pattern, String relocatedPattern) {
      this(var1, var2, (Collection)null, (Collection)null);
   }

   public String getPattern() {
      return this.pattern;
   }

   public String getRelocatedPattern() {
      return this.relocatedPattern;
   }

   public Collection<String> getIncludes() {
      return this.includes;
   }

   public Collection<String> getExcludes() {
      return this.excludes;
   }

   public static Relocation.Builder builder() {
      return new Relocation.Builder();
   }

   public static class Builder {
      private String pattern;
      private String relocatedPattern;
      private final Collection<String> includes = new LinkedList();
      private final Collection<String> excludes = new LinkedList();

      public Relocation.Builder pattern(String pattern) {
         this.pattern = (String)Objects.requireNonNull(var1, "pattern");
         return this;
      }

      public Relocation.Builder relocatedPattern(String relocatedPattern) {
         this.relocatedPattern = (String)Objects.requireNonNull(var1, "relocatedPattern");
         return this;
      }

      public Relocation.Builder include(String include) {
         this.includes.add((String)Objects.requireNonNull(var1, "include"));
         return this;
      }

      public Relocation.Builder exclude(String exclude) {
         this.excludes.add((String)Objects.requireNonNull(var1, "exclude"));
         return this;
      }

      public Relocation build() {
         return new Relocation(this.pattern, this.relocatedPattern, this.includes, this.excludes);
      }
   }
}
