package github.nighter.smartspawner.spawner.gui.main;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.language.MessageService;
import github.nighter.smartspawner.spawner.gui.layout.GuiButton;
import github.nighter.smartspawner.spawner.gui.layout.GuiLayout;
import github.nighter.smartspawner.spawner.gui.sell.SpawnerSellConfirmUI;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import github.nighter.smartspawner.spawner.properties.VirtualInventory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.form.SimpleForm.Builder;
import org.geysermc.cumulus.util.FormImage.Type;
import org.geysermc.floodgate.api.FloodgateApi;

public class SpawnerMenuFormUI {
   private static final int TICKS_PER_SECOND = 20;
   private final SmartSpawner plugin;
   private final LanguageManager languageManager;
   private final MessageService messageService;
   private final Map<String, SpawnerMenuFormUI.CachedForm> formCache = new HashMap();
   private static final long CACHE_EXPIRY_TIME_MS = 30000L;
   private static final Map<String, SpawnerMenuFormUI.ActionButtonInfo> ACTION_BUTTON_CONFIG = new HashMap();

   public SpawnerMenuFormUI(SmartSpawner plugin) {
      this.plugin = plugin;
      this.languageManager = plugin.getLanguageManager();
      this.messageService = plugin.getMessageService();
   }

   public void clearCache() {
      this.formCache.clear();
   }

   public void invalidateSpawnerCache(String spawnerId) {
      this.formCache.entrySet().removeIf((entry) -> {
         return ((String)entry.getKey()).startsWith(spawnerId + "|");
      });
   }

   public void openSpawnerForm(Player player, SpawnerData spawner) {
      Map<String, String> placeholders = this.createPlaceholders(spawner);
      String title;
      if (spawner.getStackSize() > 1) {
         title = this.languageManager.getGuiTitle("bedrock.main_gui.title_stacked_spawner", placeholders);
      } else {
         title = this.languageManager.getGuiTitle("bedrock.main_gui.title_single_spawner", placeholders);
      }

      GuiLayout layout = this.plugin.getGuiLayoutConfig().getCurrentMainLayout();
      List<SpawnerMenuFormUI.ButtonInfo> availableButtons = this.collectAvailableButtons(layout, player, placeholders);
      if (availableButtons.isEmpty()) {
         this.messageService.sendMessage(player, "no_permission");
      } else {
         String var10000 = spawner.getSpawnerId();
         String cacheKey = var10000 + "|" + spawner.getStackSize() + "|" + spawner.getSpawnerExp() + "|" + spawner.getVirtualInventory().getUsedSlots();
         SpawnerMenuFormUI.CachedForm cachedForm = (SpawnerMenuFormUI.CachedForm)this.formCache.get(cacheKey);
         if (cachedForm != null && !cachedForm.isExpired() && cachedForm.buttons.equals(availableButtons)) {
            FloodgateApi.getInstance().getPlayer(player.getUniqueId()).sendForm(cachedForm.form);
         } else {
            Builder formBuilder = (Builder)SimpleForm.builder().title(title);
            Iterator var10 = availableButtons.iterator();

            while(var10.hasNext()) {
               SpawnerMenuFormUI.ButtonInfo buttonInfo = (SpawnerMenuFormUI.ButtonInfo)var10.next();
               formBuilder.button(buttonInfo.text, Type.URL, buttonInfo.imageUrl);
            }

            SimpleForm form = (SimpleForm)((Builder)((Builder)formBuilder.closedOrInvalidResultHandler(() -> {
            })).validResultHandler((response) -> {
               int buttonId = response.clickedButtonId();
               if (buttonId < availableButtons.size()) {
                  SpawnerMenuFormUI.ButtonInfo buttonInfo = (SpawnerMenuFormUI.ButtonInfo)availableButtons.get(buttonId);
                  Scheduler.runTask(() -> {
                     String var4 = buttonInfo.action;
                     byte var5 = -1;
                     switch(var4.hashCode()) {
                     case -1573338616:
                        if (var4.equals("view_info")) {
                           var5 = 5;
                        }
                        break;
                     case -1410715584:
                        if (var4.equals("open_stacker")) {
                           var5 = 1;
                        }
                        break;
                     case -1397348986:
                        if (var4.equals("open_storage")) {
                           var5 = 0;
                        }
                        break;
                     case 672232392:
                        if (var4.equals("sell_and_exp")) {
                           var5 = 2;
                        }
                        break;
                     case 1197899572:
                        if (var4.equals("sell_all")) {
                           var5 = 3;
                        }
                        break;
                     case 1853584776:
                        if (var4.equals("collect_exp")) {
                           var5 = 4;
                        }
                     }

                     switch(var5) {
                     case 0:
                        this.plugin.getSpawnerMenuAction().handleStorageClick(player, spawner);
                        break;
                     case 1:
                        this.handleSpawnerInfo(player, spawner);
                        break;
                     case 2:
                        this.handleSellInventory(player, spawner);
                        break;
                     case 3:
                        this.handleSellAll(player, spawner);
                        break;
                     case 4:
                        this.handleExpCollection(player, spawner);
                        break;
                     case 5:
                        this.openViewInfoForm(player, spawner);
                        break;
                     default:
                        this.plugin.getLogger().warning("Unknown action in FormUI: " + buttonInfo.action);
                     }

                  });
               }

            })).build();
            this.formCache.put(cacheKey, new SpawnerMenuFormUI.CachedForm(form, availableButtons));
            FloodgateApi.getInstance().getPlayer(player.getUniqueId()).sendForm(form);
         }
      }
   }

