package com.bergerkiller.bukkit.tc.controller.global;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.config.SavedAttachmentModel;
import com.bergerkiller.bukkit.tc.attachments.ui.AttachmentEditor;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.exception.IllegalNameException;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TrainCartsPlayer implements TrainCarts.Provider {
   private final TrainCarts traincarts;
   private final UUID uuid;
   private WeakReference<Player> player;
   private WeakReference<CartProperties> editedCart;
   private String editedModelName;
   private ConfigurationNode modelClipboard;
   private boolean modelSearchCompactFolders;

   TrainCartsPlayer(TrainCarts traincarts, Player player) {
      this(traincarts, player.getUniqueId());
      this.player = new WeakReference(player);
   }

   TrainCartsPlayer(TrainCarts traincarts, UUID uuid) {
      this.modelSearchCompactFolders = TCConfig.modelSearchCompactFolders;
      this.traincarts = traincarts;
      this.uuid = uuid;
      this.player = LogicUtil.nullWeakReference();
      this.editedCart = LogicUtil.nullWeakReference();
      this.editedModelName = null;
      this.modelClipboard = null;
   }

   public TrainCarts getTrainCarts() {
      return this.traincarts;
   }

   public UUID getUniqueId() {
      return this.uuid;
   }

   public Player getOnlinePlayer() {
      Player p = (Player)this.player.get();
      if (p == null || !p.isValid()) {
         p = Bukkit.getPlayer(this.uuid);
         if (p != null) {
            this.player = new WeakReference(p);
         }
      }

      return p;
   }

   public void sendMessage(String message) {
      Player p = this.getOnlinePlayer();
      if (p != null) {
         p.sendMessage(message);
      }

   }

   public SavedAttachmentModel getEditedModel() {
      return this.editedModelName == null ? null : this.traincarts.getSavedAttachmentModels().getModelOrNone(this.editedModelName);
   }

   public SavedAttachmentModel getEditedModelInit() {
      if (this.editedModelName == null) {
         return null;
      } else {
         try {
            return this.traincarts.getSavedAttachmentModels().setDefaultConfigIfMissing(this.editedModelName, this.getOnlinePlayer());
         } catch (IllegalNameException var2) {
            this.editedModelName = null;
            this.traincarts.getLogger().log(Level.SEVERE, "Unexpected illegal name exception", var2);
            return null;
         }
      }
   }

   public CartProperties getEditedCart() {
      CartProperties edited = (CartProperties)this.editedCart.get();
      if (edited != null && edited.isRemoved()) {
         this.editedCart = LogicUtil.nullWeakReference();
         edited = null;
      }

      return edited;
   }

   public void editModel(SavedAttachmentModel model) {
      boolean changed = false;
      if (model == null) {
         changed = this.editedModelName != null || this.getEditedCart() != null;
         this.editedModelName = null;
      } else {
         changed = !model.getName().equals(this.editedModelName) || this.getEditedCart() != null;
         this.editedModelName = model.getName();
      }

      this.editedCart = LogicUtil.nullWeakReference();
      if (changed) {
         AttachmentEditor.reloadAttachmentEditorFor(this.uuid);
      }

   }

   public void editMember(MinecartMember<?> member) {
      this.editCart(member == null ? null : member.getProperties());
   }

   public void editCart(CartProperties cartProperties) {
      if (cartProperties != null && cartProperties.isRemoved()) {
         throw new IllegalArgumentException("Cannot edit a cart that has been removed");
      } else {
         boolean changed;
         if (cartProperties == null) {
            changed = this.editedModelName != null || this.getEditedCart() != null;
            this.editedCart = LogicUtil.nullWeakReference();
         } else {
            changed = this.getEditedCart() != cartProperties;
            this.editedCart = new WeakReference(cartProperties);
         }

         this.editedModelName = null;
         if (changed) {
            AttachmentEditor.reloadAttachmentEditorFor(this.uuid);
         }

      }
   }

   public ConfigurationNode getModelClipboard() {
      return this.modelClipboard;
   }

   public void setModelClipboard(ConfigurationNode attachmentConfig) {
      this.modelClipboard = attachmentConfig == null ? null : attachmentConfig.clone();
   }

   public boolean getModelSearchCompactFolders() {
      return this.modelSearchCompactFolders;
   }

   public void setModelSearchCompactFolders(boolean compact) {
      this.modelSearchCompactFolders = compact;
   }
}
