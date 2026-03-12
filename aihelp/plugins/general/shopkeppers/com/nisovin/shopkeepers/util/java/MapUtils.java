package com.nisovin.shopkeepers.util.java;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class MapUtils {
   private static final int MAX_CAPACITY = 1073741824;

   public static int getIdealHashMapCapacity(int expectedSize) {
      Validate.isTrue(expectedSize >= 0, "expectedSize cannot be negative");
      if (expectedSize < 3) {
         return expectedSize + 1;
      } else {
         return expectedSize < 1073741824 ? (int)((float)expectedSize / 0.75F + 1.0F) : Integer.MAX_VALUE;
      }
   }

   public static <K, V> Map<K, V> createMap(K key, V value) {
      Map<K, V> map = new LinkedHashMap(getIdealHashMapCapacity(1));
      map.put(key, value);
      return map;
   }

   public static <K, V> Map<K, V> createMap(K key1, V value1, K key2, V value2) {
      Map<K, V> map = new LinkedHashMap(getIdealHashMapCapacity(2));
      map.put(key1, value1);
      map.put(key2, value2);
      return map;
   }

   public static <K, V> Map<K, V> createMap(K key1, V value1, K key2, V value2, K key3, V value3) {
      Map<K, V> map = new LinkedHashMap(getIdealHashMapCapacity(3));
      map.put(key1, value1);
      map.put(key2, value2);
      map.put(key3, value3);
      return map;
   }

   public static <K, V> Map<K, V> createMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
      Map<K, V> map = new LinkedHashMap(getIdealHashMapCapacity(4));
      map.put(key1, value1);
      map.put(key2, value2);
      map.put(key3, value3);
      map.put(key4, value4);
      return map;
   }

   public static <K, V> Map<K, V> createMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
      Map<K, V> map = new LinkedHashMap(getIdealHashMapCapacity(5));
      map.put(key1, value1);
      map.put(key2, value2);
      map.put(key3, value3);
      map.put(key4, value4);
      map.put(key5, value5);
      return map;
   }

   public static <K, V> Map<K, V> createMap(Object... keyValuePairs) {
      Map<K, V> map = new LinkedHashMap(getIdealHashMapCapacity(keyValuePairs.length / 2));
      int keyLimit = keyValuePairs.length - 1;

      for(int i = 0; i < keyLimit; i += 2) {
         Object key = keyValuePairs[i];
         Object value = keyValuePairs[i + 1];
         map.put(key, value);
      }

      return map;
   }

   public static <K, V> Entry<K, V> entry(K key, V value) {
      return new SimpleImmutableEntry(key, value);
   }

   public static Map<String, Object> toStringMap(Map<?, ?> map) {
      Validate.notNull(map, (String)"map is null");
      Map<String, Object> stringMap = new LinkedHashMap(getIdealHashMapCapacity(map.size()));
      map.forEach((key, value) -> {
         String stringKey = StringUtils.toStringOrNull(key);
         stringMap.put(stringKey, value);
      });
      return stringMap;
   }

   public static <K, V> Map<K, V> getOrEmpty(@Nullable Map<K, V> map) {
      return map != null ? map : Collections.emptyMap();
   }

   private MapUtils() {
   }
}
