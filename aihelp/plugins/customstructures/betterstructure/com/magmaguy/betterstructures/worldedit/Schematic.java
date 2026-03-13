/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.EditSession
 *  com.sk89q.worldedit.WorldEdit
 *  com.sk89q.worldedit.WorldEditException
 *  com.sk89q.worldedit.bukkit.BukkitAdapter
 *  com.sk89q.worldedit.extent.Extent
 *  com.sk89q.worldedit.extent.clipboard.Clipboard
 *  com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat
 *  com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
 *  com.sk89q.worldedit.extent.clipboard.io.ClipboardReader
 *  com.sk89q.worldedit.function.operation.Operation
 *  com.sk89q.worldedit.function.operation.Operations
 *  com.sk89q.worldedit.math.BlockVector3
 *  com.sk89q.worldedit.session.ClipboardHolder
 *  com.sk89q.worldedit.world.World
 *  com.sk89q.worldedit.world.block.BaseBlock
 *  com.sk89q.worldedit.world.block.BlockState
 *  com.sk89q.worldedit.world.block.BlockType
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.data.BlockData
 *  org.bukkit.util.Vector
 */
package com.magmaguy.betterstructures.worldedit;

import com.magmaguy.betterstructures.MetadataHandler;
import com.magmaguy.betterstructures.config.DefaultConfig;
import com.magmaguy.betterstructures.util.WorldEditUtils;
import com.magmaguy.magmacore.util.Logger;
import com.magmaguy.magmacore.util.WorkloadRunnable;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

public class Schematic {
    private static final Queue<PasteBlockOperation> pasteQueue = new ConcurrentLinkedQueue<PasteBlockOperation>();
    private static boolean erroredOnce = false;
    private static boolean isDistributedPasting = false;

    private Schematic() {
    }

    public static Clipboard load(File schematicFile) {
        Clipboard clipboard;
        ClipboardFormat format = ClipboardFormats.findByFile((File)schematicFile);
        try (ClipboardReader reader = format.getReader((InputStream)new FileInputStream(schematicFile));){
            clipboard = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchElementException e) {
            Logger.warn("Failed to get element from schematic " + schematicFile.getName());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            Logger.warn("Failed to load schematic " + schematicFile.getName() + " ! 99% of the time, this is because you are not using the correct WorldEdit version for your Minecraft server. You should be downloading WorldEdit from here https://dev.bukkit.org/projects/worldedit . You can check which versions the download links are compatible with by hovering over them.");
            erroredOnce = true;
            if (!erroredOnce) {
                e.printStackTrace();
            } else {
                Logger.warn("Hiding stacktrace for this error, as it has already been printed once");
            }
            return null;
        }
        return clipboard;
    }

