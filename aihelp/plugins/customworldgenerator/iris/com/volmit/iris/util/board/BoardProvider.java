package com.volmit.iris.util.board;

import java.util.List;
import org.bukkit.entity.Player;

public interface BoardProvider {
   String getTitle(Player player);

   List<String> getLines(Player player);
}
