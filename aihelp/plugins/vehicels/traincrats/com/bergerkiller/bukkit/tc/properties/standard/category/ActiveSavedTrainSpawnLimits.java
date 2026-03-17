package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedProperty;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedStandardTrainProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ActiveSavedTrainSpawnLimits extends FieldBackedStandardTrainProperty<List<String>> {
   public List<String> getDefault() {
      return Collections.emptyList();
   }

   public void addSavedTrainToConfig(ConfigurationNode config, String savedTrainName) {
      List<String> names = config.getList("activeSavedTrainSpawnLimits", String.class);
      if (!names.contains(savedTrainName)) {
         names.add(savedTrainName);
      }

   }

   public Optional<List<String>> readFromConfig(ConfigurationNode config) {
      if (config.contains("activeSavedTrainSpawnLimits")) {
         List<String> names = config.getList("activeSavedTrainSpawnLimits", String.class);
         names = Collections.unmodifiableList(new ArrayList(names));
         return Optional.of(names);
      } else {
         return Optional.empty();
      }
   }

   public void writeToConfig(ConfigurationNode config, Optional<List<String>> value) {
      List names;
      if (value.isPresent() && !(names = (List)value.get()).isEmpty()) {
         config.set("activeSavedTrainSpawnLimits", names);
      } else {
         config.remove("activeSavedTrainSpawnLimits");
      }

   }

   public List<String> getData(FieldBackedProperty.TrainInternalData data) {
      return data.activeSavedTrainSpawnLimits;
   }

   public void setData(FieldBackedProperty.TrainInternalData data, List<String> value) {
      data.activeSavedTrainSpawnLimits = value;
   }
}
