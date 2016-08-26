package org.mcmonkey.denizen2core.tags.handlers;

import org.mcmonkey.denizen2core.tags.AbstractTagBase;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.tags.objects.TextTag;

public class EscapeTagBase extends AbstractTagBase {

    // <--[explanation]
    // @name Property Escaping
    // @group Useful Lists
    // @description
    // Some item properties (and corresponding mechanisms) need to escape their
    // text output/input to prevent players using them to cheat the system
    // (EG, if a player set the display name of an item to:
    //      'name;enchantments=damage_all,3', they would get a free enchantment!)
    // These are the escape codes used to prevent that:
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
    // @ = &at
    // . = &dot
    // \ = &bs
    // ' = &sq
    // " = &quo
    // ! = &exc
    // / = &fs
    // § = &ss
    //
    // Also, you can input a non-breaking space via &sp
    //
    // These symbols are automatically used by the internal system, if you are
    // writing your own property string and need to escape some symbols, you
    // can just directly type them in, EG: i@stick[display_name=&ltFancy&spStick&gt]
    // -->

    // <--[tagbase]
    // @Base escape[<TextTag>]
    // @Group Escaping
    // @ReturnType TextTag
    // @Returns an escaped copy of the input text. See <@link explanation Property Escaping>Property Escaping<@/link>.
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
                .replace("§", "&ss");
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
                .replace("&amp", "&");
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
