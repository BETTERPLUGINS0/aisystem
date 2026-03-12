package emanondev.itemtag.activity;

import emanondev.itemedit.YMLConfig;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.activity.action.EmptyActionType;
import emanondev.itemtag.activity.condition.EmptyConditionType;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

public class Activity {
   private static final YMLConfig config;
   private final List<ActionType.Action> actions = new ArrayList();
   private final List<ActionType.Action> alternativeActions = new ArrayList();
   private final List<ActionType.Action> noConsumesActions = new ArrayList();
   private final List<ConditionType.Condition> conditions = new ArrayList();
   private final String id;
   private int consumes;

   public Activity(@NotNull String id) {
      if (!Pattern.compile("[a-z][_a-z0-9]*").matcher(id).matches()) {
         throw new IllegalArgumentException();
      } else {
         this.id = id;
         this.consumes = config.getInteger(this.getId() + ".consumes", 1);
         List<String> rawList = config.getStringList(this.getId() + ".conditions");
         Iterator var3 = rawList.iterator();

         String line;
         while(var3.hasNext()) {
            line = (String)var3.next();

            try {
               ConditionType.Condition cond = ConditionManager.read(line);
               if (cond == null) {
                  throw new NullPointerException();
               }

               this.conditions.add(cond);
            } catch (Exception var9) {
               this.conditions.add(EmptyConditionType.INST.read(line));
            }
         }

         rawList = config.getStringList(this.getId() + ".actions");
         var3 = rawList.iterator();

         while(var3.hasNext()) {
            line = (String)var3.next();

            try {
               this.actions.add(ActionManager.read(line));
            } catch (Exception var8) {
               this.actions.add(EmptyActionType.INST.read(line));
            }
         }

         rawList = config.getStringList(this.getId() + ".alternativeActions");
         var3 = rawList.iterator();

         while(var3.hasNext()) {
            line = (String)var3.next();

            try {
               this.alternativeActions.add(ActionManager.read(line));
            } catch (Exception var7) {
               this.alternativeActions.add(EmptyActionType.INST.read(line));
            }
         }

         rawList = config.getStringList(this.getId() + ".noConsumesActions");
         var3 = rawList.iterator();

         while(var3.hasNext()) {
            line = (String)var3.next();

            try {
               this.noConsumesActions.add(ActionManager.read(line));
            } catch (Exception var6) {
               this.noConsumesActions.add(EmptyActionType.INST.read(line));
            }
         }

      }
   }

   public int getConsumes() {
      return this.consumes;
   }

   public void setConsumes(int consumes) {
      this.consumes = Math.max(consumes, 0);
      this.save();
   }

   public List<ActionType.Action> getActions() {
      return Collections.unmodifiableList(this.actions);
   }

   public List<ActionType.Action> getAlternativeActions() {
      return Collections.unmodifiableList(this.alternativeActions);
   }

   public List<ActionType.Action> getNoConsumesActions() {
      return Collections.unmodifiableList(this.noConsumesActions);
   }

   public List<ConditionType.Condition> getConditions() {
      return Collections.unmodifiableList(this.conditions);
   }

   public void addCondition(@NotNull ConditionType.Condition cond) {
      this.conditions.add(cond);
      this.save();
   }

   public void addCondition(int place, @NotNull ConditionType.Condition cond) {
      this.conditions.add(place, cond);
      this.save();
   }

   public void setCondition(int place, @NotNull ConditionType.Condition cond) {
      this.conditions.set(place, cond);
      this.save();
   }

   public void removeCondition(int place) {
      this.conditions.remove(place);
      this.save();
   }

   public void addAction(@NotNull ActionType.Action action) {
      this.actions.add(action);
      this.save();
   }

   public void addAction(int place, @NotNull ActionType.Action action) {
      this.actions.add(place, action);
      this.save();
   }

   public void setAction(int place, @NotNull ActionType.Action action) {
      this.actions.set(place, action);
      this.save();
   }

   public void removeAction(int place) {
      this.actions.remove(place);
      this.save();
   }

   public void addAlternativeAction(@NotNull ActionType.Action action) {
      this.alternativeActions.add(action);
      this.save();
   }

   public void addAlternativeAction(int place, @NotNull ActionType.Action action) {
      this.alternativeActions.add(place, action);
      this.save();
   }

   public void setAlternativeAction(int place, @NotNull ActionType.Action action) {
      this.alternativeActions.set(place, action);
      this.save();
   }

   public void removeAlternativeAction(int place) {
      this.alternativeActions.remove(place);
      this.save();
   }

   public void addNoConsumesAction(@NotNull ActionType.Action action) {
      this.noConsumesActions.add(action);
      this.save();
   }

   public void addNoConsumesAction(int place, @NotNull ActionType.Action action) {
      this.noConsumesActions.add(place, action);
      this.save();
   }

   public void setNoConsumesAction(int place, @NotNull ActionType.Action action) {
      this.noConsumesActions.set(place, action);
      this.save();
   }

   public void removeNoConsumesAction(int place) {
      this.noConsumesActions.remove(place);
      this.save();
   }

   void delete() {
      config.set(this.getId(), (Object)null);
      config.save();
   }

   void save() {
      if (ActivityManager.getActivity(this.getId()) == this) {
         config.set(this.getId() + ".consumes", this.consumes);
         List<String> rawList1 = new ArrayList();
         this.conditions.forEach((condition) -> {
            rawList1.add(condition.toString());
         });
         config.set(this.getId() + ".conditions", rawList1);
         List<String> rawList2 = new ArrayList();
         this.actions.forEach((action) -> {
            rawList2.add(action.toString());
         });
         config.set(this.getId() + ".actions", rawList2);
         List<String> rawList3 = new ArrayList();
         this.alternativeActions.forEach((action) -> {
            rawList3.add(action.toString());
         });
         config.set(this.getId() + ".alternativeActions", rawList3);
         List<String> rawList4 = new ArrayList();
         this.noConsumesActions.forEach((action) -> {
            rawList4.add(action.toString());
         });
         config.set(this.getId() + ".noConsumesActions", rawList4);
         config.save();
      }
   }

   Activity clone(@NotNull String newId) {
      Activity act = new Activity(newId);
      act.actions.clear();
      act.actions.addAll(this.actions);
      act.alternativeActions.clear();
      act.alternativeActions.addAll(this.alternativeActions);
      act.noConsumesActions.clear();
      act.noConsumesActions.addAll(this.noConsumesActions);
      act.conditions.clear();
      act.conditions.addAll(this.conditions);
      act.consumes = this.consumes;
      return act;
   }

   public final String getId() {
      return this.id;
   }

   static {
      config = ItemTag.get().getConfig("activity" + File.separator + "config.yml");
   }
}
