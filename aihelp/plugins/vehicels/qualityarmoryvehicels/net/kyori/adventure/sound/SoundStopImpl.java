/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.sound;

import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class SoundStopImpl
implements SoundStop {
    static final SoundStop ALL = new SoundStopImpl(null){

        @Override
        @Nullable
        public Key sound() {
            return null;
        }
    };
    private final @Nullable Sound.Source source;

    SoundStopImpl(@Nullable Sound.Source source) {
        this.source = source;
    }

    @Override
    public @Nullable Sound.Source source() {
        return this.source;
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof SoundStopImpl)) {
            return false;
        }
        SoundStopImpl soundStopImpl = (SoundStopImpl)object;
        return Objects.equals(this.sound(), soundStopImpl.sound()) && Objects.equals((Object)this.source, (Object)soundStopImpl.source);
    }

    public int hashCode() {
        int n = Objects.hashCode(this.sound());
        n = 31 * n + Objects.hashCode((Object)this.source);
        return n;
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("name", this.sound()), ExaminableProperty.of("source", (Object)this.source));
    }

    public String toString() {
        return Internals.toString(this);
    }
}

