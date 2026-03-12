package com.nisovin.shopkeepers.util.data.container;

import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.Nullable;

public class MapBasedDataContainer extends AbstractDataContainer {
   private final Map<String, Object> dataMap;
   @Nullable
   private Set<? extends String> keysView;
   @Nullable
   private Map<? extends String, ?> mapView;

   public MapBasedDataContainer() {
      this(new LinkedHashMap());
   }

   public MapBasedDataContainer(Map<String, Object> dataMap) {
      this.keysView = null;
      this.mapView = null;
      Validate.notNull(dataMap, (String)"dataMap is null");
      this.dataMap = dataMap;
   }

   @Nullable
   public Object getOrDefault(String key, @Nullable Object defaultValue) {
      Validate.notEmpty(key, "key is empty");
      Object value = this.dataMap.get(key);
      return value != null ? value : defaultValue;
   }

   public void internalSet(String key, Object value) {
      this.dataMap.put(key, value);
   }

   public void remove(String key) {
      this.dataMap.remove(key);
   }

   public void clear() {
      this.dataMap.clear();
   }

   public int size() {
      return this.dataMap.size();
   }

   public boolean isEmpty() {
      return this.dataMap.isEmpty();
   }

   public Set<? extends String> getKeys() {
      if (this.keysView == null) {
         this.keysView = Collections.unmodifiableSet(this.dataMap.keySet());
      }

      assert this.keysView != null;

      return this.keysView;
   }

   public Map<? extends String, ?> getValues() {
      if (this.mapView == null) {
         this.mapView = Collections.unmodifiableMap(this.dataMap);
      }

      assert this.mapView != null;

      return this.mapView;
   }

   public Map<String, Object> getValuesCopy() {
      return new LinkedHashMap(this.dataMap);
   }

   @Nullable
   public Object serialize() {
      return this.getValues();
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("MapBasedDataContainer [dataMap=");
      builder.append(this.dataMap);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      return this.dataMap.hashCode();
   }

   public boolean equals(@Nullable Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof DataContainer)) {
         return false;
      } else {
         DataContainer otherContainer = (DataContainer)obj;
         return this.dataMap.equals(otherContainer.getValues());
      }
   }
}
