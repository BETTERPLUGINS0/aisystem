/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text;

import java.util.List;
import java.util.Objects;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.AbstractComponent;
import net.kyori.adventure.text.AbstractComponentBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class KeybindComponentImpl
extends AbstractComponent
implements KeybindComponent {
    private final String keybind;

    static KeybindComponent create(@NotNull List<? extends ComponentLike> list, @NotNull Style style, @NotNull String string) {
        return new KeybindComponentImpl(ComponentLike.asComponents(list, IS_NOT_EMPTY), Objects.requireNonNull(style, "style"), Objects.requireNonNull(string, "keybind"));
    }

    KeybindComponentImpl(@NotNull List<Component> list, @NotNull Style style, @NotNull String string) {
        super(list, style);
        this.keybind = string;
    }

    @Override
    @NotNull
    public String keybind() {
        return this.keybind;
    }

    @Override
    @NotNull
    public KeybindComponent keybind(@NotNull String string) {
        if (Objects.equals(this.keybind, string)) {
            return this;
        }
        return KeybindComponentImpl.create(this.children, this.style, string);
    }

    @Override
    @NotNull
    public KeybindComponent children(@NotNull List<? extends ComponentLike> list) {
        return KeybindComponentImpl.create(list, this.style, this.keybind);
    }

    @Override
    @NotNull
    public KeybindComponent style(@NotNull Style style) {
        return KeybindComponentImpl.create(this.children, style, this.keybind);
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof KeybindComponent)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        KeybindComponent keybindComponent = (KeybindComponent)object;
        return Objects.equals(this.keybind, keybindComponent.keybind());
    }

    @Override
    public int hashCode() {
        int n = super.hashCode();
        n = 31 * n + this.keybind.hashCode();
        return n;
    }

    @Override
    public String toString() {
        return Internals.toString(this);
    }

    @Override
    @NotNull
    public KeybindComponent.Builder toBuilder() {
        return new BuilderImpl(this);
    }

    static final class BuilderImpl
    extends AbstractComponentBuilder<KeybindComponent, KeybindComponent.Builder>
    implements KeybindComponent.Builder {
        @Nullable
        private String keybind;

        BuilderImpl() {
        }

        BuilderImpl(@NotNull KeybindComponent keybindComponent) {
            super(keybindComponent);
            this.keybind = keybindComponent.keybind();
        }

        @Override
        @NotNull
        public KeybindComponent.Builder keybind(@NotNull String string) {
            this.keybind = Objects.requireNonNull(string, "keybind");
            return this;
        }

        @Override
        @NotNull
        public KeybindComponent build() {
            if (this.keybind == null) {
                throw new IllegalStateException("keybind must be set");
            }
            return KeybindComponentImpl.create(this.children, this.buildStyle(), this.keybind);
        }
    }
}

