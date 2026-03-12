package emanondev.itemtag.activity;

import emanondev.itemtag.ItemTag;
import emanondev.itemtag.activity.condition.AirLevelConditionType;
import emanondev.itemtag.activity.condition.EnvironmentConditionType;
import emanondev.itemtag.activity.condition.FoodLevelConditionType;
import emanondev.itemtag.activity.condition.HasUsesConditionType;
import emanondev.itemtag.activity.condition.HealthConditionType;
import emanondev.itemtag.activity.condition.IsDawnConditionType;
import emanondev.itemtag.activity.condition.IsDayConditionType;
import emanondev.itemtag.activity.condition.IsDuskConditionType;
import emanondev.itemtag.activity.condition.IsFlyingConditionType;
import emanondev.itemtag.activity.condition.IsFrozenConditionType;
import emanondev.itemtag.activity.condition.IsFullChargedConditionType;
import emanondev.itemtag.activity.condition.IsInWaterConditionType;
import emanondev.itemtag.activity.condition.IsInvulnerableConditionType;
import emanondev.itemtag.activity.condition.IsNightConditionType;
import emanondev.itemtag.activity.condition.IsOnFireConditionType;
import emanondev.itemtag.activity.condition.IsOnGroundConditionType;
import emanondev.itemtag.activity.condition.IsOutsideConditionType;
import emanondev.itemtag.activity.condition.IsPvpConditionType;
import emanondev.itemtag.activity.condition.IsRainingConditionType;
import emanondev.itemtag.activity.condition.IsSneakingConditionType;
import emanondev.itemtag.activity.condition.IsSprintingConditionType;
import emanondev.itemtag.activity.condition.IsSunnyConditionType;
import emanondev.itemtag.activity.condition.IsThunderingConditionType;
import emanondev.itemtag.activity.condition.LuckPermGroupConditionType;
import emanondev.itemtag.activity.condition.PermissionConditionType;
import emanondev.itemtag.activity.condition.TimeConditionType;
import emanondev.itemtag.activity.condition.WorldConditionType;
import emanondev.itemtag.activity.condition.XLocConditionType;
import emanondev.itemtag.activity.condition.YLocConditionType;
import emanondev.itemtag.activity.condition.ZLocConditionType;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConditionManager {
   private static final HashMap<String, ConditionType> conditionTypes = new HashMap();

   @Nullable
   public static ConditionType.Condition read(@NotNull String line) {
      int index = line.indexOf(" ");
      String id = (index == -1 ? line : line.substring(0, index)).toLowerCase(Locale.ENGLISH);
      String info = index == -1 ? "" : line.substring(index + 1);
      boolean reversed = id.startsWith("!");
      ConditionType type = getConditionType(id);
      return type == null ? null : type.read(info, reversed);
   }

   public static void register(@NotNull ConditionType condition) {
      String id = condition.getId();
      if (conditionTypes.containsKey(id)) {
         throw new IllegalArgumentException();
      } else {
         conditionTypes.put(id, condition);
         ItemTag.get().log("ConditionManager registered Condition Type &e" + condition.getId());
      }
   }

   public static void load() {
      register(new PermissionConditionType());
      register(new WorldConditionType());
      register(new LuckPermGroupConditionType());
      register(new HasUsesConditionType());
      register(new IsPvpConditionType());
      register(new AirLevelConditionType());
      register(new FoodLevelConditionType());
      register(new HealthConditionType());
      register(new IsSneakingConditionType());
      register(new IsSprintingConditionType());
      register(new XLocConditionType());
      register(new YLocConditionType());
      register(new ZLocConditionType());
      register(new TimeConditionType());
      register(new IsNightConditionType());
      register(new IsDayConditionType());
      register(new IsDawnConditionType());
      register(new IsDuskConditionType());
      register(new IsSunnyConditionType());
      register(new IsRainingConditionType());
      register(new IsThunderingConditionType());
      register(new EnvironmentConditionType());
      register(new IsOnGroundConditionType());
      register(new IsInWaterConditionType());
      register(new IsFlyingConditionType());
      register(new IsInvulnerableConditionType());
      register(new IsFrozenConditionType());
      register(new IsOnFireConditionType());
      register(new IsOutsideConditionType());
      register(new IsFullChargedConditionType());
   }

   @Nullable
   public static ConditionType getConditionType(@NotNull String id) {
      id = id.toLowerCase(Locale.ENGLISH);
      return (ConditionType)conditionTypes.get(id.startsWith("!") ? id.substring(1) : id);
   }

   public static Collection<String> getConditionTypeIds() {
      return Collections.unmodifiableSet(conditionTypes.keySet());
   }
}
