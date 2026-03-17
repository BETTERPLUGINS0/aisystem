package com.bergerkiller.bukkit.tc.commands.annotations;

import com.bergerkiller.bukkit.tc.Permission;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CommandRequiresMultiplePermissions.class)
public @interface CommandRequiresPermission {
   Permission value();
}
