/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  net.md_5.bungee.api.chat.hover.content.Content
 *  net.md_5.bungee.api.chat.hover.content.Text
 */
package com.magmaguy.magmacore.util;

import com.magmaguy.magmacore.util.ChatColorConverter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;

public class SpigotMessage {
    private SpigotMessage() {
    }

    public static TextComponent simpleMessage(String message) {
        TextComponent wrapper = new TextComponent();
        for (BaseComponent component : TextComponent.fromLegacyText((String)ChatColorConverter.convert(message))) {
            wrapper.addExtra(component);
        }
        return wrapper;
    }

    public static TextComponent hoverMessage(String message, String hoverMessage) {
        TextComponent textComponent = SpigotMessage.simpleMessage(message);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Content[]{new Text(TextComponent.fromLegacyText((String)hoverMessage))}));
        return textComponent;
    }

    public static TextComponent commandHoverMessage(String message, String hoverMessage, String commandString) {
        TextComponent textComponent = SpigotMessage.hoverMessage(message, ChatColorConverter.convert(hoverMessage));
        if (!commandString.isEmpty()) {
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandString));
        }
        return textComponent;
    }

    public static TextComponent hoverLinkMessage(String message, String hoverMessage, String link) {
        TextComponent textComponent = SpigotMessage.hoverMessage(message, hoverMessage);
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
        return textComponent;
    }
}

