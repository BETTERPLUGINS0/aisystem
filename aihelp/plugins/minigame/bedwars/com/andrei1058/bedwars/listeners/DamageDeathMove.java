/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.IronGolem
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.entity.Silverfish
 *  org.bukkit.entity.Snowball
 *  org.bukkit.entity.TNTPrimed
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.projectiles.ProjectileSource
 *  org.bukkit.util.Vector
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.generator.IGenerator;
import com.andrei1058.bedwars.api.arena.shop.ShopHolo;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.entity.Despawnable;
import com.andrei1058.bedwars.api.events.player.PlayerInvisibilityPotionEvent;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.andrei1058.bedwars.api.events.team.TeamEliminatedEvent;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.LastHit;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.arena.team.BedWarsTeam;
import com.andrei1058.bedwars.configuration.Sounds;
import com.andrei1058.bedwars.listeners.dropshandler.PlayerDrops;
import com.andrei1058.bedwars.support.paper.TeleportManager;
import java.text.DecimalFormat;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class DamageDeathMove
implements Listener {
    private final double tntJumpBarycenterAlterationInY = BedWars.config.getYml().getDouble("tnt-jump-settings.barycenter-alteration-in-y");
    private final double tntJumpStrengthReductionConstant = BedWars.config.getYml().getDouble("tnt-jump-settings.strength-reduction-constant");
    private final double tntJumpYAxisReductionConstant = BedWars.config.getYml().getDouble("tnt-jump-settings.y-axis-reduction-constant");
    private final double tntDamageSelf = BedWars.config.getYml().getDouble("tnt-jump-settings.damage-self");
    private final double tntDamageTeammates = BedWars.config.getYml().getDouble("tnt-jump-settings.damage-teammates");
    private final double tntDamageOthers = BedWars.config.getYml().getDouble("tnt-jump-settings.damage-others");

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Player p;
        IArena a;
        if (e.getEntity() instanceof Player && (a = Arena.getArenaByPlayer(p = (Player)e.getEntity())) != null) {
            if (a.isSpectator(p)) {
                e.setCancelled(true);
                return;
            }
            if (a.isReSpawning(p)) {
                e.setCancelled(true);
                return;
            }
            if (a.getStatus() != GameState.playing) {
                e.setCancelled(true);
                return;
            }
            if (BedWarsTeam.reSpawnInvulnerability.containsKey(p.getUniqueId())) {
                if (BedWarsTeam.reSpawnInvulnerability.get(p.getUniqueId()) > System.currentTimeMillis()) {
                    e.setCancelled(true);
                } else {
                    BedWarsTeam.reSpawnInvulnerability.remove(p.getUniqueId());
                }
            }
        }
        if (BedWars.getServerType() == ServerType.MULTIARENA && e.getEntity().getLocation().getWorld().getName().equalsIgnoreCase(BedWars.getLobbyWorld())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onBowHit(EntityDamageByEntityEvent e) {
        if (e.getEntity().getType() != EntityType.PLAYER) {
            return;
        }
        if (!(e.getDamager() instanceof Projectile)) {
            return;
        }
        Projectile projectile = (Projectile)e.getDamager();
        if (projectile.getShooter() == null) {
            return;
        }
        if (!(projectile.getShooter() instanceof Player)) {
            return;
        }
        Player p = (Player)e.getEntity();
        Player damager = (Player)projectile.getShooter();
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null) {
            return;
        }
        if (a.getStatus() != GameState.playing) {
            return;
        }
        ITeam team = a.getTeam(p);
        Language lang = Language.getPlayerLanguage(damager);
        if (lang.m(Messages.PLAYER_HIT_BOW).isEmpty()) {
            return;
        }
        String message = lang.m(Messages.PLAYER_HIT_BOW).replace("{amount}", new DecimalFormat("00.#").format(((Player)e.getEntity()).getHealth() - e.getFinalDamage())).replace("{TeamColor}", team.getColor().chat().toString()).replace("{TeamName}", team.getDisplayName(lang)).replace("{PlayerName}", ChatColor.stripColor((String)p.getDisplayName()));
        damager.sendMessage(message);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();
            IArena a = Arena.getArenaByPlayer(p);
            if (a != null) {
                LastHit lh;
                if (a.getStatus() != GameState.playing) {
                    e.setCancelled(true);
                    return;
                }
                if (a.isSpectator(p) || a.isReSpawning(p)) {
                    e.setCancelled(true);
                    return;
                }
                Player damager = null;
                boolean projectile = false;
                if (e.getDamager() instanceof Player) {
                    damager = (Player)e.getDamager();
                } else if (e.getDamager() instanceof Projectile) {
                    ProjectileSource shooter = ((Projectile)e.getDamager()).getShooter();
                    if (!(shooter instanceof Player)) {
                        return;
                    }
                    damager = (Player)shooter;
                    projectile = true;
                } else if (e.getDamager() instanceof Player) {
                    damager = (Player)e.getDamager();
                    if (a.isReSpawning(damager)) {
                        e.setCancelled(true);
                        return;
                    }
                } else if (e.getDamager() instanceof TNTPrimed) {
                    TNTPrimed tnt = (TNTPrimed)e.getDamager();
                    if (tnt.getSource() != null) {
                        if (!(tnt.getSource() instanceof Player)) return;
                        damager = (Player)tnt.getSource();
                        if (damager.equals((Object)p)) {
                            if (this.tntDamageSelf > -1.0) {
                                e.setDamage(this.tntDamageSelf);
                            }
                            LivingEntity damaged = (LivingEntity)e.getEntity();
                            Vector distance = damaged.getLocation().subtract(0.0, this.tntJumpBarycenterAlterationInY, 0.0).toVector().subtract(tnt.getLocation().toVector());
                            Vector direction = distance.clone().normalize();
                            double force = (double)(tnt.getYield() * tnt.getYield()) / (this.tntJumpStrengthReductionConstant + distance.length());
                            Vector resultingForce = direction.clone().multiply(force);
                            resultingForce.setY(resultingForce.getY() / (distance.length() + this.tntJumpYAxisReductionConstant));
                            damaged.setVelocity(resultingForce);
                        } else {
                            ITeam damagerTeam;
                            ITeam currentTeam = a.getTeam(p);
                            if (currentTeam.equals(damagerTeam = a.getTeam(damager))) {
                                if (this.tntDamageTeammates > -1.0) {
                                    e.setDamage(this.tntDamageTeammates);
                                }
                            } else if (this.tntDamageOthers > -1.0) {
                                e.setDamage(this.tntDamageOthers);
                            }
                        }
                    }
                } else if (e.getDamager() instanceof Silverfish || e.getDamager() instanceof IronGolem) {
                    lh = LastHit.getLastHit(p);
                    if (lh != null) {
                        lh.setDamager(e.getDamager());
                        lh.setTime(System.currentTimeMillis());
                    } else {
                        new LastHit(p, e.getDamager(), System.currentTimeMillis());
                    }
                }
                if (damager != null) {
                    if (a.isSpectator(damager) || a.isReSpawning(damager.getUniqueId())) {
                        e.setCancelled(true);
                        return;
                    }
                    if (a.getTeam(p).equals(a.getTeam(damager))) {
                        if (e.getDamager() instanceof TNTPrimed) return;
                        e.setCancelled(true);
                        return;
                    }
                    if (BedWarsTeam.reSpawnInvulnerability.containsKey(p.getUniqueId())) {
                        if (BedWarsTeam.reSpawnInvulnerability.get(p.getUniqueId()) > System.currentTimeMillis()) {
                            e.setCancelled(true);
                            return;
                        }
                        BedWarsTeam.reSpawnInvulnerability.remove(p.getUniqueId());
                    }
                    BedWarsTeam.reSpawnInvulnerability.remove(damager.getUniqueId());
                    lh = LastHit.getLastHit(p);
                    if (lh != null) {
                        lh.setDamager((Entity)damager);
                        lh.setTime(System.currentTimeMillis());
                    } else {
                        new LastHit(p, (Entity)damager, System.currentTimeMillis());
                    }
                    if (a.getShowTime().containsKey(p)) {
                        Bukkit.getScheduler().runTask((Plugin)BedWars.plugin, () -> {
                            for (Player on : a.getWorld().getPlayers()) {
                                BedWars.nms.showArmor(p, on);
                            }
                            a.getShowTime().remove(p);
                            p.removePotionEffect(PotionEffectType.INVISIBILITY);
                            ITeam team = a.getTeam(p);
                            p.sendMessage(Language.getMsg(p, Messages.INTERACT_INVISIBILITY_REMOVED_DAMGE_TAKEN));
                            Bukkit.getPluginManager().callEvent((Event)new PlayerInvisibilityPotionEvent(PlayerInvisibilityPotionEvent.Type.REMOVED, team, p, a));
                        });
                    }
                }
            }
        } else if (BedWars.nms.isDespawnable(e.getEntity())) {
            Player damager;
            if (e.getDamager() instanceof Player) {
                damager = (Player)e.getDamager();
            } else if (e.getDamager() instanceof Projectile) {
                Projectile proj = (Projectile)e.getDamager();
                damager = (Player)proj.getShooter();
            } else {
                if (!(e.getDamager() instanceof TNTPrimed)) return;
                TNTPrimed tnt = (TNTPrimed)e.getDamager();
                if (!(tnt.getSource() instanceof Player)) return;
                damager = (Player)tnt.getSource();
            }
            IArena a = Arena.getArenaByPlayer(damager);
            if (a != null) {
                if (a.isPlayer(damager)) {
                    if (a.getTeam(damager) == BedWars.nms.getDespawnablesList().get(e.getEntity().getUniqueId()).getTeam()) {
                        e.setCancelled(true);
                    }
                } else {
                    e.setCancelled(true);
                }
            }
        }
        if (BedWars.getServerType() != ServerType.MULTIARENA || !e.getEntity().getLocation().getWorld().getName().equalsIgnoreCase(BedWars.getLobbyWorld())) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onDeath(@NotNull PlayerDeathEvent e) {
        Player victim = e.getEntity();
        Player killer = e.getEntity().getKiller();
        ITeam killersTeam = null;
        IArena a = Arena.getArenaByPlayer(victim);
        if (BedWars.getServerType() == ServerType.MULTIARENA && BedWars.getLobbyWorld().equals(e.getEntity().getWorld().getName()) || a != null) {
            e.setDeathMessage(null);
        }
        if (a != null) {
            Language lang;
            PlayerKillEvent.PlayerKillCause cause;
            if (a.isSpectator(victim)) {
                victim.spigot().respawn();
                return;
            }
            if (a.getStatus() != GameState.playing) {
                victim.spigot().respawn();
                return;
            }
            EntityDamageEvent damageEvent = e.getEntity().getLastDamageCause();
            ITeam victimsTeam = a.getTeam(victim);
            if (a.getStatus() != GameState.playing) {
                victim.spigot().respawn();
                return;
            }
            if (victimsTeam == null) {
                victim.spigot().respawn();
                return;
            }
            BedWars.nms.clearArrowsFromPlayerBody(victim);
            String message = victimsTeam.isBedDestroyed() ? Messages.PLAYER_DIE_UNKNOWN_REASON_FINAL_KILL : Messages.PLAYER_DIE_UNKNOWN_REASON_REGULAR;
            PlayerKillEvent.PlayerKillCause playerKillCause = cause = victimsTeam.isBedDestroyed() ? PlayerKillEvent.PlayerKillCause.UNKNOWN_FINAL_KILL : PlayerKillEvent.PlayerKillCause.UNKNOWN;
            if (damageEvent != null) {
                LastHit lh;
                if (damageEvent.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                    lh = LastHit.getLastHit(victim);
                    if (lh != null && lh.getTime() >= System.currentTimeMillis() - 15000L) {
                        if (lh.getDamager() instanceof Player) {
                            killer = (Player)lh.getDamager();
                        }
                        if (killer != null && killer.getUniqueId().equals(victim.getUniqueId())) {
                            killer = null;
                        }
                    }
                    message = killer == null ? (victimsTeam.isBedDestroyed() ? Messages.PLAYER_DIE_EXPLOSION_WITHOUT_SOURCE_FINAL_KILL : Messages.PLAYER_DIE_EXPLOSION_WITHOUT_SOURCE_REGULAR) : (killer != victim ? (victimsTeam.isBedDestroyed() ? Messages.PLAYER_DIE_EXPLOSION_WITH_SOURCE_FINAL_KILL : Messages.PLAYER_DIE_EXPLOSION_WITH_SOURCE_REGULAR_KILL) : (victimsTeam.isBedDestroyed() ? Messages.PLAYER_DIE_EXPLOSION_WITHOUT_SOURCE_FINAL_KILL : Messages.PLAYER_DIE_EXPLOSION_WITHOUT_SOURCE_REGULAR));
                    cause = victimsTeam.isBedDestroyed() ? PlayerKillEvent.PlayerKillCause.EXPLOSION_FINAL_KILL : PlayerKillEvent.PlayerKillCause.EXPLOSION;
                } else if (damageEvent.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    lh = LastHit.getLastHit(victim);
                    if (lh != null && lh.getTime() >= System.currentTimeMillis() - 15000L) {
                        if (lh.getDamager() instanceof Player) {
                            killer = (Player)lh.getDamager();
                        }
                        if (killer != null && killer.getUniqueId().equals(victim.getUniqueId())) {
                            killer = null;
                        }
                    }
                    message = killer == null ? (victimsTeam.isBedDestroyed() ? Messages.PLAYER_DIE_VOID_FALL_FINAL_KILL : Messages.PLAYER_DIE_VOID_FALL_REGULAR_KILL) : (killer != victim ? (victimsTeam.isBedDestroyed() ? Messages.PLAYER_DIE_KNOCKED_IN_VOID_FINAL_KILL : Messages.PLAYER_DIE_KNOCKED_IN_VOID_REGULAR_KILL) : (victimsTeam.isBedDestroyed() ? Messages.PLAYER_DIE_VOID_FALL_FINAL_KILL : Messages.PLAYER_DIE_VOID_FALL_REGULAR_KILL));
                    cause = victimsTeam.isBedDestroyed() ? PlayerKillEvent.PlayerKillCause.VOID_FINAL_KILL : PlayerKillEvent.PlayerKillCause.VOID;
                } else if (damageEvent.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    if (killer == null) {
                        lh = LastHit.getLastHit(victim);
                        if (lh != null && lh.getTime() >= System.currentTimeMillis() - 15000L && BedWars.nms.isDespawnable(lh.getDamager())) {
                            Despawnable d = BedWars.nms.getDespawnablesList().get(lh.getDamager().getUniqueId());
                            killersTeam = d.getTeam();
                            message = d.getEntity().getType() == EntityType.IRON_GOLEM ? (victimsTeam.isBedDestroyed() ? Messages.PLAYER_DIE_IRON_GOLEM_FINAL_KILL : Messages.PLAYER_DIE_IRON_GOLEM_REGULAR) : (victimsTeam.isBedDestroyed() ? Messages.PLAYER_DIE_DEBUG_FINAL_KILL : Messages.PLAYER_DIE_DEBUG_REGULAR);
                            cause = victimsTeam.isBedDestroyed() ? d.getDeathFinalCause() : d.getDeathRegularCause();
                        }
                    } else {
                        message = victimsTeam.isBedDestroyed() ? Messages.PLAYER_DIE_PVP_FINAL_KILL : Messages.PLAYER_DIE_PVP_REGULAR_KILL;
                        cause = victimsTeam.isBedDestroyed() ? PlayerKillEvent.PlayerKillCause.PVP_FINAL_KILL : PlayerKillEvent.PlayerKillCause.PVP;
                    }
                } else if (damageEvent.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                    if (killer != null) {
                        message = victimsTeam.isBedDestroyed() ? Messages.PLAYER_DIE_SHOOT_FINAL_KILL : Messages.PLAYER_DIE_SHOOT_REGULAR;
                        cause = victimsTeam.isBedDestroyed() ? PlayerKillEvent.PlayerKillCause.PLAYER_SHOOT_FINAL_KILL : PlayerKillEvent.PlayerKillCause.PLAYER_SHOOT;
                    }
                } else if (damageEvent.getCause() == EntityDamageEvent.DamageCause.FALL && (lh = LastHit.getLastHit(victim)) != null && lh.getTime() >= System.currentTimeMillis() - 10000L) {
                    if (lh.getDamager() instanceof Player) {
                        killer = (Player)lh.getDamager();
                    }
                    if (killer != null && killer.getUniqueId().equals(victim.getUniqueId())) {
                        killer = null;
                    }
                    if (killer != null) {
                        message = killer != victim ? (victimsTeam.isBedDestroyed() ? Messages.PLAYER_DIE_KNOCKED_BY_FINAL_KILL : Messages.PLAYER_DIE_KNOCKED_BY_REGULAR_KILL) : (victimsTeam.isBedDestroyed() ? Messages.PLAYER_DIE_VOID_FALL_FINAL_KILL : Messages.PLAYER_DIE_VOID_FALL_REGULAR_KILL);
                    }
                    PlayerKillEvent.PlayerKillCause playerKillCause2 = cause = victimsTeam.isBedDestroyed() ? PlayerKillEvent.PlayerKillCause.PLAYER_PUSH_FINAL : PlayerKillEvent.PlayerKillCause.PLAYER_PUSH;
                }
            }
            if (killer != null) {
                killersTeam = a.getTeam(killer);
            }
            String finalMessage = message;
            PlayerKillEvent playerKillEvent = new PlayerKillEvent(a, victim, victimsTeam, killer, killersTeam, player -> Language.getMsg(player, finalMessage), cause);
            Bukkit.getPluginManager().callEvent((Event)playerKillEvent);
            if (killer != null && playerKillEvent.playSound()) {
                Sounds.playSound("kill", killer);
            }
            if (null != playerKillEvent.getMessage()) {
                for (Player on : a.getPlayers()) {
                    lang = Language.getPlayerLanguage(on);
                    on.sendMessage(playerKillEvent.getMessage().apply(on).replace("{PlayerColor}", victimsTeam.getColor().chat().toString()).replace("{PlayerName}", victim.getDisplayName()).replace("{PlayerNameUnformatted}", victim.getName()).replace("{PlayerTeamName}", victimsTeam.getDisplayName(lang)).replace("{KillerColor}", killersTeam == null ? "" : killersTeam.getColor().chat().toString()).replace("{KillerName}", killer == null ? "" : killer.getDisplayName()).replace("{KillerNameUnformatted}", killer == null ? "" : killer.getName()).replace("{KillerTeamName}", killersTeam == null ? "" : killersTeam.getDisplayName(lang)));
                }
            }
            if (null != playerKillEvent.getMessage()) {
                for (Player on : a.getSpectators()) {
                    lang = Language.getPlayerLanguage(on);
                    on.sendMessage(playerKillEvent.getMessage().apply(on).replace("{PlayerColor}", victimsTeam.getColor().chat().toString()).replace("{PlayerName}", victim.getDisplayName()).replace("{PlayerNameUnformatted}", victim.getName()).replace("{KillerColor}", killersTeam == null ? "" : killersTeam.getColor().chat().toString()).replace("{PlayerTeamName}", victimsTeam.getDisplayName(lang)).replace("{KillerName}", killer == null ? "" : killer.getDisplayName()).replace("{KillerNameUnformatted}", killer == null ? "" : killer.getName()).replace("{KillerTeamName}", killersTeam == null ? "" : killersTeam.getDisplayName(lang)));
                }
            }
            if (PlayerDrops.handlePlayerDrops(a, victim, killer, victimsTeam, killersTeam, cause, e.getDrops())) {
                e.getDrops().clear();
            }
            Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> victim.spigot().respawn(), 3L);
            LastHit lastHit = LastHit.getLastHit(victim);
            if (lastHit != null) {
                lastHit.setDamager(null);
            }
            if (victimsTeam.isBedDestroyed() && victimsTeam.getSize() == 1 && a.getConfig().getBoolean("disable-generator-for-empty-teams")) {
                for (IGenerator g : victimsTeam.getGenerators()) {
                    g.disable();
                }
                victimsTeam.getGenerators().clear();
            }
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onRespawn(PlayerRespawnEvent e) {
        IArena a = Arena.getArenaByPlayer(e.getPlayer());
        if (a == null) {
            SetupSession ss = SetupSession.getSession(e.getPlayer().getUniqueId());
            if (ss != null) {
                e.setRespawnLocation(e.getPlayer().getWorld().getSpawnLocation());
            }
        } else {
            if (a.isSpectator(e.getPlayer())) {
                e.setRespawnLocation(a.getSpectatorLocation());
                String iso = Language.getPlayerLanguage(e.getPlayer()).getIso();
                for (IGenerator o : a.getOreGenerators()) {
                    o.updateHolograms(e.getPlayer(), iso);
                }
                for (ITeam t : a.getTeams()) {
                    for (IGenerator o : t.getGenerators()) {
                        o.updateHolograms(e.getPlayer(), iso);
                    }
                }
                for (ShopHolo sh : ShopHolo.getShopHolo()) {
                    if (sh.getA() != a) continue;
                    sh.updateForPlayer(e.getPlayer(), iso);
                }
                a.sendSpectatorCommandItems(e.getPlayer());
                return;
            }
            ITeam t = a.getTeam(e.getPlayer());
            if (t == null) {
                e.setRespawnLocation(a.getReSpawnLocation());
                BedWars.plugin.getLogger().severe(e.getPlayer().getName() + " re-spawn error on " + a.getArenaName() + "[" + a.getWorldName() + "] because the team was NULL and he was not spectating!");
                BedWars.plugin.getLogger().severe("This is caused by one of your plugins: remove or configure any re-spawn related plugins.");
                a.removePlayer(e.getPlayer(), false);
                a.removeSpectator(e.getPlayer(), false);
                return;
            }
            if (t.isBedDestroyed()) {
                e.setRespawnLocation(a.getSpectatorLocation());
                a.addSpectator(e.getPlayer(), true, null);
                t.getMembers().remove(e.getPlayer());
                e.getPlayer().sendMessage(Language.getMsg(e.getPlayer(), Messages.PLAYER_DIE_ELIMINATED_CHAT));
                if (t.getMembers().isEmpty()) {
                    Bukkit.getPluginManager().callEvent((Event)new TeamEliminatedEvent(a, t));
                    for (Player p : a.getWorld().getPlayers()) {
                        p.sendMessage(Language.getMsg(p, Messages.TEAM_ELIMINATED_CHAT).replace("{TeamColor}", t.getColor().chat().toString()).replace("{TeamName}", t.getDisplayName(Language.getPlayerLanguage(p))));
                    }
                    Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, a::checkWinner, 40L);
                }
            } else {
                int respawnTime = BedWars.config.getInt("countdowns.player-re-spawn");
                if (respawnTime > 1) {
                    e.setRespawnLocation(a.getReSpawnLocation());
                    a.startReSpawnSession(e.getPlayer(), respawnTime);
                } else {
                    e.setRespawnLocation(t.getSpawn());
                    t.respawnMember(e.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (Arena.isInArena(e.getPlayer())) {
            IArena a = Arena.getArenaByPlayer(e.getPlayer());
            if (e.getFrom().getChunk().getX() != e.getTo().getChunk().getX() || e.getFrom().getChunk().getZ() != e.getTo().getChunk().getZ() || !e.getFrom().getChunk().getWorld().equals((Object)e.getTo().getChunk().getWorld())) {
                String iso = Language.getPlayerLanguage(e.getPlayer()).getIso();
                for (IGenerator iGenerator : a.getOreGenerators()) {
                    iGenerator.updateHolograms(e.getPlayer(), iso);
                }
                for (ITeam iTeam : a.getTeams()) {
                    for (IGenerator o : iTeam.getGenerators()) {
                        o.updateHolograms(e.getPlayer(), iso);
                    }
                }
                for (ShopHolo shopHolo : ShopHolo.getShopHolo()) {
                    if (shopHolo.getA() != a) continue;
                    shopHolo.updateForPlayer(e.getPlayer(), iso);
                }
                if (!a.getShowTime().isEmpty()) {
                    for (Map.Entry entry : a.getShowTime().entrySet()) {
                        if ((Integer)entry.getValue() <= 1) continue;
                        BedWars.nms.hideArmor((Player)entry.getKey(), e.getPlayer());
                    }
                    if (a.getShowTime().containsKey(e.getPlayer())) {
                        for (Player player : a.getPlayers()) {
                            BedWars.nms.hideArmor(e.getPlayer(), player);
                        }
                    }
                    if (a.getShowTime().containsKey(e.getPlayer())) {
                        for (Player player : a.getSpectators()) {
                            BedWars.nms.hideArmor(e.getPlayer(), player);
                        }
                    }
                }
            }
            if (a.isSpectator(e.getPlayer()) || a.isReSpawning(e.getPlayer())) {
                if (e.getTo().getY() < 0.0) {
                    TeleportManager.teleportC((Entity)e.getPlayer(), a.isSpectator(e.getPlayer()) ? a.getSpectatorLocation() : a.getReSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                    e.getPlayer().setAllowFlight(true);
                    e.getPlayer().setFlying(true);
                }
            } else if (a.getStatus() == GameState.playing) {
                if (e.getPlayer().getLocation().getBlockY() <= a.getYKillHeight()) {
                    BedWars.nms.voidKill(e.getPlayer());
                }
                for (ITeam t : a.getTeams()) {
                    if (e.getPlayer().getLocation().distance(t.getBed()) < 4.0) {
                        if (!t.isMember(e.getPlayer()) || !(t instanceof BedWarsTeam) || ((BedWarsTeam)t).getBedHolo(e.getPlayer()) == null || ((BedWarsTeam)t).getBedHolo(e.getPlayer()).isHidden()) continue;
                        ((BedWarsTeam)t).getBedHolo(e.getPlayer()).hide();
                        continue;
                    }
                    if (!t.isMember(e.getPlayer()) || !(t instanceof BedWarsTeam) || ((BedWarsTeam)t).getBedHolo(e.getPlayer()) == null || !((BedWarsTeam)t).getBedHolo(e.getPlayer()).isHidden()) continue;
                    ((BedWarsTeam)t).getBedHolo(e.getPlayer()).show();
                }
                if (e.getFrom() != e.getTo()) {
                    Arena.afkCheck.remove(e.getPlayer().getUniqueId());
                    BedWars.getAPI().getAFKUtil().setPlayerAFK(e.getPlayer(), false);
                }
            } else if (e.getPlayer().getLocation().getBlockY() <= 0) {
                ITeam bwt = a.getTeam(e.getPlayer());
                if (bwt != null) {
                    TeleportManager.teleport((Entity)e.getPlayer(), bwt.getSpawn());
                } else {
                    TeleportManager.teleport((Entity)e.getPlayer(), a.getSpectatorLocation());
                }
            }
        } else if (BedWars.config.getBoolean("lobby-settings.void-tp") && e.getPlayer().getWorld().getName().equalsIgnoreCase(BedWars.config.getLobbyWorldName()) && BedWars.getServerType() == ServerType.MULTIARENA && e.getTo().getY() < (double)BedWars.config.getInt("lobby-settings.void-height")) {
            TeleportManager.teleportC((Entity)e.getPlayer(), BedWars.config.getConfigLoc("lobbyLoc"), PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
    }

    @EventHandler
    public void onProjHit(ProjectileHitEvent e) {
        IArena a;
        Projectile proj = e.getEntity();
        if (proj == null) {
            return;
        }
        if (e.getEntity().getShooter() instanceof Player && (a = Arena.getArenaByPlayer((Player)e.getEntity().getShooter())) != null) {
            if (!a.isPlayer((Player)e.getEntity().getShooter())) {
                return;
            }
            String utility = "";
            if (proj instanceof Snowball) {
                utility = "silverfish";
            }
            if (!utility.isEmpty()) {
                DamageDeathMove.spawnUtility(utility, e.getEntity().getLocation(), a.getTeam((Player)e.getEntity().getShooter()), (Player)e.getEntity().getShooter());
            }
        }
    }

    @EventHandler
    public void onItemFrameDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity().getType() == EntityType.ITEM_FRAME) {
            IArena a = Arena.getArenaByIdentifier(e.getEntity().getWorld().getName());
            if (a != null) {
                e.setCancelled(true);
            }
            if (BedWars.getServerType() == ServerType.MULTIARENA && BedWars.getLobbyWorld().equals(e.getEntity().getWorld().getName())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (Arena.getArenaByIdentifier(e.getEntity().getLocation().getWorld().getName()) != null && (e.getEntityType() == EntityType.IRON_GOLEM || e.getEntityType() == EntityType.SILVERFISH)) {
            e.getDrops().clear();
            e.setDroppedExp(0);
        }
        BedWars.nms.getDespawnablesList().remove(e.getEntity().getUniqueId());
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() == BedWars.nms.materialCake() && Arena.getArenaByIdentifier(e.getPlayer().getWorld().getName()) != null) {
            e.setCancelled(true);
        }
    }

    private static void spawnUtility(String s, Location loc, ITeam t, Player p) {
        if ("silverfish".equalsIgnoreCase(s)) {
            BedWars.nms.spawnSilverfish(loc, t, BedWars.shop.getYml().getDouble("shop-specials.silverfish.speed"), BedWars.shop.getYml().getDouble("shop-specials.silverfish.health"), BedWars.shop.getInt("shop-specials.silverfish.despawn"), BedWars.shop.getYml().getDouble("shop-specials.silverfish.damage"));
        }
    }
}