   private List<SpawnerMenuFormUI.ButtonInfo> collectAvailableButtons(GuiLayout layout, Player player, Map<String, String> placeholders) {
      List<SpawnerMenuFormUI.ButtonInfo> buttons = new ArrayList();
      Set<String> addedActions = new HashSet();
      List<GuiButton> sortedButtons = layout.getAllButtons().values().stream().filter(GuiButton::isEnabled).sorted(Comparator.comparing(GuiButton::getSlot)).toList();
      Iterator var7 = sortedButtons.iterator();

      while(true) {
         GuiButton button;
         do {
            if (!var7.hasNext()) {
               if (!addedActions.contains("view_info")) {
                  SpawnerMenuFormUI.ActionButtonInfo viewInfoConfig = (SpawnerMenuFormUI.ActionButtonInfo)ACTION_BUTTON_CONFIG.get("view_info");
                  if (viewInfoConfig != null) {
                     String text = this.languageManager.getGuiItemName(viewInfoConfig.langKey, placeholders);
                     buttons.add(new SpawnerMenuFormUI.ButtonInfo("view_info", text, viewInfoConfig.imageUrl));
                  }
               }

               return buttons;
            }

            button = (GuiButton)var7.next();
         } while(button.getCondition() != null && !this.evaluateCondition(button.getCondition()));

         Set<String> buttonActions = new HashSet(button.getActions().values());
         Iterator var10 = buttonActions.iterator();

         while(var10.hasNext()) {
            String action = (String)var10.next();
            if (!addedActions.contains(action) && !"none".equals(action) && this.hasPermissionForAction(player, action)) {
               SpawnerMenuFormUI.ActionButtonInfo actionConfig = (SpawnerMenuFormUI.ActionButtonInfo)ACTION_BUTTON_CONFIG.get(action);
               if (actionConfig != null) {
                  String text = this.languageManager.getGuiItemName(actionConfig.langKey, placeholders);
                  buttons.add(new SpawnerMenuFormUI.ButtonInfo(action, text, actionConfig.imageUrl));
                  addedActions.add(action);
               }
            }
         }
      }
   }

   private boolean evaluateCondition(String condition) {
      byte var3 = -1;
      switch(condition.hashCode()) {
      case -947888951:
         if (condition.equals("no_shop_integration")) {
            var3 = 1;
         }
         break;
      case 1040182795:
         if (condition.equals("shop_integration")) {
            var3 = 0;
         }
      }

      switch(var3) {
      case 0:
         return this.plugin.hasSellIntegration();
      case 1:
         return !this.plugin.hasSellIntegration();
      default:
         this.plugin.getLogger().warning("Unknown condition in FormUI: " + condition);
         return true;
      }
   }

