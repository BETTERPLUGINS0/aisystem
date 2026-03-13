package com.volmit.iris.core.pregenerator.cache;

import com.volmit.iris.Iris;
import com.volmit.iris.util.data.Varint;
import com.volmit.iris.util.documentation.ChunkCoordinates;
import com.volmit.iris.util.documentation.RegionCoordinates;
import com.volmit.iris.util.io.IO;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;
import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\t\u0018\u0000 \u001f2\u00020\u0001:\u0003\u001d\u001e\u001fB\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0017J\u0018\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0017J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0017J\u0018\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0017J\b\u0010\u0014\u001a\u00020\u0012H\u0016J\u0010\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u0017H\u0016J\u0019\u0010\u0018\u001a\u00020\u000b2\u0006\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0082\u0002J\u0018\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0002J\u0010\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\u000bH\u0002J\u0018\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R&\u0010\b\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\n\u0012\u0004\u0012\u00020\u000b0\tX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006 "},
   d2 = {"Lcom/volmit/iris/core/pregenerator/cache/PregenCacheImpl;", "Lcom/volmit/iris/core/pregenerator/cache/PregenCache;", "directory", "Ljava/io/File;", "maxSize", "", "<init>", "(Ljava/io/File;I)V", "cache", "Lit/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenHashMap;", "Lkotlin/Pair;", "Lcom/volmit/iris/core/pregenerator/cache/PregenCacheImpl$Plate;", "isChunkCached", "", "x", "z", "isRegionCached", "cacheChunk", "", "cacheRegion", "write", "trim", "unloadDuration", "", "get", "readPlate", "writePlate", "plate", "fileForPlate", "Plate", "Region", "Companion", "core"}
)
@SourceDebugExtension({"SMAP\nPregenCacheImpl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PregenCacheImpl.kt\ncom/volmit/iris/core/pregenerator/cache/PregenCacheImpl\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,232:1\n1#2:233\n*E\n"})
public final class PregenCacheImpl implements PregenCache {
   @NotNull
   public static final PregenCacheImpl.Companion Companion = new PregenCacheImpl.Companion((DefaultConstructorMarker)null);
   @NotNull
   private final File directory;
   private final int maxSize;
   @NotNull
   private final Object2ObjectLinkedOpenHashMap<Pair<Integer, Integer>, PregenCacheImpl.Plate> cache;
   @NotNull
   private static final CoroutineDispatcher dispatcher = CoroutineDispatcher.limitedParallelism$default(Dispatchers.getIO(), 4, (String)null, 2, (Object)null);
   private static final short SIZE = 1024;

   public PregenCacheImpl(@NotNull File var1, int var2) {
      Intrinsics.checkNotNullParameter(var1, "directory");
      super();
      this.directory = var1;
      this.maxSize = var2;
      this.cache = new Object2ObjectLinkedOpenHashMap();
   }

   @ChunkCoordinates
   public boolean isChunkCached(int var1, int var2) {
      return this.get(var1 >> 10, var2 >> 10).isCached(var1 >> 5 & 31, var2 >> 5 & 31, PregenCacheImpl::isChunkCached$lambda$0);
   }

   @RegionCoordinates
   public boolean isRegionCached(int var1, int var2) {
      return this.get(var1 >> 5, var2 >> 5).isCached(var1 & 31, var2 & 31, (Function1)null.INSTANCE);
   }

   @ChunkCoordinates
   public void cacheChunk(int var1, int var2) {
      this.get(var1 >> 10, var2 >> 10).cache(var1 >> 5 & 31, var2 >> 5 & 31, PregenCacheImpl::cacheChunk$lambda$1);
   }

   @RegionCoordinates
   public void cacheRegion(int var1, int var2) {
      this.get(var1 >> 5, var2 >> 5).cache(var1 & 31, var2 & 31, (Function1)null.INSTANCE);
   }

