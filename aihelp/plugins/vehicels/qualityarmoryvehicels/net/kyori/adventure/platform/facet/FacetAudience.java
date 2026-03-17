/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.ApiStatus$OverrideOnly
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.platform.facet;

import java.io.Closeable;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.platform.facet.Facet;
import net.kyori.adventure.platform.facet.FacetAudienceProvider;
import net.kyori.adventure.platform.facet.FacetBossBarListener;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class FacetAudience<V>
implements Audience,
Closeable {
    @NotNull
    protected final FacetAudienceProvider<V, FacetAudience<V>> provider;
    @NotNull
    private final Set<V> viewers;
    @Nullable
    private V viewer;
    private volatile Pointers pointers;
    private final @Nullable Facet.Chat<V, Object> chat;
    private final @Nullable Facet.ActionBar<V, Object> actionBar;
    private final @Nullable Facet.Title<V, Object, Object, Object> title;
    private final @Nullable Facet.Sound<V, Object> sound;
    private final @Nullable Facet.EntitySound<V, Object> entitySound;
    private final @Nullable Facet.Book<V, Object, Object> book;
    private final @Nullable Facet.BossBar.Builder<V, Facet.BossBar<V>> bossBar;
    @Nullable
    private final Map<BossBar, Facet.BossBar<V>> bossBars;
    private final @Nullable Facet.TabList<V, Object> tabList;
    @NotNull
    private final Collection<? extends Facet.Pointers<V>> pointerProviders;

    public FacetAudience(@NotNull FacetAudienceProvider facetAudienceProvider, @NotNull Collection<? extends V> collection, @Nullable Collection<? extends Facet.Chat> collection2, @Nullable Collection<? extends Facet.ActionBar> collection3, @Nullable Collection<? extends Facet.Title> collection4, @Nullable Collection<? extends Facet.Sound> collection5, @Nullable Collection<? extends Facet.EntitySound> collection6, @Nullable Collection<? extends Facet.Book> collection7, @Nullable Collection<? extends Facet.BossBar.Builder> collection8, @Nullable Collection<? extends Facet.TabList> collection9, @Nullable Collection<? extends Facet.Pointers> collection10) {
        this.provider = Objects.requireNonNull(facetAudienceProvider, "audience provider");
        this.viewers = new CopyOnWriteArraySet<V>();
        for (V v : Objects.requireNonNull(collection, "viewers")) {
            this.addViewer(v);
        }
        this.refresh();
        this.chat = Facet.of(collection2, this.viewer);
        this.actionBar = Facet.of(collection3, this.viewer);
        this.title = Facet.of(collection4, this.viewer);
        this.sound = Facet.of(collection5, this.viewer);
        this.entitySound = Facet.of(collection6, this.viewer);
        this.book = Facet.of(collection7, this.viewer);
        this.bossBar = Facet.of(collection8, this.viewer);
        this.bossBars = this.bossBar == null ? null : Collections.synchronizedMap(new IdentityHashMap(4));
        this.tabList = Facet.of(collection9, this.viewer);
        this.pointerProviders = collection10 == null ? Collections.emptyList() : collection10;
    }

    public void addViewer(@NotNull V v) {
        if (this.viewers.add(v) && this.viewer == null) {
            this.viewer = v;
            this.refresh();
        }
    }

    public void removeViewer(@NotNull V v) {
        if (this.viewers.remove(v) && this.viewer == v) {
            this.viewer = this.viewers.isEmpty() ? null : this.viewers.iterator().next();
            this.refresh();
        }
        if (this.bossBars == null) {
            return;
        }
        for (Facet.BossBar<V> bossBar : this.bossBars.values()) {
            bossBar.removeViewer(v);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void refresh() {
        FacetAudience facetAudience = this;
        synchronized (facetAudience) {
            this.pointers = null;
        }
        if (this.bossBars == null) {
            return;
        }
        for (Map.Entry entry : this.bossBars.entrySet()) {
            BossBar bossBar = (BossBar)entry.getKey();
            Facet.BossBar bossBar2 = (Facet.BossBar)entry.getValue();
            bossBar2.bossBarNameChanged(bossBar, bossBar.name(), bossBar.name());
        }
    }

    @Override
    public void sendMessage(@NotNull Identity identity, @NotNull Component component, @NotNull MessageType messageType) {
        if (this.chat == null) {
            return;
        }
        Object object = this.createMessage(component, this.chat);
        if (object == null) {
            return;
        }
        for (V v : this.viewers) {
            this.chat.sendMessage(v, identity, object, (Object)messageType);
        }
    }

    @Override
    public void sendMessage(@NotNull Component component, @NotNull ChatType.Bound bound) {
        if (this.chat == null) {
            return;
        }
        Object object = this.createMessage(component, this.chat);
        if (object == null) {
            return;
        }
        Component component2 = this.provider.componentRenderer.render(bound.name(), this);
        Component component3 = null;
        if (bound.target() != null) {
            component3 = this.provider.componentRenderer.render(bound.target(), this);
        }
        ChatType.Bound bound2 = bound.type().bind(component2, component3);
        for (V v : this.viewers) {
            this.chat.sendMessage(v, Identity.nil(), object, bound2);
        }
    }

    @Override
    public void sendMessage(@NotNull SignedMessage signedMessage, @NotNull ChatType.Bound bound) {
        if (signedMessage.isSystem()) {
            Component component = signedMessage.unsignedContent() != null ? signedMessage.unsignedContent() : Component.text(signedMessage.message());
            this.sendMessage(component, bound);
        } else {
            Audience.super.sendMessage(signedMessage, bound);
        }
    }

    @Override
    public void sendActionBar(@NotNull Component component) {
        if (this.actionBar == null) {
            return;
        }
        Object object = this.createMessage(component, this.actionBar);
        if (object == null) {
            return;
        }
        for (V v : this.viewers) {
            this.actionBar.sendMessage(v, object);
        }
    }

    @Override
    public void playSound(@NotNull Sound sound) {
        if (this.sound == null) {
            return;
        }
        for (V v : this.viewers) {
            Object p = this.sound.createPosition(v);
            if (p == null) continue;
            this.sound.playSound(v, sound, p);
        }
    }

    @Override
    public void playSound(@NotNull Sound sound, @NotNull Sound.Emitter emitter) {
        if (this.entitySound == null) {
            return;
        }
        if (emitter == Sound.Emitter.self()) {
            for (V v : this.viewers) {
                Object object = this.entitySound.createForSelf(v, sound);
                if (object == null) continue;
                this.entitySound.playSound(v, object);
            }
        } else {
            Object object = this.entitySound.createForEmitter(sound, emitter);
            if (object == null) {
                return;
            }
            for (V v : this.viewers) {
                this.entitySound.playSound(v, object);
            }
        }
    }

    @Override
    public void playSound(@NotNull Sound sound, double d, double d2, double d3) {
        if (this.sound == null) {
            return;
        }
        Object p = this.sound.createPosition(d, d2, d3);
        for (V v : this.viewers) {
            this.sound.playSound(v, sound, p);
        }
    }

    @Override
    public void stopSound(@NotNull SoundStop soundStop) {
        if (this.sound == null) {
            return;
        }
        for (V v : this.viewers) {
            this.sound.stopSound(v, soundStop);
        }
    }

    @Override
    public void openBook(@NotNull Book book) {
        if (this.book == null) {
            return;
        }
        String string = this.toPlain(book.title());
        String string2 = this.toPlain(book.author());
        LinkedList<Object> linkedList = new LinkedList<Object>();
        for (Component object : book.pages()) {
            Object object2 = this.createMessage(object, this.book);
            if (object2 == null) continue;
            linkedList.add(object2);
        }
        if (string == null || string2 == null || linkedList.isEmpty()) {
            return;
        }
        Object object = this.book.createBook(string, string2, linkedList);
        if (object == null) {
            return;
        }
        for (Object object2 : this.viewers) {
            this.book.openBook(object2, object);
        }
    }

    private String toPlain(Component component) {
        if (component == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        ComponentFlattener.basic().flatten(this.provider.componentRenderer.render(component, this), stringBuilder::append);
        return stringBuilder.toString();
    }

    @Override
    public void showTitle(@NotNull Title title) {
        if (this.title == null) {
            return;
        }
        Object object = this.createMessage(title.title(), this.title);
        Object object2 = this.createMessage(title.subtitle(), this.title);
        @Nullable Title.Times times = title.times();
        int n = times == null ? -1 : this.title.toTicks(times.fadeIn());
        int n2 = times == null ? -1 : this.title.toTicks(times.stay());
        int n3 = times == null ? -1 : this.title.toTicks(times.fadeOut());
        Object object3 = this.title.createTitleCollection();
        if (n != -1 || n2 != -1 || n3 != -1) {
            this.title.contributeTimes(object3, n, n2, n3);
        }
        this.title.contributeSubtitle(object3, object2);
        this.title.contributeTitle(object3, object);
        Object object4 = this.title.completeTitle(object3);
        if (object4 == null) {
            return;
        }
        for (V v : this.viewers) {
            this.title.showTitle(v, object4);
        }
    }

    @Override
    public <T> void sendTitlePart(@NotNull TitlePart<T> titlePart, @NotNull T t) {
        Object object;
        if (this.title == null) {
            return;
        }
        Objects.requireNonNull(t, "value");
        Object object2 = this.title.createTitleCollection();
        if (titlePart == TitlePart.TITLE) {
            object = this.createMessage((Component)t, this.title);
            if (object != null) {
                this.title.contributeTitle(object2, object);
            }
        } else if (titlePart == TitlePart.SUBTITLE) {
            object = this.createMessage((Component)t, this.title);
            if (object != null) {
                this.title.contributeSubtitle(object2, object);
            }
        } else if (titlePart == TitlePart.TIMES) {
            object = (Title.Times)t;
            int n = this.title.toTicks(object.fadeIn());
            int n2 = this.title.toTicks(object.stay());
            int n3 = this.title.toTicks(object.fadeOut());
            if (n != -1 || n2 != -1 || n3 != -1) {
                this.title.contributeTimes(object2, n, n2, n3);
            }
        } else {
            throw new IllegalArgumentException("Unknown TitlePart '" + titlePart + "'");
        }
        object = this.title.completeTitle(object2);
        if (object == null) {
            return;
        }
        for (V v : this.viewers) {
            this.title.showTitle(v, object);
        }
    }

    @Override
    public void clearTitle() {
        if (this.title == null) {
            return;
        }
        for (V v : this.viewers) {
            this.title.clearTitle(v);
        }
    }

    @Override
    public void resetTitle() {
        if (this.title == null) {
            return;
        }
        for (V v : this.viewers) {
            this.title.resetTitle(v);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void showBossBar(@NotNull BossBar bossBar) {
        Facet.BossBar bossBar2;
        if (this.bossBar == null || this.bossBars == null) {
            return;
        }
        Map<BossBar, Facet.BossBar<V>> map = this.bossBars;
        synchronized (map) {
            bossBar2 = this.bossBars.get(bossBar);
            if (bossBar2 == null) {
                bossBar2 = new FacetBossBarListener<V>(this.bossBar.createBossBar(this.viewers), component -> this.provider.componentRenderer.render((Component)component, this));
                this.bossBars.put(bossBar, bossBar2);
            }
        }
        if (bossBar2.isEmpty()) {
            bossBar2.bossBarInitialized(bossBar);
            bossBar.addListener(bossBar2);
        }
        for (Object e : this.viewers) {
            bossBar2.addViewer(e);
        }
    }

    @Override
    public void hideBossBar(@NotNull BossBar bossBar) {
        if (this.bossBars == null) {
            return;
        }
        Facet.BossBar<V> bossBar2 = this.bossBars.get(bossBar);
        if (bossBar2 == null) {
            return;
        }
        for (V v : this.viewers) {
            bossBar2.removeViewer(v);
        }
        if (bossBar2.isEmpty() && this.bossBars.remove(bossBar) != null) {
            bossBar.removeListener(bossBar2);
            bossBar2.close();
        }
    }

    @Override
    public void sendPlayerListHeader(@NotNull Component component) {
        if (this.tabList != null) {
            Object object = this.createMessage(component, this.tabList);
            if (object == null) {
                return;
            }
            for (V v : this.viewers) {
                this.tabList.send(v, object, null);
            }
        }
    }

    @Override
    public void sendPlayerListFooter(@NotNull Component component) {
        if (this.tabList != null) {
            Object object = this.createMessage(component, this.tabList);
            if (object == null) {
                return;
            }
            for (V v : this.viewers) {
                this.tabList.send(v, null, object);
            }
        }
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull Component component, @NotNull Component component2) {
        if (this.tabList != null) {
            Object object = this.createMessage(component, this.tabList);
            Object object2 = this.createMessage(component2, this.tabList);
            if (object == null || object2 == null) {
                return;
            }
            for (V v : this.viewers) {
                this.tabList.send(v, object, object2);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    @NotNull
    public Pointers pointers() {
        if (this.pointers == null) {
            FacetAudience facetAudience = this;
            synchronized (facetAudience) {
                if (this.pointers == null) {
                    V v = this.viewer;
                    if (v == null) {
                        return Pointers.empty();
                    }
                    Pointers.Builder builder = Pointers.builder();
                    this.contributePointers(builder);
                    for (Facet.Pointers<V> pointers : this.pointerProviders) {
                        if (!pointers.isApplicable(v)) continue;
                        pointers.contributePointers(v, builder);
                    }
                    this.pointers = (Pointers)builder.build();
                    return this.pointers;
                }
            }
        }
        return this.pointers;
    }

    @ApiStatus.OverrideOnly
    protected void contributePointers(Pointers.Builder builder) {
    }

    @Override
    public void close() {
        if (this.bossBars != null) {
            for (BossBar object : new LinkedList<BossBar>(this.bossBars.keySet())) {
                this.hideBossBar(object);
            }
            this.bossBars.clear();
        }
        for (Object object : this.viewers) {
            this.removeViewer(object);
        }
        this.viewers.clear();
    }

    @Nullable
    private Object createMessage(@NotNull Component component, @NotNull Facet.Message<V, Object> message) {
        Component component2 = this.provider.componentRenderer.render(component, this);
        V v = this.viewer;
        return v == null ? null : message.createMessage(v, component2);
    }
}

