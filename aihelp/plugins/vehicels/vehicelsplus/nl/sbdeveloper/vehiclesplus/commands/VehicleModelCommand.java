/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.ComponentBuilder
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  net.md_5.bungee.api.chat.hover.content.Content
 *  net.md_5.bungee.api.chat.hover.content.Text
 *  org.bukkit.command.CommandSender
 *  org.bukkit.conversations.ConversationContext
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.entity.Player
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
import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.logging.Level;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.Part;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.Setting;
import nl.sbdeveloper.vehiclesplus.conversations.ChatConversation;
import nl.sbdeveloper.vehiclesplus.conversations.prompts.generic.ConfirmationPrompt;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.MainUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandAlias(value="vehiclemodel|vmodel|vm")
@Description(value="{@@vehiclesplus.commands.vehiclemodel.descriptions.main}")
public class VehicleModelCommand
extends BaseCommand {
    private static final String vehicleModelPlaceholder = "Model: %%__USER__%%";
    private static final String licensePlaceholder1 = "License 1: %%__LICENSE_1__%%";
    private static final String licensePlaceholder2 = "License 2: %%__LICENSE_2__%%";
    private static final String userPlaceholder = "User: %%__USER__%%";
    private static final String noncePlaceholder = "Nonce: %%__NONCE__%%";

    @HelpCommand
    @CatchUnknown
    @Default
    public static void onHelp(CommandSender commandSender, CommandHelp commandHelp) {
        commandHelp.showHelp();
    }

    @Subcommand(value="delete")
    @Description(value="{@@vehiclesplus.commands.vehiclemodel.descriptions.delete}")
    @CommandPermission(value="vp.model.delete")
    @CommandCompletion(value="@vehiclemodels")
    public void onDelete(final Player player, final VehicleModel vehicleModel) {
        new ChatConversation((Prompt)new ConfirmationPrompt(){

            @NotNull
            public String getPromptText(@NotNull ConversationContext conversationContext) {
                return Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_DELETE_CONFIRM, (Map<String, String>)Map.of((Object)"%model%", (Object)vehicleModel.getId()));
            }

            @Override
            protected void onSuccess() {
                VehiclesPlusAPI.getVehicleModels().remove(vehicleModel.getId());
                File file = new File(String.valueOf(VehiclesPlus.getInstance().getDataFolder()) + "/vehicles/" + vehicleModel.getTypeId(), vehicleModel.getId() + ".hjson");
                if (!file.exists()) {
                    VehiclesPlus.getInstance().getLogger().log(Level.WARNING, "Could not remove the vehicle model " + vehicleModel.getId() + ", because the file does not exists!");
                    return;
                }
                player.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_DELETE_DELETED, (Map<String, String>)Map.of((Object)"%model%", (Object)vehicleModel.getId())));
            }

            @Override
            protected void onCancel() {
                player.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_DELETE_CANCELLED));
            }
        }).begin(player);
    }

    @Subcommand(value="list")
    @Description(value="{@@vehiclesplus.commands.vehiclemodel.descriptions.list}")
    @CommandPermission(value="vp.model.list")
    public void onList(CommandSender commandSender) {
        for (VehicleModel vehicleModel : VehiclesPlusAPI.getVehicleModels().values()) {
            TextComponent textComponent = new TextComponent(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_LIST_TITLE, (Map<String, String>)Map.of((Object)"%name%", (Object)vehicleModel.getDisplayNameColored(), (Object)"%type%", (Object)vehicleModel.getTypeId())));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/vmodel info " + vehicleModel.getId()));
            if (XMaterial.supports(13)) {
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Content[]{new Text(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_LIST_HOVER))}));
            } else {
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_LIST_HOVER)).create()));
            }
            commandSender.spigot().sendMessage((BaseComponent)textComponent);
        }
    }

    @Subcommand(value="info")
    @Description(value="{@@vehiclesplus.commands.vehiclemodel.descriptions.info}")
    @CommandPermission(value="vp.model.info")
    @CommandCompletion(value="@vehiclemodels")
    public void displayInfo(CommandSender commandSender, VehicleModel vehicleModel) {
        commandSender.spigot().sendMessage((BaseComponent)new TextComponent(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_NAME, (Map<String, String>)Map.of((Object)"%name%", (Object)vehicleModel.getDisplayNameColored()))));
        commandSender.spigot().sendMessage((BaseComponent)new TextComponent(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_TYPE, (Map<String, String>)Map.of((Object)"%type%", (Object)vehicleModel.getTypeId()))));
        commandSender.spigot().sendMessage((BaseComponent)new TextComponent(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_PARTS_HEADER)));
        for (Part part : vehicleModel.getParts()) {
            TextComponent textComponent = new TextComponent(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_PARTS_PART, (Map<String, String>)Map.of((Object)"%part%", (Object)part.getClass().getSimpleName())));
            if (XMaterial.supports(13)) {
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Content[]{new Text(part.toString().trim())}));
            } else {
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(part.toString().trim()).create()));
            }
            commandSender.spigot().sendMessage((BaseComponent)textComponent);
        }
        commandSender.spigot().sendMessage((BaseComponent)new TextComponent(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_HEADER)));
        Arrays.stream(vehicleModel.getClass().getDeclaredFields()).sorted(Comparator.comparing(Field::getName)).forEachOrdered(field -> {
            if (Setting.class.isAssignableFrom(field.getType())) {
                Setting setting;
                field.setAccessible(true);
                try {
                    setting = (Setting)field.get(vehicleModel);
                } catch (IllegalAccessException illegalAccessException) {
                    VehiclesPlus.getInstance().getLogger().log(Level.WARNING, "Could not get the setting " + field.getName() + " from the vehicle model " + vehicleModel.getId() + "!", illegalAccessException);
                    return;
                }
                if (setting == null) {
                    return;
                }
                TextComponent textComponent = new TextComponent(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_SETTING, (Map<String, String>)Map.of((Object)"%setting%", (Object)MainUtil.capitalize(field.getName()))));
                if (XMaterial.supports(13)) {
                    textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Content[]{new Text(setting.toString().trim())}));
                } else {
                    textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(setting.toString().trim()).create()));
                }
                commandSender.spigot().sendMessage((BaseComponent)textComponent);
            }
        });
        commandSender.spigot().sendMessage((BaseComponent)new TextComponent(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_DRIFT, (Map<String, String>)Map.of((Object)"%drift%", (Object)String.valueOf(vehicleModel.isDrift())))));
        commandSender.spigot().sendMessage((BaseComponent)new TextComponent(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_EXITWHILEMOVING, (Map<String, String>)Map.of((Object)"%exitwhilemoving%", (Object)String.valueOf(vehicleModel.isExitWhileMoving())))));
        commandSender.spigot().sendMessage((BaseComponent)new TextComponent(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_PRICE, (Map<String, String>)Map.of((Object)"%price%", (Object)String.valueOf(vehicleModel.getPrice())))));
        commandSender.spigot().sendMessage((BaseComponent)new TextComponent(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_HEALTH, (Map<String, String>)Map.of((Object)"%health%", (Object)String.valueOf(vehicleModel.getHealth())))));
        commandSender.spigot().sendMessage((BaseComponent)new TextComponent(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_TRUNKSIZE, (Map<String, String>)Map.of((Object)"%trunksize%", (Object)String.valueOf(vehicleModel.getTrunkSize())))));
    }
}

