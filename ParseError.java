/**
 * File: ParseError
 * Date: 3/13/16
 * Author: ben risher
 * Purpose: specify my own type of exception to throw during syntax checking
 */
public class ParseError extends Exception {
    ParseError(String msg) {
        super(msg);
    }
}  // end ParseError
