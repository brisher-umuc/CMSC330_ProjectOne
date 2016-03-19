/**
 * File: KeyWord
 * Date: 3/13/16
 * Author: ben risher
 * Purpose: class representing a Token housing a form of keyword
 */
public class KeyWord extends BaseToken {
    private static Type type = null;

    public enum Type {
        Window, End, Layout, Flow, Grid, Button, Group, Label, Panel, Textfield, Radio;
    }

    /**
     * Constructor
     * @param s sets keyword type, see enum KeyWord.Type for list of possible types
     */
    public KeyWord(String s) {
        type = Type.valueOf(s);
    }  // end constructor

    // getters / setters
    public static Type getType() {
        return type;
    }
    public static void setType(Type type) {
        KeyWord.type = type;
    }

    /**
     * simple getter to get the more friendly name of the token
     * @return String; type of token
     */
    public String getName() {
        if (type != null) {
            return type.name();
        }
        else {
            return "";
        }
    }  // end getName

    public String toString() {
        if (type != null) {
            return "KeyWord<" + type.name() + ">";
        }
        else {
            return "";
        }
    } // end toString
}  // end KeyWord
