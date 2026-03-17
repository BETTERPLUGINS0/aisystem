/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text.serializer.legacy;

import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LegacyFormat
implements Examinable {
    static final LegacyFormat RESET = new LegacyFormat(true);
    @Nullable
    private final NamedTextColor color;
    @Nullable
    private final TextDecoration decoration;
    private final boolean reset;

    LegacyFormat(@Nullable NamedTextColor namedTextColor) {
        this.color = namedTextColor;
        this.decoration = null;
        this.reset = false;
    }

    LegacyFormat(@Nullable TextDecoration textDecoration) {
        this.color = null;
        this.decoration = textDecoration;
        this.reset = false;
    }

    private LegacyFormat(boolean bl) {
        this.color = null;
        this.decoration = null;
        this.reset = bl;
    }

    @Nullable
    public TextColor color() {
        return this.color;
    }

    @Nullable
    public TextDecoration decoration() {
        return this.decoration;
    }

    public boolean reset() {
        return this.reset;
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        LegacyFormat legacyFormat = (LegacyFormat)object;
        return this.color == legacyFormat.color && this.decoration == legacyFormat.decoration && this.reset == legacyFormat.reset;
    }

    public int hashCode() {
        int n = Objects.hashCode(this.color);
        n = 31 * n + Objects.hashCode(this.decoration);
        n = 31 * n + Boolean.hashCode(this.reset);
        return n;
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("color", this.color), ExaminableProperty.of("decoration", this.decoration), ExaminableProperty.of("reset", this.reset));
    }
}

