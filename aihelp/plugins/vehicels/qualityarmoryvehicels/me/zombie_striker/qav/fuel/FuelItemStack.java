/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.zombie_striker.qav.fuel;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FuelItemStack {
    private static final HashMap<FuelItemStack, Integer> fuels = new HashMap();
    private final String name;
    private final Material material;
    private final int data;
    private final List<String> lore;
    private final boolean shouldBeInShop;
    private final int cost;
    private static final String OLD_BAD = "%name=%";
    private static final String newSplit = "%";

    public static Set<FuelItemStack> getFuels() {
        return fuels.keySet();
    }

    public FuelItemStack(String string, Material material, int n, List<String> list) {
        this(string, material, n, list, false, 0);
    }

    public FuelItemStack(String string, Material material, int n, List<String> list, boolean bl, int n2) {
        this.name = string != null ? ChatColor.translateAlternateColorCodes((char)'&', (String)string) : null;
        this.material = material;
        this.data = n;
        this.lore = list;
        this.shouldBeInShop = bl;
        this.cost = n2;
    }

    public boolean isFuel(ItemStack itemStack) {
        if (itemStack.getType() != this.material) {
            return false;
        }
        try {
            if (this.data != 0 && !itemStack.getItemMeta().hasCustomModelData() || itemStack.getItemMeta().hasCustomModelData() && itemStack.getItemMeta().getCustomModelData() != this.data) {
                return false;
            }
        } catch (Error | Exception throwable) {
            // empty catch block
        }
        return !this.hasCustomName() || itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equals(this.name);
    }

    public int getCost() {
        return this.cost;
    }

    public boolean isAllowedInShop() {
        return this.shouldBeInShop;
    }

    public Material getMaterial() {
        return this.material;
    }

    public int getData() {
        return this.data;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public String getDisplayname() {
        return this.name;
    }

    public boolean hasCustomName() {
        return this.name != null;
    }

    public boolean hasLore() {
        return this.lore != null;
    }

    public static int getFuelForItem(ItemStack itemStack) {
        for (Map.Entry<FuelItemStack, Integer> entry : fuels.entrySet()) {
            if (!entry.getKey().isFuel(itemStack)) continue;
            return entry.getValue();
        }
        return 0;
    }

    public static FuelItemStack getFuelItemInstance(ItemStack itemStack) {
        for (Map.Entry<FuelItemStack, Integer> entry : fuels.entrySet()) {
            if (!entry.getKey().isFuel(itemStack)) continue;
            return entry.getKey();
        }
        return null;
    }

    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(this.material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(this.lore.stream().map(string -> ChatColor.translateAlternateColorCodes((char)'&', (String)string)).collect(Collectors.toList()));
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)this.getDisplayname()));
        try {
            itemMeta.setCustomModelData(Integer.valueOf(this.data));
        } catch (Error | Exception throwable) {
            // empty catch block
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static void registerNewFuel(FuelItemStack fuelItemStack, Integer n) {
        fuels.put(fuelItemStack, n);
    }

    public static void loadFuels(File file) {
        fuels.clear();
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        String string = newSplit;
        if (Main.ENABLE_FILE_CREATION) {
            FuelItemStack.registerNewFuelToConfig(null, Material.COAL, (short)0, null, 500, file);
            FuelItemStack.registerNewFuelToConfig(null, Material.COAL_BLOCK, (short)0, null, 4500, file);
            FuelItemStack.registerNewFuelToConfig(null, Material.BLAZE_POWDER, (short)0, null, 500, file);
            FuelItemStack.registerNewFuelToConfig(null, Material.BLAZE_ROD, (short)0, null, 1000, file);
            FuelItemStack.registerNewFuelToConfig(null, Material.LAVA_BUCKET, (short)0, null, 10000, file);
            FuelItemStack.registerNewFuelToConfig("&6Fuel Canister", XReflection.supports(14) ? Material.RABBIT_HIDE : Material.DIAMOND_AXE, (short)38, Collections.singletonList("&7Fuel for: 500 seconds"), 10000, file, true, 50);
        }
        for (String string2 : yamlConfiguration.getKeys(false)) {
            if (string2.contains(OLD_BAD)) {
                string = OLD_BAD;
            }
            String[] stringArray = string2.split(string);
            Material material = Material.getMaterial((String)stringArray[0]);
            String string3 = stringArray.length > 1 && stringArray[1].length() > 0 && !stringArray[1].equals("null") ? stringArray[1] : null;
            short s = (short)(yamlConfiguration.contains(string2 + ".data") ? yamlConfiguration.getInt(string2 + ".data") : 0);
            List list = yamlConfiguration.contains(string2 + ".lore") ? yamlConfiguration.getStringList(string2 + ".lore") : null;
            short s2 = (short)(yamlConfiguration.contains(string2 + ".fuelevel") ? yamlConfiguration.getInt(string2 + ".fuelevel") : 100);
            boolean bl = yamlConfiguration.contains(string2 + ".shouldBeInShop") && yamlConfiguration.getBoolean(string2 + ".shouldBeInShop");
            int n = yamlConfiguration.contains(string2 + ".cost") ? yamlConfiguration.getInt(string2 + ".cost") : 0;
            FuelItemStack fuelItemStack = new FuelItemStack(string3, material, s, list, bl, n);
            FuelItemStack.registerNewFuel(fuelItemStack, Integer.valueOf(s2));
        }
    }

    public static void registerNewFuelToConfig(String string, Material material, short s, List<String> list, int n, File file) {
        FuelItemStack.registerNewFuelToConfig(string, material, s, list, n, file, false, 0);
    }

    public static void registerNewFuelToConfig(String string, Material material, short s, List<String> list, int n, File file, boolean bl, int n2) {
        FuelItemStack fuelItemStack = new FuelItemStack(string, material, s, list, bl, n2);
        FuelItemStack.registerNewFuel(fuelItemStack, n);
        String string2 = newSplit;
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        yamlConfiguration.set(material.name() + string2 + string + ".data", (Object)s);
        if (list != null) {
            yamlConfiguration.set(material.name() + string2 + string + ".lore", list);
        }
        yamlConfiguration.set(material.name() + string2 + string + ".fuelevel", (Object)n);
        yamlConfiguration.set(material.name() + string2 + string + ".shouldBeInShop", (Object)bl);
        yamlConfiguration.set(material.name() + string2 + string + ".cost", (Object)n2);
        try {
            yamlConfiguration.save(file);
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static void updateFuel(VehicleEntity vehicleEntity) {
        if (vehicleEntity.getFuel() <= 0) {
            for (int i = 0; i < vehicleEntity.getFuels().getSize(); ++i) {
                ItemStack itemStack;
                int n;
                ItemStack itemStack2 = vehicleEntity.getFuels().getItem(i);
                if (itemStack2 == null || (n = FuelItemStack.getFuelForItem(itemStack = itemStack2.clone())) <= 0) continue;
                if (itemStack.getAmount() > 1) {
                    itemStack.setAmount(itemStack.getAmount() - 1);
                    vehicleEntity.getFuels().setItem(i, itemStack);
                } else if (itemStack.getAmount() <= 1) {
                    vehicleEntity.getFuels().setItem(i, null);
                }
                vehicleEntity.setFuel(vehicleEntity.getFuel() + n);
                break;
            }
        }
    }

    public static void addNewItem(File file, ItemStack itemStack, int n) {
        FuelItemStack.addNewItem(file, itemStack, n, false, 0);
    }

    public static void addNewItem(File file, ItemStack itemStack, int n, boolean bl, int n2) {
        Material material = itemStack.getType();
        short s = itemStack.getDurability();
        String string = newSplit;
        String string2 = itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : null;
        List list = itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore() ? itemStack.getItemMeta().getLore() : null;
        FuelItemStack fuelItemStack = new FuelItemStack(string2, material, s, list, bl, n2);
        FuelItemStack.registerNewFuel(fuelItemStack, n);
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        yamlConfiguration.set(material.name() + string + string2 + ".data", (Object)s);
        if (list != null) {
            yamlConfiguration.set(material.name() + string + string2 + ".lore", (Object)list);
        }
        yamlConfiguration.set(material.name() + string + string2 + ".fuelevel", (Object)n);
        try {
            yamlConfiguration.save(file);
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }
}

