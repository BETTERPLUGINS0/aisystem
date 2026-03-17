/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text.serializer.legacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.format.TextFormat;
import net.kyori.adventure.text.serializer.legacy.CharacterAndFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class CharacterAndFormatImpl
implements CharacterAndFormat {
    private final char character;
    private final TextFormat format;
    private final boolean caseInsensitive;

    CharacterAndFormatImpl(char c, @NotNull TextFormat textFormat, boolean bl) {
        this.character = c;
        this.format = Objects.requireNonNull(textFormat, "format");
        this.caseInsensitive = bl;
    }

    @Override
    public char character() {
        return this.character;
    }

    @Override
    @NotNull
    public TextFormat format() {
        return this.format;
    }

    @Override
    public boolean caseInsensitive() {
        return this.caseInsensitive;
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof CharacterAndFormatImpl)) {
            return false;
        }
        CharacterAndFormatImpl characterAndFormatImpl = (CharacterAndFormatImpl)object;
        return this.character == characterAndFormatImpl.character && this.format.equals(characterAndFormatImpl.format) && this.caseInsensitive == characterAndFormatImpl.caseInsensitive;
    }

    public int hashCode() {
        int n = this.character;
        n = 31 * n + this.format.hashCode();
        n = 31 * n + Boolean.hashCode(this.caseInsensitive);
        return n;
    }

    @NotNull
    public String toString() {
        return Internals.toString(this);
    }

    static final class Defaults {
        static final List<CharacterAndFormat> DEFAULTS = Defaults.createDefaults();

        private Defaults() {
        }

        static List<CharacterAndFormat> createDefaults() {
            ArrayList<CharacterAndFormat> arrayList = new ArrayList<CharacterAndFormat>(22);
            arrayList.add(CharacterAndFormat.BLACK);
            arrayList.add(CharacterAndFormat.DARK_BLUE);
            arrayList.add(CharacterAndFormat.DARK_GREEN);
            arrayList.add(CharacterAndFormat.DARK_AQUA);
            arrayList.add(CharacterAndFormat.DARK_RED);
            arrayList.add(CharacterAndFormat.DARK_PURPLE);
            arrayList.add(CharacterAndFormat.GOLD);
            arrayList.add(CharacterAndFormat.GRAY);
            arrayList.add(CharacterAndFormat.DARK_GRAY);
            arrayList.add(CharacterAndFormat.BLUE);
            arrayList.add(CharacterAndFormat.GREEN);
            arrayList.add(CharacterAndFormat.AQUA);
            arrayList.add(CharacterAndFormat.RED);
            arrayList.add(CharacterAndFormat.LIGHT_PURPLE);
            arrayList.add(CharacterAndFormat.YELLOW);
            arrayList.add(CharacterAndFormat.WHITE);
            arrayList.add(CharacterAndFormat.OBFUSCATED);
            arrayList.add(CharacterAndFormat.BOLD);
            arrayList.add(CharacterAndFormat.STRIKETHROUGH);
            arrayList.add(CharacterAndFormat.UNDERLINED);
            arrayList.add(CharacterAndFormat.ITALIC);
            arrayList.add(CharacterAndFormat.RESET);
            return Collections.unmodifiableList(arrayList);
        }
    }
}

