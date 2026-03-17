package com.bergerkiller.bukkit.tc.controller.components;

import com.bergerkiller.bukkit.tc.attachments.animation.Animation;
import com.bergerkiller.bukkit.tc.attachments.animation.AnimationOptions;
import java.util.Collection;
import java.util.List;

public interface AnimationController {
   List<String> getAnimationNames();

   Collection<String> getAnimationScenes(String var1);

   boolean playNamedAnimationFor(int[] var1, AnimationOptions var2);

   boolean playAnimationFor(int[] var1, Animation var2);

   default boolean playNamedAnimation(String name) {
      return this.playNamedAnimation(new AnimationOptions(name));
   }

   boolean playNamedAnimation(AnimationOptions var1);
}
