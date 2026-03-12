package ac.grim.grimac.shaded.incendo.cloud.paper;

import ac.grim.grimac.shaded.incendo.cloud.SenderMapper;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.BrigadierManagerHolder;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.BrigadierSetting;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.CloudBrigadierManager;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitCommandManager;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.CloudBukkitCapabilities;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.CraftBukkitReflection;
import ac.grim.grimac.shaded.incendo.cloud.execution.ExecutionCoordinator;
import ac.grim.grimac.shaded.incendo.cloud.paper.suggestion.SuggestionListener;
import ac.grim.grimac.shaded.incendo.cloud.paper.suggestion.SuggestionListenerFactory;
import ac.grim.grimac.shaded.incendo.cloud.state.RegistrationState;
import java.util.function.Function;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class LegacyPaperCommandManager<C> extends BukkitCommandManager<C> {
   @Nullable
   private BrigadierManagerHolder<C, ?> brigadierManagerHolder = null;

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   public LegacyPaperCommandManager(@NonNull final Plugin owningPlugin, @NonNull final ExecutionCoordinator<C> commandExecutionCoordinator, @NonNull final SenderMapper<CommandSender, C> senderMapper) throws BukkitCommandManager.InitializationException {
      super(owningPlugin, commandExecutionCoordinator, senderMapper);
      this.registerCommandPreProcessor(new PaperCommandPreprocessor(this, this.senderMapper(), Function.identity()));
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static LegacyPaperCommandManager<CommandSender> createNative(@NonNull final Plugin owningPlugin, @NonNull final ExecutionCoordinator<CommandSender> commandExecutionCoordinator) throws BukkitCommandManager.InitializationException {
      return new LegacyPaperCommandManager(owningPlugin, commandExecutionCoordinator, SenderMapper.identity());
   }

   public synchronized void registerBrigadier() throws BukkitCommandManager.BrigadierInitializationException {
      this.registerBrigadier(true);
   }

   /** @deprecated */
   @Deprecated
   public synchronized void registerLegacyPaperBrigadier() throws BukkitCommandManager.BrigadierInitializationException {
      this.registerBrigadier(false);
   }

   private void registerBrigadier(final boolean allowModern) {
      this.requireState(RegistrationState.BEFORE_REGISTRATION);
      this.checkBrigadierCompatibility();
      if (this.brigadierManagerHolder != null) {
         throw new IllegalStateException("Brigadier is already registered! Holder: " + this.brigadierManagerHolder);
      } else {
         if (!this.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            super.registerBrigadier();
         } else if (allowModern && CraftBukkitReflection.classExists("io.papermc.paper.command.brigadier.CommandSourceStack")) {
            try {
               ModernPaperBrigadier<C, CommandSender> brig = new ModernPaperBrigadier(CommandSender.class, this, this.senderMapper(), () -> {
                  this.lockRegistration();
               });
               this.brigadierManagerHolder = brig;
               brig.registerPlugin(this.owningPlugin());
               this.commandRegistrationHandler(brig);
            } catch (Exception var4) {
               throw new BukkitCommandManager.BrigadierInitializationException("Failed to register ModernPaperBrigadier", var4);
            }
         } else {
            try {
               this.brigadierManagerHolder = new LegacyPaperBrigadier(this);
               Bukkit.getPluginManager().registerEvents((Listener)this.brigadierManagerHolder, this.owningPlugin());
               this.brigadierManagerHolder.brigadierManager().settings().set(BrigadierSetting.FORCE_EXECUTABLE, true);
            } catch (Exception var3) {
               throw new BukkitCommandManager.BrigadierInitializationException("Failed to register LegacyPaperBrigadier", var3);
            }
         }

      }
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   public boolean hasBrigadierManager() {
      return this.brigadierManagerHolder != null || super.hasBrigadierManager();
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public CloudBrigadierManager<C, ?> brigadierManager() {
      return this.brigadierManagerHolder != null ? this.brigadierManagerHolder.brigadierManager() : super.brigadierManager();
   }

   public void registerAsynchronousCompletions() throws IllegalStateException {
      this.requireState(RegistrationState.BEFORE_REGISTRATION);
      if (!this.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
         throw new IllegalStateException("Failed to register asynchronous command completion listener.");
      } else {
         SuggestionListenerFactory<C> suggestionListenerFactory = SuggestionListenerFactory.create(this);
         SuggestionListener<C> suggestionListener = suggestionListenerFactory.createListener();
         Bukkit.getServer().getPluginManager().registerEvents(suggestionListener, this.owningPlugin());
      }
   }
}
