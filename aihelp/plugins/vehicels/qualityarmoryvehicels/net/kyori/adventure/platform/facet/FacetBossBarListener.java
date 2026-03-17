/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.kyori.adventure.platform.facet;

import java.util.Set;
import java.util.function.Function;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.platform.facet.Facet;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

class FacetBossBarListener<V>
implements Facet.BossBar<V> {
    private final Facet.BossBar<V> facet;
    private final Function<Component, Component> translator;

    FacetBossBarListener(@NotNull Facet.BossBar<V> bossBar, @NotNull Function<Component, Component> function) {
        this.facet = bossBar;
        this.translator = function;
    }

    @Override
    public void bossBarInitialized(@NotNull BossBar bossBar) {
        this.facet.bossBarInitialized(bossBar);
        this.bossBarNameChanged(bossBar, bossBar.name(), bossBar.name());
    }

    @Override
    public void bossBarNameChanged(@NotNull BossBar bossBar, @NotNull Component component, @NotNull Component component2) {
        this.facet.bossBarNameChanged(bossBar, component, this.translator.apply(component2));
    }

    @Override
    public void bossBarProgressChanged(@NotNull BossBar bossBar, float f, float f2) {
        this.facet.bossBarProgressChanged(bossBar, f, f2);
    }

    @Override
    public void bossBarColorChanged(@NotNull BossBar bossBar, @NotNull BossBar.Color color, @NotNull BossBar.Color color2) {
        this.facet.bossBarColorChanged(bossBar, color, color2);
    }

    @Override
    public void bossBarOverlayChanged(@NotNull BossBar bossBar, @NotNull BossBar.Overlay overlay, @NotNull BossBar.Overlay overlay2) {
        this.facet.bossBarOverlayChanged(bossBar, overlay, overlay2);
    }

    @Override
    public void bossBarFlagsChanged(@NotNull BossBar bossBar, @NotNull Set<BossBar.Flag> set, @NotNull Set<BossBar.Flag> set2) {
        this.facet.bossBarFlagsChanged(bossBar, set, set2);
    }

    @Override
    public void addViewer(@NotNull V v) {
        this.facet.addViewer(v);
    }

    @Override
    public void removeViewer(@NotNull V v) {
        this.facet.removeViewer(v);
    }

    @Override
    public boolean isEmpty() {
        return this.facet.isEmpty();
    }

    @Override
    public void close() {
        this.facet.close();
    }
}

