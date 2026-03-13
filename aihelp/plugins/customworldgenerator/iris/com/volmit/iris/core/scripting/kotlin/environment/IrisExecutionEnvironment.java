package com.volmit.iris.core.scripting.kotlin.environment;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.core.scripting.environment.EngineEnvironment;
import com.volmit.iris.core.scripting.func.UpdateExecutor;
import com.volmit.iris.core.scripting.kotlin.base.ChunkUpdateScript;
import com.volmit.iris.core.scripting.kotlin.base.EngineScript;
import com.volmit.iris.core.scripting.kotlin.base.MobSpawningScript;
import com.volmit.iris.core.scripting.kotlin.base.PostMobSpawningScript;
import com.volmit.iris.core.scripting.kotlin.base.PreprocessorScript;
import com.volmit.iris.core.scripting.kotlin.runner.ScriptRunner;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.util.mantle.MantleChunk;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SpreadBuilder;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u00012\u00020\u0002B\u001b\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\u0004\b\u0007\u0010\bB\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0004\b\u0007\u0010\tJ\b\u0010\n\u001a\u00020\u0004H\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u0012\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u001a\u0010\u0011\u001a\u0004\u0018\u00010\u00102\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J \u0010\u0014\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0018\u0010\u0017\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J(\u0010\u001a\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0016JO\u0010!\u001a\u0010\u0012\u0004\u0012\u00020\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00100\"*\u00020\u00042.\u0010#\u001a\u0018\u0012\u0014\b\u0001\u0012\u0010\u0012\u0004\u0012\u00020\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00100%0$\"\u0010\u0012\u0004\u0012\u00020\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00100%H\u0002¢\u0006\u0002\u0010&JO\u0010'\u001a\u0010\u0012\u0004\u0012\u00020\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00100\"*\u00020\u00042.\u0010#\u001a\u0018\u0012\u0014\b\u0001\u0012\u0010\u0012\u0004\u0012\u00020\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00100%0$\"\u0010\u0012\u0004\u0012\u00020\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00100%H\u0002¢\u0006\u0002\u0010&R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006("},
   d2 = {"Lcom/volmit/iris/core/scripting/kotlin/environment/IrisExecutionEnvironment;", "Lcom/volmit/iris/core/scripting/kotlin/environment/IrisPackExecutionEnvironment;", "Lcom/volmit/iris/core/scripting/environment/EngineEnvironment;", "engine", "Lcom/volmit/iris/engine/framework/Engine;", "parent", "Lcom/volmit/iris/core/scripting/kotlin/runner/ScriptRunner;", "<init>", "(Lcom/volmit/iris/engine/framework/Engine;Lcom/volmit/iris/core/scripting/kotlin/runner/ScriptRunner;)V", "(Lcom/volmit/iris/engine/framework/Engine;)V", "getEngine", "execute", "", "script", "", "evaluate", "", "spawnMob", "location", "Lorg/bukkit/Location;", "postSpawnMob", "mob", "Lorg/bukkit/entity/Entity;", "preprocessObject", "object", "Lcom/volmit/iris/core/loader/IrisRegistrant;", "updateChunk", "mantleChunk", "Lcom/volmit/iris/util/mantle/MantleChunk;", "chunk", "Lorg/bukkit/Chunk;", "executor", "Lcom/volmit/iris/core/scripting/func/UpdateExecutor;", "limitedParameters", "", "values", "", "Lkotlin/Pair;", "(Lcom/volmit/iris/engine/framework/Engine;[Lkotlin/Pair;)Ljava/util/Map;", "parameters", "core"}
)
public final class IrisExecutionEnvironment extends IrisPackExecutionEnvironment implements EngineEnvironment {
   @NotNull
   private final Engine engine;

   public IrisExecutionEnvironment(@NotNull Engine var1, @Nullable ScriptRunner var2) {
      Intrinsics.checkNotNullParameter(var1, "engine");
      IrisData var10001 = var1.getData();
      Intrinsics.checkNotNullExpressionValue(var10001, "getData(...)");
      super(var10001, var2);
      this.engine = var1;
   }

