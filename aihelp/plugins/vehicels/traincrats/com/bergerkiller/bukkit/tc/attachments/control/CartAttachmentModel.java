package com.bergerkiller.bukkit.tc.attachments.control;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapEventPropagation;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetText;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetTabView.Tab;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;
import com.bergerkiller.bukkit.tc.attachments.config.SavedAttachmentModel;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentNode;
import com.bergerkiller.bukkit.tc.attachments.ui.menus.general.ModelStorageTypeSelectionDialog;
import com.bergerkiller.bukkit.tc.attachments.ui.models.MapWidgetModelStoreSelect;
import com.bergerkiller.bukkit.tc.exception.IllegalNameException;
import java.util.Iterator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CartAttachmentModel extends CartAttachment {
   public static final AttachmentType TYPE = new AttachmentType() {
      public String getID() {
         return "MODEL";
      }

      public MapTexture getIcon(ConfigurationNode config) {
         return MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/model.png");
      }

      public Attachment createController(ConfigurationNode config) {
         return new CartAttachmentModel();
      }

      public void createAppearanceTab(final Tab tab, final MapWidgetAttachmentNode attachment) {
         final TrainCarts traincarts = TrainCarts.plugin;
         ((MapWidgetText)tab.addWidget((new MapWidgetText()).setText("Current Model:"))).setBounds(0, 3, 100, 16);
         final MapWidgetModelStoreSelect modelSelector = (MapWidgetModelStoreSelect)tab.addWidget(new MapWidgetModelStoreSelect(traincarts) {
            public void onAttached() {
               this.setSelectedModel(getModelOf(traincarts, attachment));
            }

            public void onSelectedModelChanged(SavedAttachmentModel model) {
               attachment.getConfig().set("modelName", model == null ? null : model.getName());
               this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed", attachment);
               Iterator var2 = tab.getWidgets().iterator();

               while(var2.hasNext()) {
                  MapWidget widget = (MapWidget)var2.next();
                  if (widget instanceof null.ModelActionButton) {
                     ((null.ModelActionButton)widget).updateEnabled();
                  }
               }

            }
         });
         modelSelector.setBounds(0, 13, 100, 13);
         ((<undefinedtype>)tab.addWidget(new null.ModelActionButton(traincarts, attachment) {
            public void onActivate() {
               final SavedAttachmentModel model = getModelOf(traincarts, attachment);
               if (model == null) {
                  this.setEnabled(false);
               } else if (this.checkPerm(model)) {
                  this.display.playSound(SoundEffect.CLICK);
                  tab.addWidget((new ModelStorageTypeSelectionDialog.LoadDialog() {
                     public void onConfigLoaded(ConfigurationNode attachmentConfig) {
                        if (checkPerm(model)) {
                           try {
                              traincarts.getSavedAttachmentModels().setConfigAsPlayer(model.getName(), attachmentConfig, this.getPlayerOwner().getOnlinePlayer());
                              modelSelector.setSelectedModel(model);
                           } catch (IllegalNameException var3) {
                              Localization.COMMAND_MODEL_CONFIG_INVALID_NAME.message((CommandSender)this.display.getOwners().get(0), new String[]{model.getName()});
                           }

                        }
                     }

                     public void close() {
                        super.close();
                        focus();
                     }
                  }).setPosition(0, 5));
               }
            }
         })).setText("Load").setBounds(0, 30, 49, 14);
         ((<undefinedtype>)tab.addWidget(new null.ModelActionButton(traincarts, attachment) {
            public void onActivate() {
               SavedAttachmentModel model = getModelOf(traincarts, attachment);
               if (model == null) {
                  this.setEnabled(false);
               } else if (this.checkPerm(model)) {
                  boolean isNewModel = model.isNone();
                  Player player = (Player)this.display.getOwners().get(0);
                  traincarts.getPlayer(player).editModel(model);
                  if (isNewModel) {
                     Localization.COMMAND_MODEL_CONFIG_EDIT_NEW.message(player, new String[]{model.getName()});
                  } else {
                     Localization.COMMAND_MODEL_CONFIG_EDIT_EXISTING.message(player, new String[]{model.getName()});
                  }
               }

            }
         })).setText("Edit").setBounds(51, 30, 49, 14);
      }

      private SavedAttachmentModel getModelOf(TrainCarts traincarts, MapWidgetAttachmentNode attachment) {
         String modelName = (String)attachment.getConfig().getOrDefault("modelName", "");
         return modelName.trim().isEmpty() ? null : traincarts.getSavedAttachmentModels().getModelOrNone(modelName);
      }

      class ModelActionButton extends MapWidgetButton {
         private final TrainCarts traincarts;
         private final MapWidgetAttachmentNode attachment;

         public ModelActionButton(TrainCarts traincarts, MapWidgetAttachmentNode attachment) {
            this.traincarts = traincarts;
            this.attachment = attachment;
         }

         public void updateEnabled() {
            this.setEnabled(getModelOf(this.traincarts, this.attachment) != null);
         }

         public void onAttached() {
            this.updateEnabled();
         }

         protected boolean checkPerm(SavedAttachmentModel model) {
            Player editing = (Player)this.display.getOwners().get(0);
            if (model.hasPermission(editing)) {
               return true;
            } else {
               Localization.COMMAND_MODEL_CONFIG_CLAIMED.message(editing, new String[]{model.getName()});
               this.display.playSound(SoundEffect.EXTINGUISH);
               return false;
            }
         }
      }
   };

   public void makeVisible(Player viewer) {
   }

   public void makeHidden(Player viewer) {
   }

   public void onTick() {
   }

   public void onMove(boolean absolute) {
   }
}
