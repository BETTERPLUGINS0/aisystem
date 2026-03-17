package me.PM2.infinitevehicles.commands;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandExecutionContext<CEC extends CommandExecutionContext, I extends CommandIssuer> {
   private final RegisteredCommand cmd;
   private final CommandParameter param;
   protected final I issuer;
   private final List<String> args;
   private final int index;
   private final Map<String, Object> passedArgs;
   private final Map<String, String> flags;
   private final CommandManager manager;

   CommandExecutionContext(RegisteredCommand cmd, CommandParameter param, I sender, List<String> args, int index, Map<String, Object> passedArgs) {
      this.cmd = var1;
      this.manager = var1.scope.manager;
      this.param = var2;
      this.issuer = var3;
      this.args = var4;
      this.index = var5;
      this.passedArgs = var6;
      this.flags = var2.getFlags();
   }

   public String popFirstArg() {
      return !this.args.isEmpty() ? (String)this.args.remove(0) : null;
   }

   public String popLastArg() {
      return !this.args.isEmpty() ? (String)this.args.remove(this.args.size() - 1) : null;
   }

   public String getFirstArg() {
      return !this.args.isEmpty() ? (String)this.args.get(0) : null;
   }

   public String getLastArg() {
      return !this.args.isEmpty() ? (String)this.args.get(this.args.size() - 1) : null;
   }

   public boolean isLastArg() {
      return this.cmd.parameters.length - 1 == this.index;
   }

   public int getNumParams() {
      return this.cmd.parameters.length;
   }

   public boolean canOverridePlayerContext() {
      return this.cmd.requiredResolvers >= this.args.size();
   }

   public Object getResolvedArg(String arg) {
      return this.passedArgs.get(var1);
   }

   public Object getResolvedArg(Class<?>... classes) {
      Class[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Class var5 = var2[var4];
         Iterator var6 = this.passedArgs.values().iterator();

         while(var6.hasNext()) {
            Object var7 = var6.next();
            if (var5.isInstance(var7)) {
               return var7;
            }
         }
      }

      return null;
   }

   public <T> T getResolvedArg(String key, Class<?>... classes) {
      Object var3 = this.passedArgs.get(var1);
      Class[] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Class var7 = var4[var6];
         if (var7.isInstance(var3)) {
            return var3;
         }
      }

      return null;
   }

   public Set<String> getParameterPermissions() {
      return this.param.getRequiredPermissions();
   }

   public boolean isOptional() {
      return this.param.isOptional();
   }

   public boolean hasFlag(String flag) {
      return this.flags.containsKey(var1);
   }

   public String getFlagValue(String flag, String def) {
      return (String)this.flags.getOrDefault(var1, var2);
   }

   public Integer getFlagValue(String flag, Integer def) {
      return ACFUtil.parseInt((String)this.flags.get(var1), var2);
   }

   public Long getFlagValue(String flag, Long def) {
      return ACFUtil.parseLong((String)this.flags.get(var1), var2);
   }

   public Float getFlagValue(String flag, Float def) {
      return ACFUtil.parseFloat((String)this.flags.get(var1), var2);
   }

   public Double getFlagValue(String flag, Double def) {
      return ACFUtil.parseDouble((String)this.flags.get(var1), var2);
   }

   public Integer getIntFlagValue(String flag, Number def) {
      return ACFUtil.parseInt((String)this.flags.get(var1), var2 != null ? var2.intValue() : null);
   }

   public Long getLongFlagValue(String flag, Number def) {
      return ACFUtil.parseLong((String)this.flags.get(var1), var2 != null ? var2.longValue() : null);
   }

   public Float getFloatFlagValue(String flag, Number def) {
      return ACFUtil.parseFloat((String)this.flags.get(var1), var2 != null ? var2.floatValue() : null);
   }

   public Double getDoubleFlagValue(String flag, Number def) {
      return ACFUtil.parseDouble((String)this.flags.get(var1), var2 != null ? var2.doubleValue() : null);
   }

   public Boolean getBooleanFlagValue(String flag) {
      return this.getBooleanFlagValue(var1, false);
   }

   public Boolean getBooleanFlagValue(String flag, Boolean def) {
      String var3 = (String)this.flags.get(var1);
      return var3 == null ? var2 : ACFUtil.isTruthy(var3);
   }

   public Double getFlagValue(String flag, Number def) {
      return ACFUtil.parseDouble((String)this.flags.get(var1), var2 != null ? var2.doubleValue() : null);
   }

   /** @deprecated */
   @Deprecated
   public <T extends Annotation> T getAnnotation(Class<T> cls) {
      return this.param.getParameter().getAnnotation(var1);
   }

   public <T extends Annotation> String getAnnotationValue(Class<T> cls) {
      return this.manager.getAnnotations().getAnnotationValue(this.param.getParameter(), var1);
   }

   public <T extends Annotation> String getAnnotationValue(Class<T> cls, int options) {
      return this.manager.getAnnotations().getAnnotationValue(this.param.getParameter(), var1, var2);
   }

   public <T extends Annotation> boolean hasAnnotation(Class<T> cls) {
      return this.manager.getAnnotations().hasAnnotation(this.param.getParameter(), var1);
   }

   public RegisteredCommand getCmd() {
      return this.cmd;
   }

   @UnstableAPI
   CommandParameter getCommandParameter() {
      return this.param;
   }

   /** @deprecated */
   @Deprecated
   public Parameter getParam() {
      return this.param.getParameter();
   }

   public I getIssuer() {
      return this.issuer;
   }

   public List<String> getArgs() {
      return this.args;
   }

   public int getIndex() {
      return this.index;
   }

   public Map<String, Object> getPassedArgs() {
      return this.passedArgs;
   }

   public Map<String, String> getFlags() {
      return this.flags;
   }

   public String joinArgs() {
      return ACFUtil.join((Collection)this.args, " ");
   }

   public String joinArgs(String sep) {
      return ACFUtil.join((Collection)this.args, var1);
   }
}
