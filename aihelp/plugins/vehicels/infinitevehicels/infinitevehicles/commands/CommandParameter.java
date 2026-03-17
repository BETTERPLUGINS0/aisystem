package me.PM2.infinitevehicles.commands;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import me.PM2.infinitevehicles.commands.annotation.CommandPermission;
import me.PM2.infinitevehicles.commands.annotation.Conditions;
import me.PM2.infinitevehicles.commands.annotation.ConsumesRest;
import me.PM2.infinitevehicles.commands.annotation.Default;
import me.PM2.infinitevehicles.commands.annotation.Description;
import me.PM2.infinitevehicles.commands.annotation.Flags;
import me.PM2.infinitevehicles.commands.annotation.Name;
import me.PM2.infinitevehicles.commands.annotation.Optional;
import me.PM2.infinitevehicles.commands.annotation.Single;
import me.PM2.infinitevehicles.commands.annotation.Syntax;
import me.PM2.infinitevehicles.commands.annotation.Values;
import me.PM2.infinitevehicles.commands.contexts.ContextResolver;
import me.PM2.infinitevehicles.commands.contexts.IssuerAwareContextResolver;
import me.PM2.infinitevehicles.commands.contexts.IssuerOnlyContextResolver;
import me.PM2.infinitevehicles.commands.contexts.OptionalContextResolver;
import me.PM2.infinitevehicles.locales.MessageKey;

public class CommandParameter<CEC extends CommandExecutionContext<CEC, ? extends CommandIssuer>> {
   private final Parameter parameter;
   private final Class<?> type;
   private final String name;
   private final CommandManager manager;
   private final int paramIndex;
   private ContextResolver<?, CEC> resolver;
   private boolean optional;
   private Set<String> permissions = new HashSet();
   private String permission;
   private String description;
   private String defaultValue;
   private String syntax;
   private String conditions;
   private boolean requiresInput;
   private boolean commandIssuer;
   private String[] values;
   private Map<String, String> flags;
   private boolean canConsumeInput;
   private boolean optionalResolver;
   boolean consumesRest;
   private boolean isLast;
   private boolean isOptionalInput;
   private CommandParameter<CEC> nextParam;

   public CommandParameter(RegisteredCommand<CEC> command, Parameter param, int paramIndex, boolean isLast) {
      this.parameter = var2;
      this.isLast = var4;
      this.type = var2.getType();
      this.manager = var1.manager;
      this.paramIndex = var3;
      Annotations var5 = this.manager.getAnnotations();
      String var6 = var5.getAnnotationValue(var2, Name.class, 1);
      this.name = var6 != null ? var6 : var2.getName();
      this.defaultValue = var5.getAnnotationValue(var2, Default.class, 1 | (this.type != String.class ? 8 : 0));
      this.description = var5.getAnnotationValue(var2, Description.class, 17);
      this.conditions = var5.getAnnotationValue(var2, Conditions.class, 9);
      this.resolver = this.manager.getCommandContexts().getResolver(this.type);
      if (this.resolver == null) {
         ACFUtil.sneaky(new InvalidCommandContextException("Parameter " + this.type.getSimpleName() + " of " + var1 + " has no applicable context resolver"));
      }

      this.optional = var5.hasAnnotation(var2, Optional.class) || this.defaultValue != null || var4 && this.type == String[].class;
      this.permission = var5.getAnnotationValue(var2, CommandPermission.class, 9);
      this.optionalResolver = this.isOptionalResolver(this.resolver);
      this.requiresInput = !this.optional && !this.optionalResolver;
      this.commandIssuer = var3 == 0 && this.manager.isCommandIssuer(this.type);
      this.canConsumeInput = !this.commandIssuer && !(this.resolver instanceof IssuerOnlyContextResolver);
      this.consumesRest = var4 && (var5.hasAnnotation(var2, ConsumesRest.class) || this.type == String.class && !var5.hasAnnotation(var2, Single.class) || this.type == String[].class);
      this.values = var5.getAnnotationValues(var2, Values.class, 9);
      this.syntax = null;
      this.isOptionalInput = !this.requiresInput && this.canConsumeInput;
      if (!this.commandIssuer) {
         this.syntax = var5.getAnnotationValue(var2, Syntax.class);
      }

      this.flags = new HashMap();
      String var7 = var5.getAnnotationValue(var2, Flags.class, 9);
      if (var7 != null) {
         this.parseFlags(var7);
      }

      this.inheritContextFlags(var1.scope);
      this.computePermissions();
   }

