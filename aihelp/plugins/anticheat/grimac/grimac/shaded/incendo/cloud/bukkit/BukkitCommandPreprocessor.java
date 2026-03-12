package ac.grim.grimac.shaded.incendo.cloud.bukkit;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitBackwardsBrigadierSenderMapper;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitHelper;
import ac.grim.grimac.shaded.incendo.cloud.execution.preprocessor.CommandPreprocessingContext;
import ac.grim.grimac.shaded.incendo.cloud.execution.preprocessor.CommandPreprocessor;
import ac.grim.grimac.shaded.incendo.cloud.key.CloudKey;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class BukkitCommandPreprocessor<C> implements CommandPreprocessor<C> {
   private final BukkitCommandManager<C> commandManager;
   @Nullable
   private final BukkitBackwardsBrigadierSenderMapper<C, ?> mapper;

   BukkitCommandPreprocessor(@NonNull final BukkitCommandManager<C> commandManager) {
      this.commandManager = commandManager;
      if (this.commandManager.hasCapability(CloudBukkitCapabilities.BRIGADIER)) {
         this.mapper = new BukkitBackwardsBrigadierSenderMapper(this.commandManager.senderMapper());
      } else {
         this.mapper = null;
      }

   }

   public void accept(@NonNull final CommandPreprocessingContext<C> context) {
      if (this.mapper != null && !context.commandContext().contains("_cloud_brigadier_native_sender")) {
         context.commandContext().store("_cloud_brigadier_native_sender", this.mapper.apply(context.commandContext().sender()));
      }

      context.commandContext().store((CloudKey)BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER, (CommandSender)this.commandManager.senderMapper().reverse(context.commandContext().sender()));
      context.commandContext().computeIfAbsent(BukkitCommandContextKeys.SENDER_SCHEDULER_EXECUTOR, ($) -> {
         return BukkitHelper.mainThreadExecutor(this.commandManager);
      });
   }
}
