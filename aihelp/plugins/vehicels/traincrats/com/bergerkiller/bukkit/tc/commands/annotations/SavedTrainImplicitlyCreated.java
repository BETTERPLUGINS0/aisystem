package com.bergerkiller.bukkit.tc.commands.annotations;

import com.bergerkiller.bukkit.common.dep.cloud.parser.ParserParameter;
import com.bergerkiller.bukkit.common.dep.typetoken.TypeToken;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SavedTrainImplicitlyCreated {
   ParserParameter<Boolean> PARAM = new ParserParameter("savedtrain.implicitlycreated", TypeToken.get(Boolean.class));
}
