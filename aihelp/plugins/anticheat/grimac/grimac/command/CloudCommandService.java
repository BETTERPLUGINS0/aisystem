package ac.grim.grimac.command;

import ac.grim.grimac.command.commands.GrimAlerts;
import ac.grim.grimac.command.commands.GrimBrands;
import ac.grim.grimac.command.commands.GrimDebug;
import ac.grim.grimac.command.commands.GrimDump;
import ac.grim.grimac.command.commands.GrimHelp;
import ac.grim.grimac.command.commands.GrimHistory;
import ac.grim.grimac.command.commands.GrimList;
import ac.grim.grimac.command.commands.GrimLog;
import ac.grim.grimac.command.commands.GrimPerf;
import ac.grim.grimac.command.commands.GrimProfile;
import ac.grim.grimac.command.commands.GrimReload;
import ac.grim.grimac.command.commands.GrimSendAlert;
import ac.grim.grimac.command.commands.GrimSpectate;
import ac.grim.grimac.command.commands.GrimStopSpectating;
import ac.grim.grimac.command.commands.GrimTestWebhook;
import ac.grim.grimac.command.commands.GrimVerbose;
import ac.grim.grimac.command.commands.GrimVersion;
import ac.grim.grimac.command.handler.GrimCommandFailureHandler;
import ac.grim.grimac.platform.api.command.CommandService;
import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.exception.InvalidSyntaxException;
import ac.grim.grimac.shaded.incendo.cloud.key.CloudKey;
import ac.grim.grimac.shaded.incendo.cloud.processors.requirements.RequirementApplicable;
import ac.grim.grimac.shaded.incendo.cloud.processors.requirements.RequirementPostprocessor;
import ac.grim.grimac.shaded.incendo.cloud.processors.requirements.Requirements;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentLike;
import ac.grim.grimac.shaded.kyori.adventure.text.format.NamedTextColor;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import java.util.function.Function;
import java.util.function.Supplier;

public class CloudCommandService implements CommandService {
   public static final CloudKey<Requirements<Sender, SenderRequirement>> REQUIREMENT_KEY = CloudKey.of("requirements", new TypeToken<Requirements<Sender, SenderRequirement>>() {
   });
   public static final RequirementApplicable.RequirementApplicableFactory<Sender, SenderRequirement> REQUIREMENT_FACTORY;
   private boolean commandsRegistered = false;
   private final Supplier<CommandManager<Sender>> commandManagerSupplier;
   private final CloudCommandAdapter commandAdapter;

   public CloudCommandService(Supplier<CommandManager<Sender>> commandManagerSupplier, CloudCommandAdapter commandAdapter) {
      this.commandManagerSupplier = commandManagerSupplier;
      this.commandAdapter = commandAdapter;
   }

   public void registerCommands() {
      if (!this.commandsRegistered) {
         CommandManager<Sender> commandManager = (CommandManager)this.commandManagerSupplier.get();
         (new GrimPerf()).register(commandManager, this.commandAdapter);
         (new GrimDebug()).register(commandManager, this.commandAdapter);
         (new GrimAlerts()).register(commandManager, this.commandAdapter);
         (new GrimProfile()).register(commandManager, this.commandAdapter);
         (new GrimSendAlert()).register(commandManager, this.commandAdapter);
         (new GrimHelp()).register(commandManager, this.commandAdapter);
         (new GrimHistory()).register(commandManager, this.commandAdapter);
         (new GrimReload()).register(commandManager, this.commandAdapter);
         (new GrimSpectate()).register(commandManager, this.commandAdapter);
         (new GrimStopSpectating()).register(commandManager, this.commandAdapter);
         (new GrimLog()).register(commandManager, this.commandAdapter);
         (new GrimVerbose()).register(commandManager, this.commandAdapter);
         (new GrimVersion()).register(commandManager, this.commandAdapter);
         (new GrimDump()).register(commandManager, this.commandAdapter);
         (new GrimBrands()).register(commandManager, this.commandAdapter);
         (new GrimList()).register(commandManager, this.commandAdapter);
         (new GrimTestWebhook()).register(commandManager, this.commandAdapter);
         RequirementPostprocessor<Sender, SenderRequirement> senderRequirementPostprocessor = RequirementPostprocessor.of(REQUIREMENT_KEY, new GrimCommandFailureHandler());
         commandManager.registerCommandPostProcessor(senderRequirementPostprocessor);
         this.registerExceptionHandler(commandManager, InvalidSyntaxException.class, (e) -> {
            return MessageUtil.miniMessage(e.correctSyntax());
         });
         this.commandsRegistered = true;
      }
   }

   protected <E extends Exception> void registerExceptionHandler(CommandManager<Sender> commandManager, Class<E> ex, Function<E, ComponentLike> toComponent) {
      commandManager.exceptionController().registerHandler(ex, (c) -> {
         ((Sender)c.context().sender()).sendMessage(((ComponentLike)toComponent.apply((Exception)c.exception())).asComponent().colorIfAbsent(NamedTextColor.RED));
      });
   }

   static {
      REQUIREMENT_FACTORY = RequirementApplicable.factory(REQUIREMENT_KEY);
   }
}
