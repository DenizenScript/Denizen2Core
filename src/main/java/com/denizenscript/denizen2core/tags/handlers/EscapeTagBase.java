package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.TextTag;

public class EscapeTagBase extends AbstractTagBase {

    // <--[explanation]
// @Since 0.3.0
    // @name Escaping
    // @group Escaping
    // @description
    // There are several text codes ("escape codes") to prevent errors while parsing complicated text.
    // For example, to properly include a pipe (the | symbol) in a list without it reading as a new list item.
    // These are those codes:
    //
    // | = &pipe
    // < = &lt
    // > = &gt
    // newline = &nl
    // & = &amp
    // ; = &sc
    // [ = &lb
    // ] = &rb
    // : = &co
    // . = &dot
    // \ = &bs
    // ' = &sq
    // " = &quo
    // ! = &exc
    // / = &fs
    // § = &ss
    // and finally @ = &at
    //
    // Also, you can input a non-breaking space via &sp
    // -->

    // <--[tagbase]
// @Since 0.3.0
    // @Base escape[<TextTag>]
    // @Group Escaping
    // @ReturnType TextTag
    // @Returns an escaped copy of the input text. See <@link explanation Escaping>Escaping<@/link>.
    // -->

    /**
     * A quick function to escape text.
     *
     * @param input the unescaped data.
     * @return the escaped data.
     */
    public static String escape(String input) {
        if (input == null) {
            return null;
        }
        return input
                .replace("&", "&amp").replace("|", "&pipe")
                .replace(">", "&gt").replace("<", "&lt")
                .replace("\n", "&nl").replace(";", "&sc")
                .replace("[", "&lb").replace("]", "&rb")
                .replace(":", "&co").replace("@", "&at")
                .replace(".", "&dot").replace("\\", "&bs")
                .replace("'", "&sq").replace("\"", "&quo")
                .replace("!", "&exc").replace("/", "&fs")
                .replace("#", "&ns").replace("§", "&ss");
    }

    /**
     * A quick function to reverse a text escaping.
     *
     * @param input the escaped data.
     * @return the unescaped data.
     */
    public static String unescape(String input) {
        if (input == null) {
            return null;
        }
        return input
                .replace("&pipe", "|").replace("&nl", "\n")
                .replace("&gt", ">").replace("&lt", "<")
                .replace("&sc", ";").replace("&sq", "'")
                .replace("&lb", "[").replace("&rb", "]")
                .replace("&sp", String.valueOf((char) 0x00A0))
                .replace("&co", ":").replace("&at", "@")
                .replace("&dot", ".").replace("&bs", "\\")
                .replace("&quo", "\"").replace("&exc", "!")
                .replace("&fs", "/").replace("&ss", "§")
                .replace("&ns", "#").replace("&amp", "&");
    }

    @Override
    public String getName() {
        return "escape";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return new TextTag(escape(data.getNextModifier().toString())).handle(data.shrink());
    }
}
