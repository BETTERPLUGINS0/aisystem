package com.volmit.iris.util.matter.slices;

import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.data.palette.Palette;
import com.volmit.iris.util.matter.MatterMarker;
import com.volmit.iris.util.matter.Sliced;
import java.io.DataInputStream;
import java.io.DataOutputStream;

@Sliced
public class MarkerMatter extends RawMatter<MatterMarker> {
   public static final MatterMarker NONE = new MatterMarker("none");
   public static final MatterMarker CAVE_FLOOR = new MatterMarker("cave_floor");
   public static final MatterMarker CAVE_CEILING = new MatterMarker("cave_ceiling");
   private static final KMap<String, MatterMarker> markers = new KMap();

   public MarkerMatter() {
      this(1, 1, 1);
   }

   public MarkerMatter(int width, int height, int depth) {
      super(var1, var2, var3, MatterMarker.class);
   }

   public Palette<MatterMarker> getGlobalPalette() {
      return null;
   }

   public void writeNode(MatterMarker b, DataOutputStream dos) {
      var2.writeUTF(var1.getTag());
   }

   public MatterMarker readNode(DataInputStream din) {
      return (MatterMarker)markers.computeIfAbsent(var1.readUTF(), MatterMarker::new);
   }
}
