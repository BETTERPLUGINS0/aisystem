package com.bergerkiller.bukkit.tc.actions.registry;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.StreamUtil;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.actions.Action;
import com.bergerkiller.bukkit.tc.actions.GroupActionSizzle;
import com.bergerkiller.bukkit.tc.actions.GroupActionWaitDelay;
import com.bergerkiller.bukkit.tc.actions.GroupActionWaitForever;
import com.bergerkiller.bukkit.tc.actions.GroupActionWaitStationRouting;
import com.bergerkiller.bukkit.tc.actions.GroupActionWaitTicks;
import com.bergerkiller.bukkit.tc.actions.GroupActionWaitTill;
import com.bergerkiller.bukkit.tc.actions.MemberAction;
import com.bergerkiller.bukkit.tc.actions.MemberActionLaunch;
import com.bergerkiller.bukkit.tc.actions.MemberActionLaunchDirection;
import com.bergerkiller.bukkit.tc.actions.MemberActionLaunchLocation;
import com.bergerkiller.bukkit.tc.actions.MemberActionWaitDistance;
import com.bergerkiller.bukkit.tc.actions.MemberActionWaitLocation;
import com.bergerkiller.bukkit.tc.actions.TrackedSignActionSetOutput;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.ActionTracker;
import com.bergerkiller.bukkit.tc.controller.components.ActionTrackerMember;
import com.bergerkiller.bukkit.tc.offline.train.format.OfflineDataBlock;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.logging.Level;

public class ActionRegistry {
   private final TrainCarts plugin;
   private final Map<String, ActionRegistry.RegisteredAction> byId = new HashMap();
   private final WeakHashMap<Class<?>, ActionRegistry.RegisteredAction> byType = new WeakHashMap();

   public ActionRegistry(TrainCarts plugin) {
      this.plugin = plugin;
      this.registerTrainCartsActions();
   }

   private void registerTrainCartsActions() {
      this.register(MemberActionLaunch.class, new MemberActionLaunch.Serializer());
      this.register(MemberActionLaunchDirection.class, new MemberActionLaunchDirection.Serializer());
      this.register(MemberActionLaunchLocation.class, new MemberActionLaunchLocation.Serializer());
      this.register(MemberActionWaitDistance.class, new MemberActionWaitDistance.Serializer());
      this.register(MemberActionWaitLocation.class, new MemberActionWaitLocation.Serializer());
      this.register(GroupActionWaitForever.class, new GroupActionWaitForever.Serializer());
      this.register(GroupActionWaitTill.class, new GroupActionWaitTill.Serializer());
      this.register(GroupActionWaitTicks.class, new GroupActionWaitTicks.Serializer());
      this.register(GroupActionWaitDelay.class, new GroupActionWaitDelay.Serializer());
      this.register(TrackedSignActionSetOutput.class, new TrackedSignActionSetOutput.Serializer(this.plugin));
      this.register(GroupActionSizzle.class, new GroupActionSizzle.Serializer());
      this.register(GroupActionWaitStationRouting.class, new GroupActionWaitStationRouting.Serializer(this.plugin));
   }

   public <T extends Action> void register(Class<T> type, ActionRegistry.Serializer<T> serializer) {
      this.register(type.getName(), type, serializer);
   }

   public <T extends Action> void register(String id, Class<T> type, ActionRegistry.Serializer<T> serializer) {
      ActionRegistry.RegisteredAction registered = new ActionRegistry.RegisteredAction(id, type, serializer);
      this.byId.put(id, registered);
      this.byType.put(type, registered);
   }

   public void unregister(String id) {
      this.byId.remove(id);
   }

   public List<OfflineDataBlock> saveTracker(ActionTracker tracker) {
      if (!tracker.hasAction()) {
         return Collections.emptyList();
      } else {
         OfflineDataBlock root = OfflineDataBlock.create("root");
         Iterator var3 = tracker.getScheduledActions().iterator();

         while(var3.hasNext()) {
            Action action = (Action)var3.next();
            this.saveAction(root, action, tracker);
         }

         return Collections.unmodifiableList(root.children);
      }
   }

   public void loadTracker(ActionTracker tracker, List<OfflineDataBlock> actionDataBlocks) {
      if (!actionDataBlocks.isEmpty()) {
         MinecartGroup group = tracker.getGroupOwner();
         Iterator var4 = actionDataBlocks.iterator();

         while(var4.hasNext()) {
            OfflineDataBlock actionDataBlock = (OfflineDataBlock)var4.next();
            Action action = this.loadAction(group, actionDataBlock, tracker);
            if (action != null) {
               tracker.addAction(action);
            }
         }
      }

   }

