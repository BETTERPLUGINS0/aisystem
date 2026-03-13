package com.volmit.iris.engine.mantle;

import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.nms.container.Pair;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.documentation.ChunkCoordinates;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.mantle.MantleChunk;
import com.volmit.iris.util.mantle.flag.MantleFlag;
import com.volmit.iris.util.parallel.MultiBurst;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jdk7.AutoCloseableKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Job;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u0000  2\u00020\u0001:\u0001 J(\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u000b2\u0006\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0017J=\u0010\n\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u000b2\u0006\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b2\u001a\b\u0004\u0010\u001e\u001a\u0014\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00170\u001fH\u0082\bR\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X¦\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0012\u0010\n\u001a\u00020\u000bX¦\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0012\u0010\u000e\u001a\u00020\u000bX¦\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\rR*\u0010\u0010\u001a\u001a\u0012\u0016\u0012\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u0011\u0012\u0004\u0012\u00020\u000b0\u00120\u0011X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015¨\u0006!À\u0006\u0003"},
   d2 = {"Lcom/volmit/iris/engine/mantle/MatterGenerator;", "", "engine", "Lcom/volmit/iris/engine/framework/Engine;", "getEngine", "()Lcom/volmit/iris/engine/framework/Engine;", "mantle", "Lcom/volmit/iris/util/mantle/Mantle;", "getMantle", "()Lcom/volmit/iris/util/mantle/Mantle;", "radius", "", "getRadius", "()I", "realRadius", "getRealRadius", "components", "", "Lcom/volmit/iris/core/nms/container/Pair;", "Lcom/volmit/iris/engine/mantle/MantleComponent;", "getComponents", "()Ljava/util/List;", "generateMatter", "", "x", "z", "multicore", "", "context", "Lcom/volmit/iris/util/context/ChunkContext;", "task", "Lkotlin/Function2;", "Companion", "core"}
)
@SourceDebugExtension({"SMAP\nMatterGenerator.kt\nKotlin\n*S Kotlin\n*F\n+ 1 MatterGenerator.kt\ncom/volmit/iris/engine/mantle/MatterGenerator\n*L\n1#1,70:1\n58#1,6:71\n*S KotlinDebug\n*F\n+ 1 MatterGenerator.kt\ncom/volmit/iris/engine/mantle/MatterGenerator\n*L\n50#1:71,6\n*E\n"})
public interface MatterGenerator {
   @NotNull
   MatterGenerator.Companion Companion = MatterGenerator.Companion.$$INSTANCE;

   @NotNull
   Engine getEngine();

   @NotNull
   Mantle getMantle();

   int getRadius();

   int getRealRadius();

   @NotNull
   List<Pair<List<MantleComponent>, Integer>> getComponents();

