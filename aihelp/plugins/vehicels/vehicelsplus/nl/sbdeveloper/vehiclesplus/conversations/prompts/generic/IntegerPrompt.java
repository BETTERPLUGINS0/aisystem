/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.conversations.ConversationContext
 *  org.bukkit.conversations.Prompt
 */
package nl.sbdeveloper.vehiclesplus.conversations.prompts.generic;

import nl.sbdeveloper.vehiclesplus.conversations.prompts.generic.CancellablePrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class IntegerPrompt
extends CancellablePrompt {
    @Override
    protected boolean isInputFullyValid(@NotNull ConversationContext conversationContext, @NotNull String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    @Override
    @Nullable
    protected Prompt acceptFullyValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String string) {
        return this.acceptFullyValidatedIntegerInput(conversationContext, Integer.parseInt(string));
    }

    protected abstract Prompt acceptFullyValidatedIntegerInput(@NotNull ConversationContext var1, @Nullable Integer var2);
}

