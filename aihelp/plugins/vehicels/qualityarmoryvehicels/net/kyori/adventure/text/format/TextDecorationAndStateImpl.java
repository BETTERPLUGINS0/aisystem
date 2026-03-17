/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text.format;

import java.util.Objects;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecorationAndState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class TextDecorationAndStateImpl
implements TextDecorationAndState {
    private final TextDecoration decoration;
    private final TextDecoration.State state;

    TextDecorationAndStateImpl(TextDecoration textDecoration, TextDecoration.State state) {
        this.decoration = textDecoration;
        this.state = Objects.requireNonNull(state, "state");
    }

    @Override
    @NotNull
    public TextDecoration decoration() {
        return this.decoration;
    }

    @Override
    public @NotNull TextDecoration.State state() {
        return this.state;
    }

    public String toString() {
        return Internals.toString(this);
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        TextDecorationAndStateImpl textDecorationAndStateImpl = (TextDecorationAndStateImpl)object;
        return this.decoration == textDecorationAndStateImpl.decoration && this.state == textDecorationAndStateImpl.state;
    }

    public int hashCode() {
        int n = this.decoration.hashCode();
        n = 31 * n + this.state.hashCode();
        return n;
    }
}

