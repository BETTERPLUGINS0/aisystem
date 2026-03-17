package com.bergerkiller.bukkit.tc.attachments.helper;

import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.attachments.animation.Animation;
import com.bergerkiller.bukkit.tc.attachments.animation.AnimationOptions;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelperMethods {
   private static final ChatColor[] GLOW_COLORS;
   private static final AttachmentUpdateTransformHelper updateTransformHelper;

   /** @deprecated */
   @Deprecated
   public static void updatePositions(Attachment startAttachment, Matrix4x4 transform) {
      updateTransformHelper.startAndFinish(startAttachment, transform);
   }

   public static void makeHiddenRecursive(Attachment root, boolean active, AttachmentViewer viewer) {
      active &= root.isActive();
      Iterator var3 = root.getChildren().iterator();

      while(var3.hasNext()) {
         Attachment child = (Attachment)var3.next();
         makeHiddenRecursive(child, active, viewer);
      }

      if (active || !root.isHiddenWhenInactive()) {
         root.makeHidden(viewer);
      }

   }

   public static void makeVisibleRecursive(Attachment root, boolean active, AttachmentViewer viewer) {
      active &= root.isActive();
      if (active || !root.isHiddenWhenInactive()) {
         root.makeVisible(viewer);
      }

      Iterator var3 = root.getChildren().iterator();

      while(var3.hasNext()) {
         Attachment child = (Attachment)var3.next();
         makeVisibleRecursive(child, active, viewer);
      }

   }

   public static boolean hasInactiveParent(Attachment attachment) {
      for(Attachment parent = attachment.getParent(); parent != null; parent = parent.getParent()) {
         if (!parent.isActive()) {
            return true;
         }
      }

      return false;
   }

   public static void updateActiveRecursive(Attachment attachment, boolean active, Collection<Player> viewers) {
      attachment.onActiveChanged(active);
      Iterator var3;
      if (attachment.isHiddenWhenInactive()) {
         Player viewer;
         if (active) {
            var3 = viewers.iterator();

            while(var3.hasNext()) {
               viewer = (Player)var3.next();
               attachment.makeVisible(viewer);
            }
         } else {
            var3 = viewers.iterator();

            while(var3.hasNext()) {
               viewer = (Player)var3.next();
               attachment.makeHidden(viewer);
            }
         }

         attachment.getInternalState().last_transform = null;
      }

      var3 = attachment.getChildren().iterator();

      while(var3.hasNext()) {
         Attachment child = (Attachment)var3.next();
         if (child.isActive()) {
            updateActiveRecursive(child, active, viewers);
         }
      }

   }

   public static void perform_onTick(Attachment attachment) {
      attachment.onTick();
      Iterator var1 = attachment.getChildren().iterator();

      while(var1.hasNext()) {
         Attachment child = (Attachment)var1.next();
         perform_onTick(child);
      }

   }

   public static void perform_onMove(Attachment attachment, boolean absolute) {
      attachment.onMove(absolute);
      Iterator var2 = attachment.getChildren().iterator();

      while(var2.hasNext()) {
         Attachment child = (Attachment)var2.next();
         perform_onMove(child, absolute);
      }

   }

   public static void perform_onAttached(Attachment attachment) {
      perform_onAttached_single(attachment);
      Iterator var1 = attachment.getChildren().iterator();

      while(var1.hasNext()) {
         Attachment child = (Attachment)var1.next();
         perform_onAttached(child);
      }

   }

   public static void perform_onAttached_single(Attachment attachment) {
      attachment.getInternalState().attached = true;
      attachment.onAttached();
      attachment.onLoad(attachment.getConfig());
      if (attachment.isFocused()) {
         attachment.onFocus();
      }

   }

   public static void perform_onDetached(Attachment attachment) {
      Iterator var1 = attachment.getChildren().iterator();

      while(var1.hasNext()) {
         Attachment child = (Attachment)var1.next();
         perform_onDetached(child);
      }

      perform_onDetached_single(attachment);
   }

   public static void perform_onDetached_single(Attachment attachment) {
      attachment.onDetached();
      attachment.getInternalState().attached = false;
      attachment.getInternalState().reset();
   }

   public static Attachment findAttachmentWithEntityId(Attachment root, int entityId) {
      if (root.containsEntityId(entityId)) {
         return root;
      } else {
         Iterator var2 = root.getChildren().iterator();

         Attachment att;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            Attachment child = (Attachment)var2.next();
            att = findAttachmentWithEntityId(child, entityId);
         } while(att == null);

         return att;
      }
   }

   public static boolean playAnimationRecursive(Attachment attachment, AnimationOptions options) {
      if (playStoredAnimationRecursive(attachment, options)) {
         return true;
      } else {
         Animation defaultAnimation = (Animation)TCConfig.defaultAnimations.get(options.getName());
         if (defaultAnimation != null) {
            attachment.startAnimation(defaultAnimation.clone().applyOptions(options));
            return true;
         } else {
            return false;
         }
      }
   }

   public static boolean playAnimation(Attachment attachment, AnimationOptions options) {
      if (playStoredAnimation(attachment, options)) {
         return true;
      } else {
         Animation defaultAnimation = (Animation)TCConfig.defaultAnimations.get(options.getName());
         if (defaultAnimation != null) {
            attachment.startAnimation(defaultAnimation.clone().applyOptions(options));
            return true;
         } else {
            return false;
         }
      }
   }

   public static void setFocusedRecursive(Attachment attachment, boolean focused) {
      attachment.setFocused(focused);
      Iterator var2 = attachment.getChildren().iterator();

      while(var2.hasNext()) {
         Attachment child = (Attachment)var2.next();
         setFocusedRecursive(child, focused);
      }

   }

   public static ChatColor getFocusGlowColor(Attachment attachment) {
      while(true) {
         Attachment parent = attachment.getParent();
         if (parent == null) {
            return ChatColor.WHITE;
         }

         if (!parent.isFocused()) {
            return GLOW_COLORS[parent.getChildren().indexOf(attachment) & 15];
         }

         attachment = parent;
      }
   }

   public static void addAnimationNamesToSetRecursive(Set<String> out_names, Attachment attachment) {
      out_names.addAll(attachment.getAnimationNames());
      Iterator var2 = attachment.getChildren().iterator();

      while(var2.hasNext()) {
         Attachment child = (Attachment)var2.next();
         addAnimationNamesToSetRecursive(out_names, child);
      }

   }

   public static void addAnimationScenesToSetRecursive(Set<String> out_names, String animationName, Attachment attachment) {
      out_names.addAll(attachment.getAnimationScenes(animationName));
      Iterator var3 = attachment.getChildren().iterator();

      while(var3.hasNext()) {
         Attachment child = (Attachment)var3.next();
         addAnimationScenesToSetRecursive(out_names, animationName, child);
      }

   }

   private static boolean playStoredAnimation(Attachment attachment, AnimationOptions options) {
      Animation anim = (Animation)attachment.getInternalState().animations.get(options.getName());
      if (anim != null) {
         attachment.startAnimation(anim.clone().applyOptions(options));
         return true;
      } else {
         return false;
      }
   }

   private static boolean playStoredAnimationRecursive(Attachment attachment, AnimationOptions options) {
      boolean found = playStoredAnimation(attachment, options);

      Attachment child;
      for(Iterator var3 = attachment.getChildren().iterator(); var3.hasNext(); found |= playStoredAnimationRecursive(child, options)) {
         child = (Attachment)var3.next();
      }

      return found;
   }

   public static List<Attachment> listAllAttachments(Attachment root) {
      if (root == null) {
         return Collections.emptyList();
      } else {
         List<Attachment> result = new ArrayList(16);
         addAttachments(root, result);
         return Collections.unmodifiableList(result);
      }
   }

   private static void addAttachments(Attachment attachment, List<Attachment> dest) {
      dest.add(attachment);
      Iterator var2 = attachment.getChildren().iterator();

      while(var2.hasNext()) {
         Attachment child = (Attachment)var2.next();
         addAttachments(child, dest);
      }

   }

   static {
      GLOW_COLORS = new ChatColor[]{ChatColor.DARK_RED, ChatColor.DARK_GREEN, ChatColor.DARK_BLUE, ChatColor.DARK_AQUA, ChatColor.DARK_PURPLE, ChatColor.YELLOW, ChatColor.RED, ChatColor.GREEN, ChatColor.BLUE, ChatColor.AQUA, ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.BLACK, ChatColor.DARK_GRAY, ChatColor.GRAY, ChatColor.WHITE};
      updateTransformHelper = AttachmentUpdateTransformHelper.createSimple();
   }
}
