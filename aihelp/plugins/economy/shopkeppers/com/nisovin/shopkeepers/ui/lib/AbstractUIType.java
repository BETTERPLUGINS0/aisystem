package com.nisovin.shopkeepers.ui.lib;

import com.nisovin.shopkeepers.api.ui.UIType;
import com.nisovin.shopkeepers.types.AbstractType;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractUIType extends AbstractType implements UIType {
   protected AbstractUIType(String identifier, @Nullable String permission) {
      super(identifier, permission);
   }
}
