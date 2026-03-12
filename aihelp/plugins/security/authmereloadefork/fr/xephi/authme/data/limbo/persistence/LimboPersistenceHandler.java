package fr.xephi.authme.data.limbo.persistence;

import fr.xephi.authme.data.limbo.LimboPlayer;
import org.bukkit.entity.Player;

interface LimboPersistenceHandler {
   LimboPlayer getLimboPlayer(Player var1);

   void saveLimboPlayer(Player var1, LimboPlayer var2);

   void removeLimboPlayer(Player var1);

   LimboPersistenceType getType();
}
