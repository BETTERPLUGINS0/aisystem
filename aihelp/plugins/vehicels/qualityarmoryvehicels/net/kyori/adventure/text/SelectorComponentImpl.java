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
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class SelectorComponentImpl
extends AbstractComponent
implements SelectorComponent {
    private final String pattern;
    @Nullable
    private final Component separator;

    static SelectorComponent create(@NotNull List<? extends ComponentLike> list, @NotNull Style style, @NotNull String string, @Nullable ComponentLike componentLike) {
        return new SelectorComponentImpl(ComponentLike.asComponents(list, IS_NOT_EMPTY), Objects.requireNonNull(style, "style"), Objects.requireNonNull(string, "pattern"), ComponentLike.unbox(componentLike));
    }

    SelectorComponentImpl(@NotNull List<Component> list, @NotNull Style style, @NotNull String string, @Nullable Component component) {
        super(list, style);
        this.pattern = string;
        this.separator = component;
    }

    @Override
    @NotNull
    public String pattern() {
        return this.pattern;
    }

    @Override
    @NotNull
    public SelectorComponent pattern(@NotNull String string) {
        if (Objects.equals(this.pattern, string)) {
            return this;
        }
        return SelectorComponentImpl.create(this.children, this.style, string, this.separator);
    }

    @Override
    @Nullable
    public Component separator() {
        return this.separator;
    }

    @Override
    @NotNull
    public SelectorComponent separator(@Nullable ComponentLike componentLike) {
        return SelectorComponentImpl.create(this.children, this.style, this.pattern, componentLike);
    }

    @Override
    @NotNull
    public SelectorComponent children(@NotNull List<? extends ComponentLike> list) {
        return SelectorComponentImpl.create(list, this.style, this.pattern, this.separator);
    }

    @Override
    @NotNull
    public SelectorComponent style(@NotNull Style style) {
        return SelectorComponentImpl.create(this.children, style, this.pattern, this.separator);
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof SelectorComponent)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        SelectorComponent selectorComponent = (SelectorComponent)object;
        return Objects.equals(this.pattern, selectorComponent.pattern()) && Objects.equals(this.separator, selectorComponent.separator());
    }

    @Override
    public int hashCode() {
        int n = super.hashCode();
        n = 31 * n + this.pattern.hashCode();
        n = 31 * n + Objects.hashCode(this.separator);
        return n;
    }

    @Override
    public String toString() {
        return Internals.toString(this);
    }

    @Override
    @NotNull
    public SelectorComponent.Builder toBuilder() {
        return new BuilderImpl(this);
    }

    static final class BuilderImpl
    extends AbstractComponentBuilder<SelectorComponent, SelectorComponent.Builder>
    implements SelectorComponent.Builder {
        @Nullable
        private String pattern;
        @Nullable
        private Component separator;

        BuilderImpl() {
        }

        BuilderImpl(@NotNull SelectorComponent selectorComponent) {
            super(selectorComponent);
            this.pattern = selectorComponent.pattern();
            this.separator = selectorComponent.separator();
        }

        @Override
        @NotNull
        public SelectorComponent.Builder pattern(@NotNull String string) {
            this.pattern = Objects.requireNonNull(string, "pattern");
            return this;
        }

        @Override
        @NotNull
        public SelectorComponent.Builder separator(@Nullable ComponentLike componentLike) {
            this.separator = ComponentLike.unbox(componentLike);
            return this;
        }

        @Override
        @NotNull
        public SelectorComponent build() {
            if (this.pattern == null) {
                throw new IllegalStateException("pattern must be set");
            }
            return SelectorComponentImpl.create(this.children, this.buildStyle(), this.pattern, this.separator);
        }
    }
}

