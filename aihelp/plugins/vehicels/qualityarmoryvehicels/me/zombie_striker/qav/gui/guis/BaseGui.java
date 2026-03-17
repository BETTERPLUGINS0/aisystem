/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  org.bukkit.event.inventory.InventoryOpenEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.gui.guis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import me.zombie_striker.qav.gui.components.GuiAction;
import me.zombie_striker.qav.gui.components.GuiType;
import me.zombie_striker.qav.gui.components.InteractionModifier;
import me.zombie_striker.qav.gui.components.exception.GuiException;
import me.zombie_striker.qav.gui.components.util.GuiFiller;
import me.zombie_striker.qav.gui.components.util.Legacy;
import me.zombie_striker.qav.gui.components.util.VersionHelper;
import me.zombie_striker.qav.gui.guis.GuiItem;
import me.zombie_striker.qav.gui.guis.GuiListener;
import me.zombie_striker.qav.gui.guis.InteractionModifierListener;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseGui
implements InventoryHolder {
    private static final Plugin plugin = JavaPlugin.getProvidingPlugin(BaseGui.class);
    private static Method GET_SCHEDULER_METHOD = null;
    private static Method EXECUTE_METHOD = null;
    private final GuiFiller filler = new GuiFiller(this);
    private final Map<Integer, GuiItem> guiItems;
    private final Map<Integer, GuiAction<InventoryClickEvent>> slotActions;
    private final Set<InteractionModifier> interactionModifiers;
    private Inventory inventory;
    private String title;
    private int rows = 1;
    private GuiType guiType = GuiType.CHEST;
    private GuiAction<InventoryClickEvent> defaultClickAction;
    private GuiAction<InventoryClickEvent> defaultTopClickAction;
    private GuiAction<InventoryClickEvent> playerInventoryAction;
    private GuiAction<InventoryDragEvent> dragAction;
    private GuiAction<InventoryCloseEvent> closeGuiAction;
    private GuiAction<InventoryOpenEvent> openGuiAction;
    private GuiAction<InventoryClickEvent> outsideClickAction;
    private boolean updating;
    private boolean runCloseAction = true;
    private boolean runOpenAction = true;

    public BaseGui(int n, @NotNull String string, @NotNull Set<InteractionModifier> set) {
        int n2 = n;
        if (n < 1 || n > 6) {
            n2 = 1;
        }
        this.rows = n2;
        this.interactionModifiers = this.safeCopyOf(set);
        this.title = string;
        int n3 = this.rows * 9;
        this.inventory = Bukkit.createInventory((InventoryHolder)this, (int)n3, (String)string);
        this.slotActions = new LinkedHashMap<Integer, GuiAction<InventoryClickEvent>>(n3);
        this.guiItems = new LinkedHashMap<Integer, GuiItem>(n3);
    }

    public BaseGui(@NotNull GuiType guiType, @NotNull String string, @NotNull Set<InteractionModifier> set) {
        this.guiType = guiType;
        this.interactionModifiers = this.safeCopyOf(set);
        this.title = string;
        int n = guiType.getLimit();
        this.inventory = Bukkit.createInventory((InventoryHolder)this, (InventoryType)guiType.getInventoryType(), (String)string);
        this.slotActions = new LinkedHashMap<Integer, GuiAction<InventoryClickEvent>>(n);
        this.guiItems = new LinkedHashMap<Integer, GuiItem>(n);
    }

    @Deprecated
    public BaseGui(int n, @NotNull String string) {
        int n2 = n;
        if (n < 1 || n > 6) {
            n2 = 1;
        }
        this.rows = n2;
        this.interactionModifiers = EnumSet.noneOf(InteractionModifier.class);
        this.title = string;
        this.inventory = Bukkit.createInventory((InventoryHolder)this, (int)(this.rows * 9), (String)string);
        this.slotActions = new LinkedHashMap<Integer, GuiAction<InventoryClickEvent>>();
        this.guiItems = new LinkedHashMap<Integer, GuiItem>();
    }

    @Deprecated
    public BaseGui(@NotNull GuiType guiType, @NotNull String string) {
        this.guiType = guiType;
        this.interactionModifiers = EnumSet.noneOf(InteractionModifier.class);
        this.title = string;
        this.inventory = Bukkit.createInventory((InventoryHolder)this, (InventoryType)this.guiType.getInventoryType(), (String)string);
        this.slotActions = new LinkedHashMap<Integer, GuiAction<InventoryClickEvent>>();
        this.guiItems = new LinkedHashMap<Integer, GuiItem>();
    }

    @NotNull
    private Set<InteractionModifier> safeCopyOf(@NotNull Set<InteractionModifier> set) {
        if (set.isEmpty()) {
            return EnumSet.noneOf(InteractionModifier.class);
        }
        return EnumSet.copyOf(set);
    }

    @Deprecated
    @NotNull
    public String getTitle() {
        return this.title;
    }

    @NotNull
    public Component title() {
        return Legacy.SERIALIZER.deserialize(this.title);
    }

    public void setItem(int n, @NotNull GuiItem guiItem) {
        this.validateSlot(n);
        this.guiItems.put(n, guiItem);
    }

    public void removeItem(@NotNull GuiItem guiItem) {
        Optional<Map.Entry> optional = this.guiItems.entrySet().stream().filter(entry -> ((GuiItem)entry.getValue()).equals(guiItem)).findFirst();
        optional.ifPresent(entry -> {
            this.guiItems.remove(entry.getKey());
            this.inventory.remove(((GuiItem)entry.getValue()).getItemStack());
        });
    }

    public void removeItem(@NotNull ItemStack itemStack) {
        Optional<Map.Entry> optional = this.guiItems.entrySet().stream().filter(entry -> ((GuiItem)entry.getValue()).getItemStack().equals((Object)itemStack)).findFirst();
        optional.ifPresent(entry -> {
            this.guiItems.remove(entry.getKey());
            this.inventory.remove(itemStack);
        });
    }

    public void removeItem(int n) {
        this.validateSlot(n);
        this.guiItems.remove(n);
        this.inventory.setItem(n, null);
    }

    public void removeItem(int n, int n2) {
        this.removeItem(this.getSlotFromRowCol(n, n2));
    }

    public void setItem(@NotNull List<Integer> list, @NotNull GuiItem guiItem) {
        for (int n : list) {
            this.setItem(n, guiItem);
        }
    }

    public void setItem(int n, int n2, @NotNull GuiItem guiItem) {
        this.setItem(this.getSlotFromRowCol(n, n2), guiItem);
    }

    public void addItem(@NotNull GuiItem ... guiItemArray) {
        this.addItem(false, guiItemArray);
    }

    public void addItem(boolean bl, @NotNull GuiItem ... guiItemArray) {
        ArrayList<GuiItem> arrayList = new ArrayList<GuiItem>();
        block0: for (GuiItem guiItem : guiItemArray) {
            for (int i = 0; i < this.rows * 9; ++i) {
                if (this.guiItems.get(i) != null) {
                    if (i != this.rows * 9 - 1) continue;
                    arrayList.add(guiItem);
                    continue;
                }
                this.guiItems.put(i, guiItem);
                continue block0;
            }
        }
        if (!bl || this.rows >= 6 || arrayList.isEmpty() || this.guiType != null && this.guiType != GuiType.CHEST) {
            return;
        }
        ++this.rows;
        this.inventory = Bukkit.createInventory((InventoryHolder)this, (int)(this.rows * 9), (String)this.title);
        this.update();
        this.addItem(true, arrayList.toArray(new GuiItem[0]));
    }

    public void addSlotAction(int n, @Nullable GuiAction<@NotNull InventoryClickEvent> guiAction) {
        this.validateSlot(n);
        this.slotActions.put(n, guiAction);
    }

    public void addSlotAction(int n, int n2, @Nullable GuiAction<@NotNull InventoryClickEvent> guiAction) {
        this.addSlotAction(this.getSlotFromRowCol(n, n2), guiAction);
    }

    @Nullable
    public GuiItem getGuiItem(int n) {
        return this.guiItems.get(n);
    }

    public boolean isUpdating() {
        return this.updating;
    }

    public void setUpdating(boolean bl) {
        this.updating = bl;
    }

    public void open(@NotNull HumanEntity humanEntity) {
        if (humanEntity.isSleeping()) {
            return;
        }
        this.inventory.clear();
        this.populateGui();
        humanEntity.openInventory(this.inventory);
    }

    public void close(@NotNull HumanEntity humanEntity) {
        this.close(humanEntity, true);
    }

    public void close(@NotNull HumanEntity humanEntity, boolean bl) {
        Runnable runnable = () -> {
            this.runCloseAction = bl;
            humanEntity.closeInventory();
            this.runCloseAction = true;
        };
        if (VersionHelper.IS_FOLIA) {
            if (GET_SCHEDULER_METHOD == null || EXECUTE_METHOD == null) {
                throw new GuiException("Could not find Folia Scheduler methods.");
            }
            try {
                EXECUTE_METHOD.invoke(GET_SCHEDULER_METHOD.invoke(humanEntity, new Object[0]), plugin, runnable, null, 2L);
            } catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
                throw new GuiException("Could not invoke Folia task.", reflectiveOperationException);
            }
            return;
        }
        Bukkit.getScheduler().runTaskLater(plugin, runnable, 2L);
    }

    public void update() {
        this.inventory.clear();
        this.populateGui();
        for (HumanEntity humanEntity : new ArrayList(this.inventory.getViewers())) {
            ((Player)humanEntity).updateInventory();
        }
    }

    @Contract(value="_ -> this")
    @NotNull
    public BaseGui updateTitle(@NotNull String string) {
        this.updating = true;
        ArrayList arrayList = new ArrayList(this.inventory.getViewers());
        this.inventory = Bukkit.createInventory((InventoryHolder)this, (int)this.inventory.getSize(), (String)string);
        for (HumanEntity humanEntity : arrayList) {
            this.open(humanEntity);
        }
        this.updating = false;
        this.title = string;
        return this;
    }

    public void updateItem(int n, @NotNull ItemStack itemStack) {
        GuiItem guiItem = this.guiItems.get(n);
        if (guiItem == null) {
            this.updateItem(n, new GuiItem(itemStack));
            return;
        }
        guiItem.setItemStack(itemStack);
        this.updateItem(n, guiItem);
    }

    public void updateItem(int n, int n2, @NotNull ItemStack itemStack) {
        this.updateItem(this.getSlotFromRowCol(n, n2), itemStack);
    }

    public void updateItem(int n, @NotNull GuiItem guiItem) {
        this.guiItems.put(n, guiItem);
        this.inventory.setItem(n, guiItem.getItemStack());
    }

    public void updateItem(int n, int n2, @NotNull GuiItem guiItem) {
        this.updateItem(this.getSlotFromRowCol(n, n2), guiItem);
    }

    @NotNull
    @Contract(value=" -> this")
    public BaseGui disableItemPlace() {
        this.interactionModifiers.add(InteractionModifier.PREVENT_ITEM_PLACE);
        return this;
    }

    @NotNull
    @Contract(value=" -> this")
    public BaseGui disableItemTake() {
        this.interactionModifiers.add(InteractionModifier.PREVENT_ITEM_TAKE);
        return this;
    }

    @NotNull
    @Contract(value=" -> this")
    public BaseGui disableItemSwap() {
        this.interactionModifiers.add(InteractionModifier.PREVENT_ITEM_SWAP);
        return this;
    }

    @NotNull
    @Contract(value=" -> this")
    public BaseGui disableItemDrop() {
        this.interactionModifiers.add(InteractionModifier.PREVENT_ITEM_DROP);
        return this;
    }

    @NotNull
    @Contract(value=" -> this")
    public BaseGui disableOtherActions() {
        this.interactionModifiers.add(InteractionModifier.PREVENT_OTHER_ACTIONS);
        return this;
    }

    @NotNull
    @Contract(value=" -> this")
    public BaseGui disableAllInteractions() {
        this.interactionModifiers.addAll(InteractionModifier.VALUES);
        return this;
    }

    @NotNull
    @Contract(value=" -> this")
    public BaseGui enableItemPlace() {
        this.interactionModifiers.remove((Object)InteractionModifier.PREVENT_ITEM_PLACE);
        return this;
    }

    @NotNull
    @Contract(value=" -> this")
    public BaseGui enableItemTake() {
        this.interactionModifiers.remove((Object)InteractionModifier.PREVENT_ITEM_TAKE);
        return this;
    }

    @NotNull
    @Contract(value=" -> this")
    public BaseGui enableItemSwap() {
        this.interactionModifiers.remove((Object)InteractionModifier.PREVENT_ITEM_SWAP);
        return this;
    }

    @NotNull
    @Contract(value=" -> this")
    public BaseGui enableItemDrop() {
        this.interactionModifiers.remove((Object)InteractionModifier.PREVENT_ITEM_DROP);
        return this;
    }

    @NotNull
    @Contract(value=" -> this")
    public BaseGui enableOtherActions() {
        this.interactionModifiers.remove((Object)InteractionModifier.PREVENT_OTHER_ACTIONS);
        return this;
    }

    @NotNull
    @Contract(value=" -> this")
    public BaseGui enableAllInteractions() {
        this.interactionModifiers.clear();
        return this;
    }

    public boolean allInteractionsDisabled() {
        return this.interactionModifiers.size() == InteractionModifier.VALUES.size();
    }

    public boolean canPlaceItems() {
        return !this.interactionModifiers.contains((Object)InteractionModifier.PREVENT_ITEM_PLACE);
    }

    public boolean canTakeItems() {
        return !this.interactionModifiers.contains((Object)InteractionModifier.PREVENT_ITEM_TAKE);
    }

    public boolean canSwapItems() {
        return !this.interactionModifiers.contains((Object)InteractionModifier.PREVENT_ITEM_SWAP);
    }

    public boolean canDropItems() {
        return !this.interactionModifiers.contains((Object)InteractionModifier.PREVENT_ITEM_DROP);
    }

    public boolean allowsOtherActions() {
        return !this.interactionModifiers.contains((Object)InteractionModifier.PREVENT_OTHER_ACTIONS);
    }

    @NotNull
    public GuiFiller getFiller() {
        return this.filler;
    }

    @NotNull
    public @NotNull Map<@NotNull Integer, @NotNull GuiItem> getGuiItems() {
        return this.guiItems;
    }

    @NotNull
    public Inventory getInventory() {
        return this.inventory;
    }

    public void setInventory(@NotNull Inventory inventory) {
        this.inventory = inventory;
    }

    public int getRows() {
        return this.rows;
    }

    @NotNull
    public GuiType guiType() {
        return this.guiType;
    }

    @Nullable
    GuiAction<InventoryClickEvent> getDefaultClickAction() {
        return this.defaultClickAction;
    }

    public void setDefaultClickAction(@Nullable GuiAction<@NotNull InventoryClickEvent> guiAction) {
        this.defaultClickAction = guiAction;
    }

    @Nullable
    GuiAction<InventoryClickEvent> getDefaultTopClickAction() {
        return this.defaultTopClickAction;
    }

    public void setDefaultTopClickAction(@Nullable GuiAction<@NotNull InventoryClickEvent> guiAction) {
        this.defaultTopClickAction = guiAction;
    }

    @Nullable
    GuiAction<InventoryClickEvent> getPlayerInventoryAction() {
        return this.playerInventoryAction;
    }

    public void setPlayerInventoryAction(@Nullable GuiAction<@NotNull InventoryClickEvent> guiAction) {
        this.playerInventoryAction = guiAction;
    }

    @Nullable
    GuiAction<InventoryDragEvent> getDragAction() {
        return this.dragAction;
    }

    public void setDragAction(@Nullable GuiAction<@NotNull InventoryDragEvent> guiAction) {
        this.dragAction = guiAction;
    }

    @Nullable
    GuiAction<InventoryCloseEvent> getCloseGuiAction() {
        return this.closeGuiAction;
    }

    public void setCloseGuiAction(@Nullable GuiAction<@NotNull InventoryCloseEvent> guiAction) {
        this.closeGuiAction = guiAction;
    }

    @Nullable
    GuiAction<InventoryOpenEvent> getOpenGuiAction() {
        return this.openGuiAction;
    }

    public void setOpenGuiAction(@Nullable GuiAction<@NotNull InventoryOpenEvent> guiAction) {
        this.openGuiAction = guiAction;
    }

    @Nullable
    GuiAction<InventoryClickEvent> getOutsideClickAction() {
        return this.outsideClickAction;
    }

    public void setOutsideClickAction(@Nullable GuiAction<@NotNull InventoryClickEvent> guiAction) {
        this.outsideClickAction = guiAction;
    }

    @Nullable
    GuiAction<InventoryClickEvent> getSlotAction(int n) {
        return this.slotActions.get(n);
    }

    void populateGui() {
        for (Map.Entry<Integer, GuiItem> entry : this.guiItems.entrySet()) {
            this.inventory.setItem(entry.getKey().intValue(), entry.getValue().getItemStack());
        }
    }

    boolean shouldRunCloseAction() {
        return this.runCloseAction;
    }

    boolean shouldRunOpenAction() {
        return this.runOpenAction;
    }

    int getSlotFromRowCol(int n, int n2) {
        return n2 + (n - 1) * 9 - 1;
    }

    private void validateSlot(int n) {
        int n2 = this.guiType.getLimit();
        if (this.guiType == GuiType.CHEST) {
            if (n < 0 || n >= this.rows * n2) {
                this.throwInvalidSlot(n);
            }
            return;
        }
        if (n < 0 || n > n2) {
            this.throwInvalidSlot(n);
        }
    }

    private void throwInvalidSlot(int n) {
        if (this.guiType == GuiType.CHEST) {
            throw new GuiException("Slot " + n + " is not valid for the gui type - " + this.guiType.name() + " and rows - " + this.rows + "!");
        }
        throw new GuiException("Slot " + n + " is not valid for the gui type - " + this.guiType.name() + "!");
    }

    static {
        try {
            GET_SCHEDULER_METHOD = Entity.class.getMethod("getScheduler", new Class[0]);
            Class<?> clazz = Class.forName("io.papermc.paper.threadedregions.scheduler.EntityScheduler");
            EXECUTE_METHOD = clazz.getMethod("execute", Plugin.class, Runnable.class, Runnable.class, Long.TYPE);
        } catch (ClassNotFoundException | NoSuchMethodException reflectiveOperationException) {
            // empty catch block
        }
        Bukkit.getPluginManager().registerEvents((Listener)new GuiListener(), plugin);
        Bukkit.getPluginManager().registerEvents((Listener)new InteractionModifierListener(), plugin);
    }
}

