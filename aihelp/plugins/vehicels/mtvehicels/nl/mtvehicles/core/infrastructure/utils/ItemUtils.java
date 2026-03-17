/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.RandomStringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package nl.mtvehicles.core.infrastructure.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.VersionSpecific;
import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import nl.mtvehicles.core.infrastructure.utils.ItemFactory;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemUtils {
    public static HashMap<String, Boolean> edit = new HashMap();

    public static Material getMaterial(String string) {
        try {
            Material material = Material.matchMaterial((String)string);
            assert (material != null);
            return material;
        } catch (Exception e1) {
            try {
                Material material = Material.matchMaterial((String)("LEGACY_" + string));
                assert (material != null);
                return material;
            } catch (Exception e2) {
                try {
                    Material material = Material.matchMaterial((String)string, (boolean)true);
                    assert (material != null);
                    return material;
                } catch (Exception e3) {
                    Main.logSevere("An error occurred while trying to obtain material from string '" + string + "'. This might happen after meddling with the config files or it could be a plugin issue.");
                    return null;
                }
            }
        }
    }

    public static ItemStack getMenuVehicle(@NotNull Material material, int durability, String name) {
        if (!material.isItem()) {
            return null;
        }
        ItemStack vehicle = new ItemFactory(material).setName(TextUtils.colorize("&6" + name)).setDurability(durability).setUnbreakable(true).setLore("&a").toItemStack();
        return vehicle;
    }

    public static ItemStack getVehicleItem(@NotNull Material material, int durability, String name) {
        if (!material.isItem()) {
            return null;
        }
        String licensePlate = ItemUtils.generateLicencePlate();
        ItemStack vehicle = new ItemFactory(material).setDurability(durability).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", licensePlate).setNBT("mtvehicles.naam", name).setLore("&a", "&a" + licensePlate, "&a").setUnbreakable(true).toItemStack();
        return vehicle;
    }

    public static ItemStack getVehicleItem(@NotNull Material material, int durability, String name, String nbtKey, @Nullable Object nbtValue) {
        if (!material.isItem()) {
            return null;
        }
        if (nbtValue == null) {
            return ItemUtils.getVehicleItem(material, durability, name);
        }
        String licensePlate = ItemUtils.generateLicencePlate();
        ItemStack vehicle = new ItemFactory(material).setDurability(durability).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", licensePlate).setNBT("mtvehicles.naam", name).setNBT(nbtKey, nbtValue.toString()).setLore("&a", "&a" + licensePlate, "&a").setUnbreakable(true).toItemStack();
        return vehicle;
    }

    public static ItemStack getVehicleItem(@NotNull Material material, int durability, @Nullable Boolean glowing, String name, String licensePlate) {
        if (!material.isItem()) {
            return null;
        }
        if (glowing == null) {
            glowing = false;
        }
        ItemStack vehicle = new ItemFactory(material).setDurability(durability).setName(TextUtils.colorize("&6" + name)).setGlowing(glowing).setNBT("mtvehicles.kenteken", licensePlate).setNBT("mtvehicles.naam", name).setLore("&a", "&a" + licensePlate, "&a").setUnbreakable(true).toItemStack();
        return vehicle;
    }

    public static ItemStack getVehicleItem(String licensePlate) {
        return ItemUtils.getVehicleItem(Objects.requireNonNull(ItemUtils.getMaterial(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString())), (int)((Integer)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_DAMAGE)), (boolean)((Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.IS_GLOWING)), ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NAME).toString(), licensePlate);
    }

    public static ItemStack getVehicleItem(String licensePlate, boolean nbt) {
        if (!nbt) {
            return ItemUtils.getVehicleItem(licensePlate);
        }
        return ItemUtils.getVehicleItem(Objects.requireNonNull(ItemUtils.getMaterial(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString())), (Integer)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_DAMAGE), (boolean)((Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.IS_GLOWING)), ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NAME).toString(), licensePlate, "mtcustom", ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NBT_VALUE));
    }

    public static ItemStack getVehicleItem(@NotNull Material material, int durability, Boolean glowing, String name, String licensePlate, String nbtKey, @Nullable Object nbtValue) {
        if (!material.isItem()) {
            return null;
        }
        if (nbtValue == null) {
            return ItemUtils.getVehicleItem(material, durability, glowing, name, licensePlate);
        }
        if (glowing == null) {
            glowing = false;
        }
        ItemStack vehicle = new ItemFactory(material).setDurability(durability).setName(TextUtils.colorize("&6" + name)).setGlowing(glowing).setNBT("mtvehicles.kenteken", licensePlate).setNBT("mtvehicles.naam", name).setNBT(nbtKey, nbtValue.toString()).setLore("&a", "&a" + licensePlate, "&a").setUnbreakable(true).toItemStack();
        return vehicle;
    }

    private static String generateLicencePlate() {
        String plate = String.format("%s-%s-%s", RandomStringUtils.random((int)2, (boolean)true, (boolean)false), RandomStringUtils.random((int)2, (boolean)true, (boolean)false), RandomStringUtils.random((int)2, (boolean)true, (boolean)false));
        return plate.toUpperCase();
    }

    public static ItemStack getMenuItem(String materialName, String materialLegacyName, short legacyData, int amount, String name, List<String> lores) {
        ItemStack item;
        try {
            item = new ItemStack(ItemUtils.getMaterial(materialName), amount);
        } catch (Exception e1) {
            try {
                item = new ItemStack(ItemUtils.getMaterial(materialLegacyName), amount);
                item.setDurability(legacyData);
            } catch (Exception e2) {
                Main.logSevere("An error occurred - could not get item neither from " + materialName + " nor from " + materialLegacyName + ". This is most likely a plugin issue, contact us at discord.gg/vehicle!");
                return null;
            }
        }
        return new ItemFactory(item).setName(name).setLore(lores).toItemStack();
    }

    public static ItemStack getMenuItem(String materialName, String materialLegacyName, short legacyData, int amount, String name, String ... lores) {
        return ItemUtils.getMenuItem(materialName, materialLegacyName, legacyData, amount, name, Arrays.asList(lores));
    }

    public static ItemStack getMenuItem(@NotNull Material material, int amount, String name, List<String> lores) {
        ItemStack item = new ItemFactory(material, amount).setName(name).setLore(lores).toItemStack();
        return item;
    }

    public static ItemStack getMenuItem(@NotNull Material material, int amount, String name, String ... lores) {
        return ItemUtils.getMenuItem(material, amount, name, Arrays.asList(lores));
    }

    public static ItemStack getMenuGlowingItem(@NotNull Material material, int amount, String name, List<String> lores) {
        ItemStack item = new ItemFactory(material, amount).setName(name).setGlowing(true).setLore(lores).toItemStack();
        return item;
    }

    public static ItemStack getMenuGlowingItem(@NotNull Material material, int amount, String name, String ... lores) {
        return ItemUtils.getMenuGlowingItem(material, amount, name, Arrays.asList(lores));
    }

    public static ItemStack getMenuItem(@NotNull Material material, int amount, int durability, boolean unbreakable, String name, List<String> lores) {
        ItemStack item = new ItemFactory(material, amount).setName(name).setDurability(durability).setUnbreakable(unbreakable).setLore(lores).toItemStack();
        return item;
    }

    public static ItemStack getMenuItem(@NotNull Material material, int amount, int durability, String name, List<String> lores) {
        return ItemUtils.getMenuItem(material, amount, durability, false, name, lores);
    }

    public static ItemStack getMenuItem(@NotNull Material material, int amount, int durability, String name, String ... lores) {
        return ItemUtils.getMenuItem(material, amount, durability, false, name, Arrays.asList(lores));
    }

    @VersionSpecific
    public static Material getStainedGlassPane() {
        if (VersionModule.getServerVersion().is1_12()) {
            return Material.matchMaterial((String)"STAINED_GLASS_PANE");
        }
        return Material.matchMaterial((String)"WHITE_STAINED_GLASS_PANE");
    }

    public static ItemStack getMenuRidersItem(String licensePlate) {
        ArrayList<String> lore = new ArrayList<String>();
        MessagesConfig msg = ConfigModule.messagesConfig;
        Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);
        if (vehicle == null) {
            return null;
        }
        if (vehicle.getRiders().size() == 0) {
            lore.add(msg.getMessage(Message.VEHICLE_INFO_RIDERS_NONE));
        } else {
            lore.add(String.format(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_RIDERS), vehicle.getRiders().size(), ""));
            for (String rider : vehicle.getRiders()) {
                lore.add(TextUtils.colorize("&7- &e" + Bukkit.getOfflinePlayer((UUID)UUID.fromString(rider)).getName()));
            }
        }
        return ItemUtils.getMenuItem(Material.PAPER, 1, "&6" + msg.getMessage(Message.RIDERS), lore);
    }

    public static ItemStack getMenuMembersItem(String licensePlate) {
        ArrayList<String> lore = new ArrayList<String>();
        MessagesConfig msg = ConfigModule.messagesConfig;
        Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);
        if (vehicle == null) {
            return null;
        }
        if (vehicle.getMembers().size() == 0) {
            lore.add(msg.getMessage(Message.VEHICLE_INFO_MEMBERS_NONE));
        } else {
            lore.add(String.format(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_MEMBERS), vehicle.getMembers().size(), ""));
            for (String member : vehicle.getMembers()) {
                lore.add(TextUtils.colorize("&7- &e" + Bukkit.getOfflinePlayer((UUID)UUID.fromString(member)).getName()));
            }
        }
        return ItemUtils.getMenuItem(Material.PAPER, 1, "&6" + msg.getMessage(Message.MEMBERS), lore);
    }

    public static ItemStack getMenuCustomItem(@NotNull Material material, String name, int durability, List<String> lore) {
        if (!material.isItem()) {
            return null;
        }
        ItemStack vehicle = new ItemFactory(material).setDurability(durability).setName(name).setLore(lore).setUnbreakable(true).toItemStack();
        return vehicle;
    }

    public static ItemStack getMenuCustomItem(@NotNull Material material, String name, int durability, String ... lore) {
        return ItemUtils.getMenuCustomItem(material, name, durability, Arrays.asList(lore));
    }

    public static ItemStack getMenuCustomItem(@NotNull Material material, String nbtKey, @Nullable Object nbtValue, String name, int durability, List<String> lore) {
        if (!material.isItem()) {
            return null;
        }
        if (nbtValue == null) {
            return ItemUtils.getMenuCustomItem(material, name, durability, lore);
        }
        ItemStack vehicle = new ItemFactory(material).setDurability(durability).setName(name).setNBT(nbtKey, nbtValue.toString()).setLore(lore).setUnbreakable(true).toItemStack();
        return vehicle;
    }

    public static ItemStack getMenuCustomItem(@NotNull Material material, String nbtKey, @Nullable Object nbtValue, String name, int durability, String ... lore) {
        return ItemUtils.getMenuCustomItem(material, nbtKey, nbtValue, name, durability, Arrays.asList(lore));
    }

    public static ItemStack createVoucher(String carUUID) {
        MessagesConfig msg = ConfigModule.messagesConfig;
        ItemStack voucher = new ItemFactory(Material.PAPER).setName(TextUtils.colorize(VehicleUtils.getItem(carUUID).getItemMeta().getDisplayName() + " Voucher")).setLore(TextUtils.colorize("&8&m                                    "), TextUtils.colorize(msg.getMessage(Message.VOUCHER_DESCRIPTION)), TextUtils.colorize("&2&l"), TextUtils.colorize(msg.getMessage(Message.VOUCHER_VALIDITY)), TextUtils.colorize("&2> Permanent"), TextUtils.colorize("&8&m                                    ")).setNBT("mtvehicles.item", carUUID).toItemStack();
        return voucher;
    }
}

