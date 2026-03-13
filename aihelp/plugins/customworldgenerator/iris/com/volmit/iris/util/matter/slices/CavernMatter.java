package com.volmit.iris.util.matter.slices;

import com.volmit.iris.util.data.palette.Palette;
import com.volmit.iris.util.matter.MatterCavern;
import com.volmit.iris.util.matter.Sliced;
import java.io.DataInputStream;
import java.io.DataOutputStream;

@Sliced
public class CavernMatter extends RawMatter<MatterCavern> {
   public static final MatterCavern EMPTY = new MatterCavern(false, "", (byte)0);
   public static final MatterCavern BASIC = new MatterCavern(true, "", (byte)0);

   public CavernMatter() {
      this(1, 1, 1);
   }

   public CavernMatter(int width, int height, int depth) {
      super(var1, var2, var3, MatterCavern.class);
   }

   public static MatterCavern get(String customBiome, int liquid) {
      return new MatterCavern(true, var0, (byte)var1);
   }

   public Palette<MatterCavern> getGlobalPalette() {
      return null;
   }

   public void writeNode(MatterCavern b, DataOutputStream dos) {
      var2.writeBoolean(var1.isCavern());
      var2.writeUTF(var1.getCustomBiome());
      var2.writeByte(var1.getLiquid());
   }

   public MatterCavern readNode(DataInputStream din) {
      boolean var2 = var1.readBoolean();
      String var3 = var1.readUTF();
      byte var4 = var1.readByte();
      return new MatterCavern(var2, var3, var4);
   }
}
