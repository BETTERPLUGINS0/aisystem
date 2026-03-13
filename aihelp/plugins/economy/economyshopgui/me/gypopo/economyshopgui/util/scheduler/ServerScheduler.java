package me.gypopo.economyshopgui.util.scheduler;

import me.gypopo.economyshopgui.EconomyShopGUI;

public interface ServerScheduler {
   ScheduledTask runTask(EconomyShopGUI var1, Runnable var2);

   ScheduledTask runTaskLater(EconomyShopGUI var1, Runnable var2, long var3);

   ScheduledTask runTaskTimer(EconomyShopGUI var1, Runnable var2, long var3, long var5);

   ScheduledTask runTaskAsync(EconomyShopGUI var1, Runnable var2);

   ScheduledTask runTaskLaterAsync(EconomyShopGUI var1, Runnable var2, long var3);

   ScheduledTask runTaskAsyncTimer(EconomyShopGUI var1, Runnable var2, long var3, long var5);

   void runKillableTask(Runnable var1);

   void runKillableTaskLater(Runnable var1, long var2);

   void cancelTasks(EconomyShopGUI var1);
}
