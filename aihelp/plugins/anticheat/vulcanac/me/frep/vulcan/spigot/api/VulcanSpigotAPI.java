package me.frep.vulcan.spigot.api;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import me.frep.vulcan.api.VulcanAPI;
import me.frep.vulcan.api.check.Check;
import me.frep.vulcan.api.data.IPlayerData;
import me.frep.vulcan.spigot.Vulcan_Xr;
import me.frep.vulcan.spigot.Vulcan_Xs;
import me.frep.vulcan.spigot.Vulcan_bQ;
import me.frep.vulcan.spigot.Vulcan_eG;
import me.frep.vulcan.spigot.Vulcan_i9;
import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.Vulcan_n;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.manager.CheckManager;
import org.bukkit.entity.Player;

public class VulcanSpigotAPI implements VulcanAPI {
   private static final long a = Vulcan_n.a(-3977536220833843823L, 2470505366641992099L, MethodHandles.lookup().lookupClass()).a(81792516377456L);

   public IPlayerData getPlayerData(Player var1) {
      return Vulcan_Xs.INSTANCE.Vulcan_e().Vulcan_Z(new Object[]{var1});
   }

   public int getPing(Player var1) {
      return Vulcan_bQ.Vulcan_R(new Object[]{var1});
   }

   public void executeBanWave() {
      Vulcan_Xr.Vulcan_b(new Object[0]);
   }

   public boolean isFrozen(Player var1) {
      return ((Vulcan_iE)this.getPlayerData(var1)).Vulcan_e(new Object[0]).Vulcan_lV(new Object[0]);
   }

   public double getKurtosis(Player var1) {
      return ((Vulcan_iE)this.getPlayerData(var1)).Vulcan_B(new Object[0]).Vulcan_l(new Object[0]);
   }

   public void setFrozen(Player var1, boolean var2) {
      Vulcan_iE var3 = (Vulcan_iE)this.getPlayerData(var1);

      try {
         if (var3 == null) {
            return;
         }
      } catch (RuntimeException var4) {
         throw a(var4);
      }

      var3.Vulcan_e(new Object[0]).Vulcan_Vx(new Object[]{var2});
   }

   public int getSensitivity(Player var1) {
      return ((Vulcan_iE)this.getPlayerData(var1)).Vulcan_w(new Object[0]).Vulcan_P(new Object[0]);
   }

   public double getCps(Player var1) {
      return ((Vulcan_iE)this.getPlayerData(var1)).Vulcan_B(new Object[0]).Vulcan_M(new Object[0]);
   }

   public int getTransactionPing(Player var1) {
      return (int)((Vulcan_iE)this.getPlayerData(var1)).Vulcan_P(new Object[0]).Vulcan_e(new Object[0]);
   }

   public int getTotalViolations(Player var1) {
      return this.getPlayerData(var1).getTotalViolations();
   }

   public int getCombatViolations(Player var1) {
      return this.getPlayerData(var1).getCombatViolations();
   }

   public int getMovementViolations(Player var1) {
      return this.getPlayerData(var1).getMovementViolations();
   }

   public int getPlayerViolations(Player var1) {
      return this.getPlayerData(var1).getPlayerViolations();
   }

   public double getTps() {
      long var1 = a ^ 60362790972243L;
      long var3 = var1 ^ 107092214201114L;
      return Vulcan_eG.Vulcan_X(new Object[]{var3});
   }

