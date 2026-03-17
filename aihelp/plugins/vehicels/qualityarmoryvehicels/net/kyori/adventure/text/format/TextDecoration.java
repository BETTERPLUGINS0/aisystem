/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text.format;

import java.util.Objects;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import net.kyori.adventure.text.format.TextDecorationAndState;
import net.kyori.adventure.text.format.TextDecorationAndStateImpl;
import net.kyori.adventure.text.format.TextFormat;
import net.kyori.adventure.util.Index;
import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum TextDecoration implements StyleBuilderApplicable,
TextFormat
{
    OBFUSCATED("obfuscated"),
    BOLD("bold"),
    STRIKETHROUGH("strikethrough"),
    UNDERLINED("underlined"),
    ITALIC("italic");

    public static final Index<String, TextDecoration> NAMES;
    private final String name;

    private TextDecoration(String string2) {
        this.name = string2;
    }

    @Deprecated
    @NotNull
    public final TextDecorationAndState as(boolean bl) {
        return this.withState(bl);
    }

    @Deprecated
    @NotNull
    public final TextDecorationAndState as(@NotNull State state) {
        return this.withState(state);
    }

    @NotNull
    public final TextDecorationAndState withState(boolean bl) {
        return new TextDecorationAndStateImpl(this, State.byBoolean(bl));
    }

    @NotNull
    public final TextDecorationAndState withState(@NotNull State state) {
        return new TextDecorationAndStateImpl(this, state);
    }

    @NotNull
    public final TextDecorationAndState withState(@NotNull TriState triState) {
        return new TextDecorationAndStateImpl(this, State.byTriState(triState));
    }

    @Override
    public void styleApply(@NotNull Style.Builder builder) {
        builder.decorate(this);
    }

    @NotNull
    public String toString() {
        return this.name;
    }

    static {
        NAMES = Index.create(TextDecoration.class, textDecoration -> textDecoration.name);
    }

    public static enum State {
        NOT_SET("not_set"),
        FALSE("false"),
        TRUE("true");

        private final String name;

        private State(String string2) {
            this.name = string2;
        }

        public String toString() {
            return this.name;
        }

        @NotNull
        public static State byBoolean(boolean bl) {
            return bl ? TRUE : FALSE;
        }

        @NotNull
        public static State byBoolean(@Nullable Boolean bl) {
            return bl == null ? NOT_SET : State.byBoolean((boolean)bl);
        }

        @NotNull
        public static State byTriState(@NotNull TriState triState) {
            Objects.requireNonNull(triState);
            switch (triState) {
                case TRUE: {
                    return TRUE;
                }
                case FALSE: {
                    return FALSE;
                }
                case NOT_SET: {
                    return NOT_SET;
                }
            }
            throw new IllegalArgumentException("Unable to turn TriState: " + (Object)((Object)triState) + " into a TextDecoration.State");
        }
    }
}

