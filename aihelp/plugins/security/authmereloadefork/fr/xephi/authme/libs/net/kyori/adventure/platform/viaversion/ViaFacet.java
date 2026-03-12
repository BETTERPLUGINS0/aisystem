package fr.xephi.authme.libs.net.kyori.adventure.platform.viaversion;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonParser;
import fr.xephi.authme.libs.io.netty.buffer.ByteBuf;
import fr.xephi.authme.libs.net.kyori.adventure.audience.MessageType;
import fr.xephi.authme.libs.net.kyori.adventure.identity.Identity;
import fr.xephi.authme.libs.net.kyori.adventure.platform.facet.Facet;
import fr.xephi.authme.libs.net.kyori.adventure.platform.facet.FacetBase;
import fr.xephi.authme.libs.net.kyori.adventure.platform.facet.Knob;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ViaFacet<V> extends FacetBase<V> implements Facet.Message<V, String> {
   private static final String PACKAGE = "com.viaversion.viaversion";
   private static final int SUPPORTED_VIA_MAJOR_VERSION = 4;
   private static final boolean SUPPORTED;
   private final Function<V, UserConnection> connectionFunction;
   private final int minProtocol;

   public ViaFacet(@NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction, final int minProtocol) {
      super(viewerClass);
      this.connectionFunction = connectionFunction;
      this.minProtocol = minProtocol;
   }

   public boolean isSupported() {
      return super.isSupported() && SUPPORTED && this.connectionFunction != null && this.minProtocol >= 0;
   }

   public boolean isApplicable(@NotNull final V viewer) {
      return super.isApplicable(viewer) && this.minProtocol > Via.getAPI().getServerVersion().lowestSupportedVersion() && this.findProtocol(viewer) >= this.minProtocol;
   }

   @Nullable
   public UserConnection findConnection(@NotNull final V viewer) {
      return (UserConnection)this.connectionFunction.apply(viewer);
   }

   public int findProtocol(@NotNull final V viewer) {
      UserConnection connection = this.findConnection(viewer);
      return connection != null ? connection.getProtocolInfo().getProtocolVersion() : -1;
   }

   @NotNull
   public String createMessage(@NotNull final V viewer, @NotNull final Component message) {
      int protocol = this.findProtocol(viewer);
      return protocol >= 713 ? (String)GsonComponentSerializer.gson().serialize(message) : (String)GsonComponentSerializer.colorDownsamplingGson().serialize(message);
   }

   static {
      boolean supported = false;

      try {
         Class.forName("com.viaversion.viaversion.api.ViaAPI").getDeclaredMethod("majorVersion");
         supported = Via.getAPI().majorVersion() == 4;
      } catch (Throwable var2) {
      }

      SUPPORTED = supported && Knob.isEnabled("viaversion", true);
   }

   public static final class TabList<V> extends ViaFacet.ProtocolBased<V> implements Facet.TabList<V, String> {
      public TabList(@NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> userConnection) {
         super("1_16", "1_15_2", 713, "TAB_LIST", viewerClass, userConnection);
      }

      public void send(final V viewer, @Nullable final String header, @Nullable final String footer) {
         PacketWrapper packet = this.createPacket(viewer);
         packet.write(Type.COMPONENT, this.parse(header));
         packet.write(Type.COMPONENT, this.parse(footer));
         this.sendPacket(packet);
      }
   }

   public static final class BossBar<V> extends ViaFacet.ProtocolBased<V> implements Facet.BossBarPacket<V> {
      private final Set<V> viewers;
      private UUID id;
      private String title;
      private float health;
      private int color;
      private int overlay;
      private byte flags;

      private BossBar(@NotNull final String fromProtocol, @NotNull final String toProtocol, @NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction, final Collection<V> viewers) {
         super(fromProtocol, toProtocol, 356, "BOSSBAR", viewerClass, connectionFunction);
         this.viewers = new CopyOnWriteArraySet(viewers);
      }

      public void bossBarInitialized(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar bar) {
         Facet.BossBarPacket.super.bossBarInitialized(bar);
         this.id = UUID.randomUUID();
         this.broadcastPacket(0);
      }

      public void bossBarNameChanged(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar bar, @NotNull final Component oldName, @NotNull final Component newName) {
         if (!this.viewers.isEmpty()) {
            this.title = this.createMessage(this.viewers.iterator().next(), newName);
            this.broadcastPacket(3);
         }

      }

      public void bossBarProgressChanged(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar bar, final float oldPercent, final float newPercent) {
         this.health = newPercent;
         this.broadcastPacket(2);
      }

      public void bossBarColorChanged(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar bar, @NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color oldColor, @NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color newColor) {
         this.color = this.createColor(newColor);
         this.broadcastPacket(4);
      }

      public void bossBarOverlayChanged(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar bar, @NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Overlay oldOverlay, @NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Overlay newOverlay) {
         this.overlay = this.createOverlay(newOverlay);
         this.broadcastPacket(4);
      }

      public void bossBarFlagsChanged(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar bar, @NotNull final Set<fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag> flagsAdded, @NotNull final Set<fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag> flagsRemoved) {
         this.flags = this.createFlag(this.flags, flagsAdded, flagsRemoved);
         this.broadcastPacket(5);
      }

      public void sendPacket(@NotNull final V viewer, final int action) {
         PacketWrapper packet = this.createPacket(viewer);
         packet.write(Type.UUID, this.id);
         packet.write(Type.VAR_INT, action);
         if (action == 0 || action == 3) {
            packet.write(Type.COMPONENT, this.parse(this.title));
         }

         if (action == 0 || action == 2) {
            packet.write(Type.FLOAT, this.health);
         }

         if (action == 0 || action == 4) {
            packet.write(Type.VAR_INT, this.color);
            packet.write(Type.VAR_INT, this.overlay);
         }

         if (action == 0 || action == 5) {
            packet.write(Type.BYTE, this.flags);
         }

         this.sendPacket(packet);
      }

      public void broadcastPacket(final int action) {
         if (!this.isEmpty()) {
            Iterator var2 = this.viewers.iterator();

            while(var2.hasNext()) {
               V viewer = var2.next();
               this.sendPacket(viewer, action);
            }

         }
      }

      public void addViewer(@NotNull final V viewer) {
         if (this.viewers.add(viewer)) {
            this.sendPacket(viewer, 0);
         }

      }

      public void removeViewer(@NotNull final V viewer) {
         if (this.viewers.remove(viewer)) {
            this.sendPacket(viewer, 1);
         }

      }

      public boolean isEmpty() {
         return this.id == null || this.viewers.isEmpty();
      }

      public void close() {
         this.broadcastPacket(1);
         this.viewers.clear();
      }

      // $FF: synthetic method
      BossBar(String x0, String x1, Class x2, Function x3, Collection x4, Object x5) {
         this(x0, x1, x2, x3, x4);
      }

      public static class Builder1_9_To_1_15<V> extends ViaFacet<V> implements Facet.BossBar.Builder<V, Facet.BossBar<V>> {
         public Builder1_9_To_1_15(@NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction) {
            super(viewerClass, connectionFunction, 356);
         }

         @NotNull
         public Facet.BossBar<V> createBossBar(@NotNull final Collection<V> viewer) {
            return new ViaFacet.BossBar("1_9", "1_8", this.viewerClass, this::findConnection, viewer);
         }
      }

      public static class Builder<V> extends ViaFacet<V> implements Facet.BossBar.Builder<V, Facet.BossBar<V>> {
         public Builder(@NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction) {
            super(viewerClass, connectionFunction, 713);
         }

         @NotNull
         public Facet.BossBar<V> createBossBar(@NotNull final Collection<V> viewer) {
            return new ViaFacet.BossBar("1_16", "1_15_2", this.viewerClass, this::findConnection, viewer);
         }
      }
   }

   public static class Title<V> extends ViaFacet.ProtocolBased<V> implements Facet.TitlePacket<V, String, List<Consumer<PacketWrapper>>, Consumer<V>> {
      protected Title(@NotNull final String fromProtocol, @NotNull final String toProtocol, final int minProtocol, @NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction) {
         super(fromProtocol, toProtocol, minProtocol, "TITLE", viewerClass, connectionFunction);
      }

      public Title(@NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction) {
         this("1_16", "1_15_2", 713, viewerClass, connectionFunction);
      }

      @NotNull
      public List<Consumer<PacketWrapper>> createTitleCollection() {
         return new ArrayList();
      }

      public void contributeTitle(@NotNull final List<Consumer<PacketWrapper>> coll, @NotNull final String title) {
         coll.add((packet) -> {
            packet.write(Type.VAR_INT, 0);
            packet.write(Type.COMPONENT, this.parse(title));
         });
      }

      public void contributeSubtitle(@NotNull final List<Consumer<PacketWrapper>> coll, @NotNull final String subtitle) {
         coll.add((packet) -> {
            packet.write(Type.VAR_INT, 1);
            packet.write(Type.COMPONENT, this.parse(subtitle));
         });
      }

      public void contributeTimes(@NotNull final List<Consumer<PacketWrapper>> coll, final int inTicks, final int stayTicks, final int outTicks) {
         coll.add((packet) -> {
            packet.write(Type.VAR_INT, 3);
            packet.write(Type.INT, inTicks);
            packet.write(Type.INT, stayTicks);
            packet.write(Type.INT, outTicks);
         });
      }

      @Nullable
      public Consumer<V> completeTitle(@NotNull final List<Consumer<PacketWrapper>> coll) {
         return (v) -> {
            int i = 0;

            for(int length = coll.size(); i < length; ++i) {
               PacketWrapper pkt = this.createPacket(v);
               ((Consumer)coll.get(i)).accept(pkt);
               this.sendPacket(pkt);
            }

         };
      }

      public void showTitle(@NotNull final V viewer, @NotNull final Consumer<V> title) {
         title.accept(viewer);
      }

      public void clearTitle(@NotNull final V viewer) {
         PacketWrapper packet = this.createPacket(viewer);
         packet.write(Type.VAR_INT, 4);
         this.sendPacket(packet);
      }

      public void resetTitle(@NotNull final V viewer) {
         PacketWrapper packet = this.createPacket(viewer);
         packet.write(Type.VAR_INT, 5);
         this.sendPacket(packet);
      }
   }

   public static class ActionBarTitle<V> extends ViaFacet.ProtocolBased<V> implements Facet.ActionBar<V, String> {
      public ActionBarTitle(@NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction) {
         super("1_11", "1_10", 310, "TITLE", viewerClass, connectionFunction);
      }

      public void sendMessage(@NotNull final V viewer, @NotNull final String message) {
         PacketWrapper packet = this.createPacket(viewer);
         packet.write(Type.VAR_INT, 2);
         packet.write(Type.COMPONENT, this.parse(message));
         this.sendPacket(packet);
      }
   }

   public static class ActionBar<V> extends ViaFacet.Chat<V> implements Facet.ActionBar<V, String> {
      public ActionBar(@NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction) {
         super(viewerClass, connectionFunction);
      }

      public byte createMessageType(@NotNull final MessageType type) {
         return 2;
      }

      public void sendMessage(@NotNull final V viewer, @NotNull final String message) {
         this.sendMessage(viewer, Identity.nil(), message, MessageType.CHAT);
      }
   }

   public static class Chat<V> extends ViaFacet.ProtocolBased<V> implements Facet.ChatPacket<V, String> {
      public Chat(@NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction) {
         super("1_16", "1_15_2", 713, "CHAT_MESSAGE", viewerClass, connectionFunction);
      }

      public void sendMessage(@NotNull final V viewer, @NotNull final Identity source, @NotNull final String message, @NotNull final Object type) {
         PacketWrapper packet = this.createPacket(viewer);
         packet.write(Type.COMPONENT, this.parse(message));
         packet.write(Type.BYTE, this.createMessageType(type instanceof MessageType ? (MessageType)type : MessageType.SYSTEM));
         packet.write(Type.UUID, source.uuid());
         this.sendPacket(packet);
      }
   }

   public static class ProtocolBased<V> extends ViaFacet<V> {
      private final Class<? extends Protocol<?, ?, ?, ?>> protocolClass;
      private final Class<? extends ClientboundPacketType> packetClass;
      private final int packetId;

      protected ProtocolBased(@NotNull final String fromProtocol, @NotNull final String toProtocol, final int minProtocol, @NotNull final String packetName, @NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction) {
         super(viewerClass, connectionFunction, minProtocol);
         String protocolClassName = MessageFormat.format("{0}.protocols.protocol{1}to{2}.Protocol{1}To{2}", "com.viaversion.viaversion", fromProtocol, toProtocol);
         String packetClassName = MessageFormat.format("{0}.protocols.protocol{1}to{2}.ClientboundPackets{1}", "com.viaversion.viaversion", fromProtocol, toProtocol);
         Class<? extends Protocol<?, ?, ?, ?>> protocolClass = null;
         Class<? extends ClientboundPacketType> packetClass = null;
         int packetId = -1;

         try {
            protocolClass = Class.forName(protocolClassName);
            packetClass = Class.forName(packetClassName);
            ClientboundPacketType[] var12 = (ClientboundPacketType[])packetClass.getEnumConstants();
            int var13 = var12.length;

            for(int var14 = 0; var14 < var13; ++var14) {
               ClientboundPacketType type = var12[var14];
               if (type.getName().equals(packetName)) {
                  packetId = type.getId();
                  break;
               }
            }
         } catch (Throwable var16) {
         }

         this.protocolClass = protocolClass;
         this.packetClass = packetClass;
         this.packetId = packetId;
      }

      public boolean isSupported() {
         return super.isSupported() && this.protocolClass != null && this.packetClass != null && this.packetId >= 0;
      }

      public PacketWrapper createPacket(@NotNull final V viewer) {
         return PacketWrapper.create(this.packetId, (ByteBuf)null, this.findConnection(viewer));
      }

      public void sendPacket(@NotNull final PacketWrapper packet) {
         if (packet.user() != null) {
            try {
               packet.scheduleSend(this.protocolClass);
            } catch (Throwable var3) {
               Knob.logError(var3, "Failed to send ViaVersion packet: %s %s", packet.user(), packet);
            }

         }
      }

      @NotNull
      public JsonElement parse(@NotNull final String message) {
         return JsonParser.parseString(message);
      }
   }
}
