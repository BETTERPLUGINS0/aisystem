package advancedplugins.pm2.cv.handler;

import advancedplugins.pm2.cv.api.handler.PluginHandler;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.NotNull;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface PluginHandlerOptions {
   boolean eventListener() default false;

   boolean packetInjector() default false;

   @NotNull
   Class<? extends PluginHandler> apiClass() default PluginHandler.class;
}
