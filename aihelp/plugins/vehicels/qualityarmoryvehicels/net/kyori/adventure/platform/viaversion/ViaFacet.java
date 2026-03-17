/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.api.Via
 *  com.viaversion.viaversion.api.connection.UserConnection
 *  com.viaversion.viaversion.api.protocol.Protocol
 *  com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType
 *  com.viaversion.viaversion.api.protocol.packet.PacketWrapper
 *  com.viaversion.viaversion.api.type.Type
 *  com.viaversion.viaversion.libs.gson.JsonElement
 *  com.viaversion.viaversion.libs.gson.JsonParser
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.platform.viaversion;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonParser;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import java.util.function.Function;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.platform.facet.Facet;
import net.kyori.adventure.platform.facet.FacetBase;
import net.kyori.adventure.platform.facet.Knob;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ViaFacet<V>
extends FacetBase<V>
implements Facet.Message<V, String> {
    private static final String PACKAGE = "com.viaversion.viaversion";
    private static final int SUPPORTED_VIA_MAJOR_VERSION = 4;
    private static final boolean SUPPORTED;
    private final Function<V, UserConnection> connectionFunction;
    private final int minProtocol;

    public ViaFacet(@NotNull Class<? extends V> clazz, @NotNull Function<V, UserConnection> function, int n) {
        super(clazz);
        this.connectionFunction = function;
        this.minProtocol = n;
    }

    @Override
    public boolean isSupported() {
        return super.isSupported() && SUPPORTED && this.connectionFunction != null && this.minProtocol >= 0;
    }

    @Override
    public boolean isApplicable(@NotNull V v) {
        return super.isApplicable(v) && this.minProtocol > Via.getAPI().getServerVersion().lowestSupportedVersion() && this.findProtocol(v) >= this.minProtocol;
    }

    @Nullable
    public UserConnection findConnection(@NotNull V v) {
        return this.connectionFunction.apply(v);
    }

    public int findProtocol(@NotNull V v) {
        UserConnection userConnection = this.findConnection(v);
        if (userConnection != null) {
            return userConnection.getProtocolInfo().getProtocolVersion();
        }
        return -1;
    }

    @Override
    @NotNull
    public String createMessage(@NotNull V v, @NotNull Component component) {
        int n = this.findProtocol(v);
        if (n >= 713) {
            return (String)GsonComponentSerializer.gson().serialize(component);
        }
        return (String)GsonComponentSerializer.colorDownsamplingGson().serialize(component);
    }

    static {
        boolean bl = false;
        try {
            Class.forName("com.viaversion.viaversion.api.ViaAPI").getDeclaredMethod("majorVersion", new Class[0]);
            bl = Via.getAPI().majorVersion() == 4;
        } catch (Throwable throwable) {
            // empty catch block
        }
        SUPPORTED = bl && Knob.isEnabled("viaversion", true);
    }

    public static final class TabList<V>
    extends ProtocolBased<V>
    implements Facet.TabList<V, String> {
        public TabList(@NotNull Class<? extends V> clazz, @NotNull Function<V, UserConnection> function) {
            super("1_16", "1_15_2", 713, "TAB_LIST", clazz, function);
        }

        @Override
        public void send(V v, @Nullable String string, @Nullable String string2) {
            PacketWrapper packetWrapper = this.createPacket(v);
            packetWrapper.write(Type.COMPONENT, (Object)this.parse(string));
            packetWrapper.write(Type.COMPONENT, (Object)this.parse(string2));
            this.sendPacket(packetWrapper);
        }
    }

    public static final class BossBar<V>
    extends ProtocolBased<V>
    implements Facet.BossBarPacket<V> {
        private final Set<V> viewers;
        private UUID id;
        private String title;
        private float health;
        private int color;
        private int overlay;
        private byte flags;

        private BossBar(@NotNull String string, @NotNull String string2, @NotNull Class<? extends V> clazz, @NotNull Function<V, UserConnection> function, Collection<V> collection) {
            super(string, string2, 356, "BOSSBAR", clazz, function);
            this.viewers = new CopyOnWriteArraySet<V>(collection);
        }

        @Override
        public void bossBarInitialized(@NotNull net.kyori.adventure.bossbar.BossBar bossBar) {
            Facet.BossBarPacket.super.bossBarInitialized(bossBar);
            this.id = UUID.randomUUID();
            this.broadcastPacket(0);
        }

        @Override
        public void bossBarNameChanged(@NotNull net.kyori.adventure.bossbar.BossBar bossBar, @NotNull Component component, @NotNull Component component2) {
            if (!this.viewers.isEmpty()) {
                this.title = this.createMessage((Object)this.viewers.iterator().next(), component2);
                this.broadcastPacket(3);
            }
        }

        @Override
        public void bossBarProgressChanged(@NotNull net.kyori.adventure.bossbar.BossBar bossBar, float f, float f2) {
            this.health = f2;
            this.broadcastPacket(2);
        }

        @Override
        public void bossBarColorChanged(@NotNull net.kyori.adventure.bossbar.BossBar bossBar, @NotNull BossBar.Color color, @NotNull BossBar.Color color2) {
            this.color = this.createColor(color2);
            this.broadcastPacket(4);
        }

        @Override
        public void bossBarOverlayChanged(@NotNull net.kyori.adventure.bossbar.BossBar bossBar, @NotNull BossBar.Overlay overlay, @NotNull BossBar.Overlay overlay2) {
            this.overlay = this.createOverlay(overlay2);
            this.broadcastPacket(4);
        }

        @Override
        public void bossBarFlagsChanged(@NotNull net.kyori.adventure.bossbar.BossBar bossBar, @NotNull Set<BossBar.Flag> set, @NotNull Set<BossBar.Flag> set2) {
            this.flags = this.createFlag(this.flags, set, set2);
            this.broadcastPacket(5);
        }

        public void sendPacket(@NotNull V v, int n) {
            PacketWrapper packetWrapper = this.createPacket(v);
            packetWrapper.write(Type.UUID, (Object)this.id);
            packetWrapper.write((Type)Type.VAR_INT, (Object)n);
            if (n == 0 || n == 3) {
                packetWrapper.write(Type.COMPONENT, (Object)this.parse(this.title));
            }
            if (n == 0 || n == 2) {
                packetWrapper.write((Type)Type.FLOAT, (Object)Float.valueOf(this.health));
            }
            if (n == 0 || n == 4) {
                packetWrapper.write((Type)Type.VAR_INT, (Object)this.color);
                packetWrapper.write((Type)Type.VAR_INT, (Object)this.overlay);
            }
            if (n == 0 || n == 5) {
                packetWrapper.write((Type)Type.BYTE, (Object)this.flags);
            }
            this.sendPacket(packetWrapper);
        }

        public void broadcastPacket(int n) {
            if (this.isEmpty()) {
                return;
            }
            for (V v : this.viewers) {
                this.sendPacket(v, n);
            }
        }

        @Override
        public void addViewer(@NotNull V v) {
            if (this.viewers.add(v)) {
                this.sendPacket(v, 0);
            }
        }

        @Override
        public void removeViewer(@NotNull V v) {
            if (this.viewers.remove(v)) {
                this.sendPacket(v, 1);
            }
        }

        @Override
        public boolean isEmpty() {
            return this.id == null || this.viewers.isEmpty();
        }

        @Override
        public void close() {
            this.broadcastPacket(1);
            this.viewers.clear();
        }

        public static class Builder1_9_To_1_15<V>
        extends ViaFacet<V>
        implements Facet.BossBar.Builder<V, Facet.BossBar<V>> {
            public Builder1_9_To_1_15(@NotNull Class<? extends V> clazz, @NotNull Function<V, UserConnection> function) {
                super(clazz, function, 356);
            }

            @Override
            public @NotNull Facet.BossBar<V> createBossBar(@NotNull Collection<V> collection) {
                return new BossBar("1_9", "1_8", this.viewerClass, this::findConnection, collection);
            }
        }

        public static class Builder<V>
        extends ViaFacet<V>
        implements Facet.BossBar.Builder<V, Facet.BossBar<V>> {
            public Builder(@NotNull Class<? extends V> clazz, @NotNull Function<V, UserConnection> function) {
                super(clazz, function, 713);
            }

            @Override
            public @NotNull Facet.BossBar<V> createBossBar(@NotNull Collection<V> collection) {
                return new BossBar("1_16", "1_15_2", this.viewerClass, this::findConnection, collection);
            }
        }
    }

    public static class Title<V>
    extends ProtocolBased<V>
    implements Facet.TitlePacket<V, String, List<Consumer<PacketWrapper>>, Consumer<V>> {
        protected Title(@NotNull String string, @NotNull String string2, int n, @NotNull Class<? extends V> clazz, @NotNull Function<V, UserConnection> function) {
            super(string, string2, n, "TITLE", clazz, function);
        }

        public Title(@NotNull Class<? extends V> clazz, @NotNull Function<V, UserConnection> function) {
            this("1_16", "1_15_2", 713, clazz, function);
        }

        @Override
        @NotNull
        public List<Consumer<PacketWrapper>> createTitleCollection() {
            return new ArrayList<Consumer<PacketWrapper>>();
        }

        @Override
        public void contributeTitle(@NotNull List<Consumer<PacketWrapper>> list, @NotNull String string) {
            list.add(packetWrapper -> {
                packetWrapper.write((Type)Type.VAR_INT, (Object)0);
                packetWrapper.write(Type.COMPONENT, (Object)this.parse(string));
            });
        }

        @Override
        public void contributeSubtitle(@NotNull List<Consumer<PacketWrapper>> list, @NotNull String string) {
            list.add(packetWrapper -> {
                packetWrapper.write((Type)Type.VAR_INT, (Object)1);
                packetWrapper.write(Type.COMPONENT, (Object)this.parse(string));
            });
        }

        @Override
        public void contributeTimes(@NotNull List<Consumer<PacketWrapper>> list, int n, int n2, int n3) {
            list.add(packetWrapper -> {
                packetWrapper.write((Type)Type.VAR_INT, (Object)3);
                packetWrapper.write((Type)Type.INT, (Object)n);
                packetWrapper.write((Type)Type.INT, (Object)n2);
                packetWrapper.write((Type)Type.INT, (Object)n3);
            });
        }

        @Override
        @Nullable
        public Consumer<V> completeTitle(@NotNull List<Consumer<PacketWrapper>> list) {
            return object -> {
                int n = list.size();
                for (int i = 0; i < n; ++i) {
                    PacketWrapper packetWrapper = this.createPacket(object);
                    ((Consumer)list.get(i)).accept(packetWrapper);
                    this.sendPacket(packetWrapper);
                }
            };
        }

        @Override
        public void showTitle(@NotNull V v, @NotNull Consumer<V> consumer) {
            consumer.accept(v);
        }

        @Override
        public void clearTitle(@NotNull V v) {
            PacketWrapper packetWrapper = this.createPacket(v);
            packetWrapper.write((Type)Type.VAR_INT, (Object)4);
            this.sendPacket(packetWrapper);
        }

        @Override
        public void resetTitle(@NotNull V v) {
            PacketWrapper packetWrapper = this.createPacket(v);
            packetWrapper.write((Type)Type.VAR_INT, (Object)5);
            this.sendPacket(packetWrapper);
        }
    }

    public static class ActionBarTitle<V>
    extends ProtocolBased<V>
    implements Facet.ActionBar<V, String> {
        public ActionBarTitle(@NotNull Class<? extends V> clazz, @NotNull Function<V, UserConnection> function) {
            super("1_11", "1_10", 310, "TITLE", clazz, function);
        }

        @Override
        public void sendMessage(@NotNull V v, @NotNull String string) {
            PacketWrapper packetWrapper = this.createPacket(v);
            packetWrapper.write((Type)Type.VAR_INT, (Object)2);
            packetWrapper.write(Type.COMPONENT, (Object)this.parse(string));
            this.sendPacket(packetWrapper);
        }
    }

    public static class ActionBar<V>
    extends Chat<V>
    implements Facet.ActionBar<V, String> {
        public ActionBar(@NotNull Class<? extends V> clazz, @NotNull Function<V, UserConnection> function) {
            super(clazz, function);
        }

        @Override
        public byte createMessageType(@NotNull MessageType messageType) {
            return 2;
        }

        @Override
        public void sendMessage(@NotNull V v, @NotNull String string) {
            this.sendMessage(v, Identity.nil(), string, (Object)MessageType.CHAT);
        }
    }

    public static class Chat<V>
    extends ProtocolBased<V>
    implements Facet.ChatPacket<V, String> {
        public Chat(@NotNull Class<? extends V> clazz, @NotNull Function<V, UserConnection> function) {
            super("1_16", "1_15_2", 713, "CHAT_MESSAGE", clazz, function);
        }

        @Override
        public void sendMessage(@NotNull V v, @NotNull Identity identity, @NotNull String string, @NotNull Object object) {
            PacketWrapper packetWrapper = this.createPacket(v);
            packetWrapper.write(Type.COMPONENT, (Object)this.parse(string));
            packetWrapper.write((Type)Type.BYTE, (Object)this.createMessageType(object instanceof MessageType ? (MessageType)((Object)object) : MessageType.SYSTEM));
            packetWrapper.write(Type.UUID, (Object)identity.uuid());
            this.sendPacket(packetWrapper);
        }
    }

    public static class ProtocolBased<V>
    extends ViaFacet<V> {
        private final Class<? extends Protocol<?, ?, ?, ?>> protocolClass;
        private final Class<? extends ClientboundPacketType> packetClass;
        private final int packetId;

        protected ProtocolBased(@NotNull String string, @NotNull String string2, int n, @NotNull String string3, @NotNull Class<? extends V> clazz, @NotNull Function<V, UserConnection> function) {
            super(clazz, function, n);
            String string4 = MessageFormat.format("{0}.protocols.protocol{1}to{2}.Protocol{1}To{2}", ViaFacet.PACKAGE, string, string2);
            String string5 = MessageFormat.format("{0}.protocols.protocol{1}to{2}.ClientboundPackets{1}", ViaFacet.PACKAGE, string, string2);
            Class<?> clazz2 = null;
            Class<?> clazz3 = null;
            int n2 = -1;
            try {
                clazz2 = Class.forName(string4);
                clazz3 = Class.forName(string5);
                for (ClientboundPacketType clientboundPacketType : (ClientboundPacketType[])clazz3.getEnumConstants()) {
                    if (!clientboundPacketType.getName().equals(string3)) continue;
                    n2 = clientboundPacketType.getId();
                    break;
                }
            } catch (Throwable throwable) {
                // empty catch block
            }
            this.protocolClass = clazz2;
            this.packetClass = clazz3;
            this.packetId = n2;
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && this.protocolClass != null && this.packetClass != null && this.packetId >= 0;
        }

        public PacketWrapper createPacket(@NotNull V v) {
            return PacketWrapper.create((int)this.packetId, null, (UserConnection)this.findConnection(v));
        }

        public void sendPacket(@NotNull PacketWrapper packetWrapper) {
            if (packetWrapper.user() == null) {
                return;
            }
            try {
                packetWrapper.scheduleSend(this.protocolClass);
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to send ViaVersion packet: %s %s", packetWrapper.user(), packetWrapper);
            }
        }

        @NotNull
        public JsonElement parse(@NotNull String string) {
            return JsonParser.parseString((String)string);
        }
    }
}

