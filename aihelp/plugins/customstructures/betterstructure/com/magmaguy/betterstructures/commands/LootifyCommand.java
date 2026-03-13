/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.magmaguy.betterstructures.commands;

import com.magmaguy.betterstructures.config.treasures.TreasureConfig;
import com.magmaguy.betterstructures.config.treasures.TreasureConfigFields;
import com.magmaguy.betterstructures.util.ItemStackSerialization;
import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.command.CommandData;
import com.magmaguy.magmacore.command.arguments.IntegerCommandArgument;
import com.magmaguy.magmacore.command.arguments.ListStringCommandArgument;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LootifyCommand
extends AdvancedCommand {
    public LootifyCommand() {
        super(List.of((Object)"lootify"));
        ArrayList<String> treasures = new ArrayList<String>(TreasureConfig.getTreasureConfigurations().keySet());
        this.addArgument("generator", new ListStringCommandArgument(treasures, "<treasures>"));
        this.addArgument("rarity", new ListStringCommandArgument("<rarity>"));
        this.addArgument("minAmount", new IntegerCommandArgument("<minAmount>"));
        this.addArgument("maxAmount", new IntegerCommandArgument("<maxAmount>"));
        this.addArgument("weight", new IntegerCommandArgument("<weight>"));
        this.setPermission("betterstructures.*");
        this.setUsage("/betterstructures lootify <generator> <rarity> <minAmount> <maxAmount> <weight>");
        this.setDescription("Adds a held item to the loot settings of a generator");
    }

    @Override
    public void execute(CommandData commandData) {
        this.lootify(commandData.getStringArgument("generator"), commandData.getStringArgument("rarity"), commandData.getStringArgument("minAmount"), commandData.getStringArgument("maxAmount"), commandData.getStringArgument("weight"), commandData.getPlayerSender());
    }

    private void lootify(String generator, String rarity, String minAmount, String maxAmount, String weight, Player player) {
        double weightDouble;
        int maxAmountInt;
        int minAmountInt;
        TreasureConfigFields treasureConfigFields = TreasureConfig.getConfigFields(generator);
        if (treasureConfigFields == null) {
            player.sendMessage("[BetterStructures] Not a valid generator! Try again.");
            return;
        }
        if (treasureConfigFields.getRawLoot().get(rarity) == null) {
            player.sendMessage("[BetterStructures] Not a valid rarity! Try again.");
            return;
        }
        try {
            minAmountInt = Integer.parseInt(minAmount);
        } catch (Exception exception) {
            player.sendMessage("[BetterStructures] Not a valid minimum amount! Try again.");
            return;
        }
        if (minAmountInt < 1) {
            player.sendMessage("[BetterStructures] Minimum amount should not be less than 1! This value will not be saved.");
            return;
        }
        try {
            maxAmountInt = Integer.parseInt(maxAmount);
        } catch (Exception exception) {
            player.sendMessage("[BetterStructures] Not a valid maximum amount! Try again.");
            return;
        }
        if (maxAmountInt > 64) {
            player.sendMessage("[BetterStructures] Maximum amount should not be more than 64! If you want more than one stack, make multiple entries. This value will not be saved.");
            return;
        }
        try {
            weightDouble = Double.parseDouble(weight);
        } catch (Exception exception) {
            player.sendMessage("[BetterStructures] Not a valid weight! Try again.");
            return;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getType().isAir()) {
            player.sendMessage("[BetterStructures] You need to be holding an item in order to register the item you're holding! This value will not be saved.");
            return;
        }
        String info = itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName().replace(" ", "_") : (itemStack.hasItemMeta() && itemStack.getItemMeta().hasItemName() ? itemStack.getItemMeta().getItemName() : itemStack.getType().toString());
        HashMap<String, Object> configMap = new HashMap<String, Object>();
        configMap.put("serialized", ItemStackSerialization.deserializeItem(itemStack));
        configMap.put("amount", minAmount + "-" + maxAmount);
        configMap.put("weight", weightDouble);
        configMap.put("info", info);
        treasureConfigFields.addChestEntry(configMap, rarity, player);
    }
}

