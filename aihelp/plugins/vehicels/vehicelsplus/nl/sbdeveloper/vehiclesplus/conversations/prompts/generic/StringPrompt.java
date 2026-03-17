/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.conversations.ConversationContext
 */
package nl.sbdeveloper.vehiclesplus.conversations.prompts.generic;

import nl.sbdeveloper.vehiclesplus.conversations.prompts.generic.CancellablePrompt;
import org.bukkit.conversations.ConversationContext;
import org.jetbrains.annotations.NotNull;

public abstract class StringPrompt
extends CancellablePrompt {
    @Override
    protected boolean isInputFullyValid(@NotNull ConversationContext conversationContext, @NotNull String string) {
        return true;
    }
}

