/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.fuel;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

public class RepairItemStack {
    private String name;
    private Material material;
    private List<String> lore;
    private int data;
    private boolean shouldBeInShop;
    private int cost;

    public boolean isItem(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        if (itemStack.getType() != this.material) {
            return false;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return false;
        }
        if (this.tryData(itemMeta)) {
            return true;
        }
        if (this.name == null && !itemMeta.hasDisplayName()) {
            itemMeta.getDisplayName();
        }
        return this.name != null && itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals(ChatColor.translateAlternateColorCodes((char)'&', (String)this.name));
    }

    private boolean tryData(ItemMeta itemMeta) {
        try {
            if (!itemMeta.hasCustomModelData()) {
                return false;
            }
            if (this.data == 0) {
                return false;
            }
            return itemMeta.getCustomModelData() == this.data;
        } catch (Error | Exception throwable) {
            return false;
        }
    }

    private RepairItemStack() {
    }

    public static RepairItemStack loadFromFile() {
        File file = Main.repairYML;
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        RepairItemStack.update((FileConfiguration)yamlConfiguration, "name", "&6Repair Vehicle");
        RepairItemStack.update((FileConfiguration)yamlConfiguration, "material", XReflection.supports(14) ? Material.RABBIT_HIDE.name() : Material.DIAMOND_AXE.name());
        RepairItemStack.update((FileConfiguration)yamlConfiguration, "lore", Collections.singletonList("&7Use this item to repair your vehicle"));
        RepairItemStack.update((FileConfiguration)yamlConfiguration, "data", 0);
        RepairItemStack.update((FileConfiguration)yamlConfiguration, "shouldBeInShop", true);
        RepairItemStack.update((FileConfiguration)yamlConfiguration, "cost", 50);
        yamlConfiguration.save(file);
        return new RepairItemStack().setMaterial(Material.getMaterial((String)yamlConfiguration.getString("material", ""))).setName(yamlConfiguration.getString("name")).setLore(yamlConfiguration.getStringList("lore")).setData(yamlConfiguration.getInt("data")).setShouldBeInShop(yamlConfiguration.getBoolean("shouldBeInShop")).setCost(yamlConfiguration.getInt("cost"));
    }

    public void reload() {
        File file = Main.repairYML;
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.setName(yamlConfiguration.getString("name")).setMaterial(Material.getMaterial((String)yamlConfiguration.getString("material", ""))).setLore(yamlConfiguration.getStringList("lore")).setData(yamlConfiguration.getInt("data")).setShouldBeInShop(yamlConfiguration.getBoolean("shouldBeInShop")).setCost(yamlConfiguration.getInt("cost"));
    }

    private static void update(FileConfiguration fileConfiguration, String string, Object object) {
        if (!fileConfiguration.contains(string)) {
            fileConfiguration.set(string, object);
        }
    }

    public String getName() {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)this.name);
    }

    public RepairItemStack setName(String string) {
        this.name = string;
        return this;
    }

    public Material getMaterial() {
        return this.material;
    }

    public RepairItemStack setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public RepairItemStack setLore(List<String> list) {
        this.lore = list;
        return this;
    }

    public int getData() {
        return this.data;
    }

    public RepairItemStack setData(int n) {
        this.data = n;
        return this;
    }

    public boolean shouldBeInShop() {
        return this.shouldBeInShop;
    }

    public RepairItemStack setShouldBeInShop(boolean bl) {
        this.shouldBeInShop = bl;
        return this;
    }

    public int getCost() {
        return this.cost;
    }

    public RepairItemStack setCost(int n) {
        this.cost = n;
        return this;
    }

    public ItemStack asItem() {
        ItemStack itemStack = new ItemStack(this.material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return null;
        }
        if (this.data != 0) {
            itemMeta.setCustomModelData(Integer.valueOf(this.data));
        }
        if (this.name != null) {
            itemMeta.setDisplayName(this.getName());
        }
        if (this.lore != null && !this.lore.isEmpty()) {
            itemMeta.setLore(this.lore.stream().map(MessagesConfig::colorize).collect(Collectors.toList()));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}

