package com.nisovin.shopkeepers.config.lib.annotation;

import com.nisovin.shopkeepers.config.lib.value.ValueTypeProvider;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Repeatable(WithValueTypeProviders.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithValueTypeProvider {
   Class<? extends ValueTypeProvider> value();
}
