package com.bergerkiller.bukkit.tc.attachments.ui;

import com.bergerkiller.bukkit.tc.attachments.animation.AnimationNode;
import java.util.List;

public interface AnimationFramesImportExport {
   String getAnimationName();

   List<AnimationNode> exportAnimationFrames();

   void importAnimationFrames(List<AnimationNode> var1, boolean var2);
}
