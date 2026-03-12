package ac.grim.grimac.api;

public interface PacketWorld {
   int getBlockStateId(int var1, int var2, int var3);

   boolean isChunkLoaded(int var1, int var2);
}
