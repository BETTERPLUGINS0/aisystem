package com.nisovin.shopkeepers.commands.lib;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.fallback.FallbackArgument;
import com.nisovin.shopkeepers.commands.lib.argument.fallback.FallbackArgumentException;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentRejectedException;
import com.nisovin.shopkeepers.commands.lib.context.BufferedCommandContext;
import com.nisovin.shopkeepers.commands.lib.context.CommandContext;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.lib.context.SimpleCommandContext;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.MapUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import com.nisovin.shopkeepers.util.text.MessageArguments;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.Supplier;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class Command {
   public static final String COMMAND_PREFIX = "/";
   public static final String ARGUMENTS_SEPARATOR = " ";
   private static final Text DEFAULT_HELP_TITLE_FORMAT;
   private static final Text DEFAULT_HELP_USAGE_FORMAT;
   private static final Text DEFAULT_HELP_DESC_FORMAT;
   private static final Text HELP_ENTRY_FORMAT;
   private final String name;
   private final List<? extends String> aliases;
   private Text description;
   @Nullable
   private String permission;
   private final List<CommandArgument<?>> arguments;
   @Nullable
   private Command parent;
   private final CommandRegistry childCommands;
   private boolean hiddenInParentHelp;
   private boolean hiddenInOwnHelp;
   private boolean includeChildsInParentHelp;
   private final MessageArguments commonMessageArgs;
   @Nullable
   private Text helpTitleFormat;
   @Nullable
   private Text helpUsageFormat;
   @Nullable
   private Text helpDescFormat;
   @Nullable
   private Text helpChildUsageFormat;
   @Nullable
   private Text helpChildDescFormat;
   private static final Text MSG_COMMAND_SOURCE_REJECTED;

   public Command(String name) {
      this(name, Collections.emptyList());
   }

   public Command(String name, List<? extends String> aliases) {
      this.description = Text.EMPTY;
      this.permission = null;
      this.arguments = new ArrayList();
      this.parent = null;
      this.childCommands = (CommandRegistry)Unsafe.initialized(new CommandRegistry(this));
      this.hiddenInParentHelp = false;
      this.hiddenInOwnHelp = false;
      this.includeChildsInParentHelp = false;
      Map<String, Supplier<?>> commonMessageArgs = new HashMap();
      Command var10002 = (Command)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      commonMessageArgs.put("name", var10002::getName);
      var10002 = (Command)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      commonMessageArgs.put("description", var10002::getDescription);
      var10002 = (Command)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      commonMessageArgs.put("command", var10002::getCommandFormat);
      var10002 = (Command)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      commonMessageArgs.put("usage", var10002::getUsageFormat);
      var10002 = (Command)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      commonMessageArgs.put("arguments", var10002::getArgumentsFormat);
      this.commonMessageArgs = MessageArguments.ofMap(commonMessageArgs);
      this.helpTitleFormat = null;
      this.helpUsageFormat = null;
      this.helpDescFormat = null;
      this.helpChildUsageFormat = null;
      this.helpChildDescFormat = null;
      Validate.notEmpty(name, "name is null or empty");
      Validate.notNull(aliases, (String)"aliases is null");
      this.name = name;
      if (aliases.isEmpty()) {
         this.aliases = Collections.emptyList();
      } else {
         List<String> aliasesCopy = new ArrayList(aliases);
         Iterator var4 = aliasesCopy.iterator();

         while(var4.hasNext()) {
            String alias = (String)var4.next();
            Validate.notEmpty(alias, "aliases contains null or empty alias");
            Validate.isTrue(!StringUtils.containsWhitespace(alias), "aliases contains alias with whitespace");
         }

         this.aliases = Collections.unmodifiableList(aliasesCopy);
      }

   }

   public final String getName() {
      return this.name;
   }

   public final List<? extends String> getAliases() {
      return this.aliases;
   }

   public final Text getDescription() {
      return this.description;
   }

   protected final void setDescription(@Nullable Text description) {
      this.description = description == null ? Text.EMPTY : description;
   }

   @Nullable
   public final String getPermission() {
      return this.permission;
   }

   protected final void setPermission(@Nullable String permission) {
      this.permission = permission;
   }

   public boolean testPermission(CommandSender sender) {
      Validate.notNull(sender, (String)"sender is null");
      return this.permission != null ? PermissionUtils.hasPermission(sender, this.permission) : true;
   }

   public void checkPermission(CommandSender sender) throws NoPermissionException {
      Validate.notNull(sender, (String)"sender is null");
      if (!this.testPermission(sender)) {
         throw this.noPermissionException();
      }
   }

   public void checkPermission(CommandSender sender, @Nullable String permission) throws NoPermissionException {
      Validate.notNull(sender, (String)"sender is null");
      if (permission != null && !PermissionUtils.hasPermission(sender, permission)) {
         throw this.noPermissionException();
      }
   }

   protected NoPermissionException noPermissionException() {
      return new NoPermissionException(Messages.noPermission);
   }

   public boolean isAccepted(CommandSender sender) {
      Validate.notNull(sender, (String)"sender is null");
      return true;
   }

   public void checkCommandSource(CommandSender sender) throws CommandSourceRejectedException {
      Validate.notNull(sender, (String)"sender is null");
      if (!this.isAccepted(sender)) {
         throw new CommandSourceRejectedException(MSG_COMMAND_SOURCE_REJECTED);
      }
   }

   public final String getCommandFormat() {
      if (this.parent != null) {
         String var10000 = this.parent.getCommandFormat();
         return var10000 + " " + this.getName();
      } else {
         return "/" + this.getName();
      }
   }

   public final List<? extends CommandArgument<?>> getArguments() {
      return Collections.unmodifiableList(this.arguments);
   }

   @Nullable
   public final CommandArgument<?> getArgument(String name) {
      Iterator var2 = this.arguments.iterator();

      CommandArgument argument;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         argument = (CommandArgument)var2.next();
      } while(!argument.getName().equals(name));

      return argument;
   }

   protected final void addArgument(CommandArgument<?> argument) {
      Validate.notNull(argument, (String)"argument is null");
      boolean var10000 = this.getArgument(argument.getName()) == null;
      String var10001 = argument.getName();
      Validate.isTrue(var10000, "There is already another argument with this name: " + var10001);
      Validate.isTrue(argument.getParent() == null, "argument already has a parent");
      argument.setParent((CommandArgument)null);
      this.arguments.add(argument);
   }

   public final String getArgumentsFormat() {
      if (this.arguments.isEmpty()) {
         return "";
      } else {
         StringBuilder argumentsFormat = new StringBuilder();
         Iterator var2 = this.arguments.iterator();

         while(var2.hasNext()) {
            CommandArgument<?> argument = (CommandArgument)var2.next();
            String argumentFormat = argument.getFormat();
            if (!argumentFormat.isEmpty()) {
               argumentsFormat.append(argumentFormat).append(" ");
            }
         }

         if (argumentsFormat.length() == 0) {
            return "";
         } else {
            return argumentsFormat.substring(0, argumentsFormat.length() - " ".length());
         }
      }
   }

   public final String getUsageFormat() {
      String usageFormat = this.getCommandFormat();
      String argsFormat = this.getArgumentsFormat();
      if (!argsFormat.isEmpty()) {
         usageFormat = usageFormat + " " + argsFormat;
      }

      return usageFormat;
   }

   public final MessageArguments getCommonMessageArgs() {
      return this.commonMessageArgs;
   }

   @Nullable
   public final Command getParent() {
      return this.parent;
   }

   final void setParent(@Nullable Command parent) {
      this.parent = parent;
   }

   public final Command getRootCommand() {
      return this.parent != null ? this.parent.getRootCommand() : this;
   }

   public final CommandRegistry getChildCommands() {
      return this.childCommands;
   }

   @Nullable
   protected Command getChildCommand(ArgumentsReader argsReader) {
      String childCommandAlias = argsReader.peekIfPresent();
      if (childCommandAlias != null) {
         Command childcommand = this.getChildCommands().getCommand(childCommandAlias);
         if (childcommand != null) {
            argsReader.next();
            return childcommand;
         }
      }

      return null;
   }

   public void handleCommand(CommandInput input) {
      Validate.notNull(input, (String)"input is null");
      Validate.isTrue(input.getCommand() == this.getRootCommand(), "input is meant for a different command");
      CommandSender sender = input.getSender();
      CommandContext context = new SimpleCommandContext();
      ArgumentsReader argsReader = new ArgumentsReader(input);

      try {
         this.processCommand(input, context, argsReader);
         Log.debug(DebugOptions.commands, () -> {
            return "Command succeeded. Context: " + String.valueOf(context);
         });
      } catch (CommandException var6) {
         TextUtils.sendMessage(sender, var6.getMessageText());
         Log.debug(DebugOptions.commands, () -> {
            StringBuilder sb = new StringBuilder();
            sb.append("Command failed. Argument chain: ");
            CommandArgument<?> argument = null;
            if (var6 instanceof ArgumentParseException) {
               argument = ((ArgumentParseException)var6).getArgument();
            }

            sb.append(this.getArgumentChain(argument));
            return sb.toString();
         });
         Log.debug(DebugOptions.commands, (String)"Command exception: ", (Throwable)var6);
         Log.debug(DebugOptions.commands, () -> {
            return "Context: " + String.valueOf(context);
         });
         Log.debug(DebugOptions.commands, () -> {
            return "ArgumentsReader: " + String.valueOf(argsReader);
         });
      } catch (Exception var7) {
         TextUtils.sendMessage(sender, (Text)Text.color(ChatColor.RED).text("An error occurred during command handling! Check the console log."));
         Log.severe((String)"An error occurred during command handling!", (Throwable)var7);
         Log.severe("Context: " + String.valueOf(context));
      }

   }

   private String getArgumentChain(@Nullable CommandArgument<?> argument) {
      if (argument == null) {
         return "-";
      } else {
         String delimiter = " < ";
         StringBuilder sb = new StringBuilder();

         for(CommandArgument currentArgument = argument; currentArgument != null; currentArgument = currentArgument.getParent()) {
            sb.append(currentArgument.getClass().getName());
            sb.append(" (");
            sb.append(currentArgument.getName());
            sb.append(")");
            sb.append(delimiter);
         }

         return sb.substring(0, sb.length() - delimiter.length());
      }
   }

   public void processCommand(CommandInput input) throws CommandException {
      Validate.notNull(input, (String)"input is null");
      Validate.isTrue(input.getCommand() == this.getRootCommand(), "input is meant for a different command");
      CommandContext context = new SimpleCommandContext();
      ArgumentsReader argsReader = new ArgumentsReader(input);
      this.processCommand(input, context, argsReader);
   }

   protected void processCommand(CommandInput input, CommandContext context, ArgumentsReader argsReader) throws CommandException {
      assert input != null && context != null && argsReader != null;

      assert input.getCommand() == this.getRootCommand();

      assert argsReader.getArgs() == input.getArguments();

      Command childCommand = this.getChildCommand(argsReader);
      if (childCommand != null) {
         childCommand.processCommand(input, context, argsReader);
      } else {
         CommandSender sender = input.getSender();
         this.checkCommandSource(sender);
         this.checkPermission(sender);
         this.parseArguments(input, context, argsReader);
         this.execute(input, context.getView());
      }

   }

   protected void parseArguments(CommandInput input, CommandContext context, ArgumentsReader argsReader) throws ArgumentParseException {
      int argumentsCount = this.arguments.size();

      Command.ParsingContext parsingContext;
      for(parsingContext = new Command.ParsingContext(input, context, argsReader, argumentsCount); parsingContext.currentArgumentIndex < argumentsCount; ++parsingContext.currentArgumentIndex) {
         CommandArgument<?> argument = (CommandArgument)this.arguments.get(parsingContext.currentArgumentIndex);
         this.parseArgument(argument, parsingContext);
         ArgumentRejectedException argumentRejectedException = parsingContext.getCurrentArgumentRejectedException();
         if (argumentRejectedException != null) {
            Log.debug(DebugOptions.commands, () -> {
               String var10000 = argument.getName();
               return "Argument rejected  '" + var10000 + "': " + argumentRejectedException.getMessage();
            });
            throw argumentRejectedException;
         }

         this.handleFallbacks(parsingContext);
         ArgumentParseException parseException = parsingContext.currentParseException;
         if (parseException != null) {
            if (parsingContext.overrideParseException != null) {
               throw parsingContext.overrideParseException;
            }

            throw parseException;
         }
      }

      this.handleUnparsedArguments(parsingContext);
   }

   protected void parseArgument(CommandArgument<?> argument, Command.ParsingContext parsingContext) {
      CommandContext context = parsingContext.context;
      ArgumentsReader argsReader = parsingContext.argsReader;
      ArgumentsReader argsReaderState = argsReader.createSnapshot();
      ArgumentParseException parseException = null;

      try {
         argument.parse(parsingContext.input, context, argsReader);
      } catch (FallbackArgumentException var10) {
         Log.debug(DebugOptions.commands, () -> {
            String var10000 = argument.getName();
            return "Fallback for argument '" + var10000 + "': " + var10.getMessage();
         });
         argsReader.setState(argsReaderState);
         BufferedCommandContext bufferedContext = new BufferedCommandContext(context);
         parsingContext.context = bufferedContext;
         Command.Fallback fallback = new Command.Fallback(parsingContext.currentArgumentIndex, var10, bufferedContext, argsReaderState);
         parsingContext.appendFallback(fallback);
      } catch (ArgumentParseException var11) {
         argsReader.setState(argsReaderState);
         parseException = var11;
      }

      assert !(parseException instanceof FallbackArgumentException);

      parsingContext.currentParseException = parseException;
   }

   protected void handleFallbacks(Command.ParsingContext parsingContext) throws ArgumentParseException {
      Command.Fallback fallback = parsingContext.getPendingFallback();
      if (fallback != null) {
         ArgumentParseException parseException = parsingContext.currentParseException;

         assert !(parseException instanceof FallbackArgumentException);

         boolean currentParsingFailed = parseException != null;
         boolean hasUnparsedCommandArguments = parsingContext.hasUnparsedCommandArguments();
         if (currentParsingFailed || !hasUnparsedCommandArguments) {
            assert currentParsingFailed || !hasUnparsedCommandArguments;

            ArgumentsReader argsReader = parsingContext.argsReader;
            boolean parsingFailed = currentParsingFailed || argsReader.hasNext();
            parsingContext.pendingFallback = null;
            parsingContext.context = parsingContext.rootContext;
            ArgumentsReader prevArgsState = argsReader.createSnapshot();
            if (parsingFailed) {
               argsReader.setState(fallback.getOriginalArgsReader());
            } else {
               assert !currentParsingFailed && !hasUnparsedCommandArguments && !argsReader.hasNext();
            }

            parsingContext.setOverrideParseException(fallback.argumentIndex, (ArgumentParseException)null);
            FallbackArgument<?> fallbackArgument = fallback.getFallbackArgument();
            ArgumentParseException fallbackError = null;
            boolean hasRemainingArgs = argsReader.hasNext();
            ArgumentsReader argsReaderState = argsReader.createSnapshot();

            try {
               fallbackArgument.parseFallback(parsingContext.input, parsingContext.context, argsReader, fallback.exception, parsingFailed);
            } catch (FallbackArgumentException var14) {
               String var10000 = fallbackArgument.getName();
               throw Validate.State.error("Argument '" + var10000 + "' threw another FallbackArgumentException while parsing fallback: " + String.valueOf(var14));
            } catch (ArgumentParseException var15) {
               argsReader.setState(argsReaderState);
               fallbackError = var15;
            }

            boolean fallbackConsumedArgs = argsReaderState.getCursor() != argsReader.getCursor();

            assert fallbackError == null || !fallbackConsumedArgs;

            if (hasRemainingArgs && !fallbackConsumedArgs) {
               if (fallbackError != null) {
                  fallbackError = fallback.exception.getRootException();
               } else {
                  parsingContext.setOverrideParseException(fallback.argumentIndex, fallback.exception.getRootException());
               }
            }

            if (fallbackError != null) {
               parsingContext.currentArgumentIndex = fallback.argumentIndex;
               parsingContext.currentParseException = fallbackError;
            } else if (!parsingFailed) {
               fallback.getBufferedContext().applyBuffer(parsingContext.context);
               argsReader.setState(prevArgsState);
               parsingContext.pendingFallback = fallback.getNextFallback();
               this.handleFallbacks(parsingContext);
            } else {
               parsingContext.currentArgumentIndex = fallback.argumentIndex;
               parsingContext.currentParseException = null;
            }
         }
      }
   }

   private void handleUnparsedArguments(Command.ParsingContext parsingContext) throws ArgumentParseException {
      ArgumentsReader argsReader = parsingContext.argsReader;
      if (argsReader.getRemainingSize() != 0) {
         if (parsingContext.overrideParseException != null) {
            throw parsingContext.overrideParseException;
         } else {
            String firstUnparsedArg = argsReader.peek();
            if (!this.getChildCommands().getCommands().isEmpty()) {
               throw new ArgumentParseException((CommandArgument)null, this.getUnknownCommandMessage(firstUnparsedArg));
            } else {
               CommandContext context = parsingContext.context;
               CommandArgument<?> firstUnparsedArgument = null;

               CommandArgument argument;
               for(ListIterator argumentsIter = this.arguments.listIterator(this.arguments.size()); argumentsIter.hasPrevious(); firstUnparsedArgument = argument) {
                  argument = (CommandArgument)argumentsIter.previous();

                  assert argument != null;

                  if (context.has(argument.getName())) {
                     break;
                  }
               }

               if (firstUnparsedArgument != null) {
                  throw firstUnparsedArgument.invalidArgumentError(firstUnparsedArg);
               } else {
                  Text errorMsg = Messages.commandArgumentUnexpected;
                  errorMsg.setPlaceholderArguments("argument", firstUnparsedArg);
                  throw new ArgumentParseException((CommandArgument)null, errorMsg);
               }
            }
         }
      }
   }

   protected Text getUnknownCommandMessage(String command) {
      Text text = Messages.commandUnknown;
      text.setPlaceholderArguments("command", command);
      return text;
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      this.sendHelp(input.getSender());
   }

   public List<? extends String> handleTabCompletion(CommandInput input) {
      Validate.notNull(input, (String)"input is null");
      Validate.isTrue(input.getCommand() == this.getRootCommand(), "input is meant for a different command");
      CommandContext commandContext = new SimpleCommandContext();
      ArgumentsReader argsReader = new ArgumentsReader(input);
      return this.handleTabCompletion(input, commandContext, argsReader);
   }

   protected List<? extends String> handleTabCompletion(CommandInput input, CommandContext context, ArgumentsReader argsReader) {
      assert input != null && context != null && argsReader != null;

      assert input.getCommand() == this.getRootCommand();

      assert argsReader.getArgs() == input.getArguments();

      Command childCommand = this.getChildCommand(argsReader);
      if (childCommand != null) {
         return childCommand.handleTabCompletion(input, context, argsReader);
      } else {
         CommandSender sender = input.getSender();
         if (!this.isAccepted(sender)) {
            return Collections.emptyList();
         } else if (!this.testPermission(sender)) {
            return Collections.emptyList();
         } else {
            List<String> suggestions = new ArrayList();
            if (argsReader.getRemainingSize() == 1) {
               String finalArgument = CommandUtils.normalize(argsReader.peek());
               Command lastMatchingCommand = null;
               Iterator var9 = this.getChildCommands().getAliasesMap().entrySet().iterator();

               label87:
               while(true) {
                  String alias;
                  Command aliasCommand;
                  do {
                     if (!var9.hasNext()) {
                        break label87;
                     }

                     Entry<? extends String, ? extends Command> aliasEntry = (Entry)var9.next();
                     alias = (String)aliasEntry.getKey();
                     aliasCommand = (Command)aliasEntry.getValue();
                  } while(lastMatchingCommand != null && lastMatchingCommand == aliasCommand);

                  lastMatchingCommand = null;
                  if (alias.startsWith(finalArgument)) {
                     lastMatchingCommand = aliasCommand;
                     if (aliasCommand.testPermission(sender)) {
                        suggestions.add(alias);
                     }
                  }
               }
            }

            CommandContextView contextView = context.getView();
            Iterator var16 = this.arguments.iterator();

            while(var16.hasNext()) {
               CommandArgument<?> argument = (CommandArgument)var16.next();
               int remainingArgs = argsReader.getRemainingSize();
               if (remainingArgs == 0) {
                  break;
               }

               ArgumentsReader argsReaderState = argsReader.createSnapshot();

               try {
                  argument.parse(input, context, argsReader);
                  if (!argsReader.hasNext()) {
                     argsReader.setState(argsReaderState);
                     suggestions.addAll(argument.complete(input, contextView, argsReader));
                     break;
                  }

                  if (argsReader.getRemainingSize() == remainingArgs) {
                     suggestions.addAll(argument.complete(input, contextView, argsReader));
                     argsReader.setState(argsReaderState);
                  }
               } catch (FallbackArgumentException var13) {
                  argsReader.setState(argsReaderState);
                  suggestions.addAll(argument.complete(input, contextView, argsReader));
                  argsReader.setState(argsReaderState);
               } catch (ArgumentParseException var14) {
                  if (!argument.getReducedFormat().isEmpty()) {
                     argsReader.setState(argsReaderState);
                     suggestions.addAll(argument.complete(input, contextView, argsReader));
                     break;
                  }

                  argsReader.setState(argsReaderState);
                  suggestions.addAll(argument.complete(input, contextView, argsReader));
                  argsReader.setState(argsReaderState);
               }
            }

            return Collections.unmodifiableList(suggestions);
         }
      }
   }

   public final boolean isIncludeChildsInParentHelp() {
      return this.includeChildsInParentHelp;
   }

   protected final void setIncludeChildsInParentHelp(boolean includeChilds) {
      this.includeChildsInParentHelp = includeChilds;
   }

   public final boolean isHiddenInParentHelp() {
      return this.hiddenInParentHelp;
   }

   protected final void setHiddenInParentHelp(boolean hiddenInParentHelp) {
      this.hiddenInParentHelp = hiddenInParentHelp;
   }

   public final boolean isHiddenInOwnHelp() {
      return this.hiddenInOwnHelp;
   }

   protected final void setHiddenInOwnHelp(boolean hiddenInOwnHelp) {
      this.hiddenInOwnHelp = hiddenInOwnHelp;
   }

   protected void setHelpTitleFormat(Text helpTitleFormat) {
      this.helpTitleFormat = helpTitleFormat;
   }

   protected void setHelpUsageFormat(Text helpUsageFormat) {
      this.helpUsageFormat = helpUsageFormat;
   }

   protected void setHelpDescFormat(Text helpDescFormat) {
      this.helpDescFormat = helpDescFormat;
   }

   protected void setHelpChildUsageFormat(Text helpChildUsageFormat) {
      this.helpChildUsageFormat = helpChildUsageFormat;
   }

   protected void setHelpChildDescFormat(Text helpChildDescFormat) {
      this.helpChildDescFormat = helpChildDescFormat;
   }

   protected final Text getHelpTitleFormat() {
      Text format = this.helpTitleFormat;
      if (format == null) {
         Text parentFormat;
         if (this.parent != null && !(parentFormat = this.parent.getHelpTitleFormat()).isPlainTextEmpty()) {
            format = parentFormat;
         } else {
            format = DEFAULT_HELP_TITLE_FORMAT;
         }
      }

      assert format != null;

      return format;
   }

   protected final Text getHelpUsageFormat() {
      Text format = this.helpUsageFormat;
      if (format == null) {
         Text parentFormat;
         if (this.parent != null && !(parentFormat = this.parent.getHelpUsageFormat()).isPlainTextEmpty()) {
            format = parentFormat;
         } else {
            format = DEFAULT_HELP_USAGE_FORMAT;
         }
      }

      assert format != null;

      return format;
   }

   protected final Text getHelpDescFormat() {
      Text format = this.helpDescFormat;
      if (format == null) {
         Text parentFormat;
         if (this.parent != null && !(parentFormat = this.parent.getHelpDescFormat()).isPlainTextEmpty()) {
            format = parentFormat;
         } else {
            format = DEFAULT_HELP_DESC_FORMAT;
         }
      }

      assert format != null;

      return format;
   }

   protected final Text getHelpChildUsageFormat() {
      Text format = this.helpChildUsageFormat;
      if (format == null) {
         Text parentFormat;
         if (this.parent != null && !(parentFormat = this.parent.getHelpChildUsageFormat()).isPlainTextEmpty()) {
            format = parentFormat;
         } else {
            format = this.getHelpUsageFormat();
         }
      }

      assert format != null;

      return format;
   }

   protected final Text getHelpChildDescFormat() {
      Text format = this.helpChildDescFormat;
      if (format == null) {
         Text parentFormat;
         if (this.parent != null && !(parentFormat = this.parent.getHelpChildDescFormat()).isPlainTextEmpty()) {
            format = parentFormat;
         } else {
            format = this.getHelpDescFormat();
         }
      }

      assert format != null;

      return format;
   }

   protected final boolean hasHelpPermission(CommandSender recipient) {
      if (this.testPermission(recipient)) {
         return true;
      } else {
         Iterator var2 = this.getChildCommands().getCommands().iterator();

         Command childCommand;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            childCommand = (Command)var2.next();
         } while(!childCommand.hasHelpPermission(recipient));

         return true;
      }
   }

   public void sendHelp(CommandSender recipient) throws NoPermissionException {
      Validate.notNull(recipient, (String)"recipient is null");
      if (!this.hasHelpPermission(recipient)) {
         throw this.noPermissionException();
      } else {
         MessageArguments commonMsgArgs = this.getCommonMessageArgs();
         Text titleFormat = this.getHelpTitleFormat();

         assert titleFormat != null;

         if (!titleFormat.isPlainTextEmpty()) {
            TextUtils.sendMessage(recipient, titleFormat, commonMsgArgs);
         }

         Text usageFormat;
         Text descriptionFormat;
         if (!this.isHiddenInOwnHelp() && this.testPermission(recipient)) {
            usageFormat = this.getHelpUsageFormat();

            assert usageFormat != null;

            usageFormat.setPlaceholderArguments(commonMsgArgs);
            Text description = this.getDescription();
            if (description.isPlainTextEmpty()) {
               descriptionFormat = Text.EMPTY;
            } else {
               descriptionFormat = this.getHelpDescFormat();

               assert descriptionFormat != null;

               descriptionFormat.setPlaceholderArguments(commonMsgArgs);
            }

            Text helpEntryFormat = HELP_ENTRY_FORMAT;
            helpEntryFormat.setPlaceholderArguments(MapUtils.createMap("usage", usageFormat, "description", descriptionFormat));
            if (!helpEntryFormat.isPlainTextEmpty()) {
               TextUtils.sendMessage(recipient, helpEntryFormat);
            }
         }

         usageFormat = this.getHelpChildUsageFormat();
         descriptionFormat = this.getHelpChildDescFormat();
         this.sendChildCommandsHelp(recipient, usageFormat, descriptionFormat, this);
      }
   }

   protected void sendChildCommandsHelp(CommandSender recipient, @Nullable Text childUsageFormat, @Nullable Text childDescFormat, Command command) {
      Validate.notNull(recipient, (String)"recipient is null");
      if (childUsageFormat != null && !childUsageFormat.isPlainTextEmpty()) {
         boolean childDescFormatEmpty = childDescFormat == null || childDescFormat.isPlainTextEmpty();
         Iterator var6 = command.getChildCommands().getCommands().iterator();

         while(var6.hasNext()) {
            Command childCommand = (Command)var6.next();
            if (!childCommand.isHiddenInParentHelp() && childCommand.testPermission(recipient)) {
               MessageArguments childCommonMsgArgs = childCommand.getCommonMessageArgs();
               childUsageFormat.setPlaceholderArguments(childCommonMsgArgs);
               Text childDescription = childCommand.getDescription();
               Text childDescriptionFormat;
               if (!childDescFormatEmpty && !childDescription.isPlainTextEmpty()) {
                  childDescriptionFormat = (Text)Unsafe.assertNonNull(childDescFormat);
                  childDescriptionFormat.setPlaceholderArguments(childCommonMsgArgs);
               } else {
                  childDescriptionFormat = Text.EMPTY;
               }

               Text helpEntryFormat = HELP_ENTRY_FORMAT;

               assert childUsageFormat != null;

               helpEntryFormat.setPlaceholderArguments(MapUtils.createMap("usage", childUsageFormat, "description", childDescriptionFormat));
               TextUtils.sendMessage(recipient, helpEntryFormat);
            }

            if (childCommand.isIncludeChildsInParentHelp()) {
               this.sendChildCommandsHelp(recipient, childUsageFormat, childDescFormat, childCommand);
            }
         }

      }
   }

   static {
      DEFAULT_HELP_TITLE_FORMAT = Text.color(ChatColor.AQUA).text("-------[ ").color(ChatColor.DARK_GREEN).text("Command Help: ").color(ChatColor.GOLD).formatting(ChatColor.ITALIC).placeholder("command").color(ChatColor.AQUA).text(" ]-------").buildRoot();
      DEFAULT_HELP_USAGE_FORMAT = Text.color(ChatColor.YELLOW).placeholder("usage").buildRoot();
      DEFAULT_HELP_DESC_FORMAT = Text.color(ChatColor.DARK_GRAY).text(" - ").color(ChatColor.DARK_AQUA).placeholder("description").buildRoot();
      HELP_ENTRY_FORMAT = Text.placeholder("usage").placeholder("description").buildRoot();
      MSG_COMMAND_SOURCE_REJECTED = Text.of("You cannot execute this command here!");
   }

   protected static class ParsingContext {
      public final CommandInput input;
      public final CommandContext rootContext;
      public CommandContext context;
      public final ArgumentsReader argsReader;
      public final int argumentsCount;
      public int currentArgumentIndex = 0;
      @Nullable
      public ArgumentParseException currentParseException = null;
      @Nullable
      private Command.Fallback pendingFallback = null;
      @Nullable
      private ArgumentParseException overrideParseException = null;
      private int overrideParseExceptionArgumentIndex = -1;

      protected ParsingContext(CommandInput input, CommandContext context, ArgumentsReader argsReader, int argumentsCount) {
         assert input != null && context != null && argsReader != null && argumentsCount >= 0;

         this.input = input;
         this.rootContext = context;
         this.context = context;
         this.argsReader = argsReader;
         this.argumentsCount = argumentsCount;
      }

      public boolean hasPendingFallback() {
         return this.pendingFallback != null;
      }

      @Nullable
      public Command.Fallback getPendingFallback() {
         return this.pendingFallback;
      }

      @Nullable
      public Command.Fallback getLastPendingFallback() {
         Command.Fallback fallback;
         for(fallback = this.pendingFallback; fallback != null && fallback.getNextFallback() != null; fallback = fallback.getNextFallback()) {
         }

         return fallback;
      }

      public boolean hasUnparsedCommandArguments() {
         return this.currentArgumentIndex < this.argumentsCount - 1;
      }

      public void setOverrideParseException(int argumentIndex, @Nullable ArgumentParseException overrideParseException) {
         if (this.overrideParseException == null || argumentIndex <= this.overrideParseExceptionArgumentIndex) {
            this.overrideParseException = overrideParseException;
            this.overrideParseExceptionArgumentIndex = argumentIndex;
         }
      }

      public void appendFallback(Command.Fallback fallback) {
         if (this.pendingFallback == null) {
            this.pendingFallback = fallback;
         } else {
            assert this.pendingFallback != null;

            this.pendingFallback.appendFallback(fallback);
         }

      }

      @Nullable
      public ArgumentRejectedException getCurrentArgumentRejectedException() {
         ArgumentParseException fallbackRootException = this.currentParseException;
         if (fallbackRootException instanceof ArgumentRejectedException) {
            ArgumentRejectedException argumentRejectedException = (ArgumentRejectedException)fallbackRootException;
            return argumentRejectedException;
         } else {
            Command.Fallback lastPendingFallback = this.getLastPendingFallback();
            if (lastPendingFallback != null) {
               fallbackRootException = lastPendingFallback.getException().getRootException();
               if (fallbackRootException instanceof ArgumentRejectedException) {
                  ArgumentRejectedException argumentRejectedException = (ArgumentRejectedException)fallbackRootException;
                  return argumentRejectedException;
               }
            }

            return null;
         }
      }
   }

   protected static class Fallback {
      protected final int argumentIndex;
      private final FallbackArgumentException exception;
      private final BufferedCommandContext bufferedContext;
      private final ArgumentsReader originalArgsReader;
      @Nullable
      private Command.Fallback nextPendingFallback;

      protected Fallback(int argumentIndex, FallbackArgumentException exception, BufferedCommandContext bufferedContext, ArgumentsReader originalArgsReader) {
         assert exception != null && bufferedContext != null && originalArgsReader != null;

         this.argumentIndex = argumentIndex;
         this.exception = exception;
         this.bufferedContext = bufferedContext;
         this.originalArgsReader = originalArgsReader;
      }

      public FallbackArgumentException getException() {
         return this.exception;
      }

      public FallbackArgument<?> getFallbackArgument() {
         return this.exception.getArgument();
      }

      public BufferedCommandContext getBufferedContext() {
         return this.bufferedContext;
      }

      public ArgumentsReader getOriginalArgsReader() {
         return this.originalArgsReader;
      }

      public void appendFallback(Command.Fallback fallback) {
         Command.Fallback end = this;

         for(Command.Fallback next = this.nextPendingFallback; next != null; next = next.nextPendingFallback) {
            end = next;
         }

         end.nextPendingFallback = fallback;
      }

      @Nullable
      public Command.Fallback getNextFallback() {
         return this.nextPendingFallback;
      }
   }
}
