package emanondev.itemtag.activity;

import emanondev.itemtag.ItemTag;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TriggerManager {
   private static final HashMap<String, TriggerType> triggerTypes = new HashMap();

   public static void registerTriggerType(@NotNull TriggerType type) {
      if (triggerTypes.containsKey(type.getId())) {
         throw new IllegalArgumentException();
      } else {
         triggerTypes.put(type.getId(), type);
         ItemTag.get().log("TriggerManager registered Trigger Type &e" + type.getId());
      }
   }

   public static TriggerType getTriggerType(@Nullable String id) {
      return id == null ? null : (TriggerType)triggerTypes.get(id);
   }

   public static Collection<TriggerType> getTriggerTypes() {
      return Collections.unmodifiableCollection(triggerTypes.values());
   }

   public static Collection<String> getTriggerTypeIds() {
      return Collections.unmodifiableSet(triggerTypes.keySet());
   }

   public static void load() {
      ItemTag.get().registerListener(new TriggerListener());
      registerTriggerType(TriggerListener.CONSUME_ITEM);
      registerTriggerType(TriggerListener.RIGHT_INTERACT);
      registerTriggerType(TriggerListener.LEFT_INTERACT);
      registerTriggerType(TriggerListener.MELEE_HIT);
      registerTriggerType(TriggerListener.RANGED_HIT);
      registerTriggerType(TriggerListener.HITTED);
   }
}
