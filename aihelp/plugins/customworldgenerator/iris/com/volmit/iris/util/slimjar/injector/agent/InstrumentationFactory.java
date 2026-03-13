package com.volmit.iris.util.slimjar.injector.agent;

import com.volmit.iris.util.slimjar.exceptions.InjectorException;
import java.lang.instrument.Instrumentation;
import org.jetbrains.annotations.NotNull;

public interface InstrumentationFactory {
   @NotNull
   Instrumentation create() throws InjectorException;
}
