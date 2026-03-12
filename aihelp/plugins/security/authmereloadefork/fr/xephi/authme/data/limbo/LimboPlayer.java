package fr.xephi.authme.data.limbo;

import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import fr.xephi.authme.task.MessageTask;
import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.Location;

public class LimboPlayer {
   public static final float DEFAULT_WALK_SPEED = 0.2F;
   public static final float DEFAULT_FLY_SPEED = 0.1F;
   private final boolean canFly;
   private final boolean operator;
   private final Collection<UserGroup> groups;
   private final Location loc;
   private final float walkSpeed;
   private final float flySpeed;
   private MyScheduledTask timeoutTask = null;
   private MessageTask messageTask = null;
   private LimboPlayerState state;

   public LimboPlayer(Location loc, boolean operator, Collection<UserGroup> groups, boolean fly, float walkSpeed, float flySpeed) {
      this.state = LimboPlayerState.PASSWORD_REQUIRED;
      this.loc = loc;
      this.operator = operator;
      this.groups = new ArrayList(groups);
      this.canFly = fly;
      this.walkSpeed = walkSpeed;
      this.flySpeed = flySpeed;
   }

   public Location getLocation() {
      return this.loc;
   }

   public boolean isOperator() {
      return this.operator;
   }

   public Collection<UserGroup> getGroups() {
      return this.groups;
   }

   public boolean isCanFly() {
      return this.canFly;
   }

   public float getWalkSpeed() {
      return this.walkSpeed;
   }

   public float getFlySpeed() {
      return this.flySpeed;
   }

   public MyScheduledTask getTimeoutTask() {
      return this.timeoutTask;
   }

   public void setTimeoutTask(MyScheduledTask timeoutTask) {
      if (this.timeoutTask != null) {
         this.timeoutTask.cancel();
      }

      this.timeoutTask = timeoutTask;
   }

   public MessageTask getMessageTask() {
      return this.messageTask;
   }

   public void setMessageTask(MessageTask messageTask) {
      if (this.messageTask != null) {
         this.messageTask.cancel();
      }

      this.messageTask = messageTask;
   }

   public void clearTasks() {
      this.setMessageTask((MessageTask)null);
      this.setTimeoutTask((MyScheduledTask)null);
   }

   public LimboPlayerState getState() {
      return this.state;
   }

   public void setState(LimboPlayerState state) {
      this.state = state;
   }
}
