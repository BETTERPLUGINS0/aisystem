package me.PM2.infinitevehicles.commands;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.PM2.infinitevehicles.commands.annotation.Single;
import me.PM2.infinitevehicles.commands.annotation.Split;
import me.PM2.infinitevehicles.commands.annotation.Values;
import me.PM2.infinitevehicles.commands.contexts.ContextResolver;
import me.PM2.infinitevehicles.commands.contexts.IssuerAwareContextResolver;
import me.PM2.infinitevehicles.commands.contexts.IssuerOnlyContextResolver;
import me.PM2.infinitevehicles.commands.contexts.OptionalContextResolver;
import org.jetbrains.annotations.NotNull;

public class CommandContexts<R extends CommandExecutionContext<?, ? extends CommandIssuer>> {
   protected final Map<Class<?>, ContextResolver<?, R>> contextMap = new HashMap();
   protected final CommandManager manager;

   CommandContexts(CommandManager manager) {
      this.manager = var1;
      this.registerIssuerOnlyContext(CommandIssuer.class, (var0) -> {
         return var0.getIssuer();
      });
      this.registerContext(Short.class, (var1x) -> {
         String var2 = var1x.popFirstArg();

         try {
            return this.parseAndValidateNumber(var2, var1x, -32768, (short)32767).shortValue();
         } catch (NumberFormatException var4) {
            throw new InvalidCommandArgument(MessageKeys.MUST_BE_A_NUMBER, new String[]{"{num}", var2});
         }
      });
      this.registerContext(Short.TYPE, (var1x) -> {
         String var2 = var1x.popFirstArg();

         try {
            return this.parseAndValidateNumber(var2, var1x, -32768, (short)32767).shortValue();
         } catch (NumberFormatException var4) {
            throw new InvalidCommandArgument(MessageKeys.MUST_BE_A_NUMBER, new String[]{"{num}", var2});
         }
      });
      this.registerContext(Integer.class, (var1x) -> {
         String var2 = var1x.popFirstArg();

         try {
            return this.parseAndValidateNumber(var2, var1x, Integer.MIN_VALUE, Integer.MAX_VALUE).intValue();
         } catch (NumberFormatException var4) {
            throw new InvalidCommandArgument(MessageKeys.MUST_BE_A_NUMBER, new String[]{"{num}", var2});
         }
      });
      this.registerContext(Integer.TYPE, (var1x) -> {
         String var2 = var1x.popFirstArg();

         try {
            return this.parseAndValidateNumber(var2, var1x, Integer.MIN_VALUE, Integer.MAX_VALUE).intValue();
         } catch (NumberFormatException var4) {
            throw new InvalidCommandArgument(MessageKeys.MUST_BE_A_NUMBER, new String[]{"{num}", var2});
         }
      });
      this.registerContext(Long.class, (var1x) -> {
         String var2 = var1x.popFirstArg();

         try {
            return this.parseAndValidateNumber(var2, var1x, Long.MIN_VALUE, Long.MAX_VALUE).longValue();
         } catch (NumberFormatException var4) {
            throw new InvalidCommandArgument(MessageKeys.MUST_BE_A_NUMBER, new String[]{"{num}", var2});
         }
      });
      this.registerContext(Long.TYPE, (var1x) -> {
         String var2 = var1x.popFirstArg();

         try {
            return this.parseAndValidateNumber(var2, var1x, Long.MIN_VALUE, Long.MAX_VALUE).longValue();
         } catch (NumberFormatException var4) {
            throw new InvalidCommandArgument(MessageKeys.MUST_BE_A_NUMBER, new String[]{"{num}", var2});
         }
      });
      this.registerContext(Float.class, (var1x) -> {
         String var2 = var1x.popFirstArg();

         try {
            return this.parseAndValidateNumber(var2, var1x, -3.4028235E38F, Float.MAX_VALUE).floatValue();
         } catch (NumberFormatException var4) {
            throw new InvalidCommandArgument(MessageKeys.MUST_BE_A_NUMBER, new String[]{"{num}", var2});
         }
      });
      this.registerContext(Float.TYPE, (var1x) -> {
         String var2 = var1x.popFirstArg();

         try {
            return this.parseAndValidateNumber(var2, var1x, -3.4028235E38F, Float.MAX_VALUE).floatValue();
         } catch (NumberFormatException var4) {
            throw new InvalidCommandArgument(MessageKeys.MUST_BE_A_NUMBER, new String[]{"{num}", var2});
         }
      });
      this.registerContext(Double.class, (var1x) -> {
         String var2 = var1x.popFirstArg();

         try {
            return this.parseAndValidateNumber(var2, var1x, -1.7976931348623157E308D, Double.MAX_VALUE).doubleValue();
         } catch (NumberFormatException var4) {
            throw new InvalidCommandArgument(MessageKeys.MUST_BE_A_NUMBER, new String[]{"{num}", var2});
         }
      });
      this.registerContext(Double.TYPE, (var1x) -> {
         String var2 = var1x.popFirstArg();

         try {
            return this.parseAndValidateNumber(var2, var1x, -1.7976931348623157E308D, Double.MAX_VALUE).doubleValue();
         } catch (NumberFormatException var4) {
            throw new InvalidCommandArgument(MessageKeys.MUST_BE_A_NUMBER, new String[]{"{num}", var2});
         }
      });
      this.registerContext(Number.class, (var1x) -> {
         String var2 = var1x.popFirstArg();

         try {
            return this.parseAndValidateNumber(var2, var1x, -1.7976931348623157E308D, Double.MAX_VALUE);
         } catch (NumberFormatException var4) {
            throw new InvalidCommandArgument(MessageKeys.MUST_BE_A_NUMBER, new String[]{"{num}", var2});
         }
      });
      this.registerContext(BigDecimal.class, (var1x) -> {
         String var2 = var1x.popFirstArg();

         try {
            BigDecimal var3 = ACFUtil.parseBigNumber(var2, var1x.hasFlag("suffixes"));
            this.validateMinMax(var1x, var3);
            return var3;
         } catch (NumberFormatException var4) {
            throw new InvalidCommandArgument(MessageKeys.MUST_BE_A_NUMBER, new String[]{"{num}", var2});
         }
      });
      this.registerContext(BigInteger.class, (var1x) -> {
         String var2 = var1x.popFirstArg();

         try {
            BigDecimal var3 = ACFUtil.parseBigNumber(var2, var1x.hasFlag("suffixes"));
            this.validateMinMax(var1x, var3);
            return var3.toBigIntegerExact();
         } catch (NumberFormatException var4) {
            throw new InvalidCommandArgument(MessageKeys.MUST_BE_A_NUMBER, new String[]{"{num}", var2});
         }
      });
      this.registerContext(Boolean.class, (var0) -> {
         return ACFUtil.isTruthy(var0.popFirstArg());
      });
      this.registerContext(Boolean.TYPE, (var0) -> {
         return ACFUtil.isTruthy(var0.popFirstArg());
      });
      this.registerContext(Character.TYPE, (var0) -> {
         String var1 = var0.popFirstArg();
         if (var1.length() > 1) {
            throw new InvalidCommandArgument(MessageKeys.MUST_BE_MAX_LENGTH, new String[]{"{max}", String.valueOf(1)});
         } else {
            return var1.charAt(0);
         }
      });
      this.registerContext(String.class, (var0) -> {
         if (var0.hasAnnotation(Values.class)) {
            return var0.popFirstArg();
         } else {
            String var1 = var0.isLastArg() && !var0.hasAnnotation(Single.class) ? ACFUtil.join((Collection)var0.getArgs()) : var0.popFirstArg();
            Integer var2 = var0.getFlagValue("minlen", (Integer)null);
            Integer var3 = var0.getFlagValue("maxlen", (Integer)null);
            if (var2 != null && var1.length() < var2) {
               throw new InvalidCommandArgument(MessageKeys.MUST_BE_MIN_LENGTH, new String[]{"{min}", String.valueOf(var2)});
            } else if (var3 != null && var1.length() > var3) {
               throw new InvalidCommandArgument(MessageKeys.MUST_BE_MAX_LENGTH, new String[]{"{max}", String.valueOf(var3)});
            } else {
               return var1;
            }
         }
      });
      this.registerContext(String[].class, (var0) -> {
         List var2 = var0.getArgs();
         String var1;
         if (var0.isLastArg() && !var0.hasAnnotation(Single.class)) {
            var1 = ACFUtil.join((Collection)var2);
         } else {
            var1 = var0.popFirstArg();
         }

         String var3 = var0.getAnnotationValue(Split.class, 8);
         if (var3 != null) {
            if (var1.isEmpty()) {
               throw new InvalidCommandArgument();
            } else {
               return ACFPatterns.getPattern(var3).split(var1);
            }
         } else {
            if (!var0.isLastArg()) {
               ACFUtil.sneaky(new IllegalStateException("Weird Command signature... String[] should be last or @Split"));
            }

            String[] var4 = (String[])var2.toArray(new String[0]);
            var2.clear();
            return var4;
         }
      });
      this.registerContext(Enum.class, (var0) -> {
         String var1 = var0.popFirstArg();
         Class var2 = var0.getParam().getType();
         Enum var3 = ACFUtil.simpleMatch(var2, var1);
         if (var3 == null) {
            List var4 = ACFUtil.enumNames(var2);
            throw new InvalidCommandArgument(MessageKeys.PLEASE_SPECIFY_ONE_OF, new String[]{"{valid}", ACFUtil.join((Collection)var4, ", ")});
         } else {
            return var3;
         }
      });
      this.registerOptionalContext(CommandHelp.class, (var1x) -> {
         String var2 = var1x.getFirstArg();
         String var3 = var1x.getLastArg();
         Integer var4 = 1;
         List var5 = null;
         if (var3 != null && ACFUtil.isInteger(var3)) {
            var1x.popLastArg();
            var4 = ACFUtil.parseInt(var3);
            if (var4 == null) {
               throw new InvalidCommandArgument(MessageKeys.MUST_BE_A_NUMBER, new String[]{"{num}", var3});
            }

            if (!var1x.getArgs().isEmpty()) {
               var5 = var1x.getArgs();
            }
         } else if (var2 != null && ACFUtil.isInteger(var2)) {
            var1x.popFirstArg();
            var4 = ACFUtil.parseInt(var2);
            if (var4 == null) {
               throw new InvalidCommandArgument(MessageKeys.MUST_BE_A_NUMBER, new String[]{"{num}", var2});
            }

            if (!var1x.getArgs().isEmpty()) {
               var5 = var1x.getArgs();
            }
         } else if (!var1x.getArgs().isEmpty()) {
            var5 = var1x.getArgs();
         }

         CommandHelp var6 = var1.generateCommandHelp();
         var6.setPage(var4);
         Integer var7 = var1x.getFlagValue("perpage", (Integer)null);
         if (var7 != null) {
            var6.setPerPage(var7);
         }

         if (var5 != null) {
            String var8 = String.join(" ", var5);
            if (var6.testExactMatch(var8)) {
               return var6;
            }
         }

         var6.setSearch(var5);
         return var6;
      });
   }

