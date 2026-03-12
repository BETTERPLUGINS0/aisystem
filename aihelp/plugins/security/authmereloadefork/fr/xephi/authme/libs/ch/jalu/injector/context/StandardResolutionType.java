package fr.xephi.authme.libs.ch.jalu.injector.context;

public enum StandardResolutionType implements ResolutionType {
   SINGLETON,
   REQUEST_SCOPED,
   REQUEST_SCOPED_IF_HAS_DEPENDENCIES;
}
