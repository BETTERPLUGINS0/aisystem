package com.nisovin.shopkeepers.compat;

import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public class CompatVersion {
   public static final String VARIANT_PAPER = "paper";
   private final String compatVersion;
   private final String variant;
   private final List<ServerVersion> supportedServerVersions;

   private static String getVariant(String compatVersion) {
      int variantSeparatorIndex = compatVersion.indexOf(95, 7);
      return variantSeparatorIndex < 0 ? "" : compatVersion.substring(variantSeparatorIndex + 1);
   }

   public CompatVersion(String compatVersion, List<ServerVersion> supportedServerVersions) {
      Validate.notEmpty(compatVersion, "compatVersion is empty");
      Validate.notNull(supportedServerVersions, (String)"supportedServerVersions is null");
      Validate.isTrue(!supportedServerVersions.isEmpty(), "supportedServerVersions is empty");
      this.compatVersion = compatVersion;
      this.variant = getVariant(compatVersion);
      this.supportedServerVersions = Collections.unmodifiableList(new ArrayList(supportedServerVersions));
   }

   public CompatVersion(String compatVersion, String minecraftVersion, String mappingsVersion) {
      this(compatVersion, Collections.singletonList(new ServerVersion(minecraftVersion, mappingsVersion)));
   }

   public String getCompatVersion() {
      return this.compatVersion;
   }

   public String getVariant() {
      return this.variant;
   }

   public boolean hasVariant() {
      return !this.getVariant().isEmpty();
   }

   public boolean isPaper() {
      return this.getVariant().equals("paper");
   }

   public List<ServerVersion> getSupportedServerVersions() {
      return this.supportedServerVersions;
   }

   public String getFirstMinecraftVersion() {
      return ((ServerVersion)this.supportedServerVersions.getFirst()).getMinecraftVersion();
   }

   public String getFirstMappingsVersion() {
      return ((ServerVersion)this.supportedServerVersions.getFirst()).getMappingsVersion();
   }

   public String getLastMinecraftVersion() {
      return ((ServerVersion)this.supportedServerVersions.getLast()).getMinecraftVersion();
   }

   public String getLastMappingsVersion() {
      return ((ServerVersion)this.supportedServerVersions.getLast()).getMappingsVersion();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.compatVersion.hashCode();
      result = 31 * result + this.supportedServerVersions.hashCode();
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof CompatVersion)) {
         return false;
      } else {
         CompatVersion other = (CompatVersion)obj;
         if (!this.compatVersion.equals(other.compatVersion)) {
            return false;
         } else {
            return this.supportedServerVersions.equals(other.supportedServerVersions);
         }
      }
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("CompatVersion [compatVersion=");
      builder.append(this.compatVersion);
      builder.append(", serverVersions=");
      builder.append(this.supportedServerVersions);
      builder.append("]");
      return builder.toString();
   }
}
