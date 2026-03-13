package com.volmit.iris.util.context;

import com.volmit.iris.util.documentation.BlockCoordinates;
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
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.SupervisorKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u00008\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B1\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u0012\u000e\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0004\b\n\u0010\u000bB3\b\u0017\u0012\u000e\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u0012\b\b\u0002\u0010\b\u001a\u00020\t¢\u0006\u0004\b\n\u0010\fJ\u0018\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0013H\u0086@¢\u0006\u0002\u0010\u0014J\u001f\u0010\u0015\u001a\u0004\u0018\u00018\u00002\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007¢\u0006\u0002\u0010\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0018\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u000eX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u000f¨\u0006\u0017"},
   d2 = {"Lcom/volmit/iris/util/context/ChunkedDataCache;", "T", "", "x", "", "z", "stream", "Lcom/volmit/iris/util/stream/ProceduralStream;", "cache", "", "<init>", "(IILcom/volmit/iris/util/stream/ProceduralStream;Z)V", "(Lcom/volmit/iris/util/stream/ProceduralStream;IIZ)V", "data", "", "[Ljava/lang/Object;", "fill", "", "context", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "get", "(II)Ljava/lang/Object;", "core"}
)
public final class ChunkedDataCache<T> {
   private final int x;
   private final int z;
   @NotNull
   private final ProceduralStream<T> stream;
   private final boolean cache;
   @NotNull
   private final Object[] data;

   private ChunkedDataCache(int var1, int var2, ProceduralStream<T> var3, boolean var4) {
      this.x = var1;
      this.z = var2;
      this.stream = var3;
      this.cache = var4;
      this.data = new Object[this.cache ? 256 : 0];
   }

   @BlockCoordinates
   @JvmOverloads
   public ChunkedDataCache(@NotNull ProceduralStream<T> var1, int var2, int var3, boolean var4) {
      Intrinsics.checkNotNullParameter(var1, "stream");
      this(var2, var3, var1, var4);
   }

   // $FF: synthetic method
   public ChunkedDataCache(ProceduralStream var1, int var2, int var3, boolean var4, int var5, DefaultConstructorMarker var6) {
      if ((var5 & 8) != 0) {
         var4 = true;
      }

      this(var1, var2, var3, var4);
   }

   @Nullable
   public final Object fill(@NotNull final CoroutineContext var1, @NotNull Continuation<? super Unit> var2) {
      if (!this.cache) {
         return Unit.INSTANCE;
      } else {
         Object var10000 = SupervisorKt.supervisorScope((Function2)(new Function2<CoroutineScope, Continuation<? super Unit>, Object>((Continuation)null) {
            int label;
            // $FF: synthetic field
            private Object L$0;

            public final Object invokeSuspend(Object var1x) {
               CoroutineScope var2 = (CoroutineScope)this.L$0;
               IntrinsicsKt.getCOROUTINE_SUSPENDED();
               switch(this.label) {
               case 0:
                  ResultKt.throwOnFailure(var1x);

                  for(final int var3 = 0; var3 < 16; ++var3) {
                     for(final int var4 = 0; var4 < 16; ++var4) {
                        BuildersKt.launch$default(var2, var1, (CoroutineStart)null, (Function2)(new Function2<CoroutineScope, Continuation<? super Unit>, Object>((Continuation)null) {
                           int label;

                           public final Object invokeSuspend(Object var1x) {
                              IntrinsicsKt.getCOROUTINE_SUSPENDED();
                              switch(this.label) {
                              case 0:
                                 ResultKt.throwOnFailure(var1x);
                                 Object var2 = ChunkedDataCache.this.stream.get((double)(ChunkedDataCache.this.x + var3), (double)(ChunkedDataCache.this.z + var4));
                                 ChunkedDataCache.this.data[var4 * 16 + var3] = var2;
                                 return Unit.INSTANCE;
                              default:
                                 throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                              }
                           }

                           public final Continuation<Unit> create(Object var1x, Continuation<?> var2) {
                              return (Continuation)(new <anonymous constructor>(var2));
                           }

                           public final Object invoke(CoroutineScope var1x, Continuation<? super Unit> var2) {
                              return ((<undefinedtype>)this.create(var1x, var2)).invokeSuspend(Unit.INSTANCE);
                           }
                        }), 2, (Object)null);
                     }
                  }

                  return Unit.INSTANCE;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
               }
            }

            public final Continuation<Unit> create(Object var1x, Continuation<?> var2) {
               Function2 var3 = new <anonymous constructor>(var2);
               var3.L$0 = var1x;
               return (Continuation)var3;
            }

            public final Object invoke(CoroutineScope var1x, Continuation<? super Unit> var2) {
               return ((<undefinedtype>)this.create(var1x, var2)).invokeSuspend(Unit.INSTANCE);
            }
         }), var2);
         return var10000 == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? var10000 : Unit.INSTANCE;
      }
   }

   // $FF: synthetic method
   public static Object fill$default(ChunkedDataCache var0, CoroutineContext var1, Continuation var2, int var3, Object var4) {
      if ((var3 & 1) != 0) {
         var1 = (CoroutineContext)Dispatchers.getDefault();
      }

      return var0.fill(var1, var2);
   }

   @BlockCoordinates
   @Nullable
   public final T get(int var1, int var2) {
      if (!this.cache) {
         return this.stream.get((double)(this.x + var1), (double)(this.z + var2));
      } else {
         Object var10000 = this.data[var2 * 16 + var1];
         if (var10000 == null) {
            var10000 = null;
         }

         Object var3 = var10000;
         var10000 = var3;
         if (var3 == null) {
            var10000 = this.stream.get((double)(this.x + var1), (double)(this.z + var2));
         }

         return var10000;
      }
   }

   @BlockCoordinates
   @JvmOverloads
   public ChunkedDataCache(@NotNull ProceduralStream<T> var1, int var2, int var3) {
      Intrinsics.checkNotNullParameter(var1, "stream");
      this(var1, var2, var3, false, 8, (DefaultConstructorMarker)null);
   }
}
