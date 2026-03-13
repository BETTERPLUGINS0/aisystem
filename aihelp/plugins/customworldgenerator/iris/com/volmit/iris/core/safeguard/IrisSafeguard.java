package com.volmit.iris.core.safeguard;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.safeguard.task.Diagnostic;
import com.volmit.iris.core.safeguard.task.Task;
import com.volmit.iris.core.safeguard.task.TasksKt;
import com.volmit.iris.core.safeguard.task.ValueWithDiagnostics;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.scheduling.J;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\t\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0012\u001a\u00020\u0013H\u0007J\b\u0010\u000f\u001a\u00020\nH\u0007J\u0014\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\f0\u0007H\u0007J\u001a\u0010\u0015\u001a\u0014\u0012\u0004\u0012\u00020\f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000e0\u0007H\u0007J\b\u0010\u0016\u001a\u00020\u0013H\u0007J\b\u0010\u0017\u001a\u00020\u0013H\u0007J\b\u0010\u0018\u001a\u00020\u0013H\u0007J\b\u0010\u0019\u001a\u00020\u0005H\u0007J\b\u0010\u001a\u001a\u00020\u0013H\u0002J\b\u0010\u001b\u001a\u00020\u0013H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R \u0010\u0006\u001a\u0014\u0012\u0004\u0012\u00020\b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\f0\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R \u0010\r\u001a\u0014\u0012\u0004\u0012\u00020\f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000e0\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001c"},
   d2 = {"Lcom/volmit/iris/core/safeguard/IrisSafeguard;", "", "<init>", "()V", "forceShutdown", "", "results", "", "Lcom/volmit/iris/core/safeguard/task/Task;", "Lcom/volmit/iris/core/safeguard/task/ValueWithDiagnostics;", "Lcom/volmit/iris/core/safeguard/Mode;", "context", "", "attachment", "", "mode", "count", "", "execute", "", "asContext", "asAttachment", "splash", "printReports", "printFooter", "isForceShutdown", "warning", "unstable", "core"}
)
@SourceDebugExtension({"SMAP\nIrisSafeguard.kt\nKotlin\n*S Kotlin\n*F\n+ 1 IrisSafeguard.kt\ncom/volmit/iris/core/safeguard/IrisSafeguard\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,143:1\n1374#2:144\n1460#2,5:145\n1869#2,2:150\n*S KotlinDebug\n*F\n+ 1 IrisSafeguard.kt\ncom/volmit/iris/core/safeguard/IrisSafeguard\n*L\n44#1:144\n44#1:145,5\n79#1:150,2\n*E\n"})
public final class IrisSafeguard {
   @NotNull
   public static final IrisSafeguard INSTANCE = new IrisSafeguard();
   private static volatile boolean forceShutdown;
   @NotNull
   private static Map<Task, ? extends ValueWithDiagnostics<? extends Mode>> results = MapsKt.emptyMap();
   @NotNull
   private static Map<String, String> context = MapsKt.emptyMap();
   @NotNull
   private static Map<String, ? extends List<String>> attachment = MapsKt.emptyMap();
   @NotNull
   private static Mode mode;
   private static int count;

   private IrisSafeguard() {
   }

   @JvmStatic
   public static final void execute() {
      LinkedHashMap var0 = new LinkedHashMap(TasksKt.getTasks().size());
      LinkedHashMap var1 = new LinkedHashMap(TasksKt.getTasks().size());
      LinkedHashMap var2 = new LinkedHashMap(TasksKt.getTasks().size());
      Mode var3 = Mode.STABLE;
      int var4 = 0;
      Iterator var5 = TasksKt.getTasks().iterator();

      while(var5.hasNext()) {
         Task var6 = (Task)var5.next();
         ValueWithDiagnostics var7 = null;

         try {
            var7 = var6.run();
         } catch (Throwable var20) {
            Iris.reportError(var20);
            Mode var10002 = Mode.WARNING;
            Diagnostic[] var9 = new Diagnostic[]{new Diagnostic(Diagnostic.Logger.ERROR, "Error while running task " + var6.getId(), var20)};
            var7 = new ValueWithDiagnostics(var10002, var9);
         }

         var3 = var3.highest((Mode)var7.getValue());
         ((Map)var0).put(var6, var7);
         ((Map)var1).put(var6.getId(), ((Mode)var7.getValue()).getId());
         Map var8 = (Map)var2;
         String var21 = var6.getId();
         Iterable var10 = (Iterable)var7.getDiagnostics();
         boolean var11 = false;
         Collection var13 = (Collection)(new ArrayList());
         boolean var14 = false;
         Iterator var15 = var10.iterator();

         while(var15.hasNext()) {
            Object var16 = var15.next();
            Diagnostic var17 = (Diagnostic)var16;
            boolean var18 = false;
            CharSequence var10000 = (CharSequence)var17.toString();
            char[] var19 = new char[]{'\n'};
            Iterable var25 = (Iterable)StringsKt.split$default(var10000, var19, false, 0, 6, (Object)null);
            CollectionsKt.addAll(var13, var25);
         }

         List var22 = (List)var13;
         var8.put(var21, var22);
         if (var7.getValue() != Mode.STABLE) {
            ++var4;
         }
      }

      IrisSafeguard var23 = INSTANCE;
      Map var24 = Collections.unmodifiableMap((Map)var0);
      Intrinsics.checkNotNullExpressionValue(var24, "unmodifiableMap(...)");
      results = var24;
      var23 = INSTANCE;
      var24 = Collections.unmodifiableMap((Map)var1);
      Intrinsics.checkNotNullExpressionValue(var24, "unmodifiableMap(...)");
      context = var24;
      var23 = INSTANCE;
      var24 = Collections.unmodifiableMap((Map)var2);
      Intrinsics.checkNotNullExpressionValue(var24, "unmodifiableMap(...)");
      attachment = var24;
      var23 = INSTANCE;
      mode = var3;
      var23 = INSTANCE;
      count = var4;
   }