   public OfflineDataBlock saveAction(OfflineDataBlock root, Action action, ActionTracker tracker) {
      ActionRegistry.RegisteredAction registeredAction = (ActionRegistry.RegisteredAction)this.byType.get(action.getClass());
      if (registeredAction == null) {
         return null;
      } else {
         boolean addedToMember = tracker instanceof ActionTrackerMember;

         OfflineDataBlock child;
         try {
            child = root.addChild("action", (stream) -> {
               stream.writeUTF(registeredAction.id);
               int elapsedTicks = action.elapsedTicks();
               stream.writeInt(elapsedTicks);
               if (elapsedTicks > 0) {
                  stream.writeLong(action.elapsedTimeMillis());
               }

               Set<String> tags = action.getTags();
               Util.writeVariableLengthInt(stream, tags.size());
               Iterator var6 = tags.iterator();

               while(var6.hasNext()) {
                  String tag = (String)var6.next();
                  stream.writeUTF(tag);
               }

               MinecartMember member;
               if (!addedToMember && action instanceof MemberAction && (member = ((MemberAction)action).getMember()) != null) {
                  stream.writeBoolean(true);
                  StreamUtil.writeUUID(stream, ((CommonMinecart)member.getEntity()).getUniqueId());
               } else {
                  stream.writeBoolean(false);
               }

            });
         } catch (Throwable var10) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to save action " + action.getClass().getName(), var10);
            return null;
         }

         boolean success = false;

         try {
            success = registeredAction.serializer.save(action, child, tracker);
         } catch (Throwable var9) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to save action " + action.getClass().getName(), var9);
         }

         if (success) {
            return child;
         } else {
            root.children.remove(child);
            return null;
         }
      }
   }

   public Action loadAction(MinecartGroup group, OfflineDataBlock dataBlock, ActionTracker tracker) {
      ActionRegistry.RegisteredAction registeredAction = null;

      try {
         DataInputStream stream = dataBlock.readData();

         Object var19;
         label137: {
            Iterator var14;
            label138: {
               MinecartMember memberFound;
               label139: {
                  Action var23;
                  try {
                     registeredAction = (ActionRegistry.RegisteredAction)this.byId.get(stream.readUTF());
                     if (registeredAction == null) {
                        var19 = null;
                        break label137;
                     }

                     int elapsedTicks = stream.readInt();
                     long elapsedTimeMillis = elapsedTicks > 0 ? stream.readLong() : 0L;
                     int numTags = Util.readVariableLengthInt(stream);
                     Object tags;
                     if (numTags > 0) {
                        tags = new ArrayList(numTags);

                        for(int i = 0; i < numTags; ++i) {
                           ((List)tags).add(stream.readUTF());
                        }
                     } else {
                        tags = Collections.emptyList();
                     }

                     MinecartMember member;
                     if (stream.readBoolean()) {
                        UUID memberUUID = StreamUtil.readUUID(stream);
                        memberFound = null;
                        var14 = group.iterator();

                        while(var14.hasNext()) {
                           MinecartMember<?> groupMember = (MinecartMember)var14.next();
                           if (((CommonMinecart)groupMember.getEntity()).getUniqueId().equals(memberUUID)) {
                              memberFound = groupMember;
                              break;
                           }
                        }

                        if (memberFound == null) {
                           var14 = null;
                           break label138;
                        }

                        member = memberFound;
                     } else {
                        member = null;
                     }

                     Action action = registeredAction.serializer.load(dataBlock, tracker);
                     if (action == null) {
                        memberFound = null;
                        break label139;
                     }

                     Action.loadElapsedTime(action, elapsedTicks, elapsedTimeMillis);
                     Iterator var22 = ((List)tags).iterator();

                     while(var22.hasNext()) {
                        String tag = (String)var22.next();
                        action.addTag(tag);
                     }

                     if (member != null && action instanceof MemberAction) {
                        ((MemberAction)action).setMember(member);
                     }

                     var23 = action;
                  } catch (Throwable var17) {
                     if (stream != null) {
                        try {
                           stream.close();
                        } catch (Throwable var16) {
                           var17.addSuppressed(var16);
                        }
                     }

                     throw var17;
                  }

                  if (stream != null) {
                     stream.close();
                  }

                  return var23;
               }

               if (stream != null) {
                  stream.close();
               }

               return memberFound;
            }

            if (stream != null) {
               stream.close();
            }

            return var14;
         }

         if (stream != null) {
            stream.close();
         }

         return (Action)var19;
      } catch (Throwable var18) {
         if (registeredAction != null) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to load action " + registeredAction.type.getName(), var18);
         } else {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to load corrupted action", var18);
         }

         return null;
      }
   }

   public interface Serializer<T extends Action> {
      boolean save(T var1, OfflineDataBlock var2, ActionTracker var3) throws IOException;

      T load(OfflineDataBlock var1, ActionTracker var2) throws IOException;
   }

   private static final class RegisteredAction {
      public final String id;
      public final Class<?> type;
      public final ActionRegistry.Serializer<Action> serializer;

      public <T extends Action> RegisteredAction(String id, Class<T> type, ActionRegistry.Serializer<T> serializer) {
         this.id = id;
         this.type = type;
         this.serializer = serializer;
      }
   }
}
