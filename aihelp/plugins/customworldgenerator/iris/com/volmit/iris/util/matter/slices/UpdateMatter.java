package com.volmit.iris.util.matter.slices;

import com.volmit.iris.util.data.palette.GlobalPalette;
import com.volmit.iris.util.data.palette.Palette;
import com.volmit.iris.util.matter.MatterUpdate;
import com.volmit.iris.util.matter.Sliced;
import java.io.DataInputStream;
import java.io.DataOutputStream;

@Sliced
public class UpdateMatter extends RawMatter<MatterUpdate> {
   public static final MatterUpdate ON = new MatterUpdate(true);
   public static final MatterUpdate OFF = new MatterUpdate(false);
   private static final Palette<MatterUpdate> GLOBAL;

   public UpdateMatter() {
      this(1, 1, 1);
   }

   public UpdateMatter(int width, int height, int depth) {
      super(var1, var2, var3, MatterUpdate.class);
   }

   public Palette<MatterUpdate> getGlobalPalette() {
      return GLOBAL;
   }

   public void writeNode(MatterUpdate b, DataOutputStream dos) {
      var2.writeBoolean(var1.isUpdate());
   }

   public MatterUpdate readNode(DataInputStream din) {
      return var1.readBoolean() ? ON : OFF;
   }

   static {
      GLOBAL = new GlobalPalette(new MatterUpdate[]{OFF, ON});
   }
}