   public IrisExecutionEnvironment(@NotNull Engine var1) {
      Intrinsics.checkNotNullParameter(var1, "engine");
      this(var1, (ScriptRunner)null);
   }

   @NotNull
   public Engine getEngine() {
      return this.engine;
   }

   public void execute(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "script");
      this.execute(var1, EngineScript.class, this.parameters(this.engine));
   }

   @Nullable
   public Object evaluate(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "script");
      return this.evaluate(var1, EngineScript.class, this.parameters(this.engine));
   }

   @Nullable
   public Object spawnMob(@NotNull String var1, @NotNull Location var2) {
      Intrinsics.checkNotNullParameter(var1, "script");
      Intrinsics.checkNotNullParameter(var2, "location");
      Engine var10004 = this.engine;
      Pair[] var3 = new Pair[]{TuplesKt.to("location", var2)};
      return this.evaluate(var1, MobSpawningScript.class, this.parameters(var10004, var3));
   }

   public void postSpawnMob(@NotNull String var1, @NotNull Location var2, @NotNull Entity var3) {
      Intrinsics.checkNotNullParameter(var1, "script");
      Intrinsics.checkNotNullParameter(var2, "location");
      Intrinsics.checkNotNullParameter(var3, "mob");
      Engine var10004 = this.engine;
      Pair[] var4 = new Pair[]{TuplesKt.to("location", var2), TuplesKt.to("entity", var3)};
      this.execute(var1, PostMobSpawningScript.class, this.parameters(var10004, var4));
   }

   public void preprocessObject(@NotNull String var1, @NotNull IrisRegistrant var2) {
      Intrinsics.checkNotNullParameter(var1, "script");
      Intrinsics.checkNotNullParameter(var2, "object");
      Engine var10004 = this.engine;
      Pair[] var3 = new Pair[]{TuplesKt.to("object", var2)};
      this.execute(var1, PreprocessorScript.class, this.limitedParameters(var10004, var3));
   }

   public void updateChunk(@NotNull String var1, @NotNull MantleChunk var2, @NotNull Chunk var3, @NotNull UpdateExecutor var4) {
      Intrinsics.checkNotNullParameter(var1, "script");
      Intrinsics.checkNotNullParameter(var2, "mantleChunk");
      Intrinsics.checkNotNullParameter(var3, "chunk");
      Intrinsics.checkNotNullParameter(var4, "executor");
      Engine var10004 = this.engine;
      Pair[] var5 = new Pair[]{TuplesKt.to("mantleChunk", var2), TuplesKt.to("chunk", var3), TuplesKt.to("executor", var4)};
      this.execute(var1, ChunkUpdateScript.class, this.parameters(var10004, var5));
   }

   private final Map<String, Object> limitedParameters(Engine var1, Pair<String, ? extends Object>... var2) {
      SpreadBuilder var3 = new SpreadBuilder(5);
      var3.add(TuplesKt.to("data", var1.getData()));
      var3.add(TuplesKt.to("engine", var1));
      var3.add(TuplesKt.to("seed", var1.getSeedManager().getSeed()));
      var3.add(TuplesKt.to("dimension", var1.getDimension()));
      var3.addSpread(var2);
      return MapsKt.mapOf((Pair[])var3.toArray(new Pair[var3.size()]));
   }

   private final Map<String, Object> parameters(Engine var1, Pair<String, ? extends Object>... var2) {
      SpreadBuilder var3 = new SpreadBuilder(3);
      var3.add(TuplesKt.to("complex", var1.getComplex()));
      var3.add(TuplesKt.to("biome", var1::getSurfaceBiome));
      var3.addSpread(var2);
      return this.limitedParameters(var1, (Pair[])var3.toArray(new Pair[var3.size()]));
   }
}
