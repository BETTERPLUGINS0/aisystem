package com.bergerkiller.bukkit.tc.attachments.control;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapEventPropagation;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetSubmitText;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetTabView.Tab;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.VirtualDisplayBlockEntity;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.control.schematic.MovingSchematic;
import com.bergerkiller.bukkit.tc.attachments.control.schematic.WorldEditSchematicLoader;
import com.bergerkiller.bukkit.tc.attachments.helper.HelperMethods;
import com.bergerkiller.bukkit.tc.attachments.particle.VirtualDisplayBoundingBox;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentNode;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetSizeBox;
import com.bergerkiller.bukkit.tc.attachments.ui.menus.PositionMenu;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CartAttachmentSchematic extends CartAttachment {
   public static final AttachmentType TYPE = new AttachmentType() {
      public String getID() {
         return "WE_SCHEMATIC";
      }

      public String getName() {
         return "SCHEMATIC";
      }

      public boolean isListed(Player player) {
         return TrainCarts.plugin.getWorldEditSchematicLoader().isEnabled() && this.hasPermission(player);
      }

      public boolean hasPermission(Player player) {
         return Permission.USE_SCHEMATIC_ATTACHMENTS.has(player);
      }

      public MapTexture getIcon(ConfigurationNode config) {
         return MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/schematic.png");
      }

      public Attachment createController(ConfigurationNode config) {
         return new CartAttachmentSchematic();
      }

      public void createAppearanceTab(final Tab tab, final MapWidgetAttachmentNode attachment) {
         final MapWidgetSubmitText textBox = new MapWidgetSubmitText() {
            public void onAttached() {
               this.setDescription("Enter schematic");
            }

            public void onAccept(String text) {
               attachment.getConfig().set("schematic", text.trim());
               this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed", attachment);
               attachment.resetIcon();
               Iterator var2 = tab.getWidgets().iterator();

               while(var2.hasNext()) {
                  MapWidget widget = (MapWidget)var2.next();
                  if (widget instanceof SchematicButton) {
                     ((SchematicButton)widget).updateText();
                     break;
                  }
               }

            }
         };
         tab.addWidget(textBox);

         class SchematicButton extends MapWidgetButton {
            // $FF: synthetic field
            final MapWidgetAttachmentNode val$attachment;

            SchematicButton(MapWidgetAttachmentNode var2) {
               this.val$attachment = var2;
            }

            public void onAttached() {
               this.updateText();
            }

            public void updateText() {
               String schematicName = (String)this.val$attachment.getConfig().get("schematic", "");
               if (schematicName.isEmpty()) {
                  this.setText("<No Schematic>");
               } else {
                  this.setText(schematicName);
               }

            }
         }

         ((<undefinedtype>)tab.addWidget(new SchematicButton() {
            public void onActivate() {
               textBox.activate();
            }
         })).setBounds(0, 5, 100, 13);
      }

      public void createPositionMenu(PositionMenu.Builder builder) {
         builder.addRow((menu) -> {
            return (new MapWidgetButton() {
               public void onAttached() {
                  super.onAttached();
                  this.updateText((Boolean)menu.getPositionConfigValue("clipEnabled", true));
               }

               public void onActivate() {
                  boolean enabled = !(Boolean)menu.getPositionConfigValue("clipEnabled", true);
                  menu.updatePositionConfig((config) -> {
                     if (enabled) {
                        config.remove("clipEnabled");
                     } else {
                        config.set("clipEnabled", false);
                     }

                  });
                  this.updateText(enabled);
               }

               private void updateText(boolean enabled) {
                  this.setText(enabled ? "Enabled" : "Disabled");
               }
            }).setBounds(32, 0, 72, 11);
         }).addLabel(0, 3, "Clipping").setSpacingAbove(3);
         builder.addPositionSlider("originX", "Origin X", "Schematic Origin X-Coordinate", 0.0D).setSpacingAbove(3);
         builder.addPositionSlider("originY", "Origin Y", "Schematic Origin Y-Coordinate", 0.0D);
         builder.addPositionSlider("originZ", "Origin Z", "Schematic Origin Z-Coordinate", 0.0D);
         builder.addRow((menu) -> {
            return (new MapWidgetSizeBox() {
               public void onAttached() {
                  super.onAttached();
                  this.setInitialSize((Double)menu.getPositionConfigValue("spacingX", 0.0D), (Double)menu.getPositionConfigValue("spacingY", 0.0D), (Double)menu.getPositionConfigValue("spacingZ", 0.0D));
               }

               public void onSizeChanged() {
                  menu.updatePositionConfig((config) -> {
                     if (this.x.getValue() == 0.0D && this.y.getValue() == 0.0D && this.z.getValue() == 0.0D) {
                        config.remove("spacingX");
                        config.remove("spacingY");
                        config.remove("spacingZ");
                     } else {
                        config.set("spacingX", this.x.getValue());
                        config.set("spacingY", this.y.getValue());
                        config.set("spacingZ", this.z.getValue());
                     }

                  });
               }
            }).setRangeAndDefault(true, 0.0D).setBounds(25, 0, menu.getSliderWidth(), 35);
         }).addLabel(0, 3, "Gap X").addLabel(0, 15, "Gap Y").addLabel(0, 27, "Gap Z").setSpacingAbove(3);
         builder.addSizeBox();
      }
   };
   private WorldEditSchematicLoader.SchematicReader schematicReader;
   private MovingSchematic schematic;
   private CartAttachmentSchematic.DebugDisplay debug;

   public void onAttached() {
      this.schematic = new MovingSchematic(this.getManager());
      this.schematicReader = TrainCarts.plugin.getWorldEditSchematicLoader().startReading((String)this.getConfig().get("schematic", ""));
      this.loadNextBlocks();
   }

   public void onDetached() {
      this.schematic = null;
      this.schematicReader.abort();
   }

   public boolean checkCanReload(ConfigurationNode config) {
      if (!super.checkCanReload(config)) {
         return false;
      } else {
         return this.schematicReader.fileName().equals(config.get("schematic", ""));
      }
   }

   public void onLoad(ConfigurationNode config) {
      this.schematic.setScale(this.getConfiguredPosition().size);
      this.schematic.setHasClipping((Boolean)config.getOrDefault("position.clipEnabled", true));
      this.schematic.setSpacing(new Vector((Double)config.getOrDefault("position.spacingX", 0.0D), (Double)config.getOrDefault("position.spacingY", 0.0D), (Double)config.getOrDefault("position.spacingZ", 0.0D)));
      this.schematic.setOrigin(new Vector((Double)config.getOrDefault("position.originX", 0.0D), (Double)config.getOrDefault("position.originY", 0.0D), (Double)config.getOrDefault("position.originZ", 0.0D)));
   }

   private void loadNextBlocks() {
      if (!this.schematicReader.isDone()) {
         WorldEditSchematicLoader.SchematicBlock block = this.schematicReader.next();
         if (block != null) {
            this.schematic.setBlockBounds(block.schematic.dimensions);
            double originX = 0.5D * (double)block.schematic.dimensions.x;
            double originY = 0.0D;
            double originZ = 0.5D * (double)block.schematic.dimensions.z;

            do {
               this.schematic.addBlock((double)block.x - originX, (double)block.y - originY, (double)block.z - originZ, block.blockData);
            } while((block = this.schematicReader.next()) != null);

            this.schematic.resendMounts();
         }

      }
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
      this.schematic.spawn(viewer, new Vector(0.0D, 0.0D, 0.0D));
      if (this.debug != null) {
         this.debug.makeVisible(viewer);
      }

   }

   public void makeHidden(AttachmentViewer viewer) {
      this.schematic.destroy(viewer);
      if (this.debug != null) {
         this.debug.makeHidden(viewer);
      }

   }

   public void onFocus() {
      if (this.debug == null) {
         this.debug = new CartAttachmentSchematic.DebugDisplay();
         Iterator var1 = this.getAttachmentViewers().iterator();

         while(var1.hasNext()) {
            AttachmentViewer viewer = (AttachmentViewer)var1.next();
            this.debug.makeVisible(viewer);
         }
      } else {
         this.debug.focus();
      }

   }

   public void onBlur() {
      if (this.debug != null) {
         this.debug.blur();
      }

   }

   public void onTick() {
      this.loadNextBlocks();
      if (this.debug != null) {
         this.debug.ticksShown++;
         if (this.debug.ticksShown == 2) {
            this.debug.setGlowColor(HelperMethods.getFocusGlowColor(this));
         } else if (this.debug.ticksShown >= 40) {
            this.debug.hideForAll();
            this.debug = null;
         }
      }

   }

   public void onTransformChanged(Matrix4x4 transform) {
      this.schematic.updatePosition(transform);
      if (this.debug != null) {
         this.debug.updatePosition();
      }

   }

   public void onMove(boolean absolute) {
      this.schematic.syncPosition(absolute);
      if (this.debug != null) {
         this.debug.syncPosition(absolute);
      }

   }

   private class DebugDisplay {
      private VirtualDisplayBoundingBox bbox = new VirtualDisplayBoundingBox(CartAttachmentSchematic.this.getManager());
      private VirtualDisplayBlockEntity originPoint;
      private int ticksShown = 0;

      public DebugDisplay() {
         this.bbox.update(CartAttachmentSchematic.this.schematic.createBBOX());
         this.bbox.setGlowColor((ChatColor)null);
         if (CartAttachmentSchematic.this.schematic.hasOrigin()) {
            this.initOriginPoint();
         }

      }

      public void makeVisible(AttachmentViewer viewer) {
         this.bbox.spawn(viewer, new Vector(0.0D, 0.0D, 0.0D));
         if (this.originPoint != null) {
            this.originPoint.spawn(viewer, new Vector(0.0D, 0.0D, 0.0D));
         }

      }

      public void makeHidden(AttachmentViewer viewer) {
         this.bbox.destroy(viewer);
         if (this.originPoint != null) {
            this.originPoint.destroy(viewer);
         }

      }

      public void hideForAll() {
         this.bbox.destroyForAll();
         if (this.originPoint != null) {
            this.originPoint.destroyForAll();
         }

      }

      public void updatePosition() {
         this.bbox.update(CartAttachmentSchematic.this.schematic.createBBOX());
         if (CartAttachmentSchematic.this.schematic.hasOrigin()) {
            if (this.originPoint == null) {
               this.initOriginPoint();
               Iterator var1 = CartAttachmentSchematic.this.getAttachmentViewers().iterator();

               while(var1.hasNext()) {
                  AttachmentViewer viewer = (AttachmentViewer)var1.next();
                  this.originPoint.spawn(viewer, new Vector(0.0D, 0.0D, 0.0D));
               }
            } else {
               this.originPoint.updatePosition(CartAttachmentSchematic.this.schematic.createOriginPointTransform());
            }
         } else if (this.originPoint != null) {
            this.originPoint.destroyForAll();
            this.originPoint = null;
         }

      }

      private void initOriginPoint() {
         this.originPoint = new VirtualDisplayBlockEntity(CartAttachmentSchematic.this.getManager());
         this.originPoint.setBlockData(BlockData.fromMaterial(MaterialUtil.getMaterial("REDSTONE_BLOCK")));
         this.originPoint.setScale(new Vector(0.1D, 0.1D, 0.1D));
         this.originPoint.setGlowColor(ChatColor.RED);
         this.originPoint.updatePosition(CartAttachmentSchematic.this.schematic.createOriginPointTransform());
         this.originPoint.syncPosition(true);
      }

      public void syncPosition(boolean absolute) {
         this.bbox.syncPosition(absolute);
         if (this.originPoint != null) {
            this.originPoint.syncPosition(absolute);
         }

      }

      public void focus() {
         this.setGlowColor((ChatColor)null);
         this.ticksShown = 0;
      }

      public void blur() {
         this.setGlowColor((ChatColor)null);
         this.ticksShown = 20;
      }

      public void setGlowColor(ChatColor color) {
         this.bbox.setGlowColor(color);
      }
   }
}
