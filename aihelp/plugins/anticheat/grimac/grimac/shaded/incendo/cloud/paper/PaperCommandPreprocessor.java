package ac.grim.grimac.shaded.incendo.cloud.paper;

import ac.grim.grimac.shaded.incendo.cloud.SenderMapper;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitCommandContextKeys;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitCommandManager;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.PluginHolder;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitHelper;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.CraftBukkitReflection;
import ac.grim.grimac.shaded.incendo.cloud.execution.preprocessor.CommandPreprocessingContext;
import ac.grim.grimac.shaded.incendo.cloud.execution.preprocessor.CommandPreprocessor;
import ac.grim.grimac.shaded.incendo.cloud.key.CloudKey;
import java.util.concurrent.Executor;
import java.util.function.Function;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

final class PaperCommandPreprocessor<B, C> implements CommandPreprocessor<C> {
   private static final boolean FOLIA = CraftBukkitReflection.classExists("io.papermc.paper.threadedregions.RegionizedServer");
   private final PluginHolder pluginHolder;
   private final SenderMapper<B, C> mapper;
   private final Function<B, CommandSender> senderExtractor;

   PaperCommandPreprocessor(final PluginHolder pluginHolder, final SenderMapper<B, C> mapper, final Function<B, CommandSender> senderExtractor) {
      this.pluginHolder = pluginHolder;
      this.mapper = mapper;
      this.senderExtractor = senderExtractor;
   }

   public void accept(final CommandPreprocessingContext<C> ctx) {
      if (FOLIA) {
         ctx.commandContext().store((CloudKey)BukkitCommandContextKeys.SENDER_SCHEDULER_EXECUTOR, this.foliaExecutorFor(ctx.commandContext().sender()));
      } else if (!(this.pluginHolder instanceof BukkitCommandManager)) {
         ctx.commandContext().store((CloudKey)BukkitCommandContextKeys.SENDER_SCHEDULER_EXECUTOR, BukkitHelper.mainThreadExecutor(this.pluginHolder));
      }

   }

   private Executor foliaExecutorFor(final C sender) {
      CommandSender commandSender = (CommandSender)this.senderExtractor.apply(this.mapper.reverse(sender));
      Plugin plugin = this.pluginHolder.owningPlugin();
      if (commandSender instanceof Entity) {
         return (task) -> {
            ((Entity)commandSender).getScheduler().run(plugin, (handle) -> {
               task.run();
            }, (Runnable)null);
         };
      } else if (commandSender instanceof BlockCommandSender) {
         BlockCommandSender blockSender = (BlockCommandSender)commandSender;
         return (task) -> {
            blockSender.getServer().getRegionScheduler().run(plugin, blockSender.getBlock().getLocation(), (handle) -> {
               task.run();
            });
         };
      } else {
         return (task) -> {
            plugin.getServer().getGlobalRegionScheduler().run(plugin, (handle) -> {
               task.run();
            });
         };
      }
   }
}
