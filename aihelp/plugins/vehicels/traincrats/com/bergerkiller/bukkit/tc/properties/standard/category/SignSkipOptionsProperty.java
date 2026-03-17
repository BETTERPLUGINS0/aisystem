package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.BlockLocation;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedProperty;
import com.bergerkiller.bukkit.tc.properties.standard.type.SignSkipOptions;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class SignSkipOptionsProperty extends FieldBackedProperty<SignSkipOptions> {
   public SignSkipOptions getDefault() {
      return SignSkipOptions.NONE;
   }

   public boolean isAppliedAsDefault() {
      return false;
   }

   public Optional<SignSkipOptions> readFromConfig(ConfigurationNode config) {
      if (!config.isNode("skipOptions")) {
         return Optional.empty();
      } else {
         ConfigurationNode skipOptions = config.getNode("skipOptions");
         int ignoreCtr = (Integer)skipOptions.get("ignoreCtr", 0);
         int skipCtr = (Integer)skipOptions.get("skipCtr", 0);
         String filter = (String)skipOptions.get("filter", "");
         Set<BlockLocation> signs = Collections.emptySet();
         if (skipOptions.contains("signs")) {
            List<String> signLocationNames = skipOptions.getList("signs", String.class);
            if (!signLocationNames.isEmpty()) {
               Set<BlockLocation> signs = new LinkedHashSet(signLocationNames.size());
               Iterator var8 = signLocationNames.iterator();

               while(var8.hasNext()) {
                  String signLocationName = (String)var8.next();
                  BlockLocation loc = BlockLocation.parseLocation(signLocationName);
                  if (loc != null) {
                     signs.add(loc);
                  }
               }

               signs = Collections.unmodifiableSet(signs);
            }
         }

         return Optional.of(SignSkipOptions.create(ignoreCtr, skipCtr, filter, signs));
      }
   }

   public void writeToConfig(ConfigurationNode config, Optional<SignSkipOptions> value) {
      if (value.isPresent()) {
         SignSkipOptions data = (SignSkipOptions)value.get();
         ConfigurationNode skipOptions = config.getNode("skipOptions");
         skipOptions.set("ignoreCtr", data.ignoreCounter());
         skipOptions.set("skipCtr", data.skipCounter());
         skipOptions.set("filter", data.filter());
         if (data.hasSkippedSigns()) {
            List<String> signs = skipOptions.getList("signs", String.class);
            Iterator<BlockLocation> signBlockIter = data.skippedSigns().iterator();

            int num_signs;
            for(num_signs = 0; signBlockIter.hasNext(); ++num_signs) {
               if (num_signs >= signs.size()) {
                  signs.add(((BlockLocation)signBlockIter.next()).toString());
               } else {
                  signs.set(num_signs, ((BlockLocation)signBlockIter.next()).toString());
               }
            }

            while(signs.size() > num_signs) {
               signs.remove(signs.size() - 1);
            }
         } else {
            skipOptions.remove("signs");
         }
      } else {
         config.remove("skipOptions");
      }

   }

   public SignSkipOptions get(CartProperties properties) {
      return FieldBackedProperty.CartInternalData.get(properties).signSkipOptionsData;
   }

   public void set(CartProperties properties, SignSkipOptions value) {
      if (value.equals(SignSkipOptions.NONE)) {
         FieldBackedProperty.CartInternalData.get(properties).signSkipOptionsData = SignSkipOptions.NONE;
         this.writeToConfig(properties.getConfig(), Optional.empty());
      } else {
         FieldBackedProperty.CartInternalData.get(properties).signSkipOptionsData = value;
         this.writeToConfig(properties.getConfig(), Optional.of(value));
      }

   }

   public SignSkipOptions get(TrainProperties properties) {
      return FieldBackedProperty.TrainInternalData.get(properties).signSkipOptionsData;
   }

   public void set(TrainProperties properties, SignSkipOptions value) {
      if (value.equals(SignSkipOptions.NONE)) {
         FieldBackedProperty.TrainInternalData.get(properties).signSkipOptionsData = SignSkipOptions.NONE;
         this.writeToConfig(properties.getConfig(), Optional.empty());
      } else {
         FieldBackedProperty.TrainInternalData.get(properties).signSkipOptionsData = value;
         this.writeToConfig(properties.getConfig(), Optional.of(value));
      }

   }

   public void onConfigurationChanged(CartProperties properties) {
      Optional<SignSkipOptions> opt = this.readFromConfig(properties.getConfig());
      FieldBackedProperty.CartInternalData.get(properties).signSkipOptionsData = opt.isPresent() ? (SignSkipOptions)opt.get() : SignSkipOptions.NONE;
   }

   public void onConfigurationChanged(TrainProperties properties) {
      Optional<SignSkipOptions> opt = this.readFromConfig(properties.getConfig());
      FieldBackedProperty.TrainInternalData.get(properties).signSkipOptionsData = opt.isPresent() ? (SignSkipOptions)opt.get() : SignSkipOptions.NONE;
   }
}
