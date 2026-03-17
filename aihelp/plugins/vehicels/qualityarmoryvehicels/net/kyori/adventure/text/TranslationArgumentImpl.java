/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text;

import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class TranslationArgumentImpl
implements TranslationArgument {
    private static final Component TRUE = Component.text("true");
    private static final Component FALSE = Component.text("false");
    private final Object value;

    TranslationArgumentImpl(Object object) {
        this.value = object;
    }

    @Override
    @NotNull
    public Object value() {
        return this.value;
    }

    @Override
    @NotNull
    public Component asComponent() {
        if (this.value instanceof Component) {
            return (Component)this.value;
        }
        if (this.value instanceof Boolean) {
            return (Boolean)this.value != false ? TRUE : FALSE;
        }
        return Component.text(String.valueOf(this.value));
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        TranslationArgumentImpl translationArgumentImpl = (TranslationArgumentImpl)object;
        return Objects.equals(this.value, translationArgumentImpl.value);
    }

    public int hashCode() {
        return Objects.hash(this.value);
    }

    public String toString() {
        return Internals.toString(this);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("value", this.value));
    }
}

