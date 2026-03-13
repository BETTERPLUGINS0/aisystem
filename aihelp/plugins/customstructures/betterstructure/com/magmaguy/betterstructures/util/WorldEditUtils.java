/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.jnbt.CompoundTag
 *  com.sk89q.jnbt.ListTag
 *  com.sk89q.jnbt.StringTag
 *  com.sk89q.worldedit.EditSession
 *  com.sk89q.worldedit.WorldEdit
 *  com.sk89q.worldedit.WorldEditException
 *  com.sk89q.worldedit.bukkit.BukkitAdapter
 *  com.sk89q.worldedit.entity.BaseEntity
 *  com.sk89q.worldedit.entity.Entity
 *  com.sk89q.worldedit.extent.Extent
 *  com.sk89q.worldedit.extent.clipboard.Clipboard
 *  com.sk89q.worldedit.function.mask.BlockTypeMask
 *  com.sk89q.worldedit.function.mask.Mask
 *  com.sk89q.worldedit.function.operation.Operation
 *  com.sk89q.worldedit.function.operation.Operations
 *  com.sk89q.worldedit.math.BlockVector3
 *  com.sk89q.worldedit.regions.CuboidRegion
 *  com.sk89q.worldedit.regions.Region
 *  com.sk89q.worldedit.session.ClipboardHolder
 *  com.sk89q.worldedit.util.Location
 *  com.sk89q.worldedit.util.SideEffectSet
 *  com.sk89q.worldedit.world.World
 *  com.sk89q.worldedit.world.block.BaseBlock
 *  com.sk89q.worldedit.world.block.BlockState
 *  com.sk89q.worldedit.world.block.BlockStateHolder
 *  com.sk89q.worldedit.world.block.BlockType
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.util.Vector
 */
package com.magmaguy.betterstructures.util;

