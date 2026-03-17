package com.bergerkiller.bukkit.tc.properties;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentTypeRegistry;
import com.bergerkiller.bukkit.tc.attachments.config.SavedAttachmentModel;
import com.bergerkiller.bukkit.tc.attachments.config.SavedAttachmentModelStore;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.controller.spawnable.SpawnableGroup;
import com.bergerkiller.bukkit.tc.properties.standard.StandardProperties;
import com.bergerkiller.bukkit.tc.properties.standard.type.CartLockOrientation;
import com.bergerkiller.bukkit.tc.utils.SetCallbackCollector;
import com.bergerkiller.bukkit.tc.utils.modularconfiguration.ModularConfigurationEntry;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.bukkit.command.CommandSender;

public class SavedTrainProperties implements TrainCarts.Provider, SavedAttachmentModelStore.ModelUsing {
   private final TrainCarts traincarts;
   private final ModularConfigurationEntry<SavedTrainProperties> entry;

   SavedTrainProperties(TrainCarts traincarts, ModularConfigurationEntry<SavedTrainProperties> entry) {
      this.traincarts = traincarts;
      this.entry = entry;
   }

   public TrainCarts getTrainCarts() {
      return this.traincarts;
   }

   public SavedTrainPropertiesStore getModule() {
      return this.entry.isRemoved() ? null : SavedTrainPropertiesStore.createModule(this.entry.getModule());
   }

   public boolean isNone() {
      return this.entry.isRemoved();
   }

   public String getName() {
      return this.entry.getName();
   }

   public ConfigurationNode getConfig() {
      return this.entry.getConfig();
   }

   public ConfigurationNode getExportedConfig() {
      ConfigurationNode exportedConfig = this.getConfig().clone();
      exportedConfig.remove("claims");
      exportedConfig.set("usedModels", this.getUsedModelsAsExport());
      return exportedConfig;
   }

   public void getUsedModels(SetCallbackCollector<SavedAttachmentModel> collector) {
      Iterator var2 = this.getCarts().iterator();

      while(var2.hasNext()) {
         ConfigurationNode cart = (ConfigurationNode)var2.next();
         ConfigurationNode modelConfig = cart.getNodeIfExists("model");
         if (modelConfig != null) {
            this.traincarts.getSavedAttachmentModels().findModelsUsedInConfiguration(modelConfig, collector);
         }
      }

   }

   public boolean isEmpty() {
      if (this.entry.isRemoved()) {
         return true;
      } else {
         ConfigurationNode config = this.entry.getConfig();
         return !config.contains("carts") && !config.contains("spawnPattern");
      }
   }

   public boolean hasSpawnPattern() {
      return !this.entry.isRemoved() && this.entry.getConfig().contains("spawnPattern");
   }

   public String getSpawnPattern() {
      return this.entry.isRemoved() ? null : (String)this.entry.getConfig().getOrDefault("spawnPattern", String.class, (Object)null);
   }

   public void reverse() {
      if (!this.isEmpty()) {
         List<ConfigurationNode> carts = this.entry.getWritableConfig().getNodeList("carts");
         if (carts.isEmpty() && this.hasSpawnPattern()) {
            this.entry.getWritableConfig().set("flipped", !(Boolean)this.entry.getWritableConfig().get("flipped", false));
         } else {
            carts.forEach(StandardProperties::reverseSavedCart);
            Collections.reverse(carts);
            this.entry.getWritableConfig().setNodeList("carts", carts);
         }
      }
   }

   public void setOrientationLocked(boolean locked) {
      if (!this.isEmpty()) {
         List<ConfigurationNode> carts = this.entry.getWritableConfig().getNodeList("carts");
         Iterator var3 = carts.iterator();

         while(var3.hasNext()) {
            ConfigurationNode cart = (ConfigurationNode)var3.next();
            if (locked) {
               StandardProperties.LOCK_ORIENTATION_FLIPPED.writeToConfig(cart, Optional.of(CartLockOrientation.locked((Boolean)cart.get("flipped", false))));
            } else {
               StandardProperties.LOCK_ORIENTATION_FLIPPED.writeToConfig(cart, Optional.empty());
            }
         }

         this.entry.getWritableConfig().setNodeList("carts", carts);
      }
   }

