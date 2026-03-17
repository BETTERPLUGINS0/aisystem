package com.bergerkiller.bukkit.tc.properties;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.StreamUtil;
import com.bergerkiller.bukkit.tc.CollisionMode;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.properties.defaults.DefaultProperties;
import com.bergerkiller.bukkit.tc.properties.defaults.DefaultPropertiesLookup;
import com.bergerkiller.bukkit.tc.properties.standard.StandardProperties;
import com.bergerkiller.bukkit.tc.properties.standard.type.CollisionMobCategory;
import com.bergerkiller.bukkit.tc.properties.standard.type.TrainNameFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;

public class TrainPropertiesStore extends LinkedHashSet<CartProperties> {
   private static boolean hasChanges = false;
   private static final long serialVersionUID = 1L;
   private static final String propertiesFile = "TrainProperties.yml";
   private static FileConfiguration config = null;
   private static DefaultPropertiesLookup defaultProperties = null;
   private static TrainPropertiesMap trainProperties = new TrainPropertiesMap();

   public static Collection<TrainProperties> getAll() {
      return trainProperties.values();
   }

   public static Collection<TrainProperties> matchAll(String expression) {
      if (expression != null && !expression.isEmpty()) {
         String[] elements = expression.split("\\*", -1);
         boolean first = expression.startsWith("*");
         boolean last = expression.endsWith("*");
         return (Collection)trainProperties.values().stream().filter((p) -> {
            return p.matchName(elements, first, last);
         }).collect(StreamUtil.toUnmodifiableList());
      } else {
         return Collections.emptySet();
      }
   }

   public static void rename(TrainProperties properties, String newTrainName) {
      if (!properties.getTrainName().equals(newTrainName)) {
         if (exists(newTrainName)) {
            throw new IllegalArgumentException("Another train with name '" + newTrainName + "' already exists");
         } else {
            properties.getTrainCarts().getOfflineGroups().rename(properties.getTrainName(), newTrainName);
            ConfigurationNode oldConfig = properties.getConfig();
            trainProperties.remove(properties.getTrainName());
            config.remove(properties.getTrainName());
            properties.trainname = newTrainName;
            trainProperties.add(newTrainName, properties);
            config.set(newTrainName, oldConfig);
            hasChanges = true;
         }
      }
   }

   public static void remove(String trainName) {
      TrainProperties prop = trainProperties.remove(trainName);
      if (prop != null) {
         hasChanges = true;
         config.remove(trainName);
         if (!prop.isEmpty()) {
            Iterator var2 = (new ArrayList(prop)).iterator();

            while(true) {
               CartProperties cProp;
               do {
                  if (!var2.hasNext()) {
                     return;
                  }

                  cProp = (CartProperties)var2.next();
                  prop.remove(cProp);
               } while(cProp.getHolder() != null && cProp.getHolder().getEntity() != null && !((CommonMinecart)cProp.getHolder().getEntity()).isRemoved());

               CartPropertiesStore.remove(cProp.getUUID());
            }
         }
      }
   }

   public static TrainProperties get(String trainName) {
      return trainName == null ? null : trainProperties.get(trainName);
   }

   public static TrainProperties getRelaxed(String trainName) {
      return trainName == null ? null : trainProperties.getRelaxed(trainName);
   }

   public static TrainProperties create(String trainname) {
      if (trainname == null) {
         return null;
      } else {
         TrainProperties prop = trainProperties.get(trainname);
         return prop != null ? prop : createDefaultWithName(trainname);
      }
   }

   public static String generateTrainName() {
      return TrainNameFormat.DEFAULT.search(TrainPropertiesStore::isUseableName);
   }

   public static String generateTrainName(String format) {
      return TrainNameFormat.parse(format).search(TrainPropertiesStore::isUseableName);
   }

   public static boolean isMatchingTrainNameFormat(String trainName, String format) {
      return TrainNameFormat.parse(format).matches(trainName);
   }

   public static String generateSplitTrainName(String trainName) {
      int split_idx = trainName.indexOf(126);
      int index = -1;
      if (split_idx != -1 && (index = fromAlphabeticRadix(trainName.substring(split_idx + 1))) != -1) {
         trainName = trainName.substring(0, split_idx);
      }

      trainName = trainName + "~";

      String splitName;
      do {
         StringBuilder var10000 = (new StringBuilder()).append(trainName);
         ++index;
         splitName = var10000.append(toAlphabeticRadix(index)).toString();
      } while(exists(splitName));

      return splitName;
   }

