package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.BoundedDoubleArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.PositiveIntegerArgument;
import com.nisovin.shopkeepers.commands.lib.commands.PlayerCommand;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.EntityUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.bukkit.Ticks;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

class CommandTestDamage extends PlayerCommand {
   private static final String ARGUMENT_DAMAGE = "damage";
   private static final String ARGUMENT_TIMES_PER_TICK = "times-per-tick";
   private static final String ARGUMENT_DURATION_TICKS = "duration-ticks";
   private final SKShopkeepersPlugin plugin;

   CommandTestDamage(SKShopkeepersPlugin plugin) {
      super("testDamage");
      this.plugin = plugin;
      this.setPermission("shopkeeper.debug");
      this.setDescription(Text.of("Produces damage events for the targeted entity."));
      this.setHiddenInParentHelp(true);
      this.addArgument((new BoundedDoubleArgument("damage", 0.0D, Double.MAX_VALUE)).orDefaultValue(0.0D));
      this.addArgument((new PositiveIntegerArgument("times-per-tick")).orDefaultValue(1));
      this.addArgument((new PositiveIntegerArgument("duration-ticks")).orDefaultValue((int)Ticks.fromSeconds(10.0D)));
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      final Player player = (Player)input.getSender();
      final double damage = (Double)context.get("damage");

      assert damage >= 0.0D;

      final int timesPerTick = (Integer)context.get("times-per-tick");

      assert timesPerTick >= 1;

      final int durationTicks = (Integer)context.get("duration-ticks");

      assert durationTicks >= 1;

      final LivingEntity target = (LivingEntity)EntityUtils.getTargetedEntity(player, (entity) -> {
         return entity instanceof LivingEntity;
      });
      if (target == null) {
         player.sendMessage(String.valueOf(ChatColor.RED) + "No living entity targeted!");
      } else {
         String var10001 = target.getName();
         player.sendMessage(TextUtils.colorize("&aStarting damage task: Target: &e" + var10001 + "&a, Damage: &e" + TextUtils.format(damage) + "&a, Per tick: &e" + timesPerTick + "&a, Duration &e" + durationTicks + " ticks &a..."));
         (new BukkitRunnable(this) {
            private int tickCounter = 0;

            public void run() {
               boolean playerValid = player.isValid();
               if (this.tickCounter < durationTicks && playerValid && target.isValid()) {
                  for(int i = 0; i < timesPerTick; ++i) {
                     target.setNoDamageTicks(0);
                     target.setLastDamage(0.0D);
                     target.damage(damage, player);
                     if (target.isDead()) {
                        break;
                     }
                  }

                  ++this.tickCounter;
                  if (this.tickCounter % 20 == 0) {
                     Player var10000 = player;
                     String var10001 = String.valueOf(ChatColor.GRAY);
                     var10000.sendMessage(var10001 + "... (" + String.valueOf(ChatColor.YELLOW) + this.tickCounter + String.valueOf(ChatColor.GRAY) + " / " + String.valueOf(ChatColor.YELLOW) + durationTicks + String.valueOf(ChatColor.GRAY) + ")");
                  }

               } else {
                  if (playerValid) {
                     player.sendMessage(String.valueOf(ChatColor.GREEN) + "... Done");
                  }

                  this.cancel();
               }
            }
         }).runTaskTimer(this.plugin, 1L, 1L);
      }
   }
}
