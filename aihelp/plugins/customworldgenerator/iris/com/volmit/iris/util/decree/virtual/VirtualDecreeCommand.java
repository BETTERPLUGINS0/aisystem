package com.volmit.iris.util.decree.virtual;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.decree.DecreeContext;
import com.volmit.iris.util.decree.DecreeContextHandler;
import com.volmit.iris.util.decree.DecreeNode;
import com.volmit.iris.util.decree.DecreeOrigin;
import com.volmit.iris.util.decree.DecreeParameter;
import com.volmit.iris.util.decree.annotations.Decree;
import com.volmit.iris.util.decree.exceptions.DecreeParsingException;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.plugin.CommandDummy;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.J;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Generated;

public class VirtualDecreeCommand {
   private final Class<?> type;
   private final VirtualDecreeCommand parent;
   private final KList<VirtualDecreeCommand> nodes;
   private final DecreeNode node;
   String[] gradients = new String[]{"<gradient:#f5bc42:#45b32d>", "<gradient:#1ed43f:#1ecbd4>", "<gradient:#1e2ad4:#821ed4>", "<gradient:#d41ea7:#611ed4>", "<gradient:#1ed473:#1e55d4>", "<gradient:#6ad41e:#9a1ed4>"};
   private ChronoLatch cl = new ChronoLatch(1000L);

   private VirtualDecreeCommand(Class<?> type, VirtualDecreeCommand parent, KList<VirtualDecreeCommand> nodes, DecreeNode node) {
      this.parent = var2;
      this.type = var1;
      this.nodes = var3;
      this.node = var4;
   }

   public static VirtualDecreeCommand createRoot(Object v) {
      return createRoot((VirtualDecreeCommand)null, var0);
   }

   public static VirtualDecreeCommand createRoot(VirtualDecreeCommand parent, Object v) {
      VirtualDecreeCommand var2 = new VirtualDecreeCommand(var1.getClass(), var0, new KList(), (DecreeNode)null);
      Field[] var3 = var1.getClass().getDeclaredFields();
      int var4 = var3.length;

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         Field var6 = var3[var5];
         if (!Modifier.isStatic(var6.getModifiers()) && !Modifier.isFinal(var6.getModifiers()) && !Modifier.isTransient(var6.getModifiers()) && !Modifier.isVolatile(var6.getModifiers()) && var6.getType().isAnnotationPresent(Decree.class)) {
            var6.setAccessible(true);
            Object var7 = var6.get(var1);
            if (var7 == null) {
               var7 = var6.getType().getConstructor().newInstance();
               var6.set(var1, var7);
            }

            var2.getNodes().add((Object)createRoot(var2, var7));
         }
      }

      Method[] var8 = var1.getClass().getDeclaredMethods();
      var4 = var8.length;

      for(var5 = 0; var5 < var4; ++var5) {
         Method var9 = var8[var5];
         if (!Modifier.isStatic(var9.getModifiers()) && !Modifier.isFinal(var9.getModifiers()) && !Modifier.isPrivate(var9.getModifiers()) && var9.isAnnotationPresent(Decree.class)) {
            var2.getNodes().add((Object)(new VirtualDecreeCommand(var1.getClass(), var2, new KList(), new DecreeNode(var1, var9))));
         }
      }

