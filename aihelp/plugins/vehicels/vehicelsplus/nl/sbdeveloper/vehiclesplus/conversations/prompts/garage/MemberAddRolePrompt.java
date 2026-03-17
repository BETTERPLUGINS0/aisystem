/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.conversations.ConversationContext
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.conversations.prompts.garage;

import java.util.Optional;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.garages.permissions.GarageMember;
import nl.sbdeveloper.vehiclesplus.api.garages.permissions.GarageRole;
import nl.sbdeveloper.vehiclesplus.conversations.prompts.generic.StringPrompt;
import nl.sbdeveloper.vehiclesplus.inventories.garages.VehicleGarageMemberListGUI;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MemberAddRolePrompt
extends StringPrompt {
    private final Garage garage;
    private final GarageMember member;

    public MemberAddRolePrompt(Garage garage, GarageMember garageMember) {
        this.garage = garage;
        this.member = garageMember;
    }

    @NotNull
    public String getPromptText(@NotNull ConversationContext conversationContext) {
        return Locale.getMessage(PluginMessage.PROMPTS_GARAGE_ROLE_ADDMEMBER);
    }

    @Override
    @Nullable
    public Prompt acceptFullyValidatedInput(@NotNull ConversationContext conversationContext, @Nullable String string) {
        if (string == null) {
            return END_OF_CONVERSATION;
        }
        Player player = (Player)conversationContext.getForWhom();
        Optional<GarageRole> optional = this.garage.getRole(string);
        if (optional.isEmpty()) {
            conversationContext.getForWhom().sendRawMessage(String.valueOf(ChatColor.RED) + "This role does not exist yet.");
            return END_OF_CONVERSATION;
        }
        this.member.setGarageRole(string);
        this.garage.save();
        conversationContext.getForWhom().sendRawMessage(String.valueOf(ChatColor.GREEN) + "The role has been successfully added to the member.");
        new VehicleGarageMemberListGUI(player, this.garage);
        return END_OF_CONVERSATION;
    }
}

