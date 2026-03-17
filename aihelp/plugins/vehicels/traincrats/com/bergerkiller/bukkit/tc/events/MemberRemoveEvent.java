package com.bergerkiller.bukkit.tc.events;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import org.bukkit.event.HandlerList;

public class MemberRemoveEvent extends MemberEvent {
   private static final HandlerList handlers = new HandlerList();

   public MemberRemoveEvent(MinecartMember<?> member) {
      super(member);
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public static void call(MinecartMember<?> member) {
      CommonUtil.callEvent(new MemberRemoveEvent(member));
   }

   public HandlerList getHandlers() {
      return handlers;
   }
}
