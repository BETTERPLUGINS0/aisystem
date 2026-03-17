/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BuildableComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.Buildable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class AbstractComponentBuilder<C extends BuildableComponent<C, B>, B extends ComponentBuilder<C, B>>
implements ComponentBuilder<C, B> {
    protected List<Component> children = Collections.emptyList();
    @Nullable
    private Style style;
    private @Nullable Style.Builder styleBuilder;

    protected AbstractComponentBuilder() {
    }

    protected AbstractComponentBuilder(@NotNull C c) {
        List<Component> list = c.children();
        if (!list.isEmpty()) {
            this.children = new ArrayList<Component>(list);
        }
        if (c.hasStyling()) {
            this.style = c.style();
        }
    }

    @Override
    @NotNull
    public B append(@NotNull Component component) {
        if (component == Component.empty()) {
            return (B)this;
        }
        this.prepareChildren();
        this.children.add(Objects.requireNonNull(component, "component"));
        return (B)this;
    }

    @Override
    @NotNull
    public B append(@NotNull @NotNull Component @NotNull ... componentArray) {
        return this.append((ComponentLike[])componentArray);
    }

    @Override
    @NotNull
    public B append(@NotNull @NotNull ComponentLike @NotNull ... componentLikeArray) {
        Objects.requireNonNull(componentLikeArray, "components");
        boolean bl = false;
        int n = componentLikeArray.length;
        for (int i = 0; i < n; ++i) {
            Component component = Objects.requireNonNull(componentLikeArray[i], "components[?]").asComponent();
            if (component == Component.empty()) continue;
            if (!bl) {
                this.prepareChildren();
                bl = true;
            }
            this.children.add(Objects.requireNonNull(component, "components[?]"));
        }
        return (B)this;
    }

    @Override
    @NotNull
    public B append(@NotNull Iterable<? extends ComponentLike> iterable) {
        Objects.requireNonNull(iterable, "components");
        boolean bl = false;
        for (ComponentLike componentLike : iterable) {
            Component component = Objects.requireNonNull(componentLike, "components[?]").asComponent();
            if (component == Component.empty()) continue;
            if (!bl) {
                this.prepareChildren();
                bl = true;
            }
            this.children.add(Objects.requireNonNull(component, "components[?]"));
        }
        return (B)this;
    }

    private void prepareChildren() {
        if (this.children == Collections.emptyList()) {
            this.children = new ArrayList<Component>();
        }
    }

    @Override
    @NotNull
    public B applyDeep(@NotNull Consumer<? super ComponentBuilder<?, ?>> consumer) {
        this.apply(consumer);
        if (this.children == Collections.emptyList()) {
            return (B)this;
        }
        ListIterator<Component> listIterator = this.children.listIterator();
        while (listIterator.hasNext()) {
            Component component = listIterator.next();
            if (!(component instanceof BuildableComponent)) continue;
            Buildable.Builder builder = ((BuildableComponent)component).toBuilder();
            builder.applyDeep(consumer);
            listIterator.set((Component)builder.build());
        }
        return (B)this;
    }

    @Override
    @NotNull
    public B mapChildren(@NotNull Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function) {
        if (this.children == Collections.emptyList()) {
            return (B)this;
        }
        ListIterator<Component> listIterator = this.children.listIterator();
        while (listIterator.hasNext()) {
            BuildableComponent<?, ?> buildableComponent;
            Component component = listIterator.next();
            if (!(component instanceof BuildableComponent) || component == (buildableComponent = Objects.requireNonNull(function.apply((BuildableComponent)component), "mappedChild"))) continue;
            listIterator.set(buildableComponent);
        }
        return (B)this;
    }

    @Override
    @NotNull
    public B mapChildrenDeep(@NotNull Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function) {
        if (this.children == Collections.emptyList()) {
            return (B)this;
        }
        ListIterator<Component> listIterator = this.children.listIterator();
        while (listIterator.hasNext()) {
            Component component = listIterator.next();
            if (!(component instanceof BuildableComponent)) continue;
            BuildableComponent<?, ?> buildableComponent = Objects.requireNonNull(function.apply((BuildableComponent)component), "mappedChild");
            if (buildableComponent.children().isEmpty()) {
                if (component == buildableComponent) continue;
                listIterator.set(buildableComponent);
                continue;
            }
            Buildable.Builder builder = buildableComponent.toBuilder();
            builder.mapChildrenDeep(function);
            listIterator.set((Component)builder.build());
        }
        return (B)this;
    }

    @Override
    @NotNull
    public List<Component> children() {
        return Collections.unmodifiableList(this.children);
    }

    @Override
    @NotNull
    public B style(@NotNull Style style) {
        this.style = style;
        this.styleBuilder = null;
        return (B)this;
    }

    @Override
    @NotNull
    public B style(@NotNull Consumer<Style.Builder> consumer) {
        consumer.accept(this.styleBuilder());
        return (B)this;
    }

    @Override
    @NotNull
    public B font(@Nullable Key key) {
        this.styleBuilder().font(key);
        return (B)this;
    }

    @Override
    @NotNull
    public B color(@Nullable TextColor textColor) {
        this.styleBuilder().color(textColor);
        return (B)this;
    }

    @Override
    @NotNull
    public B colorIfAbsent(@Nullable TextColor textColor) {
        this.styleBuilder().colorIfAbsent(textColor);
        return (B)this;
    }

    @Override
    @NotNull
    public B decoration(@NotNull TextDecoration textDecoration, @NotNull TextDecoration.State state) {
        this.styleBuilder().decoration(textDecoration, state);
        return (B)this;
    }

    @Override
    @NotNull
    public B decorationIfAbsent(@NotNull TextDecoration textDecoration, @NotNull TextDecoration.State state) {
        this.styleBuilder().decorationIfAbsent(textDecoration, state);
        return (B)this;
    }

    @Override
    @NotNull
    public B clickEvent(@Nullable ClickEvent clickEvent) {
        this.styleBuilder().clickEvent(clickEvent);
        return (B)this;
    }

    @Override
    @NotNull
    public B hoverEvent(@Nullable HoverEventSource<?> hoverEventSource) {
        this.styleBuilder().hoverEvent((HoverEventSource)hoverEventSource);
        return (B)this;
    }

    @Override
    @NotNull
    public B insertion(@Nullable String string) {
        this.styleBuilder().insertion(string);
        return (B)this;
    }

    @Override
    @NotNull
    public B mergeStyle(@NotNull Component component, @NotNull Set<Style.Merge> set) {
        this.styleBuilder().merge(Objects.requireNonNull(component, "component").style(), set);
        return (B)this;
    }

    @Override
    @NotNull
    public B resetStyle() {
        this.style = null;
        this.styleBuilder = null;
        return (B)this;
    }

    private @NotNull Style.Builder styleBuilder() {
        if (this.styleBuilder == null) {
            if (this.style != null) {
                this.styleBuilder = this.style.toBuilder();
                this.style = null;
            } else {
                this.styleBuilder = Style.style();
            }
        }
        return this.styleBuilder;
    }

    protected final boolean hasStyle() {
        return this.styleBuilder != null || this.style != null;
    }

    @NotNull
    protected Style buildStyle() {
        if (this.styleBuilder != null) {
            return this.styleBuilder.build();
        }
        if (this.style != null) {
            return this.style;
        }
        return Style.empty();
    }
}

