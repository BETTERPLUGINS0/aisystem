package me.frep.vulcan.api;

import java.util.List;
import java.util.Map;
import java.util.Set;
import me.frep.vulcan.api.check.Check;
import me.frep.vulcan.api.data.IPlayerData;
import org.bukkit.entity.Player;

public interface VulcanAPI {
   void toggleAlerts(Player var1);

   void toggleVerbose(Player var1);

   IPlayerData getPlayerData(Player var1);

   int getPing(Player var1);

   double getKurtosis(Player var1);

   int getTransactionPing(Player var1);

   int getSensitivity(Player var1);

   void executeBanWave();

   void setFrozen(Player var1, boolean var2);

   double getCps(Player var1);

   int getTotalViolations(Player var1);

   boolean isFrozen(Player var1);

   int getMovementViolations(Player var1);

   int getPlayerViolations(Player var1);

   int getCombatViolations(Player var1);

   double getTps();

   String getServerVersion();

   Map getCheckData();

   int getJoinTicks(Player var1);

   boolean hasAlertsEnabled(Player var1);

   Check getCheck(Player var1, String var2, char var3);

   String getVulcanVersion();

   boolean isCheckEnabled(String var1);

   int getTicks();

   List getChecks(Player var1);

   Map getEnabledChecks();

   Map getMaxViolations();

   Map getAlertIntervals();

   Map getMinimumViolationsToNotify();

   Map getPunishmentCommands();

   Map getPunishableChecks();

   Map getBroadcastPunishments();

   Map getMaximumPings();

   Map getMinimumTps();

   Map getMaxBuffers();

   Map getBufferDecays();

   Map getBufferMultiples();

   Map getHotbarShuffle();

   Map getHotbarShuffleMinimums();

   Map getHotbarShuffleIntervals();

   Map getRandomRotation();

   Map getRandomRotationMinimums();

   Map getRandomRotationIntervals();

   Set getChecks();

   void flag(Player var1, String var2, String var3, String var4);
}
