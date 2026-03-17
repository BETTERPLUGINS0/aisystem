package com.bergerkiller.bukkit.tc.attachments.ui;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapEventPropagation;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentTypeRegistry;
import com.bergerkiller.bukkit.tc.attachments.config.AttachmentConfig;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentBlock;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentItem;
import com.bergerkiller.bukkit.tc.attachments.ui.menus.AnimationMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.menus.AppearanceMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.menus.GeneralMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.menus.PhysicalMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.menus.PositionMenu;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.AttachmentControllerMember;
import com.bergerkiller.bukkit.tc.utils.SetCallbackCollector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Supplier;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MapWidgetAttachmentNode extends MapWidget implements ItemDropTarget {
   private static final int COL_WIDTH = 17;
   private final MapWidgetAttachmentTree tree;
   private static MapTexture expanded_icon = null;
   private static MapTexture collapsed_icon = null;
   private AttachmentConfig config;
   private final List<MapWidgetAttachmentNode> attachments = new ArrayList();
   private MapWidgetAttachmentNode parentAttachment = null;
   private int col;
   private int row;
   private MapTexture icon = null;
   private boolean changingOrder = false;
   private boolean expanded = true;
   private MapWidgetAttachmentNode.MapWidgetMenuButton appearanceMenuButton;
   private final MapWidgetAttachmentNode.MapWidgetNameBox topNameBox = new MapWidgetAttachmentNode.MapWidgetNameBox();

   public static MapWidgetAttachmentNode createNewRoot(MapWidgetAttachmentTree tree, AttachmentConfig config) {
      return new MapWidgetAttachmentNode((MapWidgetAttachmentNode)null, tree, config);
   }

   public MapWidgetAttachmentNode(MapWidgetAttachmentNode parentAttachment, MapWidgetAttachmentTree tree, AttachmentConfig config) {
      this.parentAttachment = parentAttachment;
      this.tree = tree;
      this.config = config;
      this.loadFromConfig();
      this.setFocusable(true);
   }

   public void loadFromConfig() {
      this.attachments.clear();
      Iterator var1 = this.config.children().iterator();

      while(var1.hasNext()) {
         AttachmentConfig childConfig = (AttachmentConfig)var1.next();
         this.attachments.add(new MapWidgetAttachmentNode(this, this.tree, childConfig));
      }

      this.expanded = this.parentAttachment == null || (Boolean)this.getEditorOption("expanded", true);
      if (!this.expanded && this.attachments.isEmpty()) {
         this.expanded = true;
         this.setEditorOption("expanded", true, true);
      }

   }

   public boolean sync(AttachmentConfig config) {
      this.config = config;
      this.resetIcon();
      if (this.isActivated() && this.appearanceMenuButton != null) {
         this.appearanceMenuButton.setIcon(this.getIcon());
      }

      boolean changed = false;
      List<AttachmentConfig> childConfigs = config.children();

      for(int i = 0; i < childConfigs.size(); ++i) {
         AttachmentConfig childConfig = (AttachmentConfig)childConfigs.get(i);
         if (i < this.attachments.size()) {
            MapWidgetAttachmentNode node = (MapWidgetAttachmentNode)this.attachments.get(i);
            if (node.getConfig() == childConfig.config()) {
               changed |= node.sync(childConfig);
               continue;
            }

            boolean found = false;

            for(int j = i + 1; j < this.attachments.size(); ++j) {
               node = (MapWidgetAttachmentNode)this.attachments.get(j);
               if (node.getConfig() == childConfig.config()) {
                  this.attachments.remove(j);
                  this.attachments.add(i, node);
                  changed = true;
                  found = true;
                  node.sync(childConfig);
                  break;
               }
            }

            if (found) {
               continue;
            }
         }

         this.attachments.add(i, new MapWidgetAttachmentNode(this, this.tree, childConfig));
         changed = true;
      }

      while(this.attachments.size() > childConfigs.size()) {
         this.attachments.remove(childConfigs.size());
         changed = true;
      }

      return changed;
   }

   public MapWidgetAttachmentTree getTree() {
      return this.tree;
   }

   public MapWidgetAttachmentNode getParentAttachment() {
      return this.parentAttachment;
   }

   public void setParentAttachment(MapWidgetAttachmentNode newParent) {
      this.parentAttachment = newParent;
   }

   public void openMenu(MapWidgetAttachmentNode.MenuItem item) {
      this.getTree().onMenuOpen(this, item);
   }

   public List<MapWidgetAttachmentNode> getChildAttachmentNodes() {
      return this.attachments;
   }

   public ConfigurationNode getConfig() {
      return this.config.config();
   }

   public AttachmentConfig getAttachmentConfig() {
      return this.config;
   }

   public <T> T getEditorOption(String name, T defaultValue) {
      ConfigurationNode config = this.getConfig();
      return config.contains("editor." + name) ? config.get("editor." + name, defaultValue) : defaultValue;
   }

   public <T> void setEditorOption(String name, T defaultValue, T value) {
      ConfigurationNode config = this.getConfig();
      if (config.contains("editor." + name) || !LogicUtil.bothNullOrEqual(defaultValue, value)) {
         config.set("editor." + name, value);
      }
   }

   public void update() {
      this.getTree().sync();
   }

   public MapWidgetAttachmentNode addAttachment(ConfigurationNode config) {
      return this.addAttachment(this.attachments.size(), config);
   }

   public MapWidgetAttachmentNode addAttachment(int index, ConfigurationNode config) {
      MapWidgetAttachmentNode attachment = new MapWidgetAttachmentNode(this, this.tree, this.config.addChild(index, config));
      this.attachments.add(index, attachment);
      return attachment;
   }

   public void remove() {
      if (this.parentAttachment != null && this.parentAttachment.attachments.remove(this)) {
         this.config.remove();
         this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "reset");
      }

   }

   public void setCell(int col, int row) {
      this.col = col;
      this.row = row;
   }

   public int getCellColumn() {
      return this.col;
   }

   public int getCellRow() {
      return this.row;
   }

   public AttachmentType getType() {
      return AttachmentTypeRegistry.instance().fromConfig(this.getConfig());
   }

   public void setType(AttachmentType type) {
      AttachmentTypeRegistry.instance().toConfig(this.getConfig(), type);
   }

   public int[] getTargetPath() {
      return this.config.childPath();
   }

   /** @deprecated */
   @Deprecated
   public Attachment getAttachment() {
      List<Attachment> attachments = this.config.liveAttachments();
      return attachments.isEmpty() ? null : (Attachment)attachments.get(0);
   }

   public List<Attachment> getAttachments() {
      return this.config.liveAttachments();
   }

   public <T extends Attachment> List<T> getAttachmentsOfType(Class<T> type) {
      return this.config.liveAttachmentsOfType(type);
   }

   public Set<MinecartMember<?>> getMembersUsingAttachment() {
      SetCallbackCollector<MinecartMember<?>> collector = new SetCallbackCollector();
      this.config.runAction((attachment) -> {
         AttachmentManager manager = attachment.getManager();
         if (manager instanceof AttachmentControllerMember) {
            MinecartMember<?> member = ((AttachmentControllerMember)manager).getMember();
            if (!member.isUnloaded()) {
               collector.accept(member);
            }
         }

      });
      return collector.result();
   }

   public AttachmentEditor getEditor() {
      return this.display == null && this.root != null ? (AttachmentEditor)this.root.getDisplay() : (AttachmentEditor)this.getDisplay();
   }

   public boolean checkModifyPermissions() {
      if (this.display != null) {
         AttachmentType type = this.getType();
         Iterator var2 = this.display.getOwners().iterator();

         while(var2.hasNext()) {
            Player player = (Player)var2.next();
            if (!type.hasPermission(player)) {
               Iterator var4 = this.display.getOwners().iterator();

               while(var4.hasNext()) {
                  Player notifPlayer = (Player)var4.next();
                  notifPlayer.sendMessage(ChatColor.RED + "You do not have permission to modify this type of attachment");
               }

               return false;
            }
         }
      }

      return true;
   }

   public void onAttached() {
      this.setSize(this.parent.getWidth(), 18);
   }

   public void onActivate() {
      super.onActivate();
      if (this.display != null) {
         this.display.playSound(SoundEffect.PISTON_EXTEND);
         int px = this.col * 17 + 1;
         this.appearanceMenuButton = (MapWidgetAttachmentNode.MapWidgetMenuButton)this.addWidget(new MapWidgetAttachmentNode.MapWidgetMenuButton(MapWidgetAttachmentNode.MenuItem.APPEARANCE));
         this.appearanceMenuButton.setIcon(this.getIcon()).setPosition(px, 1);
         px += 17;
         if (this.parentAttachment == null && this.getEditor().getEditedCartProperties() != null) {
            this.addWidget((new MapWidgetAttachmentNode.MapWidgetMenuButton(MapWidgetAttachmentNode.MenuItem.PHYSICAL)).setPosition(px, 1));
            px += 17;
         }

         this.addWidget((new MapWidgetAttachmentNode.MapWidgetMenuButton(MapWidgetAttachmentNode.MenuItem.POSITION)).setPosition(px, 1));
         px += 17;
         this.addWidget((new MapWidgetAttachmentNode.MapWidgetMenuButton(MapWidgetAttachmentNode.MenuItem.ANIMATION)).setPosition(px, 1));
         px += 17;
         this.addWidget((new MapWidgetAttachmentNode.MapWidgetMenuButton(MapWidgetAttachmentNode.MenuItem.GENERAL)).setPosition(px, 1));
         px += 17;
         if (this.isChangingOrder()) {
            Iterator var2 = this.getWidgets().iterator();

            while(var2.hasNext()) {
               MapWidget child = (MapWidget)var2.next();
               child.setEnabled(false);
            }
         }

         List names;
         if (!(names = this.getConfig().getList("names", String.class)).isEmpty()) {
            int xoff = this.col * 17;
            this.topNameBox.setText(String.join(" - ", names));
            this.topNameBox.setBounds(xoff + 1, -this.topNameBox.getHeight(), this.getWidth() - xoff - 2, this.topNameBox.getHeight());
            this.addWidget(this.topNameBox);
         }

      }
   }

   public void onDeactivate() {
      this.clearWidgets();
      this.display.playSound(SoundEffect.PISTON_CONTRACT);
   }

   public void onFocus() {
      this.activate();
   }

   public void onKeyPressed(MapKeyEvent event) {
      if (event.getKey() == Key.LEFT && this.parentAttachment != null && this.getWidgetCount() > 0 && this.getWidget(0).isFocused() && !this.attachments.isEmpty()) {
         this.setExpanded(!this.isExpanded());
      } else {
         super.onKeyPressed(event);
      }

   }

   public boolean acceptItem(ItemStack item) {
      if (this.getType() == CartAttachmentItem.TYPE) {
         this.getConfig().set("item", item.clone());
         this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
         this.resetIcon();
         ((MapWidgetAttachmentNode.MapWidgetMenuButton)this.getWidget(0)).setIcon(this.getIcon());
         return true;
      } else {
         return false;
      }
   }

   public void onBlockInteract(PlayerInteractEvent event) {
      if (event.getAction() == Action.RIGHT_CLICK_BLOCK && this.getType() == CartAttachmentBlock.TYPE) {
         Block block = event.getClickedBlock();
         if (block != null) {
            BlockData blockData = WorldUtil.getBlockData(block);
            this.getConfig().set("blockData", blockData.serializeToString());
            this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
            this.resetIcon();
            ((MapWidgetAttachmentNode.MapWidgetMenuButton)this.getWidget(0)).setIcon(this.getIcon());
            event.setUseInteractedBlock(Result.DENY);
         }
      }

   }

   public void onDraw() {
      int px = this.col * 17;
      if (this.isActivated() || this.isFocused()) {
         byte bgColor;
         if (this.getEditor().isEditingSavedModel()) {
            bgColor = MapColorPalette.getColor(77, 238, 250);
         } else {
            bgColor = MapColorPalette.getColor(220, 255, 220);
         }

         this.view.fillRectangle(px, 0, this.getWidth() - px, this.getHeight(), bgColor);
      }

      if (this.parentAttachment != null) {
         int dotOffset = (this.row - this.parentAttachment.row & 1) == 1 ? 1 : 0;
         byte dotColor = MapColorPalette.getColor(64, 64, 64);

         int childIdx;
         for(childIdx = 0; childIdx < 5; ++childIdx) {
            this.view.drawPixel(px - 17 + 8, childIdx * 2 + dotOffset, dotColor);
         }

         for(childIdx = 1; childIdx < 5; ++childIdx) {
            this.view.drawPixel(px - 17 + 8 + childIdx * 2, 8 + dotOffset, dotColor);
         }

         childIdx = this.parentAttachment.attachments.indexOf(this);
         int tmpX;
         if (childIdx != this.parentAttachment.attachments.size() - 1) {
            for(tmpX = 5; tmpX < 9; ++tmpX) {
               this.view.drawPixel(px - 17 + 8, tmpX * 2 + dotOffset, dotColor);
            }
         }

         tmpX = px - 26;

         for(MapWidgetAttachmentNode tmpNode = this.parentAttachment; tmpNode != null; tmpX -= 17) {
            MapWidgetAttachmentNode tmpNodeParent = tmpNode.parentAttachment;
            if (tmpNodeParent != null && tmpNode != tmpNodeParent.attachments.get(tmpNodeParent.attachments.size() - 1)) {
               int childDotOffset = (this.row - tmpNodeParent.row & 1) == 1 ? 1 : 0;

               for(int n = 0; n < 9; ++n) {
                  this.view.drawPixel(tmpX, n * 2 + childDotOffset, dotColor);
               }
            }

            tmpNode = tmpNodeParent;
         }

         if (!this.attachments.isEmpty()) {
            if (this.expanded) {
               if (expanded_icon == null) {
                  expanded_icon = this.getDisplay().loadTexture("com/bergerkiller/bukkit/tc/textures/attachments/expanded.png");
               }

               this.view.draw(expanded_icon, px - 9 - expanded_icon.getWidth() / 2, (this.view.getHeight() - expanded_icon.getHeight()) / 2 + dotOffset);
            } else {
               if (collapsed_icon == null) {
                  collapsed_icon = this.getDisplay().loadTexture("com/bergerkiller/bukkit/tc/textures/attachments/collapsed.png");
               }

               this.view.draw(collapsed_icon, px - 9 - collapsed_icon.getWidth() / 2, (this.view.getHeight() - collapsed_icon.getHeight()) / 2 + dotOffset);
            }
         }
      }

      if (!this.isActivated()) {
         this.view.draw(this.getIcon(), px + 1, 1);
      }

      if (this.isChangingOrder()) {
         this.view.drawRectangle(px, 0, this.getWidth() - px, this.getHeight(), (byte)18);
      } else if (this.isFocused()) {
         this.view.drawRectangle(px, 0, this.getWidth() - px, this.getHeight(), (byte)119);
      } else if (this.isActivated()) {
         this.view.drawRectangle(px, 0, this.getWidth() - px, this.getHeight(), (byte)30);
      }

   }

   public void resetIcon() {
      this.icon = null;
   }

   public void setChangingOrder(boolean changing) {
      if (this.changingOrder != changing) {
         this.changingOrder = changing;
         this.topNameBox.invalidate();
         this.invalidate();
         Iterator var2 = this.getWidgets().iterator();

         while(var2.hasNext()) {
            MapWidget child = (MapWidget)var2.next();
            child.setEnabled(!changing);
         }
      }

   }

   public boolean isChangingOrder() {
      return this.changingOrder;
   }

   public void setExpanded(boolean expanded) {
      if (this.expanded != expanded) {
         this.expanded = expanded;
         this.setEditorOption("expanded", true, this.expanded);
         this.getTree().updateView();
         this.invalidate();
      }

   }

   public boolean isExpanded() {
      return this.expanded;
   }

   private MapTexture getIcon() {
      if (this.icon == null) {
         AttachmentType type = this.getType();
         if (type == null) {
            this.icon = MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/missing.png");
         } else {
            this.icon = this.getType().getIcon(this.getConfig());
         }
      }

      return this.icon;
   }

   public String toString() {
      AttachmentType type = this.getType();
      String name = type == null ? "MISSING_TYPE" : type.toString();
      int[] var3 = this.getTargetPath();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int p = var3[var5];
         name = name + "." + p;
      }

      return name;
   }

   private class MapWidgetNameBox extends MapWidget {
      private static final int SCROLL_DELAY = 40;
      private static final int SCROLL_HOLD = 20;
      private static final int SCROLL_STEP = 3;
      private String text = "";
      private int textWidth = -1;
      private int textScroll = 0;
      private int delayCtr = 0;
      private int holdCtr = 0;

      public MapWidgetNameBox() {
         this.setDepthOffset(1);
         this.setSize(64, 6);
      }

      public void setText(String text) {
         this.text = text;
         this.textWidth = -1;
         this.textScroll = 0;
         this.delayCtr = 0;
         this.holdCtr = 0;
         this.invalidate();
      }

      private int getTextWidth() {
         int w = this.textWidth;
         if (w == -1) {
            this.textWidth = w = this.view.calcFontSize(MapFont.TINY, this.text).width;
         }

         return w;
      }

      public void onTick() {
         int overflow = this.getTextWidth() - this.getWidth() + 1;
         if (overflow > 0 && ++this.delayCtr > 40) {
            int newScroll = Math.min(overflow, this.textScroll + 3);
            if (newScroll != this.textScroll) {
               this.textScroll = newScroll;
               this.invalidate();
            } else if (++this.holdCtr > 20) {
               this.delayCtr = 0;
               this.holdCtr = 0;
               this.textScroll = 0;
               this.invalidate();
            }
         }

      }

      public void onDraw() {
         if (!this.text.isEmpty() && !MapWidgetAttachmentNode.this.isChangingOrder()) {
            int width = Math.min(this.getWidth(), this.getTextWidth() + 1);
            int x = (this.getWidth() - width) / 2;
            this.view.fillRectangle(x, 0, width, this.getHeight(), (byte)30);
            this.view.getView(x + 1, 1, width - 2, this.getHeight() - 1).draw(MapFont.TINY, -this.textScroll, 0, MapColorPalette.getColor(255, 255, 255), this.text);
         }
      }
   }

   private class MapWidgetMenuButton extends MapWidgetBlinkyButton {
      private final MapWidgetAttachmentNode.MenuItem _menu;

      public MapWidgetMenuButton(MapWidgetAttachmentNode.MenuItem menu) {
         this._menu = menu;
         this.setTooltip(Character.toUpperCase(menu.name().charAt(0)) + menu.name().substring(1).toLowerCase(Locale.ENGLISH));
         if (menu.getIcon() != null) {
            this.setIcon(menu.getIcon());
         }

      }

      public void onClick() {
         MapWidgetAttachmentNode.this.openMenu(this._menu);
      }
   }

   public static enum MenuItem {
      APPEARANCE(AppearanceMenu::new, (String)null),
      POSITION(PositionMenu::new, "attachments/move.png"),
      ANIMATION(AnimationMenu::new, "attachments/animation.png"),
      GENERAL(GeneralMenu::new, "attachments/general_menu.png"),
      PHYSICAL(PhysicalMenu::new, "attachments/physical.png");

      private final Supplier<? extends MapWidgetMenu> _menuConstructor;
      private final String _icon;

      private MenuItem(Supplier<? extends MapWidgetMenu> menuConstructor, String icon) {
         this._menuConstructor = menuConstructor;
         this._icon = icon;
      }

      public String getIcon() {
         return this._icon;
      }

      public MapWidgetMenu createMenu(MapWidgetAttachmentNode attachmentNode) {
         MapWidgetMenu menu = (MapWidgetMenu)this._menuConstructor.get();
         menu.setAttachment(attachmentNode);
         return menu;
      }

      // $FF: synthetic method
      private static MapWidgetAttachmentNode.MenuItem[] $values() {
         return new MapWidgetAttachmentNode.MenuItem[]{APPEARANCE, POSITION, ANIMATION, GENERAL, PHYSICAL};
      }
   }
}
