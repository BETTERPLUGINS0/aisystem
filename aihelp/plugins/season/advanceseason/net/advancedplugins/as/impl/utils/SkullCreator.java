/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.destroystokyo.paper.profile.PlayerProfile
 *  com.destroystokyo.paper.profile.ProfileProperty
 *  com.mojang.authlib.GameProfile
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.SkullType
 *  org.bukkit.block.Block
 *  org.bukkit.block.Skull
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 */
package net.advancedplugins.as.impl.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullCreator {
    private static boolean warningPosted = true;
    private static Field blockProfileField;
    private static Method metaSetProfileMethod;
    private static Field metaProfileField;
    private static final Map<String, GameProfile> profilesCache;

    private SkullCreator() {
    }

    public static ItemStack createSkull() {
        SkullCreator.checkLegacy();
        try {
            return new ItemStack(Material.valueOf((String)"PLAYER_HEAD"));
        } catch (IllegalArgumentException illegalArgumentException) {
            return new ItemStack(Material.valueOf((String)"SKULL_ITEM"), 1, 3);
        }
    }

    public static ItemStack itemFromName(String string) {
        return SkullCreator.itemWithName(SkullCreator.createSkull(), string);
    }

    public static ItemStack itemFromUuid(UUID uUID) {
        return SkullCreator.itemWithUuid(SkullCreator.createSkull(), uUID);
    }

    public static ItemStack itemFromUrl(String string) {
        return SkullCreator.itemWithUrl(SkullCreator.createSkull(), string);
    }

    public static ItemStack itemFromBase64(String string) {
        return SkullCreator.itemWithBase64(SkullCreator.createSkull(), string);
    }

    @Deprecated
    public static ItemStack itemWithName(ItemStack itemStack, String string) {
        SkullCreator.notNull(itemStack, "item");
        SkullCreator.notNull(string, "name");
        if (string.isEmpty() || string.equalsIgnoreCase("-")) {
            return itemStack;
        }
        SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
        skullMeta.setOwner(string);
        itemStack.setItemMeta((ItemMeta)skullMeta);
        return itemStack;
    }

    public static ItemStack itemWithUuid(ItemStack itemStack, UUID uUID) {
        SkullCreator.notNull(itemStack, "item");
        SkullCreator.notNull(uUID, "id");
        SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer((UUID)uUID));
        itemStack.setItemMeta((ItemMeta)skullMeta);
        return itemStack;
    }

    public static ItemStack itemWithUrl(ItemStack itemStack, String string) {
        SkullCreator.notNull(itemStack, "item");
        SkullCreator.notNull(string, "url");
        return SkullCreator.itemWithBase64(itemStack, SkullCreator.urlToBase64(string));
    }

    public static ItemStack itemWithBase64(ItemStack itemStack, String string) {
        if (!(itemStack.getItemMeta() instanceof SkullMeta)) {
            return null;
        }
        SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
        SkullCreator.mutateItemMeta(skullMeta, string);
        itemStack.setItemMeta((ItemMeta)skullMeta);
        return itemStack;
    }

    @Deprecated
    public static void blockWithName(Block block, String string) {
        SkullCreator.notNull(block, "block");
        SkullCreator.notNull(string, "name");
        Skull skull = (Skull)block.getState();
        skull.setOwningPlayer(Bukkit.getOfflinePlayer((String)string));
        skull.update(false, false);
    }

    public static void blockWithUuid(Block block, UUID uUID) {
        SkullCreator.notNull(block, "block");
        SkullCreator.notNull(uUID, "id");
        SkullCreator.setToSkull(block);
        Skull skull = (Skull)block.getState();
        skull.setOwningPlayer(Bukkit.getOfflinePlayer((UUID)uUID));
        skull.update(false, false);
    }

    public static void blockWithUrl(Block block, String string) {
        SkullCreator.notNull(block, "block");
        SkullCreator.notNull(string, "url");
        SkullCreator.blockWithBase64(block, SkullCreator.urlToBase64(string));
    }

    public static void blockWithBase64(Block block, String string) {
        SkullCreator.notNull(block, "block");
        SkullCreator.notNull(string, "base64");
        SkullCreator.setToSkull(block);
        Skull skull = (Skull)block.getState();
        SkullCreator.mutateBlockState(skull, string);
        skull.update(false, false);
    }

    private static void setToSkull(Block block) {
        SkullCreator.checkLegacy();
        try {
            block.setType(Material.valueOf((String)"PLAYER_HEAD"), false);
        } catch (IllegalArgumentException illegalArgumentException) {
            block.setType(Material.valueOf((String)"SKULL"), false);
            Skull skull = (Skull)block.getState();
            skull.setSkullType(SkullType.PLAYER);
            skull.update(false, false);
        }
    }

    private static void notNull(Object object, String string) {
        if (object == null) {
            throw new NullPointerException(string + " should not be null!");
        }
    }

    private static String urlToBase64(String string) {
        URI uRI;
        try {
            uRI = new URI(string);
        } catch (URISyntaxException uRISyntaxException) {
            throw new RuntimeException(uRISyntaxException);
        }
        String string2 = "{\"textures\":{\"SKIN\":{\"url\":\"" + uRI.toString() + "\"}}}";
        return Base64.getEncoder().encodeToString(string2.getBytes());
    }

    private static GameProfile makeProfile(String string) {
        if (profilesCache.containsKey(string)) {
            return profilesCache.get(string);
        }
        UUID uUID = new UUID(string.substring(string.length() - 20).hashCode(), string.substring(string.length() - 10).hashCode());
        try {
            Class<?> clazz = Class.forName("com.mojang.authlib.GameProfile");
            Class<?> clazz2 = Class.forName("com.mojang.authlib.properties.Property");
            Object obj = clazz.getConstructor(UUID.class, String.class).newInstance(uUID, "aaaaa");
            Object obj2 = clazz2.getConstructor(String.class, String.class).newInstance("textures", string);
            Method method = obj.getClass().getMethod("getProperties", new Class[0]);
            Object object = method.invoke(obj, new Object[0]);
            Method method2 = object.getClass().getMethod("put", Object.class, Object.class);
            method2.invoke(object, "textures", obj2);
            profilesCache.put(string, (GameProfile)obj);
            return (GameProfile)obj;
        } catch (ReflectiveOperationException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            return null;
        }
    }

    private static void mutateBlockState(Skull skull, String string) {
        try {
            if (blockProfileField == null) {
                blockProfileField = skull.getClass().getDeclaredField("profile");
                blockProfileField.setAccessible(true);
            }
            blockProfileField.set(skull, SkullCreator.makeProfile(string));
        } catch (IllegalAccessException | NoSuchFieldException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
        }
    }

    private static void mutateItemMeta(SkullMeta skullMeta, String string) {
        if (MinecraftVersion.isPaper()) {
            UUID uUID = new UUID(string.substring(string.length() - 20).hashCode(), string.substring(string.length() - 10).hashCode());
            PlayerProfile playerProfile = Bukkit.createProfile((UUID)uUID, (String)uUID.toString().substring(0, 16));
            playerProfile.setProperty(new ProfileProperty("textures", string));
            skullMeta.setPlayerProfile(playerProfile);
            return;
        }
        try {
            if (metaSetProfileMethod == null) {
                metaSetProfileMethod = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                metaSetProfileMethod.setAccessible(true);
            }
            metaSetProfileMethod.invoke(skullMeta, SkullCreator.makeProfile(string));
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
            try {
                if (metaProfileField == null) {
                    metaProfileField = skullMeta.getClass().getDeclaredField("profile");
                    metaProfileField.setAccessible(true);
                }
                Object object = SkullCreator.makeProfile(string);
                if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R1)) {
                    object = Class.forName("net.minecraft.world.item.component.ResolvableProfile").getConstructor(GameProfile.class).newInstance(object);
                }
                metaProfileField.set(skullMeta, object);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException reflectiveOperationException2) {
                reflectiveOperationException2.printStackTrace();
            }
        }
    }

    private static void checkLegacy() {
        try {
            Material.class.getDeclaredField("PLAYER_HEAD");
            Material.valueOf((String)"SKULL");
            if (!warningPosted) {
                warningPosted = true;
            }
        } catch (IllegalArgumentException | NoSuchFieldException exception) {
            // empty catch block
        }
    }

    static {
        profilesCache = new HashMap<String, GameProfile>();
    }
}

