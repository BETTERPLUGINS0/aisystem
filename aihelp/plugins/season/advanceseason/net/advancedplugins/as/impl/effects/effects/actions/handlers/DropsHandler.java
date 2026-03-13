/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Statistic
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.block.Container
 *  org.bukkit.block.data.BlockData
 *  org.bukkit.block.data.type.Bed
 *  org.bukkit.block.data.type.Bed$Part
 *  org.bukkit.block.data.type.Slab
 *  org.bukkit.block.data.type.Slab$Type
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.ExperienceOrb
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.player.PlayerFishEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package net.advancedplugins.as.impl.effects.effects.actions.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.ActionExecutionBuilder;
import net.advancedplugins.as.impl.effects.effects.actions.handlers.DropsCollection;
import net.advancedplugins.as.impl.effects.effects.settings.SettingValues;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.CropUtils;
import net.advancedplugins.as.impl.utils.ItemDurability;
import net.advancedplugins.as.impl.utils.LocalLocation;
import net.advancedplugins.as.impl.utils.ReallyFastBlockHandler;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import net.advancedplugins.as.impl.utils.abilities.DropsSettings;
import net.advancedplugins.as.impl.utils.abilities.SmeltMaterial;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.plugins.AdvancedChestsHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.AuraSkillsHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.BeaconsPlus3Hook;
import net.advancedplugins.as.impl.utils.hooks.plugins.ItemsAdderHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.McMMOHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.OraxenHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.ProtectionStonesHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.SlimeFunHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.SuperiorSkyblock2Hook;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Slab;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class DropsHandler {
    private final DropsSettings settings = new DropsSettings();
    private final ConcurrentHashMap<Location, DropsCollection> dropsMap = new ConcurrentHashMap();
    private ActionExecutionBuilder builder;
    private HashSet<Block> alreadyProcessed = new HashSet();
    int xpToDrop = 0;

    public void addDrops(Block block, ItemStack ... itemStackArray) {
        this.addDrops(block, Arrays.asList(itemStackArray));
    }

    public void addDrops(Block block, Collection<ItemStack> collection) {
        DropsCollection dropsCollection = this.dropsMap.computeIfAbsent(block.getLocation(), location -> new DropsCollection());
        dropsCollection.getItems().addAll(collection);
        this.dropsMap.put(block.getLocation(), dropsCollection);
    }

    public void addDrops(Location location2, ItemStack ... itemStackArray) {
        location2 = location2.getBlock().getLocation();
        DropsCollection dropsCollection = this.dropsMap.computeIfAbsent(location2, location -> new DropsCollection());
        dropsCollection.getItems().addAll(Arrays.asList(itemStackArray));
        this.dropsMap.put(location2, dropsCollection);
    }

    private void addParsedDrop(Block block, ItemStack itemStack) {
        DropsCollection dropsCollection = this.dropsMap.computeIfAbsent(block.getLocation(), location -> new DropsCollection());
        dropsCollection.getParsedItems().add(itemStack);
    }

    public void clearDrops(Block block) {
        this.dropsMap.remove(block.getLocation());
    }

    public DropsCollection getDrops(Block block) {
        return this.dropsMap.getOrDefault(block.getLocation(), null);
    }

    public Collection<DropsCollection> getAllDrops() {
        return this.dropsMap.values();
    }

    public void handle() {
        Object object;
        if (!this.settings.isBreakBlocks() && !(this.builder.getEvent() instanceof PlayerFishEvent)) {
            this.dropBlockBreakEventExperience(0);
            return;
        }
        if (this.builder.getEvent() instanceof PlayerFishEvent && this.settings.getDropsMultiplier() == 1) {
            return;
        }
        LivingEntity livingEntity = this.builder.getMain();
        if (this.settings.getTool() == null) {
            this.settings.setTool(this.builder.getItem());
        }
        if (this.settings.getTool() == null) {
            return;
        }
        boolean bl = this.builder.getEvent() instanceof BlockBreakEvent;
        if (bl) {
            McMMOHook mcMMOHook;
            object = ((BlockBreakEvent)this.builder.getEvent()).getBlock();
            if (!this.settings.isBreakBlocks() && object.getState() instanceof Container && !object.getType().name().contains("SHULKER_BOX")) {
                mcMMOHook = (Container)object.getState();
                mcMMOHook.getInventory().forEach(arg_0 -> DropsHandler.lambda$handle$3((Block)object, arg_0));
            }
            if (livingEntity instanceof Player && HooksHandler.isEnabled(HookPlugin.MCMMO)) {
                int n;
                mcMMOHook = (McMMOHook)HooksHandler.getHook(HookPlugin.MCMMO);
                if (!object.hasMetadata("non-natural") && mcMMOHook.herbalismCheck((Player)livingEntity, (BlockBreakEvent)this.builder.getEvent()) && this.settings.isAddToInventory() && mcMMOHook.blockHasHerbalismBonusDrops((Block)object) && (n = mcMMOHook.getHerbalismBonusDropMultiplier((Block)object)) >= 1) {
                    this.settings.setDropsMultiplier(this.settings.getDropsMultiplier() + n);
                }
            }
        }
        this.settings.setSilkTouch(this.settings.getTool().containsEnchantment(Enchantment.SILK_TOUCH));
        object = new ItemDurability(livingEntity, this.settings.getTool());
        ASManager.debug("[DropsHandler] " + this.dropsMap.size() + " blocks to be processed");
        boolean bl2 = true;
        for (Map.Entry<Location, DropsCollection> entry : this.dropsMap.entrySet()) {
            SlimeFunHook slimeFunHook;
            ProtectionStonesHook protectionStonesHook;
            Block block2;
            OraxenHook oraxenHook;
            Block block3;
            ItemsAdderHook itemsAdderHook;
            AdvancedChestsHook advancedChestsHook;
            BeaconsPlus3Hook beaconsPlus3Hook;
            SuperiorSkyblock2Hook superiorSkyblock2Hook;
            Object object2;
            BlockBreakEvent blockBreakEvent2;
            Object object3;
            ItemStack itemStack2;
            Location location = entry.getKey();
            if (bl) {
                boolean bl3;
                ASManager.debug("[DropsHandler] " + new LocalLocation(location.getBlock().getLocation()).getEncode() + " has already been processed: " + this.alreadyProcessed.contains(location.getBlock()));
                if (this.alreadyProcessed.contains(location.getBlock())) continue;
                this.alreadyProcessed.add(location.getBlock());
                boolean bl4 = bl3 = this.builder.getBlock() == null || location.equals((Object)this.builder.getBlock().getLocation());
                if (livingEntity instanceof Player && !bl3 && this.settings.isBreakBlocks() && !EffectsHandler.getProtection().canBreak(location, (Player)livingEntity)) continue;
                if (HooksHandler.getHook(HookPlugin.AURASKILLS) != null && livingEntity instanceof Player) {
                    ((AuraSkillsHook)HooksHandler.getHook(HookPlugin.AURASKILLS)).addBrokenBlockToMap(location.getBlock(), (Player)livingEntity, this.settings);
                    if (this.builder.getEvent() instanceof BlockBreakEvent) {
                        boolean bl5 = ((AuraSkillsHook)HooksHandler.getHook(HookPlugin.AURASKILLS)).isTerraformEvent((BlockBreakEvent)this.builder.getEvent());
                        this.settings.setDurabilityDamage(!bl5);
                        this.settings.setTerraformEvent(bl5);
                    }
                }
                if (HooksHandler.getHook(HookPlugin.MCMMO) != null && livingEntity instanceof Player) {
                    McMMOHook mcMMOHook = (McMMOHook)HooksHandler.getHook(HookPlugin.MCMMO);
                    boolean bl6 = mcMMOHook.isTreeFellerEvent(this.builder.getEvent());
                    this.settings.setDurabilityDamage(!bl6);
                    this.settings.setTreeFellerEvent(bl6);
                    itemStack2 = location.getBlock();
                    if (!itemStack2.hasMetadata("non-natural") && !itemStack2.getType().name().contains("SHULKER_BOX")) {
                        object3 = (Player)livingEntity;
                        blockBreakEvent2 = (BlockBreakEvent)this.builder.getEvent();
                        object2 = new BlockBreakEvent((Block)itemStack2, (Player)object3);
                        object2.setCancelled(blockBreakEvent2.isCancelled());
                        object2.setDropItems(blockBreakEvent2.isDropItems());
                        object2.setExpToDrop(blockBreakEvent2.getExpToDrop());
                        mcMMOHook.processBlockBreakEvent((Player)object3, (BlockBreakEvent)object2, this.settings.isAddToInventory(), this.settings.isSmelt());
                    }
                }
            }
            if (HooksHandler.getHook(HookPlugin.SUPERIORSKYBLOCK2) != null && (superiorSkyblock2Hook = (SuperiorSkyblock2Hook)HooksHandler.getHook(HookPlugin.SUPERIORSKYBLOCK2)).isStackedBlock(location.getBlock()) || HooksHandler.getHook(HookPlugin.BEACONPLUS3) != null && (beaconsPlus3Hook = (BeaconsPlus3Hook)HooksHandler.getHook(HookPlugin.BEACONPLUS3)).isBeaconPlus(location) || HooksHandler.getHook(HookPlugin.ADVANCEDCHESTS) != null && (advancedChestsHook = (AdvancedChestsHook)HooksHandler.getHook(HookPlugin.ADVANCEDCHESTS)).isAdvancedChest(location.getBlock().getLocation())) continue;
            if (HooksHandler.getHook(HookPlugin.ITEMSADDER) != null && (itemsAdderHook = (ItemsAdderHook)HooksHandler.getHook(HookPlugin.ITEMSADDER)).isCustomBlock(block3 = entry.getKey().getBlock())) {
                if (block3.hasMetadata("ae_custom_block_break")) {
                    block3.removeMetadata("ae_custom_block_break", (Plugin)EffectsHandler.getInstance());
                    List<ItemStack> list = itemsAdderHook.getLootForCustomBlock(this.settings.getTool(), block3);
                    itemStack2 = new DropsCollection();
                    itemStack2.getParsedItems().addAll(list);
                    entry.setValue((DropsCollection)itemStack2);
                    SchedulerUtils.runTaskLater(() -> itemsAdderHook.removeBlock(block3));
                    continue;
                }
                if (block3.hasMetadata("ae-skip-trigger") || this.settings.isSmelt()) continue;
            }
            if (HooksHandler.getHook(HookPlugin.ORAXEN) != null && (oraxenHook = (OraxenHook)HooksHandler.getHook(HookPlugin.ORAXEN)).isCustomBlock(block2 = entry.getKey().getBlock()) && block2.hasMetadata("ae_custom_block_break")) {
                block2.removeMetadata("ae_custom_block_break", (Plugin)EffectsHandler.getInstance());
                List<ItemStack> list = oraxenHook.getLootForCustomBlock(block2);
                itemStack2 = new DropsCollection();
                itemStack2.getParsedItems().addAll(list);
                entry.setValue((DropsCollection)itemStack2);
                SchedulerUtils.runTaskLater(() -> oraxenHook.removeBlock(block2));
                continue;
            }
            if (HooksHandler.getHook(HookPlugin.PROTECTIONSTONES) != null && (protectionStonesHook = (ProtectionStonesHook)HooksHandler.getHook(HookPlugin.PROTECTIONSTONES)).isProtectionStone(entry.getKey().getBlock())) {
                entry.setValue(new DropsCollection());
                continue;
            }
            Material material = location.getBlock().getType();
            String string = material.name();
            if (string.contains("SPAWNER") || string.endsWith("_HEAD") || string.endsWith("_SKULL") || this.settings.getTool().getType() != Material.SHEARS && string.equals("GRASS") && MinecraftVersion.getVersionNumber() > 1122 || string.equals("TALL_GRASS") || string.equals("LONG_GRASS") || string.equals("DEAD_BUSH")) continue;
            if (string.endsWith("_BED")) {
                Block block4;
                if (entry.getValue().getItems().isEmpty()) {
                    entry.setValue(entry.getValue()).getItems().add(new ItemStack(Material.matchMaterial((String)string)));
                }
                if ((block4 = ASManager.getOtherHalfOfBed(location.getBlock())) == null) continue;
                itemStack2 = location.getBlock();
                object3 = (Bed)itemStack2.getBlockData();
                if (!object3.getPart().equals((Object)Bed.Part.FOOT)) {
                    object3.setPart(Bed.Part.FOOT);
                    itemStack2.setBlockData((BlockData)object3);
                    itemStack2.getState().update(true, true);
                }
                block4.setType(Material.AIR);
                location.getBlock().setType(Material.AIR);
            }
            if (string.equalsIgnoreCase("CHORUS_FLOWER")) {
                entry.setValue(entry.getValue()).getItems().add(ASManager.matchMaterial(string, 1, 0));
            }
            if (string.equalsIgnoreCase("SNOW") || string.equalsIgnoreCase("GRASS")) {
                entry.setValue(entry.getValue()).getItems().addAll(location.getBlock().getDrops(this.settings.getTool(), (Entity)livingEntity));
            }
            if (HooksHandler.isEnabled(HookPlugin.SLIMEFUN) && (slimeFunHook = (SlimeFunHook)HooksHandler.getHook(HookPlugin.SLIMEFUN)).isSlimefunItem(location)) continue;
            for (ItemStack itemStack2 : entry.getValue().getItems()) {
                Slab slab;
                if (!ASManager.isValid(material) || (string.contains("GLASS") || string.contains("ICE")) && !this.settings.isSilkTouch() || livingEntity instanceof Player && ((Player)livingEntity).getGameMode() == GameMode.CREATIVE && this.settings.isSmelt()) continue;
                object3 = null;
                if (this.settings.isSilkTouch() && material != Material.FARMLAND && material != Material.DIRT_PATH) {
                    object3 = material;
                } else if (this.settings.isSmelt()) {
                    blockBreakEvent2 = SmeltMaterial.material(itemStack2 == null ? material : itemStack2.getType());
                    if (material.name().equals("NETHER_GOLD_ORE")) {
                        blockBreakEvent2 = new ItemStack(Material.GOLD_NUGGET);
                    }
                    object3 = blockBreakEvent2 != null ? blockBreakEvent2.getType() : (itemStack2 == null ? material : itemStack2.getType());
                } else {
                    object3 = itemStack2 == null ? material : itemStack2.getType();
                }
                object3 = ASManager.getNonWallMaterial((Material)object3);
                if (!ASManager.isValid((Material)(object3 = ASManager.getFixedMaterial((Material)object3)))) continue;
                if (ASManager.isTall((Material)object3)) {
                    this.handleTallBlock(location.getBlock());
                }
                if (object3.name().equalsIgnoreCase("REDSTONE_WIRE")) {
                    object3 = ASManager.matchMaterial("REDSTONE", 1, 0).getType();
                }
                if (this.settings.isAddToInventory() && this.settings.isSilkTouch() && material.name().startsWith("POTTED_") && itemStack2 != null) {
                    this.addParsedDrop(location.getBlock(), itemStack2.clone());
                }
                int n = this.settings.getDropsMultiplier() < 2 ? ASManager.getDropAmount(location.getBlock(), (Material)object3, this.builder.getItem()) : this.settings.getDropsMultiplier() * ASManager.getDropAmount(location.getBlock(), (Material)object3, this.builder.getItem());
                if (itemStack2 == null) {
                    itemStack2 = new ItemStack((Material)object3);
                } else {
                    itemStack2.setType((Material)object3);
                }
                if (bl && (object2 = entry.getKey().getBlock()).getBlockData() instanceof Slab && (slab = (Slab)object2.getBlockData()).getType().equals((Object)Slab.Type.DOUBLE)) {
                    n *= 2;
                }
                if (material == Material.ENDER_CHEST && !this.settings.isSilkTouch()) {
                    n = 8;
                } else if (material == Material.ENDER_CHEST) {
                    n = 1;
                }
                if (n < 1) continue;
                itemStack2.setAmount(n);
                if (material == Material.SUGAR_CANE || material == Material.CACTUS && this.settings.isAddToInventory()) {
                    object2 = this.findAllVerticalBlocks(location.getBlock(), material, new ArrayList<Block>());
                    itemStack2.setAmount(itemStack2.getAmount() + object2.size());
                }
                if (this.settings.isAddToInventory() && this.settings.isSilkTouch() && CropUtils.isCrop(material)) {
                    if (CropUtils.isWheat(material)) {
                        this.addParsedDrop(location.getBlock(), new ItemStack(Material.matchMaterial((String)"WHEAT_SEEDS"), CropUtils.getSeedAmount()));
                    } else if (CropUtils.isBeetroot(material)) {
                        this.addParsedDrop(location.getBlock(), new ItemStack(Material.BEETROOT_SEEDS, CropUtils.getSeedAmount()));
                    }
                }
                this.addParsedDrop(location.getBlock(), itemStack2);
            }
            if (this.settings.isAddToInventory() && livingEntity instanceof Player) {
                Object object4;
                if (HooksHandler.isEnabled(HookPlugin.ITEMSADDER)) {
                    object4 = (ItemsAdderHook)HooksHandler.getHook(HookPlugin.ITEMSADDER);
                    itemStack2 = new ArrayList();
                    this.dropsMap.keySet().stream().map(Location::getBlock).filter(block -> block.getType() != Material.AIR).filter(((ItemsAdderHook)object4)::isCustomBlock).forEach(block -> {
                        block.setMetadata("telepathy-broken-itemsadder", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)true));
                        itemStack2.add(block.getLocation());
                    });
                    for (BlockBreakEvent blockBreakEvent2 : itemStack2) {
                        this.dropsMap.remove(blockBreakEvent2);
                    }
                }
                if (HooksHandler.isEnabled(HookPlugin.ORAXEN)) {
                    object4 = (OraxenHook)HooksHandler.getHook(HookPlugin.ORAXEN);
                    itemStack2 = new ArrayList();
                    this.dropsMap.keySet().stream().map(Location::getBlock).filter(block -> block.getType() != Material.AIR).filter(((OraxenHook)object4)::isCustomBlock).forEach(block -> {
                        block.setMetadata("telepathy-broken-oraxen", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)true));
                        itemStack2.add(block.getLocation());
                    });
                    for (BlockBreakEvent blockBreakEvent2 : itemStack2) {
                        this.dropsMap.remove(blockBreakEvent2);
                    }
                }
            }
            if (this.settings.isBreakBlocks()) {
                if (((ItemDurability)object).isBroken()) {
                    this.clearDrops(location.getBlock());
                    break;
                }
                if (livingEntity instanceof Player && !((Player)livingEntity).getGameMode().equals((Object)GameMode.CREATIVE) && !CropUtils.isCrop(location.getBlock().getType()) && this.settings.isDurabilityDamage()) {
                    if (!SettingValues.isBreakBlockEffectDoAllDurabilityDamage()) {
                        if (bl2) {
                            ((ItemDurability)object).damageItem(1);
                        }
                    } else if (ASManager.doChancesPass((int)(100.0 / (double)((ItemDurability)object).getUnbreakingLevel() + 1.0))) {
                        ((ItemDurability)object).damageItem(1);
                    }
                }
                boolean bl7 = this.settings.isDropExp() && !this.getSettings().isSilkTouch();
                this.breakBlock((Player)livingEntity, true, bl7 && bl, location.getBlock());
            }
            bl2 = false;
        }
        this.dropBlockBreakEventExperience(this.xpToDrop);
        if (this.settings.isAddToInventory() && livingEntity instanceof Player) {
            ItemStack[] itemStackArray = (ItemStack[])this.dropsMap.values().stream().flatMap(dropsCollection -> dropsCollection.getParsedItems().stream()).toArray(ItemStack[]::new);
            ASManager.giveItem((Player)livingEntity, itemStackArray);
            return;
        }
        for (Map.Entry<Location, DropsCollection> entry : this.dropsMap.entrySet()) {
            ASManager.dropItem(entry.getKey().add(0.5, 0.0, 0.5), entry.getValue().getParsedItems().toArray(new ItemStack[0]));
        }
    }

    private void dropBlockBreakEventExperience(int n) {
        int n2 = n + this.settings.getDropExpAmount();
        if (n2 == 0 || this.builder.getBlock() == null) {
            return;
        }
        Location location = this.builder.getBlock().getLocation().clone();
        location.setX((double)location.getBlockX() + 0.5);
        location.setY((double)location.getBlockY() + 0.5);
        location.setZ((double)location.getBlockZ() + 0.5);
        ExperienceOrb experienceOrb = (ExperienceOrb)location.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB);
        experienceOrb.setExperience(n2);
    }

    public BlockBreakEvent breakBlock(Player player, boolean bl, boolean bl2, Block ... blockArray) {
        BlockBreakEvent blockBreakEvent = null;
        if (blockArray.length == 0) {
            return null;
        }
        if (this.builder.getEvent() instanceof BlockBreakEvent && HooksHandler.isEnabled(HookPlugin.MCMMO)) {
            boolean bl3 = bl = !this.settings.isTreeFellerEvent();
            if (this.settings.isTreeFellerEvent() && this.settings.isAddToInventory()) {
                this.builder.getBlock().setMetadata("ae-mcmmo-treefeller-tpdrops", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)true));
                Arrays.stream(blockArray).forEach(block -> McMMOHook.treeFellerTelepathyBlocks.put((Block)block, System.currentTimeMillis()));
            }
        }
        ArrayList<Block> arrayList = new ArrayList<Block>(Arrays.asList(blockArray));
        if (bl && SettingValues.isCallBlockBreakEvents()) {
            for (Block block2 : blockArray) {
                if (this.builder.getBlock() != null && this.builder.getBlock().equals((Object)block2) && this.getAllBlocks().size() > 1) continue;
                block2.setMetadata("blockbreakevent-ignore", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)true));
                block2.setMetadata("breakblock-effect", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)true));
                blockBreakEvent = new BlockBreakEvent(block2, player);
                Bukkit.getPluginManager().callEvent((Event)blockBreakEvent);
                block2.removeMetadata("breakblock-effect", (Plugin)EffectsHandler.getInstance());
                block2.removeMetadata("blockbreakevent-ignore", (Plugin)EffectsHandler.getInstance());
                if (!blockBreakEvent.isCancelled()) continue;
                this.dropsMap.remove(block2.getLocation());
                arrayList.remove(block2);
            }
        }
        if (SettingValues.isAddBrokenBlocksToMCStats()) {
            arrayList.forEach(block -> player.incrementStatistic(Statistic.MINE_BLOCK, block.getType()));
        }
        if (bl2) {
            for (Block block3 : arrayList) {
                this.xpToDrop += ASManager.getExpToDrop(block3.getType(), this.settings.getTool());
            }
        }
        if (!arrayList.isEmpty() && !this.settings.isTreeFellerEvent()) {
            ReallyFastBlockHandler reallyFastBlockHandler = ReallyFastBlockHandler.getForWorld(((Block)arrayList.get(0)).getWorld());
            reallyFastBlockHandler.setType(Material.AIR, arrayList.toArray(new Block[0]));
        }
        return blockBreakEvent;
    }

    private List<Block> findAllVerticalBlocks(Block block, Material material, List<Block> list) {
        Block block2 = block.getLocation().add(0.0, 1.0, 0.0).getBlock();
        if (block2.getType() != material) {
            return list;
        }
        list.add(block2);
        block2.setType(Material.AIR);
        return this.findAllVerticalBlocks(block2, material, list);
    }

    private byte handleTallBlock(Block block) {
        byte by = block.getData();
        if (ASManager.isTall(block.getType())) {
            Block block2 = block.getRelative(BlockFace.DOWN);
            if (block.getType() == block2.getType()) {
                by = block2.getData();
                block2.setType(Material.AIR);
            }
        }
        return by;
    }

    public List<Block> getAllBlocks() {
        return this.dropsMap.keySet().stream().map(Location::getBlock).collect(Collectors.toList());
    }

    public void removeBlock(Block block) {
        this.dropsMap.remove(block.getLocation());
    }

    public DropsSettings getSettings() {
        return this.settings;
    }

    public ConcurrentHashMap<Location, DropsCollection> getDropsMap() {
        return this.dropsMap;
    }

    public void setBuilder(ActionExecutionBuilder actionExecutionBuilder) {
        this.builder = actionExecutionBuilder;
    }

    private static /* synthetic */ void lambda$handle$3(Block block, ItemStack itemStack) {
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            block.getWorld().dropItem(block.getLocation(), itemStack);
        }
    }
}

