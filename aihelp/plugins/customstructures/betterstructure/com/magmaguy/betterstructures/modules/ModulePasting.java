/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.EditSession
 *  com.sk89q.worldedit.WorldEdit
 *  com.sk89q.worldedit.WorldEditException
 *  com.sk89q.worldedit.bukkit.BukkitAdapter
 *  com.sk89q.worldedit.extent.clipboard.Clipboard
 *  com.sk89q.worldedit.math.BlockVector3
 *  com.sk89q.worldedit.math.transform.AffineTransform
 *  com.sk89q.worldedit.math.transform.Transform
 *  com.sk89q.worldedit.util.SideEffectSet
 *  com.sk89q.worldedit.world.World
 *  com.sk89q.worldedit.world.block.BaseBlock
 *  com.sk89q.worldedit.world.block.BlockStateHolder
 *  net.md_5.bungee.api.chat.BaseComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.Container
 *  org.bukkit.block.data.BlockData
 *  org.bukkit.block.data.Directional
 *  org.bukkit.block.data.Rail
 *  org.bukkit.block.data.type.Chest
 *  org.bukkit.block.data.type.Sign
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 */
package com.magmaguy.betterstructures.modules;

import com.magmaguy.betterstructures.MetadataHandler;
import com.magmaguy.betterstructures.api.ChestFillEvent;
import com.magmaguy.betterstructures.chests.ChestContents;
import com.magmaguy.betterstructures.config.DefaultConfig;
import com.magmaguy.betterstructures.config.modulegenerators.ModuleGeneratorsConfigFields;
import com.magmaguy.betterstructures.config.treasures.TreasureConfig;
import com.magmaguy.betterstructures.config.treasures.TreasureConfigFields;
import com.magmaguy.betterstructures.easyminecraftgoals.NMSManager;
import com.magmaguy.betterstructures.modules.ModularWorld;
import com.magmaguy.betterstructures.modules.WFCNode;
import com.magmaguy.betterstructures.util.WorldEditUtils;
import com.magmaguy.magmacore.util.Logger;
import com.magmaguy.magmacore.util.SpigotMessage;
import com.magmaguy.magmacore.util.WorkloadRunnable;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.util.SideEffectSet;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import java.io.File;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public final class ModulePasting {
    private final List<InterpretedSign> interpretedSigns = new ArrayList<InterpretedSign>();
    private final List<ChestPlacement> chestsToPlace = new ArrayList<ChestPlacement>();
    private final List<EntitySpawn> entitiesToSpawn = new ArrayList<EntitySpawn>();
    private final String spawnPoolSuffix;
    private final Location startLocation;
    private final boolean createModularWorld;
    private final List<NbtPlacement> nbtToPlace = new ArrayList<NbtPlacement>();
    private ModularWorld modularWorld;
    private final org.bukkit.World world;
    private final File worldFolder;
    private final ModuleGeneratorsConfigFields moduleGeneratorsConfigFields;

    public ModulePasting(org.bukkit.World world, File worldFolder, Deque<WFCNode> WFCNodeDeque, String spawnPoolSuffix, Location startLocation, ModuleGeneratorsConfigFields moduleGeneratorsConfigFields) {
        this.spawnPoolSuffix = spawnPoolSuffix;
        this.startLocation = startLocation;
        this.world = world;
        this.worldFolder = worldFolder;
        this.moduleGeneratorsConfigFields = moduleGeneratorsConfigFields;
        WFCNode firstNode = WFCNodeDeque.peek();
        this.createModularWorld = firstNode != null && firstNode.getWfcGenerator() != null && firstNode.getWfcGenerator().getModuleGeneratorsConfigFields().isWorldGeneration();
        this.batchPaste(WFCNodeDeque, this.interpretedSigns);
        this.createModularWorld(world, worldFolder);
        if (DefaultConfig.isNewBuildingWarn()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.hasPermission("betterstructures.warn")) continue;
                player.spigot().sendMessage((BaseComponent)SpigotMessage.commandHoverMessage("[BetterStructures] New dungeon started generating! Do not stop your server now. Click to teleport. Do \"/betterstructures silent\" to stop getting warnings!", "Click to teleport to " + startLocation.getWorld().getName() + ", " + startLocation.getBlockX() + ", " + startLocation.getBlockY() + ", " + startLocation.getBlockZ(), "/betterstructures teleport " + startLocation.getWorld().getName() + " " + startLocation.getBlockX() + " " + startLocation.getBlockY() + " " + startLocation.getBlockZ()));
            }
        }
    }

    private static boolean isNbtRichMaterial(Material m) {
        if (m == Material.CHEST || m == Material.TRAPPED_CHEST) {
            return false;
        }
        if (m.name().endsWith("_SIGN") || m.name().endsWith("_WALL_SIGN") || m.name().endsWith("_HANGING_SIGN")) {
            return false;
        }
        return switch (m) {
            case Material.SPAWNER, Material.DISPENSER, Material.DROPPER, Material.HOPPER, Material.BEACON, Material.LECTERN, Material.JUKEBOX, Material.COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK, Material.CHAIN_COMMAND_BLOCK, Material.PLAYER_HEAD, Material.PLAYER_WALL_HEAD, Material.SCULK_CATALYST, Material.SCULK_SHRIEKER -> true;
            default -> false;
        };
    }

    public static void paste(Clipboard clipboard, Location location, Integer rotation) {
        Clipboard transformedClipboard;
        if (rotation == null) {
            return;
        }
        AffineTransform transform = new AffineTransform().rotateY((double)ModulePasting.normalizeRotation(rotation));
        try {
            transformedClipboard = clipboard.transform((Transform)transform);
        } catch (WorldEditException e) {
            Logger.warn("Failed to transform clipboard: " + e.getMessage());
            throw new RuntimeException(e);
        }
        BlockVector3 minPoint = transformedClipboard.getMinimumPoint();
        org.bukkit.World world = location.getWorld();
        int baseX = location.getBlockX();
        int baseY = location.getBlockY();
        int baseZ = location.getBlockZ();
        World adaptedWorld = BukkitAdapter.adapt((org.bukkit.World)world);
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(adaptedWorld);){
            editSession.setTrackingHistory(false);
            editSession.setSideEffectApplier(SideEffectSet.none());
            transformedClipboard.getRegion().forEach(blockPos -> {
                try {
                    BaseBlock baseBlock = transformedClipboard.getFullBlock(blockPos);
                    if (baseBlock.getBlockType().getMaterial().isAir()) {
                        return;
                    }
                    int worldX = baseX + (blockPos.x() - minPoint.x());
                    int worldY = baseY + (blockPos.y() - minPoint.y());
                    int worldZ = baseZ + (blockPos.z() - minPoint.z());
                    BlockVector3 worldPos = BlockVector3.at((int)worldX, (int)worldY, (int)worldZ);
                    editSession.setBlock(worldPos, (BlockStateHolder)baseBlock);
                } catch (WorldEditException e) {
                    Logger.warn("Failed to place block at " + String.valueOf(blockPos) + ": " + e.getMessage());
                }
            });
            ModulePasting.pasteArmorStands(transformedClipboard, location, rotation);
        } catch (Exception e) {
            Logger.warn("Failed to paste structure: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static int normalizeRotation(int rotation) {
        return (360 - rotation) % 360;
    }

    public static void pasteArmorStands(Clipboard clipboard, Location location, Integer rotation) {
        Clipboard transformedClipboard;
        if (rotation == null) {
            rotation = 0;
        }
        AffineTransform transform = new AffineTransform().rotateY((double)ModulePasting.normalizeRotation(rotation));
        try {
            transformedClipboard = clipboard.transform((Transform)transform);
        } catch (WorldEditException e) {
            Logger.warn("Failed to transform clipboard for entities: " + e.getMessage());
            return;
        }
        WorldEditUtils.pasteArmorStandsOnlyFromTransformed(transformedClipboard, location);
    }

    private List<Pasteable> generatePasteMeList(Clipboard clipboard, Location worldPasteOriginLocation, Integer rotation, List<InterpretedSign> interpretedSigns) {
        Clipboard transformedClipboard;
        ArrayList<Pasteable> pasteableList = new ArrayList<Pasteable>();
        AffineTransform transform = new AffineTransform().rotateY((double)ModulePasting.normalizeRotation(rotation));
        try {
            transformedClipboard = clipboard.transform((Transform)transform);
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }
        BlockVector3 minPoint = transformedClipboard.getMinimumPoint();
        org.bukkit.World world = worldPasteOriginLocation.getWorld();
        int baseX = worldPasteOriginLocation.getBlockX();
        int baseY = worldPasteOriginLocation.getBlockY();
        int baseZ = worldPasteOriginLocation.getBlockZ();
        transformedClipboard.getRegion().forEach(blockPos -> {
            BaseBlock baseBlock = transformedClipboard.getFullBlock(blockPos);
            BlockData blockData = Bukkit.createBlockData((String)baseBlock.toImmutableState().getAsString());
            if (blockData.getMaterial().equals((Object)Material.BARRIER)) {
                return;
            }
            int worldX = baseX + (blockPos.x() - minPoint.x());
            int worldY = baseY + (blockPos.y() - minPoint.y());
            int worldZ = baseZ + (blockPos.z() - minPoint.z());
            Location pasteLocation = new Location(world, (double)worldX, (double)worldY, (double)worldZ);
            if (blockData.getMaterial().toString().toLowerCase().contains("sign")) {
                List<String> lines = this.getLines(baseBlock);
                interpretedSigns.add(new InterpretedSign(pasteLocation, lines));
                for (String line : lines) {
                    if (line.contains("[spawn]") && lines.size() > 1) {
                        try {
                            EntityType entityType = EntityType.valueOf((String)lines.get(1).toUpperCase());
                            this.entitiesToSpawn.add(new EntitySpawn(pasteLocation, entityType));
                        } catch (Exception e) {
                            Logger.warn("Invalid entity type in sign: " + lines.get(1));
                        }
                        continue;
                    }
                    if (line.contains("[chest]")) {
                        this.chestsToPlace.add(new ChestPlacement(pasteLocation, Material.CHEST, rotation));
                        continue;
                    }
                    if (!line.contains("[trapped_chest]")) continue;
                    this.chestsToPlace.add(new ChestPlacement(pasteLocation, Material.TRAPPED_CHEST, rotation));
                }
                blockData = Material.AIR.createBlockData();
            }
            if (blockData.getMaterial().equals((Object)Material.BEDROCK)) {
                if (pasteLocation.getBlock().getType().isSolid()) {
                    return;
                }
                blockData = Material.STONE.createBlockData();
            }
            if (ModulePasting.isNbtRichMaterial(blockData.getMaterial())) {
                this.nbtToPlace.add(new NbtPlacement(pasteLocation, baseBlock));
                return;
            }
            pasteableList.add(new Pasteable(pasteLocation, blockData));
        });
        return pasteableList;
    }

    private List<String> getLines(BaseBlock baseBlock) {
        ArrayList<String> strings = new ArrayList<String>();
        for (String line : WorldEditUtils.getLines(baseBlock)) {
            if (line != null && !line.isBlank() && line.contains("[pool:")) {
                strings.add(line.replace("]", this.spawnPoolSuffix + "]"));
                continue;
            }
            strings.add(line);
        }
        return strings;
    }

    public List<InterpretedSign> batchPaste(Deque<WFCNode> WFCNodeDeque, List<InterpretedSign> interpretedSigns) {
        ArrayList<Pasteable> pasteableList = new ArrayList<Pasteable>();
        ArrayList<EntityPasteInfo> entityPasteInfos = new ArrayList<EntityPasteInfo>();
        while (!WFCNodeDeque.isEmpty()) {
            Clipboard clipboard;
            WFCNode WFCNode2 = WFCNodeDeque.poll();
            if (WFCNode2 == null || WFCNode2.getModulesContainer() == null || (clipboard = WFCNode2.getModulesContainer().getClipboard()) == null) continue;
            pasteableList.addAll(this.generatePasteMeList(clipboard, WFCNode2.getRealLocation(this.startLocation), WFCNode2.getModulesContainer().getRotation(), interpretedSigns));
            AffineTransform transform = new AffineTransform().rotateY((double)ModulePasting.normalizeRotation(WFCNode2.getModulesContainer().getRotation()));
            try {
                Clipboard transformedClipboard = clipboard.transform((Transform)transform);
                entityPasteInfos.add(new EntityPasteInfo(transformedClipboard, WFCNode2.getRealLocation(this.startLocation), WFCNode2.getModulesContainer().getRotation()));
            } catch (WorldEditException e) {
                Logger.warn("Failed to transform clipboard for entities: " + e.getMessage());
            }
        }
        ArrayList<Pasteable> slowBlocks = new ArrayList<Pasteable>();
        WorkloadRunnable pasteMeRunnable = new WorkloadRunnable(0.1, () -> {
            WorkloadRunnable vanillaPlacementRunnable = new WorkloadRunnable(0.1, () -> this.postPasteProcessing(entityPasteInfos));
            for (Pasteable slowBlock : slowBlocks) {
                vanillaPlacementRunnable.addWorkload(() -> slowBlock.location.getBlock().setBlockData(slowBlock.blockData, false));
            }
            vanillaPlacementRunnable.runTaskTimer(MetadataHandler.PLUGIN, 0L, 1L);
        });
        ArrayList<InterpretedSign> freshlyInterpretedSigns = new ArrayList<InterpretedSign>();
        boolean fastPathEnabled = this.createModularWorld;
        for (Pasteable pasteable : pasteableList) {
            if (!fastPathEnabled) {
                slowBlocks.add(pasteable);
                continue;
            }
            if (pasteable.blockData.getLightEmission() > 0 || pasteable.blockData instanceof Directional || pasteable.blockData instanceof Rail || pasteable.blockData instanceof Sign) {
                slowBlocks.add(pasteable);
                continue;
            }
            pasteMeRunnable.addWorkload(() -> NMSManager.getAdapter().setBlockInNativeDataPalette(pasteable.location.getWorld(), pasteable.location.getBlockX(), pasteable.location.getBlockY(), pasteable.location.getBlockZ(), pasteable.blockData, true));
        }
        pasteMeRunnable.runTaskTimer(MetadataHandler.PLUGIN, 0L, 1L);
        return freshlyInterpretedSigns;
    }

    private void postPasteProcessing(List<EntityPasteInfo> entityPasteInfos) {
        BlockData wp;
        if (this.createModularWorld) {
            this.createModularWorld(this.world, this.worldFolder);
            this.modularWorld.spawnOtherEntities();
        }
        if (!this.nbtToPlace.isEmpty()) {
            World adaptedWorld = BukkitAdapter.adapt((org.bukkit.World)this.world);
            try (EditSession editSession = WorldEdit.getInstance().newEditSession(adaptedWorld);){
                editSession.setTrackingHistory(false);
                editSession.setSideEffectApplier(SideEffectSet.none());
                for (NbtPlacement np : this.nbtToPlace) {
                    wp = BlockVector3.at((int)np.location().getBlockX(), (int)np.location().getBlockY(), (int)np.location().getBlockZ());
                    try {
                        editSession.setBlock((BlockVector3)wp, (BlockStateHolder)np.baseBlock());
                    } catch (WorldEditException e) {
                        Logger.warn("Failed to set NBT block at " + String.valueOf(np.location()) + ": " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                Logger.warn("Failed NBT post-paste session: " + e.getMessage());
            }
        }
        this.pasteArmorStandsForBatch(entityPasteInfos);
        for (ChestPlacement chestPlacement : this.chestsToPlace) {
            Block block = chestPlacement.location.getBlock();
            block.setType(chestPlacement.material);
            wp = block.getBlockData();
            if (!(wp instanceof Chest)) continue;
            Chest chest = (Chest)wp;
            block.setBlockData((BlockData)chest, false);
            String treasureFilename = this.moduleGeneratorsConfigFields.getTreasureFile();
            TreasureConfigFields treasureConfigFields = TreasureConfig.getConfigFields(treasureFilename);
            if (treasureConfigFields == null) continue;
            ChestContents chestContents = new ChestContents(treasureConfigFields);
            Container container = (Container)block.getState();
            chestContents.rollChestContents(container);
            ChestFillEvent chestFillEvent = new ChestFillEvent(container, treasureFilename);
            Bukkit.getServer().getPluginManager().callEvent((Event)chestFillEvent);
            if (chestFillEvent.isCancelled()) continue;
            container.update(true);
        }
        for (EntitySpawn entitySpawn : this.entitiesToSpawn) {
            try {
                LivingEntity entity = (LivingEntity)this.world.spawnEntity(entitySpawn.location, entitySpawn.entityType);
                entity.setRemoveWhenFarAway(false);
                entity.setPersistent(true);
            } catch (Exception e) {
                Logger.warn("Failed to spawn entity of type " + String.valueOf(entitySpawn.entityType) + " at " + String.valueOf(entitySpawn.location));
            }
        }
    }

    private void pasteArmorStandsForBatch(List<EntityPasteInfo> entityPasteInfos) {
        for (EntityPasteInfo info : entityPasteInfos) {
            try {
                WorldEditUtils.pasteArmorStandsOnlyFromTransformed(info.clipboard, info.location);
            } catch (Exception e) {
                Logger.warn("Failed to paste entities for batch operation at " + String.valueOf(info.location) + ": " + e.getMessage());
            }
        }
    }

    private void createModularWorld(org.bukkit.World world, File worldFolder) {
        this.modularWorld = new ModularWorld(world, worldFolder, this.interpretedSigns);
    }

    private record EntityPasteInfo(Clipboard clipboard, Location location, Integer rotation) {
    }

    private record Pasteable(Location location, BlockData blockData) {
    }

    private record NbtPlacement(Location location, BaseBlock baseBlock) {
    }

    private record ChestPlacement(Location location, Material material, Integer rotation) {
    }

    private record EntitySpawn(Location location, EntityType entityType) {
    }

    public record InterpretedSign(Location location, List<String> text) {
    }
}

