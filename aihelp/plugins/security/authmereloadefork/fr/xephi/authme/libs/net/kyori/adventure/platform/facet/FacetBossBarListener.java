package fr.xephi.authme.libs.net.kyori.adventure.platform.facet;

import fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import java.util.Set;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

class FacetBossBarListener<V> implements Facet.BossBar<V> {
   private final Facet.BossBar<V> facet;
   private final Function<Component, Component> translator;

   FacetBossBarListener(@NotNull final Facet.BossBar<V> facet, @NotNull final Function<Component, Component> translator) {
      this.facet = facet;
      this.translator = translator;
   }

   public void bossBarInitialized(@NotNull final BossBar bar) {
      this.facet.bossBarInitialized(bar);
      this.bossBarNameChanged(bar, bar.name(), bar.name());
   }

   public void bossBarNameChanged(@NotNull final BossBar bar, @NotNull final Component oldName, @NotNull final Component newName) {
      this.facet.bossBarNameChanged(bar, oldName, (Component)this.translator.apply(newName));
   }

   public void bossBarProgressChanged(@NotNull final BossBar bar, final float oldPercent, final float newPercent) {
      this.facet.bossBarProgressChanged(bar, oldPercent, newPercent);
   }

   public void bossBarColorChanged(@NotNull final BossBar bar, @NotNull final BossBar.Color oldColor, @NotNull final BossBar.Color newColor) {
      this.facet.bossBarColorChanged(bar, oldColor, newColor);
   }

   public void bossBarOverlayChanged(@NotNull final BossBar bar, @NotNull final BossBar.Overlay oldOverlay, @NotNull final BossBar.Overlay newOverlay) {
      this.facet.bossBarOverlayChanged(bar, oldOverlay, newOverlay);
   }

   public void bossBarFlagsChanged(@NotNull final BossBar bar, @NotNull final Set<BossBar.Flag> flagsAdded, @NotNull final Set<BossBar.Flag> flagsRemoved) {
      this.facet.bossBarFlagsChanged(bar, flagsAdded, flagsRemoved);
   }

   public void addViewer(@NotNull final V viewer) {
      this.facet.addViewer(viewer);
   }

   public void removeViewer(@NotNull final V viewer) {
      this.facet.removeViewer(viewer);
   }

   public boolean isEmpty() {
      return this.facet.isEmpty();
   }

   public void close() {
      this.facet.close();
   }
}
