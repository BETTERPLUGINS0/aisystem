package fr.xephi.authme.libs.ch.jalu.configme.beanmapper.leafvaluehandler;

import fr.xephi.authme.libs.ch.jalu.configme.utils.TypeInformation;
import javax.annotation.Nullable;

public interface LeafValueHandler {
   @Nullable
   Object convert(TypeInformation var1, @Nullable Object var2);

   @Nullable
   Object toExportValue(@Nullable Object var1);
}
