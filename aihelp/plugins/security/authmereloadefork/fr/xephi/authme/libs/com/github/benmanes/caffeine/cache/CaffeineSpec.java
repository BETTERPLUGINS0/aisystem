package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class CaffeineSpec {
   static final String SPLIT_OPTIONS = ",";
   static final String SPLIT_KEY_VALUE = "=";
   final String specification;
   int initialCapacity = -1;
   long maximumWeight = -1L;
   long maximumSize = -1L;
   boolean recordStats;
   @Nullable
   Caffeine.Strength keyStrength;
   @Nullable
   Caffeine.Strength valueStrength;
   @Nullable
   Duration expireAfterWrite;
   @Nullable
   Duration expireAfterAccess;
   @Nullable
   Duration refreshAfterWrite;

   private CaffeineSpec(String specification) {
      this.specification = (String)Objects.requireNonNull(specification);
   }

   Caffeine<Object, Object> toBuilder() {
      Caffeine<Object, Object> builder = Caffeine.newBuilder();
      if (this.initialCapacity != -1) {
         builder.initialCapacity(this.initialCapacity);
      }

      if (this.maximumSize != -1L) {
         builder.maximumSize(this.maximumSize);
      }

      if (this.maximumWeight != -1L) {
         builder.maximumWeight(this.maximumWeight);
      }

      if (this.keyStrength != null) {
         Caffeine.requireState(this.keyStrength == Caffeine.Strength.WEAK);
         builder.weakKeys();
      }

      if (this.valueStrength != null) {
         if (this.valueStrength == Caffeine.Strength.WEAK) {
            builder.weakValues();
         } else {
            if (this.valueStrength != Caffeine.Strength.SOFT) {
               throw new IllegalStateException();
            }

            builder.softValues();
         }
      }

      if (this.expireAfterWrite != null) {
         builder.expireAfterWrite(this.expireAfterWrite);
      }

      if (this.expireAfterAccess != null) {
         builder.expireAfterAccess(this.expireAfterAccess);
      }

      if (this.refreshAfterWrite != null) {
         builder.refreshAfterWrite(this.refreshAfterWrite);
      }

      if (this.recordStats) {
         builder.recordStats();
      }

      return builder;
   }

   @NonNull
   public static CaffeineSpec parse(@NonNull String specification) {
      CaffeineSpec spec = new CaffeineSpec(specification);
      String[] var2 = specification.split(",");
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String option = var2[var4];
         spec.parseOption(option.trim());
      }

      return spec;
   }

   void parseOption(String option) {
      if (!option.isEmpty()) {
         String[] keyAndValue = option.split("=");
         Caffeine.requireArgument(keyAndValue.length <= 2, "key-value pair %s with more than one equals sign", option);
         String key = keyAndValue[0].trim();
         String value = keyAndValue.length == 1 ? null : keyAndValue[1].trim();
         this.configure(key, value);
      }
   }

   void configure(String key, @Nullable String value) {
      byte var4 = -1;
      switch(key.hashCode()) {
      case -1076762142:
         if (key.equals("expireAfterWrite")) {
            var4 = 7;
         }
         break;
      case -737229428:
         if (key.equals("weakKeys")) {
            var4 = 3;
         }
         break;
      case -83937812:
         if (key.equals("softValues")) {
            var4 = 5;
         }
         break;
      case 336225217:
         if (key.equals("expireAfterAccess")) {
            var4 = 6;
         }
         break;
      case 502967994:
         if (key.equals("weakValues")) {
            var4 = 4;
         }
         break;
      case 706249886:
         if (key.equals("refreshAfterWrite")) {
            var4 = 8;
         }
         break;
      case 817286328:
         if (key.equals("maximumWeight")) {
            var4 = 2;
         }
         break;
      case 1306358478:
         if (key.equals("recordStats")) {
            var4 = 9;
         }
         break;
      case 1685649985:
         if (key.equals("maximumSize")) {
            var4 = 1;
         }
         break;
      case 1725385758:
         if (key.equals("initialCapacity")) {
            var4 = 0;
         }
      }

      switch(var4) {
      case 0:
         this.initialCapacity(key, value);
         return;
      case 1:
         this.maximumSize(key, value);
         return;
      case 2:
         this.maximumWeight(key, value);
         return;
      case 3:
         this.weakKeys(value);
         return;
      case 4:
         this.valueStrength(key, value, Caffeine.Strength.WEAK);
         return;
      case 5:
         this.valueStrength(key, value, Caffeine.Strength.SOFT);
         return;
      case 6:
         this.expireAfterAccess(key, value);
         return;
      case 7:
         this.expireAfterWrite(key, value);
         return;
      case 8:
         this.refreshAfterWrite(key, value);
         return;
      case 9:
         this.recordStats(value);
         return;
      default:
         throw new IllegalArgumentException("Unknown key " + key);
      }
   }

   void initialCapacity(String key, @Nullable String value) {
      Caffeine.requireArgument(this.initialCapacity == -1, "initial capacity was already set to %,d", this.initialCapacity);
      this.initialCapacity = parseInt(key, value);
   }

   void maximumSize(String key, @Nullable String value) {
      Caffeine.requireArgument(this.maximumSize == -1L, "maximum size was already set to %,d", this.maximumSize);
      Caffeine.requireArgument(this.maximumWeight == -1L, "maximum weight was already set to %,d", this.maximumWeight);
      this.maximumSize = parseLong(key, value);
   }

   void maximumWeight(String key, @Nullable String value) {
      Caffeine.requireArgument(this.maximumWeight == -1L, "maximum weight was already set to %,d", this.maximumWeight);
      Caffeine.requireArgument(this.maximumSize == -1L, "maximum size was already set to %,d", this.maximumSize);
      this.maximumWeight = parseLong(key, value);
   }

   void weakKeys(@Nullable String value) {
      Caffeine.requireArgument(value == null, "weak keys does not take a value");
      Caffeine.requireArgument(this.keyStrength == null, "weak keys was already set");
      this.keyStrength = Caffeine.Strength.WEAK;
   }

   void valueStrength(String key, @Nullable String value, Caffeine.Strength strength) {
      Caffeine.requireArgument(value == null, "%s does not take a value", key);
      Caffeine.requireArgument(this.valueStrength == null, "%s was already set to %s", key, this.valueStrength);
      this.valueStrength = strength;
   }

   void expireAfterAccess(String key, @Nullable String value) {
      Caffeine.requireArgument(this.expireAfterAccess == null, "expireAfterAccess was already set");
      this.expireAfterAccess = parseDuration(key, value);
   }

   void expireAfterWrite(String key, @Nullable String value) {
      Caffeine.requireArgument(this.expireAfterWrite == null, "expireAfterWrite was already set");
      this.expireAfterWrite = parseDuration(key, value);
   }

   void refreshAfterWrite(String key, @Nullable String value) {
      Caffeine.requireArgument(this.refreshAfterWrite == null, "refreshAfterWrite was already set");
      this.refreshAfterWrite = parseDuration(key, value);
   }

   void recordStats(@Nullable String value) {
      Caffeine.requireArgument(value == null, "record stats does not take a value");
      Caffeine.requireArgument(!this.recordStats, "record stats was already set");
      this.recordStats = true;
   }

   static int parseInt(String key, @Nullable String value) {
      Caffeine.requireArgument(value != null && !value.isEmpty(), "value of key %s was omitted", key);

      try {
         return Integer.parseInt(value);
      } catch (NumberFormatException var3) {
         throw new IllegalArgumentException(String.format("key %s value was set to %s, must be an integer", key, value), var3);
      }
   }

   static long parseLong(String key, @Nullable String value) {
      Caffeine.requireArgument(value != null && !value.isEmpty(), "value of key %s was omitted", key);

      try {
         return Long.parseLong(value);
      } catch (NumberFormatException var3) {
         throw new IllegalArgumentException(String.format("key %s value was set to %s, must be a long", key, value), var3);
      }
   }

   static Duration parseDuration(String key, @Nullable String value) {
      Caffeine.requireArgument(value != null && !value.isEmpty(), "value of key %s omitted", key);
      boolean isIsoFormat = value.contains("p") || value.contains("P");
      if (isIsoFormat) {
         Duration duration = Duration.parse(value);
         Caffeine.requireArgument(!duration.isNegative(), "key %s invalid format; was %s, but the duration cannot be negative", key, value);
         return duration;
      } else {
         long duration = parseLong(key, value.substring(0, value.length() - 1));
         TimeUnit unit = parseTimeUnit(key, value);
         return Duration.ofNanos(unit.toNanos(duration));
      }
   }

   static TimeUnit parseTimeUnit(String key, @Nullable String value) {
      Caffeine.requireArgument(value != null && !value.isEmpty(), "value of key %s omitted", key);
      char lastChar = Character.toLowerCase(value.charAt(value.length() - 1));
      switch(lastChar) {
      case 'd':
         return TimeUnit.DAYS;
      case 'h':
         return TimeUnit.HOURS;
      case 'm':
         return TimeUnit.MINUTES;
      case 's':
         return TimeUnit.SECONDS;
      default:
         throw new IllegalArgumentException(String.format("key %s invalid format; was %s, must end with one of [dDhHmMsS]", key, value));
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof CaffeineSpec)) {
         return false;
      } else {
         CaffeineSpec spec = (CaffeineSpec)o;
         return Objects.equals(this.refreshAfterWrite, spec.refreshAfterWrite) && Objects.equals(this.expireAfterAccess, spec.expireAfterAccess) && Objects.equals(this.expireAfterWrite, spec.expireAfterWrite) && this.initialCapacity == spec.initialCapacity && this.maximumWeight == spec.maximumWeight && this.valueStrength == spec.valueStrength && this.keyStrength == spec.keyStrength && this.maximumSize == spec.maximumSize && this.recordStats == spec.recordStats;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.initialCapacity, this.maximumSize, this.maximumWeight, this.keyStrength, this.valueStrength, this.recordStats, this.expireAfterWrite, this.expireAfterAccess, this.refreshAfterWrite});
   }

   public String toParsableString() {
      return this.specification;
   }

   public String toString() {
      return this.getClass().getSimpleName() + '{' + this.toParsableString() + '}';
   }
}
