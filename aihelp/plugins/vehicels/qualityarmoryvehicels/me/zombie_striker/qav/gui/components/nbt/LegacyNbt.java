/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.gui.components.nbt;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import me.zombie_striker.qav.gui.components.nbt.NbtWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LegacyNbt
implements NbtWrapper {
    public static final String PACKAGE_NAME = Bukkit.getServer().getClass().getPackage().getName();
    public static final String NMS_VERSION = PACKAGE_NAME.substring(PACKAGE_NAME.lastIndexOf(46) + 1);
    private static Method getStringMethod;
    private static Method setStringMethod;
    private static Method setBooleanMethod;
    private static Method hasTagMethod;
    private static Method getTagMethod;
    private static Method setTagMethod;
    private static Method removeTagMethod;
    private static Method asNMSCopyMethod;
    private static Method asBukkitCopyMethod;
    private static Constructor<?> nbtCompoundConstructor;

    @Override
    public ItemStack setString(@NotNull ItemStack itemStack, String string, String string2) {
        if (itemStack.getType() == Material.AIR) {
            return itemStack;
        }
        Object object = LegacyNbt.asNMSCopy(itemStack);
        Object object2 = LegacyNbt.hasTag(object) ? LegacyNbt.getTag(object) : LegacyNbt.newNBTTagCompound();
        LegacyNbt.setString(object2, string, string2);
        LegacyNbt.setTag(object, object2);
        return LegacyNbt.asBukkitCopy(object);
    }

    @Override
    public ItemStack removeTag(@NotNull ItemStack itemStack, String string) {
        if (itemStack.getType() == Material.AIR) {
            return itemStack;
        }
        Object object = LegacyNbt.asNMSCopy(itemStack);
        Object object2 = LegacyNbt.hasTag(object) ? LegacyNbt.getTag(object) : LegacyNbt.newNBTTagCompound();
        LegacyNbt.remove(object2, string);
        LegacyNbt.setTag(object, object2);
        return LegacyNbt.asBukkitCopy(object);
    }

    @Override
    public ItemStack setBoolean(@NotNull ItemStack itemStack, String string, boolean bl) {
        if (itemStack.getType() == Material.AIR) {
            return itemStack;
        }
        Object object = LegacyNbt.asNMSCopy(itemStack);
        Object object2 = LegacyNbt.hasTag(object) ? LegacyNbt.getTag(object) : LegacyNbt.newNBTTagCompound();
        LegacyNbt.setBoolean(object2, string, bl);
        LegacyNbt.setTag(object, object2);
        return LegacyNbt.asBukkitCopy(object);
    }

    @Override
    @Nullable
    public String getString(@NotNull ItemStack itemStack, String string) {
        if (itemStack.getType() == Material.AIR) {
            return null;
        }
        Object object = LegacyNbt.asNMSCopy(itemStack);
        Object object2 = LegacyNbt.hasTag(object) ? LegacyNbt.getTag(object) : LegacyNbt.newNBTTagCompound();
        return LegacyNbt.getString(object2, string);
    }

    private static void setString(Object object, String string, String string2) {
        try {
            setStringMethod.invoke(object, string, string2);
        } catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            // empty catch block
        }
    }

    private static void setBoolean(Object object, String string, boolean bl) {
        try {
            setBooleanMethod.invoke(object, string, bl);
        } catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            // empty catch block
        }
    }

    private static void remove(Object object, String string) {
        try {
            removeTagMethod.invoke(object, string);
        } catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            // empty catch block
        }
    }

    private static String getString(Object object, String string) {
        try {
            return (String)getStringMethod.invoke(object, string);
        } catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            return null;
        }
    }

    private static boolean hasTag(Object object) {
        try {
            return (Boolean)hasTagMethod.invoke(object, new Object[0]);
        } catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            return false;
        }
    }

    public static Object getTag(Object object) {
        try {
            return getTagMethod.invoke(object, new Object[0]);
        } catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            return null;
        }
    }

    private static void setTag(Object object, Object object2) {
        try {
            setTagMethod.invoke(object, object2);
        } catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            // empty catch block
        }
    }

    private static Object newNBTTagCompound() {
        try {
            return nbtCompoundConstructor.newInstance(new Object[0]);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException reflectiveOperationException) {
            return null;
        }
    }

    public static Object asNMSCopy(ItemStack itemStack) {
        try {
            return asNMSCopyMethod.invoke(null, itemStack);
        } catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            return null;
        }
    }

    public static ItemStack asBukkitCopy(Object object) {
        try {
            return (ItemStack)asBukkitCopyMethod.invoke(null, object);
        } catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            return null;
        }
    }

    private static Class<?> getNMSClass(String string) {
        try {
            return Class.forName("net.minecraft.server." + NMS_VERSION + "." + string);
        } catch (ClassNotFoundException classNotFoundException) {
            return null;
        }
    }

    private static Class<?> getCraftItemStackClass() {
        try {
            return Class.forName("org.bukkit.craftbukkit." + NMS_VERSION + ".inventory.CraftItemStack");
        } catch (ClassNotFoundException classNotFoundException) {
            return null;
        }
    }

    static {
        try {
            getStringMethod = Objects.requireNonNull(LegacyNbt.getNMSClass("NBTTagCompound")).getMethod("getString", String.class);
            removeTagMethod = Objects.requireNonNull(LegacyNbt.getNMSClass("NBTTagCompound")).getMethod("remove", String.class);
            setStringMethod = Objects.requireNonNull(LegacyNbt.getNMSClass("NBTTagCompound")).getMethod("setString", String.class, String.class);
            setBooleanMethod = Objects.requireNonNull(LegacyNbt.getNMSClass("NBTTagCompound")).getMethod("setBoolean", String.class, Boolean.TYPE);
            hasTagMethod = Objects.requireNonNull(LegacyNbt.getNMSClass("ItemStack")).getMethod("hasTag", new Class[0]);
            getTagMethod = Objects.requireNonNull(LegacyNbt.getNMSClass("ItemStack")).getMethod("getTag", new Class[0]);
            setTagMethod = Objects.requireNonNull(LegacyNbt.getNMSClass("ItemStack")).getMethod("setTag", LegacyNbt.getNMSClass("NBTTagCompound"));
            nbtCompoundConstructor = Objects.requireNonNull(LegacyNbt.getNMSClass("NBTTagCompound")).getDeclaredConstructor(new Class[0]);
            asNMSCopyMethod = Objects.requireNonNull(LegacyNbt.getCraftItemStackClass()).getMethod("asNMSCopy", ItemStack.class);
            asBukkitCopyMethod = Objects.requireNonNull(LegacyNbt.getCraftItemStackClass()).getMethod("asBukkitCopy", LegacyNbt.getNMSClass("ItemStack"));
        } catch (NoSuchMethodException noSuchMethodException) {
            noSuchMethodException.printStackTrace();
        }
    }
}

