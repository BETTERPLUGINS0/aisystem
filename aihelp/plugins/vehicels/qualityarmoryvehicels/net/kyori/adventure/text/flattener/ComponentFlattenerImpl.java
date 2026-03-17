/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text.flattener;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.flattener.FlattenerListener;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.util.InheritanceAwareMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ComponentFlattenerImpl
implements ComponentFlattener {
    static final ComponentFlattener BASIC = (ComponentFlattener)new BuilderImpl().mapper(KeybindComponent.class, keybindComponent -> keybindComponent.keybind()).mapper(ScoreComponent.class, scoreComponent -> {
        @Nullable String string = scoreComponent.value();
        return string != null ? string : "";
    }).mapper(SelectorComponent.class, SelectorComponent::pattern).mapper(TextComponent.class, TextComponent::content).mapper(TranslatableComponent.class, translatableComponent -> {
        @Nullable String string = translatableComponent.fallback();
        return string != null ? string : translatableComponent.key();
    }).build();
    static final ComponentFlattener TEXT_ONLY = (ComponentFlattener)new BuilderImpl().mapper(TextComponent.class, TextComponent::content).build();
    private static final int MAX_DEPTH = 512;
    private final InheritanceAwareMap<Component, Handler> flatteners;
    private final Function<Component, String> unknownHandler;

    ComponentFlattenerImpl(InheritanceAwareMap<Component, Handler> inheritanceAwareMap, @Nullable Function<Component, String> function) {
        this.flatteners = inheritanceAwareMap;
        this.unknownHandler = function;
    }

    @Override
    public void flatten(@NotNull Component component, @NotNull FlattenerListener flattenerListener) {
        this.flatten0(component, flattenerListener, 0);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void flatten0(@NotNull Component component, @NotNull FlattenerListener flattenerListener, int n) {
        Objects.requireNonNull(component, "input");
        Objects.requireNonNull(flattenerListener, "listener");
        if (component == Component.empty()) {
            return;
        }
        if (n > 512) {
            throw new IllegalStateException("Exceeded maximum depth of 512 while attempting to flatten components!");
        }
        @Nullable Handler handler = this.flattener(component);
        Style style = component.style();
        flattenerListener.pushStyle(style);
        try {
            if (handler != null) {
                handler.handle(this, component, flattenerListener, n + 1);
            }
            if (!component.children().isEmpty() && flattenerListener.shouldContinue()) {
                for (Component component2 : component.children()) {
                    this.flatten0(component2, flattenerListener, n + 1);
                }
            }
        } finally {
            flattenerListener.popStyle(style);
        }
    }

    @Nullable
    private <T extends Component> Handler flattener(T t) {
        Handler handler = this.flatteners.get(t.getClass());
        if (handler == null && this.unknownHandler != null) {
            return (componentFlattenerImpl, component, flattenerListener, n) -> flattenerListener.component(this.unknownHandler.apply(component));
        }
        return handler;
    }

    @Override
    public @NotNull ComponentFlattener.Builder toBuilder() {
        return new BuilderImpl(this.flatteners, this.unknownHandler);
    }

    static final class BuilderImpl
    implements ComponentFlattener.Builder {
        private final InheritanceAwareMap.Builder<Component, Handler> flatteners;
        @Nullable
        private Function<Component, String> unknownHandler;

        BuilderImpl() {
            this.flatteners = InheritanceAwareMap.builder().strict(true);
        }

        BuilderImpl(InheritanceAwareMap<Component, Handler> inheritanceAwareMap, @Nullable Function<Component, String> function) {
            this.flatteners = InheritanceAwareMap.builder(inheritanceAwareMap).strict(true);
            this.unknownHandler = function;
        }

        @Override
        @NotNull
        public ComponentFlattener build() {
            return new ComponentFlattenerImpl((InheritanceAwareMap)this.flatteners.build(), this.unknownHandler);
        }

        @Override
        public <T extends Component> @NotNull ComponentFlattener.Builder mapper(@NotNull Class<T> clazz, @NotNull Function<T, String> function) {
            this.flatteners.put(clazz, (componentFlattenerImpl, component, flattenerListener, n) -> flattenerListener.component((String)function.apply(component)));
            return this;
        }

        @Override
        public <T extends Component> @NotNull ComponentFlattener.Builder complexMapper(@NotNull Class<T> clazz, @NotNull BiConsumer<T, Consumer<Component>> biConsumer) {
            this.flatteners.put(clazz, (componentFlattenerImpl, component2, flattenerListener, n) -> biConsumer.accept(component2, component -> componentFlattenerImpl.flatten0(component, flattenerListener, n)));
            return this;
        }

        @Override
        public @NotNull ComponentFlattener.Builder unknownMapper(@Nullable Function<Component, String> function) {
            this.unknownHandler = function;
            return this;
        }
    }

    @FunctionalInterface
    static interface Handler {
        public void handle(ComponentFlattenerImpl var1, Component var2, FlattenerListener var3, int var4);
    }
}

