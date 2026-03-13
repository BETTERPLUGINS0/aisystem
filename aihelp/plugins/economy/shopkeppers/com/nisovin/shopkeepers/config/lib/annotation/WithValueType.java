package com.nisovin.shopkeepers.config.lib.annotation;

import com.nisovin.shopkeepers.config.lib.value.ValueType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WithValueType {
   Class<? extends ValueType<?>> value();
}
