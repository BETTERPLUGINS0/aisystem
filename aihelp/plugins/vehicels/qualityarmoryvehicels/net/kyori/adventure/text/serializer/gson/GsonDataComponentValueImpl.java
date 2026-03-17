/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.serializer.gson.GsonDataComponentValue;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class GsonDataComponentValueImpl
implements GsonDataComponentValue {
    private final JsonElement element;

    GsonDataComponentValueImpl(@NotNull JsonElement jsonElement) {
        this.element = jsonElement;
    }

    @Override
    @NotNull
    public JsonElement element() {
        return this.element;
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("element", this.element));
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
        GsonDataComponentValueImpl gsonDataComponentValueImpl = (GsonDataComponentValueImpl)object;
        return Objects.equals(this.element, gsonDataComponentValueImpl.element);
    }

    public int hashCode() {
        return Objects.hashCode(this.element);
    }

    static final class RemovedGsonComponentValueImpl
    extends GsonDataComponentValueImpl
    implements DataComponentValue.Removed {
        static final RemovedGsonComponentValueImpl INSTANCE = new RemovedGsonComponentValueImpl();

        private RemovedGsonComponentValueImpl() {
            super(JsonNull.INSTANCE);
        }
    }
}

