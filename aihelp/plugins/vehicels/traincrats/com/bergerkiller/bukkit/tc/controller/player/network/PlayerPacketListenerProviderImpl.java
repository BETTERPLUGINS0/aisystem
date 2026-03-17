package com.bergerkiller.bukkit.tc.controller.player.network;

import com.bergerkiller.bukkit.common.RunOnceTask;
import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.events.PacketReceiveEvent;
import com.bergerkiller.bukkit.common.events.PacketSendEvent;
import com.bergerkiller.bukkit.common.protocol.PacketListener;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

final class PlayerPacketListenerProviderImpl implements PlayerPacketListener.Provider {
   private static final int CLEANUP_INTERVAL = 200;
   private final Map<Set<PacketType>, PlayerPacketListenerProviderImpl.TypeSetListener> activeTypeSetListeners = new HashMap();
   private final List<Player> recentlyQuitPlayers = new ArrayList();
   private final TrainCarts traincarts;
   private final Task cleanupTask;
   private boolean disabled = false;

   public PlayerPacketListenerProviderImpl(TrainCarts traincarts) {
      this.traincarts = traincarts;
      this.cleanupTask = new Task(traincarts) {
         public void run() {
            synchronized(PlayerPacketListenerProviderImpl.this) {
               PlayerPacketListenerProviderImpl.this.recentlyQuitPlayers.clear();
               PlayerPacketListenerProviderImpl.this.activeTypeSetListeners.values().removeIf(PlayerPacketListenerProviderImpl.TypeSetListener::isTerminated);
            }
         }
      };
   }

   public synchronized <L extends PacketListener> PlayerPacketListener<L> create(Player player, L packetListener, PacketType... packetTypes) {
      if (!this.disabled && player != null && (player.isValid() || Bukkit.getPlayer(player.getUniqueId()) == player)) {
         if (!this.recentlyQuitPlayers.isEmpty()) {
            Iterator var4 = this.recentlyQuitPlayers.iterator();

            while(var4.hasNext()) {
               Player p = (Player)var4.next();
               if (p == player) {
                  return PlayerPacketListener.createNoOp(player, packetListener);
               }
            }
         }

         Set<PacketType> packetTypesSet = new HashSet(Arrays.asList(packetTypes));
         PlayerPacketListenerProviderImpl.TypeSetListener typeSetListener = (PlayerPacketListenerProviderImpl.TypeSetListener)this.activeTypeSetListeners.compute(packetTypesSet, (packets, existing) -> {
            return existing != null && !existing.isTerminated() ? existing : new PlayerPacketListenerProviderImpl.TypeSetListener(this.traincarts, packets);
         });
         return typeSetListener.addListener(player, packetListener);
      } else {
         return PlayerPacketListener.createNoOp(player, packetListener);
      }
   }

   public synchronized void enable() {
      this.disabled = false;
      this.cleanupTask.start(200L, 200L);
      this.traincarts.register(new Listener() {
         @EventHandler(
            priority = EventPriority.MONITOR
         )
         public void onPlayerQuit(PlayerQuitEvent event) {
            PlayerPacketListenerProviderImpl.this.recentlyQuitPlayers.add(event.getPlayer());
            PlayerPacketListenerProviderImpl.this.activeTypeSetListeners.values().forEach((l) -> {
               l.onPlayerQuit(event.getPlayer());
            });
         }
      });
   }

   public synchronized void disable() {
      this.disabled = true;
      this.cleanupTask.stop();
   }

   private static class TypeSetListener implements PacketListener {
      private final TrainCarts traincarts;
      private final Multimap<Player, PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl<?>> packetListeners = Multimaps.newMultimap(new IdentityHashMap(), ArrayList::new);
      private Map<Player, List<PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl<?>>> packetListenersVisible = Collections.emptyMap();
      private final RunOnceTask checkTerminated;
      private boolean terminated;

      public TypeSetListener(TrainCarts traincarts, Set<PacketType> packetTypes) {
         this.traincarts = traincarts;
         this.checkTerminated = RunOnceTask.create(traincarts, this::tryTerminateIfEmpty);
         this.terminated = false;
         traincarts.register(this, (PacketType[])packetTypes.toArray(new PacketType[0]));
      }

      private synchronized void tryTerminateIfEmpty() {
         if (!this.terminated && this.packetListeners.isEmpty()) {
            this.terminate();
         }

      }

