package github.nighter.smartspawner.commands.clear;

import com.mojang.brigadier.context.CommandContext;
import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.commands.BaseSubCommand;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ClearGhostSpawnersSubCommand extends BaseSubCommand {
   public ClearGhostSpawnersSubCommand(SmartSpawner plugin) {
      super(plugin);
   }

   public String getName() {
      return "ghost_spawners";
   }

   public String getPermission() {
      return "smartspawner.command.clear";
   }

   public String getDescription() {
      return "Check and remove all ghost spawners asynchronously";
   }

   public int execute(CommandContext<CommandSourceStack> context) {
      CommandSender sender = ((CommandSourceStack)context.getSource()).getSender();
      this.plugin.getMessageService().sendMessage(sender, "command_ghost_spawner_check_start");
      List<SpawnerData> allSpawners = this.plugin.getSpawnerManager().getAllSpawners();
      AtomicInteger removedCount = new AtomicInteger(0);
      int totalSpawners = allSpawners.size();
      Iterator var6 = allSpawners.iterator();

      while(var6.hasNext()) {
         SpawnerData spawner = (SpawnerData)var6.next();
         Location loc = spawner.getSpawnerLocation();
         if (loc != null && loc.getWorld() != null) {
            Scheduler.runLocationTask(loc, () -> {
               if (this.plugin.getSpawnerManager().isGhostSpawner(spawner)) {
                  this.plugin.getSpawnerManager().removeGhostSpawner(spawner.getSpawnerId());
                  removedCount.incrementAndGet();
               }

            });
         }
      }

      Scheduler.runTaskLater(() -> {
         int count = removedCount.get();
         if (count > 0) {
            this.plugin.getMessageService().sendMessage(sender, "command_ghost_spawner_cleared", Map.of("count", String.valueOf(count)));
         } else {
            this.plugin.getMessageService().sendMessage(sender, "command_ghost_spawner_none_found");
         }

      }, 100L);
      return 1;
   }
}
