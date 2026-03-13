package com.volmit.iris.util.context;

import com.volmit.iris.engine.IrisComplex;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.util.parallel.MultiBurst;
import com.volmit.iris.util.stream.ProceduralStream;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Job;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B+\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b¢\u0006\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\fR\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0012R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00140\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0012R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0012R\u0017\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00190\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0012R\u0017\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001e0\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0012¨\u0006 "},
   d2 = {"Lcom/volmit/iris/util/context/ChunkContext;", "", "x", "", "z", "c", "Lcom/volmit/iris/engine/IrisComplex;", "cache", "", "<init>", "(IILcom/volmit/iris/engine/IrisComplex;Z)V", "getX", "()I", "getZ", "height", "Lcom/volmit/iris/util/context/ChunkedDataCache;", "", "getHeight", "()Lcom/volmit/iris/util/context/ChunkedDataCache;", "biome", "Lcom/volmit/iris/engine/object/IrisBiome;", "getBiome", "cave", "getCave", "rock", "Lorg/bukkit/block/data/BlockData;", "getRock", "fluid", "getFluid", "region", "Lcom/volmit/iris/engine/object/IrisRegion;", "getRegion", "core"}
)
public final class ChunkContext {
   private final int x;
   private final int z;
   @NotNull
   private final ChunkedDataCache<Double> height;
   @NotNull
   private final ChunkedDataCache<IrisBiome> biome;
   @NotNull
   private final ChunkedDataCache<IrisBiome> cave;
   @NotNull
   private final ChunkedDataCache<BlockData> rock;
   @NotNull
   private final ChunkedDataCache<BlockData> fluid;
   @NotNull
   private final ChunkedDataCache<IrisRegion> region;

