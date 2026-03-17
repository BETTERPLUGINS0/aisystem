package com.bergerkiller.bukkit.tc.attachments.control;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapEventPropagation;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetText;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetTabView.Tab;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentTypeRegistry;
import com.bergerkiller.bukkit.tc.attachments.control.light.LightAPIController;
import com.bergerkiller.bukkit.tc.attachments.helper.HelperMethods;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentNode;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetNumberBox;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CartAttachmentLight extends CartAttachment {
   public static final AttachmentType TYPE = new AttachmentType() {
      private int numRegistries = 0;

      public String getID() {
         return "LIGHT";
      }

      public MapTexture getIcon(ConfigurationNode config) {
         return MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/light.png");
      }

      public Attachment createController(ConfigurationNode config) {
         return new CartAttachmentLight();
      }

      public void getDefaultConfig(ConfigurationNode config) {
         config.set("lightType", "BLOCK");
         config.set("lightLevel", 15);
      }

      public void onRegister(AttachmentTypeRegistry registry) {
         ++this.numRegistries;
      }

      public void onUnregister(AttachmentTypeRegistry registry) {
         if (--this.numRegistries <= 0) {
            LightAPIController.disable();
         }

      }

      public void createAppearanceTab(Tab tab, final MapWidgetAttachmentNode attachment) {
         ((<undefinedtype>)tab.addWidget(new MapWidgetButton() {
            private boolean skylight = false;

            public void onAttached() {
               super.onAttached();
               this.skylight = ((String)attachment.getConfig().get("lightType", "BLOCK")).equalsIgnoreCase("SKY");
               this.updateText();
            }

            private void updateText() {
               this.setText("Type: " + (this.skylight ? "SKY" : "BLOCK"));
            }

            public void onActivate() {
               this.skylight = !this.skylight;
               this.updateText();
               attachment.getConfig().set("lightType", this.skylight ? "SKY" : "BLOCK");
               this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
               attachment.resetIcon();
               this.display.playSound(SoundEffect.CLICK);
            }
         })).setBounds(7, 10, 86, 16);
         ((<undefinedtype>)tab.addWidget(new MapWidgetNumberBox() {
            public void onAttached() {
               super.onAttached();
               this.setRange(1.0D, 15.0D);
               this.setIncrement(1.0D);
               this.setValue((double)(Integer)attachment.getConfig().get("lightLevel", 15));
            }

            public String getValueText() {
               return "Level: " + Integer.toString((int)this.getValue());
            }

            public void onValueChanged() {
               attachment.getConfig().set("lightLevel", (int)this.getValue());
               this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
            }
         })).setBounds(0, 30, 100, 16);
         ((<undefinedtype>)tab.addWidget(new MapWidgetText() {
            public void onAttached() {
               super.onAttached();
               this.setText("Powered by LightAPI");
               this.setColor(MapColorPalette.getColor(0, 1, 79));
               this.setShadowColor(MapColorPalette.getColor(0, 0, 220));
            }
         })).setBounds(0, 74, 100, 10);
      }
   };
   private IntVector3 prev_block = null;
   private LightAPIController controller = null;
   private int lightLevel = 15;
   private boolean lightVisible = true;

   public void onAttached() {
      boolean isSky = ((String)this.getConfig().get("lightType", "BLOCK")).equalsIgnoreCase("SKY");
      this.controller = LightAPIController.get(this.getManager().getWorld(), isSky);
      this.lightLevel = (Integer)this.getConfig().get("lightLevel", 15);
      this.lightVisible = !HelperMethods.hasInactiveParent(this);
   }

   public boolean checkCanReload(ConfigurationNode config) {
      boolean isSky = ((String)this.getConfig().get("lightType", "BLOCK")).equalsIgnoreCase("SKY");
      return this.controller == LightAPIController.get(this.getManager().getWorld(), isSky);
   }

   public void onLoad(ConfigurationNode config) {
      super.onLoad(config);
      int newLightLevel = (Integer)this.getConfig().get("lightLevel", 15);
      if (newLightLevel != this.lightLevel) {
         if (this.lightVisible && this.prev_block != null) {
            this.controller.update(this.prev_block, this.lightLevel, newLightLevel);
         }

         this.lightLevel = newLightLevel;
      }

   }

   public void onDetached() {
      if (this.prev_block != null) {
         this.controller.remove(this.prev_block, this.lightLevel);
         this.prev_block = null;
      }

   }

   public void makeVisible(Player viewer) {
   }

   public void makeHidden(Player viewer) {
   }

   public void onActiveChanged(boolean active) {
      this.lightVisible = active;
      if (!active && this.prev_block != null) {
         this.controller.remove(this.prev_block, this.lightLevel);
         this.prev_block = null;
      }

   }

   public void onTick() {
      Vector pos_d = this.getTransform().toVector();
      IntVector3 pos = new IntVector3(pos_d.getX(), pos_d.getY(), pos_d.getZ());
      if (this.lightVisible) {
         if (this.prev_block == null) {
            this.controller.add(pos, this.lightLevel);
            this.prev_block = pos;
         } else if (!pos.equals(this.prev_block)) {
            this.controller.move(this.prev_block, pos, this.lightLevel);
            this.prev_block = pos;
         }
      } else if (this.prev_block != null) {
         this.controller.remove(this.prev_block, this.lightLevel);
         this.prev_block = null;
      }

   }

   public void onMove(boolean absolute) {
   }
}
