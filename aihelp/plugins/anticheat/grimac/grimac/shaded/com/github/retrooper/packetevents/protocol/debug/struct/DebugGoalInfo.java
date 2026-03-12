package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DebugGoalInfo {
   private final List<DebugGoalInfo.DebugGoal> goals;

   public DebugGoalInfo(List<DebugGoalInfo.DebugGoal> goals) {
      this.goals = goals;
   }

   public static DebugGoalInfo read(PacketWrapper<?> wrapper) {
      List<DebugGoalInfo.DebugGoal> goals = wrapper.readList(DebugGoalInfo.DebugGoal::read);
      return new DebugGoalInfo(goals);
   }

   public static void write(PacketWrapper<?> wrapper, DebugGoalInfo info) {
      wrapper.writeList(info.goals, DebugGoalInfo.DebugGoal::write);
   }

   public List<DebugGoalInfo.DebugGoal> getGoals() {
      return this.goals;
   }

   public static final class DebugGoal {
      private final int priority;
      private final boolean running;
      private final String name;

      public DebugGoal(int priority, boolean running, String name) {
         this.priority = priority;
         this.running = running;
         this.name = name;
      }

      public static DebugGoalInfo.DebugGoal read(PacketWrapper<?> wrapper) {
         int priority = wrapper.readVarInt();
         boolean running = wrapper.readBoolean();
         String name = wrapper.readString(255);
         return new DebugGoalInfo.DebugGoal(priority, running, name);
      }

      public static void write(PacketWrapper<?> wrapper, DebugGoalInfo.DebugGoal goal) {
         wrapper.writeVarInt(goal.priority);
         wrapper.writeBoolean(goal.running);
         wrapper.writeString(goal.name, 255);
      }

      public int getPriority() {
         return this.priority;
      }

      public boolean isRunning() {
         return this.running;
      }

      public String getName() {
         return this.name;
      }
   }
}
