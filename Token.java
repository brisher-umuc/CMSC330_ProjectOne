/**
 * File: Token
 * Date: 3/13/16
 * Author: ben risher
 * Purpose: class representing a Token
 */
public class Token extends BaseToken {
    private String s_value;
    private Double d_value;

    /**
     * Constructor
     * @param s sets string value of the token
     */
    public Token(String s) {
        s_value = s;
    }  // end string constructor

    /**
     * Constructor
     * @param s sets double value of the token
     */
    public Token(Double d) {
        d_value = d;
    }  // end double constructor

    // getters / setters
    public Double getD_value() {
        return d_value;
    }
    public void setD_value(Double d) {
        d_value = d;
    }
    public String getS_value() {
        return s_value;
    }
    public void setS_value(String s) {
        s_value = s;
    }

    /**
     * simple getter to get the more friendly name of the token
     * @return String; type of token
     */
    public String getName() {
        if (s_value != null) {
            return s_value;
        }
        else {
            return d_value.toString();
        }
    } // end getName

    public String toString() {
        if (s_value != null) {
            return "Token<" + s_value + ">";
        }
        else {
            return "Token<" + d_value.toString() + ">";
        }
    } // end toString
} // end Token