      return var2;
   }

   public void cacheAll() {
      VolmitSender var1 = new VolmitSender(new CommandDummy());
      if (this.isNode()) {
         var1.sendDecreeHelpNode(this);
      }

      Iterator var2 = this.nodes.iterator();

      while(var2.hasNext()) {
         VirtualDecreeCommand var3 = (VirtualDecreeCommand)var2.next();
         var3.cacheAll();
      }

   }

   public String getPath() {
      KList var1 = new KList();
      VirtualDecreeCommand var2 = this;

      while(var2.getParent() != null) {
         var2 = var2.getParent();
         var1.add((Object)var2.getName());
      }

      KList var10000 = var1.reverse().qadd(this.getName());
      return "/" + var10000.toString(" ");
   }

   public String getParentPath() {
      return this.getParent().getPath();
   }

   public String getName() {
      return this.isNode() ? this.getNode().getName() : ((Decree)this.getType().getDeclaredAnnotation(Decree.class)).name();
   }

   private boolean isStudio() {
      return this.isNode() ? this.getNode().getDecree().studio() : ((Decree)this.getType().getDeclaredAnnotation(Decree.class)).studio();
   }

   public String getDescription() {
      return this.isNode() ? this.getNode().getDescription() : ((Decree)this.getType().getDeclaredAnnotation(Decree.class)).description();
   }

   public KList<String> getNames() {
      if (this.isNode()) {
         return this.getNode().getNames();
      } else {
         Decree var1 = (Decree)this.getType().getDeclaredAnnotation(Decree.class);
         KList var2 = new KList();
         var2.add((Object)var1.name());
         String[] var3 = var1.aliases();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (!var6.isEmpty()) {
               var2.add((Object)var6);
            }
         }

         var2.removeDuplicates();
         return var2;
      }
   }

   public boolean isNode() {
      return this.node != null;
   }

   public KList<String> tabComplete(KList<String> args, String raw) {
      KList var3 = new KList();
      KList var4 = new KList();
      this.invokeTabComplete(var1, var3, var4, var2);
      return var4;
   }

   private boolean invokeTabComplete(KList<String> args, KList<Integer> skip, KList<String> tabs, String raw) {
      if (this.isStudio() && !IrisSettings.get().getStudio().isStudio()) {
         return false;
      } else if (this.isNode()) {
         this.tab(var1, var3);
         var2.add((Object)this.hashCode());
         return false;
      } else if (var1.isEmpty()) {
         this.tab(var1, var3);
         return true;
      } else {
         String var5 = (String)var1.get(0);
         if (var1.size() <= 1 && !var5.endsWith(" ")) {
            this.tab(var1, var3);
         } else {
            VirtualDecreeCommand var6 = this.matchNode(var5, var2);
            if (var6 != null) {
               var1.pop();
               return var6.invokeTabComplete(var1, var2, var3, var4);
            }

            var2.add((Object)this.hashCode());
         }

         return false;
      }
   }

   private void tab(KList<String> args, KList<String> tabs) {
      String var3 = null;
      KList var4 = new KList();
      Runnable var5 = () -> {
      };

      Iterator var6;
      String var7;
      for(var6 = var1.iterator(); var6.hasNext(); var5 = () -> {
         if (this.isNode()) {
            String var3 = var7.contains("=") ? var7.split("\\Q=\\E")[0] : var7;
            var3 = var3.trim();
            Iterator var4x = this.getNode().getParameters().iterator();

            while(true) {
               while(var4x.hasNext()) {
                  DecreeParameter var5 = (DecreeParameter)var4x.next();
                  Iterator var6 = var5.getNames().iterator();

                  while(var6.hasNext()) {
                     String var7x = (String)var6.next();
                     if (var7x.equalsIgnoreCase(var3) || var7x.toLowerCase().contains(var3.toLowerCase()) || var3.toLowerCase().contains(var7x.toLowerCase())) {
                        var4.add((Object)var5);
                        break;
                     }
                  }
               }

               return;
            }
         }
      }) {
         var7 = (String)var6.next();
         var5.run();
         var3 = var7;
      }

      if (var3 != null) {
         if (this.isNode()) {
            var6 = this.getNode().getParameters().iterator();

            while(true) {
               DecreeParameter var13;
               do {
                  if (!var6.hasNext()) {
                     return;
                  }

                  var13 = (DecreeParameter)var6.next();
               } while(var4.contains(var13));

               int var8 = 0;
               String var10;
               String var10001;
               if (var3.contains("=")) {
                  String[] var16 = var3.trim().split("\\Q=\\E");
                  var10 = var16.length == 2 ? var16[1] : "";
                  Iterator var11 = var13.getHandler().getPossibilities(var10).convert((var1x) -> {
                     return var13.getHandler().toStringForce(var1x);
                  }).iterator();

                  while(var11.hasNext()) {
                     String var12 = (String)var11.next();
                     ++var8;
                     var10001 = var13.getName();
                     var2.add((Object)(var10001 + "=" + var12));
                  }
               } else {
                  Iterator var9 = var13.getHandler().getPossibilities("").convert((var1x) -> {
                     return var13.getHandler().toStringForce(var1x);
                  }).iterator();

                  while(var9.hasNext()) {
                     var10 = (String)var9.next();
                     ++var8;
                     var10001 = var13.getName();
                     var2.add((Object)(var10001 + "=" + var10));
                  }
               }

               if (var8 == 0) {
                  var2.add((Object)(var13.getName() + "="));
               }
            }
         } else {
            var6 = this.getNodes().iterator();

            while(true) {
               VirtualDecreeCommand var14;
               String var15;
               do {
                  if (!var6.hasNext()) {
                     return;
                  }

                  var14 = (VirtualDecreeCommand)var6.next();
                  var15 = var14.getName();
               } while(!var15.equalsIgnoreCase(var3) && !var15.toLowerCase().contains(var3.toLowerCase()) && !var3.toLowerCase().contains(var15.toLowerCase()));

               var2.addAll(var14.getNames());
            }
         }
      }
   }

   private KMap<String, Object> map(VolmitSender sender, KList<String> in) {
      KMap var3 = new KMap();
      KSet var4 = new KSet(new Integer[0]);
      KList var5 = new KList((Collection)var2.stream().filter((var0) -> {
         return !var0.contains("=");
      }).collect(Collectors.toList()));
      KList var6 = new KList((Collection)var2.stream().filter((var0) -> {
         return var0.contains("=");
      }).collect(Collectors.toList()));

      for(int var7 = 0; var7 < var6.size(); ++var7) {
         String var8 = (String)var6.get(var7);
         int var9 = var2.indexOf(var8);
         String[] var10 = var8.split("\\Q=\\E");
         String var11 = var10[0];
         String var12 = var10[1];
         DecreeParameter var13 = null;
         Iterator var14 = this.getNode().getParameters().iterator();

         while(true) {
            DecreeParameter var15;
            Iterator var16;
            String var17;
            while(var14.hasNext()) {
               var15 = (DecreeParameter)var14.next();
               var16 = var15.getNames().iterator();

               while(var16.hasNext()) {
                  var17 = (String)var16.next();
                  if (var17.equalsIgnoreCase(var11)) {
                     var13 = var15;
                     break;
                  }
               }
            }

            if (var13 == null) {
               var14 = this.getNode().getParameters().iterator();

               label93:
               while(true) {
                  while(true) {
                     if (!var14.hasNext()) {
                        break label93;
                     }

                     var15 = (DecreeParameter)var14.next();
                     var16 = var15.getNames().iterator();

                     while(var16.hasNext()) {
                        var17 = (String)var16.next();
                        if (var17.toLowerCase().contains(var11.toLowerCase()) || var11.toLowerCase().contains(var17.toLowerCase())) {
                           var13 = var15;
                           break;
                        }
                     }
                  }
               }
            }

            if (var13 == null) {
               Iris.debug("Can't find parameter key for " + var11 + "=" + var12 + " in " + this.getPath());
               String var10001 = String.valueOf(C.YELLOW);
               var1.sendMessage(var10001 + "Unknown Parameter: " + var11);
               var5.add((Object)var12);
            } else {
               var11 = var13.getName();

               try {
                  var3.put(var11, var13.getHandler().parse(var12, var4.contains(var9)));
               } catch (DecreeParsingException var20) {
                  Iris.debug("Can't parse parameter value for " + var11 + "=" + var12 + " in " + this.getPath() + " using handler " + var13.getHandler().getClass().getSimpleName());
                  var1.sendMessage(String.valueOf(C.RED) + "Cannot convert \"" + var12 + "\" into a " + var13.getType().getSimpleName());
                  var20.printStackTrace();
                  return null;
               }
            }
            break;
         }
      }

      KList var21 = new KList((Collection)this.getNode().getParameters().stream().filter((var1x) -> {
         return !var3.contains(var1x.getName());
      }).collect(Collectors.toList()));

      for(int var22 = 0; var22 < var5.size(); ++var22) {
         String var23 = (String)var5.get(var22);
         int var24 = var2.indexOf(var23);

         try {
            DecreeParameter var25 = (DecreeParameter)var21.get(var22);

            try {
               var3.put(var25.getName(), var25.getHandler().parse(var23, var4.contains(var24)));
            } catch (DecreeParsingException var18) {
               String var10000 = var25.getName();
               Iris.debug("Can't parse parameter value for " + var10000 + "=" + var23 + " in " + this.getPath() + " using handler " + var25.getHandler().getClass().getSimpleName());
               var1.sendMessage(String.valueOf(C.RED) + "Cannot convert \"" + var23 + "\" into a " + var25.getType().getSimpleName());
               var18.printStackTrace();
               return null;
            }
         } catch (IndexOutOfBoundsException var19) {
            var1.sendMessage(String.valueOf(C.YELLOW) + "Unknown Parameter: " + var23 + " (" + Form.getNumberSuffixThStRd(var22 + 1) + " argument)");
         }
      }

      return var3;
   }

   public boolean invoke(VolmitSender sender, KList<String> realArgs) {
      return this.invoke(var1, var2, new KList());
   }

   public boolean invoke(VolmitSender sender, KList<String> args, KList<Integer> skip) {
      if (this.isStudio() && !IrisSettings.get().getStudio().isStudio()) {
         var1.sendMessage(String.valueOf(C.RED) + "To use Iris Studio Commands, please enable studio in Iris/settings.json (settings auto-reload)");
         return false;
      } else {
         DecreeOrigin var4 = ((Decree)this.type.getDeclaredAnnotation(Decree.class)).origin();
         if (!var4.validFor(var1)) {
            String var10001 = String.valueOf(C.RED);
            var1.sendMessage(var10001 + "This command has to be sent from another origin: " + String.valueOf(C.GOLD) + String.valueOf(var4));
            return false;
         } else {
            String var10000 = this.getPath();
            Iris.debug("@ " + var10000 + " with " + var2.toString(", "));
            if (this.isNode()) {
               var10000 = this.getPath();
               Iris.debug("Invoke " + var10000 + "(" + var2.toString(",") + ") at ");
               if (this.invokeNode(var1, this.map(var1, var2))) {
                  return true;
               } else {
                  var3.add((Object)this.hashCode());
                  return false;
               }
            } else if (var2.isEmpty()) {
               var1.sendDecreeHelp(this);
               return true;
            } else {
               if (var2.size() == 1) {
                  Iterator var5 = var2.iterator();

                  while(var5.hasNext()) {
                     String var6 = (String)var5.next();
                     if (var6.startsWith("help=")) {
                        var1.sendDecreeHelp(this, Integer.parseInt(var6.split("\\Q=\\E")[1]) - 1);
                        return true;
                     }
                  }
               }

               String var7 = (String)var2.get(0);
               VirtualDecreeCommand var8 = this.matchNode(var7, var3);
               if (var8 != null) {
                  var2.pop();
                  return var8.invoke(var1, var2, var3);
               } else {
                  var3.add((Object)this.hashCode());
                  return false;
               }
            }
         }
      }
   }

   private boolean invokeNode(VolmitSender sender, KMap<String, Object> map) {
      if (var2 == null) {
         return false;
      } else {
         Object[] var3 = new Object[this.getNode().getMethod().getParameterCount()];
         int var4 = 0;

         for(Iterator var5 = this.getNode().getParameters().iterator(); var5.hasNext(); ++var4) {
            DecreeParameter var6 = (DecreeParameter)var5.next();
            Object var7 = var2.get(var6.getName());

            String var10000;
            String var10001;
            try {
               if (var7 == null && var6.hasDefault()) {
                  var7 = var6.getDefaultValue();
               }
            } catch (DecreeParsingException var15) {
               var10000 = var6.getName();
               Iris.debug("Can't parse parameter value for " + var10000 + "=" + var6.getParam().defaultValue() + " in " + this.getPath() + " using handler " + var6.getHandler().getClass().getSimpleName());
               var10001 = String.valueOf(C.RED);
               var1.sendMessage(var10001 + "Cannot convert \"" + var6.getParam().defaultValue() + "\" into a " + var6.getType().getSimpleName());
               return false;
            }

            if (var1.isPlayer() && var6.isContextual() && var7 == null) {
               Iris.debug("Contextual!");
               DecreeContextHandler var8 = (DecreeContextHandler)DecreeContextHandler.contextHandlers.get(var6.getType());
               if (var8 != null) {
                  var7 = var8.handle(var1);
                  if (var7 != null) {
                     var10000 = var6.getName();
                     Iris.debug("Parameter \"" + var10000 + "\" derived a value of \"" + var6.getHandler().toStringForce(var7) + "\" from " + var8.getClass().getSimpleName());
                  } else {
                     var10000 = var6.getName();
                     Iris.debug("Parameter \"" + var10000 + "\" could not derive a value from \"" + var8.getClass().getSimpleName());
                  }
               } else {
                  var10000 = var6.getName();
                  Iris.debug("Parameter \"" + var10000 + "\" is contextual but has no context handler for \"" + var6.getType().getCanonicalName() + "\"");
               }
            }

            if (var6.hasDefault() && var7 == null) {
               try {
                  var10000 = var6.getName();
                  Iris.debug("Parameter \"" + var10000 + "\" is using default value \"" + var6.getParam().defaultValue() + "\"");
                  var7 = var6.getDefaultValue();
               } catch (Throwable var14) {
                  var14.printStackTrace();
               }
            }

            if (var6.isRequired() && var7 == null) {
               var10001 = String.valueOf(C.RED);
               var1.sendMessage(var10001 + "Missing argument \"" + var6.getName() + "\" (" + var6.getType().getSimpleName() + ") as the " + Form.getNumberSuffixThStRd(var4 + 1) + " argument.");
               var1.sendDecreeHelpNode(this);
               return false;
            }

            var3[var4] = var7;
         }

         DecreeContext.touch(var1);

         try {
            Runnable var16 = () -> {
               DecreeContext.touch(var1);

               try {
                  this.getNode().getMethod().setAccessible(true);
                  this.getNode().getMethod().invoke(this.getNode().getInstance(), var3);
               } catch (Throwable var7) {
                  var7.printStackTrace();
                  throw new RuntimeException("Failed to execute <INSERT REAL NODE HERE>");
               } finally {
                  DecreeContext.remove();
               }

            };
            if (this.getNode().isSync()) {
               J.s(var16);
            } else {
               var16.run();
            }
         } finally {
            DecreeContext.remove();
         }

         return true;
      }
   }

   public KList<VirtualDecreeCommand> matchAllNodes(String in) {
      KList var2 = new KList();
      if (var1.trim().isEmpty()) {
         var2.addAll(this.nodes);
         return var2;
      } else {
         Iterator var3 = this.nodes.iterator();

         VirtualDecreeCommand var4;
         while(var3.hasNext()) {
            var4 = (VirtualDecreeCommand)var3.next();
            if (var4.matches(var1)) {
               var2.add((Object)var4);
            }
         }

         var3 = this.nodes.iterator();

         while(var3.hasNext()) {
            var4 = (VirtualDecreeCommand)var3.next();
            if (var4.deepMatches(var1)) {
               var2.add((Object)var4);
            }
         }

         var2.removeDuplicates();
         return var2;
      }
   }

   public VirtualDecreeCommand matchNode(String in, KList<Integer> skip) {
      if (var1.trim().isEmpty()) {
         return null;
      } else {
         Iterator var3 = this.nodes.iterator();

         VirtualDecreeCommand var4;
         do {
            if (!var3.hasNext()) {
               var3 = this.nodes.iterator();

               do {
                  if (!var3.hasNext()) {
                     return null;
                  }

                  var4 = (VirtualDecreeCommand)var3.next();
               } while(var2.contains(var4.hashCode()) || !var4.deepMatches(var1));

               return var4;
            }

            var4 = (VirtualDecreeCommand)var3.next();
         } while(var2.contains(var4.hashCode()) || !var4.matches(var1));

         return var4;
      }
   }

   public boolean deepMatches(String in) {
      KList var2 = this.getNames();
      Iterator var3 = var2.iterator();

      String var4;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         var4 = (String)var3.next();
      } while(!var4.toLowerCase().contains(var1.toLowerCase()) && !var1.toLowerCase().contains(var4.toLowerCase()));

      return true;
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.getName(), this.getDescription(), this.getType(), this.getPath()});
   }

   public boolean equals(Object obj) {
      if (!(var1 instanceof VirtualDecreeCommand)) {
         return false;
      } else {
         return this.hashCode() == var1.hashCode();
      }
   }

   public boolean matches(String in) {
      KList var2 = this.getNames();
      Iterator var3 = var2.iterator();

      String var4;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         var4 = (String)var3.next();
      } while(!var4.equalsIgnoreCase(var1));

      return true;
   }

   @Generated
   public Class<?> getType() {
      return this.type;
   }

   @Generated
   public VirtualDecreeCommand getParent() {
      return this.parent;
   }

   @Generated
   public KList<VirtualDecreeCommand> getNodes() {
      return this.nodes;
   }

   @Generated
   public DecreeNode getNode() {
      return this.node;
   }

   @Generated
   public String[] getGradients() {
      return this.gradients;
   }

   @Generated
   public ChronoLatch getCl() {
      return this.cl;
   }

   @Generated
   public void setGradients(final String[] gradients) {
      this.gradients = var1;
   }

   @Generated
   public void setCl(final ChronoLatch cl) {
      this.cl = var1;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getType());
      return "VirtualDecreeCommand(type=" + var10000 + ", parent=" + String.valueOf(this.getParent()) + ", nodes=" + String.valueOf(this.getNodes()) + ", node=" + String.valueOf(this.getNode()) + ", gradients=" + Arrays.deepToString(this.getGradients()) + ", cl=" + String.valueOf(this.getCl()) + ")";
   }
}
