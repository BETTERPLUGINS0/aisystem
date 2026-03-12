package ac.grim.grimac.manager;

import ac.grim.grimac.manager.tick.Tickable;
import ac.grim.grimac.manager.tick.impl.ClearRecentlyUpdatedBlocks;
import ac.grim.grimac.manager.tick.impl.ClientVersionSetter;
import ac.grim.grimac.manager.tick.impl.ResetTick;
import ac.grim.grimac.manager.tick.impl.TickInventory;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap.Builder;

public class TickManager {
   public int currentTick;
   private final ClassToInstanceMap<Tickable> syncTick = (new Builder()).put(ResetTick.class, new ResetTick()).build();
   private final ClassToInstanceMap<Tickable> asyncTick = (new Builder()).put(ClientVersionSetter.class, new ClientVersionSetter()).put(TickInventory.class, new TickInventory()).put(ClearRecentlyUpdatedBlocks.class, new ClearRecentlyUpdatedBlocks()).build();

   public void tickSync() {
      ++this.currentTick;
      this.syncTick.values().forEach(Tickable::tick);
   }

   public void tickAsync() {
      this.asyncTick.values().forEach(Tickable::tick);
   }
}
