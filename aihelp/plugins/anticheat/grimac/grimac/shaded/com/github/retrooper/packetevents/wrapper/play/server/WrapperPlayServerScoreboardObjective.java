package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.score.ScoreFormat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.LegacyFormat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Locale;

public class WrapperPlayServerScoreboardObjective extends PacketWrapper<WrapperPlayServerScoreboardObjective> {
   private String name;
   private WrapperPlayServerScoreboardObjective.ObjectiveMode mode;
   private Component displayName;
   @Nullable
   private WrapperPlayServerScoreboardObjective.RenderType renderType;
   @Nullable
   private ScoreFormat scoreFormat;

   public WrapperPlayServerScoreboardObjective(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerScoreboardObjective(String name, WrapperPlayServerScoreboardObjective.ObjectiveMode mode, Component displayName, @Nullable WrapperPlayServerScoreboardObjective.RenderType renderType) {
      this(name, mode, displayName, renderType, (ScoreFormat)null);
   }

   public WrapperPlayServerScoreboardObjective(String name, WrapperPlayServerScoreboardObjective.ObjectiveMode mode, Component displayName, @Nullable WrapperPlayServerScoreboardObjective.RenderType renderType, @Nullable ScoreFormat scoreFormat) {
      super((PacketTypeCommon)PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
      this.name = name;
      this.mode = mode;
      this.displayName = displayName;
      this.renderType = renderType;
      this.scoreFormat = scoreFormat;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
         this.name = this.readString();
      } else {
         this.name = this.readString(16);
      }

      this.mode = WrapperPlayServerScoreboardObjective.ObjectiveMode.getById(this.readByte());
      if (this.mode != WrapperPlayServerScoreboardObjective.ObjectiveMode.CREATE && this.mode != WrapperPlayServerScoreboardObjective.ObjectiveMode.UPDATE) {
         this.displayName = Component.empty();
         this.renderType = WrapperPlayServerScoreboardObjective.RenderType.INTEGER;
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
            this.scoreFormat = null;
         }
      } else if (this.serverVersion.isOlderThan(ServerVersion.V_1_13)) {
         this.displayName = this.getSerializers().fromLegacy(this.readString(32));
         this.renderType = WrapperPlayServerScoreboardObjective.RenderType.getByName(this.readString());
      } else {
         this.displayName = this.readComponent();
         this.renderType = WrapperPlayServerScoreboardObjective.RenderType.getById(this.readVarInt());
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
            this.scoreFormat = (ScoreFormat)this.readOptional(ScoreFormat::readTyped);
         }
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
         this.writeString(this.name);
      } else {
         this.writeString(this.name, 16);
      }

      this.writeByte((byte)this.mode.ordinal());
      if (this.mode == WrapperPlayServerScoreboardObjective.ObjectiveMode.CREATE || this.mode == WrapperPlayServerScoreboardObjective.ObjectiveMode.UPDATE) {
         if (this.serverVersion.isOlderThan(ServerVersion.V_1_13)) {
            String legacyText = this.getSerializers().asLegacy(this.displayName);
            this.writeString(LegacyFormat.trimLegacyFormat(legacyText, 32));
            if (this.renderType != null) {
               this.writeString(this.renderType.name().toLowerCase(Locale.ROOT));
            } else {
               this.writeString(WrapperPlayServerScoreboardObjective.RenderType.INTEGER.name().toLowerCase(Locale.ROOT));
            }
         } else {
            this.writeComponent(this.displayName);
            if (this.renderType != null) {
               this.writeVarInt(this.renderType.ordinal());
            } else {
               this.writeVarInt(WrapperPlayServerScoreboardObjective.RenderType.INTEGER.ordinal());
            }

            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
               this.writeOptional(this.scoreFormat, ScoreFormat::writeTyped);
            }
         }
      }

   }

   public void copy(WrapperPlayServerScoreboardObjective wrapper) {
      this.name = wrapper.name;
      this.mode = wrapper.mode;
      this.displayName = wrapper.displayName;
      this.renderType = wrapper.renderType;
      this.scoreFormat = wrapper.scoreFormat;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public WrapperPlayServerScoreboardObjective.ObjectiveMode getMode() {
      return this.mode;
   }

   public void setMode(WrapperPlayServerScoreboardObjective.ObjectiveMode mode) {
      this.mode = mode;
   }

   public Component getDisplayName() {
      return this.displayName;
   }

   public void setDisplayName(@Nullable Component displayName) {
      this.displayName = displayName;
   }

   @Nullable
   public WrapperPlayServerScoreboardObjective.RenderType getRenderType() {
      return this.renderType;
   }

   public void setRenderType(@Nullable WrapperPlayServerScoreboardObjective.RenderType renderType) {
      this.renderType = renderType;
   }

   @Nullable
   public ScoreFormat getScoreFormat() {
      return this.scoreFormat;
   }

   public void setScoreFormat(@Nullable ScoreFormat scoreFormat) {
      this.scoreFormat = scoreFormat;
   }

   public static enum ObjectiveMode {
      CREATE,
      REMOVE,
      UPDATE;

      private static final WrapperPlayServerScoreboardObjective.ObjectiveMode[] VALUES = values();

      @Nullable
      public static WrapperPlayServerScoreboardObjective.ObjectiveMode getByName(String name) {
         WrapperPlayServerScoreboardObjective.ObjectiveMode[] var1 = VALUES;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            WrapperPlayServerScoreboardObjective.ObjectiveMode mode = var1[var3];
            if (mode.name().equalsIgnoreCase(name)) {
               return mode;
            }
         }

         return null;
      }

      public static WrapperPlayServerScoreboardObjective.ObjectiveMode getById(int id) {
         return VALUES[id];
      }

      // $FF: synthetic method
      private static WrapperPlayServerScoreboardObjective.ObjectiveMode[] $values() {
         return new WrapperPlayServerScoreboardObjective.ObjectiveMode[]{CREATE, REMOVE, UPDATE};
      }
   }

   public static enum RenderType {
      INTEGER,
      HEARTS;

      private static final WrapperPlayServerScoreboardObjective.RenderType[] VALUES = values();

      @Nullable
      public static WrapperPlayServerScoreboardObjective.RenderType getByName(String name) {
         WrapperPlayServerScoreboardObjective.RenderType[] var1 = VALUES;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            WrapperPlayServerScoreboardObjective.RenderType display = var1[var3];
            if (display.name().equalsIgnoreCase(name)) {
               return display;
            }
         }

         return null;
      }

      @Nullable
      public static WrapperPlayServerScoreboardObjective.RenderType getById(int id) {
         return VALUES[id];
      }

      // $FF: synthetic method
      private static WrapperPlayServerScoreboardObjective.RenderType[] $values() {
         return new WrapperPlayServerScoreboardObjective.RenderType[]{INTEGER, HEARTS};
      }
   }
}
