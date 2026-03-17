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
import net.kyori.adventure.nbt.DoubleBinaryTag;
import net.kyori.adventure.nbt.ShadyPines;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Debug.Renderer(text="String.valueOf(this.value) + \"d\"", hasChildren="false")
final class DoubleBinaryTagImpl
extends AbstractBinaryTag
implements DoubleBinaryTag {
    private final double value;

    DoubleBinaryTagImpl(double d) {
        this.value = d;
    }

    @Override
    public double value() {
        return this.value;
    }

    @Override
    public byte byteValue() {
        return (byte)(ShadyPines.floor(this.value) & 0xFF);
    }

    @Override
    public double doubleValue() {
        return this.value;
    }

    @Override
    public float floatValue() {
        return (float)this.value;
    }

    @Override
    public int intValue() {
        return ShadyPines.floor(this.value);
    }

    @Override
    public long longValue() {
        return (long)Math.floor(this.value);
    }

    @Override
    public short shortValue() {
        return (short)(ShadyPines.floor(this.value) & 0xFFFF);
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        DoubleBinaryTagImpl doubleBinaryTagImpl = (DoubleBinaryTagImpl)object;
        return Double.doubleToLongBits(this.value) == Double.doubleToLongBits(doubleBinaryTagImpl.value);
    }

    public int hashCode() {
        return Double.hashCode(this.value);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("value", this.value));
    }
}

