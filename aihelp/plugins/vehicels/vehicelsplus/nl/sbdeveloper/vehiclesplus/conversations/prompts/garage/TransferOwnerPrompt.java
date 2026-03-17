/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.conversations.ConversationContext
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.conversations.prompts.garage;

import co.aikar.commands.MessageKeys;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.conversations.prompts.generic.StringPrompt;
import nl.sbdeveloper.vehiclesplus.inventories.garages.VehicleGarageGUI;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TransferOwnerPrompt
extends StringPrompt {
    private final Garage garage;

    public TransferOwnerPrompt(Garage garage) {
        this.garage = garage;
    }

    @NotNull
    public String getPromptText(@NotNull ConversationContext conversationContext) {
        return Locale.getMessage(PluginMessage.PROMPTS_GARAGE_TRANSFER);
    }

    @Override
    protected Prompt acceptFullyValidatedInput(@NotNull ConversationContext conversationContext, @Nullable String string) {
        if (string == null) {
            return END_OF_CONVERSATION;
        }
        Player player = Bukkit.getPlayer((String)string);
        if (player == null) {
            conversationContext.getForWhom().sendRawMessage(Locale.getMessage(MessageKeys.COULD_NOT_FIND_PLAYER));
            return END_OF_CONVERSATION;
        }
        this.garage.setOwner(player);
        this.garage.save();
        new VehicleGarageGUI((Player)conversationContext.getForWhom(), this.garage);
        return END_OF_CONVERSATION;
    }
}

