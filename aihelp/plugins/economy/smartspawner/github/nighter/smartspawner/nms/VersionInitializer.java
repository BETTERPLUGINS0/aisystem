package github.nighter.smartspawner.nms;

import github.nighter.smartspawner.SmartSpawner;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class VersionInitializer {
   private final SmartSpawner plugin;
   private final String serverVersion;
   private static boolean supportsDataComponentAPI = false;
   private static Class<?> dataComponentTypeKeysClass = null;
   private static Class<?> dataComponentTypesClass = null;
   private static Class<?> tooltipDisplayClass = null;

   public VersionInitializer(SmartSpawner plugin) {
      this.plugin = plugin;
      this.serverVersion = Bukkit.getServer().getBukkitVersion();
   }

   public void initialize() {
      this.plugin.debug("Server version: " + this.serverVersion);
      this.detectDataComponentAPISupport();
   }

   private void detectDataComponentAPISupport() {
      try {
         dataComponentTypeKeysClass = Class.forName("io.papermc.paper.registry.keys.DataComponentTypeKeys");
         dataComponentTypesClass = Class.forName("io.papermc.paper.datacomponent.DataComponentTypes");
         tooltipDisplayClass = Class.forName("io.papermc.paper.datacomponent.item.TooltipDisplay");
         supportsDataComponentAPI = true;
         this.plugin.getLogger().info("Server supports DataComponent API (Paper 1.21.5+)");
      } catch (Exception var2) {
         supportsDataComponentAPI = false;
         this.plugin.getLogger().info("Server does not support DataComponent API, using fallback methods (Paper < 1.21.5)");
      }

   }

   public static boolean supportsDataComponentAPI() {
      return supportsDataComponentAPI;
   }

   public static void hideTooltip(ItemStack item) {
      if (item != null) {
         if (supportsDataComponentAPI) {
            try {
               hideTooltipUsingDataComponent(item);
            } catch (Exception var2) {
               hideTooltipUsingItemFlag(item);
            }
         } else {
            hideTooltipUsingItemFlag(item);
         }

      }
   }

   private static void hideTooltipUsingDataComponent(ItemStack item) {
      try {
         Class<?> dataComponentTypeClass = Class.forName("io.papermc.paper.datacomponent.DataComponentType");
         Class<?> registryAccessClass = Class.forName("io.papermc.paper.registry.RegistryAccess");
         Class<?> registryKeyClass = Class.forName("io.papermc.paper.registry.RegistryKey");
         Object tooltipDisplayType = dataComponentTypesClass.getField("TOOLTIP_DISPLAY").get((Object)null);
         Object registryAccess = registryAccessClass.getMethod("registryAccess").invoke((Object)null);
         Object dataComponentTypeKey = registryKeyClass.getField("DATA_COMPONENT_TYPE").get((Object)null);
         Object registry = registryAccess.getClass().getMethod("getRegistry", registryKeyClass).invoke(registryAccess, dataComponentTypeKey);
         Object blockEntityDataKey = dataComponentTypeKeysClass.getField("BLOCK_ENTITY_DATA").get((Object)null);
         Object blockEntityDataComponent = registry.getClass().getMethod("get", Object.class).invoke(registry, blockEntityDataKey);
         Set<Object> hiddenComponents = Set.of(blockEntityDataComponent);
         Object tooltipDisplayBuilder = tooltipDisplayClass.getMethod("tooltipDisplay").invoke((Object)null);
         tooltipDisplayBuilder = tooltipDisplayBuilder.getClass().getMethod("hiddenComponents", Set.class).invoke(tooltipDisplayBuilder, hiddenComponents);
         Object tooltipDisplay = tooltipDisplayBuilder.getClass().getMethod("build").invoke(tooltipDisplayBuilder);
         item.getClass().getMethod("setData", dataComponentTypeClass, Object.class).invoke(item, tooltipDisplayType, tooltipDisplay);
      } catch (Exception var13) {
         throw new RuntimeException("Failed to hide tooltip using DataComponent API", var13);
      }
   }

   private static void hideTooltipUsingItemFlag(ItemStack item) {
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         try {
            ItemFlag hideAdditionalTooltip = ItemFlag.valueOf("HIDE_ADDITIONAL_TOOLTIP");
            meta.addItemFlags(new ItemFlag[]{hideAdditionalTooltip});
            item.setItemMeta(meta);
         } catch (IllegalArgumentException var3) {
         }
      }

   }
}
