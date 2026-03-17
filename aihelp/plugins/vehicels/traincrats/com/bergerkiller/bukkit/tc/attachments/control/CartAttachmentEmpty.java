package com.bergerkiller.bukkit.tc.attachments.control;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;
import org.bukkit.entity.Player;

public class CartAttachmentEmpty extends CartAttachment {
   public static final AttachmentType TYPE = new AttachmentType() {
      public String getID() {
         return "EMPTY";
      }

      public double getSortPriority() {
         return -1.0D;
      }

      public MapTexture getIcon(ConfigurationNode config) {
         return MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/empty.png");
      }

      public Attachment createController(ConfigurationNode config) {
         return new CartAttachmentEmpty();
      }
   };

   public void makeVisible(Player viewer) {
   }

   public void makeHidden(Player viewer) {
   }

   public void onTick() {
   }

   public void onMove(boolean absolute) {
   }
}
