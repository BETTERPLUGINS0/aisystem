package advancedplugins.pm2.cv.fake;

import com.google.common.collect.Sets;
import io.netty.channel.ChannelPipeline;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public abstract class FakeEntityLinker {
   protected final Set<ChannelPipeline> toFlush = Sets.newConcurrentHashSet();
   protected volatile boolean active;

   public static FakeEntityLinker.Generic getNewGenericLinker() {
      return new FakeEntityLinker.Generic();
   }

   public static FakeEntityLinker.Location getNewLocationLinker() {
      return new FakeEntityLinker.Location();
   }

   public static FakeEntityLinker.Metadata getNewMetadataLinker() {
      return new FakeEntityLinker.Metadata();
   }

   private FakeEntityLinker() {
   }

   public abstract void link(FakeEntity<?, ?> entry);

   public abstract void unlink(FakeEntity<?, ?> entry);

   public synchronized void begin() {
      this.active = true;
   }

   public synchronized void complete() {
      this.active = false;
      Iterator var1 = this.toFlush.iterator();

      while(var1.hasNext()) {
         ChannelPipeline var2 = (ChannelPipeline)var1.next();
         var2.flush();
      }

      this.toFlush.clear();
   }

   protected synchronized void toFlush(@NotNull Collection<ChannelPipeline> pipelines) {
      if (this.active) {
         this.toFlush.addAll(var1);
      }

   }

   public String toString() {
      String var10000 = String.valueOf(this.toFlush);
      return "FakeEntityLinker(toFlush=" + var10000 + ", active=" + this.active + ")";
   }

   public static class Generic extends FakeEntityLinker {
      private Generic() {
      }

      public synchronized void track(@NotNull Collection<ChannelPipeline> pipelines) {
         if (this.active) {
            this.toFlush.addAll(var1);
         }

      }

      public synchronized void track(@NotNull ChannelPipeline pipeline) {
         if (this.active) {
            this.toFlush.add(var1);
         }

      }

      public void link(FakeEntity<?, ?> entry) {
      }

      public void unlink(FakeEntity<?, ?> entry) {
      }
   }

   public static class Location extends FakeEntityLinker {
      private Location() {
      }

      public void link(FakeEntity<?, ?> entry) {
         if (var1.locationLinker != null && var1.locationLinker != this) {
            var1.locationLinker.unlink(var1);
         }

         var1.locationLinker = this;
      }

      public void unlink(FakeEntity<?, ?> entry) {
         if (var1.locationLinker == this) {
            var1.locationLinker = null;
         }

      }

      public String toString() {
         return "FakeEntityLinker.Location()";
      }
   }

   public static class Metadata extends FakeEntityLinker {
      private Metadata() {
      }

      public void link(FakeEntity<?, ?> entry) {
         if (var1.metadataLinker != null && var1.metadataLinker != this) {
            var1.metadataLinker.unlink(var1);
         }

         var1.metadataLinker = this;
      }

      public void unlink(FakeEntity<?, ?> entry) {
         if (var1.metadataLinker == this) {
            var1.metadataLinker = null;
         }

      }
   }
}
