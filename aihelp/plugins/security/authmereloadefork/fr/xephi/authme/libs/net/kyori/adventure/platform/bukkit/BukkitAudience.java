package fr.xephi.authme.libs.net.kyori.adventure.platform.bukkit;

import com.viaversion.viaversion.api.connection.UserConnection;
import fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar;
import fr.xephi.authme.libs.net.kyori.adventure.platform.facet.Facet;
import fr.xephi.authme.libs.net.kyori.adventure.platform.facet.FacetAudience;
import fr.xephi.authme.libs.net.kyori.adventure.platform.facet.FacetAudienceProvider;
import fr.xephi.authme.libs.net.kyori.adventure.platform.viaversion.ViaFacet;
import java.util.Collection;
import java.util.function.Function;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

final class BukkitAudience extends FacetAudience<CommandSender> {
   static final ThreadLocal<Plugin> PLUGIN = new ThreadLocal();
   private static final Function<Player, UserConnection> VIA = new BukkitFacet.ViaHook();
   private static final Collection<Facet.Chat<? extends CommandSender, ?>> CHAT = Facet.of(() -> {
      return new ViaFacet.Chat(Player.class, VIA);
   }, () -> {
      return new CraftBukkitFacet.Chat1_19_3();
   }, () -> {
      return new CraftBukkitFacet.Chat();
   }, () -> {
      return new BukkitFacet.Chat();
   });
   private static final Collection<Facet.ActionBar<Player, ?>> ACTION_BAR = Facet.of(() -> {
      return new ViaFacet.ActionBarTitle(Player.class, VIA);
   }, () -> {
      return new ViaFacet.ActionBar(Player.class, VIA);
   }, () -> {
      return new CraftBukkitFacet.ActionBar_1_17();
   }, () -> {
      return new CraftBukkitFacet.ActionBar();
   }, () -> {
      return new CraftBukkitFacet.ActionBarLegacy();
   });
   private static final Collection<Facet.Title<Player, ?, ?, ?>> TITLE = Facet.of(() -> {
      return new ViaFacet.Title(Player.class, VIA);
   }, () -> {
      return new CraftBukkitFacet.Title_1_17();
   }, () -> {
      return new CraftBukkitFacet.Title();
   });
   private static final Collection<Facet.Sound<Player, Vector>> SOUND = Facet.of(() -> {
      return new BukkitFacet.SoundWithCategory();
   }, () -> {
      return new BukkitFacet.Sound();
   });
   private static final Collection<Facet.EntitySound<Player, Object>> ENTITY_SOUND = Facet.of(() -> {
      return new CraftBukkitFacet.EntitySound_1_19_3();
   }, () -> {
      return new CraftBukkitFacet.EntitySound();
   });
   private static final Collection<Facet.Book<Player, ?, ?>> BOOK = Facet.of(() -> {
      return new CraftBukkitFacet.Book_1_20_5();
   }, () -> {
      return new CraftBukkitFacet.BookPost1_13();
   }, () -> {
      return new CraftBukkitFacet.Book1_13();
   }, () -> {
      return new CraftBukkitFacet.BookPre1_13();
   });
   private static final Collection<Facet.BossBar.Builder<Player, ?>> BOSS_BAR = Facet.of(() -> {
      return new ViaFacet.BossBar.Builder(Player.class, VIA);
   }, () -> {
      return new ViaFacet.BossBar.Builder1_9_To_1_15(Player.class, VIA);
   }, () -> {
      return new CraftBukkitFacet.BossBar.Builder();
   }, () -> {
      return new BukkitFacet.BossBarBuilder();
   }, () -> {
      return new CraftBukkitFacet.BossBarWither.Builder();
   });
   private static final Collection<Facet.TabList<Player, ?>> TAB_LIST = Facet.of(() -> {
      return new ViaFacet.TabList(Player.class, VIA);
   }, () -> {
      return new PaperFacet.TabList();
   }, () -> {
      return new CraftBukkitFacet.TabList();
   }, () -> {
      return new BukkitFacet.TabList();
   });
   private static final Collection<Facet.Pointers<? extends CommandSender>> POINTERS = Facet.of(() -> {
      return new BukkitFacet.CommandSenderPointers();
   }, () -> {
      return new BukkitFacet.ConsoleCommandSenderPointers();
   }, () -> {
      return new BukkitFacet.PlayerPointers();
   });
   @NotNull
   private final Plugin plugin;

   BukkitAudience(@NotNull final Plugin plugin, final FacetAudienceProvider<?, ?> provider, @NotNull final Collection<CommandSender> viewers) {
      super(provider, viewers, CHAT, ACTION_BAR, TITLE, SOUND, ENTITY_SOUND, BOOK, BOSS_BAR, TAB_LIST, POINTERS);
      this.plugin = plugin;
   }

   public void showBossBar(@NotNull final BossBar bar) {
      PLUGIN.set(this.plugin);
      super.showBossBar(bar);
      PLUGIN.set((Object)null);
   }
}
