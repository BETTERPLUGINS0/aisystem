package me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins;

import me.SuperRonanCraft.BetterRTP.references.settings.SoftDepends;

public enum REGIONPLUGINS {
   FACTIONSUUID("FactionsUUID", "Factions", new RTP_FactionsUUID()),
   GRIEFDEFENDER("GriefDefender", new RTP_GriefDefender()),
   GRIEFPREVENTION("GriefPrevention", new RTP_GriefPrevention()),
   HCLAIMS("hClaims", "hClaim", new RTP_hClaims()),
   HUSKTOWNS("HuskTowns", new RTP_HuskTowns()),
   KINGDOMSX("KingdomsX", "Kingdoms", new RTP_KingdomsX()),
   LANDS("Lands", new RTP_Lands()),
   PUEBLOS("Pueblos", new RTP_Pueblos()),
   REDPROTECT("RedProtect", new RTP_RedProtect()),
   RESIDENCE("Residence", new RTP_Residence()),
   SABERFACTIONS("SaberFactions", "Factions", new RTP_SaberFactions()),
   TOWNY("Towny", new RTP_Towny()),
   ULTIMATECLAIMS("UltimateClaims", new RTP_UltimateClaims()),
   WORLDGUARD("WorldGuard", new RTP_WorldGuard()),
   MINEPLOTS("MinePlots", new RTP_MinePlots()),
   FACTIONSBRIDGE("FactionsBridge", new RTP_FactionsBridge()),
   CRASH_CLAIM("CrashClaim", new RTP_CrashClaim());

   private final SoftDepends.RegionPlugin plugin;
   private final String setting_name;
   private final String pluginyml_name;
   private final RegionPluginCheck validator;

   private REGIONPLUGINS(String all_name, RegionPluginCheck validator) {
      this(all_name, all_name, validator);
   }

   private REGIONPLUGINS(String setting_name, String pluginyml_name, RegionPluginCheck validator) {
      this.plugin = new SoftDepends.RegionPlugin();
      this.setting_name = setting_name;
      this.pluginyml_name = pluginyml_name;
      this.validator = validator;
   }

   public boolean isEnabled() {
      return this.plugin.isEnabled();
   }

   public SoftDepends.RegionPlugin getPlugin() {
      return this.plugin;
   }

   public String getSetting_name() {
      return this.setting_name;
   }

   public String getPluginyml_name() {
      return this.pluginyml_name;
   }

   public RegionPluginCheck getValidator() {
      return this.validator;
   }

   // $FF: synthetic method
   private static REGIONPLUGINS[] $values() {
      return new REGIONPLUGINS[]{FACTIONSUUID, GRIEFDEFENDER, GRIEFPREVENTION, HCLAIMS, HUSKTOWNS, KINGDOMSX, LANDS, PUEBLOS, REDPROTECT, RESIDENCE, SABERFACTIONS, TOWNY, ULTIMATECLAIMS, WORLDGUARD, MINEPLOTS, FACTIONSBRIDGE, CRASH_CLAIM};
   }
}
