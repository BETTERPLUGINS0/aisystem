/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.conversations.ConversationContext
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.conversations.prompts.garage;

import java.util.Map;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.conversations.prompts.generic.StringPrompt;
import nl.sbdeveloper.vehiclesplus.inventories.garages.VehicleGarageRoleListGUI;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RoleCreatePrompt
extends StringPrompt {
    private final Garage garage;

    public RoleCreatePrompt(Garage garage) {
        this.garage = garage;
    }

    @NotNull
    public String getPromptText(@NotNull ConversationContext conversationContext) {
        return Locale.getMessage(PluginMessage.PROMPTS_GARAGE_ROLE_CREATE);
    }

    @Override
    @Nullable
    public Prompt acceptFullyValidatedInput(@NotNull ConversationContext conversationContext, @Nullable String string) {
        if (string == null) {
            return END_OF_CONVERSATION;
        }
        Player player = (Player)conversationContext.getForWhom();
        this.garage.addRole(string);
        this.garage.save();
        conversationContext.getForWhom().sendRawMessage(Locale.getMessage(PluginMessage.PROMPTS_GARAGE_ROLE_CREATED, (Map<String, String>)Map.of((Object)"%role%", (Object)string)));
        new VehicleGarageRoleListGUI(player, this.garage);
        return END_OF_CONVERSATION;
    }
}

