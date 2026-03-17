package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.tc.CollisionMode;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.standard.type.CollisionMobCategory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class StatementProperty extends Statement {
   private ArrayList<String> properties = new ArrayList();
   private String[] maxspeed = this.add("maxspeed", "speedlimit");
   private String[] playerEnter = this.add("playerenter", "playersenter");
   private String[] playerExit = this.add("playerexit", "playersexit");
   private String[] mobEnter = this.add("mobenter", "mobsenter");

   private String[] add(String... properties) {
      Collections.addAll(this.properties, properties);
      return properties;
   }

   private boolean match(String[] property, String text) {
      String[] var3 = property;
      int var4 = property.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String propval = var3[var5];
         if (text.startsWith(propval)) {
            return true;
         }
      }

      return false;
   }

   public boolean match(String text) {
      Iterator var2 = this.properties.iterator();

      String property;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         property = (String)var2.next();
      } while(!text.startsWith(property));

      return true;
   }

   public boolean matchArray(String text) {
      return false;
   }

   public boolean handle(MinecartGroup group, String text, SignActionEvent event) {
      TrainProperties prop = group.getProperties();
      String lower = text.toLowerCase(Locale.ENGLISH);
      if (this.match(this.maxspeed, lower)) {
         return Util.evaluate(prop.getSpeedLimit(), text);
      } else if (this.match(this.playerEnter, lower)) {
         return prop.getPlayersEnter();
      } else if (this.match(this.playerExit, lower)) {
         return prop.getPlayersExit();
      } else if (this.match(this.mobEnter, lower)) {
         return prop.getCollision().mobModes().values().contains(CollisionMode.ENTER);
      } else {
         CollisionMobCategory category = CollisionMobCategory.findMobType(lower, (String)null, "enter");
         if (category != null) {
            return prop.getCollision().mobMode(category) == CollisionMode.ENTER;
         } else {
            return super.handle(group, text, event);
         }
      }
   }

   public boolean handle(MinecartMember<?> member, String text, SignActionEvent event) {
      CartProperties prop = member.getProperties();
      String lower = text.toLowerCase(Locale.ENGLISH);
      if (this.match(this.playerEnter, lower)) {
         return prop.getPlayersEnter();
      } else {
         return this.match(this.playerExit, lower) ? prop.getPlayersExit() : this.handle(member.getGroup(), text, event);
      }
   }

   public boolean requiredEvent() {
      return false;
   }
}
