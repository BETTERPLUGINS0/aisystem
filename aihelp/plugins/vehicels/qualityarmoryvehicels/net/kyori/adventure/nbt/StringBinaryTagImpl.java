/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Debug$Renderer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.nbt;

import java.util.stream.Stream;
import net.kyori.adventure.nbt.AbstractBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Debug.Renderer(text="\"\\\"\" + this.value + \"\\\"\"", hasChildren="false")
final class StringBinaryTagImpl
extends AbstractBinaryTag
implements StringBinaryTag {
    private final String value;

    StringBinaryTagImpl(String string) {
        this.value = string;
    }

    @Override
    @NotNull
    public String value() {
        return this.value;
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        StringBinaryTagImpl stringBinaryTagImpl = (StringBinaryTagImpl)object;
        return this.value.equals(stringBinaryTagImpl.value);
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("value", this.value));
    }
}

