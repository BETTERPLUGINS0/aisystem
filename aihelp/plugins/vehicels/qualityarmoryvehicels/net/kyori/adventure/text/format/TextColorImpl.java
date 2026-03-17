/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Debug$Renderer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text.format;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Debug.Renderer(text="asHexString()")
final class TextColorImpl
implements TextColor {
    private final int value;

    TextColorImpl(int n) {
        this.value = n;
    }

    @Override
    public int value() {
        return this.value;
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof TextColorImpl)) {
            return false;
        }
        TextColorImpl textColorImpl = (TextColorImpl)object;
        return this.value == textColorImpl.value;
    }

    public int hashCode() {
        return this.value;
    }

    public String toString() {
        return this.asHexString();
    }

    static float distance(@NotNull HSVLike hSVLike, @NotNull HSVLike hSVLike2) {
        float f = 3.0f * Math.min(Math.abs(hSVLike.h() - hSVLike2.h()), 1.0f - Math.abs(hSVLike.h() - hSVLike2.h()));
        float f2 = hSVLike.s() - hSVLike2.s();
        float f3 = hSVLike.v() - hSVLike2.v();
        return f * f + f2 * f2 + f3 * f3;
    }
}

