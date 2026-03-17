package com.bergerkiller.bukkit.tc.controller.components;

import com.bergerkiller.bukkit.tc.actions.Action;
import com.bergerkiller.bukkit.tc.actions.GroupAction;
import com.bergerkiller.bukkit.tc.actions.GroupActionRefill;
import com.bergerkiller.bukkit.tc.actions.GroupActionSizzle;
import com.bergerkiller.bukkit.tc.actions.GroupActionWaitDelay;
import com.bergerkiller.bukkit.tc.actions.GroupActionWaitForever;
import com.bergerkiller.bukkit.tc.actions.GroupActionWaitState;
import com.bergerkiller.bukkit.tc.actions.GroupActionWaitTicks;
import com.bergerkiller.bukkit.tc.actions.GroupActionWaitTill;
import com.bergerkiller.bukkit.tc.actions.MemberAction;
import com.bergerkiller.bukkit.tc.actions.MemberActionLaunch;
import com.bergerkiller.bukkit.tc.actions.MemberActionWaitOccupied;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import java.util.Iterator;

public class ActionTrackerGroup extends ActionTracker {
   private final MinecartGroup owner;

   public ActionTrackerGroup(MinecartGroup owner) {
      this.owner = owner;
   }

   public MinecartGroup getOwner() {
      return this.owner;
   }

   public MinecartGroup getGroupOwner() {
      return this.owner;
   }

   public void doTick() {
      super.doTick();
      Iterator var1 = this.owner.iterator();

      while(var1.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var1.next();
         member.getActions().doTick();
      }

   }

   public void launchReset() {
      Action action = this.getCurrentAction();
      if (action instanceof MemberActionLaunch && action.elapsedTicks() == 0) {
         MemberActionLaunch launchAction = (MemberActionLaunch)action;
         this.getOwner().setForwardForce(launchAction.getTargetVelocity());
      } else if (action instanceof MemberActionWaitOccupied) {
         MemberActionWaitOccupied waitOccupied = (MemberActionWaitOccupied)action;
         if (!Double.isNaN(waitOccupied.getPostWaitLaunchForce())) {
            this.getOwner().setForwardForce(waitOccupied.getPostWaitLaunchForce());
         }
      }

      this.clear();
   }

   public void clear() {
      super.clear();
      Iterator var1 = this.owner.iterator();

      while(var1.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var1.next();
         member.getActions().clear();
      }

   }

   public <T extends Action> T addAction(T action) {
      if (action instanceof GroupAction) {
         ((GroupAction)action).setGroup(this.owner);
      } else if (action instanceof MemberAction && ((MemberAction)action).getMember() == null) {
         throw new RuntimeException("Can not add member action without a member set beforehand!");
      }

      return super.addAction(action);
   }

   public GroupActionWaitDelay addActionWait(long delay) {
      return (GroupActionWaitDelay)this.addAction(new GroupActionWaitDelay(delay));
   }

   public GroupActionWaitTill addActionWaitTill(long time) {
      return (GroupActionWaitTill)this.addAction(new GroupActionWaitTill(time));
   }

   public GroupActionWaitTicks addActionWaitTicks(int ticks) {
      return (GroupActionWaitTicks)this.addAction(new GroupActionWaitTicks(ticks));
   }

   public GroupActionWaitForever addActionWaitForever() {
      return (GroupActionWaitForever)this.addAction(new GroupActionWaitForever());
   }

   public GroupActionWaitState addActionWaitState() {
      return (GroupActionWaitState)this.addAction(new GroupActionWaitState());
   }

   public GroupActionSizzle addActionSizzle() {
      return (GroupActionSizzle)this.addAction(new GroupActionSizzle());
   }

   public GroupActionRefill addActionRefill() {
      return (GroupActionRefill)this.addAction(new GroupActionRefill());
   }
}
