/**
 * File: PunctuationToken
 * Date: 3/13/16
 * Author: ben risher
 * Purpose: class representing a Token housing a form of punctuation
 */
public class PunctuationToken extends BaseToken {
    private Type type = null;

    public enum Type {  // enum with input instead of default name
        LEFT_PAREN("("),
        COMMA(","),
        RIGHT_PAREN(")"),
        PERIOD("."),
        COLON(":"),
        SEMI_COLON(";");

        private String value;

        Type(String v) {
            this.value = v;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return this.value;
        }
    }  // end enum

    /**
     * Constructor
     * @param s sets punctuation type, see enum PunctuationToken.Type for list of possible types
     */
    public PunctuationToken(String s) {
        for (Type t: Type.values()) {
            if (t.value.equals(s)) {
                type = t;
                break;
            }
        }
    } // end constructor

    // getters / setters
    public Type getType() {
        return type;
    }
    public void setType(Type t) {
        type = t;
    }

    /**
     * simple getter to get the more friendly name of the token
     * @return String; type of token
     */
    public String getName() {
        return type.name();
    } // end getName

    public String toString() {
        if (type != null) {
            return "PunctuationToken<" + type.name() + ">";
        }
        else {
            return "";
        }
    } // end toString
} // end PunctuationToken
