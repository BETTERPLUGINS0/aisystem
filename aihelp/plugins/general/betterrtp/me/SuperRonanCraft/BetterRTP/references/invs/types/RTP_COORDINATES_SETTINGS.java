package me.SuperRonanCraft.BetterRTP.references.invs.types;

import java.util.ArrayList;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import me.SuperRonanCraft.BetterRTP.references.invs.SETTINGS_TYPE;

enum RTP_COORDINATES_SETTINGS {
   CENTER_Z(SETTINGS_TYPE.INTEGER, FileOther.FILETYPE.CONFIG, "Settings.Importance.Enabled", new Object[]{true, "Center Z", "&7Toggle Categories system", "paper"}),
   CENTER_X(SETTINGS_TYPE.INTEGER, FileOther.FILETYPE.CONFIG, "Settings.Cooldown.Enabled", new Object[]{true, "Center X", "&7Toggle Cooldown system", "paper"}),
   USE_WORLDBORDER(SETTINGS_TYPE.BOOLEAN, FileOther.FILETYPE.CONFIG, "Settings.Cooldown.Time", new Object[]{true, "Cooldown Time", new ArrayList<String>() {
      {
         this.add("&7Set the time (in minutes)");
         this.add("&7between making a new ticket");
      }
   }, "paper"});

   SETTINGS_TYPE type;
   FileOther.FILETYPE filetype;
   String path;
   String[] condition = null;
   Object[] info;

   private RTP_COORDINATES_SETTINGS(SETTINGS_TYPE type, FileOther.FILETYPE filetype, String path, Object[] info) {
      this.type = type;
      this.filetype = filetype;
      this.path = path;
      this.info = info;
   }

   private RTP_COORDINATES_SETTINGS(SETTINGS_TYPE type, FileOther.FILETYPE filetype, String[] arry, Object[] info) {
      this.type = type;
      this.filetype = filetype;
      this.path = null;
      this.info = info;
      this.condition = arry;
   }

   void setValue(Object value) {
      BetterRTP.getInstance().getFiles().getType(this.filetype).setValue(this.path, value);
   }

   public Object[] getInfo() {
      return this.info;
   }

   public Object getValue() {
      String path = this.path;
      if (path == null && this.condition != null) {
         if (BetterRTP.getInstance().getFiles().getType(this.filetype).getBoolean(this.condition[0])) {
            path = this.condition[1];
         } else {
            path = this.condition[2];
         }
      }

      return this.getValuePath(path);
   }

   private Object getValuePath(String path) {
      if (path != null) {
         if (this.getType() == SETTINGS_TYPE.BOOLEAN) {
            return BetterRTP.getInstance().getFiles().getType(this.filetype).getBoolean(path);
         }

         if (this.getType() == SETTINGS_TYPE.STRING) {
            return BetterRTP.getInstance().getFiles().getType(this.filetype).getString(path);
         }

         if (this.getType() == SETTINGS_TYPE.INTEGER) {
            return BetterRTP.getInstance().getFiles().getType(this.filetype).getInt(path);
         }
      }

      return null;
   }

   public SETTINGS_TYPE getType() {
      return this.type;
   }

   public FileOther.FILETYPE getFiletype() {
      return this.filetype;
   }

   // $FF: synthetic method
   private static RTP_COORDINATES_SETTINGS[] $values() {
      return new RTP_COORDINATES_SETTINGS[]{CENTER_Z, CENTER_X, USE_WORLDBORDER};
   }
}
