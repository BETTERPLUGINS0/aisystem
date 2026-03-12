package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockBoundingBox;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DebugStructureInfos {
   private final List<DebugStructureInfos.DebugStructureInfo> infos;

   public DebugStructureInfos(List<DebugStructureInfos.DebugStructureInfo> infos) {
      this.infos = infos;
   }

   public static DebugStructureInfos read(PacketWrapper<?> wrapper) {
      List<DebugStructureInfos.DebugStructureInfo> infos = wrapper.readList(DebugStructureInfos.DebugStructureInfo::read);
      return new DebugStructureInfos(infos);
   }

   public static void write(PacketWrapper<?> wrapper, DebugStructureInfos infos) {
      wrapper.writeList(infos.infos, DebugStructureInfos.DebugStructureInfo::write);
   }

   public List<DebugStructureInfos.DebugStructureInfo> getInfos() {
      return this.infos;
   }

   public static final class DebugStructureInfo {
      private final BlockBoundingBox boundingBox;
      private final List<DebugStructureInfos.DebugStructureInfo.Piece> pieces;

      public DebugStructureInfo(BlockBoundingBox boundingBox, List<DebugStructureInfos.DebugStructureInfo.Piece> pieces) {
         this.boundingBox = boundingBox;
         this.pieces = pieces;
      }

      public static DebugStructureInfos.DebugStructureInfo read(PacketWrapper<?> wrapper) {
         BlockBoundingBox box = BlockBoundingBox.read(wrapper);
         List<DebugStructureInfos.DebugStructureInfo.Piece> pieces = wrapper.readList(DebugStructureInfos.DebugStructureInfo.Piece::read);
         return new DebugStructureInfos.DebugStructureInfo(box, pieces);
      }

      public static void write(PacketWrapper<?> wrapper, DebugStructureInfos.DebugStructureInfo info) {
         BlockBoundingBox.write(wrapper, info.boundingBox);
         wrapper.writeList(info.pieces, DebugStructureInfos.DebugStructureInfo.Piece::write);
      }

      public BlockBoundingBox getBoundingBox() {
         return this.boundingBox;
      }

      public List<DebugStructureInfos.DebugStructureInfo.Piece> getPieces() {
         return this.pieces;
      }

      public static final class Piece {
         private final BlockBoundingBox boundingBox;
         private final boolean start;

         public Piece(BlockBoundingBox boundingBox, boolean start) {
            this.boundingBox = boundingBox;
            this.start = start;
         }

         public static DebugStructureInfos.DebugStructureInfo.Piece read(PacketWrapper<?> wrapper) {
            BlockBoundingBox boundingBox = BlockBoundingBox.read(wrapper);
            boolean start = wrapper.readBoolean();
            return new DebugStructureInfos.DebugStructureInfo.Piece(boundingBox, start);
         }

         public static void write(PacketWrapper<?> wrapper, DebugStructureInfos.DebugStructureInfo.Piece piece) {
            BlockBoundingBox.write(wrapper, piece.boundingBox);
            wrapper.writeBoolean(piece.start);
         }

         public BlockBoundingBox getBoundingBox() {
            return this.boundingBox;
         }

         public boolean isStart() {
            return this.start;
         }
      }
   }
}
