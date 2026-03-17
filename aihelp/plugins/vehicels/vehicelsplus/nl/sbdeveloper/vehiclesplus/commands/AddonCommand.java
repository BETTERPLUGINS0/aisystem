/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 *  org.bukkit.command.CommandSender
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import java.util.Map;
import nl.sbdeveloper.vehiclesplus.api.nbt.NBTColorAdapter;
import nl.sbdeveloper.vehiclesplus.api.nbt.NBTDataType;
import nl.sbdeveloper.vehiclesplus.api.vehicles.rims.RimDesign;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.ColorUtil;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

@CommandAlias(value="addon|vpaddon|vaddon|addons")
@Description(value="{@@vehiclesplus.commands.addon.descriptions.main}")
public class AddonCommand
extends BaseCommand {
    @HelpCommand
    @CatchUnknown
    @Default
    public static void onHelp(CommandSender commandSender, CommandHelp commandHelp) {
        commandHelp.showHelp();
    }

    @Subcommand(value="givepaint")
    @Description(value="{@@vehiclesplus.commands.addon.descriptions.givepaint}")
    @CommandPermission(value="vp.addon.givepaint")
    @CommandCompletion(value="@players @range:255 @range:255 @range:255")
    public void givePaint(CommandSender commandSender, OnlinePlayer onlinePlayer, @Default(value="255") Integer n, @Default(value="255") Integer n2, @Default(value="255") Integer n3) {
        ItemStack itemStack = new ItemBuilder(XMaterial.BUCKET).displayname(Locale.getMessage(PluginMessage.COMMANDS_ADDON_GIVEPAINT_ITEM_NAME)).lore(Locale.getMessage(PluginMessage.COMMANDS_ADDON_GIVEPAINT_ITEM_LORE, (Map<String, String>)Map.of((Object)"%red%", (Object)n.toString(), (Object)"%green%", (Object)n2.toString(), (Object)"%blue%", (Object)n3.toString()))).applyNBT(readWriteItemNBT -> readWriteItemNBT.setString(NBTDataType.ADDON_PAINT_COLOR.name(), NBTColorAdapter.INSTANCE.serialize(Color.fromRGB((int)n, (int)n2, (int)n3)))).getItemStack();
        onlinePlayer.getPlayer().getInventory().addItem(new ItemStack[]{itemStack});
        commandSender.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_ADDON_GIVEPAINT_GIVEN, (Map<String, String>)Map.of((Object)"%player%", (Object)onlinePlayer.getPlayer().getName())));
        onlinePlayer.getPlayer().sendMessage(Locale.getMessage(PluginMessage.COMMANDS_ADDON_GIVEPAINT_RECEIVED));
    }

    @Subcommand(value="givewheel")
    @Description(value="{@@vehiclesplus.commands.addon.descriptions.givewheel}")
    @CommandPermission(value="vp.addon.givewheel")
    @CommandCompletion(value="@players @rimdesign")
    public void giveWheel(CommandSender commandSender, OnlinePlayer onlinePlayer, RimDesign rimDesign) {
        ItemStack itemStack = new ItemBuilder(rimDesign.getSkin()).displayname(ColorUtil.__("&fWheel")).lore(ColorUtil.__("&cType: &f" + rimDesign.getName())).applyNBT(readWriteItemNBT -> readWriteItemNBT.setString(NBTDataType.ADDON_WHEEL_PART.name(), rimDesign.getName())).getItemStack();
        onlinePlayer.getPlayer().getInventory().addItem(new ItemStack[]{itemStack});
        commandSender.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_ADDON_GIVEWHEEL_GIVEN, (Map<String, String>)Map.of((Object)"%player%", (Object)onlinePlayer.getPlayer().getName())));
        onlinePlayer.getPlayer().sendMessage(Locale.getMessage(PluginMessage.COMMANDS_ADDON_GIVEWHEEL_RECEIVED));
    }
}

