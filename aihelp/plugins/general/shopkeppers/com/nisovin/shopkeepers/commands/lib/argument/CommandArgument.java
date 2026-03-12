package com.nisovin.shopkeepers.commands.lib.argument;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.DefaultValueFallback;
import com.nisovin.shopkeepers.commands.lib.arguments.OptionalArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.TransformedArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContext;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.text.MessageArguments;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class CommandArgument<T> {
   public static final int MAX_SUGGESTIONS = 20;
   public static final String REQUIRED_FORMAT_PREFIX = "<";
   public static final String REQUIRED_FORMAT_SUFFIX = ">";
   public static final String OPTIONAL_FORMAT_PREFIX = "[";
   public static final String OPTIONAL_FORMAT_SUFFIX = "]";
   private final String name;
   @Nullable
   private String displayName = null;
   @Nullable
   private Optional<CommandArgument<?>> parent = null;
   private final MessageArguments defaultErrorMsgArgs = this.setupDefaultErrorMsgArgs();

   public CommandArgument(String name) {
      Validate.notEmpty(name, "name is null or empty");
      Validate.isTrue(!StringUtils.containsWhitespace(name), "name contains whitespace");
      this.name = name;
   }

   public final String getName() {
      return this.name;
   }

   public final String getDisplayName() {
      return this.displayName != null ? this.displayName : this.name;
   }

   public CommandArgument<T> setDisplayName(@Nullable String displayName) {
      if (displayName != null) {
         Validate.notEmpty(displayName, "displayName is empty");
         Validate.isTrue(!StringUtils.containsWhitespace(displayName), "displayName contains whitespace");
      }

      if (this.getName().equals(displayName)) {
         this.displayName = null;
      } else {
         this.displayName = displayName;
      }

      return this;
   }

   public final void setParent(@Nullable CommandArgument<?> parent) {
      Validate.State.isTrue(this.parent == null, "Parent has already been set!");
      Validate.isTrue(parent != this, "Cannot set parent to self!");
      this.parent = Optional.ofNullable(parent);
   }

   @Nullable
   public final CommandArgument<?> getParent() {
      return this.parent != null ? (CommandArgument)this.parent.orElse((Object)null) : null;
   }

   public final CommandArgument<?> getRootArgument() {
      CommandArgument<?> current = this;

      for(CommandArgument currentParent = this.getParent(); currentParent != null; currentParent = currentParent.getParent()) {
         current = currentParent;
      }

      return current;
   }

   public boolean isOptional() {
      return false;
   }

   public String getFormat() {
      String reducedFormat = this.getReducedFormat();
      if (reducedFormat.isEmpty()) {
         return "";
      } else {
         return this.isOptional() ? "[" + reducedFormat + "]" : "<" + reducedFormat + ">";
      }
   }

   public String getReducedFormat() {
      return this.getDisplayName();
   }

   private MessageArguments setupDefaultErrorMsgArgs() {
      Map<String, Object> args = new HashMap();
      Supplier<?> argumentNameSupplier = () -> {
         CommandArgument<?> rootArgument = ((CommandArgument)Unsafe.initialized(this)).getRootArgument();
         return rootArgument.getDisplayName();
      };
      Supplier<?> argumentFormatSupplier = () -> {
         CommandArgument<?> rootArgument = ((CommandArgument)Unsafe.initialized(this)).getRootArgument();
         String format = rootArgument.getFormat();
         return format.isEmpty() ? argumentNameSupplier.get() : format;
      };
      args.put("argumentName", argumentNameSupplier);
      args.put("argumentFormat", argumentFormatSupplier);
      return MessageArguments.ofMap(args);
   }

   public final MessageArguments getDefaultErrorMsgArgs() {
      return this.defaultErrorMsgArgs;
   }

   public Text getRequiresPlayerErrorMsg() {
      Text text = Messages.commandArgumentRequiresPlayer;
      text.setPlaceholderArguments(this.getDefaultErrorMsgArgs());
      return text;
   }

   protected final RequiresPlayerArgumentException requiresPlayerError() {
      return new RequiresPlayerArgumentException(this, this.getRequiresPlayerErrorMsg());
   }

   public Text getMissingArgumentErrorMsg() {
      Text text = Messages.commandArgumentMissing;
      text.setPlaceholderArguments(this.getDefaultErrorMsgArgs());
      return text;
   }

   public final MissingArgumentException missingArgumentError() {
      return new MissingArgumentException(this, this.getMissingArgumentErrorMsg());
   }

   protected Text getInvalidArgumentErrorMsgText() {
      return Messages.commandArgumentInvalid;
   }

   public Text getInvalidArgumentErrorMsg(String argumentInput) {
      Validate.notNull(argumentInput, (String)"argumentInput is null");
      Text text = this.getInvalidArgumentErrorMsgText();
      text.setPlaceholderArguments(this.getDefaultErrorMsgArgs());
      text.setPlaceholderArguments("argument", argumentInput);
      return text;
   }

   public final InvalidArgumentException invalidArgumentError(String argumentInput) {
      return new InvalidArgumentException(this, this.getInvalidArgumentErrorMsg(argumentInput));
   }

   public T parse(CommandInput input, CommandContext context, ArgumentsReader argsReader) throws ArgumentParseException {
      T value = this.parseValue(input, context.getView(), argsReader);
      if (value != null) {
         context.put(this.name, value);
      }

      return Unsafe.cast(value);
   }

   public abstract T parseValue(CommandInput var1, CommandContextView var2, ArgumentsReader var3) throws ArgumentParseException;

   public abstract List<? extends String> complete(CommandInput var1, CommandContextView var2, ArgumentsReader var3);

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("CommandArgument [name=");
      builder.append(this.name);
      builder.append("]");
      return builder.toString();
   }

   public final CommandArgument<T> optional() {
      return new OptionalArgument((CommandArgument)Unsafe.initialized(this));
   }

   public final CommandArgument<T> orDefaultValue(T defaultValue) {
      return new DefaultValueFallback((CommandArgument)Unsafe.initialized(this), defaultValue);
   }

   public final <R> CommandArgument<R> transformed(TransformedArgument.ArgumentTransformer<T, R> transformer) {
      return new TransformedArgument((CommandArgument)Unsafe.initialized(this), transformer);
   }
}