   public void write() {
      if (!this.cache.isEmpty()) {
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
                  ObjectIterator var10000 = ((ObjectCollection)PregenCacheImpl.this.cache.values()).iterator();
                  Intrinsics.checkNotNullExpressionValue(var10000, "iterator(...)");
                  ObjectIterator var3 = var10000;

                  while(var3.hasNext()) {
                     final PregenCacheImpl.Plate var4 = (PregenCacheImpl.Plate)var3.next();
                     if (var4.getDirty()) {
                        BuildersKt.launch$default(var2, (CoroutineContext)PregenCacheImpl.dispatcher, (CoroutineStart)null, (Function2)(new Function2<CoroutineScope, Continuation<? super Unit>, Object>((Continuation)null) {
                           int label;

                           public final Object invokeSuspend(Object var1) {
                              IntrinsicsKt.getCOROUTINE_SUSPENDED();
                              switch(this.label) {
                              case 0:
                                 ResultKt.throwOnFailure(var1);
                                 PregenCacheImpl var10000 = PregenCacheImpl.this;
                                 PregenCacheImpl.Plate var2 = var4;
                                 Intrinsics.checkNotNull(var2);
                                 var10000.writePlate(var2);
                                 return Unit.INSTANCE;
                              default:
                                 throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                              }
                           }

                           public final Continuation<Unit> create(Object var1, Continuation<?> var2) {
                              return (Continuation)(new <anonymous constructor>(var2));
                           }

                           public final Object invoke(CoroutineScope var1, Continuation<? super Unit> var2) {
                              return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
                           }
                        }), 2, (Object)null);
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
   }

   public void trim(long var1) {
      if (!this.cache.isEmpty()) {
         final long var3 = System.currentTimeMillis() - var1;
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
                  ObjectIterator var10000 = ((ObjectCollection)PregenCacheImpl.this.cache.values()).iterator();
                  Intrinsics.checkNotNullExpressionValue(var10000, "iterator(...)");

                  final PregenCacheImpl.Plate var4;
                  for(ObjectIterator var3x = var10000; var3x.hasNext(); BuildersKt.launch$default(var2, (CoroutineContext)PregenCacheImpl.dispatcher, (CoroutineStart)null, (Function2)(new Function2<CoroutineScope, Continuation<? super Unit>, Object>((Continuation)null) {
                     int label;

                     public final Object invokeSuspend(Object var1) {
                        IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        switch(this.label) {
                        case 0:
                           ResultKt.throwOnFailure(var1);
                           PregenCacheImpl var10000 = PregenCacheImpl.this;
                           PregenCacheImpl.Plate var2 = var4;
                           Intrinsics.checkNotNull(var2);
                           var10000.writePlate(var2);
                           return Unit.INSTANCE;
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                     }

                     public final Continuation<Unit> create(Object var1, Continuation<?> var2) {
                        return (Continuation)(new <anonymous constructor>(var2));
                     }

                     public final Object invoke(CoroutineScope var1, Continuation<? super Unit> var2) {
                        return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
                     }
                  }), 2, (Object)null)) {
                     var4 = (PregenCacheImpl.Plate)var3x.next();
                     if (var4.getLastAccess() < var3) {
                        var3x.remove();
                     }
                  }

                  return Unit.INSTANCE;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
               }
            }

            public final Continuation<Unit> create(Object var1, Continuation<?> var2) {
               Function2 var3x = new <anonymous constructor>(var2);
               var3x.L$0 = var1;
               return (Continuation)var3x;
            }

            public final Object invoke(CoroutineScope var1, Continuation<? super Unit> var2) {
               return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
            }
         }), 1, (Object)null);
      }
   }

   private final PregenCacheImpl.Plate get(int var1, int var2) {
      Pair var3 = TuplesKt.to(var1, var2);
      PregenCacheImpl.Plate var4 = (PregenCacheImpl.Plate)this.cache.getAndMoveToFirst(var3);
      if (var4 != null) {
         return var4;
      } else {
         PregenCacheImpl.Plate var5 = this.readPlate(var1, var2);
         boolean var7 = false;
         this.cache.putAndMoveToFirst(var3, var5);
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

                  while(PregenCacheImpl.this.cache.size() > PregenCacheImpl.this.maxSize) {
                     final PregenCacheImpl.Plate var3 = (PregenCacheImpl.Plate)PregenCacheImpl.this.cache.removeLast();
                     BuildersKt.launch$default(var2, (CoroutineContext)PregenCacheImpl.dispatcher, (CoroutineStart)null, (Function2)(new Function2<CoroutineScope, Continuation<? super Unit>, Object>((Continuation)null) {
                        int label;

                        public final Object invokeSuspend(Object var1) {
                           IntrinsicsKt.getCOROUTINE_SUSPENDED();
                           switch(this.label) {
                           case 0:
                              ResultKt.throwOnFailure(var1);
                              PregenCacheImpl var10000 = PregenCacheImpl.this;
                              PregenCacheImpl.Plate var2 = var3;
                              Intrinsics.checkNotNull(var2);
                              var10000.writePlate(var2);
                              return Unit.INSTANCE;
                           default:
                              throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                           }
                        }

                        public final Continuation<Unit> create(Object var1, Continuation<?> var2) {
                           return (Continuation)(new <anonymous constructor>(var2));
                        }

                        public final Object invoke(CoroutineScope var1, Continuation<? super Unit> var2) {
                           return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
                        }
                     }), 2, (Object)null);
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
         return var5;
      }
   }

   private final PregenCacheImpl.Plate readPlate(int var1, int var2) {
      File var3 = this.fileForPlate(var1, var2);
      if (!var3.exists()) {
         return new PregenCacheImpl.Plate(var1, var2, (short)0, (PregenCacheImpl.Region[])null, 12, (DefaultConstructorMarker)null);
      } else {
         try {
            Closeable var4 = (Closeable)(new DataInputStream((InputStream)(new LZ4BlockInputStream((InputStream)(new FileInputStream(var3))))));
            Throwable var5 = null;

            PregenCacheImpl.Plate var8;
            try {
               DataInputStream var6 = (DataInputStream)var4;
               boolean var7 = false;
               var8 = Companion.readPlate(var1, var2, (DataInput)var6);
            } catch (Throwable var12) {
               var5 = var12;
               throw var12;
            } finally {
               CloseableKt.closeFinally(var4, var5);
            }

            return var8;
         } catch (IOException var14) {
            Iris.error("Failed to read pregen cache " + var3);
            var14.printStackTrace();
            Iris.reportError((Throwable)var14);
            return new PregenCacheImpl.Plate(var1, var2, (short)0, (PregenCacheImpl.Region[])null, 12, (DefaultConstructorMarker)null);
         }
      }
   }

   private final void writePlate(PregenCacheImpl.Plate var1) {
      if (var1.getDirty()) {
         File var2 = this.fileForPlate(var1.getX(), var1.getZ());

         try {
            IO.write(var2, PregenCacheImpl::writePlate$lambda$4, var1::write);
            var1.setDirty(false);
         } catch (IOException var4) {
            Iris.error("Failed to write preen cache " + var2);
            var4.printStackTrace();
            Iris.reportError((Throwable)var4);
         }

      }
   }

   private final File fileForPlate(int var1, int var2) {
      if (!this.directory.exists() && !this.directory.mkdirs()) {
         boolean var3 = false;
         String var4 = "Cannot create directory: " + this.directory.getAbsolutePath();
         throw new IllegalStateException(var4.toString());
      } else {
         return new File(this.directory, "c." + var1 + "." + var2 + ".lz4b");
      }
   }

   private static final boolean isChunkCached$lambda$0(int var0, int var1, PregenCacheImpl.Region var2) {
      Intrinsics.checkNotNullParameter(var2, "$this$isCached");
      return var2.isCached(var0 & 31, var1 & 31);
   }

   private static final boolean cacheChunk$lambda$1(int var0, int var1, PregenCacheImpl.Region var2) {
      Intrinsics.checkNotNullParameter(var2, "$this$cache");
      return var2.cache(var0 & 31, var1 & 31);
   }

   private static final DataOutputStream writePlate$lambda$4(FileOutputStream var0) {
      return new DataOutputStream((OutputStream)(new LZ4BlockOutputStream((OutputStream)var0)));
   }

   @Metadata(
      mv = {2, 2, 0},
      k = 1,
      xi = 48,
      d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\n\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J \u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\r\u001a\u00020\u000eH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0011"},
      d2 = {"Lcom/volmit/iris/core/pregenerator/cache/PregenCacheImpl$Companion;", "", "<init>", "()V", "dispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "SIZE", "", "readPlate", "Lcom/volmit/iris/core/pregenerator/cache/PregenCacheImpl$Plate;", "x", "", "z", "din", "Ljava/io/DataInput;", "readRegion", "Lcom/volmit/iris/core/pregenerator/cache/PregenCacheImpl$Region;", "core"}
   )
   public static final class Companion {
      private Companion() {
      }

      private final PregenCacheImpl.Plate readPlate(int var1, int var2, DataInput var3) {
         int var4 = Varint.readSignedVarInt(var3);
         if (var4 == 1024) {
            return new PregenCacheImpl.Plate(var1, var2, (short)1024, (PregenCacheImpl.Region[])null);
         } else {
            short var10002 = (short)var4;
            int var5 = 0;
            PregenCacheImpl.Region[] var6 = new PregenCacheImpl.Region[1024];

            short var10;
            for(var10 = var10002; var5 < 1024; ++var5) {
               var6[var5] = var3.readBoolean() ? null : PregenCacheImpl.Companion.readRegion(var3);
            }

            return new PregenCacheImpl.Plate(var1, var2, var10, var6);
         }
      }

      private final PregenCacheImpl.Region readRegion(DataInput var1) {
         int var2 = Varint.readSignedVarInt(var1);
         PregenCacheImpl.Region var10000;
         if (var2 == 1024) {
            var10000 = new PregenCacheImpl.Region((short)1024, (long[])null);
         } else {
            short var9 = (short)var2;
            int var3 = 0;
            long[] var4 = new long[64];

            short var6;
            for(var6 = var9; var3 < 64; ++var3) {
               var4[var3] = Varint.readUnsignedVarLong(var1);
            }

            var10000 = new PregenCacheImpl.Region(var6, var4);
         }

         return var10000;
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }
   }

   @Metadata(
      mv = {2, 2, 0},
      k = 1,
      xi = 48,
      d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\n\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\u0012\b\u0002\u0010\u0007\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\t\u0018\u00010\b¢\u0006\u0004\b\n\u0010\u000bJ/\u0010\u001c\u001a\u00020\u00112\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0017\u0010\u001d\u001a\u0013\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00110\u001e¢\u0006\u0002\b\u001fJ/\u0010 \u001a\u00020\u00112\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0017\u0010\u001d\u001a\u0013\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00110\u001e¢\u0006\u0002\b\u001fJ\u000e\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\t\u0018\u00010\bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u000fR\u001a\u0010\u0010\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u001a\u0010\u0016\u001a\u00020\u0017X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001b¨\u0006%"},
      d2 = {"Lcom/volmit/iris/core/pregenerator/cache/PregenCacheImpl$Plate;", "", "x", "", "z", "count", "", "regions", "", "Lcom/volmit/iris/core/pregenerator/cache/PregenCacheImpl$Region;", "<init>", "(IIS[Lcom/volmit/iris/core/pregenerator/cache/PregenCacheImpl$Region;)V", "getX", "()I", "getZ", "[Lcom/volmit/iris/core/pregenerator/cache/PregenCacheImpl$Region;", "dirty", "", "getDirty", "()Z", "setDirty", "(Z)V", "lastAccess", "", "getLastAccess", "()J", "setLastAccess", "(J)V", "cache", "predicate", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "isCached", "write", "", "dos", "Ljava/io/DataOutput;", "core"}
   )
   @SourceDebugExtension({"SMAP\nPregenCacheImpl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PregenCacheImpl.kt\ncom/volmit/iris/core/pregenerator/cache/PregenCacheImpl$Plate\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 _Arrays.kt\nkotlin/collections/ArraysKt___ArraysKt\n*L\n1#1,232:1\n1#2:233\n13472#3,2:234\n*S KotlinDebug\n*F\n+ 1 PregenCacheImpl.kt\ncom/volmit/iris/core/pregenerator/cache/PregenCacheImpl$Plate\n*L\n161#1:234,2\n*E\n"})
   private static final class Plate {
      private final int x;
      private final int z;
      private short count;
      @Nullable
      private PregenCacheImpl.Region[] regions;
      private boolean dirty;
      private long lastAccess;

      public Plate(int var1, int var2, short var3, @Nullable PregenCacheImpl.Region[] var4) {
         this.x = var1;
         this.z = var2;
         this.count = var3;
         this.regions = var4;
         this.lastAccess = System.currentTimeMillis();
      }

      // $FF: synthetic method
      public Plate(int var1, int var2, short var3, PregenCacheImpl.Region[] var4, int var5, DefaultConstructorMarker var6) {
         if ((var5 & 4) != 0) {
            var3 = 0;
         }

         if ((var5 & 8) != 0) {
            var4 = new PregenCacheImpl.Region[1024];
         }

         this(var1, var2, var3, var4);
      }

      public final int getX() {
         return this.x;
      }

      public final int getZ() {
         return this.z;
      }

      public final boolean getDirty() {
         return this.dirty;
      }

      public final void setDirty(boolean var1) {
         this.dirty = var1;
      }

      public final long getLastAccess() {
         return this.lastAccess;
      }

      public final void setLastAccess(long var1) {
         this.lastAccess = var1;
      }

      public final boolean cache(int var1, int var2, @NotNull Function1<? super PregenCacheImpl.Region, Boolean> var3) {
         Intrinsics.checkNotNullParameter(var3, "predicate");
         this.lastAccess = System.currentTimeMillis();
         if (this.count == 1024) {
            return false;
         } else {
            PregenCacheImpl.Region[] var10000 = this.regions;
            Intrinsics.checkNotNull(var10000);
            PregenCacheImpl.Region[] var5 = var10000;
            boolean var6 = false;
            PregenCacheImpl.Region var10 = var5[var1 * 32 + var2];
            if (var5[var1 * 32 + var2] == null) {
               PregenCacheImpl.Region var7 = new PregenCacheImpl.Region((short)0, (long[])null, 3, (DefaultConstructorMarker)null);
               boolean var9 = false;
               var5[var1 * 32 + var2] = var7;
               var10 = var7;
            }

            PregenCacheImpl.Region var4 = var10;
            if (!(Boolean)var3.invoke(var4)) {
               return false;
            } else {
               ++this.count;
               if (this.count == 1024) {
                  this.regions = null;
               }

               this.dirty = true;
               return true;
            }
         }
      }

      public final boolean isCached(int var1, int var2, @NotNull Function1<? super PregenCacheImpl.Region, Boolean> var3) {
         Intrinsics.checkNotNullParameter(var3, "predicate");
         this.lastAccess = System.currentTimeMillis();
         if (this.count == 1024) {
            return true;
         } else {
            PregenCacheImpl.Region[] var10000 = this.regions;
            Intrinsics.checkNotNull(var10000);
            PregenCacheImpl.Region var5 = var10000[var1 * 32 + var2];
            if (var5 == null) {
               return false;
            } else {
               PregenCacheImpl.Region var4 = var5;
               return (Boolean)var3.invoke(var4);
            }
         }
      }

      public final void write(@NotNull DataOutput var1) {
         Intrinsics.checkNotNullParameter(var1, "dos");
         Varint.writeSignedVarInt(this.count, var1);
         PregenCacheImpl.Region[] var10000 = this.regions;
         if (var10000 != null) {
            PregenCacheImpl.Region[] var2 = var10000;
            boolean var3 = false;
            int var4 = 0;

            for(int var5 = var2.length; var4 < var5; ++var4) {
               PregenCacheImpl.Region var6 = var2[var4];
               boolean var8 = false;
               var1.writeBoolean(var6 == null);
               if (var6 != null) {
                  var6.write(var1);
               }
            }
         }

      }
   }

   @Metadata(
      mv = {2, 2, 0},
      k = 1,
      xi = 48,
      d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\n\n\u0000\n\u0002\u0010\u0016\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u001d\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u0006\u0010\b\u001a\u00020\tJ\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bJ\u0006\u0010\r\u001a\u00020\tJ\u0016\u0010\r\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bJ\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0012"},
      d2 = {"Lcom/volmit/iris/core/pregenerator/cache/PregenCacheImpl$Region;", "", "count", "", "words", "", "<init>", "(S[J)V", "cache", "", "x", "", "z", "isCached", "write", "", "dos", "Ljava/io/DataOutput;", "core"}
   )
   @SourceDebugExtension({"SMAP\nPregenCacheImpl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PregenCacheImpl.kt\ncom/volmit/iris/core/pregenerator/cache/PregenCacheImpl$Region\n+ 2 _Arrays.kt\nkotlin/collections/ArraysKt___ArraysKt\n*L\n1#1,232:1\n13500#2,2:233\n*S KotlinDebug\n*F\n+ 1 PregenCacheImpl.kt\ncom/volmit/iris/core/pregenerator/cache/PregenCacheImpl$Region\n*L\n207#1:233,2\n*E\n"})
   private static final class Region {
      private short count;
      @Nullable
      private long[] words;

      public Region(short var1, @Nullable long[] var2) {
         this.count = var1;
         this.words = var2;
      }

      // $FF: synthetic method
      public Region(short var1, long[] var2, int var3, DefaultConstructorMarker var4) {
         if ((var3 & 1) != 0) {
            var1 = 0;
         }

         if ((var3 & 2) != 0) {
            var2 = new long[64];
         }

         this(var1, var2);
      }

      public final boolean cache() {
         if (this.count == 1024) {
            return false;
         } else {
            this.count = 1024;
            this.words = null;
            return true;
         }
      }

      public final boolean cache(int var1, int var2) {
         if (this.count == 1024) {
            return false;
         } else {
            long[] var10000 = this.words;
            if (var10000 == null) {
               return false;
            } else {
               long[] var3 = var10000;
               int var4 = var1 * 32 + var2;
               int var5 = var4 >> 6;
               long var6 = 1L << (var4 & 63);
               boolean var8 = (var3[var5] & var6) != 0L;
               if (var8) {
                  return false;
               } else {
                  ++this.count;
                  if (this.count == 1024) {
                     this.words = null;
                     return true;
                  } else {
                     var3[var5] |= var6;
                     return false;
                  }
               }
            }
         }
      }

      public final boolean isCached() {
         return this.count == 1024;
      }

      public final boolean isCached(int var1, int var2) {
         int var3 = var1 * 32 + var2;
         boolean var4;
         if (this.count != 1024) {
            long[] var10000 = this.words;
            Intrinsics.checkNotNull(var10000);
            if ((var10000[var3 >> 6] & 1L << (var3 & 63)) == 0L) {
               var4 = false;
               return var4;
            }
         }

         var4 = true;
         return var4;
      }

      public final void write(@NotNull DataOutput var1) {
         Intrinsics.checkNotNullParameter(var1, "dos");
         Varint.writeSignedVarInt(this.count, var1);
         long[] var10000 = this.words;
         if (var10000 != null) {
            long[] var2 = var10000;
            boolean var3 = false;
            int var4 = 0;

            for(int var5 = var2.length; var4 < var5; ++var4) {
               long var6 = var2[var4];
               boolean var10 = false;
               Varint.writeUnsignedVarLong(var6, var1);
            }
         }

      }

      public Region() {
         this((short)0, (long[])null, 3, (DefaultConstructorMarker)null);
      }
   }
}
