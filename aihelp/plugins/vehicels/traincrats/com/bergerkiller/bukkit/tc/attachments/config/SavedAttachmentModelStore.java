package com.bergerkiller.bukkit.tc.attachments.config;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentTypeRegistry;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentItem;
import com.bergerkiller.bukkit.tc.exception.IllegalNameException;
import com.bergerkiller.bukkit.tc.properties.SavedClaim;
import com.bergerkiller.bukkit.tc.utils.SetCallbackCollector;
import com.bergerkiller.bukkit.tc.utils.modularconfiguration.BasicModularConfiguration;
import com.bergerkiller.bukkit.tc.utils.modularconfiguration.ModularConfigurationEntry;
import com.bergerkiller.bukkit.tc.utils.modularconfiguration.ModularConfigurationFile;
import com.bergerkiller.bukkit.tc.utils.modularconfiguration.ModularConfigurationModule;
import com.bergerkiller.bukkit.tc.utils.modularconfiguration.ReadOnlyModuleException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class SavedAttachmentModelStore implements TrainCarts.Provider {
   protected final TrainCarts traincarts;
   protected final ModularConfigurationEntry.Container<SavedAttachmentModel> container;

   protected SavedAttachmentModelStore(TrainCarts traincarts, ModularConfigurationEntry.Container<SavedAttachmentModel> container) {
      this.traincarts = traincarts;
      this.container = container;
   }

   public static SavedAttachmentModelStore create(TrainCarts traincarts, String filename, String directoryName) {
      SavedAttachmentModelStore.ModularConfig modularConfig = new SavedAttachmentModelStore.ModularConfig(traincarts, filename, directoryName);
      return new SavedAttachmentModelStore.DefaultStore(traincarts, modularConfig);
   }

   static SavedAttachmentModelStore createModule(ModularConfigurationModule<SavedAttachmentModel> module) {
      SavedAttachmentModelStore.ModularConfig modularConfig = (SavedAttachmentModelStore.ModularConfig)module.getMain();
      return (SavedAttachmentModelStore)(module == modularConfig.getDefaultModule() ? modularConfig.traincarts.getSavedAttachmentModels() : new SavedAttachmentModelStore.ModuleStore(modularConfig.traincarts, module));
   }

   public TrainCarts getTrainCarts() {
      return this.traincarts;
   }

   public String getName() {
      return this.container.getName();
   }

   public abstract boolean isDefault();

   public abstract List<String> getModuleNames();

   public abstract SavedAttachmentModelStore getModule(String var1);

   public String getModuleNameOfModel(String name) {
      ModularConfigurationEntry<SavedAttachmentModel> entry = this.container.getIfExists(name);
      return entry == null ? null : entry.getModule().getName();
   }

   public abstract void setModuleNameOfModel(String var1, String var2);

   public boolean hasPermission(CommandSender sender, String name) {
      SavedAttachmentModel savedProperties = this.getModel(name);
      return savedProperties == null || savedProperties.hasPermission(sender);
   }

   public Set<SavedClaim> getClaims(String name) {
      SavedAttachmentModel savedProperties = this.getModel(name);
      return savedProperties == null ? Collections.emptySet() : savedProperties.getClaims();
   }

   public void setClaim(String name, Player player) {
      this.setClaims(name, Collections.singleton(new SavedClaim(player)));
   }

   public void setClaims(String name, Collection<SavedClaim> claims) {
      SavedAttachmentModel savedModel = this.getModel(name);
      if (savedModel != null) {
         savedModel.setClaims(claims);
      }

   }

   public boolean containsModel(String name) {
      return this.container.getIfExists(name) != null;
   }

   public abstract void save(boolean var1);

   public abstract void reload();

   public SavedAttachmentModel setDefaultConfigIfMissing(String name) throws IllegalNameException {
      return this.setDefaultConfigIfMissing(name, (CommandSender)null);
   }

   public SavedAttachmentModel setDefaultConfigIfMissing(String name, CommandSender editingPlayer) throws IllegalNameException {
      SavedAttachmentModel existing = this.getModel(name);
      if (existing != null) {
         return existing;
      } else {
         ConfigurationNode config = new ConfigurationNode();
         AttachmentTypeRegistry.instance().toConfig(config, CartAttachmentItem.TYPE);
         config.set("item", new ItemStack(MaterialUtil.getMaterial("LEGACY_WOOD")));
         return this.setConfigAsPlayer(name, config, editingPlayer);
      }
   }

   public SavedAttachmentModel setConfig(String name, ConfigurationNode config) throws IllegalNameException {
      return this.setConfigAsPlayer(name, config, (CommandSender)null);
   }

   public SavedAttachmentModel setConfigAsPlayer(String name, ConfigurationNode config, CommandSender editingPlayer) throws IllegalNameException {
      if (name != null && !name.isEmpty()) {
         List<String> claims = Collections.emptyList();
         ModularConfigurationEntry<SavedAttachmentModel> entry = this.container.getIfExists(name);
         if (entry == null && editingPlayer instanceof Player && TCConfig.claimNewSavedModels) {
            claims = Collections.singletonList((new SavedClaim((Player)editingPlayer)).toString());
         } else if (entry != null && entry.getConfig().contains("claims")) {
            claims = new ArrayList(entry.getConfig().getList("claims", String.class));
         }

         entry = this.container.add(name, config);
         entry.getWritableConfig().set("claims", claims);
         return (SavedAttachmentModel)entry.get();
      } else {
         throw new IllegalNameException("Name is empty");
      }
   }

   public SavedAttachmentModel getModel(String name) {
      ModularConfigurationEntry<SavedAttachmentModel> entry = this.container.getIfExists(name);
      return entry == null ? null : (SavedAttachmentModel)entry.get();
   }

   public abstract SavedAttachmentModel getModelOrNone(String var1);

   public final SavedAttachmentModel getEditingInit(Player player) {
      return this.traincarts.getPlayer(player).getEditedModelInit();
   }

   public final SavedAttachmentModel getEditingInit(UUID playerUUID) {
      return this.traincarts.getPlayer(playerUUID).getEditedModelInit();
   }

   public final SavedAttachmentModel getEditing(Player player) {
      return this.traincarts.getPlayer(player).getEditedModel();
   }

   public final SavedAttachmentModel getEditing(UUID playerUUID) {
      return this.traincarts.getPlayer(playerUUID).getEditedModel();
   }

   public final void setEditing(Player player, SavedAttachmentModel model) {
      this.traincarts.getPlayer(player).editModel(model);
   }

   public final void setEditing(UUID playerUUID, SavedAttachmentModel model) {
      this.traincarts.getPlayer(playerUUID).editModel(model);
   }

   public ConfigurationNode getConfig(String name) {
      ModularConfigurationEntry<SavedAttachmentModel> entry = this.container.getIfExists(name);
      return entry == null ? null : entry.getConfig();
   }

   public String findName(String text) {
      String foundName = null;
      Iterator var3 = this.getNames().iterator();

      while(true) {
         String name;
         do {
            do {
               if (!var3.hasNext()) {
                  return foundName;
               }

               name = (String)var3.next();
            } while(!text.startsWith(name));
         } while(foundName != null && name.length() <= foundName.length());

         foundName = name;
      }
   }

   public boolean remove(String name) {
      try {
         return this.container.remove(name) != null;
      } catch (ReadOnlyModuleException var3) {
         return false;
      }
   }

   public boolean rename(String name, String newName) {
      return this.container.rename(name, newName);
   }

   public List<String> getNames() {
      return this.container.getNames();
   }

   public List<SavedAttachmentModel> getAll() {
      return this.container.getAllValues();
   }

   public void findModelsUsedInConfiguration(AttachmentConfig attachmentConfig, SetCallbackCollector<SavedAttachmentModel> models) {
      if (attachmentConfig instanceof AttachmentConfig.Model) {
         String name = ((AttachmentConfig.Model)attachmentConfig).modelName();
         SavedAttachmentModel model = this.getModelOrNone(name);
         if (models.acceptCheckAdded(model) && !model.isNone()) {
            this.findModelsUsedInConfiguration(model.getRoot().get(), models);
         }
      }

      Iterator var5 = attachmentConfig.children().iterator();

      while(var5.hasNext()) {
         AttachmentConfig child = (AttachmentConfig)var5.next();
         this.findModelsUsedInConfiguration(child, models);
      }

   }

   public void findModelsUsedInConfiguration(ConfigurationNode attachmentConfig, SetCallbackCollector<SavedAttachmentModel> models) {
      if ("MODEL".equals(attachmentConfig.getOrDefault("type", "EMPTY"))) {
         String modelName = (String)attachmentConfig.getOrDefault("modelName", "");
         if (!modelName.isEmpty()) {
            SavedAttachmentModel model = this.traincarts.getSavedAttachmentModels().getModelOrNone(modelName);
            if (models.acceptCheckAdded(model) && !model.isNone()) {
               this.findModelsUsedInConfiguration(model.getRoot().get(), models);
            }
         }
      }

      Iterator var5 = attachmentConfig.getNodeList("attachments").iterator();

      while(var5.hasNext()) {
         ConfigurationNode child = (ConfigurationNode)var5.next();
         this.findModelsUsedInConfiguration(child, models);
      }

   }

   private static class ModularConfig extends BasicModularConfiguration<SavedAttachmentModel> {
      private final TrainCarts traincarts;

      public ModularConfig(TrainCarts plugin, String mainFilePath, String moduleDirectoryPath) {
         super(plugin, mainFilePath, moduleDirectoryPath);
         this.traincarts = plugin;
         this.addResourcePack(TCConfig.resourcePack, "traincarts", "saved_models");
      }

      protected void preProcessModuleConfiguration(ConfigurationNode moduleConfig) {
         this.storeSavedNameInConfig(moduleConfig);
      }

      protected void postProcessEntryConfiguration(ModularConfigurationEntry<SavedAttachmentModel> entry) {
         ConfigurationNode config = entry.getWritableConfig();
         if (!config.contains("savedName") || !((String)config.get("savedName", "")).equals(entry.getName())) {
            config.set("savedName", entry.getName());
         }

      }

      protected SavedAttachmentModel decodeConfig(ModularConfigurationEntry<SavedAttachmentModel> entry) {
         return new SavedAttachmentModel(entry);
      }

      private void storeSavedNameInConfig(ConfigurationNode savedModelsConfig) {
         boolean logSavedNameFieldWarning = false;
         Iterator var3 = savedModelsConfig.getNodes().iterator();

         while(var3.hasNext()) {
            ConfigurationNode config = (ConfigurationNode)var3.next();
            if (!config.contains("savedName")) {
               config.set("savedName", config.getName());
            } else {
               String setName = (String)config.get("savedName", config.getName());
               if (!config.getName().equals(setName)) {
                  this.logger.log(Level.WARNING, "Saved attachment model '" + config.getName() + "' has a different name set: '" + setName + "'");
                  logSavedNameFieldWarning = true;
                  config.set("savedName", config.getName());
               }
            }
         }

         if (logSavedNameFieldWarning) {
            this.logger.log(Level.WARNING, "If the intention was to rename the model, instead rename the key, not field 'savedName'");
         }

      }
   }

   private static class DefaultStore extends SavedAttachmentModelStore {
      private final SavedAttachmentModelStore.ModularConfig modularConfig;

      public DefaultStore(TrainCarts traincarts, SavedAttachmentModelStore.ModularConfig modularConfig) {
         super(traincarts, modularConfig);
         this.modularConfig = modularConfig;
      }

      public void save(boolean autosave) {
         if (autosave) {
            this.modularConfig.saveChanges();
         } else {
            this.modularConfig.save();
         }

      }

      public void reload() {
         this.modularConfig.reload();
      }

      public boolean isDefault() {
         return true;
      }

      public List<String> getModuleNames() {
         return this.modularConfig.MODULES.getFileNames();
      }

      public SavedAttachmentModelStore getModule(String moduleName) {
         ModularConfigurationFile<SavedAttachmentModel> module = this.modularConfig.MODULES.getFile(moduleName);
         return module == null ? null : createModule(module);
      }

      public void setModuleNameOfModel(String name, String module) {
         ModularConfigurationEntry<SavedAttachmentModel> entry = this.modularConfig.getIfExists(name);
         if (entry != null) {
            entry.setModule(this.modularConfig.createModule(module));
         }

      }

      public SavedAttachmentModel getModelOrNone(String name) {
         return (SavedAttachmentModel)this.modularConfig.get(name).get();
      }
   }

   public static class ModuleStore extends SavedAttachmentModelStore {
      private final ModularConfigurationModule<SavedAttachmentModel> module;

      public ModuleStore(TrainCarts traincarts, ModularConfigurationModule<SavedAttachmentModel> module) {
         super(traincarts, module);
         this.module = module;
      }

      public void save(boolean autosave) {
         if (autosave) {
            this.module.saveChanges();
         } else {
            this.module.save();
         }

      }

      public void reload() {
         this.module.reload();
      }

      public boolean isDefault() {
         return false;
      }

      public List<String> getModuleNames() {
         return Collections.emptyList();
      }

      public SavedAttachmentModelStore getModule(String moduleName) {
         return null;
      }

      public void setModuleNameOfModel(String name, String module) {
      }

      public SavedAttachmentModel getModelOrNone(String name) {
         return (SavedAttachmentModel)this.module.getMain().get(name).get();
      }
   }

   public interface ModelUsing {
      default ConfigurationNode getUsedModelsAsExport() {
         Set<SavedAttachmentModel> models = this.getUsedModels();
         if (!models.isEmpty()) {
            ConfigurationNode result = new ConfigurationNode();
            Iterator var3 = models.iterator();

            while(var3.hasNext()) {
               SavedAttachmentModel model = (SavedAttachmentModel)var3.next();
               if (!model.isNone()) {
                  result.set(model.getName(), model.getConfig().clone());
               }
            }

            if (!result.isEmpty()) {
               return result;
            }
         }

         return null;
      }

      default Set<SavedAttachmentModel> getUsedModels() {
         SetCallbackCollector<SavedAttachmentModel> models = new SetCallbackCollector();
         this.getUsedModels(models);
         return models.result();
      }

      void getUsedModels(SetCallbackCollector<SavedAttachmentModel> var1);
   }
}
