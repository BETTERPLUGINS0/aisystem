package com.lenis0012.bukkit.loginsecurity.session.action;

@FunctionalInterface
public interface ActionCallback {
   ActionCallback EMPTY = (response) -> {
   };

   void call(ActionResponse var1);
}
