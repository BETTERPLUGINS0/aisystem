package com.bergerkiller.bukkit.tc.properties;

import com.bergerkiller.bukkit.common.utils.StringUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

class TrainPropertiesMap {
   private Map<String, TrainProperties> trainProperties = new TreeMap();
   private Map<String, List<TrainProperties>> trainPropertiesRelaxed = new TreeMap();

   public Collection<TrainProperties> values() {
      return Collections.unmodifiableCollection(this.trainProperties.values());
   }

   public TrainProperties get(String trainName) {
      return (TrainProperties)this.trainProperties.get(trainName);
   }

   public TrainProperties getRelaxed(String trainName) {
      List<TrainProperties> result = (List)this.trainPropertiesRelaxed.get(createRelaxedKey(trainName));
      return result != null && result.size() == 1 ? (TrainProperties)result.get(0) : null;
   }

   public boolean containsKey(String trainName) {
      return this.trainProperties.containsKey(trainName);
   }

   public void add(String trainName, TrainProperties properties) {
      TrainProperties previous = (TrainProperties)this.trainProperties.put(trainName, properties);
      if (previous != null) {
         previous.removed = true;
         this.removeFromRelaxedMappings(trainName, previous);
      }

      properties.removed = false;
      String relaxed = createRelaxedKey(trainName);
      List<TrainProperties> prevAtRelaxedKey = (List)this.trainPropertiesRelaxed.put(relaxed, Collections.singletonList(properties));
      if (prevAtRelaxedKey != null) {
         List<TrainProperties> combined = new ArrayList(prevAtRelaxedKey);
         combined.add(properties);
         this.trainPropertiesRelaxed.put(relaxed, combined);
      }

   }

   public TrainProperties remove(String trainName) {
      TrainProperties properties = (TrainProperties)this.trainProperties.remove(trainName);
      if (properties != null) {
         properties.removed = true;
         this.removeFromRelaxedMappings(trainName, properties);
      }

      return properties;
   }

   public void clear() {
      this.trainProperties.values().forEach((p) -> {
         p.removed = true;
      });
      this.trainProperties.clear();
      this.trainPropertiesRelaxed.clear();
   }

   private void removeFromRelaxedMappings(String trainName, TrainProperties properties) {
      String relaxed = createRelaxedKey(trainName);
      List<TrainProperties> atRelaxedKey = (List)this.trainPropertiesRelaxed.remove(relaxed);
      if (atRelaxedKey != null) {
         if (atRelaxedKey.size() > 1) {
            atRelaxedKey.remove(properties);
            this.trainPropertiesRelaxed.put(relaxed, atRelaxedKey);
         } else if (atRelaxedKey.size() == 1 && atRelaxedKey.get(0) != properties) {
            this.trainPropertiesRelaxed.put(relaxed, atRelaxedKey);
         }
      }

   }

   private static String createRelaxedKey(String trainName) {
      return StringUtil.stripChatStyle(trainName).toLowerCase(Locale.ENGLISH);
   }
}
