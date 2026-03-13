/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.data.BlockData
 *  org.bukkit.block.data.Levelled
 *  org.bukkit.block.data.Waterlogged
 *  org.bukkit.plugin.Plugin
 */
package net.advancedplugins.as.impl.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.FoliaScheduler;
import net.advancedplugins.as.impl.utils.nbt.backend.ClassWrapper;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.plugin.Plugin;

public class ReallyFastBlockHandler {
    private static final Map<World, ReallyFastBlockHandler> handlers = new HashMap<World, ReallyFastBlockHandler>();
    private static Class<?> craftWorldClass;
    private static Class<?> craftBlockData;
    private static Class<?> craftMagicNumbersClass;
    private static Constructor<?> blockPos;
    private static Method getHandle;
    private static Method setType;
    private static Method getState;
    private static Method getMagicBlock;
    private static Method getMagicBlockData;
    private Object nmsWorld;

    public static ReallyFastBlockHandler getForWorld(World world) {
        if (handlers.containsKey(world)) {
            return handlers.get(world);
        }
        ReallyFastBlockHandler reallyFastBlockHandler = new ReallyFastBlockHandler(world);
        handlers.put(world, reallyFastBlockHandler);
        return reallyFastBlockHandler;
    }

    public static void init() {
        try {
            Class<?> clazz = ClassWrapper.NMS_WORLD.getClazz();
            craftWorldClass = ClassWrapper.CRAFT_World.getClazz();
            Class<?> clazz2 = ClassWrapper.NMS_BLOCKPOSITION.getClazz();
            Class<?> clazz3 = ClassWrapper.NMS_IBLOCKDATA.getClazz();
            if (MinecraftVersion.isNew()) {
                craftBlockData = ClassWrapper.CRAFT_BlockData.getClazz();
                getState = craftBlockData.getMethod("getState", new Class[0]);
            }
            blockPos = clazz2.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE);
            getHandle = craftWorldClass.getMethod("getHandle", new Class[0]);
            Method method = setType = MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_18_R1) ? clazz.getMethod("a", clazz2, clazz3, Integer.TYPE) : clazz.getMethod("setTypeAndData", clazz2, clazz3, Integer.TYPE);
            if (!MinecraftVersion.isNew()) {
                craftMagicNumbersClass = ClassWrapper.CRAFT_MagicNumbers.getClazz();
                getMagicBlock = craftMagicNumbersClass.getMethod("getBlock", Material.class);
                Class<?> clazz4 = ClassWrapper.NMS_Block.getClazz();
                getMagicBlockData = clazz4.getMethod("getBlockData", new Class[0]);
            }
        } catch (NoSuchMethodException noSuchMethodException) {
            noSuchMethodException.printStackTrace();
        }
    }

    public ReallyFastBlockHandler(World world) {
        Object obj = craftWorldClass.cast(world);
        try {
            this.nmsWorld = getHandle.invoke(obj, new Object[0]);
        } catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
        }
    }

    public void setType(Material material, Block ... blockArray) {
        if (!Bukkit.isPrimaryThread()) {
            FoliaScheduler.runTask((Plugin)ASManager.getInstance(), () -> this.setType(material, blockArray));
            return;
        }
        try {
            Object object = MinecraftVersion.isNew() ? getState.invoke(craftBlockData.cast(material.createBlockData()), new Object[0]) : getMagicBlockData.invoke(getMagicBlock.invoke(craftMagicNumbersClass, material), new Object[0]);
            for (Block block : blockArray) {
                BlockData blockData;
                Object object2;
                if (block.getType() == material) continue;
                boolean bl = false;
                int n = 0;
                BlockData blockData2 = block.getBlockData();
                if (blockData2 instanceof Waterlogged) {
                    object2 = (Waterlogged)blockData2;
                    bl = object2.isWaterlogged();
                }
                if (MinecraftVersion.isPaper() && ((object2 = block.getBlockData().getMaterial().name()).equals("SEA_PICKLE") || object2.contains("CORAL") || object2.contains("KELP") || object2.contains("SEAGRASS"))) {
                    n = block.getWorld().getFluidData(block.getLocation()).getLevel();
                }
                object2 = blockPos.newInstance(block.getX(), block.getY(), block.getZ());
                setType.invoke(this.nmsWorld, object2, object, 3);
                if (bl || n > 0) {
                    block.setType(Material.WATER);
                }
                if (n <= 0 || !((blockData = block.getBlockData()) instanceof Levelled)) continue;
                blockData2 = (Levelled)blockData;
                blockData2.setLevel(n);
                block.setBlockData(blockData2);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    static {
        ReallyFastBlockHandler.init();
    }
}

