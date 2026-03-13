/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.block.Biome
 *  org.bukkit.block.Block
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.commands.sub;

import net.advancedplugins.as.impl.utils.commands.SimpleCommand;
import net.advancedplugins.as.impl.utils.text.Text;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.biomes.AdvancedBiomeBase;
import net.advancedplugins.seasons.commands.sub.ASSubCommand;
import net.advancedplugins.seasons.enums.Season;
import net.advancedplugins.seasons.handlers.sub.BlockProcessHandler;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockInfoSub
extends ASSubCommand {
    public BlockInfoSub(JavaPlugin javaPlugin, SimpleCommand simpleCommand) {
        super(javaPlugin, "advancedseasons.admin", simpleCommand, false);
        this.setDescription("Info of block in front of player");
        this.addFlat("blockinfo");
    }

    @Override
    public void onExecute(CommandSender commandSender, String[] stringArray) {
        Player player = (Player)commandSender;
        Block block = player.getTargetBlock(null, 5);
        if (block == null || block.getType().isAir()) {
            block = player.getLocation().getBlock();
        }
        Biome biome = block.getBiome();
        AdvancedBiomeBase advancedBiomeBase = Core.getBiomesHandler().getAdvancedBiomeAt(block.getLocation()).orElse(null);
        Season season = Core.getSeasonHandler().getSeason(player.getWorld().getName());
        player.sendMessage(Text.modify("&cInformation of block in front of you:"));
        player.sendMessage(Text.modify(" &f- type: " + String.valueOf(block.getType())));
        player.sendMessage(Text.modify(" &f- vanilla biome: " + biome.name()));
        player.sendMessage(Text.modify(" &f- custom biome: " + (advancedBiomeBase != null ? advancedBiomeBase.getName() : "no custom biome")));
        player.sendMessage(Text.modify(" &f- sub/season: " + season.name() + "/" + season.getType().name()));
        player.sendMessage(Text.modify(" &f- chunk data: " + String.valueOf(block.getChunk().getPersistentDataContainer().get(BlockProcessHandler.getKEY_CHUNK_STAGE(), PersistentDataType.INTEGER)) + "/" + (String)block.getChunk().getPersistentDataContainer().get(BlockProcessHandler.getKEY_CHUNK_STAGE_SEASON(), PersistentDataType.STRING)));
        Core.getBiomesHandler().getRenderHandler().refreshVisualBiomes(true);
    }
}

