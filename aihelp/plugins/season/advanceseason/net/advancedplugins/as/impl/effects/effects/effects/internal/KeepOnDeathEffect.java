/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.advancedplugins.ae.utils.YamlFile
 *  org.bukkit.GameRule
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.advancedplugins.ae.utils.YamlFile;
import net.advancedplugins.as.impl.effects.armorutils.ArmorType;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.effects.effects.settings.SettingValues;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.ColorUtils;
import net.advancedplugins.as.impl.utils.Pair;
import net.advancedplugins.as.impl.utils.RemoveDeathItems;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.plugins.SlimeFunHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.TownyHook;
import net.advancedplugins.as.impl.utils.nbt.NBTapi;
import org.bukkit.GameRule;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class KeepOnDeathEffect
extends AdvancedEffect {
    public static String holyWhiteScrollDisplayLore;
    public static HashMap<UUID, List<ItemStack>> returnItems;
    public static List<UUID> uuids;
    public static final Cache<Player, List<Pair<ItemStack, Integer>>> DEATH_QUEUE;
    public static final Cache<LivingEntity, Boolean> DEAD_PLAYERS;

    public KeepOnDeathEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "KEEP_ON_DEATH", "Keep item on death", "%e");
        this.addArgument(0, ArmorType.class);
    }

    public static void handleDeath(Player player, PlayerDeathEvent playerDeathEvent) {
        boolean bl;
        boolean bl2 = bl = ASManager.notNullAndTrue((Boolean)player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY)) || playerDeathEvent.getKeepInventory();
        if (HooksHandler.isEnabled(HookPlugin.TOWNY) && ((TownyHook)HooksHandler.getHook(HookPlugin.TOWNY)).hasKeepInventory(player)) {
            bl = true;
        }
        if (bl) {
            return;
        }
        if (SettingValues.getHolyWhitescrollWorldBlacklist().contains(player.getWorld().getName())) {
            return;
        }
        ArrayList<ItemStack> arrayList = new ArrayList<ItemStack>();
        for (ItemStack itemStack : new ArrayList(playerDeathEvent.getDrops())) {
            if (!ASManager.isValid(itemStack) || !NBTapi.contains("holywhitescrolled", itemStack)) continue;
            try {
                player.getInventory().remove(itemStack.clone());
            } catch (Exception exception) {
                // empty catch block
            }
            playerDeathEvent.getDrops().remove(itemStack.clone());
            if (HooksHandler.isEnabled(HookPlugin.ADVANCEDENCHANTMENTS) && YamlFile.CONFIG.getBoolean("items.holywhitescroll.settings.keep-after-death", false)) {
                arrayList.add(itemStack);
                continue;
            }
            itemStack = KeepOnDeathEffect.removeHolyWhiteScroll(itemStack);
            arrayList.add(itemStack);
        }
        if (arrayList.isEmpty()) {
            return;
        }
        returnItems.put(player.getUniqueId(), arrayList);
    }

    public static ItemStack removeHolyWhiteScroll(ItemStack itemStack) {
        itemStack = NBTapi.removeTag("holywhitescrolled", itemStack);
        ItemMeta itemMeta = itemStack.getItemMeta();
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string : itemMeta.getLore()) {
            if (ColorUtils.stripColor(string).equalsIgnoreCase(ColorUtils.stripColor(holyWhiteScrollDisplayLore))) continue;
            arrayList.add(string);
        }
        itemMeta.setLore(arrayList);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static List<UUID> getUUIDs() {
        return uuids;
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (executionTask.getActionExecution().getAllEffectsRaw().contains("REVIVE")) {
            return true;
        }
        Player player = (Player)livingEntity;
        if (DEAD_PLAYERS.getIfPresent(player) != null) {
            return true;
        }
        ItemStack itemStack = executionTask.getBuilder().getItem().clone();
        if (HooksHandler.isEnabled(HookPlugin.SLIMEFUN) && ((SlimeFunHook)HooksHandler.getHook(HookPlugin.SLIMEFUN)).hasSoulbound(itemStack, player.getWorld())) {
            return true;
        }
        boolean bl = ASManager.notNullAndTrue((Boolean)player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY));
        if (bl) {
            return true;
        }
        int n = (int)Arrays.stream(player.getInventory().getContents()).filter(itemStack2 -> itemStack2 != null && itemStack.isSimilar(itemStack2) && itemStack.getAmount() == itemStack2.getAmount()).count();
        DEATH_QUEUE.get(player, ArrayList::new).add(new Pair<ItemStack, Integer>(itemStack, n));
        RemoveDeathItems.add(player.getUniqueId(), itemStack);
        return true;
    }

    private void setUniqueIdentifier(ItemStack itemStack) {
        UUID uUID = UUID.randomUUID();
        KeepOnDeathEffect.getUUIDs().add(uUID);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null && itemMeta.hasLore()) {
            ArrayList<String> arrayList = new ArrayList<String>(itemMeta.getLore());
            arrayList.add(uUID.toString());
            itemMeta.setLore(arrayList);
            itemStack.setItemMeta(itemMeta);
        }
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
    public void onDeathQueue(PlayerDeathEvent playerDeathEvent) {
        Player player = playerDeathEvent.getEntity();
        List<Pair<ItemStack, Integer>> list = DEATH_QUEUE.getIfPresent(player);
        if (list != null && player.isDead() && !player.isValid()) {
            boolean bl;
            DEATH_QUEUE.invalidate(player);
            ArrayList<ItemStack> arrayList = KeepOnDeathEffect.getReturnItems().containsKey(player.getUniqueId()) ? KeepOnDeathEffect.getReturnItems().get(player.getUniqueId()) : new ArrayList<ItemStack>();
            boolean bl2 = bl = ASManager.notNullAndTrue((Boolean)player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY)) || playerDeathEvent.getKeepInventory();
            if (bl) {
                return;
            }
            for (Pair<ItemStack, Integer> pair : list) {
                ItemStack itemStack = pair.getKey();
                int n = pair.getValue();
                player.getInventory().removeItem(new ItemStack[]{itemStack});
                if (n > 1) {
                    this.setUniqueIdentifier(itemStack);
                    player.updateInventory();
                }
                for (int i = 0; i < n; ++i) {
                    arrayList.add(itemStack);
                }
            }
            KeepOnDeathEffect.getReturnItems().put(player.getUniqueId(), arrayList);
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent playerDeathEvent) {
        KeepOnDeathEffect.handleDeath(playerDeathEvent.getEntity(), playerDeathEvent);
        if (playerDeathEvent.getEntity().hasMetadata("lavaWalker") || playerDeathEvent.getEntity().hasMetadata("waterWalker")) {
            playerDeathEvent.getEntity().removeMetadata("lavaWalker", (Plugin)EffectsHandler.getInstance());
            playerDeathEvent.getEntity().removeMetadata("waterWalker", (Plugin)EffectsHandler.getInstance());
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onRespawn(PlayerRespawnEvent playerRespawnEvent) {
        DEAD_PLAYERS.invalidate(playerRespawnEvent.getPlayer());
        Player player = playerRespawnEvent.getPlayer();
        List<ItemStack> list = KeepOnDeathEffect.getReturnItems().get(player.getUniqueId());
        if (list == null) {
            return;
        }
        for (ItemStack itemStack : list) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null || !itemMeta.hasLore()) continue;
            List list2 = itemMeta.getLore();
            for (UUID uUID : KeepOnDeathEffect.getUUIDs()) {
                for (String string : list2) {
                    if (!string.contains(uUID.toString())) continue;
                    ArrayList arrayList = new ArrayList(list2);
                    arrayList.remove(uUID.toString());
                    itemMeta.setLore(arrayList);
                    itemStack.setItemMeta(itemMeta);
                }
            }
        }
        ASManager.giveItem(player, list.toArray(new ItemStack[0]));
        KeepOnDeathEffect.getReturnItems().remove(player.getUniqueId());
        KeepOnDeathEffect.getUUIDs().clear();
    }

    public static HashMap<UUID, List<ItemStack>> getReturnItems() {
        return returnItems;
    }

    static {
        returnItems = new HashMap();
        uuids = new ArrayList<UUID>();
        DEATH_QUEUE = CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.SECONDS).build();
        DEAD_PLAYERS = CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.MINUTES).build();
    }
}

