package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Server;

// $FF: synthetic class
class Vulcan_ii {
   static final int[] Vulcan_T = new int[Server.values().length];

   static {
      try {
         Vulcan_T[Server.JOIN_GAME.ordinal()] = 1;
      } catch (NoSuchFieldError var16) {
      }

      try {
         Vulcan_T[Server.PING.ordinal()] = 2;
      } catch (NoSuchFieldError var15) {
      }

      try {
         Vulcan_T[Server.WINDOW_CONFIRMATION.ordinal()] = 3;
      } catch (NoSuchFieldError var14) {
      }

      try {
         Vulcan_T[Server.ENTITY_VELOCITY.ordinal()] = 4;
      } catch (NoSuchFieldError var13) {
      }

      try {
         Vulcan_T[Server.EXPLOSION.ordinal()] = 5;
      } catch (NoSuchFieldError var12) {
      }

      try {
         Vulcan_T[Server.PLAYER_POSITION_AND_LOOK.ordinal()] = 6;
      } catch (NoSuchFieldError var11) {
      }

      try {
         Vulcan_T[Server.ENTITY_METADATA.ordinal()] = 7;
      } catch (NoSuchFieldError var10) {
      }

      try {
         Vulcan_T[Server.UPDATE_ATTRIBUTES.ordinal()] = 8;
      } catch (NoSuchFieldError var9) {
      }

      try {
         Vulcan_T[Server.PLAYER_ABILITIES.ordinal()] = 9;
      } catch (NoSuchFieldError var8) {
      }

      try {
         Vulcan_T[Server.HELD_ITEM_CHANGE.ordinal()] = 10;
      } catch (NoSuchFieldError var7) {
      }

      try {
         Vulcan_T[Server.ENTITY_EFFECT.ordinal()] = 11;
      } catch (NoSuchFieldError var6) {
      }

      try {
         Vulcan_T[Server.REMOVE_ENTITY_EFFECT.ordinal()] = 12;
      } catch (NoSuchFieldError var5) {
      }

      try {
         Vulcan_T[Server.CHANGE_GAME_STATE.ordinal()] = 13;
      } catch (NoSuchFieldError var4) {
      }

      try {
         Vulcan_T[Server.ENTITY_RELATIVE_MOVE.ordinal()] = 14;
      } catch (NoSuchFieldError var3) {
      }

      try {
         Vulcan_T[Server.ENTITY_RELATIVE_MOVE_AND_ROTATION.ordinal()] = 15;
      } catch (NoSuchFieldError var2) {
      }

      try {
         Vulcan_T[Server.ENTITY_ANIMATION.ordinal()] = 16;
      } catch (NoSuchFieldError var1) {
      }

   }
}
