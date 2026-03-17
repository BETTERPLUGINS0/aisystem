/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.kyori.adventure.text.serializer.legacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextFormat;
import net.kyori.adventure.text.serializer.legacy.CharacterAndFormat;

final class CharacterAndFormatSet {
    static final CharacterAndFormatSet DEFAULT = CharacterAndFormatSet.of(CharacterAndFormat.defaults());
    final List<TextFormat> formats;
    final List<TextColor> colors;
    final String characters;

    static CharacterAndFormatSet of(List<CharacterAndFormat> list) {
        int n = list.size();
        ArrayList<TextColor> arrayList = new ArrayList<TextColor>();
        ArrayList<TextFormat> arrayList2 = new ArrayList<TextFormat>(n);
        StringBuilder stringBuilder = new StringBuilder(n);
        for (int i = 0; i < n; ++i) {
            CharacterAndFormat characterAndFormat = list.get(i);
            char c = characterAndFormat.character();
            TextFormat textFormat = characterAndFormat.format();
            boolean bl = textFormat instanceof TextColor;
            stringBuilder.append(c);
            arrayList2.add(textFormat);
            if (bl) {
                arrayList.add((TextColor)textFormat);
            }
            if (!characterAndFormat.caseInsensitive()) continue;
            boolean bl2 = false;
            if (Character.isUpperCase(c)) {
                stringBuilder.append(Character.toLowerCase(c));
                bl2 = true;
            } else if (Character.isLowerCase(c)) {
                stringBuilder.append(Character.toUpperCase(c));
                bl2 = true;
            }
            if (!bl2) continue;
            arrayList2.add(textFormat);
            if (!bl) continue;
            arrayList.add((TextColor)textFormat);
        }
        if (arrayList2.size() != stringBuilder.length()) {
            throw new IllegalStateException("formats length differs from characters length");
        }
        return new CharacterAndFormatSet(Collections.unmodifiableList(arrayList2), Collections.unmodifiableList(arrayList), stringBuilder.toString());
    }

    CharacterAndFormatSet(List<TextFormat> list, List<TextColor> list2, String string) {
        this.formats = list;
        this.colors = list2;
        this.characters = string;
    }
}