   @ChunkCoordinates
   default void generateMatter(final int x, final int z, boolean multicore, @NotNull final ChunkContext context) {
      Intrinsics.checkNotNullParameter(context, "context");
      if (this.getEngine().getDimension().isUseMantle()) {
         final boolean multicore = multicore || IrisSettings.get().getGenerator().isUseMulticoreMantle();
         AutoCloseable var6 = (AutoCloseable)this.getMantle().write(this.getEngine().getMantle(), x, z, this.getRadius(), multicore);
         Throwable var7 = null;

         try {
            final MantleWriter writer = (MantleWriter)var6;
            int var9 = false;
            Iterator var10 = this.getComponents().iterator();

            while(var10.hasNext()) {
               final Pair pair = (Pair)var10.next();
               BuildersKt.runBlocking$default((CoroutineContext)null, (Function2)(new Function2<CoroutineScope, Continuation<? super Unit>, Object>((Continuation)null) {
                  int label;
                  // $FF: synthetic field
                  private Object L$0;

                  public final Object invokeSuspend(Object var1) {
                     CoroutineScope var2 = (CoroutineScope)this.L$0;
                     IntrinsicsKt.getCOROUTINE_SUSPENDED();
                     switch(this.label) {
                     case 0:
                        ResultKt.throwOnFailure(var1);
                        MatterGenerator var3 = MatterGenerator.this;
                        int var4 = x;
                        int var5 = z;
                        Object var10000 = pair.getB();
                        Intrinsics.checkNotNullExpressionValue(var10000, "getB(...)");
                        int var6 = ((Number)var10000).intValue();
                        final MantleWriter var7 = writer;
                        Pair var8 = pair;
                        boolean var9 = multicore;
                        final ChunkContext var10 = context;
                        boolean var11 = false;
                        int var12 = -var6;
                        if (var12 <= var6) {
                           while(true) {
                              int var13 = -var6;
                              if (var13 <= var6) {
                                 while(true) {
                                    int var20 = var4 + var12;
                                    final int var14 = var5 + var13;
                                    final int var15 = var20;
                                    boolean var16 = false;
                                    final MantleChunk var17 = var7.acquireChunk(var15, var14);
                                    MantleFlag var10001 = MantleFlag.PLANNED;
                                    Intrinsics.checkNotNullExpressionValue(var10001, "PLANNED");
                                    if (!var17.isFlagged(var10001)) {
                                       Iterator var18 = ((List)var8.getA()).iterator();

                                       while(var18.hasNext()) {
                                          final MantleComponent var19 = (MantleComponent)var18.next();
                                          var10001 = var19.getFlag();
                                          Intrinsics.checkNotNullExpressionValue(var10001, "getFlag(...)");
                                          if (!var17.isFlagged(var10001)) {
                                             MatterGenerator.Companion.launch(var2, var9, (Function2)(new Function2<CoroutineScope, Continuation<? super Unit>, Object>((Continuation)null) {
                                                int label;

                                                public final Object invokeSuspend(Object var1) {
                                                   Object var2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                                                   switch(this.label) {
                                                   case 0:
                                                      ResultKt.throwOnFailure(var1);
                                                      MantleChunk var10000 = var17;
                                                      MantleFlag var10001 = var19.getFlag();
                                                      Intrinsics.checkNotNullExpressionValue(var10001, "getFlag(...)");
                                                      Function1 var10002 = (Function1)(new Function1<Continuation<? super Unit>, Object>((Continuation)null) {
                                                         int label;

                                                         public final Object invokeSuspend(Object var1) {
                                                            IntrinsicsKt.getCOROUTINE_SUSPENDED();
                                                            switch(this.label) {
                                                            case 0:
                                                               ResultKt.throwOnFailure(var1);
                                                               var19.generateLayer(var7, var15, var14, var10);
                                                               return Unit.INSTANCE;
                                                            default:
                                                               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                                            }
                                                         }

                                                         public final Continuation<Unit> create(Continuation<?> var1) {
                                                            return (Continuation)(new <anonymous constructor>(var1));
                                                         }

                                                         public final Object invoke(Continuation<? super Unit> var1) {
                                                            return ((<undefinedtype>)this.create(var1)).invokeSuspend(Unit.INSTANCE);
                                                         }
                                                      });
                                                      Continuation var10003 = (Continuation)this;
                                                      this.label = 1;
                                                      if (var10000.raiseFlagSuspend(var10001, var10002, var10003) == var2) {
                                                         return var2;
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
                                             }));
                                          }
                                       }
                                    }

                                    if (var13 == var6) {
                                       break;
                                    }

                                    ++var13;
                                 }
                              }

                              if (var12 == var6) {
                                 break;
                              }

                              ++var12;
                           }
                        }

                        return Unit.INSTANCE;
                     default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                     }
                  }

                  public final Continuation<Unit> create(Object var1, Continuation<?> var2) {
                     Function2 var3 = new <anonymous constructor>(var2);
                     var3.L$0 = var1;
                     return (Continuation)var3;
                  }

                  public final Object invoke(CoroutineScope var1, Continuation<? super Unit> var2) {
                     return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
                  }
               }), 1, (Object)null);
            }

            int x$iv = x;
            int z$iv = z;
            int radius$iv = this.getRealRadius();
            int $i$f$radius = false;
            int i$iv = -radius$iv;
            if (i$iv <= radius$iv) {
               while(true) {
                  int j$iv = -radius$iv;
                  if (j$iv <= radius$iv) {
                     while(true) {
                        int var10000 = x$iv + i$iv;
                        int z = z$iv + j$iv;
                        int x = var10000;
                        int var19 = false;
                        MantleChunk var26 = writer.acquireChunk(x, z);
                        MantleFlag var10001 = MantleFlag.PLANNED;
                        Intrinsics.checkNotNullExpressionValue(var10001, "PLANNED");
                        var26.flag(var10001, true);
                        if (j$iv == radius$iv) {
                           break;
                        }

                        ++j$iv;
                     }
                  }

                  if (i$iv == radius$iv) {
                     break;
                  }

                  ++i$iv;
               }
            }

            Unit var24 = Unit.INSTANCE;
         } catch (Throwable var22) {
            var7 = var22;
            throw var22;
         } finally {
            AutoCloseableKt.closeFinally(var6, var7);
         }

      }
   }

   private default void radius(int x, int z, int radius, Function2<? super Integer, ? super Integer, Unit> task) {
      int $i$f$radius = false;
      int i = -radius;
      if (i <= radius) {
         while(true) {
            int j = -radius;
            if (j <= radius) {
               while(true) {
                  task.invoke(x + i, z + j);
                  if (j == radius) {
                     break;
                  }

                  ++j;
               }
            }

            if (i == radius) {
               break;
            }

            ++i;
         }
      }

   }

   @Metadata(
      mv = {2, 2, 0},
      k = 1,
      xi = 48,
      d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003JB\u0010\u0007\u001a\u00020\b*\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2'\u0010\f\u001a#\b\u0001\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00010\r¢\u0006\u0002\b\u0010H\u0002¢\u0006\u0002\u0010\u0011R\u0016\u0010\u0004\u001a\n \u0006*\u0004\u0018\u00010\u00050\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"},
      d2 = {"Lcom/volmit/iris/engine/mantle/MatterGenerator$Companion;", "", "<init>", "()V", "dispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "kotlin.jvm.PlatformType", "launch", "Lkotlinx/coroutines/Job;", "Lkotlinx/coroutines/CoroutineScope;", "multicore", "", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/CoroutineScope;ZLkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/Job;", "core"}
   )
   public static final class Companion {
      // $FF: synthetic field
      static final MatterGenerator.Companion $$INSTANCE = new MatterGenerator.Companion();
      private static final CoroutineDispatcher dispatcher;

      private Companion() {
      }

      private final Job launch(CoroutineScope var1, boolean var2, Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> var3) {
         CoroutineContext var4 = var2 ? (CoroutineContext)dispatcher : (CoroutineContext)EmptyCoroutineContext.INSTANCE;
         Intrinsics.checkNotNull(var4);
         return BuildersKt.launch$default(var1, var4, (CoroutineStart)null, var3, 2, (Object)null);
      }

      static {
         dispatcher = MultiBurst.burst.getDispatcher();
      }
   }

   @Metadata(
      mv = {2, 2, 0},
      k = 3,
      xi = 48
   )
   public static final class DefaultImpls {
      /** @deprecated */
      @ChunkCoordinates
      @Deprecated
      public static void generateMatter(@NotNull MatterGenerator var0, int var1, int var2, boolean var3, @NotNull ChunkContext var4) {
         Intrinsics.checkNotNullParameter(var4, "context");
         var0.generateMatter(var1, var2, var3, var4);
      }
   }
}