   private boolean hasPermissionForAction(Player player, String action) {
      byte var4 = -1;
      switch(action.hashCode()) {
      case -1573338616:
         if (action.equals("view_info")) {
            var4 = 5;
         }
         break;
      case -1410715584:
         if (action.equals("open_stacker")) {
            var4 = 1;
         }
         break;
      case -1397348986:
         if (action.equals("open_storage")) {
            var4 = 0;
         }
         break;
      case 672232392:
         if (action.equals("sell_and_exp")) {
            var4 = 2;
         }
         break;
      case 1197899572:
         if (action.equals("sell_all")) {
            var4 = 3;
         }
         break;
      case 1853584776:
         if (action.equals("collect_exp")) {
            var4 = 4;
         }
      }

      switch(var4) {
      case 0:
         return true;
      case 1:
         return player.hasPermission("smartspawner.stack");
      case 2:
      case 3:
         return this.plugin.hasSellIntegration() && player.hasPermission("smartspawner.sellall");
      case 4:
         return true;
      case 5:
         return true;
      default:
         return false;
      }
   }

   private void handleSpawnerInfo(Player player, SpawnerData spawner) {
      if (!player.hasPermission("smartspawner.stack")) {
         this.messageService.sendMessage(player, "no_permission");
      } else {
         this.plugin.getSpawnerStackerUI().openStackerGui(player, spawner);
      }
   }

   private void handleSellInventory(Player player, SpawnerData spawner) {
      if (this.plugin.hasSellIntegration() && player.hasPermission("smartspawner.sellall")) {
         if (spawner.getVirtualInventory().getUsedSlots() == 0) {
            this.plugin.getSpawnerMenuAction().handleExpBottleClick(player, spawner, false);
         } else {
            this.plugin.getSpawnerSellConfirmUI().openSellConfirmGui(player, spawner, SpawnerSellConfirmUI.PreviousGui.MAIN_MENU, true);
         }
      } else {
         this.messageService.sendMessage(player, "no_permission");
      }
   }

   private void handleSellAll(Player player, SpawnerData spawner) {
      if (this.plugin.hasSellIntegration() && player.hasPermission("smartspawner.sellall")) {
         if (spawner.getVirtualInventory().getUsedSlots() == 0) {
            this.messageService.sendMessage(player, "no_items");
         } else {
            this.plugin.getSpawnerSellConfirmUI().openSellConfirmGui(player, spawner, SpawnerSellConfirmUI.PreviousGui.MAIN_MENU, false);
         }
      } else {
         this.messageService.sendMessage(player, "no_permission");
      }
   }

   private void handleExpCollection(Player player, SpawnerData spawner) {
      this.plugin.getSpawnerMenuAction().handleExpBottleClick(player, spawner, false);
   }

   private void openViewInfoForm(Player player, SpawnerData spawner) {
      Map<String, String> placeholders = this.createPlaceholders(spawner);
      String title = this.languageManager.getGuiTitle("bedrock.main_gui.view_info_title", placeholders);
      String spawnerInfo = this.createSpawnerInfoContent(placeholders);
      String backButtonText = this.languageManager.getGuiItemName("bedrock.main_gui.button_names.back", placeholders);
      SimpleForm form = (SimpleForm)((Builder)((Builder)((Builder)SimpleForm.builder().title(title)).content(spawnerInfo).button(backButtonText).closedOrInvalidResultHandler(() -> {
      })).validResultHandler((response) -> {
         Scheduler.runTask(() -> {
            this.openSpawnerForm(player, spawner);
         });
      })).build();
      FloodgateApi.getInstance().getPlayer(player.getUniqueId()).sendForm(form);
   }

