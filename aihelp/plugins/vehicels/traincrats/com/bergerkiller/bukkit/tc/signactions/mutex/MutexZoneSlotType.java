package com.bergerkiller.bukkit.tc.signactions.mutex;

import com.bergerkiller.bukkit.tc.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public enum MutexZoneSlotType {
   NORMAL,
   SMART;

   private static final MutexZoneSlotType[] SLOT_TYPES = values();

   public static MutexZoneSlotType readFrom(InputStream stream) throws IOException {
      int typeOrd = Util.readVariableLengthInt(stream);
      return typeOrd >= 0 && typeOrd < SLOT_TYPES.length ? SLOT_TYPES[typeOrd] : NORMAL;
   }

   public void writeTo(OutputStream stream) throws IOException {
      Util.writeVariableLengthInt(stream, this.ordinal());
   }

   // $FF: synthetic method
   private static MutexZoneSlotType[] $values() {
      return new MutexZoneSlotType[]{NORMAL, SMART};
   }
}
