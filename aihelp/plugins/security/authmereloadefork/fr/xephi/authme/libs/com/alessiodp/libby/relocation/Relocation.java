package fr.xephi.authme.libs.com.alessiodp.libby.relocation;

import fr.xephi.authme.libs.com.alessiodp.libby.Util;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Relocation {
   @NotNull
   private final String pattern;
   @NotNull
   private final String relocatedPattern;
   @NotNull
   private final Collection<String> includes;
   @NotNull
   private final Collection<String> excludes;

   public Relocation(@NotNull String pattern, @NotNull String relocatedPattern, @Nullable Collection<String> includes, @Nullable Collection<String> excludes) {
      this.pattern = Util.replaceWithDots((String)Objects.requireNonNull(pattern, "pattern"));
      this.relocatedPattern = Util.replaceWithDots((String)Objects.requireNonNull(relocatedPattern, "relocatedPattern"));
      this.includes = includes != null ? Collections.unmodifiableSet((Set)includes.stream().map(Util::replaceWithDots).collect(Collectors.toSet())) : Collections.emptySet();
      this.excludes = excludes != null ? Collections.unmodifiableSet((Set)excludes.stream().map(Util::replaceWithDots).collect(Collectors.toSet())) : Collections.emptySet();
   }

   public Relocation(@NotNull String pattern, @NotNull String relocatedPattern) {
      this(pattern, relocatedPattern, (Collection)null, (Collection)null);
   }

   @NotNull
   public String getPattern() {
      return this.pattern;
   }

   @NotNull
   public String getRelocatedPattern() {
      return this.relocatedPattern;
   }

   @NotNull
   public Collection<String> getIncludes() {
      return this.includes;
   }

   @NotNull
   public Collection<String> getExcludes() {
      return this.excludes;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Relocation that = (Relocation)o;
         if (!this.pattern.equals(that.pattern)) {
            return false;
         } else if (!this.relocatedPattern.equals(that.relocatedPattern)) {
            return false;
         } else {
            return !this.includes.equals(that.includes) ? false : this.excludes.equals(that.excludes);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.pattern.hashCode();
      result = 31 * result + this.relocatedPattern.hashCode();
      result = 31 * result + this.includes.hashCode();
      result = 31 * result + this.excludes.hashCode();
      return result;
   }

   @NotNull
   public static Relocation.Builder builder() {
      return new Relocation.Builder();
   }

   public static class Builder {
      private String pattern;
      private String relocatedPattern;
      private final Collection<String> includes = new LinkedList();
      private final Collection<String> excludes = new LinkedList();

      @NotNull
      public Relocation.Builder pattern(@NotNull String pattern) {
         this.pattern = (String)Objects.requireNonNull(pattern, "pattern");
         return this;
      }

      @NotNull
      public Relocation.Builder relocatedPattern(@NotNull String relocatedPattern) {
         this.relocatedPattern = (String)Objects.requireNonNull(relocatedPattern, "relocatedPattern");
         return this;
      }

      @NotNull
      public Relocation.Builder include(@NotNull String include) {
         this.includes.add((String)Objects.requireNonNull(include, "include"));
         return this;
      }

      @NotNull
      public Relocation.Builder exclude(@NotNull String exclude) {
         this.excludes.add((String)Objects.requireNonNull(exclude, "exclude"));
         return this;
      }

      @NotNull
      public Relocation build() {
         return new Relocation(this.pattern, this.relocatedPattern, this.includes, this.excludes);
      }
   }
}
