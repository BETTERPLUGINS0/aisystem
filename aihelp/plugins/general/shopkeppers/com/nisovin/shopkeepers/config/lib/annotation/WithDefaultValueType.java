package com.nisovin.shopkeepers.config.lib.annotation;

import com.nisovin.shopkeepers.config.lib.value.ValueType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Repeatable(WithDefaultValueTypes.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithDefaultValueType {
   Class<?> fieldType();

   Class<? extends ValueType<?>> valueType();
}
