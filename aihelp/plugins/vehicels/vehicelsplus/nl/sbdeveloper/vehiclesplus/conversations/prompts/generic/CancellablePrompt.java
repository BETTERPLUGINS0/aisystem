/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.conversations.ConversationContext
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.conversations.ValidatingPrompt
 */
package nl.sbdeveloper.vehiclesplus.conversations.prompts.generic;

import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CancellablePrompt
extends ValidatingPrompt {
    protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String string) {
        return string.equalsIgnoreCase("stop") || this.isInputFullyValid(conversationContext, string);
    }

    protected abstract boolean isInputFullyValid(@NotNull ConversationContext var1, @NotNull String var2);

    @Nullable
    protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String string) {
        if (string.equalsIgnoreCase("stop")) {
            conversationContext.getForWhom().sendRawMessage(Locale.getMessage(PluginMessage.PROMPTS_GENERAL_CANCEL));
            return END_OF_CONVERSATION;
        }
        return this.acceptFullyValidatedInput(conversationContext, string);
    }

    protected abstract Prompt acceptFullyValidatedInput(@NotNull ConversationContext var1, @Nullable String var2);
}