   private Map<String, String> createPlaceholders(SpawnerData spawner) {
      String entityName = this.languageManager.getFormattedMobName(spawner.getEntityType());
      String entityNameSmallCaps = this.languageManager.getSmallCaps(entityName);
      Map<String, String> placeholders = new HashMap();
      placeholders.put("ᴇɴᴛɪᴛʏ", entityNameSmallCaps);
      placeholders.put("entity", entityName);
      placeholders.put("amount", String.valueOf(spawner.getStackSize()));
      placeholders.put("stack_size", String.valueOf(spawner.getStackSize()));
      placeholders.put("range", String.valueOf(spawner.getSpawnerRange()));
      long delaySeconds = spawner.getSpawnDelay() / 20L;
      placeholders.put("delay", String.valueOf(delaySeconds));
      placeholders.put("delay_raw", String.valueOf(spawner.getSpawnDelay()));
      placeholders.put("min_mobs", String.valueOf(spawner.getMinMobs()));
      placeholders.put("max_mobs", String.valueOf(spawner.getMaxMobs()));
      VirtualInventory virtualInventory = spawner.getVirtualInventory();
      int currentItems = virtualInventory.getUsedSlots();
      int maxSlots = spawner.getMaxSpawnerLootSlots();
      double percentStorageDecimal = maxSlots > 0 ? (double)currentItems / (double)maxSlots * 100.0D : 0.0D;
      String formattedPercentStorage = String.format("%.1f", percentStorageDecimal);
      int percentStorageRounded = (int)Math.round(percentStorageDecimal);
      placeholders.put("current_items", String.valueOf(currentItems));
      placeholders.put("max_items", this.languageManager.formatNumber((double)maxSlots));
      placeholders.put("percent_storage_decimal", formattedPercentStorage);
      placeholders.put("percent_storage_rounded", String.valueOf(percentStorageRounded));
      long currentExp = (long)spawner.getSpawnerExp();
      long maxExp = (long)spawner.getMaxStoredExp();
      double percentExpDecimal = maxExp > 0L ? (double)currentExp / (double)maxExp * 100.0D : 0.0D;
      String formattedPercentExp = String.format("%.1f", percentExpDecimal);
      int percentExpRounded = (int)Math.round(percentExpDecimal);
      String formattedCurrentExp = this.languageManager.formatNumber((double)currentExp);
      String formattedMaxExp = this.languageManager.formatNumber((double)maxExp);
      placeholders.put("current_exp", formattedCurrentExp);
      placeholders.put("max_exp", formattedMaxExp);
      placeholders.put("raw_current_exp", String.valueOf(currentExp));
      placeholders.put("raw_max_exp", String.valueOf(maxExp));
      placeholders.put("percent_exp_decimal", formattedPercentExp);
      placeholders.put("percent_exp_rounded", String.valueOf(percentExpRounded));
      if (spawner.isSellValueDirty()) {
         spawner.recalculateSellValue();
      }

      double totalSellPrice = spawner.getAccumulatedSellValue();
      placeholders.put("total_sell_price", this.languageManager.formatNumber(totalSellPrice));
      return placeholders;
   }

   private String createSpawnerInfoContent(Map<String, String> placeholders) {
      List<String> infoLines = this.languageManager.getGuiItemLoreAsList("bedrock.main_gui.spawner_info", placeholders);
      if (infoLines != null && !infoLines.isEmpty()) {
         StringBuilder content = new StringBuilder();
         Iterator var4 = infoLines.iterator();

         while(var4.hasNext()) {
            String line = (String)var4.next();
            String bedrockLine = this.convertToBedrockColors(line);
            content.append(bedrockLine).append("\n");
         }

         if (content.length() > 0) {
            content.setLength(content.length() - 1);
         }

         return content.toString();
      } else {
         return "";
      }
   }

   private String convertToBedrockColors(String text) {
      if (text == null) {
         return "";
      } else {
         String result = text.replaceAll("&#([A-Fa-f0-9]{6})", "");
         result = this.mapHexToBedrockColors(result, text);
         result = result.replace("&0", "§0");
         result = result.replace("&1", "§1");
         result = result.replace("&2", "§2");
         result = result.replace("&3", "§3");
         result = result.replace("&4", "§4");
         result = result.replace("&5", "§5");
         result = result.replace("&6", "§6");
         result = result.replace("&7", "§7");
         result = result.replace("&8", "§8");
         result = result.replace("&9", "§9");
         result = result.replace("&a", "§a");
         result = result.replace("&b", "§b");
         result = result.replace("&c", "§c");
         result = result.replace("&d", "§d");
         result = result.replace("&e", "§e");
         result = result.replace("&f", "§f");
         result = result.replace("&g", "§g");
         return result;
      }
   }

