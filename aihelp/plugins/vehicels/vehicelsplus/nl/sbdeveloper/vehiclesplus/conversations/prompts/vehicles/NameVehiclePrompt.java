/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.conversations.ConversationContext
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.conversations.prompts.vehicles;

import java.util.Map;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.conversations.prompts.generic.StringPrompt;
import nl.sbdeveloper.vehiclesplus.handlers.EconomyAdapter;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NameVehiclePrompt
extends StringPrompt {
    private final DrivableVehicle vehicle;

    public NameVehiclePrompt(DrivableVehicle drivableVehicle) {
        this.vehicle = drivableVehicle;
    }

    @NotNull
    public String getPromptText(@NotNull ConversationContext conversationContext) {
        return Locale.getMessage(PluginMessage.PROMPTS_VEHICLES_SETNAME);
    }

    @Override
    @Nullable
    public Prompt acceptFullyValidatedInput(@NotNull ConversationContext conversationContext, @Nullable String string) {
        if (string == null) {
            return END_OF_CONVERSATION;
        }
        if (!EconomyAdapter.withdraw((Player)conversationContext.getForWhom(), VehiclesPlus.getStorage().getConfig().getRenameCost())) {
            return END_OF_CONVERSATION;
        }
        this.vehicle.getStorageVehicle().setDisplayName(string);
        this.vehicle.getStorageVehicle().save();
        conversationContext.getForWhom().sendRawMessage(Locale.getMessage(PluginMessage.PROMPTS_VEHICLES_RENAMED, (Map<String, String>)Map.of((Object)"%name%", (Object)ChatColor.translateAlternateColorCodes((char)'&', (String)string))));
        return END_OF_CONVERSATION;
    }
}