import com.magmaguy.magmacore.util.Logger;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.mask.BlockTypeMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.SideEffectSet;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldedit.world.block.BlockType;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldEditUtils {
    private static final ArrayList<String> values = new ArrayList();

    public static Vector getSchematicOffset(Clipboard clipboard) {
        return new Vector(clipboard.getMinimumPoint().x() - clipboard.getOrigin().x(), clipboard.getMinimumPoint().y() - clipboard.getOrigin().y(), clipboard.getMinimumPoint().z() - clipboard.getOrigin().z());
    }

    public static List<String> getLines(@NotNull BaseBlock baseBlock) {
        values.clear();
        ArrayList<String> lines = new ArrayList<String>();
        if (baseBlock.getNbtData() == null) {
            return lines;
        }
        for (int i = 1; i < 5; ++i) {
            String line = WorldEditUtils.getLine(baseBlock, i);
            if (line == null) {
                return new ArrayList<String>();
            }
            if (line.isEmpty() || line.isBlank()) continue;
            lines.add(line);
        }
        return lines;
    }

    public static String getLine(@NotNull BaseBlock baseBlock, @Positive int line) {
        values.clear();
        if (baseBlock.getNbtData() == null) {
            return "";
        }
        CompoundTag data = baseBlock.getNbtData();
        return WorldEditUtils.getLineWe(data, line);
    }

    private static String getLineWe(@NotNull CompoundTag data, @Positive int line) {
        try {
            if (data.getValue().containsKey("Text" + line)) {
                return WorldEditUtils.getOldWEFormat(data, line);
            }
            return WorldEditUtils.getNewWEFormat(data, line);
        } catch (Exception ex) {
            Bukkit.getLogger().warning("Unexpected sign format!" + String.valueOf(data));
            return "";
        }
    }

    private static String getOldWEFormat(@NotNull CompoundTag data, @Positive int line) {
        try {
            String text = ((StringTag)data.getValue().get("Text" + line)).getValue();
            Pattern pattern = Pattern.compile("\\{\"text\":\"(.*?)\"\\}");
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                String extractedText = matcher.group(1);
                return extractedText;
            }
            throw new Exception();
        } catch (Exception ex) {
            Bukkit.getLogger().warning("Unexpected sign format in legacy read!\n" + String.valueOf(data));
            return null;
        }
    }

    private static String getNewWEFormat(@NotNull CompoundTag data, @Positive int line) {
        try {
            CompoundTag frontText = (CompoundTag)data.getValue().get("front_text");
            ListTag messages = (ListTag)frontText.getValue().get("messages");
            String text = messages.getString(line - 1);
            if (text.contains("\"text\":")) {
                text = text.split("text\":\"")[1].split("\"")[0];
            }
            if ((text = text.replaceAll("\"", "")).contains("test")) {
                Bukkit.getLogger().warning("boss name:" + text);
            }
            return text;
        } catch (Exception ex) {
            Bukkit.getLogger().warning("Unexpected sign format in new read!\n" + String.valueOf(data));
            return null;
        }
    }

    public static Clipboard createSingleBlockClipboard(Location location, final BaseBlock baseBlock, final BlockState blockState) {
        return new Clipboard(){

            public <T extends BlockStateHolder<T>> boolean setBlock(BlockVector3 position, T block) throws WorldEditException {
                return false;
            }

            @Nullable
            public Operation commit() {
                return null;
            }

            public BlockState getBlock(BlockVector3 position) {
                return blockState;
            }

            public BaseBlock getFullBlock(BlockVector3 position) {
                return baseBlock;
            }

            public BlockVector3 getMinimumPoint() {
                return BlockVector3.at((int)0, (int)0, (int)0);
            }

            public BlockVector3 getMaximumPoint() {
                return BlockVector3.at((int)0, (int)0, (int)0);
            }

            public List<? extends Entity> getEntities(Region region) {
                return new ArrayList();
            }

            public List<? extends Entity> getEntities() {
                return new ArrayList();
            }

            @Nullable
            public Entity createEntity(com.sk89q.worldedit.util.Location location, BaseEntity entity) {
                return null;
            }

            public Region getRegion() {
                return new CuboidRegion(BlockVector3.at((int)0, (int)0, (int)0), BlockVector3.at((int)0, (int)0, (int)0));
            }

            public BlockVector3 getDimensions() {
                return BlockVector3.at((int)1, (int)1, (int)1);
            }

            public BlockVector3 getOrigin() {
                return BlockVector3.at((int)0, (int)0, (int)0);
            }

            public void setOrigin(BlockVector3 origin) {
            }
        };
    }

    public static void pasteArmorStandsOnlyFromTransformed(Clipboard transformedClipboard, Location location) {
        World adaptedWorld = BukkitAdapter.adapt((org.bukkit.World)location.getWorld());
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(adaptedWorld);){
            editSession.setTrackingHistory(false);
            editSession.setSideEffectApplier(SideEffectSet.none());
            ClipboardHolder clipboardHolder = new ClipboardHolder(transformedClipboard);
            BlockVector3 minPoint = transformedClipboard.getMinimumPoint();
            BlockVector3 origin = transformedClipboard.getOrigin();
            BlockVector3 pastePosition = BlockVector3.at((int)(location.getBlockX() + (origin.x() - minPoint.x())), (int)(location.getBlockY() + (origin.y() - minPoint.y())), (int)(location.getBlockZ() + (origin.z() - minPoint.z())));
            Operation operation = clipboardHolder.createPaste((Extent)editSession).to(pastePosition).copyEntities(true).copyBiomes(false).ignoreAirBlocks(true).maskSource((Mask)new BlockTypeMask((Extent)transformedClipboard, new BlockType[0])).build();
            Operations.complete((Operation)operation);
        } catch (Exception e) {
            Logger.warn("Failed to paste entities at " + String.valueOf(location) + ": " + e.getMessage());
        }
    }
}

