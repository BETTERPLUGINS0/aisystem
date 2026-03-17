package com.bergerkiller.bukkit.tc.properties.defaults;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.controller.spawnable.SpawnableGroup;
import com.bergerkiller.bukkit.tc.controller.spawnable.SpawnableMember;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.IProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.ICartProperty;
import com.bergerkiller.bukkit.tc.properties.api.IProperty;
import com.bergerkiller.bukkit.tc.properties.api.IPropertyRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.bukkit.command.CommandSender;

public class DefaultProperties {
   private final ConfigurationNode config;
   private final List<DefaultProperties.DefaultProperty<?>> properties;
   private final List<DefaultProperties.DefaultProperty<?>> propertiesWithValues;

   public static DefaultProperties of(ConfigurationNode defaultConfig) {
      return new DefaultProperties(defaultConfig);
   }

   private DefaultProperties(ConfigurationNode config) {
      Collection<IProperty<Object>> registeredProperties = IPropertyRegistry.instance().all();
      this.config = config;
      this.properties = new ArrayList(registeredProperties.size());
      this.propertiesWithValues = new ArrayList(registeredProperties.size());
      Iterator var3 = registeredProperties.iterator();

      while(var3.hasNext()) {
         IProperty<?> property = (IProperty)var3.next();
         if (property.isAppliedAsDefault()) {
            Object defaultProperty;
            if (property instanceof ICartProperty) {
               defaultProperty = new DefaultProperties.DefaultCartProperty(property, config);
            } else {
               defaultProperty = new DefaultProperties.DefaultTrainProperty(property, config);
            }

            this.properties.add(defaultProperty);
            if (((DefaultProperties.DefaultProperty)defaultProperty).set) {
               this.propertiesWithValues.add(defaultProperty);
            }
         }
      }

   }

   public ConfigurationNode getConfig() {
      return this.config;
   }

   public void applyTo(TrainProperties properties) {
      Iterator var2 = this.propertiesWithValues.iterator();

      while(var2.hasNext()) {
         DefaultProperties.DefaultProperty<?> defaultProperty = (DefaultProperties.DefaultProperty)var2.next();
         defaultProperty.applyTo(properties);
      }

      properties.tryUpdate();
      var2 = properties.iterator();

      while(var2.hasNext()) {
         CartProperties prop = (CartProperties)var2.next();
         prop.tryUpdate();
      }

   }

   public void applyTo(CartProperties properties) {
      Iterator var2 = this.propertiesWithValues.iterator();

      while(var2.hasNext()) {
         DefaultProperties.DefaultProperty<?> defaultProperty = (DefaultProperties.DefaultProperty)var2.next();
         defaultProperty.applyTo(properties);
      }

      properties.tryUpdate();
   }

   public boolean checkSavedTrainPermissions(CommandSender sender, SpawnableGroup spawnableGroup) {
      List<ConfigurationNode> cartConfigs = new ArrayList(spawnableGroup.getMembers().size());
      Iterator var4 = spawnableGroup.getMembers().iterator();

      while(var4.hasNext()) {
         SpawnableMember member = (SpawnableMember)var4.next();
         cartConfigs.add(member.getConfig());
      }

      return this.checkSavedTrainPermissions(sender, spawnableGroup.getConfig(), cartConfigs);
   }

   public boolean checkSavedTrainPermissions(CommandSender player, ConfigurationNode trainConfig) {
      List<ConfigurationNode> cartConfigs = trainConfig.getNodeList("carts");
      return this.checkSavedTrainPermissions(player, trainConfig, cartConfigs);
   }

   private boolean checkSavedTrainPermissions(CommandSender sender, ConfigurationNode trainConfig, List<ConfigurationNode> cartConfigs) {
      boolean canChangeProperties = Permission.COMMAND_PROPERTIES.has(sender) || Permission.COMMAND_GLOBALPROPERTIES.has(sender);
      Iterator var5 = this.properties.iterator();

      while(var5.hasNext()) {
         DefaultProperties.DefaultProperty<?> property = (DefaultProperties.DefaultProperty)var5.next();
         if (!property.isEqual(trainConfig, cartConfigs)) {
            if (!canChangeProperties) {
               Localization.PROPERTY_NOPERM.message(sender, new String[]{property.permissionName});
               Localization.PROPERTY_NOPERM_ANY.message(sender, new String[0]);
               return false;
            }

            if (!property.property.hasPermission(sender, property.permissionName)) {
               Localization.PROPERTY_NOPERM.message(sender, new String[]{property.permissionName});
               return false;
            }
         }
      }

      return true;
   }

   private static class DefaultCartProperty<T> extends DefaultProperties.DefaultProperty<T> {
      public DefaultCartProperty(IProperty<T> property, ConfigurationNode config) {
         super(property, config);
      }

      public boolean isEqual(ConfigurationNode trainConfig, List<ConfigurationNode> cartConfigs) {
         Iterator var3 = cartConfigs.iterator();

         ConfigurationNode cartConfig;
         do {
            if (!var3.hasNext()) {
               return true;
            }

            cartConfig = (ConfigurationNode)var3.next();
         } while(LogicUtil.bothNullOrEqual(this.property.readFromConfig(cartConfig).orElse(this.property.getDefault()), this.value));

         return false;
      }
   }

   private static class DefaultTrainProperty<T> extends DefaultProperties.DefaultProperty<T> {
      public DefaultTrainProperty(IProperty<T> property, ConfigurationNode config) {
         super(property, config);
      }

      public boolean isEqual(ConfigurationNode trainConfig, List<ConfigurationNode> cartConfigs) {
         return LogicUtil.bothNullOrEqual(this.property.readFromConfig(trainConfig).orElse(this.property.getDefault()), this.value);
      }
   }

   private abstract static class DefaultProperty<T> {
      public final IProperty<T> property;
      public final String permissionName;
      public final boolean set;
      public final T value;

      public DefaultProperty(IProperty<T> property, ConfigurationNode config) {
         this.property = property;
         this.permissionName = property.getPermissionName();
         Optional<T> valueOpt = property.readFromConfig(config);
         this.set = valueOpt.isPresent();
         this.value = valueOpt.orElse(property.getDefault());
      }

      public void applyTo(IProperties properties) {
         properties.set(this.property, this.value);
      }

      public abstract boolean isEqual(ConfigurationNode var1, List<ConfigurationNode> var2);
   }
}