   private void inheritContextFlags(BaseCommand scope) {
      if (!var1.contextFlags.isEmpty()) {
         Class var2 = this.type;

         do {
            this.parseFlags((String)var1.contextFlags.get(var2));
         } while((var2 = var2.getSuperclass()) != null);
      }

      if (var1.parentCommand != null) {
         this.inheritContextFlags(var1.parentCommand);
      }

   }

   private void parseFlags(String flags) {
      if (var1 != null) {
         String[] var2 = ACFPatterns.COMMA.split(this.manager.getCommandReplacements().replace(var1));
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            String[] var6 = ACFPatterns.EQUALS.split(var5, 2);
            if (!this.flags.containsKey(var6[0])) {
               this.flags.put(var6[0], var6.length > 1 ? var6[1] : null);
            }
         }
      }

   }

   private void computePermissions() {
      this.permissions.clear();
      if (this.permission != null && !this.permission.isEmpty()) {
         this.permissions.addAll(Arrays.asList(ACFPatterns.COMMA.split(this.permission)));
      }

   }

   private boolean isOptionalResolver(ContextResolver<?, CEC> resolver) {
      return var1 instanceof IssuerAwareContextResolver || var1 instanceof IssuerOnlyContextResolver || var1 instanceof OptionalContextResolver;
   }

   public Parameter getParameter() {
      return this.parameter;
   }

   public Class<?> getType() {
      return this.type;
   }

   public String getName() {
      return this.name;
   }

   public String getDisplayName(CommandIssuer issuer) {
      String var2 = this.manager.getLocales().getOptionalMessage(var1, MessageKey.of("acf-core.parameter." + this.name.toLowerCase()));
      return var2 != null ? var2 : this.name;
   }

   public CommandManager getManager() {
      return this.manager;
   }

   public int getParamIndex() {
      return this.paramIndex;
   }

   public ContextResolver<?, CEC> getResolver() {
      return this.resolver;
   }

   public void setResolver(ContextResolver<?, CEC> resolver) {
      this.resolver = var1;
   }

   public boolean isOptionalInput() {
      return this.isOptionalInput;
   }

   public boolean isOptional() {
      return this.optional;
   }

   public void setOptional(boolean optional) {
      this.optional = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = var1;
   }

   public String getDefaultValue() {
      return this.defaultValue;
   }

   public void setDefaultValue(String defaultValue) {
      this.defaultValue = var1;
   }

   public boolean isCommandIssuer() {
      return this.commandIssuer;
   }

   public void setCommandIssuer(boolean commandIssuer) {
      this.commandIssuer = var1;
   }

   public String[] getValues() {
      return this.values;
   }

   public void setValues(String[] values) {
      this.values = var1;
   }

   public Map<String, String> getFlags() {
      return this.flags;
   }

   public void setFlags(Map<String, String> flags) {
      this.flags = var1;
   }

   public boolean canConsumeInput() {
      return this.canConsumeInput;
   }

   public void setCanConsumeInput(boolean canConsumeInput) {
      this.canConsumeInput = var1;
   }

   public void setOptionalResolver(boolean optionalResolver) {
      this.optionalResolver = var1;
   }

   public boolean isOptionalResolver() {
      return this.optionalResolver;
   }

   public boolean requiresInput() {
      return this.requiresInput;
   }

   public void setRequiresInput(boolean requiresInput) {
      this.requiresInput = var1;
   }

   public String getSyntax() {
      return this.getSyntax((CommandIssuer)null);
   }

   public String getSyntax(CommandIssuer issuer) {
      if (this.commandIssuer) {
         return null;
      } else {
         if (this.syntax == null) {
            if (this.isOptionalInput) {
               return "[" + this.getDisplayName(var1) + "]";
            }

            if (this.requiresInput) {
               return "<" + this.getDisplayName(var1) + ">";
            }
         }

         return this.syntax;
      }
   }

   public void setSyntax(String syntax) {
      this.syntax = var1;
   }

   public String getConditions() {
      return this.conditions;
   }

   public void setConditions(String conditions) {
      this.conditions = var1;
   }

   public Set<String> getRequiredPermissions() {
      return this.permissions;
   }

   public void setNextParam(CommandParameter<CEC> nextParam) {
      this.nextParam = var1;
   }

   public CommandParameter<CEC> getNextParam() {
      return this.nextParam;
   }

   public boolean canExecuteWithoutInput() {
      return (!this.canConsumeInput || this.isOptionalInput()) && (this.nextParam == null || this.nextParam.canExecuteWithoutInput());
   }

   public boolean isLast() {
      return this.isLast;
   }
}