   @JvmOverloads
   public ChunkContext(int var1, int var2, @NotNull IrisComplex var3, boolean var4) {
      Intrinsics.checkNotNullParameter(var3, "c");
      super();
      this.x = var1;
      this.z = var2;
      ProceduralStream var10003 = var3.getHeightStream();
      Intrinsics.checkNotNullExpressionValue(var10003, "getHeightStream(...)");
      this.height = new ChunkedDataCache(var10003, this.x, this.z, var4);
      var10003 = var3.getTrueBiomeStream();
      Intrinsics.checkNotNullExpressionValue(var10003, "getTrueBiomeStream(...)");
      this.biome = new ChunkedDataCache(var10003, this.x, this.z, var4);
      var10003 = var3.getCaveBiomeStream();
      Intrinsics.checkNotNullExpressionValue(var10003, "getCaveBiomeStream(...)");
      this.cave = new ChunkedDataCache(var10003, this.x, this.z, var4);
      var10003 = var3.getRockStream();
      Intrinsics.checkNotNullExpressionValue(var10003, "getRockStream(...)");
      this.rock = new ChunkedDataCache(var10003, this.x, this.z, var4);
      var10003 = var3.getFluidStream();
      Intrinsics.checkNotNullExpressionValue(var10003, "getFluidStream(...)");
      this.fluid = new ChunkedDataCache(var10003, this.x, this.z, var4);
      var10003 = var3.getRegionStream();
      Intrinsics.checkNotNullExpressionValue(var10003, "getRegionStream(...)");
      this.region = new ChunkedDataCache(var10003, this.x, this.z, var4);
      if (var4) {
         BuildersKt.runBlocking$default((CoroutineContext)null, (Function2)(new Function2<CoroutineScope, Continuation<? super Job>, Object>((Continuation)null) {
            int label;
            // $FF: synthetic field
            private Object L$0;

            public final Object invokeSuspend(Object var1) {
               CoroutineScope var2 = (CoroutineScope)this.L$0;
               IntrinsicsKt.getCOROUTINE_SUSPENDED();
               switch(this.label) {
               case 0:
                  ResultKt.throwOnFailure(var1);
                  final CoroutineDispatcher var3 = MultiBurst.burst.getDispatcher();
                  BuildersKt.launch$default(var2, (CoroutineContext)null, (CoroutineStart)null, (Function2)(new Function2<CoroutineScope, Continuation<? super Unit>, Object>((Continuation)null) {
                     int label;

                     public final Object invokeSuspend(Object var1) {
                        Object var3x = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        switch(this.label) {
                        case 0:
                           ResultKt.throwOnFailure(var1);
                           ChunkedDataCache var10000 = ChunkContext.this.getHeight();
                           CoroutineDispatcher var2 = var3;
                           Intrinsics.checkNotNull(var2);
                           CoroutineContext var10001 = (CoroutineContext)var2;
                           Continuation var10002 = (Continuation)this;
                           this.label = 1;
                           if (var10000.fill(var10001, var10002) == var3x) {
                              return var3x;
                           }
                           break;
                        case 1:
                           ResultKt.throwOnFailure(var1);
                           break;
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }

                        return Unit.INSTANCE;
                     }

                     public final Continuation<Unit> create(Object var1, Continuation<?> var2) {
                        return (Continuation)(new <anonymous constructor>(var2));
                     }

                     public final Object invoke(CoroutineScope var1, Continuation<? super Unit> var2) {
                        return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
                     }
                  }), 3, (Object)null);
                  BuildersKt.launch$default(var2, (CoroutineContext)null, (CoroutineStart)null, (Function2)(new Function2<CoroutineScope, Continuation<? super Unit>, Object>((Continuation)null) {
                     int label;

                     public final Object invokeSuspend(Object var1) {
                        Object var3x = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        switch(this.label) {
                        case 0:
                           ResultKt.throwOnFailure(var1);
                           ChunkedDataCache var10000 = ChunkContext.this.getBiome();
                           CoroutineDispatcher var2 = var3;
                           Intrinsics.checkNotNull(var2);
                           CoroutineContext var10001 = (CoroutineContext)var2;
                           Continuation var10002 = (Continuation)this;
                           this.label = 1;
                           if (var10000.fill(var10001, var10002) == var3x) {
                              return var3x;
                           }
                           break;
                        case 1:
                           ResultKt.throwOnFailure(var1);
                           break;
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }

                        return Unit.INSTANCE;
                     }

                     public final Continuation<Unit> create(Object var1, Continuation<?> var2) {
                        return (Continuation)(new <anonymous constructor>(var2));
                     }

                     public final Object invoke(CoroutineScope var1, Continuation<? super Unit> var2) {
                        return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
                     }
                  }), 3, (Object)null);
                  BuildersKt.launch$default(var2, (CoroutineContext)null, (CoroutineStart)null, (Function2)(new Function2<CoroutineScope, Continuation<? super Unit>, Object>((Continuation)null) {
                     int label;

                     public final Object invokeSuspend(Object var1) {
                        Object var3x = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        switch(this.label) {
                        case 0:
                           ResultKt.throwOnFailure(var1);
                           ChunkedDataCache var10000 = ChunkContext.this.getCave();
                           CoroutineDispatcher var2 = var3;
                           Intrinsics.checkNotNull(var2);
                           CoroutineContext var10001 = (CoroutineContext)var2;
                           Continuation var10002 = (Continuation)this;
                           this.label = 1;
                           if (var10000.fill(var10001, var10002) == var3x) {
                              return var3x;
                           }
                           break;
                        case 1:
                           ResultKt.throwOnFailure(var1);
                           break;
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }

                        return Unit.INSTANCE;
                     }

                     public final Continuation<Unit> create(Object var1, Continuation<?> var2) {
                        return (Continuation)(new <anonymous constructor>(var2));
                     }

                     public final Object invoke(CoroutineScope var1, Continuation<? super Unit> var2) {
                        return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
                     }
                  }), 3, (Object)null);
                  BuildersKt.launch$default(var2, (CoroutineContext)null, (CoroutineStart)null, (Function2)(new Function2<CoroutineScope, Continuation<? super Unit>, Object>((Continuation)null) {
                     int label;

                     public final Object invokeSuspend(Object var1) {
                        Object var3x = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        switch(this.label) {
                        case 0:
                           ResultKt.throwOnFailure(var1);
                           ChunkedDataCache var10000 = ChunkContext.this.getRock();
                           CoroutineDispatcher var2 = var3;
                           Intrinsics.checkNotNull(var2);
                           CoroutineContext var10001 = (CoroutineContext)var2;
                           Continuation var10002 = (Continuation)this;
                           this.label = 1;
                           if (var10000.fill(var10001, var10002) == var3x) {
                              return var3x;
                           }
                           break;
                        case 1:
                           ResultKt.throwOnFailure(var1);
                           break;
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }

                        return Unit.INSTANCE;
                     }

                     public final Continuation<Unit> create(Object var1, Continuation<?> var2) {
                        return (Continuation)(new <anonymous constructor>(var2));
                     }

                     public final Object invoke(CoroutineScope var1, Continuation<? super Unit> var2) {
                        return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
                     }
                  }), 3, (Object)null);
                  BuildersKt.launch$default(var2, (CoroutineContext)null, (CoroutineStart)null, (Function2)(new Function2<CoroutineScope, Continuation<? super Unit>, Object>((Continuation)null) {
                     int label;

                     public final Object invokeSuspend(Object var1) {
                        Object var3x = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        switch(this.label) {
                        case 0:
                           ResultKt.throwOnFailure(var1);
                           ChunkedDataCache var10000 = ChunkContext.this.getFluid();
                           CoroutineDispatcher var2 = var3;
                           Intrinsics.checkNotNull(var2);
                           CoroutineContext var10001 = (CoroutineContext)var2;
                           Continuation var10002 = (Continuation)this;
                           this.label = 1;
                           if (var10000.fill(var10001, var10002) == var3x) {
                              return var3x;
                           }
                           break;
                        case 1:
                           ResultKt.throwOnFailure(var1);
                           break;
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }

                        return Unit.INSTANCE;
                     }

                     public final Continuation<Unit> create(Object var1, Continuation<?> var2) {
                        return (Continuation)(new <anonymous constructor>(var2));
                     }

                     public final Object invoke(CoroutineScope var1, Continuation<? super Unit> var2) {
                        return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
                     }
                  }), 3, (Object)null);
                  return BuildersKt.launch$default(var2, (CoroutineContext)null, (CoroutineStart)null, (Function2)(new Function2<CoroutineScope, Continuation<? super Unit>, Object>((Continuation)null) {
                     int label;

                     public final Object invokeSuspend(Object var1) {
                        Object var3x = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        switch(this.label) {
                        case 0:
                           ResultKt.throwOnFailure(var1);
                           ChunkedDataCache var10000 = ChunkContext.this.getRegion();
                           CoroutineDispatcher var2 = var3;
                           Intrinsics.checkNotNull(var2);
                           CoroutineContext var10001 = (CoroutineContext)var2;
                           Continuation var10002 = (Continuation)this;
                           this.label = 1;
                           if (var10000.fill(var10001, var10002) == var3x) {
                              return var3x;
                           }
                           break;
                        case 1:
                           ResultKt.throwOnFailure(var1);
                           break;
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }

                        return Unit.INSTANCE;
                     }

                     public final Continuation<Unit> create(Object var1, Continuation<?> var2) {
                        return (Continuation)(new <anonymous constructor>(var2));
                     }

                     public final Object invoke(CoroutineScope var1, Continuation<? super Unit> var2) {
                        return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
                     }
                  }), 3, (Object)null);
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
               }
            }

            public final Continuation<Unit> create(Object var1, Continuation<?> var2) {
               Function2 var3 = new <anonymous constructor>(var2);
               var3.L$0 = var1;
               return (Continuation)var3;
            }

            public final Object invoke(CoroutineScope var1, Continuation<? super Job> var2) {
               return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
            }
         }), 1, (Object)null);
      }

   }

