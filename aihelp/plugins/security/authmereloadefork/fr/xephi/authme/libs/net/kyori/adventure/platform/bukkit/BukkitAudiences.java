package fr.xephi.authme.libs.net.kyori.adventure.platform.bukkit;

import fr.xephi.authme.libs.net.kyori.adventure.audience.Audience;
import fr.xephi.authme.libs.net.kyori.adventure.platform.AudienceProvider;
import fr.xephi.authme.libs.net.kyori.adventure.sound.Sound;
import java.util.function.Predicate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface BukkitAudiences extends AudienceProvider {
   @NotNull
   static BukkitAudiences create(@NotNull final Plugin plugin) {
      return BukkitAudiencesImpl.instanceFor(plugin);
   }

   @NotNull
   static BukkitAudiences.Builder builder(@NotNull final Plugin plugin) {
      return BukkitAudiencesImpl.builder(plugin);
   }

   @NotNull
   static Sound.Emitter asEmitter(@NotNull final Entity entity) {
      return new BukkitEmitter(entity);
   }

   @NotNull
   Audience sender(@NotNull final CommandSender sender);

   @NotNull
   Audience player(@NotNull final Player player);

   @NotNull
   Audience filter(@NotNull final Predicate<CommandSender> filter);

   public interface Builder extends AudienceProvider.Builder<BukkitAudiences, BukkitAudiences.Builder> {
   }
}
