package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.attachments.config.AttachmentModel;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedProperty;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedStandardCartProperty;
import com.bergerkiller.bukkit.tc.properties.standard.type.AttachmentModelBoundToCart;
import java.util.Iterator;
import java.util.Optional;
import org.bukkit.command.CommandSender;

public final class ModelProperty extends FieldBackedStandardCartProperty<AttachmentModel> {
   public String getPermissionName() {
      return "model (attachment editor)";
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.COMMAND_GIVE_EDITOR.has(sender);
   }

   public AttachmentModel getDefault() {
      return null;
   }

   public void onConfigurationChanged(CartProperties properties) {
      FieldBackedProperty.CartInternalData data = FieldBackedProperty.CartInternalData.get(properties);
      if (data.model != null) {
         data.model.sync();
      }

   }

   public AttachmentModel get(CartProperties properties) {
      FieldBackedProperty.CartInternalData data = FieldBackedProperty.CartInternalData.get(properties);
      if (data.model == null) {
         data.model = new AttachmentModelBoundToCart(properties);
      }

      return data.model;
   }

   public void set(CartProperties properties, AttachmentModel value) {
      FieldBackedProperty.CartInternalData data = FieldBackedProperty.CartInternalData.get(properties);
      if (value != null && !value.isDefault()) {
         if (data.model == null) {
            properties.getConfig().set("model", value.getConfig().clone());
         } else if (data.model != value) {
            data.model.update(value.getConfig());
         }
      } else if (data.model != null) {
         data.model.resetToDefaults();
      } else {
         properties.getConfig().remove("model");
      }

   }

   public AttachmentModel get(TrainProperties properties) {
      return properties.isEmpty() ? this.getDefault() : this.get(properties.get(0));
   }

   public void set(TrainProperties properties, AttachmentModel value) {
      Iterator var3 = properties.iterator();

      while(var3.hasNext()) {
         CartProperties cProp = (CartProperties)var3.next();
         this.set(cProp, value);
      }

   }

   public AttachmentModel getData(FieldBackedProperty.CartInternalData data) {
      return data.model;
   }

   public void setData(FieldBackedProperty.CartInternalData data, AttachmentModel value) {
      if (value != null && !value.isDefault()) {
         if (data.model != null && data.model != value) {
            data.model.update(value.getConfig());
         }
      } else if (data.model != null) {
         data.model.resetToDefaults();
      }

   }

   public Optional<AttachmentModel> readFromConfig(ConfigurationNode config) {
      return config.isNode("model") ? Optional.of(new AttachmentModel(config.getNode("model"))) : Optional.empty();
   }

   public void writeToConfig(ConfigurationNode config, Optional<AttachmentModel> value) {
      if (value.isPresent()) {
         config.set("model", ((AttachmentModel)value.get()).getConfig().clone());
      } else {
         config.remove("model");
      }

   }
}
