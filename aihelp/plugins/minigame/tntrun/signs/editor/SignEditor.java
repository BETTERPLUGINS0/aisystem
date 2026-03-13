package tntrun.signs.editor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.messages.Messages;
import tntrun.utils.FormattingCodesParser;

public class SignEditor {
   private TNTRun plugin;
   private HashMap<String, HashSet<SignInfo>> signs = new HashMap();
   private List<SignInfo> lbsigns = new ArrayList();
   private File configfile;
   private int position;
   private String lbentry;

   public SignEditor(TNTRun plugin) {
      this.plugin = plugin;
      String var10003 = plugin.getDataFolder().getAbsolutePath();
      this.configfile = new File(var10003 + File.separator + "signs.yml");
   }

   private void addArena(String arena) {
      if (!this.signs.containsKey(arena)) {
         this.signs.put(arena, new HashSet());
      }

   }

   public void removeArena(String arena) {
      Iterator var3 = this.getSignsBlocks(arena).iterator();

      while(var3.hasNext()) {
         Block block = (Block)var3.next();
         this.removeSign(block, arena);
      }

      this.signs.remove(arena);
   }

   public void addSign(Block block, String arena) {
      SignInfo signinfo = new SignInfo(block);
      this.addArena(arena);
      this.getSigns(arena).add(signinfo);
   }

   public void addLeaderboardSign(Block block) {
      SignInfo signinfo = new SignInfo(block);
      this.getLBSigns().add(signinfo);
   }

   private List<SignInfo> getLBSigns() {
      return this.lbsigns;
   }

   private SignInfo getLBSignInfo(Block block) {
      Iterator var3 = this.getLBSigns().iterator();

      while(var3.hasNext()) {
         SignInfo si = (SignInfo)var3.next();
         if (si.getBlock().equals(block)) {
            return si;
         }
      }

      return new SignInfo(block);
   }

   private void addLBSignInfo(SignInfo si) {
      if (!this.getLBSigns().contains(si)) {
         this.getLBSigns().add(si);
      }

   }

   public void modifyLeaderboardSign(Block block) {
      if (this.plugin.useStats()) {
         if (block.getState() instanceof Sign) {
            Sign sign = (Sign)block.getState();
            this.position = 0;
            sign.getSide(Side.FRONT).setLine(this.position, FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.prefix")));
            this.plugin.getStats().getWinMap().entrySet().stream().sorted(Entry.comparingByValue(Comparator.reverseOrder())).limit(3L).forEach((e) -> {
               if (this.plugin.useUuid()) {
                  OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString((String)e.getKey()));
                  if (!p.hasPlayedBefore()) {
                     this.plugin.getLogger().info("Invalid player data found for " + (String)e.getKey());
                     return;
                  }

                  this.lbentry = p.getName();
               } else {
                  this.lbentry = (String)e.getKey();
               }

               ++this.position;
               String line = FormattingCodesParser.parseFormattingCodes(Messages.leadersign).replace("{PLAYER}", this.lbentry.substring(0, Math.min(this.lbentry.length(), 11))).replace("{WINS}", String.valueOf(e.getValue()));
               sign.getSide(Side.FRONT).setLine(this.position, line);
            });
            sign.update();
         }

      }
   }

   public void refreshLeaderBoards() {
      Iterator var2 = this.getLBSigns().iterator();

      while(var2.hasNext()) {
         SignInfo signinfo = (SignInfo)var2.next();
         Block block = signinfo.getBlock();
         if (block != null) {
            this.modifyLeaderboardSign(block);
         }
      }

   }

   public void removeLeaderboardSign(Block block) {
      if (block.getState() instanceof Sign) {
         this.getLBSigns().remove(this.getLBSignInfo(block));
      }

   }

   public void removeSign(Block block, String arena) {
      if (block.getState() instanceof Sign) {
         Sign sign = (Sign)block.getState();
         sign.getSide(Side.FRONT).setLine(0, "");
         sign.getSide(Side.FRONT).setLine(1, "");
         sign.getSide(Side.FRONT).setLine(2, "");
         sign.getSide(Side.FRONT).setLine(3, "");
         sign.update();
      }

      this.addArena(arena);
      this.getSigns(arena).remove(this.getSignInfo(block, arena));
   }

