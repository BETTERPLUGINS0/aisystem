package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.updater;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;

public class VersionNumber {
   private final int[] parts;

   private VersionNumber(int[] parts) {
      this.parts = parts;
   }

   public boolean greaterThan(VersionNumber other) {
      for(int i = 0; i < this.parts.length && i < other.parts.length; ++i) {
         if (this.parts[i] > other.parts[i]) {
            return true;
         }

         if (this.parts[i] < other.parts[i]) {
            return false;
         }
      }

      return false;
   }

   public boolean greaterThanOrEqual(VersionNumber other) {
      return this.greaterThan(other) || this.equals(other);
   }

   public boolean lessThan(VersionNumber other) {
      return !this.greaterThanOrEqual(other);
   }

   public boolean lessThanOrEqual(VersionNumber other) {
      return !this.greaterThan(other);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         VersionNumber that = (VersionNumber)o;
         return Arrays.equals(this.parts, that.parts);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Arrays.hashCode(this.parts);
   }

   public String toString() {
      return (String)Arrays.stream(this.parts).mapToObj(String::valueOf).collect(Collectors.joining("."));
   }

   public static VersionNumber of(String versionString) {
      Pattern pattern = Pattern.compile("\\d+\\.\\d+(\\.\\d)*");
      Matcher matcher = pattern.matcher(versionString);
      if (!matcher.find()) {
         throw new IllegalArgumentException("Invalid version string: " + versionString);
      } else {
         String version = versionString.substring(matcher.start(), matcher.end());
         int[] parts = Arrays.stream(version.split("\\.")).mapToInt(Integer::parseInt).toArray();
         return new VersionNumber(parts);
      }
   }

   public static VersionNumber ofBukkit() {
      return of((String)Objects.requireNonNull(Bukkit.getServer().getBukkitVersion(), "Bukkit version is null"));
   }
}
