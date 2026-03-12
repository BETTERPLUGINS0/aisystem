package com.nisovin.shopkeepers.types;

import com.nisovin.shopkeepers.api.types.Type;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractType implements Type {
   protected final String identifier;
   protected final List<? extends String> aliases;
   @Nullable
   protected final String permission;

   protected AbstractType(String identifier, @Nullable String permission) {
      this(identifier, Collections.emptyList(), permission);
   }

   protected AbstractType(String identifier, List<? extends String> aliases, @Nullable String permission) {
      this.identifier = StringUtils.normalize(identifier);
      Validate.notEmpty(this.identifier, "identifier is null or empty");
      Validate.notNull(aliases, (String)"aliases is null");
      if (aliases.isEmpty()) {
         this.aliases = Collections.emptyList();
      } else {
         List<String> normalizedAliases = new ArrayList(aliases.size());
         Iterator var5 = aliases.iterator();

         while(var5.hasNext()) {
            String alias = (String)var5.next();
            Validate.notEmpty(alias, "aliases contains null or empty alias");
            normalizedAliases.add(StringUtils.normalize(alias));
         }

         this.aliases = Collections.unmodifiableList(normalizedAliases);
      }

      this.permission = StringUtils.isEmpty(permission) ? null : permission;
   }

   public final String getIdentifier() {
      return this.identifier;
   }

   public Collection<? extends String> getAliases() {
      return this.aliases;
   }

   @Nullable
   public String getPermission() {
      return this.permission;
   }

   public boolean hasPermission(Player player) {
      return this.permission != null ? PermissionUtils.hasPermission(player, this.permission) : true;
   }

   public boolean isEnabled() {
      return true;
   }

   public boolean matches(String identifier) {
      Validate.notNull(identifier, (String)"identifier is null");
      String normalized = StringUtils.normalize(identifier);
      if (normalized.equals(this.identifier)) {
         return true;
      } else if (this.aliases.contains(normalized)) {
         return true;
      } else {
         String displayName = StringUtils.normalize(this.getDisplayName());
         return normalized.equals(displayName);
      }
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getClass().getName());
      builder.append(" [identifier=");
      builder.append(this.identifier);
      builder.append("]");
      return builder.toString();
   }

   public final int hashCode() {
      return super.hashCode();
   }

   public final boolean equals(@Nullable Object obj) {
      return super.equals(obj);
   }
}