   @JvmStatic
   @NotNull
   public static final Mode mode() {
      return mode;
   }

   @JvmStatic
   @NotNull
   public static final Map<String, String> asContext() {
      return context;
   }

   @JvmStatic
   @NotNull
   public static final Map<String, List<String>> asAttachment() {
      return attachment;
   }

   @JvmStatic
   public static final void splash() {
      Iris.instance.splash();
      IrisSafeguard var10000 = INSTANCE;
      printReports();
      var10000 = INSTANCE;
      printFooter();
   }

   @JvmStatic
   public static final void printReports() {
      String var10000;
      Object[] var2;
      switch(IrisSafeguard.WhenMappings.$EnumSwitchMapping$0[mode.ordinal()]) {
      case 1:
         Iris.info(C.BLUE + "0 Conflicts found");
         break;
      case 2:
         var10000 = C.GOLD + "%s Issues found";
         var2 = new Object[]{count};
         Iris.warn(var10000, var2);
         break;
      case 3:
         var10000 = C.DARK_RED + "%s Issues found";
         var2 = new Object[]{count};
         Iris.error(var10000, var2);
         break;
      default:
         throw new NoWhenBranchMatchedException();
      }

      Iterable var0 = (Iterable)results.values();
      boolean var1 = false;
      Iterator var6 = var0.iterator();

      while(var6.hasNext()) {
         Object var3 = var6.next();
         ValueWithDiagnostics var4 = (ValueWithDiagnostics)var3;
         boolean var5 = false;
         ValueWithDiagnostics.log$default(var4, false, true, 1, (Object)null);
      }

   }

   @JvmStatic
   public static final void printFooter() {
      switch(IrisSafeguard.WhenMappings.$EnumSwitchMapping$0[mode.ordinal()]) {
      case 1:
         Iris.info(C.BLUE + "Iris is running Stable");
         break;
      case 2:
         INSTANCE.warning();
         break;
      case 3:
         INSTANCE.unstable();
         break;
      default:
         throw new NoWhenBranchMatchedException();
      }

   }

   @JvmStatic
   public static final boolean isForceShutdown() {
      return forceShutdown;
   }

   private final void warning() {
      Iris.warn(C.GOLD + "Iris is running in Warning Mode");
      Iris.warn("");
      Iris.warn(C.DARK_GRAY + "--==<" + C.GOLD + " IMPORTANT " + C.DARK_GRAY + ">==--");
      Iris.warn(C.GOLD + "Iris is running in warning mode which may cause the following issues:");
      Iris.warn("- Data Loss");
      Iris.warn("- Errors");
      Iris.warn("- Broken worlds");
      Iris.warn("- Unexpected behavior.");
      Iris.warn("- And perhaps further complications.");
      Iris.warn("");
   }

   private final void unstable() {
      Iris.error(C.DARK_RED + "Iris is running in Unstable Mode");
      Iris.error("");
      Iris.error(C.DARK_GRAY + "--==<" + C.RED + " IMPORTANT " + C.DARK_GRAY + ">==--");
      Iris.error("Iris is running in unstable mode which may cause the following issues:");
      Iris.error(C.DARK_RED + "Server Issues");
      Iris.error("- Server won't boot");
      Iris.error("- Data Loss");
      Iris.error("- Unexpected behavior.");
      Iris.error("- And More...");
      Iris.error(C.DARK_RED + "World Issues");
      Iris.error("- Worlds can't load due to corruption.");
      Iris.error("- Worlds may slowly corrupt until they can't load.");
      Iris.error("- World data loss.");
      Iris.error("- And More...");
      Iris.error(C.DARK_RED + "ATTENTION: " + C.RED + "While running Iris in unstable mode, you won't be eligible for support.");
      if (IrisSettings.get().getGeneral().isDoomsdayAnnihilationSelfDestructMode()) {
         Iris.error(C.DARK_RED + "Boot Unstable is set to true, continuing with the startup process in 10 seconds.");
         J.sleep(10000L);
      } else {
         Iris.error(C.DARK_RED + "Go to plugins/iris/settings.json and set DoomsdayAnnihilationSelfDestructMode to true if you wish to proceed.");
         Iris.error(C.DARK_RED + "The server will shutdown in 10 seconds.");
         J.sleep(10000L);
         Iris.error(C.DARK_RED + "Shutting down server.");
         forceShutdown = true;

         try {
            Bukkit.getPluginManager().disablePlugins();
         } finally {
            Runtime.getRuntime().halt(42);
         }
      }

      Iris.info("");
   }

   static {
      mode = Mode.STABLE;
   }

   // $FF: synthetic class
   @Metadata(
      mv = {2, 2, 0},
      k = 3,
      xi = 48
   )
   public static final class WhenMappings {
      // $FF: synthetic field
      public static final int[] $EnumSwitchMapping$0;

      static {
         int[] var0 = new int[Mode.values().length];

         try {
            var0[Mode.STABLE.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[Mode.WARNING.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[Mode.UNSTABLE.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
         }

         $EnumSwitchMapping$0 = var0;
      }
   }
}
