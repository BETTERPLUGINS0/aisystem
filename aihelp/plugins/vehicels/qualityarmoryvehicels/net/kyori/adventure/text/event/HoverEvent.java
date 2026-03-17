/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$ScheduledForRemoval
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.Range
 */
package net.kyori.adventure.text.event;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.DataComponentValueConverterRegistry;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import net.kyori.adventure.util.Index;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public final class HoverEvent<V>
implements Examinable,
HoverEventSource<V>,
StyleBuilderApplicable {
    private final Action<V> action;
    private final V value;

    @NotNull
    public static HoverEvent<Component> showText(@NotNull ComponentLike componentLike) {
        return HoverEvent.showText(componentLike.asComponent());
    }

    @NotNull
    public static HoverEvent<Component> showText(@NotNull Component component) {
        return new HoverEvent<Component>(Action.SHOW_TEXT, component);
    }

    @NotNull
    public static HoverEvent<ShowItem> showItem(@NotNull Key key, @Range(from=0L, to=0x7FFFFFFFL) int n) {
        return HoverEvent.showItem((Keyed)key, n, Collections.emptyMap());
    }

    @NotNull
    public static HoverEvent<ShowItem> showItem(@NotNull Keyed keyed, @Range(from=0L, to=0x7FFFFFFFL) int n) {
        return HoverEvent.showItem(keyed, n, Collections.emptyMap());
    }

    @Deprecated
    @NotNull
    public static HoverEvent<ShowItem> showItem(@NotNull Key key, @Range(from=0L, to=0x7FFFFFFFL) int n, @Nullable BinaryTagHolder binaryTagHolder) {
        return HoverEvent.showItem(ShowItem.showItem(key, n, binaryTagHolder));
    }

    @Deprecated
    @NotNull
    public static HoverEvent<ShowItem> showItem(@NotNull Keyed keyed, @Range(from=0L, to=0x7FFFFFFFL) int n, @Nullable BinaryTagHolder binaryTagHolder) {
        return HoverEvent.showItem(ShowItem.showItem(keyed, n, binaryTagHolder));
    }

    @NotNull
    public static HoverEvent<ShowItem> showItem(@NotNull Keyed keyed, @Range(from=0L, to=0x7FFFFFFFL) int n, @NotNull Map<Key, ? extends DataComponentValue> map) {
        return HoverEvent.showItem(ShowItem.showItem(keyed, n, map));
    }

    @NotNull
    public static HoverEvent<ShowItem> showItem(@NotNull ShowItem showItem) {
        return new HoverEvent<ShowItem>(Action.SHOW_ITEM, showItem);
    }

    @NotNull
    public static HoverEvent<ShowEntity> showEntity(@NotNull Key key, @NotNull UUID uUID) {
        return HoverEvent.showEntity(key, uUID, null);
    }

    @NotNull
    public static HoverEvent<ShowEntity> showEntity(@NotNull Keyed keyed, @NotNull UUID uUID) {
        return HoverEvent.showEntity(keyed, uUID, null);
    }

    @NotNull
    public static HoverEvent<ShowEntity> showEntity(@NotNull Key key, @NotNull UUID uUID, @Nullable Component component) {
        return HoverEvent.showEntity(ShowEntity.of(key, uUID, component));
    }

    @NotNull
    public static HoverEvent<ShowEntity> showEntity(@NotNull Keyed keyed, @NotNull UUID uUID, @Nullable Component component) {
        return HoverEvent.showEntity(ShowEntity.of(keyed, uUID, component));
    }

    @NotNull
    public static HoverEvent<ShowEntity> showEntity(@NotNull ShowEntity showEntity) {
        return new HoverEvent<ShowEntity>(Action.SHOW_ENTITY, showEntity);
    }

    @Deprecated
    @NotNull
    public static HoverEvent<String> showAchievement(@NotNull String string) {
        return new HoverEvent<String>(Action.SHOW_ACHIEVEMENT, string);
    }

    @NotNull
    public static <V> HoverEvent<V> hoverEvent(@NotNull Action<V> action, @NotNull V v) {
        return new HoverEvent<V>(action, v);
    }

    private HoverEvent(@NotNull Action<V> action, @NotNull V v) {
        this.action = Objects.requireNonNull(action, "action");
        this.value = Objects.requireNonNull(v, "value");
    }

    @NotNull
    public Action<V> action() {
        return this.action;
    }

    @NotNull
    public V value() {
        return this.value;
    }

    @NotNull
    public HoverEvent<V> value(@NotNull V v) {
        return new HoverEvent<V>(this.action, v);
    }

    @NotNull
    public <C> HoverEvent<V> withRenderedValue(@NotNull ComponentRenderer<C> componentRenderer, @NotNull C c) {
        V v = this.value;
        V v2 = ((Action)this.action).renderer.render(componentRenderer, c, v);
        if (v2 != v) {
            return new HoverEvent<V>(this.action, v2);
        }
        return this;
    }

    @Override
    @NotNull
    public HoverEvent<V> asHoverEvent() {
        return this;
    }

    @Override
    @NotNull
    public HoverEvent<V> asHoverEvent(@NotNull UnaryOperator<V> unaryOperator) {
        if (unaryOperator == UnaryOperator.identity()) {
            return this;
        }
        return new HoverEvent<V>(this.action, unaryOperator.apply(this.value));
    }

    @Override
    public void styleApply( @NotNull Style.Builder builder) {
        builder.hoverEvent((HoverEventSource)this);
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        HoverEvent hoverEvent = (HoverEvent)object;
        return this.action == hoverEvent.action && this.value.equals(hoverEvent.value);
    }

    public int hashCode() {
        int n = this.action.hashCode();
        n = 31 * n + this.value.hashCode();
        return n;
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("action", this.action), ExaminableProperty.of("value", this.value));
    }

    public String toString() {
        return Internals.toString(this);
    }

    public static final class Action<V> {
        public static final Action<Component> SHOW_TEXT = new Action<Component>("show_text", Component.class, true, new Renderer<Component>(){

            @Override
            @NotNull
            public <C> Component render(@NotNull ComponentRenderer<C> componentRenderer, @NotNull C c, @NotNull Component component) {
                return componentRenderer.render(component, c);
            }
        });
        public static final Action<ShowItem> SHOW_ITEM = new Action<ShowItem>("show_item", ShowItem.class, true, new Renderer<ShowItem>(){

            @Override
            @NotNull
            public <C> ShowItem render(@NotNull ComponentRenderer<C> componentRenderer, @NotNull C c, @NotNull ShowItem showItem) {
                return showItem;
            }
        });
        public static final Action<ShowEntity> SHOW_ENTITY = new Action<ShowEntity>("show_entity", ShowEntity.class, true, new Renderer<ShowEntity>(){

            @Override
            @NotNull
            public <C> ShowEntity render(@NotNull ComponentRenderer<C> componentRenderer, @NotNull C c, @NotNull ShowEntity showEntity) {
                if (showEntity.name == null) {
                    return showEntity;
                }
                return showEntity.name(componentRenderer.render(showEntity.name, c));
            }
        });
        @Deprecated
        public static final Action<String> SHOW_ACHIEVEMENT = new Action<String>("show_achievement", String.class, true, new Renderer<String>(){

            @Override
            @NotNull
            public <C> String render(@NotNull ComponentRenderer<C> componentRenderer, @NotNull C c, @NotNull String string) {
                return string;
            }
        });
        public static final Index<String, Action<?>> NAMES = Index.create(action -> action.name, SHOW_TEXT, SHOW_ITEM, SHOW_ENTITY, SHOW_ACHIEVEMENT);
        private final String name;
        private final Class<V> type;
        private final boolean readable;
        private final Renderer<V> renderer;

        Action(String string, Class<V> clazz, boolean bl, Renderer<V> renderer) {
            this.name = string;
            this.type = clazz;
            this.readable = bl;
            this.renderer = renderer;
        }

        @NotNull
        public Class<V> type() {
            return this.type;
        }

        public boolean readable() {
            return this.readable;
        }

        @NotNull
        public String toString() {
            return this.name;
        }

        @FunctionalInterface
        static interface Renderer<V> {
            @NotNull
            public <C> V render(@NotNull ComponentRenderer<C> var1, @NotNull C var2, @NotNull V var3);
        }
    }

    public static final class ShowEntity
    implements Examinable {
        private final Key type;
        private final UUID id;
        private final Component name;

        @NotNull
        public static ShowEntity showEntity(@NotNull Key key, @NotNull UUID uUID) {
            return ShowEntity.showEntity(key, uUID, null);
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
        @NotNull
        public static ShowEntity of(@NotNull Key key, @NotNull UUID uUID) {
            return ShowEntity.of(key, uUID, null);
        }

        @NotNull
        public static ShowEntity showEntity(@NotNull Keyed keyed, @NotNull UUID uUID) {
            return ShowEntity.showEntity(keyed, uUID, null);
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
        @NotNull
        public static ShowEntity of(@NotNull Keyed keyed, @NotNull UUID uUID) {
            return ShowEntity.of(keyed, uUID, null);
        }

        @NotNull
        public static ShowEntity showEntity(@NotNull Key key, @NotNull UUID uUID, @Nullable Component component) {
            return new ShowEntity(Objects.requireNonNull(key, "type"), Objects.requireNonNull(uUID, "id"), component);
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
        @NotNull
        public static ShowEntity of(@NotNull Key key, @NotNull UUID uUID, @Nullable Component component) {
            return new ShowEntity(Objects.requireNonNull(key, "type"), Objects.requireNonNull(uUID, "id"), component);
        }

        @NotNull
        public static ShowEntity showEntity(@NotNull Keyed keyed, @NotNull UUID uUID, @Nullable Component component) {
            return new ShowEntity(Objects.requireNonNull(keyed, "type").key(), Objects.requireNonNull(uUID, "id"), component);
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
        @NotNull
        public static ShowEntity of(@NotNull Keyed keyed, @NotNull UUID uUID, @Nullable Component component) {
            return new ShowEntity(Objects.requireNonNull(keyed, "type").key(), Objects.requireNonNull(uUID, "id"), component);
        }

        private ShowEntity(@NotNull Key key, @NotNull UUID uUID, @Nullable Component component) {
            this.type = key;
            this.id = uUID;
            this.name = component;
        }

        @NotNull
        public Key type() {
            return this.type;
        }

        @NotNull
        public ShowEntity type(@NotNull Key key) {
            if (Objects.requireNonNull(key, "type").equals(this.type)) {
                return this;
            }
            return new ShowEntity(key, this.id, this.name);
        }

        @NotNull
        public ShowEntity type(@NotNull Keyed keyed) {
            return this.type(Objects.requireNonNull(keyed, "type").key());
        }

        @NotNull
        public UUID id() {
            return this.id;
        }

        @NotNull
        public ShowEntity id(@NotNull UUID uUID) {
            if (Objects.requireNonNull(uUID).equals(this.id)) {
                return this;
            }
            return new ShowEntity(this.type, uUID, this.name);
        }

        @Nullable
        public Component name() {
            return this.name;
        }

        @NotNull
        public ShowEntity name(@Nullable Component component) {
            if (Objects.equals(component, this.name)) {
                return this;
            }
            return new ShowEntity(this.type, this.id, component);
        }

        public boolean equals(@Nullable Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            ShowEntity showEntity = (ShowEntity)object;
            return this.type.equals(showEntity.type) && this.id.equals(showEntity.id) && Objects.equals(this.name, showEntity.name);
        }

        public int hashCode() {
            int n = this.type.hashCode();
            n = 31 * n + this.id.hashCode();
            n = 31 * n + Objects.hashCode(this.name);
            return n;
        }

        @Override
        @NotNull
        public Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(ExaminableProperty.of("type", this.type), ExaminableProperty.of("id", this.id), ExaminableProperty.of("name", this.name));
        }

        public String toString() {
            return Internals.toString(this);
        }
    }

    public static final class ShowItem
    implements Examinable {
        private final Key item;
        private final int count;
        @Nullable
        private final BinaryTagHolder nbt;
        private final Map<Key, DataComponentValue> dataComponents;

        @NotNull
        public static ShowItem showItem(@NotNull Key key, @Range(from=0L, to=0x7FFFFFFFL) int n) {
            return ShowItem.showItem((Keyed)key, n, Collections.emptyMap());
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
        @NotNull
        public static ShowItem of(@NotNull Key key, @Range(from=0L, to=0x7FFFFFFFL) int n) {
            return ShowItem.showItem((Keyed)key, n, Collections.emptyMap());
        }

        @NotNull
        public static ShowItem showItem(@NotNull Keyed keyed, @Range(from=0L, to=0x7FFFFFFFL) int n) {
            return ShowItem.showItem(keyed, n, Collections.emptyMap());
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
        @NotNull
        public static ShowItem of(@NotNull Keyed keyed, @Range(from=0L, to=0x7FFFFFFFL) int n) {
            return ShowItem.of(keyed, n, null);
        }

        @Deprecated
        @NotNull
        public static ShowItem showItem(@NotNull Key key, @Range(from=0L, to=0x7FFFFFFFL) int n, @Nullable BinaryTagHolder binaryTagHolder) {
            return new ShowItem(Objects.requireNonNull(key, "item"), n, binaryTagHolder, Collections.emptyMap());
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
        @NotNull
        public static ShowItem of(@NotNull Key key, @Range(from=0L, to=0x7FFFFFFFL) int n, @Nullable BinaryTagHolder binaryTagHolder) {
            return new ShowItem(Objects.requireNonNull(key, "item"), n, binaryTagHolder, Collections.emptyMap());
        }

        @Deprecated
        @NotNull
        public static ShowItem showItem(@NotNull Keyed keyed, @Range(from=0L, to=0x7FFFFFFFL) int n, @Nullable BinaryTagHolder binaryTagHolder) {
            return new ShowItem(Objects.requireNonNull(keyed, "item").key(), n, binaryTagHolder, Collections.emptyMap());
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
        @NotNull
        public static ShowItem of(@NotNull Keyed keyed, @Range(from=0L, to=0x7FFFFFFFL) int n, @Nullable BinaryTagHolder binaryTagHolder) {
            return new ShowItem(Objects.requireNonNull(keyed, "item").key(), n, binaryTagHolder, Collections.emptyMap());
        }

        @NotNull
        public static ShowItem showItem(@NotNull Keyed keyed, @Range(from=0L, to=0x7FFFFFFFL) int n, @NotNull Map<Key, ? extends DataComponentValue> map) {
            return new ShowItem(Objects.requireNonNull(keyed, "item").key(), n, null, map);
        }

        private ShowItem(@NotNull Key key, @Range(from=0L, to=0x7FFFFFFFL) int n, @Nullable BinaryTagHolder binaryTagHolder, @NotNull Map<Key, ? extends DataComponentValue> map) {
            this.item = key;
            this.count = n;
            this.nbt = binaryTagHolder;
            this.dataComponents = Collections.unmodifiableMap(new HashMap<Key, DataComponentValue>(map));
        }

        @NotNull
        public Key item() {
            return this.item;
        }

        @NotNull
        public ShowItem item(@NotNull Key key) {
            if (Objects.requireNonNull(key, "item").equals(this.item)) {
                return this;
            }
            return new ShowItem(key, this.count, this.nbt, this.dataComponents);
        }

        public @Range(from=0L, to=0x7FFFFFFFL) int count() {
            return this.count;
        }

        @NotNull
        public ShowItem count(@Range(from=0L, to=0x7FFFFFFFL) int n) {
            if (n == this.count) {
                return this;
            }
            return new ShowItem(this.item, n, this.nbt, this.dataComponents);
        }

        @Deprecated
        @Nullable
        public BinaryTagHolder nbt() {
            return this.nbt;
        }

        @Deprecated
        @NotNull
        public ShowItem nbt(@Nullable BinaryTagHolder binaryTagHolder) {
            if (Objects.equals(binaryTagHolder, this.nbt)) {
                return this;
            }
            return new ShowItem(this.item, this.count, binaryTagHolder, Collections.emptyMap());
        }

        @NotNull
        public Map<Key, DataComponentValue> dataComponents() {
            return this.dataComponents;
        }

        @NotNull
        public ShowItem dataComponents(@NotNull Map<Key, DataComponentValue> map) {
            if (Objects.equals(this.dataComponents, map)) {
                return this;
            }
            return new ShowItem(this.item, this.count, null, map.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap<Key, DataComponentValue>(map)));
        }

        @NotNull
        public <V extends DataComponentValue> Map<Key, V> dataComponentsAs(@NotNull Class<V> clazz) {
            if (this.dataComponents.isEmpty()) {
                return Collections.emptyMap();
            }
            HashMap<Key, V> hashMap = new HashMap<Key, V>(this.dataComponents.size());
            for (Map.Entry<Key, DataComponentValue> entry : this.dataComponents.entrySet()) {
                hashMap.put(entry.getKey(), DataComponentValueConverterRegistry.convert(clazz, entry.getKey(), entry.getValue()));
            }
            return Collections.unmodifiableMap(hashMap);
        }

        public boolean equals(@Nullable Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            ShowItem showItem = (ShowItem)object;
            return this.item.equals(showItem.item) && this.count == showItem.count && Objects.equals(this.nbt, showItem.nbt) && Objects.equals(this.dataComponents, showItem.dataComponents);
        }

        public int hashCode() {
            int n = this.item.hashCode();
            n = 31 * n + Integer.hashCode(this.count);
            n = 31 * n + Objects.hashCode(this.nbt);
            n = 31 * n + Objects.hashCode(this.dataComponents);
            return n;
        }

        @Override
        @NotNull
        public Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(ExaminableProperty.of("item", this.item), ExaminableProperty.of("count", this.count), ExaminableProperty.of("nbt", this.nbt), ExaminableProperty.of("dataComponents", this.dataComponents));
        }

        public String toString() {
            return Internals.toString(this);
        }
    }
}

