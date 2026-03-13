/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.EditSession
 *  com.sk89q.worldedit.WorldEdit
 *  com.sk89q.worldedit.WorldEditException
 *  com.sk89q.worldedit.bukkit.BukkitAdapter
 *  com.sk89q.worldedit.entity.BaseEntity
 *  com.sk89q.worldedit.entity.Entity
 *  com.sk89q.worldedit.extent.Extent
 *  com.sk89q.worldedit.extent.clipboard.Clipboard
 *  com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat
 *  com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
 *  com.sk89q.worldedit.extent.clipboard.io.ClipboardReader
 *  com.sk89q.worldedit.function.operation.Operation
 *  com.sk89q.worldedit.function.operation.Operations
 *  com.sk89q.worldedit.math.BlockVector3
 *  com.sk89q.worldedit.math.transform.AffineTransform
 *  com.sk89q.worldedit.math.transform.Transform
 *  com.sk89q.worldedit.regions.CuboidRegion
 *  com.sk89q.worldedit.regions.Region
 *  com.sk89q.worldedit.session.ClipboardHolder
 *  com.sk89q.worldedit.util.Location
 *  com.sk89q.worldedit.world.World
 *  com.sk89q.worldedit.world.block.BaseBlock
 *  com.sk89q.worldedit.world.block.BlockState
 *  com.sk89q.worldedit.world.block.BlockStateHolder
 *  com.sk89q.worldedit.world.block.BlockType
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.data.BlockData
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.Vector
 */
package com.magmaguy.magmacore.thirdparty;

import com.magmaguy.magmacore.MagmaCore;
import com.magmaguy.magmacore.util.Logger;
import com.magmaguy.magmacore.util.WorkloadRunnable;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockStateHolder;
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
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class SchematicManager {
    private static final Queue<PasteBlockOperation> pasteQueue = new ConcurrentLinkedQueue<PasteBlockOperation>();
    private static boolean erroredOnce = false;
    private static boolean isDistributedPasting = false;

    private SchematicManager() {
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

    private static List<PasteBlock> createPasteBlocks(Clipboard schematicClipboard, Location location, Vector schematicOffset, Function<Boolean, Material> pedestalMaterialProvider, boolean pasteAir) {
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
                    if (material == Material.BARRIER || material == Material.AIR && !pasteAir) continue;
                    if (materialString.endsWith("SIGN") || materialString.endsWith("STAIRS") || materialString.endsWith("BOX") || materialString.endsWith("CHEST_BOAT") || materialString.equals("BEACON") || materialString.endsWith("FURNACE") || materialString.equals("CALIBRATED_SCULK_SENSOR") || materialString.equals("CAMPFIRE") || materialString.equals("CARTOGRAPHY_TABLE") || materialString.equals("CAULDRON") || materialString.contains("COMMAND_BLOCK") || materialString.endsWith("ANVIL") || materialString.equals("CRAFTER") || materialString.equals("ITEM_FRAME") || materialString.equals("DISPENSER") || materialString.equals("DROPPER") || materialString.equals("ENCHANTING_TABLE") || materialString.equals("BARREL") || materialString.equals("CHEST") || materialString.equals("ENDER_CHEST") || materialString.equals("TRAPPED_CHEST") || materialString.equals("FLETCHING_TABLE") || materialString.equals("FURNACE_MINECART") || materialString.equals("GRINDSTONE") || materialString.equals("HOPPER") || materialString.equals("HOPPER_MINECART") || materialString.equals("JUKEBOX") || materialString.equals("LEVER") || materialString.equals("LOOM") || materialString.equals("LODESTONE") || materialString.startsWith("POTTED") || materialString.startsWith("SCULK") || materialString.equals("POWERED_RAIL") || materialString.equals("SMOKER") || materialString.equals("STONECUTTER") || materialString.equals("SOUL_CAMPFIRE") || materialString.contains("SPAWNER")) {
                        pasteBlocks.add(new PasteBlock(worldBlock, null, SchematicManager.createSingleBlockClipboard(adjustedLocation, baseBlock, blockState)));
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

    private static Clipboard createSingleBlockClipboard(Location location, final BaseBlock baseBlock, final BlockState blockState) {
        return new Clipboard(){

            public <T extends BlockStateHolder<T>> boolean setBlock(BlockVector3 position, T block) throws WorldEditException {
                return false;
            }

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

    public static void pasteFileDistributedWithPedestal(File schematicFile, Location location, Vector schematicOffset, Function<Boolean, Material> pedestalMaterialProvider, Runnable onComplete, double percentageOfTickUsedForPasting, boolean randomizeRotation, boolean pasteAir) {
        Clipboard clipboard = SchematicManager.load(schematicFile);
        if (randomizeRotation) {
            double rotateY = new Random().nextInt(4) * 90 - 90;
            if (rotateY < 0.0) {
                rotateY = 270.0;
            }
            try {
                clipboard.transform((Transform)new AffineTransform().rotateY(rotateY));
            } catch (WorldEditException e) {
                throw new RuntimeException(e);
            }
        }
        List<PasteBlock> pasteBlocks = SchematicManager.createPasteBlocks(SchematicManager.load(schematicFile), location, schematicOffset, pedestalMaterialProvider, pasteAir);
        SchematicManager.pasteDistributed(pasteBlocks, location, onComplete, percentageOfTickUsedForPasting);
    }

    public static void pasteDistributedWithPedestal(Clipboard schematicClipboard, Location location, Vector schematicOffset, Function<Boolean, Material> pedestalMaterialProvider, Runnable onComplete, double percentageOfTickUsedForPasting, boolean pasteAir) {
        List<PasteBlock> pasteBlocks = SchematicManager.createPasteBlocks(schematicClipboard, location, schematicOffset, pedestalMaterialProvider, pasteAir);
        SchematicManager.pasteDistributed(pasteBlocks, location, onComplete, percentageOfTickUsedForPasting);
    }

    public static void pasteDistributed(List<PasteBlock> pasteBlocks, Location location, Runnable onComplete, double percentageOfTickUsedForPasting) {
        pasteQueue.add(new PasteBlockOperation(pasteBlocks, location, onComplete));
        if (!isDistributedPasting) {
            SchematicManager.processNextPaste(percentageOfTickUsedForPasting);
        }
    }

    private static void processNextPaste(double percentageOfTickUsedForPasting) {
        if (pasteQueue.isEmpty()) {
            isDistributedPasting = false;
            return;
        }
        isDistributedPasting = true;
        PasteBlockOperation operation = pasteQueue.poll();
        WorkloadRunnable workload = new WorkloadRunnable(percentageOfTickUsedForPasting, () -> {
            if (operation.onComplete != null) {
                operation.onComplete.run();
            }
            SchematicManager.processNextPaste(percentageOfTickUsedForPasting);
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
        workload.runTaskTimer((Plugin)MagmaCore.getInstance().getRequestingPlugin(), 0L, 1L);
    }

    public record PasteBlock(Block block, BlockData blockData, Clipboard clipboard) {
    }

    private record PasteBlockOperation(List<PasteBlock> blocks, Location location, Runnable onComplete) {
    }
}

