package libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces.types;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiAction;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiFiller;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiItem;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiType;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.objects.components.InteractionComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IBaseGui {
   Map<Integer, GuiItem> getGuiItems();

   String getTitle();

   void setTitle(String var1);

   Component title();

   int getRows();

   void setRows(int var1);

   int getSize();

   GuiType getGuiType();

   GuiFiller getFiller();

   void close(Player var1, Reason var2, boolean var3);

   void close(Player var1, boolean var2);

   void close(Player var1);

   void updateTitle(Player var1);

   void updateInventory(Player var1);

   void updateTitles();

   void updateInventories();

   boolean isUpdating();

   void setUpdating(boolean var1);

   void addInteractionComponent(InteractionComponent... var1);

   void removeInteractionComponent(InteractionComponent var1);

   boolean canPerformOtherActions();

   boolean isInteractionsDisabled();

   boolean canPlaceItems();

   boolean canTakeItems();

   boolean canSwapItems();

   boolean canDropItems();

   void giveItem(Player var1, ItemStack var2);

   void giveItem(Player var1, ItemStack... var2);

   void setItem(int var1, GuiItem var2);

   void setItem(int var1, int var2, @NotNull GuiItem var3);

   void setItem(@NotNull List<Integer> var1, @NotNull GuiItem var2);

   void addItem(@NotNull GuiItem... var1);

   void addItem(boolean var1, @NotNull GuiItem... var2);

   void removeItem(ItemStack var1);

   void removeItem(int var1, int var2);

   void removeItem(GuiItem var1);

   void removeItem(int var1);

   void setDefaultClickAction(@Nullable GuiAction<InventoryClickEvent> var1);

   @Nullable
   GuiItem getGuiItem(int var1);

   void addSlotAction(int var1, @Nullable GuiAction<InventoryClickEvent> var2);

   @Nullable
   GuiAction<InventoryClickEvent> getSlotAction(int var1);

   GuiAction<InventoryClickEvent> getDefaultTopClickAction();

   void setDefaultTopClickAction(@Nullable GuiAction<InventoryClickEvent> var1);

   GuiAction<InventoryClickEvent> getPlayerInventoryAction();

   void setOpenGuiAction(@Nullable GuiAction<InventoryOpenEvent> var1);

   GuiAction<InventoryClickEvent> getOutsideClickAction();

   GuiAction<InventoryClickEvent> getDefaultClickAction();

   void setDragAction(@Nullable GuiAction<InventoryDragEvent> var1);

   GuiAction<InventoryCloseEvent> getCloseGuiAction();

   void setCloseGuiAction(@Nullable GuiAction<InventoryCloseEvent> var1);

   GuiAction<InventoryOpenEvent> getOpenGuiAction();

   void setPlayerInventoryAction(@Nullable GuiAction<InventoryClickEvent> var1);

   GuiAction<InventoryDragEvent> getDragAction();

   void setOutsideClickAction(@Nullable GuiAction<InventoryClickEvent> var1);

   void open(Player var1, boolean var2);

   void open(Player var1);

   default Set<InteractionComponent> safeCopy(Set<InteractionComponent> components) {
      return components.isEmpty() ? EnumSet.noneOf(InteractionComponent.class) : EnumSet.copyOf(components);
   }
}