      public synchronized void terminate() {
         this.terminated = true;
         this.packetListeners.values().forEach(PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl::setStateTerminated);
         this.packetListeners.clear();
         this.packetListenersVisible = Collections.emptyMap();
         this.traincarts.unregister(this);
         this.checkTerminated.cancel();
      }

      public synchronized boolean isTerminated() {
         return this.terminated;
      }

      public synchronized void terminateListener(PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl<?> playerPacketListener) {
         if (!this.terminated) {
            Player player = playerPacketListener.getPlayer();
            Collection<PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl<?>> listenersForPlayer = this.packetListeners.get(player);
            if (listenersForPlayer.remove(playerPacketListener)) {
               this.updateVisiblePacketListeners(player, listenersForPlayer);
               if (this.packetListeners.isEmpty()) {
                  this.checkTerminated.restart(10L);
               }
            }

         }
      }

      public synchronized <L extends PacketListener> PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl<L> addListener(Player player, L packetListener) {
         PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl<L> ppl = new PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl(this, player, packetListener);
         Collection<PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl<?>> newListeners = this.packetListeners.get(player);
         newListeners.add(ppl);
         this.updateVisiblePacketListeners(player, newListeners);
         return ppl;
      }

      private void updateVisiblePacketListeners(Player player, Collection<PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl<?>> newValues) {
         Map<Player, List<PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl<?>>> map = new IdentityHashMap(this.packetListenersVisible);
         if (newValues.isEmpty()) {
            map.remove(player);
         } else {
            map.put(player, new ArrayList(newValues));
         }

         this.packetListenersVisible = map;
      }

      public synchronized void onPlayerQuit(Player player) {
         Collection<PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl<?>> listeners = this.packetListeners.removeAll(player);
         listeners.forEach(PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl::setStateTerminated);
         this.updateVisiblePacketListeners(player, Collections.emptyList());
         if (this.packetListeners.isEmpty()) {
            this.checkTerminated.restart(10L);
         }

      }

      private Iterable<PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl<?>> iterateListenersFor(Player player) {
         return (Iterable)this.packetListenersVisible.getOrDefault(player, Collections.emptyList());
      }

      public void onPacketReceive(PacketReceiveEvent event) {
         Iterator var2 = this.iterateListenersFor(event.getPlayer()).iterator();

         while(var2.hasNext()) {
            PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl<?> playerPacketListener = (PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl)var2.next();
            if (playerPacketListener.isEnabled()) {
               playerPacketListener.getListener().onPacketReceive(event);
            }
         }

      }

      public void onPacketSend(PacketSendEvent event) {
         Iterator var2 = this.iterateListenersFor(event.getPlayer()).iterator();

         while(var2.hasNext()) {
            PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl<?> playerPacketListener = (PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl)var2.next();
            if (playerPacketListener.isEnabled()) {
               playerPacketListener.getListener().onPacketSend(event);
            }
         }

      }
   }

   private static final class PlayerPacketListenerImpl<L extends PacketListener> implements PlayerPacketListener<L> {
      private final AtomicReference<PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl.State> state;
      private final PlayerPacketListenerProviderImpl.TypeSetListener typeSetListener;
      private final Player player;
      private final L packetListener;

      public PlayerPacketListenerImpl(PlayerPacketListenerProviderImpl.TypeSetListener typeSetListener, Player player, L packetListener) {
         this.state = new AtomicReference(PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl.State.DISABLED);
         this.typeSetListener = typeSetListener;
         this.player = player;
         this.packetListener = packetListener;
      }

      public Player getPlayer() {
         return this.player;
      }

      public L getListener() {
         return this.packetListener;
      }

      public boolean isEnabled() {
         return this.state.get() == PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl.State.ENABLED;
      }

      public PlayerPacketListener<L> enable() {
         this.state.compareAndSet(PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl.State.DISABLED, PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl.State.ENABLED);
         return this;
      }

      public PlayerPacketListener<L> disable() {
         this.state.compareAndSet(PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl.State.ENABLED, PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl.State.DISABLED);
         return this;
      }

      public void setStateTerminated() {
         this.state.set(PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl.State.TERMINATED);
      }

      public void terminate() {
         if (this.state.getAndSet(PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl.State.TERMINATED) != PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl.State.TERMINATED) {
            this.typeSetListener.terminateListener(this);
         }

      }

      private static enum State {
         DISABLED,
         ENABLED,
         TERMINATED;

         // $FF: synthetic method
         private static PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl.State[] $values() {
            return new PlayerPacketListenerProviderImpl.PlayerPacketListenerImpl.State[]{DISABLED, ENABLED, TERMINATED};
         }
      }
   }
}
