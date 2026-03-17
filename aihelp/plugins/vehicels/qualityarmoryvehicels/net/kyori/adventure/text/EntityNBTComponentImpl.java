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
import net.kyori.adventure.text.AbstractNBTComponentBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.NBTComponentImpl;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class EntityNBTComponentImpl
extends NBTComponentImpl<EntityNBTComponent, EntityNBTComponent.Builder>
implements EntityNBTComponent {
    private final String selector;

    static EntityNBTComponent create(@NotNull List<? extends ComponentLike> list, @NotNull Style style, String string, boolean bl, @Nullable ComponentLike componentLike, String string2) {
        return new EntityNBTComponentImpl(ComponentLike.asComponents(list, IS_NOT_EMPTY), Objects.requireNonNull(style, "style"), Objects.requireNonNull(string, "nbtPath"), bl, ComponentLike.unbox(componentLike), Objects.requireNonNull(string2, "selector"));
    }

    EntityNBTComponentImpl(@NotNull List<Component> list, @NotNull Style style, String string, boolean bl, @Nullable Component component, String string2) {
        super(list, style, string, bl, component);
        this.selector = string2;
    }

    @Override
    @NotNull
    public EntityNBTComponent nbtPath(@NotNull String string) {
        if (Objects.equals(this.nbtPath, string)) {
            return this;
        }
        return EntityNBTComponentImpl.create(this.children, this.style, string, this.interpret, this.separator, this.selector);
    }

    @Override
    @NotNull
    public EntityNBTComponent interpret(boolean bl) {
        if (this.interpret == bl) {
            return this;
        }
        return EntityNBTComponentImpl.create(this.children, this.style, this.nbtPath, bl, this.separator, this.selector);
    }

    @Override
    @Nullable
    public Component separator() {
        return this.separator;
    }

    @Override
    @NotNull
    public EntityNBTComponent separator(@Nullable ComponentLike componentLike) {
        return EntityNBTComponentImpl.create(this.children, this.style, this.nbtPath, this.interpret, componentLike, this.selector);
    }

    @Override
    @NotNull
    public String selector() {
        return this.selector;
    }

    @Override
    @NotNull
    public EntityNBTComponent selector(@NotNull String string) {
        if (Objects.equals(this.selector, string)) {
            return this;
        }
        return EntityNBTComponentImpl.create(this.children, this.style, this.nbtPath, this.interpret, this.separator, string);
    }

    @Override
    @NotNull
    public EntityNBTComponent children(@NotNull List<? extends ComponentLike> list) {
        return EntityNBTComponentImpl.create(list, this.style, this.nbtPath, this.interpret, this.separator, this.selector);
    }

    @Override
    @NotNull
    public EntityNBTComponent style(@NotNull Style style) {
        return EntityNBTComponentImpl.create(this.children, style, this.nbtPath, this.interpret, this.separator, this.selector);
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof EntityNBTComponent)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        EntityNBTComponentImpl entityNBTComponentImpl = (EntityNBTComponentImpl)object;
        return Objects.equals(this.selector, entityNBTComponentImpl.selector());
    }

    @Override
    public int hashCode() {
        int n = super.hashCode();
        n = 31 * n + this.selector.hashCode();
        return n;
    }

    @Override
    public String toString() {
        return Internals.toString(this);
    }

    @Override
    public @NotNull EntityNBTComponent.Builder toBuilder() {
        return new BuilderImpl(this);
    }

    static final class BuilderImpl
    extends AbstractNBTComponentBuilder<EntityNBTComponent, EntityNBTComponent.Builder>
    implements EntityNBTComponent.Builder {
        @Nullable
        private String selector;

        BuilderImpl() {
        }

        BuilderImpl(@NotNull EntityNBTComponent entityNBTComponent) {
            super(entityNBTComponent);
            this.selector = entityNBTComponent.selector();
        }

        @Override
        public @NotNull EntityNBTComponent.Builder selector(@NotNull String string) {
            this.selector = Objects.requireNonNull(string, "selector");
            return this;
        }

        @Override
        @NotNull
        public EntityNBTComponent build() {
            if (this.nbtPath == null) {
                throw new IllegalStateException("nbt path must be set");
            }
            if (this.selector == null) {
                throw new IllegalStateException("selector must be set");
            }
            return EntityNBTComponentImpl.create(this.children, this.buildStyle(), this.nbtPath, this.interpret, this.separator, this.selector);
        }
    }
}

