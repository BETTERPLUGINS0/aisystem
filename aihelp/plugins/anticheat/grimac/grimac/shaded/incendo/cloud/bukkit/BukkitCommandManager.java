package ac.grim.grimac.shaded.incendo.cloud.bukkit;

import ac.grim.grimac.shaded.incendo.cloud.CloudCapability;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.SenderMapper;
import ac.grim.grimac.shaded.incendo.cloud.SenderMapperHolder;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.BrigadierManagerHolder;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.CloudBrigadierManager;
import ac.grim.grimac.shaded.incendo.cloud.caption.Caption;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.execution.ExecutionCoordinator;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandRegistrationHandler;
import ac.grim.grimac.shaded.incendo.cloud.state.RegistrationState;
import java.util.List;
import java.util.logging.Level;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class BukkitCommandManager<C> extends CommandManager<C> implements BrigadierManagerHolder<C, Object>, SenderMapperHolder<CommandSender, C>, PluginHolder {
   private final Plugin owningPlugin;
   private final SenderMapper<CommandSender, C> senderMapper;
   private boolean splitAliases = false;

   @API(
      status = Status.INTERNAL,
      since = "2.0.0"
   )
   protected BukkitCommandManager(@NonNull final Plugin owningPlugin, @NonNull final ExecutionCoordinator<C> commandExecutionCoordinator, @NonNull final SenderMapper<CommandSender, C> senderMapper) throws BukkitCommandManager.InitializationException {
      super(commandExecutionCoordinator, new BukkitPluginRegistrationHandler());

      try {
         ((BukkitPluginRegistrationHandler)this.commandRegistrationHandler()).initialize(this);
      } catch (ReflectiveOperationException var5) {
         throw new BukkitCommandManager.InitializationException("Failed to initialize command registration handler", var5);
      }

      this.owningPlugin = owningPlugin;
      this.senderMapper = senderMapper;
      CloudBukkitCapabilities.CAPABLE.forEach((x$0) -> {
         this.registerCapability(x$0);
      });
      this.registerCapability(CloudCapability.StandardCapabilities.ROOT_COMMAND_DELETION);
      this.registerCommandPreProcessor(new BukkitCommandPreprocessor(this));
      BukkitParsers.register(this);
      this.owningPlugin.getServer().getPluginManager().registerEvents(new CloudBukkitListener(this), this.owningPlugin);
      this.registerDefaultExceptionHandlers();
      this.captionRegistry().registerProvider(new BukkitDefaultCaptionsProvider());
   }

   @NonNull
   public final Plugin owningPlugin() {
      return this.owningPlugin;
   }

   @NonNull
   public final SenderMapper<CommandSender, C> senderMapper() {
      return this.senderMapper;
   }

   public final boolean hasPermission(@NonNull final C sender, @NonNull final String permission) {
      return permission.isEmpty() ? true : ((CommandSender)this.senderMapper.reverse(sender)).hasPermission(permission);
   }

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   protected final boolean splitAliases() {
      return this.splitAliases;
   }

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   protected final void splitAliases(final boolean value) {
      this.requireState(RegistrationState.BEFORE_REGISTRATION);
      this.splitAliases = value;
   }

   protected final void checkBrigadierCompatibility() throws BukkitCommandManager.BrigadierInitializationException {
      if (!this.hasCapability(CloudBukkitCapabilities.BRIGADIER)) {
         throw new BukkitCommandManager.BrigadierInitializationException("Missing capability " + CloudBukkitCapabilities.class.getSimpleName() + "." + CloudBukkitCapabilities.BRIGADIER + " (Minecraft version too old? Brigadier was added in 1.13). See the Javadocs for more details");
      }
   }

   public synchronized void registerBrigadier() throws BukkitCommandManager.BrigadierInitializationException {
      this.requireState(RegistrationState.BEFORE_REGISTRATION);
      this.checkBrigadierCompatibility();
      if (!this.hasCapability(CloudBukkitCapabilities.COMMODORE_BRIGADIER)) {
         throw new BukkitCommandManager.BrigadierInitializationException("Missing capability " + CloudBukkitCapabilities.class.getSimpleName() + "." + CloudBukkitCapabilities.COMMODORE_BRIGADIER + " (Minecraft version too new). See the Javadocs for more details");
      } else {
         CommandRegistrationHandler<C> handler = this.commandRegistrationHandler();
         if (handler instanceof CloudCommodoreManager) {
            throw new IllegalStateException("Brigadier is already registered! Holder: " + handler);
         } else {
            try {
               CloudCommodoreManager<C> cloudCommodoreManager = new CloudCommodoreManager(this);
               cloudCommodoreManager.initialize(this);
               this.commandRegistrationHandler(cloudCommodoreManager);
               this.splitAliases(true);
            } catch (Exception var3) {
               throw new BukkitCommandManager.BrigadierInitializationException("Unexpected exception initializing " + CloudCommodoreManager.class.getSimpleName(), var3);
            }
         }
      }
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   public boolean hasBrigadierManager() {
      return this.commandRegistrationHandler() instanceof CloudCommodoreManager;
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public CloudBrigadierManager<C, ?> brigadierManager() {
      if (this.commandRegistrationHandler() instanceof CloudCommodoreManager) {
         return ((CloudCommodoreManager)this.commandRegistrationHandler()).brigadierManager();
      } else {
         throw new BrigadierManagerHolder.BrigadierManagerNotPresent("The CloudBrigadierManager is either not supported in the current environment, or it is not enabled.");
      }
   }

   private void registerDefaultExceptionHandlers() {
      this.registerDefaultExceptionHandlers((triplet) -> {
         ((CommandSender)this.senderMapper().reverse(((CommandContext)triplet.first()).sender())).sendMessage(ChatColor.RED + ((CommandContext)triplet.first()).formatCaption((Caption)triplet.second(), (List)triplet.third()));
      }, (pair) -> {
         this.owningPlugin().getLogger().log(Level.SEVERE, (String)pair.first(), (Throwable)pair.second());
      });
   }

   final void lockIfBrigadierCapable() {
      if (this.hasCapability(CloudBukkitCapabilities.BRIGADIER)) {
         this.lockRegistration();
      }

   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   public static final class InitializationException extends IllegalStateException {
      @API(
         status = Status.INTERNAL,
         consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
      )
      public InitializationException(final String message, @Nullable final Throwable cause) {
         super(message, cause);
      }
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   public static final class BrigadierInitializationException extends IllegalStateException {
      @API(
         status = Status.INTERNAL,
         consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
      )
      public BrigadierInitializationException(@NonNull final String reason) {
         super(reason);
      }

      @API(
         status = Status.INTERNAL,
         consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
      )
      public BrigadierInitializationException(@NonNull final String reason, @Nullable final Throwable cause) {
         super(reason, cause);
      }
   }
}
