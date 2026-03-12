package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public class WrapperPlayClientEntityAction extends PacketWrapper<WrapperPlayClientEntityAction> {
   private int entityID;
   private WrapperPlayClientEntityAction.Action action;
   private int jumpBoost;

   public WrapperPlayClientEntityAction(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientEntityAction(int entityID, WrapperPlayClientEntityAction.Action action, int jumpBoost) {
      super((PacketTypeCommon)PacketType.Play.Client.ENTITY_ACTION);
      this.entityID = entityID;
      this.action = action;
      this.jumpBoost = jumpBoost;
   }

   public void read() {
      boolean v1_8 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8);
      this.entityID = v1_8 ? this.readVarInt() : this.readInt();
      int id = v1_8 ? this.readVarInt() : this.readByte();
      this.action = WrapperPlayClientEntityAction.Action.getById(this.serverVersion, id);
      this.jumpBoost = v1_8 ? this.readVarInt() : this.readInt();
   }

   public void write() {
      boolean v1_8 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8);
      int actionIndex;
      if (v1_8) {
         this.writeVarInt(this.entityID);
         actionIndex = this.action.getId(this.serverVersion);
         this.writeVarInt(actionIndex);
         this.writeVarInt(this.jumpBoost);
      } else {
         this.writeInt(this.entityID);
         actionIndex = this.action.getId(this.serverVersion);
         this.writeByte(actionIndex);
         this.writeInt(this.jumpBoost);
      }

   }

   public void copy(WrapperPlayClientEntityAction wrapper) {
      this.entityID = wrapper.entityID;
      this.action = wrapper.action;
      this.jumpBoost = wrapper.jumpBoost;
   }

   public int getEntityId() {
      return this.entityID;
   }

   public void setEntityId(int entityID) {
      this.entityID = entityID;
   }

   public WrapperPlayClientEntityAction.Action getAction() {
      return this.action;
   }

   public void setAction(WrapperPlayClientEntityAction.Action action) {
      this.action = action;
   }

   public int getJumpBoost() {
      return this.jumpBoost;
   }

   public void setJumpBoost(int jumpBoost) {
      this.jumpBoost = jumpBoost;
   }

   public static enum Action {
      @ApiStatus.Obsolete
      START_SNEAKING {
         public int getId(ServerVersion version) {
            return version.isNewerThanOrEquals(ServerVersion.V_1_21_6) ? -1 : (version.isOlderThanOrEquals(ServerVersion.V_1_7_10) ? 1 : this.ordinal());
         }
      },
      @ApiStatus.Obsolete
      STOP_SNEAKING {
         public int getId(ServerVersion version) {
            return version.isNewerThanOrEquals(ServerVersion.V_1_21_6) ? -1 : (version.isOlderThanOrEquals(ServerVersion.V_1_7_10) ? 2 : this.ordinal());
         }
      },
      LEAVE_BED {
         public int getId(ServerVersion version) {
            if (version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
               return this.ordinal() - 2;
            } else {
               return version.isOlderThanOrEquals(ServerVersion.V_1_7_10) ? 3 : this.ordinal();
            }
         }
      },
      START_SPRINTING {
         public int getId(ServerVersion version) {
            if (version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
               return this.ordinal() - 2;
            } else {
               return version.isOlderThanOrEquals(ServerVersion.V_1_7_10) ? 4 : this.ordinal();
            }
         }
      },
      STOP_SPRINTING {
         public int getId(ServerVersion version) {
            if (version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
               return this.ordinal() - 2;
            } else {
               return version.isOlderThanOrEquals(ServerVersion.V_1_7_10) ? 5 : this.ordinal();
            }
         }
      },
      START_JUMPING_WITH_HORSE {
         public int getId(ServerVersion version) {
            if (version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
               return this.ordinal() - 2;
            } else {
               return version.isOlderThanOrEquals(ServerVersion.V_1_7_10) ? (version.isOlderThanOrEquals(ServerVersion.V_1_7_5) ? -1 : 6) : this.ordinal();
            }
         }
      },
      STOP_JUMPING_WITH_HORSE {
         public int getId(ServerVersion version) {
            if (version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
               return this.ordinal() - 2;
            } else {
               return version.isNewerThanOrEquals(ServerVersion.V_1_9) ? this.ordinal() : -1;
            }
         }
      },
      OPEN_HORSE_INVENTORY {
         public int getId(ServerVersion version) {
            if (version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
               return this.ordinal() - 2;
            } else {
               return version.isNewerThanOrEquals(ServerVersion.V_1_9) ? this.ordinal() : this.ordinal() - 1;
            }
         }
      },
      START_FLYING_WITH_ELYTRA {
         public int getId(ServerVersion version) {
            if (version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
               return this.ordinal() - 2;
            } else {
               return version.isNewerThanOrEquals(ServerVersion.V_1_9) ? this.ordinal() : -1;
            }
         }
      };

      private static final WrapperPlayClientEntityAction.Action[] VALUES = values();

      private Action() {
      }

      public abstract int getId(ServerVersion version);

      public static WrapperPlayClientEntityAction.Action getById(ServerVersion version, int id) {
         WrapperPlayClientEntityAction.Action[] var2 = VALUES;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WrapperPlayClientEntityAction.Action action = var2[var4];
            if (action.getId(version) == id) {
               return action;
            }
         }

         throw new IllegalStateException("Invalid entity action id " + id + " for " + version);
      }

      // $FF: synthetic method
      private static WrapperPlayClientEntityAction.Action[] $values() {
         return new WrapperPlayClientEntityAction.Action[]{START_SNEAKING, STOP_SNEAKING, LEAVE_BED, START_SPRINTING, STOP_SPRINTING, START_JUMPING_WITH_HORSE, STOP_JUMPING_WITH_HORSE, OPEN_HORSE_INVENTORY, START_FLYING_WITH_ELYTRA};
      }

      // $FF: synthetic method
      Action(Object x2) {
         this();
      }
   }
}
