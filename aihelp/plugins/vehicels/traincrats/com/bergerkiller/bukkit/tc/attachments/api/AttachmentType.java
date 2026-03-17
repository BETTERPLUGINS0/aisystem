package com.bergerkiller.bukkit.tc.attachments.api;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetTabView.Tab;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentNode;
import com.bergerkiller.bukkit.tc.attachments.ui.menus.PositionMenu;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface AttachmentType {
   String MODEL_TYPE_ID = "MODEL";

   String getID();

   default Plugin getPlugin() {
      return CommonUtil.getPluginByClass(this.getClass());
   }

   default String getName() {
      return this.getID();
   }

   default double getSortPriority() {
      return 0.0D;
   }

   default boolean isListed(Player player) {
      return this.hasPermission(player);
   }

   default boolean hasPermission(Player player) {
      return true;
   }

   default MapTexture getIcon(ConfigurationNode config) {
      return MapTexture.createEmpty(16, 16);
   }

   default void migrateConfiguration(ConfigurationNode config) {
   }

   default void getDefaultConfig(ConfigurationNode config) {
   }

   default void createAppearanceTab(Tab tab, MapWidgetAttachmentNode attachment) {
   }

   default void createPositionMenu(PositionMenu.Builder builder) {
   }

   Attachment createController(ConfigurationNode var1);

   default void onRegister(AttachmentTypeRegistry registry) {
   }

   default void onUnregister(AttachmentTypeRegistry registry) {
   }
}