   @NotNull
   private Number parseAndValidateNumber(String number, R c, Number minValue, Number maxValue) {
      Number var5 = ACFUtil.parseNumber(var1, var2.hasFlag("suffixes"));
      this.validateMinMax(var2, var5, var3, var4);
      return var5;
   }

   private void validateMinMax(R c, Number val) {
      this.validateMinMax(var1, var2, (Number)null, (Number)null);
   }

   private void validateMinMax(R c, Number val, Number minValue, Number maxValue) {
      Double var5 = var1.getFlagValue("min", var3);
      Double var6 = var1.getFlagValue("max", var4);
      if (var6 != null && var2.doubleValue() > var6.doubleValue()) {
         throw new InvalidCommandArgument(MessageKeys.PLEASE_SPECIFY_AT_MOST, new String[]{"{max}", String.valueOf(var6)});
      } else if (var5 != null && var2.doubleValue() < var5.doubleValue()) {
         throw new InvalidCommandArgument(MessageKeys.PLEASE_SPECIFY_AT_LEAST, new String[]{"{min}", String.valueOf(var5)});
      }
   }

   /** @deprecated */
   @Deprecated
   public <T> void registerSenderAwareContext(Class<T> context, IssuerAwareContextResolver<T, R> supplier) {
      this.contextMap.put(var1, var2);
   }

   public <T> void registerIssuerAwareContext(Class<T> context, IssuerAwareContextResolver<T, R> supplier) {
      this.contextMap.put(var1, var2);
   }

   public <T> void registerIssuerOnlyContext(Class<T> context, IssuerOnlyContextResolver<T, R> supplier) {
      this.contextMap.put(var1, var2);
   }

   public <T> void registerOptionalContext(Class<T> context, OptionalContextResolver<T, R> supplier) {
      this.contextMap.put(var1, var2);
   }

   public <T> void registerContext(Class<T> context, ContextResolver<T, R> supplier) {
      this.contextMap.put(var1, var2);
   }

   public ContextResolver<?, R> getResolver(Class<?> type) {
      Class var2 = var1;

      while(var1 != Object.class) {
         ContextResolver var3 = (ContextResolver)this.contextMap.get(var1);
         if (var3 != null) {
            return var3;
         }

         if ((var1 = var1.getSuperclass()) == null) {
            break;
         }
      }

      this.manager.log(LogLevel.ERROR, "Could not find context resolver", new IllegalStateException("No context resolver defined for " + var2.getName()));
      return null;
   }
}
