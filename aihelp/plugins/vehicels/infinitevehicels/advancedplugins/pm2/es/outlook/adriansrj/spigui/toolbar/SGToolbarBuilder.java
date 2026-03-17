package es.outlook.adriansrj.spigui.toolbar;

import es.outlook.adriansrj.spigui.buttons.SGButton;
import es.outlook.adriansrj.spigui.menu.SGMenu;

public interface SGToolbarBuilder {
   SGButton buildToolbarButton(int var1, int var2, SGToolbarButtonType var3, SGMenu var4);
}
