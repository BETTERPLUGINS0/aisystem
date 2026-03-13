package com.volmit.iris.core.scripting.kotlin.environment;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.scripting.environment.PackEnvironment;
import com.volmit.iris.core.scripting.kotlin.base.DataScript;
import com.volmit.iris.core.scripting.kotlin.base.NoiseScript;
import com.volmit.iris.core.scripting.kotlin.runner.Script;
import com.volmit.iris.core.scripting.kotlin.runner.ScriptRunner;
import com.volmit.iris.core.scripting.kotlin.runner.UtilsKt;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IrisScript;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.math.RNG;
import java.io.File;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SpreadBuilder;
import kotlin.reflect.KClass;
import kotlin.script.experimental.api.ResultWithDiagnostics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0016\u0018\u00002\u00020\u00012\u00020\u0002B\u001b\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\u0004\b\u0007\u0010\bB\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0004\b\u0007\u0010\tJ\b\u0010\n\u001a\u00020\u0004H\u0016J\u001c\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\n\u0010\u000f\u001a\u0006\u0012\u0002\b\u00030\u0010H\u0014J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u0012\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u001a\u0010\u0015\u001a\u0004\u0018\u00010\u00142\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\u0017H\u0016J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0016JO\u0010\u001c\u001a\u0010\u0012\u0004\u0012\u00020\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00140\u001d*\u00020\u00042.\u0010\u001e\u001a\u0018\u0012\u0014\b\u0001\u0012\u0010\u0012\u0004\u0012\u00020\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00140 0\u001f\"\u0010\u0012\u0004\u0012\u00020\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00140 H\u0002¢\u0006\u0002\u0010!R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\""},
   d2 = {"Lcom/volmit/iris/core/scripting/kotlin/environment/IrisPackExecutionEnvironment;", "Lcom/volmit/iris/core/scripting/kotlin/environment/IrisSimpleExecutionEnvironment;", "Lcom/volmit/iris/core/scripting/environment/PackEnvironment;", "data", "Lcom/volmit/iris/core/loader/IrisData;", "parent", "Lcom/volmit/iris/core/scripting/kotlin/runner/ScriptRunner;", "<init>", "(Lcom/volmit/iris/core/loader/IrisData;Lcom/volmit/iris/core/scripting/kotlin/runner/ScriptRunner;)V", "(Lcom/volmit/iris/core/loader/IrisData;)V", "getData", "compile", "Lcom/volmit/iris/core/scripting/kotlin/runner/Script;", "script", "", "type", "Lkotlin/reflect/KClass;", "execute", "", "evaluate", "", "createNoise", "rng", "Lcom/volmit/iris/util/math/RNG;", "with", "Lcom/volmit/iris/core/scripting/kotlin/environment/IrisExecutionEnvironment;", "engine", "Lcom/volmit/iris/engine/framework/Engine;", "parameters", "", "values", "", "Lkotlin/Pair;", "(Lcom/volmit/iris/core/loader/IrisData;[Lkotlin/Pair;)Ljava/util/Map;", "core"}
)
public class IrisPackExecutionEnvironment extends IrisSimpleExecutionEnvironment implements PackEnvironment {
   @NotNull
   private final IrisData data;

   public IrisPackExecutionEnvironment(@NotNull IrisData var1, @Nullable ScriptRunner var2) {
      Intrinsics.checkNotNullParameter(var1, "data");
      File var10001 = var1.getDataFolder();
      Intrinsics.checkNotNullExpressionValue(var10001, "getDataFolder(...)");
      super(var10001, var2);
      this.data = var1;
   }

   public IrisPackExecutionEnvironment(@NotNull IrisData var1) {
      Intrinsics.checkNotNullParameter(var1, "data");
      this(var1, (ScriptRunner)null);
   }

   @NotNull
   public IrisData getData() {
      return this.data;
   }

   @NotNull
   protected Script compile(@NotNull String var1, @NotNull KClass<?> var2) {
      Intrinsics.checkNotNullParameter(var1, "script");
      Intrinsics.checkNotNullParameter(var2, "type");
      IrisScript var3 = (IrisScript)this.data.getScriptLoader().load(var1);
      Object var10000 = ((KMap)this.getCompileCache().get(var1)).computeIfAbsent(var2, IrisPackExecutionEnvironment::compile$lambda$1);
      Intrinsics.checkNotNullExpressionValue(var10000, "computeIfAbsent(...)");
      return (Script)UtilsKt.valueOrThrow((ResultWithDiagnostics)var10000, (CharSequence)("Failed to compile script " + var1));
   }

   public void execute(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "script");
      this.execute(var1, DataScript.class, this.parameters(this.data));
   }

   @Nullable
   public Object evaluate(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "script");
      return this.evaluate(var1, DataScript.class, this.parameters(this.data));
   }

   @Nullable
   public Object createNoise(@NotNull String var1, @NotNull RNG var2) {
      Intrinsics.checkNotNullParameter(var1, "script");
      Intrinsics.checkNotNullParameter(var2, "rng");
      IrisData var10004 = this.data;
      Pair[] var3 = new Pair[]{TuplesKt.to("rng", var2)};
      return this.evaluate(var1, NoiseScript.class, this.parameters(var10004, var3));
   }

   @NotNull
   public IrisExecutionEnvironment with(@NotNull Engine var1) {
      Intrinsics.checkNotNullParameter(var1, "engine");
      return new IrisExecutionEnvironment(var1, this.getRunner());
   }

   private final Map<String, Object> parameters(IrisData var1, Pair<String, ? extends Object>... var2) {
      SpreadBuilder var3 = new SpreadBuilder(2);
      var3.add(TuplesKt.to("data", var1));
      var3.addSpread(var2);
      return MapsKt.mapOf((Pair[])var3.toArray(new Pair[var3.size()]));
   }

   private static final ResultWithDiagnostics compile$lambda$0(IrisPackExecutionEnvironment var0, KClass var1, IrisScript var2, KClass var3) {
      ScriptRunner var10000 = var0.getRunner();
      File var10002 = var2.getLoadFile();
      Intrinsics.checkNotNullExpressionValue(var10002, "getLoadFile(...)");
      return var10000.compile(var1, var10002, var2.getSource());
   }

   private static final ResultWithDiagnostics compile$lambda$1(Function1 var0, Object var1) {
      return (ResultWithDiagnostics)var0.invoke(var1);
   }
}
