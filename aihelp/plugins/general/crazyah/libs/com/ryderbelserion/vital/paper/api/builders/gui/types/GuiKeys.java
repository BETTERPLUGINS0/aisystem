package libs.com.ryderbelserion.vital.paper.api.builders.gui.types;

import io.papermc.paper.persistence.PersistentDataContainerView;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class GuiKeys {
   private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(GuiKeys.class);
   public static NamespacedKey key;

   public GuiKeys() {
      throw new AssertionError();
   }

   @NotNull
   public static String getUUID(ItemStack itemStack) {
      return (String)itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, "");
   }

   @NotNull
   public static NamespacedKey build(String key) {
      return new NamespacedKey(plugin, key);
   }

   @NotNull
   public static ItemStack strip(ItemStack itemStack) {
      PersistentDataContainerView container = itemStack.getPersistentDataContainer();
      if (!container.has(key)) {
         return itemStack;
      } else {
         itemStack.editMeta((itemMeta) -> {
            itemMeta.getPersistentDataContainer().remove(key);
         });
         return itemStack;
      }
   }

   static {
      key = new NamespacedKey(plugin, "mf-gui");
   }
}
