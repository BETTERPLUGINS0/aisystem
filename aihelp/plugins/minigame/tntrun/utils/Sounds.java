package tntrun.utils;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class Sounds {
   public abstract void NOTE_PLING(Player var1, float var2, float var3);

   public abstract void ARENA_START(Player var1);

   public abstract void ITEM_SELECT(Player var1);

   public abstract void INVITE_MESSAGE(Player var1);

   public abstract void BLOCK_BREAK(Block var1);
}
