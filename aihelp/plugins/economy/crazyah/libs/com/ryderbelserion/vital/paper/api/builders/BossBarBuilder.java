package libs.com.ryderbelserion.vital.paper.api.builders;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BossBarBuilder {
   private final Player target;
   private BossBar bossBar;

   public BossBarBuilder(@NotNull Player target, @NotNull BossBar bossBar) {
      this.target = target;
      this.bossBar = bossBar;
   }

   @NotNull
   public final Player getTarget() {
      return this.target;
   }

   @NotNull
   public final BossBar getBossBar() {
      return this.bossBar;
   }

   public void applyBossBar(@Nullable BossBar bossBar) {
      this.bossBar = bossBar;
   }

   public void hideBossBar() {
      this.getTarget().hideBossBar(this.getBossBar());
      this.applyBossBar((BossBar)null);
   }
}
