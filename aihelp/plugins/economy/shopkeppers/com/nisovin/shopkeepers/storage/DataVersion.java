package com.nisovin.shopkeepers.storage;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.bukkit.ServerUtils;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class DataVersion {
   private static final int SHOPKEEPER_STORAGE_VERSION = 4;
   private static final int SHOPKEEPER_DATA_VERSION = 2;
   @Nullable
   private static DataVersion current = null;
   public static final DataVersion MISSING = new DataVersion("missing");
   private static final String SEPARATOR = "|";
   private static final Pattern SEPARATOR_PATTERN = Pattern.compile("|", 16);
   private static final String NAMED_START = "<";
   private static final String NAMED_END = "<";
   private final int shopkeeperStorageVersion;
   private final int shopkeeperDataVersion;
   private final int minecraftDataVersion;
   private final String name;

   public static DataVersion current() {
      Validate.State.notNull(current, (String)"Not yet initialized!");
      return (DataVersion)Unsafe.assertNonNull(current);
   }

   public static void init() {
      if (current == null) {
         current = new DataVersion(4, 2, getCurrentMinecraftDataVersion());
      }
   }

   private static int getCurrentMinecraftDataVersion() {
      try {
         return ServerUtils.getDataVersion();
      } catch (Exception var1) {
         throw new IllegalStateException("Could not retrieve the server's current Minecraft data version!", var1);
      }
   }

   public static DataVersion parse(String dataVersionString) {
      if (StringUtils.isEmpty(dataVersionString)) {
         return MISSING;
      } else {
         assert dataVersionString != null;

         if (dataVersionString.startsWith("<")) {
            Validate.isTrue(dataVersionString.endsWith("<"), () -> {
               return "Invalid data version: " + dataVersionString;
            });
            String name = dataVersionString.substring("<".length(), dataVersionString.length() - "<".length());

            try {
               return new DataVersion(name);
            } catch (IllegalArgumentException var10) {
               throw new IllegalArgumentException("Invalid data version: " + dataVersionString, var10);
            }
         } else {
            String[] components = SEPARATOR_PATTERN.split(dataVersionString);
            boolean legacy = components.length == 2;
            Validate.isTrue(components.length == 3 || legacy, () -> {
               return "Invalid data version: " + dataVersionString;
            });
            String skStorageVersionString = components[0];
            String skDataVersionString = legacy ? "0" : components[1];
            String mcDataVersionString = legacy ? components[1] : components[2];
            int skStorageVersion = parseVersionComponent(skStorageVersionString, dataVersionString);
            int skDataVersion = parseVersionComponent(skDataVersionString, dataVersionString);
            int mcDataVersion = parseVersionComponent(mcDataVersionString, dataVersionString);

            try {
               return new DataVersion(skStorageVersion, skDataVersion, mcDataVersion, legacy);
            } catch (IllegalArgumentException var11) {
               throw new IllegalArgumentException("Invalid data version: " + dataVersionString, var11);
            }
         }
      }
   }

   private static int parseVersionComponent(String versionString, String dataVersionString) {
      Integer version = ConversionUtils.parseInt(versionString);
      Validate.notNull(version, (Supplier)(() -> {
         return "Invalid data version: " + dataVersionString;
      }));
      return (Integer)Unsafe.assertNonNull(version);
   }

   public DataVersion(String name) {
      Validate.notEmpty(name, "name is null or empty");
      Validate.isTrue(!StringUtils.containsWhitespace(name), "name contains whitespace");
      this.shopkeeperStorageVersion = 0;
      this.shopkeeperDataVersion = 0;
      this.minecraftDataVersion = 0;
      this.name = StringUtils.normalize(name);
   }

   public DataVersion(int shopkeeperStorageVersion, int shopkeeperDataVersion, int minecraftDataVersion) {
      this(shopkeeperStorageVersion, shopkeeperDataVersion, minecraftDataVersion, false);
   }

   private DataVersion(int shopkeeperStorageVersion, int shopkeeperDataVersion, int minecraftDataVersion, boolean legacy) {
      Validate.isTrue(shopkeeperStorageVersion > 0, "shopkeeperStorageVersion <= 0");
      if (legacy) {
         Validate.isTrue(shopkeeperDataVersion == 0, "legacy but shopkeeperDataVersion != 0");
      } else {
         Validate.isTrue(shopkeeperDataVersion > 0, "shopkeeperDataVersion <= 0");
      }

      Validate.isTrue(minecraftDataVersion > 0, "minecraftDataVersion <= 0");
      this.shopkeeperStorageVersion = shopkeeperStorageVersion;
      this.shopkeeperDataVersion = shopkeeperDataVersion;
      this.minecraftDataVersion = minecraftDataVersion;
      this.name = shopkeeperStorageVersion + (legacy ? "" : "|" + shopkeeperDataVersion) + "|" + minecraftDataVersion;
   }

   public int getShopkeeperStorageVersion() {
      return this.shopkeeperStorageVersion;
   }

   public int getShopkeeperDataVersion() {
      return this.shopkeeperDataVersion;
   }

   public int getMinecraftDataVersion() {
      return this.minecraftDataVersion;
   }

   public boolean isEmpty() {
      return this.shopkeeperStorageVersion == 0 && this.shopkeeperDataVersion == 0 && this.minecraftDataVersion == 0;
   }

   public String getName() {
      return this.name;
   }

   private boolean isVersionDowngrade(int thisVersion, DataVersion previous, int previousVersion) {
      assert previous != null;

      if (this.equals(MISSING)) {
         return !previous.isEmpty();
      } else if (!previous.isEmpty() && !this.isEmpty()) {
         return thisVersion < previousVersion;
      } else {
         return false;
      }
   }

   public boolean isShopkeeperStorageDowngrade(DataVersion previous) {
      Validate.notNull(previous, (String)"previous is null");
      return this.isVersionDowngrade(this.getShopkeeperStorageVersion(), previous, previous.getShopkeeperStorageVersion());
   }

   public boolean isShopkeeperStorageUpgrade(DataVersion previous) {
      Validate.notNull(previous, (String)"previous is null");
      return previous.isShopkeeperStorageDowngrade(this);
   }

   public boolean isShopkeeperDataDowngrade(DataVersion previous) {
      Validate.notNull(previous, (String)"previous is null");
      return this.isVersionDowngrade(this.getShopkeeperDataVersion(), previous, previous.getShopkeeperDataVersion());
   }

   public boolean isShopkeeperDataUpgrade(DataVersion previous) {
      Validate.notNull(previous, (String)"previous is null");
      return previous.isShopkeeperDataDowngrade(this);
   }

   public boolean isMinecraftDowngrade(DataVersion previous) {
      Validate.notNull(previous, (String)"previous is null");
      return this.isVersionDowngrade(this.getMinecraftDataVersion(), previous, previous.getMinecraftDataVersion());
   }

   public boolean isMinecraftUpgrade(DataVersion previous) {
      Validate.notNull(previous, (String)"previous is null");
      return previous.isMinecraftDowngrade(this);
   }

   public String toString() {
      return this.isEmpty() ? "<" + this.name + "<" : this.name;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.name.hashCode();
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof DataVersion)) {
         return false;
      } else {
         DataVersion other = (DataVersion)obj;
         return this.name.equals(other.name);
      }
   }
}
