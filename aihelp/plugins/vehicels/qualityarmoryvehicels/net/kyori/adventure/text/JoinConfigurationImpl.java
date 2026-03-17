/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class JoinConfigurationImpl
implements JoinConfiguration {
    static final Function<ComponentLike, Component> DEFAULT_CONVERTOR = ComponentLike::asComponent;
    static final Predicate<ComponentLike> DEFAULT_PREDICATE = componentLike -> true;
    static final JoinConfigurationImpl NULL = new JoinConfigurationImpl();
    static final JoinConfiguration STANDARD_NEW_LINES = JoinConfiguration.separator(Component.newline());
    static final JoinConfiguration STANDARD_SPACES = JoinConfiguration.separator(Component.space());
    static final JoinConfiguration STANDARD_COMMA_SEPARATED = JoinConfiguration.separator(Component.text(","));
    static final JoinConfiguration STANDARD_COMMA_SPACE_SEPARATED = JoinConfiguration.separator(Component.text(", "));
    static final JoinConfiguration STANDARD_ARRAY_LIKE = (JoinConfiguration)JoinConfiguration.builder().separator(Component.text(", ")).prefix(Component.text("[")).suffix(Component.text("]")).build();
    private final Component prefix;
    private final Component suffix;
    private final Component separator;
    private final Component lastSeparator;
    private final Component lastSeparatorIfSerial;
    private final Function<ComponentLike, Component> convertor;
    private final Predicate<ComponentLike> predicate;
    private final Style rootStyle;

    private JoinConfigurationImpl() {
        this.prefix = null;
        this.suffix = null;
        this.separator = null;
        this.lastSeparator = null;
        this.lastSeparatorIfSerial = null;
        this.convertor = DEFAULT_CONVERTOR;
        this.predicate = DEFAULT_PREDICATE;
        this.rootStyle = Style.empty();
    }

    private JoinConfigurationImpl(@NotNull BuilderImpl builderImpl) {
        this.prefix = ComponentLike.unbox(builderImpl.prefix);
        this.suffix = ComponentLike.unbox(builderImpl.suffix);
        this.separator = ComponentLike.unbox(builderImpl.separator);
        this.lastSeparator = ComponentLike.unbox(builderImpl.lastSeparator);
        this.lastSeparatorIfSerial = ComponentLike.unbox(builderImpl.lastSeparatorIfSerial);
        this.convertor = builderImpl.convertor;
        this.predicate = builderImpl.predicate;
        this.rootStyle = builderImpl.rootStyle;
    }

    @Override
    @Nullable
    public Component prefix() {
        return this.prefix;
    }

    @Override
    @Nullable
    public Component suffix() {
        return this.suffix;
    }

    @Override
    @Nullable
    public Component separator() {
        return this.separator;
    }

    @Override
    @Nullable
    public Component lastSeparator() {
        return this.lastSeparator;
    }

    @Override
    @Nullable
    public Component lastSeparatorIfSerial() {
        return this.lastSeparatorIfSerial;
    }

    @Override
    @NotNull
    public Function<ComponentLike, Component> convertor() {
        return this.convertor;
    }

    @Override
    @NotNull
    public Predicate<ComponentLike> predicate() {
        return this.predicate;
    }

    @Override
    @NotNull
    public Style parentStyle() {
        return this.rootStyle;
    }

    @Override
    public @NotNull JoinConfiguration.Builder toBuilder() {
        return new BuilderImpl(this);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("prefix", this.prefix), ExaminableProperty.of("suffix", this.suffix), ExaminableProperty.of("separator", this.separator), ExaminableProperty.of("lastSeparator", this.lastSeparator), ExaminableProperty.of("lastSeparatorIfSerial", this.lastSeparatorIfSerial), ExaminableProperty.of("convertor", this.convertor), ExaminableProperty.of("predicate", this.predicate), ExaminableProperty.of("rootStyle", this.rootStyle));
    }

    public String toString() {
        return Internals.toString(this);
    }

    @Contract(pure=true)
    @NotNull
    static Component join(@NotNull JoinConfiguration joinConfiguration, @NotNull Iterable<? extends ComponentLike> iterable) {
        TextComponent.Builder builder;
        Objects.requireNonNull(joinConfiguration, "config");
        Objects.requireNonNull(iterable, "components");
        Iterator<? extends ComponentLike> iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            return JoinConfigurationImpl.singleElementJoin(joinConfiguration, null);
        }
        ComponentLike componentLike = Objects.requireNonNull(iterator.next(), "Null elements in \"components\" are not allowed");
        int n = 0;
        if (!iterator.hasNext()) {
            return JoinConfigurationImpl.singleElementJoin(joinConfiguration, componentLike);
        }
        Component component = joinConfiguration.prefix();
        Component component2 = joinConfiguration.suffix();
        Function<ComponentLike, Component> function = joinConfiguration.convertor();
        Predicate<ComponentLike> predicate = joinConfiguration.predicate();
        Style style = joinConfiguration.parentStyle();
        boolean bl = style != Style.empty();
        Component component3 = joinConfiguration.separator();
        boolean bl2 = component3 != null;
        TextComponent.Builder builder2 = builder = bl ? (TextComponent.Builder)Component.text().style(style) : Component.text();
        if (component != null) {
            builder.append(component);
        }
        while (componentLike != null) {
            if (!predicate.test(componentLike)) {
                if (!iterator.hasNext()) break;
                componentLike = iterator.next();
                continue;
            }
            builder.append(Objects.requireNonNull(function.apply(componentLike), "Null output from \"convertor\" is not allowed"));
            ++n;
            if (!iterator.hasNext()) {
                componentLike = null;
                continue;
            }
            componentLike = Objects.requireNonNull(iterator.next(), "Null elements in \"components\" are not allowed");
            if (iterator.hasNext()) {
                if (!bl2) continue;
                builder.append(component3);
                continue;
            }
            Component component4 = null;
            if (n > 1) {
                component4 = joinConfiguration.lastSeparatorIfSerial();
            }
            if (component4 == null) {
                component4 = joinConfiguration.lastSeparator();
            }
            if (component4 == null) {
                component4 = joinConfiguration.separator();
            }
            if (component4 == null) continue;
            builder.append(component4);
        }
        if (component2 != null) {
            builder.append(component2);
        }
        return builder.build();
    }

    @NotNull
    static Component singleElementJoin(@NotNull JoinConfiguration joinConfiguration, @Nullable ComponentLike componentLike) {
        boolean bl;
        Component component = joinConfiguration.prefix();
        Component component2 = joinConfiguration.suffix();
        Function<ComponentLike, Component> function = joinConfiguration.convertor();
        Predicate<ComponentLike> predicate = joinConfiguration.predicate();
        Style style = joinConfiguration.parentStyle();
        boolean bl2 = bl = style != Style.empty();
        if (component == null && component2 == null) {
            Component component3 = componentLike == null || !predicate.test(componentLike) ? Component.empty() : function.apply(componentLike);
            return bl ? ((TextComponent.Builder)((TextComponent.Builder)Component.text().style(style)).append(component3)).build() : component3;
        }
        TextComponent.Builder builder = Component.text();
        if (component != null) {
            builder.append(component);
        }
        if (componentLike != null && predicate.test(componentLike)) {
            builder.append(function.apply(componentLike));
        }
        if (component2 != null) {
            builder.append(component2);
        }
        return bl ? ((TextComponent.Builder)((TextComponent.Builder)Component.text().style(style)).append(builder)).build() : builder.build();
    }

    static final class BuilderImpl
    implements JoinConfiguration.Builder {
        private ComponentLike prefix;
        private ComponentLike suffix;
        private ComponentLike separator;
        private ComponentLike lastSeparator;
        private ComponentLike lastSeparatorIfSerial;
        private Function<ComponentLike, Component> convertor;
        private Predicate<ComponentLike> predicate;
        private Style rootStyle;

        BuilderImpl() {
            this(NULL);
        }

        private BuilderImpl(@NotNull JoinConfigurationImpl joinConfigurationImpl) {
            this.separator = joinConfigurationImpl.separator;
            this.lastSeparator = joinConfigurationImpl.lastSeparator;
            this.prefix = joinConfigurationImpl.prefix;
            this.suffix = joinConfigurationImpl.suffix;
            this.convertor = joinConfigurationImpl.convertor;
            this.lastSeparatorIfSerial = joinConfigurationImpl.lastSeparatorIfSerial;
            this.predicate = joinConfigurationImpl.predicate;
            this.rootStyle = joinConfigurationImpl.rootStyle;
        }

        @Override
        @NotNull
        public JoinConfiguration.Builder prefix(@Nullable ComponentLike componentLike) {
            this.prefix = componentLike;
            return this;
        }

        @Override
        @NotNull
        public JoinConfiguration.Builder suffix(@Nullable ComponentLike componentLike) {
            this.suffix = componentLike;
            return this;
        }

        @Override
        @NotNull
        public JoinConfiguration.Builder separator(@Nullable ComponentLike componentLike) {
            this.separator = componentLike;
            return this;
        }

        @Override
        @NotNull
        public JoinConfiguration.Builder lastSeparator(@Nullable ComponentLike componentLike) {
            this.lastSeparator = componentLike;
            return this;
        }

        @Override
        @NotNull
        public JoinConfiguration.Builder lastSeparatorIfSerial(@Nullable ComponentLike componentLike) {
            this.lastSeparatorIfSerial = componentLike;
            return this;
        }

        @Override
        @NotNull
        public JoinConfiguration.Builder convertor(@NotNull Function<ComponentLike, Component> function) {
            this.convertor = Objects.requireNonNull(function, "convertor");
            return this;
        }

        @Override
        @NotNull
        public JoinConfiguration.Builder predicate(@NotNull Predicate<ComponentLike> predicate) {
            this.predicate = Objects.requireNonNull(predicate, "predicate");
            return this;
        }

        @Override
        @NotNull
        public JoinConfiguration.Builder parentStyle(@NotNull Style style) {
            this.rootStyle = Objects.requireNonNull(style, "rootStyle");
            return this;
        }

        @Override
        @NotNull
        public JoinConfiguration build() {
            return new JoinConfigurationImpl(this);
        }
    }
}

