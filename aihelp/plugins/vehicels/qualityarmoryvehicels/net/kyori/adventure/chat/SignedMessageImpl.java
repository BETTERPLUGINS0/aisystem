/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.chat;

import java.security.SecureRandom;
import java.time.Instant;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class SignedMessageImpl
implements SignedMessage {
    static final SecureRandom RANDOM = new SecureRandom();
    private final Instant instant = Instant.now();
    private final long salt = RANDOM.nextLong();
    private final String message;
    private final Component unsignedContent;

    SignedMessageImpl(String string, Component component) {
        this.message = string;
        this.unsignedContent = component;
    }

    @Override
    @NotNull
    public Instant timestamp() {
        return this.instant;
    }

    @Override
    public long salt() {
        return this.salt;
    }

    @Override
    public SignedMessage.Signature signature() {
        return null;
    }

    @Override
    @Nullable
    public Component unsignedContent() {
        return this.unsignedContent;
    }

    @Override
    @NotNull
    public String message() {
        return this.message;
    }

    @Override
    @NotNull
    public Identity identity() {
        return Identity.nil();
    }

    static final class SignatureImpl
    implements SignedMessage.Signature {
        final byte[] signature;

        SignatureImpl(byte[] byArray) {
            this.signature = byArray;
        }

        @Override
        public byte[] bytes() {
            return this.signature;
        }
    }
}

