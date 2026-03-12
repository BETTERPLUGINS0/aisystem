package me.SuperRonanCraft.BetterRTP.lib.folialib.impl;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import me.SuperRonanCraft.BetterRTP.lib.folialib.enums.EntityTaskResult;
import me.SuperRonanCraft.BetterRTP.lib.folialib.wrapper.task.WrappedTask;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface ServerImplementation {
   CompletableFuture<Void> runNextTick(Consumer<WrappedTask> var1);

   CompletableFuture<Void> runAsync(Consumer<WrappedTask> var1);

   WrappedTask runLater(Runnable var1, long var2);

   void runLater(Consumer<WrappedTask> var1, long var2);

   WrappedTask runLater(Runnable var1, long var2, TimeUnit var4);

   void runLater(Consumer<WrappedTask> var1, long var2, TimeUnit var4);

   WrappedTask runLaterAsync(Runnable var1, long var2);

   void runLaterAsync(Consumer<WrappedTask> var1, long var2);

   WrappedTask runLaterAsync(Runnable var1, long var2, TimeUnit var4);

   void runLaterAsync(Consumer<WrappedTask> var1, long var2, TimeUnit var4);

   WrappedTask runTimer(Runnable var1, long var2, long var4);

   void runTimer(Consumer<WrappedTask> var1, long var2, long var4);

   WrappedTask runTimer(Runnable var1, long var2, long var4, TimeUnit var6);

   void runTimer(Consumer<WrappedTask> var1, long var2, long var4, TimeUnit var6);

   WrappedTask runTimerAsync(Runnable var1, long var2, long var4);

   void runTimerAsync(Consumer<WrappedTask> var1, long var2, long var4);

   WrappedTask runTimerAsync(Runnable var1, long var2, long var4, TimeUnit var6);

   void runTimerAsync(Consumer<WrappedTask> var1, long var2, long var4, TimeUnit var6);

   CompletableFuture<Void> runAtLocation(Location var1, Consumer<WrappedTask> var2);

   WrappedTask runAtLocationLater(Location var1, Runnable var2, long var3);

   void runAtLocationLater(Location var1, Consumer<WrappedTask> var2, long var3);

   WrappedTask runAtLocationLater(Location var1, Runnable var2, long var3, TimeUnit var5);

   void runAtLocationLater(Location var1, Consumer<WrappedTask> var2, long var3, TimeUnit var5);

   WrappedTask runAtLocationTimer(Location var1, Runnable var2, long var3, long var5);

   void runAtLocationTimer(Location var1, Consumer<WrappedTask> var2, long var3, long var5);

   WrappedTask runAtLocationTimer(Location var1, Runnable var2, long var3, long var5, TimeUnit var7);

   void runAtLocationTimer(Location var1, Consumer<WrappedTask> var2, long var3, long var5, TimeUnit var7);

   CompletableFuture<EntityTaskResult> runAtEntity(Entity var1, Consumer<WrappedTask> var2);

   CompletableFuture<EntityTaskResult> runAtEntityWithFallback(Entity var1, Consumer<WrappedTask> var2, Runnable var3);

   WrappedTask runAtEntityLater(Entity var1, Runnable var2, long var3);

   void runAtEntityLater(Entity var1, Consumer<WrappedTask> var2, long var3);

   WrappedTask runAtEntityLater(Entity var1, Runnable var2, long var3, TimeUnit var5);

   void runAtEntityLater(Entity var1, Consumer<WrappedTask> var2, long var3, TimeUnit var5);

   WrappedTask runAtEntityTimer(Entity var1, Runnable var2, long var3, long var5);

   void runAtEntityTimer(Entity var1, Consumer<WrappedTask> var2, long var3, long var5);

   WrappedTask runAtEntityTimer(Entity var1, Runnable var2, long var3, long var5, TimeUnit var7);

   void runAtEntityTimer(Entity var1, Consumer<WrappedTask> var2, long var3, long var5, TimeUnit var7);

   void cancelTask(WrappedTask var1);

   void cancelAllTasks();

   Player getPlayer(String var1);

   Player getPlayerExact(String var1);

   Player getPlayer(UUID var1);

   CompletableFuture<Boolean> teleportAsync(Player var1, Location var2);

   WrappedTask wrapTask(Object var1);
}
