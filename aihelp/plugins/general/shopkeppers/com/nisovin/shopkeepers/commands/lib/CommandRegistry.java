package com.nisovin.shopkeepers.commands.lib;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.Nullable;

public class CommandRegistry {
   private final Command parent;
   private final Set<Command> commands = new LinkedHashSet();
   private final Set<Command> commandsView;
   private final Map<String, Command> commandsByAlias;
   private final Map<String, Command> commandsByAliasView;

   public CommandRegistry(@UnknownInitialization Command parent) {
      this.commandsView = Collections.unmodifiableSet(this.commands);
      this.commandsByAlias = new LinkedHashMap();
      this.commandsByAliasView = Collections.unmodifiableMap(this.commandsByAlias);
      Validate.notNull(parent, (String)"parent is null");
      this.parent = (Command)Unsafe.initialized(parent);
   }

   public Command getParent() {
      return this.parent;
   }

   public void register(Command command) {
      Validate.notNull(command, (String)"command is null");
      Validate.isTrue(command.getParent() == null, "command has already been registered somewhere");
      Validate.isTrue(!this.commands.contains(command), "command is already registered");
      String name = CommandUtils.normalize(command.getName());
      Validate.isTrue(!this.commandsByAlias.containsKey(name), "Another command with this name is already registered: " + name);
      this.commandsByAlias.put(name, command);
      Iterator var3 = command.getAliases().iterator();

      while(var3.hasNext()) {
         String alias = (String)var3.next();
         alias = CommandUtils.normalize(alias);
         this.commandsByAlias.putIfAbsent(alias, command);
      }

      this.commands.add(command);
      command.setParent(this.parent);
   }

   public boolean isRegistered(Command command) {
      return this.commands.contains(command);
   }

   public void unregister(Command command) {
      Validate.notNull(command, (String)"command is null");
      Validate.isTrue(command.getParent() == this.parent, "command is registered somewhere else");
      Validate.isTrue(this.commands.contains(command), "command is not registered here");
      String name = CommandUtils.normalize(command.getName());

      assert this.commandsByAlias.get(name) == command;

      this.commandsByAlias.remove(name);
      Iterator var3 = command.getAliases().iterator();

      while(var3.hasNext()) {
         String alias = (String)var3.next();
         alias = CommandUtils.normalize(alias);
         this.commandsByAlias.remove(alias, command);
      }

      this.commands.remove(command);
      command.setParent((Command)null);
   }

   public Collection<? extends Command> getCommands() {
      return this.commandsView;
   }

   @Nullable
   public Command getCommand(String alias) {
      Validate.notNull(alias, (String)"alias is null");
      return (Command)this.commandsByAlias.get(CommandUtils.normalize(alias));
   }

   public Set<? extends String> getAliases() {
      return this.commandsByAliasView.keySet();
   }

   public Map<? extends String, ? extends Command> getAliasesMap() {
      return this.commandsByAliasView;
   }

   public List<? extends String> getAliases(Command command) {
      if (this.commandsByAlias.isEmpty()) {
         return Collections.emptyList();
      } else {
         List<String> aliases = new ArrayList();
         Iterator var3 = this.commandsByAlias.entrySet().iterator();

         while(var3.hasNext()) {
            Entry<? extends String, ? extends Command> entry = (Entry)var3.next();
            if (entry.getValue() == command) {
               aliases.add((String)entry.getKey());
            }
         }

         return Collections.unmodifiableList(aliases);
      }
   }
}
