package ac.grim.grimac.shaded.kyori.adventure.bossbar;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.UnmodifiableView;

public interface BossBarViewer {
   @NotNull
   @UnmodifiableView
   Iterable<? extends BossBar> activeBossBars();
}