    public static void paste(Clipboard clipboard, Location location) {
        World world = BukkitAdapter.adapt((org.bukkit.World)location.getWorld());
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(world);){
            Operation operation = new ClipboardHolder(clipboard).createPaste((Extent)editSession).to(BlockVector3.at((double)location.getX(), (double)location.getY(), (double)location.getZ())).build();
            Operations.complete((Operation)operation);
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<PasteBlock> createPasteBlocks(Clipboard schematicClipboard, Location location, Vector schematicOffset, Function<Boolean, Material> pedestalMaterialProvider) {
        ArrayList<PasteBlock> pasteBlocks = new ArrayList<PasteBlock>();
        Location adjustedLocation = location.clone().add(schematicOffset);
        for (int x = 0; x < schematicClipboard.getDimensions().x(); ++x) {
            for (int y = 0; y < schematicClipboard.getDimensions().y(); ++y) {
                for (int z = 0; z < schematicClipboard.getDimensions().z(); ++z) {
                    boolean isGround;
                    BlockVector3 adjustedClipboardLocation = BlockVector3.at((int)(x + schematicClipboard.getMinimumPoint().x()), (int)(y + schematicClipboard.getMinimumPoint().y()), (int)(z + schematicClipboard.getMinimumPoint().z()));
                    BaseBlock baseBlock = schematicClipboard.getFullBlock(adjustedClipboardLocation);
                    BlockState blockState = baseBlock.toImmutableState();
                    BlockData blockData = Bukkit.createBlockData((String)baseBlock.toImmutableState().getAsString());
                    Material material = BukkitAdapter.adapt((BlockType)baseBlock.getBlockType());
                    Block worldBlock = adjustedLocation.clone().add(new Vector(x, y, z)).getBlock();
                    String materialString = material.toString().toUpperCase(Locale.ROOT);
                    boolean bl = isGround = !BukkitAdapter.adapt((BlockType)schematicClipboard.getBlock(BlockVector3.at((int)adjustedClipboardLocation.x(), (int)(adjustedClipboardLocation.y() + 1), (int)adjustedClipboardLocation.z())).getBlockType()).isSolid();
                    if (material == Material.BARRIER) continue;
                    if (materialString.endsWith("SIGN") || materialString.endsWith("STAIRS") || materialString.endsWith("BOX") || materialString.endsWith("CHEST_BOAT") || materialString.equals("BEACON") || materialString.endsWith("FURNACE") || materialString.equals("CALIBRATED_SCULK_SENSOR") || materialString.equals("CAMPFIRE") || materialString.equals("CARTOGRAPHY_TABLE") || materialString.equals("CAULDRON") || materialString.contains("COMMAND_BLOCK") || materialString.endsWith("ANVIL") || materialString.equals("CRAFTER") || materialString.equals("ITEM_FRAME") || materialString.equals("DISPENSER") || materialString.equals("DROPPER") || materialString.equals("ENCHANTING_TABLE") || materialString.equals("BARREL") || materialString.equals("CHEST") || materialString.equals("ENDER_CHEST") || materialString.equals("TRAPPED_CHEST") || materialString.equals("FLETCHING_TABLE") || materialString.equals("FURNACE_MINECART") || materialString.equals("GRINDSTONE") || materialString.equals("HOPPER") || materialString.equals("HOPPER_MINECART") || materialString.equals("JUKEBOX") || materialString.equals("LEVER") || materialString.equals("LOOM") || materialString.equals("LODESTONE") || materialString.startsWith("POTTED") || materialString.startsWith("SCULK") || materialString.equals("POWERED_RAIL") || materialString.equals("SMOKER") || materialString.equals("STONECUTTER") || materialString.equals("SOUL_CAMPFIRE") || materialString.contains("SPAWNER")) {
                        pasteBlocks.add(new PasteBlock(worldBlock, null, WorldEditUtils.createSingleBlockClipboard(adjustedLocation, baseBlock, blockState)));
                        continue;
                    }
                    if (material == Material.BEDROCK) {
                        if (worldBlock.getType().isSolid()) continue;
                        Material pedestalMaterial = pedestalMaterialProvider.apply(isGround);
                        worldBlock.setType(pedestalMaterial);
                        pasteBlocks.add(new PasteBlock(worldBlock, pedestalMaterial.createBlockData(), null));
                        continue;
                    }
                    pasteBlocks.add(new PasteBlock(worldBlock, blockData, null));
                }
            }
        }
        return pasteBlocks;
    }

    public static void pasteSchematic(Clipboard schematicClipboard, Location location, Vector schematicOffset, Function<Boolean, Material> pedestalMaterialProvider, Runnable onComplete) {
        List<PasteBlock> pasteBlocks = Schematic.createPasteBlocks(schematicClipboard, location, schematicOffset, pedestalMaterialProvider);
        Schematic.pasteDistributed(pasteBlocks, location, onComplete);
    }

    public static void pasteDistributed(List<PasteBlock> pasteBlocks, Location location, Runnable onComplete) {
        pasteQueue.add(new PasteBlockOperation(pasteBlocks, location, onComplete));
        if (!isDistributedPasting) {
            Schematic.processNextPaste();
        }
    }

    private static void processNextPaste() {
        if (pasteQueue.isEmpty()) {
            isDistributedPasting = false;
            return;
        }
        isDistributedPasting = true;
        PasteBlockOperation operation = pasteQueue.poll();
        WorkloadRunnable workload = new WorkloadRunnable(DefaultConfig.getPercentageOfTickUsedForPasting(), () -> {
            if (operation.onComplete != null) {
                operation.onComplete.run();
            }
            Schematic.processNextPaste();
        });
        for (PasteBlock pasteBlock : operation.blocks) {
            workload.addWorkload(() -> {
                if (pasteBlock.blockData() != null) {
                    pasteBlock.block().setBlockData(pasteBlock.blockData());
                } else if (pasteBlock.clipboard() != null) {
                    try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt((org.bukkit.World)pasteBlock.block().getLocation().getWorld()));){
                        Operation worldeditPaste = new ClipboardHolder(pasteBlock.clipboard()).createPaste((Extent)editSession).to(BlockVector3.at((int)pasteBlock.block().getX(), (int)pasteBlock.block().getY(), (int)pasteBlock.block().getZ())).build();
                        Operations.complete((Operation)worldeditPaste);
                    } catch (WorldEditException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        workload.runTaskTimer(MetadataHandler.PLUGIN, 0L, 1L);
    }

    public record PasteBlock(Block block, BlockData blockData, Clipboard clipboard) {
    }

    private record PasteBlockOperation(List<PasteBlock> blocks, Location location, Runnable onComplete) {
    }
}