   public Set<SavedClaim> getClaims() {
      return this.entry.isRemoved() ? Collections.emptySet() : SavedClaim.loadClaims(this.entry.getConfig());
   }

   public void setClaims(Collection<SavedClaim> claims) {
      if (!this.entry.isRemoved()) {
         SavedClaim.saveClaims(this.entry.getWritableConfig(), claims);
      }

   }

   public boolean hasPermission(CommandSender sender) {
      return this.entry.isRemoved() || SavedClaim.hasPermission(this.entry.getConfig(), sender);
   }

   public SpawnableGroup toSpawnableGroup() {
      return SpawnableGroup.fromConfig(this);
   }

   public List<ConfigurationNode> getCarts() {
      return this.entry.getConfig().isNode("carts") ? this.entry.getConfig().getNodeList("carts") : Collections.emptyList();
   }

   public int getNumberOfCarts() {
      return this.getCarts().size();
   }

   public int getNumberOfSeats() {
      int count = 0;
      Iterator var2 = this.getCarts().iterator();

      while(var2.hasNext()) {
         ConfigurationNode cart = (ConfigurationNode)var2.next();
         if (cart.isNode("model")) {
            count += getNumberOfSeatAttachmentsRecurse(cart.getNode("model"));
         }
      }

      return count;
   }

   public double getTotalTrainLength() {
      double totalLength = 0.0D;
      List<ConfigurationNode> carts = this.getCarts();
      if (!carts.isEmpty()) {
         double prevCartCouplerLength = 0.0D;
         boolean first = true;

         ConfigurationNode cart;
         for(Iterator var7 = carts.iterator(); var7.hasNext(); totalLength += (Double)cart.getOrDefault("model.physical.cartLength", 0.9800000190734863D)) {
            cart = (ConfigurationNode)var7.next();
            double cartCouplerLength = (Double)cart.getOrDefault("model.physical.cartCouplerLength", 0.5D * TCConfig.cartDistanceGap);
            if (first) {
               first = false;
            } else {
               totalLength += prevCartCouplerLength + cartCouplerLength;
            }

            prevCartCouplerLength = cartCouplerLength;
         }
      }

      return totalLength;
   }

   public int getSpawnLimit() {
      return this.entry.isRemoved() ? -1 : (Integer)this.entry.getConfig().getOrDefault("spawnLimit", -1);
   }

   public void setSpawnLimit(int limit) {
      if (!this.entry.isRemoved()) {
         if (limit >= 0) {
            this.entry.getWritableConfig().set("spawnLimit", limit);
         } else {
            this.entry.getWritableConfig().remove("spawnLimit");
         }
      }

   }

   public int getSpawnLimitCurrentCount() {
      if (this.entry.isRemoved()) {
         return 0;
      } else {
         int count = 0;
         Iterator var2 = TrainPropertiesStore.getAll().iterator();

         while(var2.hasNext()) {
            TrainProperties properties = (TrainProperties)var2.next();
            if (((List)properties.get(StandardProperties.ACTIVE_SAVED_TRAIN_SPAWN_LIMITS)).contains(this.getName())) {
               ++count;
            }
         }

         return count;
      }
   }

   private static int getNumberOfSeatAttachmentsRecurse(ConfigurationNode attachmentConfig) {
      int count = 0;
      if (AttachmentTypeRegistry.instance().fromConfig(attachmentConfig) == CartAttachmentSeat.TYPE) {
         count = 1;
      }

      ConfigurationNode childAttachment;
      if (attachmentConfig.isNode("attachments")) {
         for(Iterator var2 = attachmentConfig.getNodeList("attachments").iterator(); var2.hasNext(); count += getNumberOfSeatAttachmentsRecurse(childAttachment)) {
            childAttachment = (ConfigurationNode)var2.next();
         }
      }

      return count;
   }
}
