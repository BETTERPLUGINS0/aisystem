/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.conversations.Conversable
 *  org.bukkit.conversations.ConversationFactory
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package nl.sbdeveloper.vehiclesplus.conversations;

import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ChatConversation
extends ConversationFactory {
    public ChatConversation(Prompt prompt) {
        super((Plugin)VehiclesPlus.getInstance());
        this.withModality(true);
        this.withFirstPrompt(prompt);
        this.withEscapeSequence("cancel");
        this.withTimeout(15);
        this.withLocalEcho(false);
    }

    public void begin(Player player) {
        this.buildConversation((Conversable)player).begin();
    }
}

