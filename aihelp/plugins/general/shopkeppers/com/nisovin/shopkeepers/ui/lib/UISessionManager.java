package com.nisovin.shopkeepers.ui.lib;

import com.nisovin.shopkeepers.api.events.PlayerOpenUIEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.ui.UISession;
import com.nisovin.shopkeepers.api.ui.UIType;
import com.nisovin.shopkeepers.util.bukkit.SchedulerUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class UISessionManager {
   @Nullable
   private static UISessionManager instance;
   private final Plugin plugin;
   private final UISessionManager.SessionHandler sessionHandler;
   private final UIListener uiListener;
   private final Map<UUID, View> uiSessions = new HashMap();
   private final Collection<? extends View> uiSessionsView;

   public static UISessionManager getInstance() {
      return (UISessionManager)Validate.State.notNull(instance, (String)"Not yet initialized!");
   }

   public static void initialize(Plugin plugin, UISessionManager.SessionHandler sessionHandler) {
      if (instance != null) {
         instance.onDisable();
      }

      instance = new UISessionManager(plugin, sessionHandler);
   }

   private UISessionManager(Plugin plugin, UISessionManager.SessionHandler sessionHandler) {
      this.uiSessionsView = Collections.unmodifiableCollection(this.uiSessions.values());
      Validate.notNull(plugin, (String)"plugin is null");
      Validate.notNull(sessionHandler, (String)"sessionHandler is null");
      this.plugin = plugin;
      this.sessionHandler = sessionHandler;
      this.uiListener = new UIListener(plugin, (UISessionManager)Unsafe.initialized(this));
   }

   public void onEnable() {
      this.uiListener.onEnable();
   }

   public void onDisable() {
      this.abortUISessions();
      this.uiListener.onDisable();
   }

   public boolean requestUI(ViewProvider viewProvider, Player player) {
      return this.requestUI(viewProvider, player, false, UIState.EMPTY);
   }

   public boolean requestUI(ViewProvider viewProvider, Player player, UIState uiState) {
      return this.requestUI(viewProvider, player, false, uiState);
   }

   public boolean requestUI(ViewProvider viewProvider, Player player, boolean silentRequest) {
      return this.requestUI(viewProvider, player, silentRequest, UIState.EMPTY);
   }

   public boolean requestUI(ViewProvider viewProvider, Player player, boolean silentRequest, UIState uiState) {
      Validate.notNull(viewProvider, (String)"viewProvider is null");
      Validate.notNull(player, (String)"player is null");
      UIType uiType = viewProvider.getUIType();
      String uiIdentifier = uiType.getIdentifier();
      String playerName = player.getName();
      if (!player.isValid()) {
         Log.debug(() -> {
            return "Player " + playerName + " cannot open UI '" + uiIdentifier + "': Player not connected.";
         });
         return false;
      } else if (!viewProvider.canOpen(player, silentRequest)) {
         Log.debug(() -> {
            return "Player " + playerName + " cannot open UI '" + uiIdentifier + "'.";
         });
         return false;
      } else {
         View oldSession = this.getUISession(player);
         if (oldSession != null && oldSession.getProvider().equals(viewProvider)) {
            Log.debug(() -> {
               return "UI '" + uiIdentifier + "' is already open for player " + playerName + ".";
            });
            return false;
         } else {
            PlayerOpenUIEvent openUIEvent = this.sessionHandler.createPlayerOpenUIEvent(viewProvider, player, silentRequest, uiState);
            Validate.notNull(openUIEvent, (String)"SessionHandler returned null PlayerOpenUIEvent!");
            Bukkit.getPluginManager().callEvent(openUIEvent);
            if (openUIEvent.isCancelled()) {
               Log.debug(() -> {
                  return "A plugin cancelled the opening of UI '" + uiIdentifier + "' for player " + playerName + ".";
               });
               return false;
            } else {
               player.closeInventory();

               assert this.getUISession(player) == null;

               Log.debug(() -> {
                  return "Opening UI '" + uiIdentifier + "' for player " + player.getName() + " ...";
               });
               View view = viewProvider.createView(player, uiState);
               if (view == null) {
                  Log.debug(() -> {
                     return "Failed to instantiate UI '" + uiIdentifier + "'!";
                  });
                  return false;
               } else {
                  Set var10000 = view.getAdditionalInventoryEvents();
                  UIListener var10001 = this.uiListener;
                  Objects.requireNonNull(var10001);
                  var10000.forEach(var10001::registerEventType);
                  this.uiSessions.put(player.getUniqueId(), view);
                  if (!view.open()) {
                     Log.debug(() -> {
                        return "Failed to open UI '" + uiIdentifier + "'!";
                     });
                     this.endUISession(player, (InventoryCloseEvent)null);
                     return false;
                  } else {
                     return true;
                  }
               }
            }
         }
      }
   }

   public Collection<? extends View> getUISessions() {
      return this.uiSessionsView;
   }

   public Collection<? extends View> getUISessionsForContext(Object contextObject) {
      Validate.notNull(contextObject, "contextObject is null");
      List<View> sessions = new ArrayList();
      this.uiSessionsView.forEach((uiSession) -> {
         if (uiSession.getContext().getObject() == contextObject) {
            sessions.add(uiSession);
         }

      });
      return sessions;
   }

   public Collection<? extends View> getUISessionsForContext(Object contextObject, UIType uiType) {
      Validate.notNull(contextObject, "contextObject is null");
      Validate.notNull(uiType, (String)"uiType is null");
      List<View> sessions = new ArrayList();
      this.uiSessionsView.forEach((uiSession) -> {
         if (uiSession.getContext().getObject() == contextObject && uiSession.getUIType() == uiType) {
            sessions.add(uiSession);
         }

      });
      return sessions;
   }

   public Collection<? extends View> getUISessions(UIType uiType) {
      Validate.notNull(uiType, (String)"uiType is null");
      List<View> sessions = new ArrayList();
      this.uiSessionsView.forEach((uiSession) -> {
         if (uiSession.getUIType() == uiType) {
            sessions.add(uiSession);
         }

      });
      return sessions;
   }

   @Nullable
   public View getUISession(Player player) {
      Validate.notNull(player, (String)"player is null");
      return (View)this.uiSessions.get(player.getUniqueId());
   }

   void onInventoryClose(InventoryCloseEvent closeEvent) {
      assert closeEvent != null;

      if (closeEvent.getPlayer() instanceof Player) {
         Player player = (Player)closeEvent.getPlayer();
         UISession session = this.getUISession(player);
         if (session != null) {
            Log.debug(() -> {
               String var10000 = player.getName();
               return "Player " + var10000 + " closed UI '" + session.getUIType().getIdentifier() + "'.";
            });
            this.endUISession(player, closeEvent);
         }
      }
   }

   void onPlayerQuit(Player player) {
      this.endUISession(player, (InventoryCloseEvent)null);
   }

   void endUISession(Player player, @Nullable InventoryCloseEvent closeEvent) {
      assert player != null;

      View session = (View)this.uiSessions.remove(player.getUniqueId());
      if (session != null) {
         this.onSessionEnded(session, closeEvent);
      }
   }

   private void onSessionEnded(View session, @Nullable InventoryCloseEvent closeEvent) {
      Log.debug(() -> {
         String var10000 = session.getUIType().getIdentifier();
         return "UI session '" + var10000 + "' ended for player " + session.getPlayer().getName() + ".";
      });
      session.onSessionEnd();
      session.onInventoryClose(closeEvent);
   }

   void abort(View uiSession) {
      assert uiSession != null;

      if (uiSession.isValid()) {
         Player player = uiSession.getPlayer();
         this.endUISession(player, (InventoryCloseEvent)null);
         player.closeInventory();
      }
   }

   public void abortUISessions() {
      (new ArrayList(this.getUISessions())).forEach(View::abort);

      assert this.uiSessions.isEmpty();

   }

   public void abortUISessionsForContext(Object contextObject) {
      this.getUISessionsForContext(contextObject).forEach(View::abort);
   }

   public void abortUISessionsForContext(Object contextObject, UIType uiType) {
      this.getUISessionsForContext(contextObject, uiType).forEach(View::abort);
   }

   public void abortUISessionsForContextDelayed(Object contextObject) {
      Validate.notNull(contextObject, "context is null");
      this.deactivateUIsForContext(contextObject);
      SchedulerUtils.runTaskOrOmit(this.plugin, () -> {
         this.abortUISessionsForContext(contextObject);
      });
   }

   public void abortUISessionsForContextDelayed(Object contextObject, UIType uiType) {
      Validate.notNull(contextObject, "context is null");
      Validate.notNull(uiType, (String)"uiType is null");
      this.deactivateUIsForContext(contextObject, uiType);
      SchedulerUtils.runTaskOrOmit(this.plugin, () -> {
         this.abortUISessionsForContext(contextObject, uiType);
      });
   }

   private void deactivateUIsForContext(Object contextObject) {
      assert contextObject != null;

      this.uiSessionsView.forEach((uiSession) -> {
         if (uiSession.getContext().getObject() == contextObject) {
            uiSession.deactivateUI();
         }

      });
   }

   private void deactivateUIsForContext(Object contextObject, UIType uiType) {
      assert contextObject != null;

      this.uiSessionsView.forEach((uiSession) -> {
         if (uiSession.getContext().getObject() == contextObject && uiSession.getUIType() == uiType) {
            uiSession.deactivateUI();
         }

      });
   }

   public interface SessionHandler {
      UISessionManager.SessionHandler DEFAULT = new UISessionManager.SessionHandler() {
      };

      default PlayerOpenUIEvent createPlayerOpenUIEvent(ViewProvider viewProvider, Player player, boolean silentRequest, UIState uiState) {
         return new PlayerOpenUIEvent(viewProvider.getUIType(), player, silentRequest);
      }
   }
}
