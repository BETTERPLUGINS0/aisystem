package com.bergerkiller.bukkit.tc.properties.standard.type;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.yaml.YamlChangeListener;
import com.bergerkiller.bukkit.common.config.yaml.YamlPath;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentTypeRegistry;
import com.bergerkiller.bukkit.tc.attachments.config.AttachmentModel;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentEntity;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import java.util.Arrays;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

public class AttachmentModelBoundToCart extends AttachmentModel {
   private final AttachmentModelBoundToCart.ModelConfigSupplier configSupplier;

   public AttachmentModelBoundToCart(CartProperties properties) {
      this(new AttachmentModelBoundToCart.ModelConfigSupplier(properties));
   }

   private AttachmentModelBoundToCart(AttachmentModelBoundToCart.ModelConfigSupplier configSupplier) {
      super((Supplier)configSupplier);
      this.configSupplier = configSupplier;
   }

   public boolean isDefault() {
      return this.configSupplier.isDefault();
   }

   public void resetToDefaults() {
      this.configSupplier.makeDefaults();
      this.sync();
   }

   public static ConfigurationNode createDefaults(AttachmentTypeRegistry typeRegistry, EntityType entityType) {
      ConfigurationNode config = new ConfigurationNode();
      typeRegistry.toConfig(config, CartAttachmentEntity.TYPE);
      config.set("entityType", entityType);
      if (entityType == EntityType.MINECART) {
         ConfigurationNode seatNode = new ConfigurationNode();
         typeRegistry.toConfig(seatNode, CartAttachmentSeat.TYPE);
         config.setNodeList("attachments", Arrays.asList(seatNode));
      }

      return config;
   }

   private static class ModelConfigSupplier implements Supplier<ConfigurationNode> {
      private final CartProperties properties;
      private EntityType cartEntityType;
      private EntityType defaultConfigEntityType;
      private ConfigurationNode defaultConfig;

      public ModelConfigSupplier(CartProperties properties) {
         this.properties = properties;
         this.cartEntityType = null;
         this.defaultConfigEntityType = null;
         this.defaultConfig = null;
      }

      public boolean isDefault() {
         return !this.properties.getConfig().isNode("model");
      }

      public ConfigurationNode get() {
         ConfigurationNode config = this.properties.getConfig();
         if (config.isNode("model")) {
            this.defaultConfig = null;
            this.defaultConfigEntityType = null;
            return config.getNode("model");
         } else {
            if (this.cartEntityType == null) {
               MinecartMember<?> member = this.properties.getHolder();
               if (member != null && member.getEntity() != null) {
                  this.cartEntityType = ((CommonMinecart)member.getEntity()).getType();
               }
            }

            EntityType entityType = this.cartEntityType == null ? EntityType.MINECART : this.cartEntityType;
            if (entityType != this.defaultConfigEntityType) {
               this.defaultConfigEntityType = entityType;
               this.defaultConfig = AttachmentModelBoundToCart.createDefaults(AttachmentTypeRegistry.instance(), entityType);
               final ConfigurationNode currConfig = this.defaultConfig;
               currConfig.addChangeListener(new YamlChangeListener() {
                  public void onNodeChanged(YamlPath yamlPath) {
                     currConfig.removeChangeListener(this);
                     if (ModelConfigSupplier.this.defaultConfig == currConfig) {
                        if (!ModelConfigSupplier.this.properties.getConfig().isNode("model")) {
                           Runnable assignTask = () -> {
                              ConfigurationNode currCartConfig = ModelConfigSupplier.this.properties.getConfig();
                              if (!currCartConfig.isNode("model")) {
                                 currCartConfig.set("model", currConfig);
                                 ModelConfigSupplier.this.defaultConfig = null;
                                 ModelConfigSupplier.this.defaultConfigEntityType = null;
                              }

                           };
                           if (ModelConfigSupplier.this.properties.getTrainCarts().isEnabled()) {
                              Bukkit.getScheduler().scheduleSyncDelayedTask(ModelConfigSupplier.this.properties.getTrainCarts(), assignTask);
                           } else {
                              assignTask.run();
                           }
                        }

                     }
                  }
               });
            }

            return this.defaultConfig;
         }
      }

      public void makeDefaults() {
         ConfigurationNode config = this.properties.getConfig();
         config.remove("model");
      }
   }
}
