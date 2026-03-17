/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.conversations.BooleanPrompt
 *  org.bukkit.conversations.ConversationContext
 *  org.bukkit.conversations.Prompt
 */
package nl.sbdeveloper.vehiclesplus.conversations.prompts.generic;

import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ConfirmationPrompt
extends BooleanPrompt {
    protected abstract void onSuccess();

    protected abstract void onCancel();

    @Nullable
    protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, boolean bl) {
        if (bl) {
            this.onSuccess();
        } else {
            this.onCancel();
        }
        return END_OF_CONVERSATION;
    }
}

