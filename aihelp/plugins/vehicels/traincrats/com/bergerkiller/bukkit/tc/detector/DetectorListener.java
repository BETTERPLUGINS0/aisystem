package com.bergerkiller.bukkit.tc.detector;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;

public interface DetectorListener {
   void onRegister(DetectorRegion var1);

   void onUnregister(DetectorRegion var1);

   void onLeave(MinecartMember<?> var1);

   void onEnter(MinecartMember<?> var1);

   void onLeave(MinecartGroup var1);

   void onEnter(MinecartGroup var1);

   void onUnload(MinecartGroup var1);

   void onUpdate(MinecartMember<?> var1);

   void onUpdate(MinecartGroup var1);
}
