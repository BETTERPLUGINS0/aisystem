package com.volmit.iris.util.decree.annotations;

import com.volmit.iris.util.decree.DecreeParameterHandler;
import com.volmit.iris.util.decree.specialhandlers.DummyHandler;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Param {
   String DEFAULT_DESCRIPTION = "No Description Provided";

   String name() default "";

   String description() default "No Description Provided";

   String defaultValue() default "";

   String[] aliases() default {""};

   boolean contextual() default false;

   Class<? extends DecreeParameterHandler<?>> customHandler() default DummyHandler.class;
}
