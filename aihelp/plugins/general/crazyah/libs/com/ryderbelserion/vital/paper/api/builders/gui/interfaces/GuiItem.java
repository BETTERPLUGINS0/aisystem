package libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces;

import com.google.common.base.Preconditions;
import java.util.UUID;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.types.GuiKeys;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuiItem {
   private final UUID uuid;
   private GuiAction<InventoryClickEvent> action;
   private ItemStack itemStack;

   public GuiItem(@NotNull ItemStack itemStack, @Nullable GuiAction<InventoryClickEvent> action) {
      this.uuid = UUID.randomUUID();
      Preconditions.checkNotNull(itemStack, "The ItemStack for the gui Item cannot be null!");
      if (action != null) {
         this.action = action;
      }

      this.setItemStack(itemStack);
   }

   public GuiItem(@NotNull ItemStack itemStack) {
      this((ItemStack)itemStack, (GuiAction)null);
   }

   public GuiItem(@NotNull Material material) {
      this((ItemStack)ItemStack.of(material), (GuiAction)null);
   }

   public GuiItem(@NotNull Material material, @Nullable GuiAction<InventoryClickEvent> action) {
      this(ItemStack.of(material), action);
   }

   @NotNull
   public final ItemStack getItemStack() {
      return this.itemStack;
   }

   public void setItemStack(@NotNull ItemStack itemStack) {
      Preconditions.checkNotNull(itemStack, "The ItemStack for the GUI Item cannot be null!");
      if (itemStack.getType() == Material.AIR) {
         this.itemStack = itemStack.clone();
      } else {
         ItemStack item = itemStack.clone();
         item.editMeta((itemMeta) -> {
            PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
            pdc.set(GuiKeys.key, PersistentDataType.STRING, this.uuid.toString());
         });
         this.itemStack = item;
      }
   }

   @NotNull
   public final UUID getUuid() {
      return this.uuid;
   }

   @Nullable
   public final GuiAction<InventoryClickEvent> getAction() {
      return this.action;
   }

   public void setAction(@Nullable GuiAction<InventoryClickEvent> action) {
      this.action = action;
   }
}
