package com.volmit.iris.core.gui.components;

import java.awt.Color;

@FunctionalInterface
public interface Renderer {
   Color draw(double x, double z);
}
