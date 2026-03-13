package com.volmit.iris.engine.object.annotations;

import com.volmit.iris.core.loader.IrisRegistrant;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.FIELD})
public @interface RegistryListResource {
   Class<? extends IrisRegistrant> value();
}
