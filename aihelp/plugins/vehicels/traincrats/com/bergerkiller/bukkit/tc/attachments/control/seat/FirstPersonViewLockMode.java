package com.bergerkiller.bukkit.tc.attachments.control.seat;

public enum FirstPersonViewLockMode {
   OFF("attachments/view_lock_off.png", "No view locking", false),
   MOVE("attachments/view_lock_move.png", "Move view when\nseat moves (Choppy!)", false),
   SPECTATOR_FREE("attachments/view_lock_spectator_free.png", "Free look in\nSpectator mode", true),
   SPECTATOR_LOCKED("attachments/view_lock_spectator.png", "View locked in\nSpectator mode", true);

   private final String _icon;
   private final String _tooltip;
   private final boolean _spectator;

   private FirstPersonViewLockMode(String icon, String tooltip, boolean spectator) {
      this._icon = icon;
      this._tooltip = tooltip;
      this._spectator = spectator;
   }

   public String getIconPath() {
      return this._icon;
   }

   public String getTooltip() {
      return this._tooltip;
   }

   public boolean isSpectator() {
      return this._spectator;
   }

   // $FF: synthetic method
   private static FirstPersonViewLockMode[] $values() {
      return new FirstPersonViewLockMode[]{OFF, MOVE, SPECTATOR_FREE, SPECTATOR_LOCKED};
   }
}
