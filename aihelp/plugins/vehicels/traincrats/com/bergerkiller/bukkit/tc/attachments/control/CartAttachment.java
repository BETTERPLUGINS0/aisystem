package com.bergerkiller.bukkit.tc.attachments.control;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.internal.CommonCapabilities;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentInternalState;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentTypeRegistry;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.AttachmentControllerMember;
import com.bergerkiller.generated.net.minecraft.world.entity.monster.EntityShulkerHandle;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class CartAttachment implements Attachment {
   private final AttachmentInternalState state = new AttachmentInternalState();

   public AttachmentInternalState getInternalState() {
      return this.state;
   }

   public void onAttached() {
   }

   public void onDetached() {
   }

   public void onLoad(ConfigurationNode config) {
   }

   public Collection<Player> getViewers() {
      return this.getManager().getViewers();
   }

   public Collection<AttachmentViewer> getAttachmentViewers() {
      return this.getManager().getAttachmentViewers();
   }

   public boolean hasController() {
      return this.getManager() instanceof AttachmentControllerMember;
   }

   public AttachmentControllerMember getController() {
      return (AttachmentControllerMember)this.getManager();
   }

   public MinecartMember<?> getMember() {
      return this.getController().getMember();
   }

   public boolean containsEntityId(int entityId) {
      return false;
   }

   public int getMountEntityId() {
      return -1;
   }

   public void onTransformChanged(Matrix4x4 transform) {
   }

   /** @deprecated */
   @Deprecated
   protected void updateGlowColorFor(UUID entityUUID, ChatColor color, Player viewer) {
      this.updateGlowColorFor(entityUUID, color, this.getManager().asAttachmentViewer(viewer));
   }

   /** @deprecated */
   @Deprecated
   protected void updateGlowColorFor(UUID entityUUID, ChatColor color, AttachmentViewer viewer) {
      viewer.updateGlowColor(entityUUID, color);
   }

   protected void updateGlowColor(UUID entityUUID, ChatColor color) {
      Iterator var3 = this.getAttachmentViewers().iterator();

      while(var3.hasNext()) {
         AttachmentViewer viewer = (AttachmentViewer)var3.next();
         viewer.updateGlowColor(entityUUID, color);
      }

   }

   public static void registerDefaultAttachments() {
      AttachmentTypeRegistry.instance().register(CartAttachmentEmpty.TYPE);
      AttachmentTypeRegistry.instance().register(CartAttachmentEntity.TYPE);
      AttachmentTypeRegistry.instance().register(CartAttachmentItem.TYPE);
      AttachmentTypeRegistry.instance().register(CartAttachmentModel.TYPE);
      AttachmentTypeRegistry.instance().register(CartAttachmentSeat.TYPE);
      AttachmentTypeRegistry.instance().register(CartAttachmentText.TYPE);
      AttachmentTypeRegistry.instance().register(CartAttachmentHitBox.TYPE);
      AttachmentTypeRegistry.instance().register(CartAttachmentSound.TYPE);
      AttachmentTypeRegistry.instance().register(CartAttachmentSequencer.TYPE);
      if (EntityShulkerHandle.T.isAvailable()) {
         AttachmentTypeRegistry.instance().register(CartAttachmentPlatform.TYPE);
      }

      if (CommonCapabilities.HAS_DISPLAY_ENTITY) {
         AttachmentTypeRegistry.instance().register(CartAttachmentBlock.TYPE);
         if (Bukkit.getPluginManager().getPlugin("WorldEdit") != null) {
            AttachmentTypeRegistry.instance().register(CartAttachmentSchematic.TYPE);
         }
      }

   }
}
