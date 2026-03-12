package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;

// $FF: synthetic class
class Vulcan_ex {
   static final int[] Vulcan_g = new int[Client.values().length];

   static {
      try {
         Vulcan_g[Client.PLAYER_FLYING.ordinal()] = 1;
      } catch (NoSuchFieldError var17) {
      }

      try {
         Vulcan_g[Client.PLAYER_POSITION.ordinal()] = 2;
      } catch (NoSuchFieldError var16) {
      }

      try {
         Vulcan_g[Client.PLAYER_ROTATION.ordinal()] = 3;
      } catch (NoSuchFieldError var15) {
      }

      try {
         Vulcan_g[Client.PLAYER_POSITION_AND_ROTATION.ordinal()] = 4;
      } catch (NoSuchFieldError var14) {
      }

      try {
         Vulcan_g[Client.CLIENT_TICK_END.ordinal()] = 5;
      } catch (NoSuchFieldError var13) {
      }

      try {
         Vulcan_g[Client.PONG.ordinal()] = 6;
      } catch (NoSuchFieldError var12) {
      }

      try {
         Vulcan_g[Client.WINDOW_CONFIRMATION.ordinal()] = 7;
      } catch (NoSuchFieldError var11) {
      }

      try {
         Vulcan_g[Client.ENTITY_ACTION.ordinal()] = 8;
      } catch (NoSuchFieldError var10) {
      }

      try {
         Vulcan_g[Client.PLAYER_DIGGING.ordinal()] = 9;
      } catch (NoSuchFieldError var9) {
      }

      try {
         Vulcan_g[Client.HELD_ITEM_CHANGE.ordinal()] = 10;
      } catch (NoSuchFieldError var8) {
      }

      try {
         Vulcan_g[Client.VEHICLE_MOVE.ordinal()] = 11;
      } catch (NoSuchFieldError var7) {
      }

      try {
         Vulcan_g[Client.CLIENT_STATUS.ordinal()] = 12;
      } catch (NoSuchFieldError var6) {
      }

      try {
         Vulcan_g[Client.KEEP_ALIVE.ordinal()] = 13;
      } catch (NoSuchFieldError var5) {
      }

      try {
         Vulcan_g[Client.PLAYER_BLOCK_PLACEMENT.ordinal()] = 14;
      } catch (NoSuchFieldError var4) {
      }

      try {
         Vulcan_g[Client.INTERACT_ENTITY.ordinal()] = 15;
      } catch (NoSuchFieldError var3) {
      }

      try {
         Vulcan_g[Client.CLICK_WINDOW.ordinal()] = 16;
      } catch (NoSuchFieldError var2) {
      }

      try {
         Vulcan_g[Client.ANIMATION.ordinal()] = 17;
      } catch (NoSuchFieldError var1) {
      }

   }
}
