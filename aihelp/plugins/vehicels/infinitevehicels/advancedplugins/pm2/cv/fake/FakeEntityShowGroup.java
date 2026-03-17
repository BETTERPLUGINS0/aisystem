package advancedplugins.pm2.cv.fake;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.player.PlayerWrapper;
import advancedplugins.pm2.cv.fake.display.FakeDisplay;
import advancedplugins.pm2.cv.util.ConvertUtil;
import io.netty.channel.ChannelPipeline;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class FakeEntityShowGroup {
   private static final int DELIVERY_CAPACITY = 25;
   private final Set<FakeEntity<?, ?>> entries = ConcurrentHashMap.newKeySet();
   private final Map<UUID, FakeEntityShowGroup.Processor> processors = new ConcurrentHashMap();
   protected int hideFarAwayChunks = 5;
   protected long showHideDelay = 2L;
   @Nullable
   protected FakeEntityShowGroup.Listener listener;

   public FakeEntityShowGroup(@NotNull Collection<FakeEntity<?, ?>> entries) {
      this.entries.addAll(var1);

      FakeEntity var3;
      for(Iterator var2 = var1.iterator(); var2.hasNext(); var3.showGroup = this) {
         var3 = (FakeEntity)var2.next();
      }

   }

   public synchronized void show(@NotNull Player player) {
      FakeEntityShowGroup.Processor var2 = this.getProcessor(var1);
      var2.process(true, var1);
   }

   public synchronized void hide(@NotNull Player player) {
      FakeEntityShowGroup.Processor var2 = this.getProcessor(var1);
      var2.process(false, var1);
   }

   public void hideAll() {
      Iterator var1 = this.processors.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         Player var3 = Bukkit.getPlayer((UUID)var2.getKey());
         FakeEntityShowGroup.Processor var4 = (FakeEntityShowGroup.Processor)var2.getValue();
         if (var3 != null) {
            var4.process(false, var3);
         }
      }

      this.processors.clear();
   }

   @NotNull
   private FakeEntityShowGroup.Processor getProcessor(@NotNull Player player) {
      return (FakeEntityShowGroup.Processor)this.processors.computeIfAbsent(var1.getUniqueId(), (var1x) -> {
         return new FakeEntityShowGroup.Processor(this);
      });
   }

   public void resetProcessors(@NotNull Player player) {
      FakeEntityShowGroup.Processor var2 = this.getProcessor(var1);
      var2.processing = false;
      var2.completed = false;
   }

   public static FakeEntityShowGroup.FakeEntityShowGroupBuilder builder() {
      return new FakeEntityShowGroup.FakeEntityShowGroupBuilder();
   }

   public int getHideFarAwayChunks() {
      return this.hideFarAwayChunks;
   }

   public void setHideFarAwayChunks(final int hideFarAwayChunks) {
      this.hideFarAwayChunks = var1;
   }

   public long getShowHideDelay() {
      return this.showHideDelay;
   }

   public void setShowHideDelay(final long showHideDelay) {
      this.showHideDelay = var1;
   }

   @Nullable
   public FakeEntityShowGroup.Listener getListener() {
      return this.listener;
   }

   public void setListener(@Nullable final FakeEntityShowGroup.Listener listener) {
      this.listener = var1;
   }

   private static class Processor {
      private final FakeEntityShowGroup showGroup;
      private final Set<FakeEntityShowGroup.Delivery> deliveries = ConcurrentHashMap.newKeySet();
      private boolean mode;
      private boolean processing;
      private boolean completed;
      @Nullable
      private Player viewer;

      public Processor(@NotNull FakeEntityShowGroup showGroup) {
         this.showGroup = var1;
      }

      private synchronized void process(final boolean mode, @NotNull Player viewer) {
         if (this.mode != var1 || !this.processing && !this.completed) {
            this.mode = var1;
            this.viewer = var2;
            if (this.showGroup.listener != null) {
               if (var1) {
                  this.showGroup.listener.onBeginShowing(var2);
               } else {
                  this.showGroup.listener.onBeginHiding(var2);
               }
            }

            this.deliveries.forEach((var0) -> {
               var0.interrupted = true;
            });
            this.deliveries.clear();
            ArrayList var3 = new ArrayList();
            FakeEntity var5;
            if (this.processing && !this.completed) {
               Iterator var4 = this.showGroup.entries.iterator();

               while(var4.hasNext()) {
                  var5 = (FakeEntity)var4.next();
                  if (var1 != var5.handle.isShownTo(var2)) {
                     var3.add(var5);
                  }
               }
            } else {
               var3.addAll(this.showGroup.entries);
            }

            if (var3.size() == 0) {
               this.processing = false;
               this.completed = true;
            } else {
               this.processing = true;
               this.completed = false;
               this.sort(var3, var2);
               FakeEntity var12 = (FakeEntity)var3.get(0);
               var5 = (FakeEntity)var3.get(var3.size() - 1);
               int var6 = (int)Math.round(this.getLocation(var12).distance(this.getLocation(var5)));
               FakeEntityShowGroup.Delivery var7 = null;

               for(int var8 = 0; var8 < var3.size(); ++var8) {
                  FakeEntity var9 = (FakeEntity)var3.get(var8);
                  long var10 = (long)((int)Math.round((double)(var8 + 1) / (double)var3.size() * (double)var6)) * this.showGroup.showHideDelay;
                  if (var7 == null || var7.delay != var10) {
                     var7 = new FakeEntityShowGroup.Delivery(this, var2, var10);
                     this.deliveries.add(var7);
                  }

                  var7.value.add(var9);
                  if (var7.value.size() >= 25 || var8 == var3.size() - 1) {
                     var7 = null;
                  }
               }

               this.deliveries.forEach((var1x) -> {
                  var1x.deliver(this.mode);
               });
            }
         }
      }

      private void onDeliveryCompleted(FakeEntityShowGroup.Delivery delivery) {
         this.deliveries.remove(var1);
         if (this.showGroup.listener != null && this.viewer != null) {
            this.showGroup.listener.onEntriesShown(this.viewer, var1.value);
         }

         if (this.deliveries.size() == 0 && this.processing && !this.completed) {
            this.processing = false;
            this.completed = true;
            if (this.showGroup.listener != null && this.viewer != null) {
               if (this.mode) {
                  this.showGroup.listener.onCompleteShowing(this.viewer);
               } else {
                  this.showGroup.listener.onCompleteHiding(this.viewer);
               }
            }
         }

      }

      private void sort(List<FakeEntity<?, ?>> toProcess, @NotNull Player viewer) {
         Vector var3 = var2.getLocation().toVector();
         var1.sort((var2x, var3x) -> {
            double var4 = this.getLocation(var2x).distanceSquared(var3);
            double var6 = this.getLocation(var3x).distanceSquared(var3);
            int var8 = Double.compare(var4, var6);
            return this.mode ? var8 : -var8;
         });
      }

      private Vector getLocation(FakeEntity<?, ?> entity) {
         Location var2 = var1.getBukkitLocation();
         if (var1 instanceof FakeDisplay) {
            var2.add(ConvertUtil.toVector(((FakeDisplay)var1).getTransformation().getTranslation(new Vector3f())));
         }

         return var2.toVector();
      }
   }

   public static class FakeEntityShowGroupBuilder {
      private ArrayList<FakeEntity<?, ?>> entries;

      FakeEntityShowGroupBuilder() {
      }

      public FakeEntityShowGroup.FakeEntityShowGroupBuilder entry(final FakeEntity<?, ?> entry) {
         if (this.entries == null) {
            this.entries = new ArrayList();
         }

         this.entries.add(var1);
         return this;
      }

      public FakeEntityShowGroup.FakeEntityShowGroupBuilder entries(final Collection<? extends FakeEntity<?, ?>> entries) {
         if (var1 == null) {
            throw new NullPointerException("entries cannot be null");
         } else {
            if (this.entries == null) {
               this.entries = new ArrayList();
            }

            this.entries.addAll(var1);
            return this;
         }
      }

      public FakeEntityShowGroup.FakeEntityShowGroupBuilder clearEntries() {
         if (this.entries != null) {
            this.entries.clear();
         }

         return this;
      }

      public FakeEntityShowGroup build() {
         List var1;
         switch(this.entries == null ? 0 : this.entries.size()) {
         case 0:
            var1 = Collections.emptyList();
            break;
         case 1:
            var1 = Collections.singletonList((FakeEntity)this.entries.get(0));
            break;
         default:
            var1 = Collections.unmodifiableList(new ArrayList(this.entries));
         }

         return new FakeEntityShowGroup(var1);
      }

      public String toString() {
         return "FakeEntityShowGroup.FakeEntityShowGroupBuilder(entries=" + String.valueOf(this.entries) + ")";
      }
   }

   public interface Listener {
      default void onBeginShowing(@NotNull Player player) {
      }

      default void onEntriesShown(@NotNull Player player, Collection<FakeEntity<?, ?>> entries) {
      }

      default void onCompleteShowing(@NotNull Player player) {
      }

      default void onBeginHiding(@NotNull Player player) {
      }

      default void onCompleteHiding(@NotNull Player player) {
      }
   }

   private static class Delivery {
      @NotNull
      private final FakeEntityShowGroup.Processor processor;
      @NotNull
      private final Player destination;
      private final long delay;
      private final List<FakeEntity<?, ?>> value;
      private volatile boolean interrupted;

      public Delivery(@NotNull FakeEntityShowGroup.Processor processor, @NotNull Player destination, long delay) {
         this.processor = var1;
         this.destination = var2;
         this.delay = var3;
         this.value = new ArrayList();
      }

      private synchronized void deliver(final boolean mode) {
         this.deliver0(var1);
      }

      private synchronized void deliver0(final boolean mode) {
         if (!this.interrupted && this.destination.isOnline()) {
            PlayerWrapper var2 = InfiniteVehicles.getPlayerWrapperHandler().getWrapper(this.destination);
            ChannelPipeline var3 = var2.getPipeline();
            Iterator var4 = this.value.iterator();

            while(var4.hasNext()) {
               FakeEntity var5 = (FakeEntity)var4.next();
               if (var1) {
                  var5.handle.writeShowPackets(var2, true);
               } else {
                  var5.handle.writeHidePackets(var2, true);
               }
            }

            if (var3 != null) {
               var3.flush();
            }

            this.processor.onDeliveryCompleted(this);
         }
      }
   }
}
