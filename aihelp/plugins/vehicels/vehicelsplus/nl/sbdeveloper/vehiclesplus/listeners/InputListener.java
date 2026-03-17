/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelDuplexHandler
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelHandlerContext
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitTask
 */
package nl.sbdeveloper.vehiclesplus.listeners;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.api.events.impl.KeyPressEvent;
import nl.sbdeveloper.vehiclesplus.api.vehicles.movement.MovementInput;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.XReflection;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.minecraft.MinecraftConnection;
import nl.sbdeveloper.vehiclesplus.utils.nms.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class InputListener
implements Listener {
    private static final Class<?> packetPlayInSteerVehicle = XReflection.getNMSClass("network.protocol.game", "PacketPlayInSteerVehicle");
    private static final Class<?> playerCommonConnection;
    private static final boolean IS_1_21_4_PLUS;
    private final Map<UUID, MovementInput> playerInputStates = new ConcurrentHashMap<UUID, MovementInput>();
    private BukkitTask globalTask;
    private final JavaPlugin plugin = VehiclesPlus.getInstance();

    public InputListener() {
        if (IS_1_21_4_PLUS) {
            this.globalTask = Bukkit.getScheduler().runTaskTimer((Plugin)this.plugin, () -> {
                if (this.playerInputStates.isEmpty()) {
                    return;
                }
                this.playerInputStates.entrySet().removeIf(entry -> {
                    Player player = Bukkit.getPlayer((UUID)((UUID)entry.getKey()));
                    if (player != null && player.isOnline() && player.getVehicle() != null) {
                        KeyPressEvent keyPressEvent = new KeyPressEvent(player, (MovementInput)entry.getValue());
                        Bukkit.getPluginManager().callEvent((Event)keyPressEvent);
                        return false;
                    }
                    return true;
                });
            }, 0L, 1L);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent playerJoinEvent) {
        this.injectPlayer(playerJoinEvent.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        UUID uUID = playerQuitEvent.getPlayer().getUniqueId();
        this.playerInputStates.remove(uUID);
        this.removePlayer(playerQuitEvent.getPlayer());
    }

    private void injectPlayer(final Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler(){

            public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) {
                if (object.getClass().isAssignableFrom(packetPlayInSteerVehicle)) {
                    MovementInput movementInput;
                    Object object2;
                    Object obj = packetPlayInSteerVehicle.cast(object);
                    if (IS_1_21_4_PLUS) {
                        object2 = ReflectionUtil.callDeclaredMethod(obj, "b", new Object[0]);
                        movementInput = new MovementInput((Boolean)ReflectionUtil.getDeclaredField(object2, "c"), (Boolean)ReflectionUtil.getDeclaredField(object2, "e"), (Boolean)ReflectionUtil.getDeclaredField(object2, "d"), (Boolean)ReflectionUtil.getDeclaredField(object2, "f"), (Boolean)ReflectionUtil.getDeclaredField(object2, "g"), (Boolean)ReflectionUtil.getDeclaredField(object2, "h"));
                    } else {
                        float f = ((Float)ReflectionUtil.getDeclaredField(obj, XReflection.supports(20, 5) ? "e" : (XReflection.supports(17) ? "d" : "b"))).floatValue();
                        float f2 = ((Float)ReflectionUtil.getDeclaredField(obj, XReflection.supports(20, 5) ? "d" : (XReflection.supports(17) ? "c" : "a"))).floatValue();
                        boolean bl = (Boolean)ReflectionUtil.getDeclaredField(obj, XReflection.supports(20, 5) ? "f" : (XReflection.supports(17) ? "e" : "c"));
                        boolean bl2 = (Boolean)ReflectionUtil.getDeclaredField(obj, XReflection.supports(20, 5) ? "g" : (XReflection.supports(17) ? "f" : "d"));
                        movementInput = new MovementInput(f > 0.0f, f2 > 0.0f, f < 0.0f, f2 < 0.0f, bl, bl2);
                    }
                    if (IS_1_21_4_PLUS) {
                        InputListener.this.playerInputStates.put(player.getUniqueId(), movementInput);
                    }
                    object2 = new KeyPressEvent(player, movementInput);
                    InputListener.this.plugin.getServer().getScheduler().runTask((Plugin)InputListener.this.plugin, () -> this.lambda$channelRead$0((KeyPressEvent)((Object)object2)));
                }
                super.channelRead(channelHandlerContext, object);
            }

            private /* synthetic */ void lambda$channelRead$0(KeyPressEvent keyPressEvent) {
                InputListener.this.plugin.getServer().getPluginManager().callEvent((Event)keyPressEvent);
            }
        };
        this.getChannel(player).pipeline().addBefore("packet_handler", player.getName(), (ChannelHandler)channelDuplexHandler);
    }

    private void removePlayer(Player player) {
        Channel channel = this.getChannel(player);
        channel.eventLoop().submit(() -> channel.pipeline().remove(player.getName()));
    }

    public void cleanup() {
        if (this.globalTask != null) {
            this.globalTask.cancel();
            this.globalTask = null;
        }
        this.playerInputStates.clear();
    }

    private Channel getChannel(Player player) {
        Object object = MinecraftConnection.getHandle(player);
        Object object2 = ReflectionUtil.getDeclaredField(object, XReflection.supports(21, 5) ? "g" : (XReflection.supports(21, 2) ? "f" : (XReflection.supports(20) ? "c" : (XReflection.supports(17) ? "b" : "playerConnection"))));
        Object object3 = ReflectionUtil.getDeclaredField(playerCommonConnection, object2, XReflection.supports(20, 5) ? "e" : (XReflection.supports(20, 2) ? "c" : (XReflection.supports(19, 3) ? "h" : (XReflection.supports(19) ? "b" : (XReflection.supports(17) ? "a" : "networkManager")))));
        return (Channel)ReflectionUtil.getDeclaredField(object3, XReflection.supports(21, 10) ? "k" : (XReflection.supports(20, 2) ? "n" : (XReflection.supports(18) ? "m" : (XReflection.supports(17) ? "k" : "channel"))));
    }

    static {
        IS_1_21_4_PLUS = XReflection.supports(21, 3);
        playerCommonConnection = XReflection.supports(20, 2) ? XReflection.getNMSClass("server.network", "ServerCommonPacketListenerImpl") : XReflection.getNMSClass("server.network", "PlayerConnection");
    }
}