   private static String toAlphabeticRadix(int num) {
      char[] str = Integer.toString(num, 26).toCharArray();

      for(int i = 0; i < str.length; ++i) {
         str[i] = (char)(str[i] + (str[i] > '9' ? 10 : 49));
      }

      return new String(str);
   }

   private static int fromAlphabeticRadix(String radixStr) {
      if (radixStr.isEmpty()) {
         return -1;
      } else {
         char[] str = radixStr.toCharArray();

         for(int i = 0; i < str.length; ++i) {
            char c = str[i];
            if (c < 'a' || c > 'z') {
               return -1;
            }

            str[i] = (char)(str[i] - (c > 'j' ? 10 : 49));
         }

         try {
            return Integer.parseInt(new String(str), 26);
         } catch (NumberFormatException var4) {
            return -1;
         }
      }
   }

   public static TrainProperties create() {
      return createDefaultWithName(generateTrainName());
   }

   private static TrainProperties createDefaultWithName(String newTrainName) {
      ConfigurationNode newTrainConfig = config.getNode(newTrainName);
      TrainProperties prop = new TrainProperties(TrainCarts.plugin, newTrainName, newTrainConfig);
      trainProperties.add(newTrainName, prop);
      prop.onConfigurationChanged(true);
      prop.setDefault();
      hasChanges = true;
      return prop;
   }

   public static TrainProperties createSplitFrom(TrainProperties fromTrainProperties) {
      String name = generateSplitTrainName(fromTrainProperties.getTrainName());
      ConfigurationNode newTrainConfig = config.getNode(name);
      fromTrainProperties.saveToConfig().cloneIntoExcept(newTrainConfig, Collections.singleton("carts"));
      TrainProperties prop = new TrainProperties(fromTrainProperties.getTrainCarts(), name, newTrainConfig);
      trainProperties.add(name, prop);
      prop.onConfigurationChanged(false);
      hasChanges = true;
      return prop;
   }

   public static TrainProperties createFromConfig(ConfigurationNode savedTrainConfig) {
      String name = ((TrainNameFormat)StandardProperties.TRAIN_NAME_FORMAT.readFromConfig(savedTrainConfig).orElse(TrainNameFormat.DEFAULT)).search(TrainPropertiesStore::isUseableName);
      ConfigurationNode newTrainConfig = config.getNode(name);
      savedTrainConfig.cloneIntoExcept(newTrainConfig, Collections.singleton("carts"));
      TrainProperties prop = new TrainProperties(TrainCarts.plugin, name, newTrainConfig);
      trainProperties.add(name, prop);
      prop.onConfigurationChanged(false);
      hasChanges = true;
      return prop;
   }

   public static boolean exists(String trainname) {
      return trainProperties != null && trainProperties.containsKey(trainname);
   }

   public static boolean isUseableName(String trainName) {
      return !exists(trainName);
   }

   public static void clearAll() {
      trainProperties.clear();
      config.clear();
      CartPropertiesStore.clearAllCarts();
      hasChanges = true;
   }

   public static void load(TrainCarts traincarts) {
      loadDefaults(traincarts);
      config = new FileConfiguration(traincarts, "TrainProperties.yml");
      config.load();
      if (fixDeprecation(config)) {
         config.save();
      }

      Iterator var1 = config.getNodes().iterator();

      while(var1.hasNext()) {
         ConfigurationNode node = (ConfigurationNode)var1.next();
         TrainProperties prop = new TrainProperties(traincarts, node.getName(), node);
         if (prop.isEmpty()) {
            config.remove(node.getName());
            traincarts.log(Level.WARNING, "Train properties with name " + prop.getTrainName() + " has no carts!");
         } else {
            trainProperties.add(prop.getTrainName(), prop);
            prop.onConfigurationChanged(true);
         }
      }

      hasChanges = false;
      config.addChangeListener((path) -> {
         hasChanges = true;
      });
   }