   private String mapHexToBedrockColors(String result, String original) {
      Map<String, String> colorMap = new HashMap();
      colorMap.put("545454", "§8");
      colorMap.put("bdc3c7", "§7");
      colorMap.put("ecf0f1", "§f");
      colorMap.put("f8f8ff", "§f");
      colorMap.put("3498db", "§9");
      colorMap.put("2ecc71", "§a");
      colorMap.put("37eb9a", "§a");
      colorMap.put("2cc483", "§a");
      colorMap.put("48e89b", "§a");
      colorMap.put("00F986", "§a");
      colorMap.put("e67e22", "§6");
      colorMap.put("ff5252", "§c");
      colorMap.put("e63939", "§4");
      colorMap.put("ff7070", "§c");
      colorMap.put("d8c5ff", "§d");
      colorMap.put("7b68ee", "§5");
      colorMap.put("a885fc", "§d");
      colorMap.put("c2a8fc", "§d");
      colorMap.put("ab7afd", "§d");
      colorMap.put("EF6C00", "§6");
      colorMap.put("607D8B", "§8");

      Entry entry;
      for(Iterator var4 = colorMap.entrySet().iterator(); var4.hasNext(); result = result.replace("&#" + ((String)entry.getKey()).toLowerCase(), (CharSequence)entry.getValue())) {
         entry = (Entry)var4.next();
         result = result.replace("&#" + (String)entry.getKey(), (CharSequence)entry.getValue());
      }

      return result;
   }

   static {
      ACTION_BUTTON_CONFIG.put("open_storage", new SpawnerMenuFormUI.ActionButtonInfo("bedrock.main_gui.button_names.storage", "https://i.pinimg.com/736x/7a/28/50/7a28504d8446ab0ad670757cfa32fe59.jpg"));
      ACTION_BUTTON_CONFIG.put("open_stacker", new SpawnerMenuFormUI.ActionButtonInfo("bedrock.main_gui.button_names.stacker", "https://cdn.modrinth.com/data/9tQwxSFr/f0f1cc267f587a39acd2c820cfe6b29d0f2ccae3.png"));
      ACTION_BUTTON_CONFIG.put("sell_and_exp", new SpawnerMenuFormUI.ActionButtonInfo("bedrock.main_gui.button_names.sell_and_exp", "https://static.wikia.nocookie.net/minecraft_gamepedia/images/8/8a/Gold_Ingot_JE4_BE2.png/revision/latest?cb=20200224211607"));
      ACTION_BUTTON_CONFIG.put("sell_all", new SpawnerMenuFormUI.ActionButtonInfo("bedrock.main_gui.button_names.sell_all", "https://static.wikia.nocookie.net/minecraft_gamepedia/images/8/8a/Gold_Ingot_JE4_BE2.png/revision/latest?cb=20200224211607"));
      ACTION_BUTTON_CONFIG.put("collect_exp", new SpawnerMenuFormUI.ActionButtonInfo("bedrock.main_gui.button_names.exp", "https://minecraft.wiki/images/Bottle_o%27_Enchanting.gif"));
      ACTION_BUTTON_CONFIG.put("view_info", new SpawnerMenuFormUI.ActionButtonInfo("bedrock.main_gui.button_names.view_info", "https://static.wikia.nocookie.net/minecraft_gamepedia/images/9/9f/Information_sign.png/revision/latest/scale-to-width-down/268?cb=20200105100749"));
   }

   private static class CachedForm {
      final SimpleForm form;
      final List<SpawnerMenuFormUI.ButtonInfo> buttons;
      final long timestamp;

      CachedForm(SimpleForm form, List<SpawnerMenuFormUI.ButtonInfo> buttons) {
         this.form = form;
         this.buttons = buttons;
         this.timestamp = System.currentTimeMillis();
      }

      boolean isExpired() {
         return System.currentTimeMillis() - this.timestamp > 30000L;
      }
   }

   private static class ButtonInfo {
      final String action;
      final String text;
      final String imageUrl;

      ButtonInfo(String action, String text, String imageUrl) {
         this.action = action;
         this.text = text;
         this.imageUrl = imageUrl;
      }
   }

   private static class ActionButtonInfo {
      final String langKey;
      final String imageUrl;

      ActionButtonInfo(String langKey, String imageUrl) {
         this.langKey = langKey;
         this.imageUrl = imageUrl;
      }
   }
}