   public int getTicks() {
      return Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]);
   }

   public int getJoinTicks(Player var1) {
      return this.getPlayerData(var1).getJoinTicks();
   }

   public String getVulcanVersion() {
      return Vulcan_Xs.INSTANCE.Vulcan_J().getDescription().getVersion();
   }

   public Check getCheck(Player param1, String param2, char param3) {
      // $FF: Couldn't be decompiled
   }

   public Map getCheckData() {
      return Vulcan_i9.Vulcan_FS;
   }

   public boolean hasAlertsEnabled(Player var1) {
      return Vulcan_Xs.INSTANCE.Vulcan_y().Vulcan_K(new Object[0]).contains(var1);
   }

   public boolean isCheckEnabled(String var1) {
      Iterator var2 = CheckManager.Vulcan__.iterator();

      while(var2.hasNext()) {
         Constructor var3 = (Constructor)var2.next();

         try {
            if (var3.getClass().getSimpleName().equalsIgnoreCase(var1)) {
               return true;
            }
         } catch (RuntimeException var4) {
            throw a(var4);
         }
      }

      return false;
   }

   public String getServerVersion() {
      return Vulcan_eG.Vulcan_t(new Object[0]).toString();
   }

   public Set getChecks() {
      return Vulcan_i9.Vulcan_FU.keySet();
   }

   public Map getEnabledChecks() {
      return Vulcan_i9.Vulcan_FU;
   }

   public Map getMaxViolations() {
      return Vulcan_i9.Vulcan_a1;
   }

   public Map getAlertIntervals() {
      return Vulcan_i9.Vulcan_Qf;
   }

   public Map getMinimumViolationsToNotify() {
      return Vulcan_i9.Vulcan_dW;
   }

   public Map getPunishmentCommands() {
      return Vulcan_i9.Vulcan_Fz;
   }

   public Map getPunishableChecks() {
      return Vulcan_i9.Vulcan_ht;
   }

   public Map getBroadcastPunishments() {
      return Vulcan_i9.Vulcan_d;
   }

   public Map getMaximumPings() {
      return Vulcan_i9.Vulcan_aN;
   }

   public Map getMinimumTps() {
      return Vulcan_i9.Vulcan_dc;
   }

   public Map getMaxBuffers() {
      return Vulcan_i9.Vulcan_QB;
   }

   public Map getBufferDecays() {
      return Vulcan_i9.Vulcan_Q7;
   }

   public Map getBufferMultiples() {
      return Vulcan_i9.Vulcan_FV;
   }

   public Map getHotbarShuffle() {
      return Vulcan_i9.Vulcan_hu;
   }

   public Map getHotbarShuffleMinimums() {
      return Vulcan_i9.Vulcan_d3;
   }

   public Map getHotbarShuffleIntervals() {
      return Vulcan_i9.Vulcan_dB;
   }

   public Map getRandomRotation() {
      return Vulcan_i9.Vulcan_aZ;
   }

   public Map getRandomRotationMinimums() {
      return Vulcan_i9.Vulcan_Q3;
   }

   public Map getRandomRotationIntervals() {
      return Vulcan_i9.Vulcan_dr;
   }

   public List getChecks(Player var1) {
      return (List)((Vulcan_iE)this.getPlayerData(var1)).Vulcan_N(new Object[0]).stream().map(VulcanSpigotAPI::lambda$getChecks$0).collect(Collectors.toList());
   }

   public void toggleAlerts(Player var1) {
      long var2 = a ^ 120469078291835L;
      long var4 = var2 ^ 65161560100675L;
      Vulcan_Xs.INSTANCE.Vulcan_y().Vulcan_U(new Object[]{var1, var4});
   }

   public void toggleVerbose(Player var1) {
      long var2 = a ^ 42934717491602L;
      long var4 = var2 ^ 122735507810276L;
      Vulcan_Xs.INSTANCE.Vulcan_y().Vulcan_t(new Object[]{var4, var1});
   }

   public void flag(Player var1, String var2, String var3, String var4) {
      long var5 = a ^ 103388446195022L;
      long var7 = var5 ^ 125709060137508L;
      Vulcan_Xs.INSTANCE.Vulcan_y().Vulcan_p(new Object[]{var1, var2, var3, var7, var4});
   }

   private static Check lambda$getChecks$0(AbstractCheck var0) {
      return var0;
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
