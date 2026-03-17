/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.AbstractComponent;
import net.kyori.adventure.text.AbstractComponentBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.TranslationArgumentLike;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class TranslatableComponentImpl
extends AbstractComponent
implements TranslatableComponent {
    private final String key;
    @Nullable
    private final String fallback;
    private final List<TranslationArgument> args;

    static TranslatableComponent create(@NotNull List<Component> list, @NotNull Style style, @NotNull String string, @Nullable String string2, @NotNull @NotNull ComponentLike @NotNull [] componentLikeArray) {
        Objects.requireNonNull(componentLikeArray, "args");
        return TranslatableComponentImpl.create(list, style, string, string2, Arrays.asList(componentLikeArray));
    }

    static TranslatableComponent create(@NotNull List<? extends ComponentLike> list, @NotNull Style style, @NotNull String string, @Nullable String string2, @NotNull List<? extends ComponentLike> list2) {
        return new TranslatableComponentImpl(ComponentLike.asComponents(list, IS_NOT_EMPTY), Objects.requireNonNull(style, "style"), Objects.requireNonNull(string, "key"), string2, TranslatableComponentImpl.asArguments(list2));
    }

    TranslatableComponentImpl(@NotNull List<Component> list, @NotNull Style style, @NotNull String string, @Nullable String string2, @NotNull List<TranslationArgument> list2) {
        super(list, style);
        this.key = string;
        this.fallback = string2;
        this.args = list2;
    }

    @Override
    @NotNull
    public String key() {
        return this.key;
    }

    @Override
    @NotNull
    public TranslatableComponent key(@NotNull String string) {
        if (Objects.equals(this.key, string)) {
            return this;
        }
        return TranslatableComponentImpl.create(this.children, this.style, string, this.fallback, this.args);
    }

    @Override
    @Deprecated
    @NotNull
    public List<Component> args() {
        return ComponentLike.asComponents(this.args);
    }

    @Override
    @NotNull
    public List<TranslationArgument> arguments() {
        return this.args;
    }

    @Override
    @NotNull
    public TranslatableComponent arguments(@NotNull @NotNull ComponentLike @NotNull ... componentLikeArray) {
        return TranslatableComponentImpl.create(this.children, this.style, this.key, this.fallback, componentLikeArray);
    }

    @Override
    @NotNull
    public TranslatableComponent arguments(@NotNull List<? extends ComponentLike> list) {
        return TranslatableComponentImpl.create(this.children, this.style, this.key, this.fallback, list);
    }

    @Override
    @Nullable
    public String fallback() {
        return this.fallback;
    }

    @Override
    @NotNull
    public TranslatableComponent fallback(@Nullable String string) {
        return TranslatableComponentImpl.create(this.children, this.style, this.key, string, this.args);
    }

    @Override
    @NotNull
    public TranslatableComponent children(@NotNull List<? extends ComponentLike> list) {
        return TranslatableComponentImpl.create(list, this.style, this.key, this.fallback, this.args);
    }

    @Override
    @NotNull
    public TranslatableComponent style(@NotNull Style style) {
        return TranslatableComponentImpl.create(this.children, style, this.key, this.fallback, this.args);
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof TranslatableComponent)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        TranslatableComponent translatableComponent = (TranslatableComponent)object;
        return Objects.equals(this.key, translatableComponent.key()) && Objects.equals(this.fallback, translatableComponent.fallback()) && Objects.equals(this.args, translatableComponent.arguments());
    }

    @Override
    public int hashCode() {
        int n = super.hashCode();
        n = 31 * n + this.key.hashCode();
        n = 31 * n + Objects.hashCode(this.fallback);
        n = 31 * n + this.args.hashCode();
        return n;
    }

    @Override
    public String toString() {
        return Internals.toString(this);
    }

    @Override
    @NotNull
    public TranslatableComponent.Builder toBuilder() {
        return new BuilderImpl(this);
    }

    static List<TranslationArgument> asArguments(@NotNull List<? extends ComponentLike> list) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList<TranslationArgument> arrayList = new ArrayList<TranslationArgument>(list.size());
        for (int i = 0; i < list.size(); ++i) {
            ComponentLike componentLike = list.get(i);
            if (componentLike == null) {
                throw new NullPointerException("likes[" + i + "]");
            }
            if (componentLike instanceof TranslationArgument) {
                arrayList.add((TranslationArgument)componentLike);
                continue;
            }
            if (componentLike instanceof TranslationArgumentLike) {
                arrayList.add(Objects.requireNonNull(((TranslationArgumentLike)componentLike).asTranslationArgument(), "likes[" + i + "].asTranslationArgument()"));
                continue;
            }
            arrayList.add(TranslationArgument.component(componentLike));
        }
        return Collections.unmodifiableList(arrayList);
    }

    static final class BuilderImpl
    extends AbstractComponentBuilder<TranslatableComponent, TranslatableComponent.Builder>
    implements TranslatableComponent.Builder {
        @Nullable
        private String key;
        @Nullable
        private String fallback;
        private List<TranslationArgument> args = Collections.emptyList();

        BuilderImpl() {
        }

        BuilderImpl(@NotNull TranslatableComponent translatableComponent) {
            super(translatableComponent);
            this.key = translatableComponent.key();
            this.args = translatableComponent.arguments();
            this.fallback = translatableComponent.fallback();
        }

        @Override
        @NotNull
        public TranslatableComponent.Builder key(@NotNull String string) {
            this.key = string;
            return this;
        }

        @Override
        @NotNull
        public TranslatableComponent.Builder arguments(@NotNull @NotNull ComponentLike @NotNull ... componentLikeArray) {
            Objects.requireNonNull(componentLikeArray, "args");
            if (componentLikeArray.length == 0) {
                return this.arguments(Collections.emptyList());
            }
            return this.arguments(Arrays.asList(componentLikeArray));
        }

        @Override
        @NotNull
        public TranslatableComponent.Builder arguments(@NotNull List<? extends ComponentLike> list) {
            this.args = TranslatableComponentImpl.asArguments(Objects.requireNonNull(list, "args"));
            return this;
        }

        @Override
        @NotNull
        public TranslatableComponent.Builder fallback(@Nullable String string) {
            this.fallback = string;
            return this;
        }

        @Override
        @NotNull
        public TranslatableComponent build() {
            if (this.key == null) {
                throw new IllegalStateException("key must be set");
            }
            return TranslatableComponentImpl.create(this.children, this.buildStyle(), this.key, this.fallback, this.args);
        }
    }
}

