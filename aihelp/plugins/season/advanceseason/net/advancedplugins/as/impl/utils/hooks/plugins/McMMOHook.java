/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.gmail.nossr50.api.AbilityAPI
 *  com.gmail.nossr50.api.ExperienceAPI
 *  com.gmail.nossr50.api.ItemSpawnReason
 *  com.gmail.nossr50.api.SkillAPI
 *  com.gmail.nossr50.api.TreeFellerBlockBreakEvent
 *  com.gmail.nossr50.datatypes.meta.BonusDropMeta
 *  com.gmail.nossr50.datatypes.player.McMMOPlayer
 *  com.gmail.nossr50.datatypes.skills.PrimarySkillType
 *  com.gmail.nossr50.datatypes.skills.SubSkillType
 *  com.gmail.nossr50.events.fake.FakePlayerFishEvent
 *  com.gmail.nossr50.events.items.McMMOItemSpawnEvent
 *  com.gmail.nossr50.mcMMO
 *  com.gmail.nossr50.util.BlockUtils
 *  com.gmail.nossr50.util.ItemUtils
 *  com.gmail.nossr50.util.player.UserManager
 *  com.gmail.nossr50.util.random.ProbabilityUtil
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import com.gmail.nossr50.api.AbilityAPI;
import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.api.ItemSpawnReason;
import com.gmail.nossr50.api.SkillAPI;
import com.gmail.nossr50.api.TreeFellerBlockBreakEvent;
import com.gmail.nossr50.datatypes.meta.BonusDropMeta;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.datatypes.skills.SubSkillType;
import com.gmail.nossr50.events.fake.FakePlayerFishEvent;
import com.gmail.nossr50.events.items.McMMOItemSpawnEvent;
import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.util.BlockUtils;
import com.gmail.nossr50.util.ItemUtils;
import com.gmail.nossr50.util.player.UserManager;
import com.gmail.nossr50.util.random.ProbabilityUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import net.advancedplugins.as.impl.utils.abilities.SmeltMaterial;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class McMMOHook
extends PluginHookInstance
implements Listener {
    public static final Map<Block, Long> treeFellerTelepathyBlocks = new HashMap<Block, Long>();

    public McMMOHook() {
        SchedulerUtils.runTaskTimer(() -> {
            long l = System.currentTimeMillis();
            treeFellerTelepathyBlocks.entrySet().removeIf(entry -> {
                if (l - (Long)entry.getValue() > 500L) {
                    ((Block)entry.getKey()).removeMetadata("ae-mcmmo-treefeller-tpdrops", (Plugin)ASManager.getInstance());
                    return true;
                }
                return false;
            });
        }, 20L, 20L);
    }

    public int getSkillLevel(Player player, String string) {
        return ExperienceAPI.getLevel((Player)player, (String)string);
    }

    public void addSkillExperience(Player player, String string, int n) {
        ExperienceAPI.addXP((Player)player, (String)string, (int)n, (String)"UNKNOWN");
    }

    public boolean isBleeding(Player player) {
        return AbilityAPI.isBleeding((LivingEntity)player);
    }

    public List<String> getSkills() {
        return SkillAPI.getSkills();
    }

    public boolean isTreeFellerEvent(Event event) {
        return event instanceof TreeFellerBlockBreakEvent;
    }

    public boolean isFakeBlockBreak(Event event) {
        return false;
    }

    public boolean isFakeFishEvent(Event event) {
        return event instanceof FakePlayerFishEvent;
    }

    public boolean callFakeEvent(Block block, Player player) {
        return true;
    }

    public boolean herbalismCheck(Player player, BlockBreakEvent blockBreakEvent) {
        Block block = blockBreakEvent.getBlock();
        BlockState blockState = block.getState();
        if (BlockUtils.affectedByGreenTerra((BlockState)blockState) && mcMMO.p.getSkillTools().doesPlayerHaveSkillPermission(player, PrimarySkillType.HERBALISM)) {
            this.processHerbalismBlockBreakEvent(player, blockBreakEvent);
            return true;
        }
        return false;
    }

    public void processHerbalismBlockBreakEvent(Player player, BlockBreakEvent blockBreakEvent) {
        McMMOPlayer mcMMOPlayer = UserManager.getPlayer((Player)player);
        if (mcMMOPlayer == null) {
            return;
        }
        mcMMOPlayer.getHerbalismManager().processHerbalismBlockBreakEvent(blockBreakEvent);
    }

    public void processBlockBreakEvent(Player player, BlockBreakEvent blockBreakEvent, boolean bl, boolean bl2) {
        Block block = blockBreakEvent.getBlock();
        BlockState blockState = block.getState();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (BlockUtils.affectedBySuperBreaker((BlockState)blockState) && (ItemUtils.isPickaxe((ItemStack)itemStack) || ItemUtils.isHoe((ItemStack)itemStack)) && mcMMO.p.getSkillTools().doesPlayerHaveSkillPermission(player, PrimarySkillType.MINING) && mcMMO.getChunkManager().isEligible(blockState)) {
            this.miningCheck(player, blockBreakEvent, bl, bl2);
        }
    }

    private void miningCheck(Player player, BlockBreakEvent blockBreakEvent, boolean bl, boolean bl2) {
        Block block = blockBreakEvent.getBlock();
        BlockState blockState = block.getState();
        McMMOPlayer mcMMOPlayer = UserManager.getPlayer((Player)player);
        if (mcMMOPlayer == null) {
            return;
        }
        if (ProbabilityUtil.isSkillRNGSuccessful((SubSkillType)SubSkillType.MINING_DOUBLE_DROPS, (McMMOPlayer)mcMMOPlayer)) {
            boolean bl3 = mcMMOPlayer.getAbilityMode(mcMMO.p.getSkillTools().getSuperAbility(PrimarySkillType.MINING)) && mcMMO.p.getAdvancedConfig().getAllowMiningTripleDrops();
            BlockUtils.markDropsAsBonus((Block)block, (boolean)bl3);
            if (block.getMetadata("mcMMO: Double Drops").size() > 0) {
                BonusDropMeta bonusDropMeta = (BonusDropMeta)block.getMetadata("mcMMO: Double Drops").get(0);
                int n = bonusDropMeta.asInt();
                for (ItemStack itemStack : block.getDrops()) {
                    for (int i = 0; i < n; ++i) {
                        if (bl) {
                            blockState.setMetadata("ae_mcmmoTP_DROPS", (MetadataValue)new FixedMetadataValue((Plugin)ASManager.getInstance(), (Object)true));
                        }
                        if (bl2) {
                            blockState.setMetadata("ae_mcmmoSMELT", (MetadataValue)new FixedMetadataValue((Plugin)ASManager.getInstance(), (Object)true));
                        }
                        ItemUtils.spawnItems((Player)blockBreakEvent.getPlayer(), (Location)blockState.getLocation(), (ItemStack)itemStack, (int)itemStack.getAmount(), (ItemSpawnReason)ItemSpawnReason.BONUS_DROPS);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onBonusDrop(McMMOItemSpawnEvent mcMMOItemSpawnEvent) {
        ItemStack itemStack;
        if (mcMMOItemSpawnEvent.getItemSpawnReason() != ItemSpawnReason.BONUS_DROPS) {
            return;
        }
        Block block = mcMMOItemSpawnEvent.getLocation().getBlock();
        if (block.hasMetadata("ae_mcmmoSMELT")) {
            block.removeMetadata("ae_mcmmoSMELT", (Plugin)ASManager.getInstance());
            itemStack = SmeltMaterial.material(mcMMOItemSpawnEvent.getItemStack());
            if (itemStack != null) {
                mcMMOItemSpawnEvent.setItemStack(itemStack);
                if (!block.hasMetadata("ae_mcmmoTP_DROPS")) {
                    ASManager.dropItem(mcMMOItemSpawnEvent.getLocation(), itemStack);
                    mcMMOItemSpawnEvent.getLocation().subtract(0.0, 10000.0, 0.0);
                }
            }
        }
        if (block.hasMetadata("ae_mcmmoTP_DROPS")) {
            block.removeMetadata("ae_mcmmoTP_DROPS", (Plugin)ASManager.getInstance());
            mcMMOItemSpawnEvent.getLocation().subtract(0.0, 10000.0, 0.0);
            itemStack = mcMMOItemSpawnEvent.getItemStack().clone();
            ASManager.giveItem(mcMMOItemSpawnEvent.getPlayer(), itemStack);
        }
    }

    public boolean blockHasHerbalismBonusDrops(Block block) {
        return block.hasMetadata("mcMMO: Double Drops");
    }

    public int getHerbalismBonusDropMultiplier(Block block) {
        BonusDropMeta bonusDropMeta = (BonusDropMeta)block.getMetadata("mcMMO: Double Drops").get(0);
        return bonusDropMeta.asInt();
    }

    @Override
    public String getName() {
        return "mcMMO";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @EventHandler(ignoreCancelled=true)
    public void onTelepathyTreeFellerBonusItemSpawn(McMMOItemSpawnEvent mcMMOItemSpawnEvent) {
        Block block = mcMMOItemSpawnEvent.getLocation().getBlock();
        if (!treeFellerTelepathyBlocks.containsKey(block)) {
            return;
        }
        mcMMOItemSpawnEvent.setCancelled(true);
        if (mcMMOItemSpawnEvent.getItemSpawnReason() == ItemSpawnReason.BONUS_DROPS) {
            ASManager.giveItem(mcMMOItemSpawnEvent.getPlayer(), mcMMOItemSpawnEvent.getItemStack());
        }
    }
}

