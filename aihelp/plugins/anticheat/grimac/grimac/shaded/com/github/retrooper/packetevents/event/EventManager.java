package ac.grim.grimac.shaded.com.github.retrooper.packetevents.event;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.exception.InvalidHandshakeException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;

public class EventManager {
   private final Map<PacketListenerPriority, Set<PacketListenerCommon>> listenersMap = new ConcurrentHashMap();
   private volatile PacketListenerCommon[] listeners = new PacketListenerCommon[0];

   public void callEvent(PacketEvent event) {
      this.callEvent(event, (Runnable)null);
   }

   public void callEvent(PacketEvent event, @Nullable Runnable postCallListenerAction, boolean preVia) {
      PacketListenerCommon[] var4 = this.listeners;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         PacketListenerCommon listener = var4[var6];

         try {
            if (listener.isPreVia() == preVia) {
               event.call(listener);
            }
         } catch (Exception var9) {
            if (var9.getClass() != InvalidHandshakeException.class && (var9.getCause() == null || var9.getCause().getClass() != InvalidHandshakeException.class)) {
               PacketEvents.getAPI().getLogger().log(Level.WARNING, "PacketEvents caught an unhandled exception while calling your listener.", var9);
            }
         }

         if (postCallListenerAction != null) {
            postCallListenerAction.run();
         }
      }

      if (event instanceof ProtocolPacketEvent && !((ProtocolPacketEvent)event).needsReEncode()) {
         ((ProtocolPacketEvent)event).setLastUsedWrapper((PacketWrapper)null);
      }

   }

   public void callEvent(PacketEvent event, @Nullable Runnable postCallListenerAction) {
      this.callEvent(event, postCallListenerAction, false);
   }

   public PacketListenerCommon registerListener(PacketListener listener, PacketListenerPriority priority) {
      PacketListenerCommon packetListenerAbstract = listener.asAbstract(priority);
      return this.registerListener(packetListenerAbstract);
   }

   public PacketListenerCommon registerListener(PacketListenerCommon listener) {
      this.registerListenerNoRecalculation(listener);
      this.recalculateListeners();
      return listener;
   }

   public PacketListenerCommon[] registerListeners(PacketListenerCommon... listeners) {
      PacketListenerCommon[] var2 = listeners;
      int var3 = listeners.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PacketListenerCommon listener = var2[var4];
         this.registerListenerNoRecalculation(listener);
      }

      this.recalculateListeners();
      return listeners;
   }

   public void unregisterListener(PacketListenerCommon listener) {
      if (this.unregisterListenerNoRecalculation(listener)) {
         this.recalculateListeners();
      }

   }

   public void unregisterListeners(PacketListenerCommon... listeners) {
      boolean modified = false;
      PacketListenerCommon[] var3 = listeners;
      int var4 = listeners.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         PacketListenerCommon listener = var3[var5];
         modified |= this.unregisterListenerNoRecalculation(listener);
      }

      if (modified) {
         this.recalculateListeners();
      }

   }

   public void unregisterAllListeners() {
      this.listenersMap.clear();
      synchronized(this) {
         this.listeners = new PacketListenerCommon[0];
      }
   }

   private void recalculateListeners() {
      synchronized(this) {
         List<PacketListenerCommon> list = new ArrayList();
         PacketListenerPriority[] var3 = PacketListenerPriority.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            PacketListenerPriority priority = var3[var5];
            Set<PacketListenerCommon> set = (Set)this.listenersMap.get(priority);
            if (set != null) {
               list.addAll(set);
            }
         }

         this.listeners = (PacketListenerCommon[])list.toArray(new PacketListenerCommon[0]);
      }
   }

   private void registerListenerNoRecalculation(PacketListenerCommon listener) {
      Set<PacketListenerCommon> listenerSet = (Set)this.listenersMap.computeIfAbsent(listener.getPriority(), (p) -> {
         return new CopyOnWriteArraySet();
      });
      listenerSet.add(listener);
   }

   private boolean unregisterListenerNoRecalculation(PacketListenerCommon listener) {
      Set<PacketListenerCommon> listenerSet = (Set)this.listenersMap.get(listener.getPriority());
      return listenerSet != null && listenerSet.remove(listener);
   }
}
