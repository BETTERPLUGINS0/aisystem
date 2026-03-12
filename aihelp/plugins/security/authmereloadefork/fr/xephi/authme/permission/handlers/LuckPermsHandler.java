package fr.xephi.authme.permission.handlers;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.limbo.UserGroup;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.permission.PermissionsSystemType;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.context.ContextSetFactory;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeEqualityPredicate;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.InheritanceNode.Builder;
import net.luckperms.api.query.QueryMode;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class LuckPermsHandler implements PermissionHandler {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(LuckPermsHandler.class);
   private LuckPerms luckPerms;

   public LuckPermsHandler() throws PermissionHandlerException {
      try {
         this.luckPerms = LuckPermsProvider.get();
      } catch (IllegalStateException var2) {
         throw new PermissionHandlerException("Could not get api of LuckPerms", var2);
      }
   }

   public boolean addToGroup(OfflinePlayer player, UserGroup group) {
      Group newGroup = this.luckPerms.getGroupManager().getGroup(group.getGroupName());
      if (newGroup == null) {
         return false;
      } else {
         String playerName = player.getName();
         if (playerName == null) {
            return false;
         } else {
            User user = this.luckPerms.getUserManager().getUser(playerName);
            if (user == null) {
               return false;
            } else {
               InheritanceNode node = this.buildGroupNode(group);
               DataMutateResult result = user.data().add(node);
               if (result == DataMutateResult.FAIL) {
                  return false;
               } else {
                  this.luckPerms.getUserManager().saveUser(user);
                  return true;
               }
            }
         }
      }
   }

   public boolean hasGroupSupport() {
      return true;
   }

   public boolean hasPermissionOffline(String name, PermissionNode node) {
      User user = this.luckPerms.getUserManager().getUser(name);
      if (user == null) {
         this.logger.warning("LuckPermsHandler: tried to check permission for offline user " + name + " but it isn't loaded!");
         return false;
      } else {
         CachedPermissionData permissionData = user.getCachedData().getPermissionData(QueryOptions.builder(QueryMode.CONTEXTUAL).build());
         return permissionData.checkPermission(node.getNode()).asBoolean();
      }
   }

   public boolean isInGroup(OfflinePlayer player, UserGroup group) {
      String playerName = player.getName();
      if (playerName == null) {
         return false;
      } else {
         User user = this.luckPerms.getUserManager().getUser(playerName);
         if (user == null) {
            this.logger.warning("LuckPermsHandler: tried to check group for offline user " + player.getName() + " but it isn't loaded!");
            return false;
         } else {
            InheritanceNode inheritanceNode = (InheritanceNode)InheritanceNode.builder(group.getGroupName()).build();
            return user.data().contains(inheritanceNode, NodeEqualityPredicate.EXACT).asBoolean();
         }
      }
   }

   public boolean removeFromGroup(OfflinePlayer player, UserGroup group) {
      String playerName = player.getName();
      if (playerName == null) {
         return false;
      } else {
         User user = this.luckPerms.getUserManager().getUser(playerName);
         if (user == null) {
            this.logger.warning("LuckPermsHandler: tried to remove group for offline user " + player.getName() + " but it isn't loaded!");
            return false;
         } else {
            InheritanceNode groupNode = (InheritanceNode)InheritanceNode.builder(group.getGroupName()).build();
            boolean result = user.data().remove(groupNode) != DataMutateResult.FAIL;
            this.luckPerms.getUserManager().saveUser(user);
            return result;
         }
      }
   }

   public boolean setGroup(OfflinePlayer player, UserGroup group) {
      String playerName = player.getName();
      if (playerName == null) {
         return false;
      } else {
         User user = this.luckPerms.getUserManager().getUser(playerName);
         if (user == null) {
            this.logger.warning("LuckPermsHandler: tried to set group for offline user " + player.getName() + " but it isn't loaded!");
            return false;
         } else {
            InheritanceNode groupNode = this.buildGroupNode(group);
            DataMutateResult result = user.data().add(groupNode);
            if (result == DataMutateResult.FAIL) {
               return false;
            } else {
               user.data().clear((node) -> {
                  if (!(node instanceof InheritanceNode)) {
                     return false;
                  } else {
                     InheritanceNode inheritanceNode = (InheritanceNode)node;
                     return !inheritanceNode.equals(groupNode);
                  }
               });
               this.luckPerms.getUserManager().saveUser(user);
               return true;
            }
         }
      }
   }

   public List<UserGroup> getGroups(OfflinePlayer player) {
      String playerName = player.getName();
      if (playerName == null) {
         return Collections.emptyList();
      } else {
         User user = this.luckPerms.getUserManager().getUser(playerName);
         if (user == null) {
            this.logger.warning("LuckPermsHandler: tried to get groups for offline user " + player.getName() + " but it isn't loaded!");
            return Collections.emptyList();
         } else {
            return (List)user.getDistinctNodes().stream().filter((node) -> {
               return node instanceof InheritanceNode;
            }).map((node) -> {
               return (InheritanceNode)node;
            }).map((node) -> {
               Group group = this.luckPerms.getGroupManager().getGroup(node.getGroupName());
               return group == null ? null : new LuckPermGroup(group, node.getContexts());
            }).filter(Objects::nonNull).sorted((o1, o2) -> {
               return this.sortGroups(user, o1, o2);
            }).map((g) -> {
               return new UserGroup(g.getGroup().getName(), g.getContexts().toFlattenedMap());
            }).collect(Collectors.toList());
         }
      }
   }

   public PermissionsSystemType getPermissionSystem() {
      return PermissionsSystemType.LUCK_PERMS;
   }

   public void loadUserData(UUID uuid) throws PermissionLoadUserException {
      try {
         this.luckPerms.getUserManager().loadUser(uuid).get(5L, TimeUnit.SECONDS);
      } catch (ExecutionException | TimeoutException | InterruptedException var3) {
         throw new PermissionLoadUserException("Unable to load the permission data of the user " + uuid, var3);
      }
   }

   @NotNull
   private InheritanceNode buildGroupNode(UserGroup group) {
      ContextSetFactory contextSetFactory = this.luckPerms.getContextManager().getContextSetFactory();
      Builder builder = InheritanceNode.builder(group.getGroupName());
      if (group.getContextMap() != null) {
         group.getContextMap().forEach((k, v) -> {
            builder.withContext(contextSetFactory.immutableOf(k, v));
         });
      }

      return (InheritanceNode)builder.build();
   }

   private int sortGroups(User user, LuckPermGroup o1, LuckPermGroup o2) {
      Group group1 = o1.getGroup();
      Group group2 = o2.getGroup();
      if (!group1.getName().equals(user.getPrimaryGroup()) && !group2.getName().equals(user.getPrimaryGroup())) {
         int i = Integer.compare(group2.getWeight().orElse(0), group1.getWeight().orElse(0));
         return i != 0 ? i : group1.getName().compareToIgnoreCase(group2.getName());
      } else {
         return group1.getName().equals(user.getPrimaryGroup()) ? 1 : -1;
      }
   }
}
