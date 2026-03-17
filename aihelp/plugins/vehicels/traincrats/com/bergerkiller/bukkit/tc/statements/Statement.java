package com.bergerkiller.bukkit.tc.statements;

import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class Statement {
   private static final List<Statement> statements = new ArrayList();

   public static String[] parseArray(String text) {
      return text.split(";", -1);
   }

   public static void init() {
      register(new StatementDestination());
      register(StatementBoolean.INSTANCE);
      register(new StatementRandom());
      register(new StatementProperty());
      register(new StatementName());
      register(new StatementEmpty());
      register(new StatementPassenger());
      register(new StatementOwners());
      register(new StatementTrainItems());
      register(new StatementFuel());
      register(new StatementType());
      register(new StatementVelocity());
      register(new StatementPlayerItems());
      register(new StatementPlayerHand());
      register(new StatementMob());
      register(new StatementRedstone());
      register(new StatementPermission());
      register(new StatementDirection());
      register(new StatementTag());
   }

   public static void deinit() {
      statements.clear();
   }

   public static <T extends Statement> T register(T statement) {
      int index = Collections.binarySearch(statements, statement, (a, b) -> {
         return Integer.compare(b.priority(), a.priority());
      });
      if (index < 0) {
         index = ~index;
      }

      for(int itemPriority = statement.priority(); index > 0 && ((Statement)statements.get(index - 1)).priority() == itemPriority; --index) {
      }

      statements.add(index, statement);
      return statement;
   }

   public static boolean has(MinecartMember<?> member, String text, SignActionEvent event) {
      return has(member, (MinecartGroup)null, text, event);
   }

   public static boolean has(MinecartGroup group, String text, SignActionEvent event) {
      return has((MinecartMember)null, group, text, event);
   }

   public static boolean has(MinecartMember<?> member, MinecartGroup group, String text, SignActionEvent event) {
      return Statement.Matcher.of(text).withMember(member).withGroup(group).withSignEvent(event).match().has();
   }

   public static boolean hasMultiple(MinecartMember<?> member, Iterable<String> statementTexts, SignActionEvent event) {
      return hasMultiple(member, (MinecartGroup)null, statementTexts, event);
   }

   public static boolean hasMultiple(MinecartGroup group, Iterable<String> statementTexts, SignActionEvent event) {
      return hasMultiple((MinecartMember)null, group, statementTexts, event);
   }

   public static boolean hasMultiple(MinecartMember<?> member, MinecartGroup group, Iterable<String> statementTexts, SignActionEvent event) {
      boolean match = true;
      Iterator var5 = statementTexts.iterator();

      while(var5.hasNext()) {
         String statementText = (String)var5.next();
         if (!statementText.isEmpty()) {
            boolean isLogicAnd = true;
            if (statementText.startsWith("&")) {
               isLogicAnd = true;
               statementText = statementText.substring(1);
            } else if (statementText.startsWith("|")) {
               isLogicAnd = false;
               statementText = statementText.substring(1);
            }

            boolean result = has(member, group, statementText, event);
            if (isLogicAnd) {
               match &= result;
            } else {
               match |= result;
            }
         }
      }

      return match;
   }

   public abstract boolean match(String var1);

   public abstract boolean matchArray(String var1);

   public boolean requiresTrain() {
      return true;
   }

   public boolean requiredEvent() {
      return false;
   }

   public boolean isConstant() {
      return false;
   }

   public int priority() {
      return 0;
   }

   public boolean hasRequiredContext(MinecartMember<?> member, MinecartGroup group, SignActionEvent event) {
      if (member == null && group == null && this.requiresTrain()) {
         return false;
      } else {
         return event != null || !this.requiredEvent();
      }
   }

   public boolean handle(MinecartGroup group, String text, SignActionEvent event) {
      Iterator var4 = group.iterator();

      MinecartMember member;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         member = (MinecartMember)var4.next();
      } while(!this.handle(member, text, event));

      return true;
   }

   public boolean handleArray(MinecartGroup group, String[] text, SignActionEvent event) {
      Iterator var4 = group.iterator();

      MinecartMember member;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         member = (MinecartMember)var4.next();
      } while(!this.handleArray(member, text, event));

      return true;
   }

   public boolean handle(MinecartMember<?> member, String text, SignActionEvent event) {
      return false;
   }

   public boolean handleArray(MinecartMember<?> member, String[] text, SignActionEvent event) {
      return false;
   }

   public static class Matcher {
      private final String text;
      private MinecartGroup group;
      private MinecartMember<?> member;
      private SignActionEvent signEvent;

      private Matcher(String text) {
         this.text = text;
      }

      public static Statement.Matcher of(String text) {
         return new Statement.Matcher(text);
      }

      public Statement.Matcher withGroup(MinecartGroup group) {
         this.group = group;
         return this;
      }

      public Statement.Matcher withMember(MinecartMember<?> member) {
         this.member = member;
         return this;
      }

      public Statement.Matcher withSignEvent(SignActionEvent event) {
         this.signEvent = event;
         return this;
      }

      public Statement.MatchResult match() {
         boolean inv = false;

         String text;
         for(text = TCConfig.statementShortcuts.replace(this.text); !text.isEmpty() && text.charAt(0) == '!'; inv = !inv) {
            text = text.substring(1);
         }

         if (text.isEmpty()) {
            return Statement.MatchResult.create(StatementBoolean.EMPTY, false, inv);
         } else {
            String lowerText = text.toLowerCase();
            int idx = lowerText.indexOf(64);
            String arrayText = idx == -1 ? null : lowerText.substring(0, idx);
            String[] array = idx == -1 ? null : Statement.parseArray(text.substring(idx + 1));
            Iterator var7 = Statement.statements.iterator();

            Statement statement;
            do {
               if (!var7.hasNext()) {
                  return Statement.MatchResult.createWithMissingContext(StatementBoolean.EMPTY, false, inv);
               }

               statement = (Statement)var7.next();
               if (arrayText != null && statement.matchArray(arrayText)) {
                  if (!statement.hasRequiredContext(this.member, this.group, this.signEvent)) {
                     return Statement.MatchResult.createWithMissingContext(statement, true, inv);
                  }

                  if (this.member != null) {
                     return Statement.MatchResult.create(statement, true, statement.handleArray(this.member, array, this.signEvent) != inv);
                  }

                  if (this.group != null) {
                     return Statement.MatchResult.create(statement, true, statement.handleArray(this.group, array, this.signEvent) != inv);
                  }

                  return Statement.MatchResult.create(statement, true, statement.handleArray((MinecartMember)null, array, this.signEvent) != inv);
               }
            } while(!statement.match(lowerText));

            if (!statement.hasRequiredContext(this.member, this.group, this.signEvent)) {
               return Statement.MatchResult.createWithMissingContext(statement, false, inv);
            } else if (this.member != null) {
               return Statement.MatchResult.create(statement, false, statement.handle(this.member, text, this.signEvent) != inv);
            } else if (this.group != null) {
               return Statement.MatchResult.create(statement, false, statement.handle(this.group, text, this.signEvent) != inv);
            } else {
               return Statement.MatchResult.create(statement, false, statement.handle((MinecartMember)null, text, this.signEvent) != inv);
            }
         }
      }
   }

   public static class MatchResult {
      private final Statement statement;
      private final boolean isArray;
      private final boolean isMissingContext;
      private final boolean has;

      public static Statement.MatchResult create(Statement statement, boolean isArray, boolean has) {
         return new Statement.MatchResult(statement, isArray, false, has);
      }

      public static Statement.MatchResult createWithMissingContext(Statement statement, boolean isArray, boolean inv) {
         return new Statement.MatchResult(statement, isArray, true, inv);
      }

      private MatchResult(Statement statement, boolean isArray, boolean isMissingContext, boolean has) {
         this.statement = statement;
         this.isArray = isArray;
         this.isMissingContext = isMissingContext;
         this.has = has;
      }

      public Statement statement() {
         return this.statement;
      }

      public boolean has() {
         return this.has;
      }

      public boolean isMissingContext() {
         return this.isMissingContext;
      }

      public boolean isArray() {
         return this.isArray;
      }

      public boolean isConstant() {
         return this.statement.isConstant();
      }

      public boolean isExactMatch() {
         if (this.statement == StatementBoolean.EMPTY) {
            return false;
         } else {
            return !(this.statement instanceof StatementTag) || this.isArray;
         }
      }
   }
}
