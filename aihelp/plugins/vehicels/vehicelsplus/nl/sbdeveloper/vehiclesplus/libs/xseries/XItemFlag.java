/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import nl.sbdeveloper.vehiclesplus.libs.xseries.base.XBase;
import nl.sbdeveloper.vehiclesplus.libs.xseries.base.XRegistry;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public enum XItemFlag implements XBase<XItemFlag, ItemFlag>
{
    HIDE_ADDITIONAL_TOOLTIP("HIDE_POTION_EFFECTS"),
    HIDE_ARMOR_TRIM(new String[0]),
    HIDE_ATTRIBUTES(new String[0]),
    HIDE_DESTROYS(new String[0]),
    HIDE_DYE(new String[0]),
    HIDE_ENCHANTS(new String[0]),
    HIDE_PLACED_ON(new String[0]),
    HIDE_STORED_ENCHANTS(new String[0]),
    HIDE_UNBREAKABLE(new String[0]);

    public static final XRegistry<XItemFlag, ItemFlag> REGISTRY;
    private static final ItemFlag[] BUKKIT_VALUES;
    private final ItemFlag itemFlag;

    private XItemFlag(String ... stringArray) {
        this.itemFlag = (ItemFlag)Data.REGISTRY.stdEnum(this, stringArray);
    }

    public static XItemFlag of(ItemFlag itemFlag) {
        return REGISTRY.getByBukkitForm(itemFlag);
    }

    public static Optional<XItemFlag> of(String string) {
        return REGISTRY.getByName(string);
    }

    @NotNull
    public static @Unmodifiable Collection<XItemFlag> getValues() {
        return REGISTRY.getValues();
    }

    @Override
    public String[] getNames() {
        return new String[]{this.name()};
    }

    @Override
    @Nullable
    public ItemFlag get() {
        return this.itemFlag;
    }

    public void set(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(new ItemFlag[]{this.itemFlag});
        itemStack.setItemMeta(itemMeta);
    }

    public void set(ItemMeta itemMeta) {
        itemMeta.addItemFlags(new ItemFlag[]{this.itemFlag});
    }

    public void removeFrom(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        this.removeFrom(itemMeta);
        itemStack.setItemMeta(itemMeta);
    }

    public void removeFrom(ItemMeta itemMeta) {
        itemMeta.removeItemFlags(new ItemFlag[]{this.itemFlag});
    }

    public Set<XItemFlag> getFlags(ItemStack itemStack) {
        return this.getFlags(itemStack.getItemMeta());
    }

    public Set<XItemFlag> getFlags(ItemMeta itemMeta) {
        return itemMeta.getItemFlags().stream().map(XItemFlag::of).collect(Collectors.toSet());
    }

    public boolean has(ItemStack itemStack) {
        return this.has(itemStack.getItemMeta());
    }

    public boolean has(ItemMeta itemMeta) {
        return itemMeta.getItemFlags().contains(this.itemFlag);
    }

    public static void hideEverything(ItemMeta itemMeta) {
        itemMeta.addItemFlags(BUKKIT_VALUES);
    }

    static {
        REGISTRY = Data.REGISTRY;
        BUKKIT_VALUES = ItemFlag.values();
    }

    private static final class Data {
        private static final XRegistry<XItemFlag, ItemFlag> REGISTRY = new XRegistry<XItemFlag, ItemFlag>(ItemFlag.class, XItemFlag.class, XItemFlag[]::new);

        private Data() {
        }
    }
}