   private HashSet<Block> getSignsBlocks(String arena) {
      HashSet<Block> signs = new HashSet();
      Iterator var4 = this.getSigns(arena).iterator();

      while(var4.hasNext()) {
         SignInfo signinfo = (SignInfo)var4.next();
         Block block = signinfo.getBlock();
         if (block != null) {
            signs.add(block);
         }
      }

      return signs;
   }

   private SignInfo getSignInfo(Block block, String arena) {
      Iterator var4 = this.getSigns(arena).iterator();

      while(var4.hasNext()) {
         SignInfo si = (SignInfo)var4.next();
         if (block.equals(si.getBlock())) {
            return si;
         }
      }

      return new SignInfo(block);
   }

   private void addSignInfo(SignInfo si, String arena) {
      this.addArena(arena);
      this.getSigns(arena).add(si);
   }

   private HashSet<SignInfo> getSigns(String arena) {
      this.addArena(arena);
      return (HashSet)this.signs.get(arena);
   }

   public void modifySigns(String arenaname) {
      try {
         Arena arena = this.plugin.amanager.getArenaByName(arenaname);
         if (arena == null) {
            return;
         }

         String text;
         String colour;
         label49: {
            text = null;
            colour = null;
            int players = arena.getPlayersManager().getPlayersCount();
            int maxPlayers = arena.getStructureManager().getMaxPlayers();
            String status = players == maxPlayers ? "Running" : arena.getStatusManager().getArenaStatus();
            switch(status.hashCode()) {
            case -1079530081:
               if (status.equals("Running")) {
                  text = FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.status.ingame")).replace("{MPS}", maxPlayers.makeConcatWithConstants<invokedynamic>(maxPlayers)).replace("{PS}", players.makeConcatWithConstants<invokedynamic>(players));
                  colour = this.plugin.getConfig().getString("signs.blockcolour.ingame");
                  break label49;
               }
               break;
            case 335584924:
               if (status.equals("Disabled")) {
                  text = FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.status.disabled"));
                  colour = this.plugin.getConfig().getString("signs.blockcolour.disabled");
                  break label49;
               }
               break;
            case 1197090693:
               if (status.equals("Regenerating")) {
                  text = FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.status.regenerating"));
                  colour = this.plugin.getConfig().getString("signs.blockcolour.ingame");
                  break label49;
               }
               break;
            case 1381450848:
               if (status.equals("Starting")) {
                  text = FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.status.waiting")).replace("{MPS}", maxPlayers.makeConcatWithConstants<invokedynamic>(maxPlayers)).replace("{PS}", players.makeConcatWithConstants<invokedynamic>(players));
                  colour = this.plugin.getConfig().getString("signs.blockcolour.starting");
                  break label49;
               }
            }

            text = FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.status.waiting")).replace("{MPS}", maxPlayers.makeConcatWithConstants<invokedynamic>(maxPlayers)).replace("{PS}", players.makeConcatWithConstants<invokedynamic>(players));
            colour = this.plugin.getConfig().getString("signs.blockcolour.waiting");
         }

         Iterator var10 = this.getSignsBlocks(arenaname).iterator();

         while(var10.hasNext()) {
            Block block = (Block)var10.next();
            if (block.getState() instanceof Sign) {
               Sign sign = (Sign)block.getState();
               sign.getSide(Side.FRONT).setLine(0, FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.prefix")));
               sign.getSide(Side.FRONT).setLine(1, FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.join")));
               SignSide var10000 = sign.getSide(Side.FRONT);
               String var10002 = this.plugin.getConfig().getString("signs.arena");
               var10000.setLine(2, FormattingCodesParser.parseFormattingCodes(var10002 + arenaname));
               sign.getSide(Side.FRONT).setLine(3, text);
               sign.update();
               this.setBlockColour(block, colour);
            } else {
               this.removeSign(block, arenaname);
            }
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      }

   }

   private void setBlockColour(Block block, String colour) {
      if (colour != null && !colour.isEmpty()) {
         BlockData data = block.getBlockData();
         if (data instanceof WallSign || data instanceof Directional) {
            Directional directional = (Directional)data;
            Block blockBehind = block.getRelative(directional.getFacing().getOppositeFace());
            if (Tag.IMPERMEABLE.isTagged(blockBehind.getType()) && blockBehind.getType() != Material.GLASS) {
               Material material = Material.getMaterial(colour.toUpperCase() + "_STAINED_GLASS");
               if (material != null) {
                  blockBehind.setType(material);
               }
            }

         }
      }
   }

   public void loadConfiguration() {
      FileConfiguration file = YamlConfiguration.loadConfiguration(this.configfile);
      ConfigurationSection blockSection;
      ConfigurationSection section;
      String block;
      Iterator var9;
      if (!file.isConfigurationSection("arenas")) {
         Iterator var3 = file.getKeys(false).iterator();

         while(var3.hasNext()) {
            String arena = (String)var3.next();
            if (!arena.equalsIgnoreCase("leaderboards")) {
               ConfigurationSection section = file.getConfigurationSection(arena);
               this.readSignInfo(section, arena);
            }
         }
      } else {
         section = file.getConfigurationSection("arenas");
         var9 = section.getKeys(false).iterator();

         while(var9.hasNext()) {
            block = (String)var9.next();
            blockSection = section.getConfigurationSection(block);
            this.readSignInfo(blockSection, block);
         }
      }

      if (file.isConfigurationSection("leaderboards")) {
         section = file.getConfigurationSection("leaderboards");
         var9 = section.getKeys(false).iterator();

         while(var9.hasNext()) {
            block = (String)var9.next();
            blockSection = section.getConfigurationSection(block);
            SignInfo si = new SignInfo(blockSection.getString("world"), blockSection.getInt("x"), blockSection.getInt("y"), blockSection.getInt("z"));
            this.addLBSignInfo(si);
         }

         this.refreshLeaderBoards();
      }

   }

   private void readSignInfo(ConfigurationSection section, String arena) {
      Iterator var4 = section.getKeys(false).iterator();

      while(var4.hasNext()) {
         String block = (String)var4.next();
         ConfigurationSection blockSection = section.getConfigurationSection(block);
         SignInfo si = new SignInfo(blockSection.getString("world"), blockSection.getInt("x"), blockSection.getInt("y"), blockSection.getInt("z"));
         this.addSignInfo(si, arena);
      }

      this.modifySigns(arena);
   }

   public void saveConfiguration() {
      FileConfiguration file = new YamlConfiguration();
      Iterator var3 = this.signs.keySet().iterator();

      while(var3.hasNext()) {
         String arena = (String)var3.next();
         ConfigurationSection section = file.createSection("arenas." + arena);
         int i = 0;
         Iterator var7 = this.getSigns(arena).iterator();

         while(var7.hasNext()) {
            SignInfo si = (SignInfo)var7.next();
            ConfigurationSection blockSection = section.createSection(Integer.toString(i++));
            this.writeSignInfo(blockSection, si);
         }
      }

      ConfigurationSection section = file.createSection("leaderboards");
      int i = 0;
      Iterator var13 = this.lbsigns.iterator();

      while(var13.hasNext()) {
         SignInfo si = (SignInfo)var13.next();
         ConfigurationSection blockSection = section.createSection(Integer.toString(i++));
         this.writeSignInfo(blockSection, si);
      }

      try {
         file.save(this.configfile);
      } catch (IOException var9) {
         this.plugin.getLogger().info("Error saving file " + String.valueOf(this.configfile));
         var9.printStackTrace();
      }

   }

   private void writeSignInfo(ConfigurationSection blockSection, SignInfo si) {
      blockSection.set("x", si.getX());
      blockSection.set("y", si.getY());
      blockSection.set("z", si.getZ());
      blockSection.set("world", si.getWorldName());
   }

   public void createJoinSign(Block block, final String arenaname) {
      Sign sign = (Sign)block.getState();
      sign.getSide(Side.FRONT).setLine(0, FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.prefix")));
      sign.getSide(Side.FRONT).setLine(1, FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.join")));
      SignSide var10000 = sign.getSide(Side.FRONT);
      String var10002 = this.plugin.getConfig().getString("signs.arena");
      var10000.setLine(2, FormattingCodesParser.parseFormattingCodes(var10002 + arenaname));
      this.addSign(block, arenaname);
      sign.update();
      (new BukkitRunnable() {
         public void run() {
            SignEditor.this.modifySigns(arenaname);
         }
      }).runTask(this.plugin);
   }
}