   // $FF: synthetic method
   public ChunkContext(int var1, int var2, IrisComplex var3, boolean var4, int var5, DefaultConstructorMarker var6) {
      if ((var5 & 8) != 0) {
         var4 = true;
      }

      this(var1, var2, var3, var4);
   }

   public final int getX() {
      return this.x;
   }

   public final int getZ() {
      return this.z;
   }

   @NotNull
   public final ChunkedDataCache<Double> getHeight() {
      return this.height;
   }

   @NotNull
   public final ChunkedDataCache<IrisBiome> getBiome() {
      return this.biome;
   }

   @NotNull
   public final ChunkedDataCache<IrisBiome> getCave() {
      return this.cave;
   }

   @NotNull
   public final ChunkedDataCache<BlockData> getRock() {
      return this.rock;
   }

   @NotNull
   public final ChunkedDataCache<BlockData> getFluid() {
      return this.fluid;
   }

   @NotNull
   public final ChunkedDataCache<IrisRegion> getRegion() {
      return this.region;
   }

   @JvmOverloads
   public ChunkContext(int var1, int var2, @NotNull IrisComplex var3) {
      Intrinsics.checkNotNullParameter(var3, "c");
      this(var1, var2, var3, false, 8, (DefaultConstructorMarker)null);
   }
}
