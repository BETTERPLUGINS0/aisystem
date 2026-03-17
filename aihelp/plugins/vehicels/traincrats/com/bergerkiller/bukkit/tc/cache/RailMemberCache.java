package com.bergerkiller.bukkit.tc.cache;

import com.bergerkiller.bukkit.common.offline.OfflineBlock;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import java.util.Collection;
import org.bukkit.block.Block;

/** @deprecated */
@Deprecated
public class RailMemberCache {
   public static MinecartMember<?> find(OfflineBlock railBlock) {
      Collection<MinecartMember<?>> members = RailLookup.findMembersOnRail(railBlock);
      return members.isEmpty() ? null : (MinecartMember)members.iterator().next();
   }

   /** @deprecated */
   @Deprecated
   public static MinecartMember<?> find(Block railBlock) {
      return find(OfflineBlock.of(railBlock));
   }

   public static Collection<MinecartMember<?>> findAll(OfflineBlock railBlock) {
      return RailLookup.findMembersOnRail(railBlock);
   }

   /** @deprecated */
   @Deprecated
   public static Collection<MinecartMember<?>> findAll(Block railBlock) {
      return findAll(OfflineBlock.of(railBlock));
   }

   public static void remove(MinecartMember<?> member) {
      RailLookup.removeMemberFromAll(member);
   }
}
