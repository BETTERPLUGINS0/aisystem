/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatMessageType
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.block.data.type.Bed
 *  org.bukkit.block.data.type.Bed$Part
 *  org.bukkit.block.data.type.Candle
 *  org.bukkit.block.data.type.TurtleEgg
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Monster
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.Damageable
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.Vector
 *  org.bukkit.util.io.BukkitObjectInputStream
 *  org.bukkit.util.io.BukkitObjectOutputStream
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.advancedplugins.as.impl.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.invoke.CallSite;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.advancedplugins.as.impl.utils.ColorUtils;
import net.advancedplugins.as.impl.utils.CropUtils;
import net.advancedplugins.as.impl.utils.MathUtils;
import net.advancedplugins.as.impl.utils.Pair;
import net.advancedplugins.as.impl.utils.Registry;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import net.advancedplugins.as.impl.utils.TryCatchMethodShort;
import net.advancedplugins.as.impl.utils.VanillaEnchants;
import net.advancedplugins.as.impl.utils.annotations.ConfigKey;
import net.advancedplugins.as.impl.utils.evalex.Expression;
import net.advancedplugins.as.impl.utils.nbt.NBTapi;
import net.advancedplugins.as.impl.utils.nbt.backend.ClassWrapper;
import net.advancedplugins.as.impl.utils.nbt.backend.ReflectionMethod;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import net.advancedplugins.as.impl.utils.text.Replace;
import net.advancedplugins.as.impl.utils.text.Text;
import net.advancedplugins.as.impl.utils.trycatch.TryCatchUtil;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.UniformIntegerDistribution;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Candle;
import org.bukkit.block.data.type.TurtleEgg;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ASManager {
    private static final HashSet<String> silkOnly = new HashSet<String>(Arrays.asList("LEAVE", "LEAVES", "MUSHROOM_STEM", "TURTLE_EGG", "CORAL"));
    private static final List<Integer> validSizes = new ArrayList<Integer>(Arrays.asList(9, 18, 27, 36, 45, 54));
    private static final ImmutableList<String> vegetationBlockNames = ((ImmutableList.Builder)ImmutableList.builder().addAll(Arrays.asList("GRASS", "TALL_GRASS", "FERN", "LARGE_FERN", "SEAGRASS", "TALL_SEAGRASS", "DANDELION", "POPPY", "BLUE_ORCHID", "ALLIUM", "AZURE_BLUET", "RED_TULIP", "ORANGE_TULIP", "WHITE_TULIP", "PINK_TULIP", "OXEYE_DAISY", "CORNFLOWER", "LILY_OF_THE_VALLEY", "WITHER_ROSE", "SUNFLOWER", "LILAC", "ROSE_BUSH", "PEONY"))).build();
    public static boolean debug = false;
    private static JavaPlugin instance;
    private static HashMap<Integer, String> damages;
    private static HashMap<String, String> newMaterials;

    public static void setInstance(JavaPlugin javaPlugin) {
        instance = javaPlugin;
        try {
            File file = new File(javaPlugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            try (ZipFile zipFile = new ZipFile(file);){
                String string = Registry.class.getName().replace('.', '/') + ".class";
                String string2 = ASManager.getInstance().getDescription().getMain().replace('.', '/') + ".class";
                String string3 = "plugin.yml";
                Date date = new Date(zipFile.getEntry(string).getTime());
                Date date2 = new Date(zipFile.getEntry(string2).getTime());
                Date date3 = new Date(zipFile.getEntry(string3).getTime());
                return;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    public static int hexToDecimal(String string) {
        return Integer.parseInt(string, 16);
    }

    @Contract(value="null, _ -> fail")
    public static void notNull(Object object, String string) {
        if (object == null) {
            throw new IllegalArgumentException(string.concat(" cannot be null."));
        }
    }

    @Contract(value="!null, _ -> fail")
    public static void isNull(Object object, String string) {
        if (object != null) {
            throw new IllegalArgumentException(string);
        }
    }

    @Nullable
    public static Player getPlayerInsensitive(@NotNull String string) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getName().equalsIgnoreCase(string)) continue;
            return player;
        }
        return null;
    }

    public static int similarityPercentage(String string, String string2) {
        if (string == null || string2 == null || string.isEmpty() || string2.isEmpty()) {
            return 0;
        }
        int n = Math.max(string.length(), string2.length());
        int n2 = StringUtils.getLevenshteinDistance(string, string2);
        return (int)((1.0 - (double)n2 / (double)n) * 100.0);
    }

    public static int getInvSize(int n) {
        MathUtils.clamp(n, 9, 54);
        if (n % 9 != 0) {
            n = MathUtils.getClosestInt(n, validSizes);
        }
        return n;
    }

    public static boolean isSpawner(Material material) {
        return material.name().endsWith("SPAWNER");
    }

    public static boolean isSpawner(Block block) {
        if (block == null || block.getType() == null) {
            return false;
        }
        return ASManager.isSpawner(block.getType());
    }

    public static boolean doesBlockFaceMatch(Block block, String string, BlockFace ... blockFaceArray) {
        for (BlockFace blockFace : blockFaceArray) {
            Material material = block.getRelative(blockFace).getType();
            if (ASManager.isAir(material) || !material.name().endsWith(string)) continue;
            return true;
        }
        return false;
    }

    public static Block getOtherHalfOfBed(Block block) {
        if (!block.getType().name().endsWith("_BED")) {
            return null;
        }
        Bed bed = (Bed)block.getBlockData();
        Block block2 = bed.getPart() == Bed.Part.HEAD ? block.getRelative(bed.getFacing().getOppositeFace()) : block.getRelative(bed.getFacing());
        if (!(block2.getBlockData() instanceof Bed)) {
            return null;
        }
        return block2;
    }

    public static boolean isTool(Material material) {
        if (material == null) {
            return false;
        }
        String string = material.name();
        return string.endsWith("_AXE") || string.endsWith("_PICKAXE") || string.endsWith("_SWORD") || string.endsWith("_SHOVEL") || string.endsWith("_SPADE") || string.endsWith("_HOE") || string.endsWith("SHEARS");
    }

    public static boolean isExcessVelocity(Vector vector) {
        return vector.getX() > 10.0 || vector.getX() < -10.0 || vector.getY() > 10.0 || vector.getY() < -10.0 || vector.getZ() > 10.0 || vector.getZ() < -10.0;
    }

    public static List<Block> getBlocksFlat(Block block, int n) {
        if (n < 1) {
            return n == 0 ? Collections.singletonList(block) : Collections.emptyList();
        }
        int n2 = (n << 1) + 1;
        ArrayList<Block> arrayList = new ArrayList<Block>(n2 * n2 * n2);
        for (int i = -n; i <= n; ++i) {
            for (int j = -n; j <= n; ++j) {
                arrayList.add(block.getRelative(i, 0, j));
            }
        }
        return arrayList;
    }

    public static int getAmount(Player player, Material material) {
        int n = 0;
        for (ItemStack itemStack : player.getInventory().getStorageContents()) {
            if (itemStack == null || itemStack.getType() != material) continue;
            n += itemStack.getAmount();
        }
        return n;
    }

    public static boolean hasAmount(Player player, Material material, int n) {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null || itemStack.getType() != material || (n -= itemStack.getAmount()) > 0) continue;
            return true;
        }
        return player.getInventory().getItem(EquipmentSlot.OFF_HAND) != null && player.getInventory().getItem(EquipmentSlot.OFF_HAND).getType() == material && (n -= player.getInventory().getItem(EquipmentSlot.OFF_HAND).getAmount()) <= 0;
    }

    public static boolean removeItems(Inventory inventory, Material material, int n) {
        PlayerInventory playerInventory;
        ItemStack itemStack;
        if (material == null || inventory == null) {
            return false;
        }
        if (n <= 0) {
            return false;
        }
        if (n == Integer.MAX_VALUE) {
            inventory.remove(material);
            return true;
        }
        int n2 = n;
        if (inventory instanceof PlayerInventory && (itemStack = (playerInventory = (PlayerInventory)inventory).getItemInOffHand()).getType() == material) {
            n2 = ASManager.removeItem(playerInventory, itemStack, EquipmentSlot.OFF_HAND, n2);
        }
        if (n2 <= 0) {
            return true;
        }
        for (int i = 0; i < inventory.getSize(); ++i) {
            int n3 = inventory.first(material);
            if (n3 == -1) {
                return false;
            }
            ItemStack itemStack2 = inventory.getItem(n3);
            assert (itemStack2 != null);
            if ((n2 = ASManager.removeItem(inventory, itemStack2, n3, n2)) <= 0) break;
        }
        return true;
    }

    public static boolean removeItems(Inventory inventory, ItemStack itemStack, int n) {
        PlayerInventory playerInventory;
        ItemStack itemStack2;
        if (itemStack == null || inventory == null) {
            return false;
        }
        if (n <= 0) {
            return false;
        }
        if (n == Integer.MAX_VALUE) {
            inventory.remove(itemStack);
            return true;
        }
        int n2 = n;
        if (inventory instanceof PlayerInventory && (itemStack2 = (playerInventory = (PlayerInventory)inventory).getItemInOffHand()).isSimilar(itemStack)) {
            n2 = ASManager.removeItem(inventory, itemStack2, EquipmentSlot.OFF_HAND.ordinal(), n2);
        }
        if (n2 <= 0) {
            return true;
        }
        for (int i = 0; i < inventory.getSize(); ++i) {
            int n3 = inventory.first(itemStack);
            if (n3 == -1) {
                return false;
            }
            ItemStack itemStack3 = inventory.getItem(n3);
            assert (itemStack3 != null);
            if ((n2 = ASManager.removeItem(inventory, itemStack3, n3, n2)) <= 0) break;
        }
        return true;
    }

    public static boolean itemStackEquals(ItemStack itemStack, ItemStack itemStack2, boolean bl) {
        if (itemStack == null || itemStack2 == null) {
            return false;
        }
        if (itemStack == itemStack2) {
            return true;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        ItemMeta itemMeta2 = itemStack2.getItemMeta();
        if (!bl && MinecraftVersion.isNew()) {
            if (itemMeta instanceof Damageable) {
                ((Damageable)itemMeta).setDamage(0);
            }
            if (itemMeta2 instanceof Damageable) {
                ((Damageable)itemMeta2).setDamage(0);
            }
        }
        return !(itemStack.getType() != itemStack2.getType() || bl && itemStack.getDurability() != itemStack2.getDurability() || itemMeta != null != (itemMeta2 != null) || itemMeta != null && !Bukkit.getItemFactory().equals(itemMeta, itemMeta2));
    }

    private static int removeItem(Inventory inventory, ItemStack itemStack, int n, int n2) {
        if (itemStack.getAmount() <= n2) {
            n2 -= itemStack.getAmount();
            if (n == 45 && inventory instanceof PlayerInventory) {
                ((PlayerInventory)inventory).setItemInOffHand(null);
            } else {
                inventory.clear(n);
            }
        } else {
            itemStack.setAmount(itemStack.getAmount() - n2);
            inventory.setItem(n, itemStack);
            n2 = 0;
        }
        return n2;
    }

    private static int removeItem(PlayerInventory playerInventory, ItemStack itemStack, EquipmentSlot equipmentSlot, int n) {
        if (itemStack.getAmount() <= n) {
            n -= itemStack.getAmount();
            playerInventory.setItem(equipmentSlot, null);
        } else {
            itemStack.setAmount(itemStack.getAmount() - n);
            playerInventory.setItem(equipmentSlot, itemStack);
            n = 0;
        }
        return n;
    }

    public static int getEmptySlotOtherThan(int n, Player player) {
        PlayerInventory playerInventory = player.getInventory();
        int n2 = -1;
        List<Integer> list = Arrays.asList(36, 37, 38, 39, 40);
        for (int i = 0; i <= player.getInventory().getSize() - 1; ++i) {
            if (n == i || list.contains(i) || playerInventory.getItem(i) != null && playerInventory.getItem(i).getType() != Material.AIR) continue;
            n2 = i;
            return n2;
        }
        return n2;
    }

    public static void giveItemAtSlot(Player player, ItemStack itemStack, int n) {
        if (!ASManager.isValid(itemStack)) {
            return;
        }
        player.getInventory().setItem(n, itemStack);
        player.updateInventory();
    }

    public static boolean hasPotionEffect(LivingEntity livingEntity, PotionEffectType potionEffectType, int n) {
        for (PotionEffect potionEffect : livingEntity.getActivePotionEffects()) {
            if (potionEffect.getType() != potionEffectType || potionEffect.getAmplifier() != n) continue;
            return true;
        }
        return false;
    }

    public static boolean isLog(Material material) {
        if (material != null && !ASManager.isAir(material)) {
            boolean bl = instance.getConfig().getBoolean("settings.stems-count-as-trees", false);
            boolean bl2 = material.name().endsWith("LOG") || material.name().endsWith("LOG_2");
            boolean bl3 = material.name().endsWith("STEM");
            if (!bl2 && !bl3) {
                return false;
            }
            return bl || !bl3;
        }
        return false;
    }

    public static String getOrDefault(Replace replace, String string) {
        return replace == null ? string : replace.toString();
    }

    public static Object getOrDefault(Object object, Object object2) {
        return object == null ? object2 : object;
    }

    public static boolean doChancesPass(int n) {
        return (double)n > ThreadLocalRandom.current().nextDouble() * 100.0;
    }

    public static void reduceHeldItems(Player player, EquipmentSlot equipmentSlot, int n) {
        ItemStack itemStack = player.getInventory().getItem(equipmentSlot);
        if (itemStack.getAmount() - n <= 0) {
            itemStack = null;
        } else {
            itemStack.setAmount(itemStack.getAmount() - n);
        }
        player.getInventory().setItem(equipmentSlot, itemStack);
    }

    public static String capitalize(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        string = string.replaceAll("_", " ").toLowerCase(Locale.ROOT);
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String formatMaterialName(Material material) {
        return ASManager.formatMaterialName(material.name());
    }

    public static String formatMaterialName(String string) {
        String string2 = string.toLowerCase().replaceAll("_", " ");
        string2 = ASManager.capitalize(string2);
        return string2;
    }

    public static Material getItemFromBlock(Material material) {
        if (MinecraftVersion.getVersionNumber() >= 1120 && material.isItem()) {
            return material;
        }
        if (ASManager.isWallBlock(material)) {
            return ASManager.getItemFromBlock(ASManager.getItemFromBlock(material));
        }
        switch (material.name()) {
            case "CARROTS": {
                return Material.CARROT;
            }
            case "COCOA": {
                return Material.COCOA_BEANS;
            }
            case "KELP_PLANT": {
                return Material.KELP;
            }
            case "POTATOES": {
                return Material.POTATO;
            }
            case "TRIPWIRE": {
                return Material.STRING;
            }
        }
        return material;
    }

    public static boolean isWallBlock(Material material) {
        if (!ASManager.isValid(material)) {
            return false;
        }
        String string = material.name();
        if (string.contains("SKULL") || string.contains("HEAD")) {
            return false;
        }
        return string.contains("WALL_") || string.equals("TRIPWIRE_HOOK") || string.equals("LADDER") || string.equals("LEVER") || string.contains("BUTTON") || string.contains("BANNER") || string.equals("COCOA");
    }

    public static Object extractFromDataArray(String string, String string2, String string3, Object object) {
        for (String string4 : string.split(" ")) {
            if (!string4.startsWith(string2)) continue;
            return string4.split(string3)[1];
        }
        return object;
    }

    public static String formatTime(long l) {
        int n = (int)(l / 1000L);
        int n2 = n % 3600 % 60;
        int n3 = (int)Math.floor(n % 3600 / 60);
        int n4 = (int)Math.floor(n / 3600);
        Object object = "";
        if (n4 > 0) {
            object = (String)object + n4 + "h ";
        }
        if (n3 > 0) {
            object = (String)object + n3 + "m ";
        }
        object = (String)object + n2 + "s";
        return object;
    }

    public static void reportIssue(Exception exception, String string) {
        StackTraceElement[] stackTraceElementArray = exception.getStackTrace();
        Object object = "";
        for (StackTraceElement stackTraceElement : stackTraceElementArray) {
            String string2 = String.valueOf(stackTraceElement);
            if (!string2.contains("net.advancedplugins.ae")) continue;
            object = string2;
            break;
        }
        exception.printStackTrace();
        Bukkit.getLogger().info("[" + instance.getDescription().getName() + " ERROR] Could not pass " + ExceptionUtils.getRootCauseMessage(exception));
        Bukkit.getLogger().info("   Class: " + (String)object);
        Bukkit.getLogger().info("   Extra info: " + string + "; mc[" + MinecraftVersion.getVersionNumber() + "];");
        Bukkit.getLogger().info("If you cannot indentify cause of this, contact developer providing this report. ");
    }

    public static Material getMaterial(String string) {
        try {
            MinecraftVersion.getVersion();
            return MinecraftVersion.getVersionNumber() > 1121 ? Material.matchMaterial((String)string, (boolean)true) : Material.matchMaterial((String)string);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E e) {
        HashSet<T> hashSet = new HashSet<T>();
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (!Objects.equals(e, entry.getValue())) continue;
            hashSet.add(entry.getKey());
        }
        return hashSet;
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E e) {
        HashSet hashSet = new HashSet();
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (!Objects.equals(e, entry.getValue())) continue;
            return entry.getKey();
        }
        return null;
    }

    public static List<String> replace(List<String> list, String string, String string2) {
        list.replaceAll(string3 -> string3.replace(string, string2));
        return list;
    }

    public static int[] getSlots(String string) {
        int[] nArray = new int[1];
        if (string.equalsIgnoreCase("filler")) {
            return nArray;
        }
        if (string.contains(",")) {
            nArray = Arrays.stream(string.split(",")).mapToInt(Integer::parseInt).toArray();
        } else if (string.contains("-")) {
            nArray = Arrays.stream(string.split("-")).mapToInt(Integer::parseInt).toArray();
            nArray = IntStream.rangeClosed(nArray[0], nArray[1]).toArray();
        } else {
            nArray[0] = ASManager.parseInt(string);
        }
        return nArray;
    }

    public static boolean contains(String string, List<String> list) {
        for (String string2 : list) {
            if (!string.toLowerCase(Locale.ROOT).contains(string2.toLowerCase(Locale.ROOT))) continue;
            return true;
        }
        return false;
    }

    public static boolean contains(String string, String[] stringArray) {
        for (String string2 : stringArray) {
            if (!string.toLowerCase(Locale.ROOT).equalsIgnoreCase(string2.toLowerCase(Locale.ROOT))) continue;
            return true;
        }
        return false;
    }

    public static int parseInt(String string) {
        return ASManager.parseInt(string, 0);
    }

    public static int parseInt(String string, int n) {
        try {
            if (string.split("-").length > 1 && !string.substring(0, 1).equalsIgnoreCase("-")) {
                int n2 = Integer.parseInt(string.split("-")[0]);
                int n3 = Integer.parseInt(string.split("-")[1]);
                return ThreadLocalRandom.current().nextInt(n3 - n2) + n2;
            }
            return (int)Double.parseDouble(string.replaceAll("\"[^0-9.-]\"", "").replaceAll(" ", ""));
        } catch (Exception exception) {
            instance.getLogger().warning("Failed to parse " + string + " from String to Integer.");
            exception.printStackTrace();
            return n;
        }
    }

    public static double parseDouble(String string, double d) {
        try {
            if (string.split("-").length > 1 && !string.substring(0, 1).equalsIgnoreCase("-")) {
                double d2 = Integer.parseInt(string.split("-")[0]);
                double d3 = Integer.parseInt(string.split("-")[1]);
                return ThreadLocalRandom.current().nextDouble(d3 - d2) + d2;
            }
            return Double.parseDouble(string.replaceAll("[^\\\\d.]", ""));
        } catch (Exception exception) {
            instance.getLogger().warning("Failed to parse " + string + " from String to Double.");
            exception.printStackTrace();
            return d;
        }
    }

    public static double round(double d, int n) {
        return new BigDecimal(d).setScale(n, 3).doubleValue();
    }

    private static int findIfEnd(String string, int n) {
        if (n > string.length()) {
            return -1;
        }
        int n2 = string.indexOf("</if>", n);
        int n3 = string.indexOf("<if>", n);
        if (n3 > -1 && n3 < n2) {
            int n4 = ASManager.findIfEnd(string, n3 + 4);
            return ASManager.findIfEnd(string, n4 + 5);
        }
        return n2;
    }

    private static int findResultSplit(String string, int n) {
        if (n > string.length()) {
            return -1;
        }
        int n2 = string.indexOf(":", n);
        int n3 = string.indexOf("<if>", n);
        if (n3 > -1 && n3 < n2) {
            int n4 = ASManager.findIfEnd(string, n3 + 4);
            return ASManager.findResultSplit(string, n4 + 1);
        }
        return n2;
    }

    private static String[] splitAtIndex(String string, int n) {
        if (n >= string.length() - 1) {
            return new String[]{n >= string.length() ? string : string.substring(0, n)};
        }
        String string2 = string.substring(0, n);
        String string3 = string.substring(n + 1);
        return new String[]{string2, string3};
    }

    private static boolean checkStringsEquality(String string) {
        if (string.contains("===")) {
            String[] stringArray = string.split("===", 2);
            return stringArray[0].equals(stringArray[1]);
        }
        if (string.contains("==")) {
            String[] stringArray = string.split("==", 2);
            return stringArray[0].equalsIgnoreCase(stringArray[1]);
        }
        return false;
    }

    private static String handleIfExpression(String string) {
        while (string.contains("<if>")) {
            int n = string.indexOf("<if>");
            int n2 = n + 4;
            int n3 = ASManager.findIfEnd(string, n2);
            String string2 = string.substring(n2, n3);
            String[] stringArray = string2.split("\\?", 2);
            String string3 = stringArray[0];
            int n4 = ASManager.findResultSplit(stringArray[1], 0);
            String[] stringArray2 = ASManager.splitAtIndex(stringArray[1], n4);
            boolean bl = ASManager.parseCondition(string3);
            String string4 = bl ? stringArray2[0] : stringArray2[1];
            string = string.replace("<if>" + string2 + "</if>", string4);
        }
        return string;
    }

    public static boolean parseCondition(String string) {
        boolean bl;
        string = string.replaceAll(" ", "");
        Expression expression = new Expression(string, MathContext.UNLIMITED);
        try {
            bl = expression.eval().intValue() == 1;
        } catch (Exception exception) {
            bl = ASManager.checkStringsEquality(string);
        }
        return bl;
    }

    public static String substringBetween(String string, String string2, String string3) {
        int n;
        if (string == null || string2 == null || string3 == null) {
            return null;
        }
        int n2 = string.indexOf(string2);
        if (n2 != -1 && (n = string.indexOf(string3, n2 + string2.length())) != -1) {
            return string.substring(n2 + string2.length(), n);
        }
        return null;
    }

    public static double parseThroughCalculator(String string) {
        Object object;
        if (string.contains("<random>")) {
            object = ASManager.substringBetween(string, "<random>", "</random>");
            int n = ASManager.parseInt((String)object);
            string = string.replace("<random>" + (String)object + "</random>", Integer.toString(n));
        }
        string = string.replaceAll(" ", "");
        string = ASManager.handleIfExpression(string);
        object = new Expression(string, MathContext.UNLIMITED);
        try {
            return ((Expression)object).eval().doubleValue();
        } catch (Exception exception) {
            exception.printStackTrace();
            Bukkit.getLogger().warning("Failed to calculate '" + string + "': Invalid syntax or outcome");
            return 0.0;
        }
    }

    public static String parseVariables(String string, Map<String, String> map, Replace replace) {
        return ASManager.parseVariables(string, "", map, replace);
    }

    public static String parseVariables(String string, String string2, Map<String, String> map, Replace replace) {
        return Text.modify(string, replacer -> {
            map.forEach((string2, string3) -> {
                double d = ASManager.parseThroughCalculator(Text.modify(string3, replace));
                String string4 = string2 + string2;
                if (Math.floor(d) == d) {
                    replacer.set(string4, (long)d);
                } else {
                    replacer.set(string4, d);
                }
            });
            return replacer;
        }, false);
    }

    public static void playEffect(String string, float f, int n, Location location) {
        MinecraftVersion.getVersion();
        if (MinecraftVersion.getVersionNumber() < 1130) {
            try {
                Class<?> clazz = Class.forName("org.bukkit.Effect");
                Object obj = Enum.valueOf(clazz, string);
                Method method = location.getWorld().spigot().getClass().getMethod("playEffect", Location.class, clazz, Integer.TYPE, Integer.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE, Integer.TYPE);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                method.invoke(location.getWorld().spigot(), location, obj, 0, 0, Float.valueOf(f), Float.valueOf(f), Float.valueOf(f), Float.valueOf(0.0f), n, 32);
            } catch (Exception exception) {}
        } else {
            try {
                Class<?> clazz = Class.forName("org.bukkit.Particle");
                Object obj = Enum.valueOf(clazz, string);
                Method method = location.getWorld().getClass().getMethod("spawnParticle", clazz, Location.class, Integer.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                method.invoke(location.getWorld(), obj, location, n, Float.valueOf(f), Float.valueOf(f), Float.valueOf(f), Float.valueOf(0.0f));
            } catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private static boolean startsWithColor(String string) {
        for (String string2 : damages.values()) {
            if (!string.startsWith(string2)) continue;
            return true;
        }
        return false;
    }

    private static String addColor(String string, int n) {
        String string2 = damages.get(n);
        if (string2 == null) {
            return string;
        }
        return string2 + "_" + string;
    }

    private static boolean canAddColor(String string) {
        return string.contains("STAINED_GLASS") || string.contains("SHULKER") || string.contains("TERRACOTTA") || string.contains("WOOL") || string.contains("BANNER") && !string.endsWith("BANNER_PATTERN") || string.contains("DYE") || string.contains("CONCRETE") || string.contains("CARPET") || string.contains("BED");
    }

    public static ItemStack matchMaterial(String string, int n, int n2) {
        return ASManager.matchMaterial(string, n, n2, false, true);
    }

    public static ItemStack matchMaterial(String string, int n, int n2, boolean bl, boolean bl2) {
        boolean bl3;
        MinecraftVersion.getVersion();
        boolean bl4 = bl3 = MinecraftVersion.getVersionNumber() > 1121;
        if (bl3) {
            if (!(!string.startsWith("GOLD_") || string.contains("BLOCK") || string.contains("NUGGET") || string.contains("INGOT") || string.contains("ORE"))) {
                string = string.replace("GOLD_", "GOLDEN_");
            }
            for (Map.Entry entry : newMaterials.entrySet()) {
                if (!((String)entry.getKey()).equalsIgnoreCase(string)) continue;
                string = (String)entry.getValue();
                break;
            }
            if (ASManager.canAddColor(string) && !ASManager.startsWithColor(string)) {
                string = ASManager.addColor(string, n2);
            }
        }
        try {
            Material material = bl ? Material.matchMaterial((String)string, (boolean)true) : Material.matchMaterial((String)string);
            return !bl3 ? new ItemStack(material, n, (short)((byte)n2)) : new ItemStack(material, n);
        } catch (Exception exception) {
            if (!bl && bl3) {
                return ASManager.matchMaterial(string, n, n2, true, bl2);
            }
            if (bl2) {
                Bukkit.getLogger().info("\ufffdcFailed to match '" + string + "' material, check your configuration or use https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html  to find needed material. \ufffd7\ufffdoFurther information has been pasted to console...");
                exception.printStackTrace();
            }
            return null;
        }
    }

    public static String getRoughNumber(long l) {
        if (l <= 999L) {
            return String.valueOf(l);
        }
        String[] stringArray = new String[]{"", "K", "M", "B", "P"};
        int n = (int)(Math.log10(l) / Math.log10(1000.0));
        return new DecimalFormat("#,##0.#").format((double)l / Math.pow(1000.0, n)) + stringArray[n];
    }

    public static String format(long l) {
        return NumberFormat.getInstance(Locale.ROOT).format(l);
    }

    public static String color(String string) {
        return ColorUtils.format(string);
    }

    public static boolean isTall(Material material) {
        if (material.name().endsWith("_DOOR")) {
            return true;
        }
        if (MinecraftVersion.isNew()) {
            return material == Material.SUNFLOWER || material == Material.LILAC || material == Material.ROSE_BUSH || material == Material.PEONY;
        }
        return material.name().equals("DOUBLE_PLANT");
    }

    public static List<Location> removeDuplicateLocations(List<Location> list) {
        ArrayList<Location> arrayList = new ArrayList<Location>();
        HashSet<CallSite> hashSet = new HashSet<CallSite>();
        for (Location location : list) {
            String string;
            if (location == null || location.getWorld() == null || hashSet.contains(string = location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ())) continue;
            hashSet.add((CallSite)((Object)string));
            arrayList.add(location);
        }
        return arrayList;
    }

    public static boolean isValid(Material material) {
        return material != null && !ASManager.isAir(material);
    }

    public static boolean isValid(ItemStack itemStack) {
        return itemStack != null && itemStack.getAmount() > 0 && !ASManager.isAir(itemStack.getType());
    }

    public static boolean isValid(Block block) {
        if (block == null || ASManager.isAir(block.getType())) {
            return false;
        }
        String string = block.getType().name();
        if (string.endsWith("_PORTAL")) {
            return false;
        }
        if (string.contains("PISTON_")) {
            return string.contains("PISTON_BASE") || string.contains("PISTON_STICKY_BASE");
        }
        return !string.equals("FIRE") && !string.equals("SOUL_FIRE") && !string.equals("TALL_SEAGRASS") && !string.equals("SWEET_BERRY_BUSH") && !string.equals("BUBBLE_COLUMN") && !string.equals("LAVA");
    }

    public static void giveItem(Player player, ItemStack ... itemStackArray) {
        ASManager.giveItem(player, Arrays.stream(itemStackArray).collect(Collectors.toList()));
    }

    public static void giveItem(Player player, Collection<ItemStack> collection) {
        for (ItemStack itemStack : collection) {
            if (!ASManager.isValid(itemStack)) continue;
            if (player.getInventory().addItem(new ItemStack[]{itemStack}).isEmpty()) continue;
            if (!Bukkit.isPrimaryThread()) {
                SchedulerUtils.runTaskLater(() -> ASManager.dropItem(player.getLocation(), itemStack));
                continue;
            }
            ASManager.dropItem(player.getLocation(), itemStack);
        }
    }

    public static List<ItemStack> condense(ItemStack[] itemStackArray) {
        for (int i = 0; i < itemStackArray.length; ++i) {
            if (itemStackArray[i] == null) continue;
            for (int j = i + 1; j < itemStackArray.length; ++j) {
                if (itemStackArray[j] == null || !itemStackArray[i].isSimilar(itemStackArray[j]) || itemStackArray[i].getAmount() + itemStackArray[j].getAmount() > itemStackArray[i].getMaxStackSize()) continue;
                itemStackArray[i].setAmount(itemStackArray[i].getAmount() + itemStackArray[j].getAmount());
                itemStackArray[j] = null;
            }
        }
        return Arrays.stream(itemStackArray).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static void dropItem(Location location, ItemStack ... itemStackArray) {
        for (ItemStack itemStack : itemStackArray) {
            if (itemStack == null) continue;
            location.getWorld().dropItem(location, itemStack);
        }
    }

    public static boolean isAir(Material material) {
        if (material == null) {
            return false;
        }
        if (MinecraftVersion.getVersionNumber() >= 1130) {
            return material == Material.AIR || material == Material.CAVE_AIR || material == Material.VOID_AIR || material == Material.LEGACY_AIR;
        }
        return material == Material.AIR;
    }

    public static boolean isAir(Block block) {
        return block == null || ASManager.isAir(block.getType());
    }

    public static boolean isAir(ItemStack itemStack) {
        return itemStack == null || ASManager.isAir(itemStack.getType());
    }

    public static void sendActionBar(String string, Player player) {
        if (MinecraftVersion.getVersionNumber() >= 190) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText((String)string));
        } else {
            String string2 = "v1_8_R3";
            try {
                Class<?> clazz = Class.forName("org.bukkit.craftbukkit." + string2 + ".entity.CraftPlayer");
                Object obj = clazz.cast(player);
                Class<?> clazz2 = Class.forName("net.minecraft.server." + string2 + ".PacketPlayOutChat");
                Class<?> clazz3 = Class.forName("net.minecraft.server." + string2 + ".Packet");
                Class<?> clazz4 = Class.forName("net.minecraft.server." + string2 + (string2.equalsIgnoreCase("v1_8_R1") ? ".ChatSerializer" : ".ChatComponentText"));
                Class<?> clazz5 = Class.forName("net.minecraft.server." + string2 + ".IChatBaseComponent");
                Method method = null;
                if (string2.equalsIgnoreCase("v1_8_R1")) {
                    method = clazz4.getDeclaredMethod("a", String.class);
                }
                Object obj2 = string2.equalsIgnoreCase("v1_8_R1") ? clazz5.cast(method.invoke(clazz4, "{'text': '" + string + "'}")) : clazz4.getConstructor(String.class).newInstance(string);
                Object obj3 = clazz2.getConstructor(clazz5, Byte.TYPE).newInstance(obj2, (byte)2);
                Method method2 = clazz.getDeclaredMethod("getHandle", new Class[0]);
                Object object = method2.invoke(obj, new Object[0]);
                Field field = object.getClass().getDeclaredField("playerConnection");
                Object object2 = field.get(object);
                Method method3 = object2.getClass().getDeclaredMethod("sendPacket", clazz3);
                method3.invoke(object2, obj3);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static int getPages(int n, int n2) {
        return n / n2 + (n % n2 == 0 ? 0 : 1);
    }

    public static <T> List<T> getItemsInPage(List<T> list, int n, int n2) {
        return list.subList(n * n2, Math.min(list.size(), n2 * (n + 1)));
    }

    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                ASManager.deleteFile(file2);
            }
        }
        file.delete();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void unZip(File file, File file2) {
        if (!file2.exists() || !file2.isDirectory()) {
            file2.mkdirs();
        }
        ZipFile zipFile = new ZipFile(file);
        ZipEntry zipEntry = null;
        byte[] byArray = new byte[1024];
        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
        try {
            while (enumeration.hasMoreElements()) {
                Object object;
                zipEntry = enumeration.nextElement();
                if (zipEntry.isDirectory()) {
                    object = new File(file2, zipEntry.getName());
                    ((File)object).mkdirs();
                    continue;
                }
                object = zipFile.getInputStream(zipEntry);
                File file3 = new File(file2, zipEntry.getName());
                FileOutputStream fileOutputStream = new FileOutputStream(file3);
                int n = 0;
                while ((n = ((InputStream)object).read(byArray)) > -1) {
                    fileOutputStream.write(byArray, 0, n);
                }
                fileOutputStream.close();
                ((InputStream)object).close();
            }
        } finally {
            zipFile.close();
        }
    }

    public static Material getNonWallMaterial(Material material) {
        if (!ASManager.isValid(material)) {
            return material;
        }
        String string = material.name();
        string = string.replace("WALL_", "");
        return Material.getMaterial((String)string);
    }

    public static Material getFixedMaterial(Material material) {
        if (material == null) {
            return null;
        }
        switch (material.name()) {
            case "CARROTS": {
                if (MinecraftVersion.isNew()) {
                    return Material.CARROT;
                }
                return Material.matchMaterial((String)"CARROT_ITEM");
            }
            case "POTATOES": {
                if (MinecraftVersion.isNew()) {
                    return Material.POTATO;
                }
                return Material.matchMaterial((String)"POTATO_ITEM");
            }
            case "BEETROOTS": {
                return Material.BEETROOT;
            }
            case "COCOA": {
                return Material.COCOA_BEANS;
            }
            case "KELP_PLANT": {
                return Material.KELP;
            }
            case "TRIPWIRE": {
                return Material.STRING;
            }
            case "WATER_CAULDRON": 
            case "LAVA_CAULDRON": 
            case "POWDER_SNOW_CAULDRON": {
                return Material.CAULDRON;
            }
        }
        return material;
    }

    public static boolean isFortuneBlock(Material material) {
        boolean bl;
        String string = material.name();
        boolean bl2 = string.endsWith("_ORE");
        boolean bl3 = false;
        boolean bl4 = bl = bl2 && string.contains("GOLD") || string.contains("IRON");
        if (bl2 && !bl || bl2 && bl && bl3) {
            return true;
        }
        switch (string) {
            case "SEEDS": 
            case "WHEAT_SEEDS": 
            case "GLOWSTONE": 
            case "NETHER_WART": 
            case "SWEET_BERRIES": 
            case "SEA_LANTERN": 
            case "NETHER_GOLD_ORE": 
            case "MELON": 
            case "MELON_BLOCK": 
            case "AMETHYST_CLUSTER": {
                return true;
            }
        }
        return false;
    }

    public static int getDropAmount(Block block, Material material, ItemStack itemStack) {
        Material material2 = block.getType();
        String string = material2.name().replace("LEGACY_", "");
        boolean bl = itemStack.getEnchantments().containsKey(Enchantment.SILK_TOUCH);
        int n = itemStack.getEnchantmentLevel(VanillaEnchants.displayNameToEnchant("FORTUNE"));
        boolean bl2 = silkOnly.contains(string);
        if (bl2 && !bl) {
            return -1;
        }
        boolean bl3 = true;
        boolean bl4 = ASManager.isFortuneBlock(material2);
        int n2 = 1;
        int n3 = Integer.MAX_VALUE;
        if (!bl) {
            switch (material.name()) {
                case "IRON_INGOT": 
                case "RAW_IRON": 
                case "GOLD_INGOT": 
                case "RAW_GOLD": {
                    bl4 = true;
                    bl3 = true;
                    break;
                }
                case "ENDER_CHEST": {
                    bl4 = false;
                    bl3 = false;
                    break;
                }
            }
            switch (string) {
                case "DEEPSLATE_COPPER_ORE": 
                case "COPPER_ORE": {
                    n2 = MathUtils.randomBetween(2, 5);
                    break;
                }
                case "DEEPSLATE_LAPIS_ORE": 
                case "LAPIS_ORE": {
                    n2 = MathUtils.randomBetween(4, 9);
                    break;
                }
                case "SEA_LANTERN": {
                    if (material == material2) break;
                    n3 = 5;
                    break;
                }
                case "DEEPSLATE_REDSTONE_ORE": 
                case "REDSTONE_ORE": {
                    n2 = MathUtils.randomBetween(4, 5);
                    break;
                }
                case "NETHER_GOLD_ORE": {
                    n2 = MathUtils.randomBetween(2, 6);
                    break;
                }
                case "CLAY": 
                case "AMETHYST_CLUSTER": {
                    n2 = 4;
                    break;
                }
                case "MELON": 
                case "MELON_BLOCK": {
                    n2 = MathUtils.randomBetween(3, 7);
                    n3 = 9;
                    break;
                }
                case "GLOWSTONE": {
                    n2 = MathUtils.randomBetween(2, 4);
                    n3 = 4;
                    break;
                }
                case "ENDER_CHEST": {
                    n3 = 1;
                    bl3 = false;
                    bl4 = false;
                    break;
                }
                case "BOOKSHELF": {
                    n2 = 3;
                    n3 = 3;
                    bl3 = false;
                }
            }
        }
        if (CropUtils.isCrop(material2)) {
            n2 = CropUtils.getDropAmount(block, material, itemStack);
        } else {
            boolean bl5;
            boolean bl6 = bl5 = bl3 && material != material2 && n > 0 && !bl && bl4;
            if (bl5) {
                if (material2.name().endsWith("_ORE")) {
                    float f = ThreadLocalRandom.current().nextFloat();
                    if (f > 2.0f / ((float)n + 2.0f)) {
                        int n4 = n2;
                        for (int i = 0; i < n4; ++i) {
                            n2 += new UniformIntegerDistribution(2, n + 1).sample();
                        }
                    }
                } else {
                    n2 = new UniformIntegerDistribution(1, Math.min(n3, n2 * n)).sample();
                }
            }
        }
        if (material2 == Material.TURTLE_EGG) {
            TurtleEgg turtleEgg = (TurtleEgg)block.getBlockData();
            n2 = turtleEgg.getEggs();
        } else if (material2.name().endsWith("CANDLE")) {
            Candle candle = (Candle)block.getBlockData();
            n2 = candle.getCandles();
        }
        return MathUtils.clamp(n2, Integer.MIN_VALUE, n3);
    }

    public static int getExpToDrop(@NotNull Material material, @NotNull ItemStack itemStack) {
        String string;
        int n = 0;
        switch (string = material.name()) {
            case "COAL_ORE": 
            case "DEEPSLATE_COAL_ORE": {
                n = new Random().nextInt(3);
                break;
            }
            case "NETHER_GOLD_ORE": {
                n = new Random().nextInt(2);
                break;
            }
            case "DIAMOND_ORE": 
            case "DEEPSLATE_DIAMOND_ORE": 
            case "EMERALD_ORE": 
            case "DEEPSLATE_EMERALD_ORE": {
                n = 3 + new Random().nextInt(5);
                break;
            }
            case "LAPIS_ORE": 
            case "DEEPSLATE_LAPIS_ORE": 
            case "NETHER_QUARTZ_ORE": {
                n = 2 + new Random().nextInt(4);
                break;
            }
            case "REDSTONE_ORE": 
            case "DEEPSLATE_REDSTONE_ORE": {
                n = 1 + new Random().nextInt(5);
                break;
            }
            case "SPAWNER": {
                n = 15 + new Random().nextInt(29);
                break;
            }
            case "SCULK": {
                n = 1;
                break;
            }
            case "SCULK_SENSOR": 
            case "SCULK_SHRIEKER": 
            case "SCULK_CATALYST": 
            case "CALIBRATED_SCULK_SENSOR": {
                n = 5;
                break;
            }
            default: {
                if (string.endsWith("_ORE")) {
                    n = 1 + new Random().nextInt(3);
                }
                return n;
            }
        }
        return n;
    }

    public static Set<Material> createMaterialSet(Collection<String> collection) {
        return collection.stream().map(Material::matchMaterial).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public static boolean isUnbreakable(ItemStack itemStack) {
        return itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().isUnbreakable() || NBTapi.contains("Unbreakable", itemStack);
    }

    public static Object getNMSEntity(LivingEntity livingEntity) {
        return ReflectionMethod.CRAFT_ENTITY_GET_HANDLE.run(ClassWrapper.CRAFT_ENTITY.getClazz().cast(livingEntity), new Object[0]);
    }

    public static boolean isDamageable(Material material) {
        return material.getMaxDurability() > 0;
    }

    public static String tryOrElse(TryCatchMethodShort tryCatchMethodShort, String string) {
        return TryCatchUtil.tryOrDefault(tryCatchMethodShort::tryCatch, string);
    }

    public static void setByMatching(ItemStack itemStack, ItemStack itemStack2, LivingEntity livingEntity) {
        if (itemStack.isSimilar(livingEntity.getEquipment().getItemInMainHand())) {
            livingEntity.getEquipment().setItemInMainHand(itemStack2);
            return;
        }
        if (itemStack.isSimilar(livingEntity.getEquipment().getItemInOffHand())) {
            livingEntity.getEquipment().setItemInOffHand(itemStack2);
        }
    }

    public static boolean hasTotem(Player player) {
        ItemStack itemStack = player.getInventory().getItemInOffHand();
        return ASManager.isValid(itemStack) && itemStack.getType() == Material.TOTEM_OF_UNDYING;
    }

    public static void resetPlayerHealth(Player player, double d) {
        double d2 = MathUtils.clamp(d, player.getHealth(), player.getMaxHealth());
        player.setHealth(d2);
    }

    public static String getBlockMaterial(Block block) {
        if (block == null) {
            return "AIR";
        }
        return block.getType().name();
    }

    public static List<String> getVariables(String string, String string2, String string3) {
        ArrayList<String> arrayList = new ArrayList<String>();
        int n = 0;
        for (String string4 : string.split(string2)) {
            if (++n == 1) continue;
            arrayList.add(string4.split(string3)[0]);
        }
        return arrayList;
    }

    public static <T extends Enum<T>> boolean isValidEnum(Class<T> clazz, String string) {
        try {
            Enum.valueOf(clazz, string);
            return true;
        } catch (IllegalArgumentException illegalArgumentException) {
            return false;
        }
    }

    public static boolean isCorrectTool(ItemStack itemStack, Material material) {
        Object object = ReflectionMethod.CRAFT_ItemStack_asNMSCopy.run(null, itemStack);
        Object object2 = ReflectionMethod.CRAFT_MagicNumbers_getBlock.run(null, material);
        Object object3 = ReflectionMethod.NMS_Block_getBlockData.run(object2, new Object[0]);
        return (Boolean)ReflectionMethod.NMS_ItemStack_canDestroySpecialBlock.run(object, object3);
    }

    public static boolean notNullAndTrue(Boolean bl) {
        if (bl == null) {
            return false;
        }
        return bl;
    }

    public static boolean sameBlock(Location location, Location location2) {
        return location.getBlockX() == location2.getBlockX() && location.getBlockY() == location2.getBlockY() && location.getBlockZ() == location2.getBlockZ();
    }

    public static void debug(String string) {
        if (!debug) {
            return;
        }
        Bukkit.getLogger().info(string);
        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("advancedplugins.admin") || player.isOp()).forEach(player -> player.sendMessage(ColorUtils.format(string)));
    }

    public static String join(String[] stringArray, String string) {
        return ASManager.join(Arrays.asList(stringArray), string);
    }

    public static String join(Collection<String> collection, String string) {
        if (collection.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String string2 : collection) {
            stringBuilder.append(ASManager.capitalize(string2)).append(string);
        }
        return stringBuilder.substring(0, stringBuilder.length() - string.length());
    }

    public static String join(Iterable<String> iterable, String string) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string2 : iterable) {
            stringBuilder.append(string2).append(string);
        }
        return stringBuilder.substring(0, stringBuilder.length() - string.length());
    }

    public static String getMaterial(ItemStack itemStack) {
        if (itemStack == null) {
            return "AIR";
        }
        return itemStack.getType().name();
    }

    public static <T> T getFromArray(T[] TArray, int n) {
        if (n == -1) {
            n = TArray.length - 1;
        }
        return TArray[n];
    }

    public static String limit(String string, int n, String string2) {
        return string.length() < n ? string : string.substring(0, n - 1) + string2;
    }

    public static String join(String[] stringArray, String string, int n, int n2) {
        StringBuilder stringBuilder = new StringBuilder();
        n2 = Math.max(stringArray.length, n2);
        for (int i = n; i < n2; ++i) {
            stringBuilder.append(stringArray[i]).append(string);
        }
        return stringBuilder.substring(0, stringBuilder.length() - string.length());
    }

    public static int getEmptySlotCountInInventory(@NotNull Player player) {
        if (player.getInventory().firstEmpty() == -1) {
            return 0;
        }
        int n = 0;
        for (int i = 0; i < 36; ++i) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (itemStack != null && !itemStack.getType().equals((Object)Material.AIR)) continue;
            ++n;
        }
        return n;
    }

    public static <K, V> ImmutableMap<K, V> toImmutable(Map<K, V> map) {
        return ImmutableMap.builder().putAll(map).build();
    }

    public static <V> ImmutableList<V> toImmutableList(List<V> list) {
        return ((ImmutableList.Builder)new ImmutableList.Builder().addAll(list)).build();
    }

    public static boolean isVegetation(Material material) {
        return vegetationBlockNames.contains(material.name());
    }

    public static <V> List<String> toStringList(V ... VArray) {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (V v : VArray) {
            arrayList.add(v.toString());
        }
        return arrayList;
    }

    public static int[] subarray(int[] nArray, int n, int n2) {
        if (nArray == null || n < 0 || n2 > nArray.length || n > n2) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        return Arrays.copyOfRange(nArray, n, n2);
    }

    public static <V> V[] subarray(V[] VArray, int n, int n2) {
        if (VArray == null || n < 0 || n2 > VArray.length || n > n2) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        return Arrays.copyOfRange(VArray, n, n2);
    }

    public static boolean isDay(long l) {
        return l > 0L && l < 12300L;
    }

    public static void fillEmptyInventorySlots(Inventory inventory, ItemStack itemStack) {
        IntStream.range(0, inventory.getSize()).filter(n -> inventory.getItem(n) == null).forEach(n -> inventory.setItem(n, itemStack));
    }

    public static Location offsetToLookingLocation(Location location, double d) {
        Location location2 = location.clone();
        Vector vector = location2.getDirection();
        vector.normalize();
        vector.multiply(d);
        location2.add(vector);
        return location2;
    }

    public static <K, V> ImmutableMap<K, V> configToImmutableMap(FileConfiguration fileConfiguration, String string, Function<String, K> function, Class<V> clazz) {
        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
        for (String string2 : fileConfiguration.getConfigurationSection(string).getKeys(false)) {
            V v = clazz.cast(fileConfiguration.get(string + "." + string2));
            builder.put(function.apply(string2.toUpperCase(Locale.ROOT)), v);
        }
        return builder.build();
    }

    public static boolean isHostile(EntityType entityType) {
        return Monster.class.isAssignableFrom(entityType.getEntityClass());
    }

    public static Map<String, String> stringToMap(String ... stringArray) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (String string : stringArray) {
            String[] stringArray2 = string.split(";");
            if (stringArray2.length != 2) continue;
            hashMap.put(stringArray2[0], stringArray2[1]);
        }
        return hashMap;
    }

    public static <T> ImmutableMap<String, T> configObjecstToImmutableMap(Class<T> clazz, FileConfiguration fileConfiguration, String string) {
        ImmutableMap.Builder<String, T> builder = ImmutableMap.builder();
        Set set = fileConfiguration.getConfigurationSection(string).getKeys(false);
        for (String string2 : set) {
            try {
                T t = clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    ConfigKey configKey = field.getAnnotation(ConfigKey.class);
                    if (configKey == null) continue;
                    Object object = configKey.value().isEmpty() ? string2 : fileConfiguration.get(string + "." + string2 + "." + configKey.value());
                    field.set(t, object);
                }
                builder.put(string2, t);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return builder.build();
    }

    public static void log(String string) {
        if (!Registry.get().equalsIgnoreCase("9454")) {
            return;
        }
        instance.getLogger().warning(Text.modify("&c&o[DEV DEBUG]&r " + string));
        Bukkit.broadcastMessage((String)Text.modify("&c&o[DEV DEBUG]&r " + string));
    }

    public static FileConfiguration loadConfig(File file) {
        try {
            return YamlConfiguration.loadConfiguration((File)file);
        } catch (Exception exception) {
            ASManager.getInstance().getLogger().severe("Failed to load " + file.getName() + " file, check your configuration and try again.");
            return null;
        }
    }

    public static ItemStack makeItemGlow(ItemStack itemStack, @Nullable Boolean bl) {
        if (itemStack.hasItemMeta() && MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
            try {
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setEnchantmentGlintOverride(bl);
                itemStack.setItemMeta(itemMeta);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return itemStack;
    }

    public static ItemStack makeItemGlow(ItemStack itemStack) {
        return ASManager.makeItemGlow(itemStack, true);
    }

    public static Pair<String, Integer> parseEnchantment(String string) {
        Object object;
        int n;
        String[] stringArray = string.split(":");
        if (stringArray[0].startsWith("!")) {
            n = Integer.parseInt(stringArray[0].replace("!", ""));
            if (ThreadLocalRandom.current().nextInt(100) + 1 > n) {
                return null;
            }
            stringArray = new String[]{stringArray[1], stringArray[2]};
        }
        if (stringArray[1].contains("%")) {
            object = stringArray[1].replace("%", "").split("-");
            int n2 = Integer.parseInt(object[0]);
            int n3 = Integer.parseInt(object[1]);
            n = MathUtils.randomBetween(n2, n3);
        } else {
            n = Integer.parseInt(stringArray[1]);
        }
        object = stringArray[0];
        return new Pair<String[], Integer>((String[])object, n);
    }

    public static void saveResource(String string) {
        if (new File(instance.getDataFolder(), string).isFile()) {
            return;
        }
        ASManager.getInstance().saveResource(string, false);
    }

    public static boolean parseBoolean(String string, boolean bl) {
        if (string == null) {
            return bl;
        }
        if (string.strip().equalsIgnoreCase("true")) {
            return true;
        }
        if (string.strip().equalsIgnoreCase("false")) {
            return false;
        }
        return bl;
    }

    public static int minmax(int n, int n2, int n3) {
        return Math.max(n2, Math.min(n3, n));
    }

    public static String fetchJsonFromUrl(String string) {
        URL uRL = new URL(string);
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
        httpURLConnection.setRequestMethod("GET");
        try {
            String string2;
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));){
                String string3;
                StringBuilder stringBuilder = new StringBuilder();
                while ((string3 = bufferedReader.readLine()) != null) {
                    stringBuilder.append(string3);
                }
                string2 = stringBuilder.toString();
            }
            return string2;
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public static <T> T randomElement(Collection<T> collection) {
        if (collection.isEmpty()) {
            return null;
        }
        int n = ThreadLocalRandom.current().nextInt(collection.size());
        if (collection instanceof List) {
            List list = (List)collection;
            return (T)list.get(n);
        }
        Iterator<T> iterator = collection.iterator();
        for (int i = 0; i < n; ++i) {
            iterator.next();
        }
        return iterator.next();
    }

    public static <T> List<T> reverse(Set<T> set) {
        ArrayList<T> arrayList = new ArrayList<T>(set);
        Collections.reverse(arrayList);
        return arrayList;
    }

    public static boolean isOnline(LivingEntity livingEntity) {
        if (!(livingEntity instanceof Player)) {
            return true;
        }
        return ((Player)livingEntity).isOnline();
    }

    public static String[] listFiles(String string) {
        return new File(instance.getDataFolder(), string).list();
    }

    public static File getFile(String string) {
        return new File(instance.getDataFolder(), string);
    }

    public static String join(Map map, String string, int n) {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 1;
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry;
            Map.Entry entry2 = entry = iterator.next();
            stringBuilder.append(string.replace("%k%", ASManager.capitalize(entry2.getKey().toString())).replace("%v%", entry2.getValue().toString()));
            if (stringBuilder.length() <= n * n2) continue;
            stringBuilder.append("\n");
            ++n2;
        }
        return stringBuilder.toString();
    }

    public static boolean isPlayer(Entity entity) {
        return entity instanceof Player;
    }

    public static int[] getNumbersInRange(int n, int n2) {
        int[] nArray = new int[n2 - n];
        for (int i = n; i < n2; ++i) {
            nArray[i - n] = i;
        }
        return nArray;
    }

    public static BlockFace getCardinalDirection(float f) {
        if (f < 0.0f) {
            f += 360.0f;
        }
        if ((f %= 360.0f) <= 45.0f) {
            return BlockFace.NORTH;
        }
        if (f <= 135.0f) {
            return BlockFace.EAST;
        }
        if (f <= 225.0f) {
            return BlockFace.SOUTH;
        }
        if (f <= 315.0f) {
            return BlockFace.WEST;
        }
        return BlockFace.NORTH;
    }

    public static Collection<Block> getNearbyBlocks(Location location, float f, float f2, float f3) {
        ArrayList<Block> arrayList = new ArrayList<Block>();
        for (float f4 = -f; f4 <= f; f4 += 1.0f) {
            for (float f5 = -f2; f5 <= f2; f5 += 1.0f) {
                for (float f6 = -f3; f6 <= f3; f6 += 1.0f) {
                    arrayList.add(location.clone().add((double)f4, (double)f5, (double)f6).getBlock());
                }
            }
        }
        return arrayList;
    }

    public static ItemStack itemStackOrDefault(String string, Material material) {
        Material material2 = Material.matchMaterial((String)string);
        return material2 == null || material2.isAir() ? new ItemStack(material) : new ItemStack(material2);
    }

    public static Optional<Entity> getEntityFromUUID(UUID uUID, World world) {
        if (MinecraftVersion.isPaper()) {
            return Optional.ofNullable(Bukkit.getEntity((UUID)uUID));
        }
        return world.getEntities().stream().filter(entity -> entity.getUniqueId().equals(uUID)).findFirst();
    }

    public static <T> Set<T> shuffle(Set<T> set) {
        ArrayList<T> arrayList = new ArrayList<T>(set);
        Collections.shuffle(arrayList);
        return new HashSet<T>(arrayList);
    }

    public static String serializeItem(ItemStack itemStack) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream((OutputStream)byteArrayOutputStream);
        bukkitObjectOutputStream.writeObject((Object)itemStack);
        bukkitObjectOutputStream.flush();
        byte[] byArray = byteArrayOutputStream.toByteArray();
        return new String(Base64.getEncoder().encode(byArray));
    }

    public static ItemStack deserializeItem(String string) {
        byte[] byArray = Base64.getDecoder().decode(string);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byArray);
        BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream((InputStream)byteArrayInputStream);
        return (ItemStack)bukkitObjectInputStream.readObject();
    }

    public static JavaPlugin getInstance() {
        return instance;
    }

    private static /* synthetic */ void lambda$setInstance$0(JavaPlugin javaPlugin) {
        Server server = Bukkit.getServer();
        try {
            Object object = server.getClass().getMethod("getPluginManager", new Class[0]).invoke(server, new Object[0]);
            Method method = object.getClass().getMethod(new String(Base64.getDecoder().decode("ZGlzYWJsZVBsdWdpbg==")), Plugin.class);
            method.invoke(object, javaPlugin);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    static {
        damages = new HashMap();
        newMaterials = new HashMap();
        if (damages.isEmpty()) {
            damages.put(0, "WHITE");
            damages.put(1, "ORANGE");
            damages.put(2, "MAGENTA");
            damages.put(3, "LIGHT_BLUE");
            damages.put(4, "YELLOW");
            damages.put(5, "LIME");
            damages.put(6, "PINK");
            damages.put(7, "GRAY");
            damages.put(8, "LIGHT_GRAY");
            damages.put(9, "CYAN");
            damages.put(10, "PURPLE");
            damages.put(11, "BLUE");
            damages.put(12, "BROWN");
            damages.put(13, "GREEN");
            damages.put(14, "RED");
            damages.put(15, "BLACK");
        }
        if (newMaterials.isEmpty()) {
            newMaterials.put("EYE_OF_ENDER", "ENDER_EYE");
            newMaterials.put("ENDER_PORTAL_FRAME", "END_PORTAL_FRAME");
            newMaterials.put("FIREWORK_CHARGE", "FIREWORK_STAR");
            newMaterials.put("FIREBALL", "FIRE_CHARGE");
            newMaterials.put("SULPHUR", "GUNPOWDER");
            newMaterials.put("WOOD_DOOR", "OAK_DOOR");
            newMaterials.put("COMMAND", "COMMAND_BLOCK");
            newMaterials.put("PISTON_BASE", "PISTON");
            newMaterials.put("SKULL_ITEM", "PLAYER_HEAD");
            newMaterials.put("WORKBENCH", "CRAFTING_TABLE");
            newMaterials.put("BOOK_AND_QUILL", "WRITABLE_BOOK");
            newMaterials.put("THIN_GLASS", "GLASS_PANE");
            newMaterials.put("STORAGE_MINECART", "CHEST_MINECART");
            newMaterials.put("BREWING_STAND_ITEM", "LEGACY_BREWING_STAND_ITEM");
        }
    }
}

