/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands;

import co.aikar.commands.CommandManager;
import co.aikar.commands.MessageKeys;
import co.aikar.commands.MessageType;
import co.aikar.locales.MessageKey;
import co.aikar.locales.MessageKeyProvider;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface CommandIssuer {
    public <T> T getIssuer();

    public CommandManager getManager();

    public boolean isPlayer();

    default public void sendMessage(String message) {
        this.getManager().sendMessage(this, MessageType.INFO, (MessageKeyProvider)MessageKeys.INFO_MESSAGE, "{message}", message);
    }

    @NotNull
    public UUID getUniqueId();

    public boolean hasPermission(String var1);

    default public void sendError(MessageKeyProvider key, String ... replacements) {
        this.sendMessage(MessageType.ERROR, key.getMessageKey(), replacements);
    }

    default public void sendSyntax(MessageKeyProvider key, String ... replacements) {
        this.sendMessage(MessageType.SYNTAX, key.getMessageKey(), replacements);
    }

    default public void sendInfo(MessageKeyProvider key, String ... replacements) {
        this.sendMessage(MessageType.INFO, key.getMessageKey(), replacements);
    }

    default public void sendError(MessageKey key, String ... replacements) {
        this.sendMessage(MessageType.ERROR, key, replacements);
    }

    default public void sendSyntax(MessageKey key, String ... replacements) {
        this.sendMessage(MessageType.SYNTAX, key, replacements);
    }

    default public void sendInfo(MessageKey key, String ... replacements) {
        this.sendMessage(MessageType.INFO, key, replacements);
    }

    default public void sendMessage(MessageType type, MessageKeyProvider key, String ... replacements) {
        this.sendMessage(type, key.getMessageKey(), replacements);
    }

    default public void sendMessage(MessageType type, MessageKey key, String ... replacements) {
        this.getManager().sendMessage(this, type, (MessageKeyProvider)key, replacements);
    }

    @Deprecated
    public void sendMessageInternal(String var1);
}

