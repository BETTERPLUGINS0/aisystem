/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.EntityPlayer
 *  net.minecraft.server.level.WorldServer
 *  net.minecraft.world.entity.Display
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityLiving
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.World
 *  net.minecraft.world.level.block.state.IBlockData
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.data.BlockData
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_spigot;

import java.lang.reflect.Method;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CraftBukkitBridge {
    private static final boolean IS_PAPER = CraftBukkitBridge.detectPaper();
    private static final String CB_PACKAGE = IS_PAPER ? "org.bukkit.craftbukkit" : "org.bukkit.craftbukkit.v1_21_R7";
    private static Class<?> craftWorldClass;
    private static Class<?> craftPlayerClass;
    private static Class<?> craftEntityClass;
    private static Class<?> craftLivingEntityClass;
    private static Class<?> craftItemStackClass;
    private static Class<?> craftBlockDataClass;
    private static Method craftWorldGetHandle;
    private static Method craftPlayerGetHandle;
    private static Method craftEntityGetHandle;
    private static Method craftLivingEntityGetHandle;
    private static Method craftItemStackAsNMSCopy;
    private static Method craftBlockDataGetState;
    private static Method displayTeleportDurationMethod;
    private static Method craftWorldCreateEntityMethod;

    private static boolean detectPaper() {
        try {
            Class.forName("io.papermc.paper.configuration.GlobalConfiguration");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static void initializeClasses() {
        try {
            craftWorldClass = Class.forName(CB_PACKAGE + ".CraftWorld");
            craftPlayerClass = Class.forName(CB_PACKAGE + ".entity.CraftPlayer");
            craftEntityClass = Class.forName(CB_PACKAGE + ".entity.CraftEntity");
            craftLivingEntityClass = Class.forName(CB_PACKAGE + ".entity.CraftLivingEntity");
            craftItemStackClass = Class.forName(CB_PACKAGE + ".inventory.CraftItemStack");
            craftBlockDataClass = Class.forName(CB_PACKAGE + ".block.data.CraftBlockData");
            craftWorldGetHandle = craftWorldClass.getMethod("getHandle", new Class[0]);
            craftPlayerGetHandle = craftPlayerClass.getMethod("getHandle", new Class[0]);
            craftEntityGetHandle = craftEntityClass.getMethod("getHandle", new Class[0]);
            craftLivingEntityGetHandle = craftLivingEntityClass.getMethod("getHandle", new Class[0]);
            craftItemStackAsNMSCopy = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class);
            craftBlockDataGetState = craftBlockDataClass.getMethod("getState", new Class[0]);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize CraftBukkit bridge for " + (IS_PAPER ? "Paper" : "Spigot"), e);
        }
    }

    public static boolean isPaper() {
        return IS_PAPER;
    }

    public static WorldServer getServerLevel(org.bukkit.World world) {
        try {
            return (WorldServer)craftWorldGetHandle.invoke(world, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get ServerLevel from World", e);
        }
    }

    public static WorldServer getServerLevel(Location location) {
        return CraftBukkitBridge.getServerLevel(location.getWorld());
    }

    public static EntityPlayer getServerPlayer(Player player) {
        try {
            return (EntityPlayer)craftPlayerGetHandle.invoke(player, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get ServerPlayer from Player", e);
        }
    }

    public static net.minecraft.world.entity.Entity getNMSEntity(Entity entity) {
        try {
            return (net.minecraft.world.entity.Entity)craftEntityGetHandle.invoke(entity, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get NMS Entity from Bukkit Entity", e);
        }
    }

    public static EntityLiving getNMSLivingEntity(LivingEntity entity) {
        try {
            return (EntityLiving)craftLivingEntityGetHandle.invoke(entity, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get NMS LivingEntity from Bukkit LivingEntity", e);
        }
    }

    public static net.minecraft.world.item.ItemStack asNMSCopy(ItemStack itemStack) {
        try {
            return (net.minecraft.world.item.ItemStack)craftItemStackAsNMSCopy.invoke(null, itemStack);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert ItemStack to NMS", e);
        }
    }

    public static IBlockData getBlockState(BlockData blockData) {
        try {
            return (IBlockData)craftBlockDataGetState.invoke(blockData, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get BlockState from BlockData", e);
        }
    }

    public static org.bukkit.World getBukkitWorld(World level) {
        try {
            Method getWorld = level.getClass().getMethod("getWorld", new Class[0]);
            return (org.bukkit.World)getWorld.invoke(level, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Bukkit World from Level", e);
        }
    }

    public static Entity getBukkitEntity(net.minecraft.world.entity.Entity nmsEntity) {
        try {
            Method getBukkitEntity = nmsEntity.getClass().getMethod("getBukkitEntity", new Class[0]);
            return (Entity)getBukkitEntity.invoke(nmsEntity, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Bukkit Entity from NMS Entity", e);
        }
    }

    public static void setDisplayTeleportDuration(Display display, int duration) {
        try {
            if (displayTeleportDurationMethod == null) {
                displayTeleportDurationMethod = CraftBukkitBridge.findDisplayTeleportDurationMethod();
            }
            displayTeleportDurationMethod.invoke(display, duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Method findDisplayTeleportDurationMethod() throws NoSuchMethodException {
        Class<Display> displayClass = Display.class;
        String methodName = IS_PAPER ? "setPosRotInterpolationDuration" : "d";
        Method method = displayClass.getDeclaredMethod(methodName, Integer.TYPE);
        method.setAccessible(true);
        return method;
    }

    public static String getEntityDimensionsFieldName() {
        return "dimensions";
    }

    public static net.minecraft.world.entity.Entity createNMSEntity(EntityType bukkitType, WorldServer level, Location location) {
        try {
            Class entityClass;
            if (craftWorldCreateEntityMethod == null) {
                craftWorldCreateEntityMethod = craftWorldClass.getMethod("createEntity", Location.class, Class.class);
            }
            if ((entityClass = bukkitType.getEntityClass()) == null) {
                throw new RuntimeException("No entity class for type: " + String.valueOf(bukkitType));
            }
            Entity bukkitEntity = (Entity)craftWorldCreateEntityMethod.invoke(location.getWorld(), location, entityClass);
            net.minecraft.world.entity.Entity nmsEntity = CraftBukkitBridge.getNMSEntity(bukkitEntity);
            if (nmsEntity != null) {
                nmsEntity.a_(location.getX(), location.getY(), location.getZ());
                if (location.getYaw() != 0.0f) {
                    nmsEntity.v(location.getYaw());
                }
                if (location.getPitch() != 0.0f) {
                    nmsEntity.w(location.getPitch());
                }
            }
            return nmsEntity;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create NMS entity for type: " + String.valueOf(bukkitType), e);
        }
    }

    static {
        CraftBukkitBridge.initializeClasses();
        displayTeleportDurationMethod = null;
        craftWorldCreateEntityMethod = null;
    }
}

