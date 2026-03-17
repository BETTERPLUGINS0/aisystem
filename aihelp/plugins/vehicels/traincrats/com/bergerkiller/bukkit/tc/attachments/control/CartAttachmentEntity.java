package com.bergerkiller.bukkit.tc.attachments.control;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.entity.CommonEntity;
import com.bergerkiller.bukkit.common.map.MapEventPropagation;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetSubmitText;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetTabView.Tab;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.wrappers.BoatWoodType;
import com.bergerkiller.bukkit.common.wrappers.ChatText;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.VirtualEntity;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.helper.HelperMethods;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentNode;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetSelectionBox;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetToggleButton;
import com.bergerkiller.bukkit.tc.attachments.ui.entity.MapWidgetEntityTypeList;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLivingHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.decoration.EntityArmorStandHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.vehicle.boat.EntityBoatHandle;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CartAttachmentEntity extends CartAttachment {
   public static final AttachmentType TYPE = new AttachmentType() {
      public String getID() {
         return "ENTITY";
      }

      public MapTexture getIcon(ConfigurationNode config) {
         EntityType type = (EntityType)config.get("entityType", EntityType.MINECART);
         if (type == EntityType.BOAT) {
            BoatWoodType boatWoodType = config.contains("boatWoodType") ? (BoatWoodType)config.get("boatWoodType", BoatWoodType.OAK) : BoatWoodType.OAK;
            Material itemMaterial;
            if (boatWoodType == BoatWoodType.OAK) {
               itemMaterial = MaterialUtil.getMaterial("LEGACY_BOAT");
            } else {
               itemMaterial = MaterialUtil.getMaterial("LEGACY_BOAT_" + boatWoodType.name());
               if (itemMaterial == null) {
                  itemMaterial = MaterialUtil.getMaterial("LEGACY_BOAT");
               }
            }

            return TCConfig.resourcePack.getItemTexture(new ItemStack(itemMaterial), 16, 16);
         } else if (type == EntityType.MINECART) {
            return TCConfig.resourcePack.getItemTexture(new ItemStack(MaterialUtil.getMaterial("LEGACY_MINECART")), 16, 16);
         } else if (type == EntityType.MINECART_CHEST) {
            return TCConfig.resourcePack.getItemTexture(new ItemStack(MaterialUtil.getMaterial("LEGACY_STORAGE_MINECART")), 16, 16);
         } else if (type == EntityType.MINECART_COMMAND) {
            return TCConfig.resourcePack.getItemTexture(new ItemStack(MaterialUtil.getMaterial("LEGACY_COMMAND_MINECART")), 16, 16);
         } else if (type == EntityType.MINECART_FURNACE) {
            return TCConfig.resourcePack.getItemTexture(new ItemStack(MaterialUtil.getMaterial("LEGACY_POWERED_MINECART")), 16, 16);
         } else if (type == EntityType.MINECART_HOPPER) {
            return TCConfig.resourcePack.getItemTexture(new ItemStack(MaterialUtil.getMaterial("LEGACY_HOPPER_MINECART")), 16, 16);
         } else if (type == EntityType.MINECART_MOB_SPAWNER) {
            return TCConfig.resourcePack.getItemTexture(new ItemStack(MaterialUtil.getMaterial("LEGACY_MOB_SPAWNER")), 16, 16);
         } else {
            return type == EntityType.MINECART_TNT ? TCConfig.resourcePack.getItemTexture(new ItemStack(MaterialUtil.getMaterial("LEGACY_EXPLOSIVE_MINECART")), 16, 16) : MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/mob.png");
         }
      }

      public Attachment createController(ConfigurationNode config) {
         return new CartAttachmentEntity();
      }

      public void getDefaultConfig(ConfigurationNode config) {
         config.set("entityType", EntityType.MINECART);
      }

      public void createAppearanceTab(Tab tab, final MapWidgetAttachmentNode attachment) {
         final MapWidget boatTypeSelector = ((<undefinedtype>)tab.addWidget(new MapWidgetSelectionBox() {
            public void onAttached() {
               super.onAttached();
               BoatWoodType[] var1 = BoatWoodType.values();
               int var2 = var1.length;

               for(int var3 = 0; var3 < var2; ++var3) {
                  BoatWoodType type = var1[var3];
                  this.addItem(type.name());
               }

               if (attachment.getConfig().contains("boatWoodType")) {
                  this.setSelectedItem((String)attachment.getConfig().get("boatWoodType", "OAK"));
               } else {
                  this.setSelectedItem("OAK");
               }

            }

            public void onSelectedItemChanged() {
               if (this.isVisible()) {
                  attachment.getConfig().set("boatWoodType", this.getSelectedItem());
                  this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
                  attachment.resetIcon();
               }

            }
         })).setBounds(0, 15, 100, 12).setVisible(false);
         ((<undefinedtype>)tab.addWidget(new MapWidgetEntityTypeList() {
            public void onAttached() {
               super.onAttached();
               this.setEntityType((EntityType)attachment.getConfig().get("entityType", EntityType.MINECART));
               boatTypeSelector.setVisible(this.getEntityType() == EntityType.BOAT);
            }

            public void onEntityTypeChanged() {
               attachment.getConfig().set("entityType", this.getEntityType());
               boatTypeSelector.setVisible(this.getEntityType() == EntityType.BOAT);
               this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
               attachment.resetIcon();
            }
         })).setBounds(0, 1, 100, 12);
         ((<undefinedtype>)tab.addWidget(new MapWidgetToggleButton<Boolean>() {
            public void onSelectionChanged() {
               attachment.getConfig().set("sitting", this.getSelectedOption());
               this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
               attachment.resetIcon();
               this.display.playSound(SoundEffect.CLICK);
            }
         })).addOptions((b) -> {
            return "Sitting: " + (b ? "YES" : "NO");
         }, new Boolean[]{Boolean.TRUE, Boolean.FALSE}).setSelectedOption((Boolean)attachment.getConfig().getOrDefault("sitting", false)).setBounds(0, 56, 102, 12);
         ((<undefinedtype>)tab.addWidget(new MapWidgetButton() {
            private void refreshText() {
               if (attachment.getConfig().contains("nametag")) {
                  ConfigurationNode nametag = attachment.getConfig().getNode("nametag");
                  if ((Boolean)nametag.get("used", true)) {
                     if ((Boolean)nametag.get("visible", true)) {
                        this.setText("Nametag (vis.)");
                     } else {
                        this.setText("Nametag (invis.)");
                     }

                     return;
                  }
               }

               this.setText("No Nametag");
            }

            public void onAttached() {
               this.refreshText();
            }

            public void onActivate() {
               ConfigurationNode nametag = attachment.getConfig().getNode("nametag");
               if ((Boolean)nametag.get("used", true)) {
                  if ((Boolean)nametag.get("visible", true)) {
                     nametag.set("visible", false);
                  } else {
                     nametag.set("used", false);
                  }
               } else {
                  nametag.set("used", true);
                  nametag.set("visible", true);
                  if (!nametag.contains("text")) {
                     nametag.set("text", "Nametag");
                  }
               }

               this.refreshText();
               this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
            }
         })).setBounds(0, 69, 79, 12);
         final MapWidgetSubmitText nameTagTextBox = (MapWidgetSubmitText)tab.addWidget(new MapWidgetSubmitText() {
            public void onAttached() {
               this.setDescription("Enter nametag title");
            }

            public void onAccept(String text) {
               ConfigurationNode nametag = attachment.getConfig().getNode("nametag");
               nametag.set("used", true);
               nametag.set("text", text);
               this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
            }
         });
         ((<undefinedtype>)tab.addWidget(new MapWidgetButton() {
            public void onActivate() {
               nameTagTextBox.activate();
            }
         })).setText("Edit").setBounds(80, 69, 22, 12);
      }
   };
   private VirtualEntity actual;
   private VirtualEntity entity;

   private VirtualEntity actualEntity() {
      return this.actual != null ? this.actual : this.entity;
   }

   public void onDetached() {
      super.onDetached();
      this.entity = null;
      this.actual = null;
   }

   public boolean checkCanReload(ConfigurationNode config) {
      if (!super.checkCanReload(config)) {
         return false;
      } else {
         VirtualEntity displayed = this.actualEntity();
         EntityType entityType = (EntityType)config.getOrDefault("entityType", EntityType.MINECART);
         if (displayed.getEntityType() != entityType) {
            return false;
         } else {
            boolean currSitting = this.actual != null;
            boolean newSitting = (Boolean)config.getOrDefault("sitting", false);
            return newSitting == currSitting || entityType.name().equals("SHULKER");
         }
      }
   }

   public void onAttached() {
      super.onAttached();
      EntityType entityType = (EntityType)this.getConfig().getOrDefault("entityType", EntityType.MINECART);
      boolean sitting = (Boolean)this.getConfig().getOrDefault("sitting", false);
      if (!isEntityTypeSupported(entityType)) {
         entityType = EntityType.MINECART;
      }

      if (this.getParent() == null && VirtualEntity.isMinecart(entityType) && this.hasController()) {
         CommonEntity<?> entity = this.getController().getMember().getEntity();
         this.entity = new VirtualEntity(this.getManager(), entity.getEntityId(), entity.getUniqueId());
         this.entity.setUseParentMetadata(true);
         this.entity.setRespawnOnPitchFlip(true);
      } else {
         this.entity = new VirtualEntity(this.getManager());
      }

      this.entity.setEntityType(entityType);
      if (this.entity.isMinecart() && !this.entity.isExperimentalMinecart()) {
         double MINECART_CENTER_Y = 0.3765D;
         this.entity.setPosition(new Vector(0.0D, 0.3765D, 0.0D));
         this.entity.setRelativeOffset(0.0D, -0.3765D, 0.0D);
      }

      if (sitting || entityType.name().equals("SHULKER")) {
         this.actual = this.entity;
         this.entity = new VirtualEntity(this.getManager());
         this.entity.setEntityType(EntityType.ARMOR_STAND);
         this.entity.getMetaData().set(EntityHandle.DATA_FLAGS, (byte)32);
         this.entity.getMetaData().set(EntityHandle.DATA_NO_GRAVITY, true);
         this.entity.getMetaData().set(EntityArmorStandHandle.DATA_ARMORSTAND_FLAGS, (byte)25);
      }

   }

   public void onLoad(ConfigurationNode config) {
      VirtualEntity displayed = this.actualEntity();
      if (config.isNode("nametag") && (Boolean)config.get("nametag.used", true)) {
         ConfigurationNode nametag = config.getNode("nametag");
         boolean visible = (Boolean)nametag.get("visible", true);
         String text = (String)nametag.get("text", "");
         displayed.getMetaData().set(EntityHandle.DATA_CUSTOM_NAME, ChatText.fromMessage(text));
         displayed.getMetaData().set(EntityHandle.DATA_CUSTOM_NAME_VISIBLE, visible);
      } else {
         displayed.getMetaData().set(EntityHandle.DATA_CUSTOM_NAME, Common.evaluateMCVersion(">=", "1.13") ? null : ChatText.empty());
      }

      if (displayed.getEntityType() == EntityType.BOAT) {
         displayed.getMetaData().set(EntityBoatHandle.DATA_WOOD_TYPE, (BoatWoodType)config.get("boatWoodType", BoatWoodType.OAK));
      }

   }

   public void onFocus() {
      this.actualEntity().setGlowColor(HelperMethods.getFocusGlowColor(this));
   }

   public void onBlur() {
      this.actualEntity().setGlowColor((ChatColor)null);
   }

   public boolean containsEntityId(int entityId) {
      return this.entity != null && this.entity.getEntityId() == entityId || this.actual != null && this.actual.getEntityId() == entityId;
   }

   public int getMountEntityId() {
      return this.entity.isMountable() ? this.entity.getEntityId() : -1;
   }

   public void applyPassengerSeatTransform(Matrix4x4 transform) {
      VirtualEntity displayed = this.actualEntity();
      if (displayed.isMinecart()) {
         transform.translate(0.0D, displayed.getMountOffset(), 0.0D);
      } else {
         Matrix4x4 relativeMatrix = new Matrix4x4();
         relativeMatrix.translate(0.0D, displayed.getMountOffset(), 0.0D);
         Matrix4x4.multiply(relativeMatrix, transform, transform);
      }
   }

   public boolean isMinecartInterpolation() {
      return this.actual == null && this.entity.isMinecart();
   }

   /** @deprecated */
   @Deprecated
   public void makeVisible(Player player) {
      this.makeVisible(this.getManager().asAttachmentViewer(player));
   }

   /** @deprecated */
   @Deprecated
   public void makeHidden(Player player) {
      this.makeHidden(this.getManager().asAttachmentViewer(player));
   }

   public void makeVisible(AttachmentViewer viewer) {
      if (this.actual != null) {
         this.actual.spawn(viewer, new Vector());
      }

      this.entity.spawn(viewer, new Vector());
      if (this.actual != null) {
         viewer.getVehicleMountController().mount(this.entity.getEntityId(), this.actual.getEntityId());
      }

   }

   public void makeHidden(AttachmentViewer viewer) {
      if (this.actual != null) {
         this.actual.destroy(viewer);
      }

      this.entity.destroy(viewer);
   }

   public void onTransformChanged(Matrix4x4 transform) {
      this.entity.updatePosition(transform);
      if (this.actual != null) {
         this.actual.updatePosition(transform);
      }

   }

   public void onMove(boolean absolute) {
      this.entity.syncPosition(absolute);
      if (this.actual != null) {
         if (this.actual.syncPositionIfMounted()) {
            this.actual.syncPosition(absolute);
         } else {
            this.actual.syncPositionSilent();
         }
      }

   }

   public void onTick() {
   }

   public static boolean isEntityTypeSupported(EntityType entityType) {
      String name = entityType.name();
      if (!name.equals("WEATHER") && !name.equals("COMPLEX_PART")) {
         switch(entityType) {
         case PAINTING:
         case FISHING_HOOK:
         case LIGHTNING:
         case PLAYER:
         case EXPERIENCE_ORB:
         case UNKNOWN:
            return false;
         default:
            return VirtualEntity.isLivingEntity(entityType) ? PacketPlayOutSpawnEntityLivingHandle.isEntityTypeSupported(entityType) : PacketPlayOutSpawnEntityHandle.isEntityTypeSupported(entityType);
         }
      } else {
         return false;
      }
   }
}