   public static boolean fixDeprecation(FileConfiguration config) {
      boolean changed = false;
      Iterator var2 = config.getNodes().iterator();

      while(true) {
         ConfigurationNode node;
         CollisionMobCategory[] var4;
         int var5;
         int var6;
         CollisionMobCategory collisionConfigObject;
         String mobType;
         do {
            if (!var2.hasNext()) {
               return changed;
            }

            node = (ConfigurationNode)var2.next();
            if (node.contains("allowLinking")) {
               node.set("collision.train", CollisionMode.fromLinking((Boolean)node.get("allowLinking", true)));
               node.remove("allowLinking");
               changed = true;
            }

            if (node.contains("collision.mobs")) {
               var4 = CollisionMobCategory.values();
               var5 = var4.length;

               for(var6 = 0; var6 < var5; ++var6) {
                  collisionConfigObject = var4[var6];
                  if (collisionConfigObject.isMobCategory()) {
                     node.set("collision." + collisionConfigObject.getMobType(), ((CollisionMode)node.get("collision.mobs", CollisionMode.DEFAULT)).toString());
                  }
               }

               node.remove("collision.mobs");
               changed = true;
            }

            if (node.contains("pushAway")) {
               var4 = CollisionMobCategory.values();
               var5 = var4.length;

               for(var6 = 0; var6 < var5; ++var6) {
                  collisionConfigObject = var4[var6];
                  if (collisionConfigObject.isMobCategory()) {
                     mobType = collisionConfigObject.getMobType();
                     node.set("collision." + mobType, CollisionMode.fromPushing((Boolean)node.get("pushAway." + mobType, false)).toString());
                  }
               }

               node.set("collision.players", CollisionMode.fromPushing((Boolean)node.get("pushAway.players", false)).toString());
               node.set("collision.misc", CollisionMode.fromPushing((Boolean)node.get("pushAway.misc", true)).toString());
               node.remove("pushAway");
               changed = true;
            }

            if (node.contains("allowMobsEnter")) {
               if ((Boolean)node.get("allowMobsEnter", false)) {
                  var4 = CollisionMobCategory.values();
                  var5 = var4.length;

                  for(var6 = 0; var6 < var5; ++var6) {
                     collisionConfigObject = var4[var6];
                     if (collisionConfigObject.isMobCategory()) {
                        mobType = collisionConfigObject.getMobType();
                        node.set("collision." + mobType, CollisionMode.ENTER.toString());
                     }
                  }
               }

               node.remove("allowMobsEnter");
               changed = true;
            }
         } while(!node.contains("mobenter") && !node.contains("mobsenter"));

         if (((Boolean)node.get("mobenter", false) || (Boolean)node.get("mobsenter", false)) && (Boolean)node.get("allowMobsEnter", false)) {
            var4 = CollisionMobCategory.values();
            var5 = var4.length;

            for(var6 = 0; var6 < var5; ++var6) {
               collisionConfigObject = var4[var6];
               if (collisionConfigObject.isMobCategory()) {
                  mobType = collisionConfigObject.getMobType();
                  node.set("collision." + mobType, CollisionMode.ENTER.toString());
               }
            }
         }

         node.remove("mobenter");
         node.remove("mobenters");
         changed = true;
      }
   }

   public static void loadDefaults(TrainCarts traincarts) {
      defaultProperties = DefaultPropertiesLookup.load(traincarts);
   }

   public static void save(boolean autosave) {
      if (!autosave || hasChanges) {
         List<TrainProperties> removedTrainProperties = (List)trainProperties.values().stream().filter((prop) -> {
            return !prop.hasHolder() && !prop.getTrainCarts().getOfflineGroups().contains(prop.getTrainName());
         }).collect(Collectors.toList());
         removedTrainProperties.forEach((prop) -> {
            remove(prop.getTrainName());
         });
         config.save();
         hasChanges = false;
      }
   }

   public static DefaultProperties getDefaultsByName(String name) {
      return defaultProperties.getByName(name);
   }

   public static DefaultProperties getDefaultsByPlayer(Player player) {
      return defaultProperties.getForPlayer(player);
   }

   public static void bindGroupToProperties(TrainProperties properties, MinecartGroup group) {
      properties.updateHolder(group, true);
   }

   public static void unbindGroupFromProperties(TrainProperties properties, MinecartGroup group) {
      properties.updateHolder(group, false);
   }
}
