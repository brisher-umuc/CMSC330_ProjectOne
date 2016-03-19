import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * File: Parser
 * Date: 3/13/16
 * Author: ben risher
 * Purpose: takes tokenized input stream and parses each token based off of the
 *     provided grammar, producing a gui if no errors are encountered
 */
public class Parser {
    private Tokenizer stream;
    private JFrame window;
    public enum widget {
        Button, Group, Label, Panel, Textfield;
    }

    /**
     * Constructor
     * @param st Tokenizer instance
     */
    public Parser(Tokenizer st) {
        stream = st;
    }  // end constructor

    /**
     * processes 'gui' from the grammar
     * gui ::=
     *     Window STRING '(' NUMBER ',' NUMBER ')' layout widgets End '.'
     * @throws ParseError
     */
    public void getGui() throws ParseError {
        Double xDimension = 0.0, yDimension = 0.0;

        // if string is next, set frame title to that string
        if (stream.peek() instanceof Token && ((Token) stream.peek()).getS_value() != null) {  // string for window is good
            window.setTitle(stream.getToken().getName());
        }
        else {
            throw new ParseError("LINE: " + stream.lineno() + " -- Expected: 'Window' name enclosed in quotes");
        }

        if (!stream.getToken().getName().equals("LEFT_PAREN")) {  // next is (
            throw new ParseError("LINE: " + stream.lineno() + " -- Expected: An opening parenthesis '(' ");
        }

        if (stream.peek() instanceof Token && ((Token) stream.peek()).getD_value() != null) { // next is some double
            xDimension = ((Token) stream.getToken()).getD_value();
        }
        else {
            throw new ParseError("LINE: " + stream.lineno() + " -- Expected: X-Dimension Value for 'Window'; Got: " + stream.sval + " | " + String.valueOf(stream.nval));

        }

        if (!stream.getToken().getName().equals("COMMA")) {  // next is ,
            throw new ParseError("LINE: " + stream.lineno() + " -- Expected: a comma ',' separating X-Dimension and Y-Dimension values.");
        }

        if (stream.peek() instanceof Token && ((Token) stream.peek()).getD_value() != null) { // next is some double
            yDimension = ((Token) stream.getToken()).getD_value();
        }
        else {
            throw new ParseError("LINE: " + stream.lineno() + " -- Expected: Y-Dimension Value for 'Window'; Got: " + stream.sval + " | " + String.valueOf(stream.nval));
        }

        if (!stream.getToken().getName().equals("RIGHT_PAREN")) {  // next is )
            throw new ParseError("LINE: " + stream.lineno() + " -- Expected: A closing parenthesis ')' ");
        }

        // if we made it this far, we got (DOUBLE, DOUBLE) and can assign those values as the size of the frame
        //window.setPreferredSize(new Dimension(xDimension.intValue(), yDimension.intValue()));
        window.setPreferredSize(new Dimension(xDimension.intValue(), yDimension.intValue()));

        getLayout(window);  // call next step in the parser
        getWidgets(window); // and the next after that
    }  // end getGUI

    /**
     * processes 'layout' from the grammar
     * layout ::=
     *        Layout layout_type ':'
     * @param container parent of jpanel and jframe to handle passing either one in here to add components
     * @throws ParseError
     */
    public void getLayout(Container container) throws ParseError{
        if (!stream.getToken().getName().equals("Layout")) {
            throw new ParseError("LINE: " + stream.lineno() + " -- Expected: 'Layout'; Got: " + stream.sval);
        }

        // got Layout keyword, moving on
        getLayoutType(container);

        if (!stream.getToken().getName().equals("COLON")) {
            throw new ParseError("LINE: " + stream.lineno() + " -- Expected: ':' ");
        }
    }  // end getLayout

    /**
     * processes 'layout_type' from the grammar
     * layout_type ::=
     *     Flow |
     *     Grid '(' NUMBER ',' NUMBER [',' NUMBER ',' NUMBER] ')'
     * @param container parent of jpanel and jframe to handle passing either one in here to add components
     * @throws ParseError
     */
    public void getLayoutType(Container container) throws ParseError {
        if (stream.peek().getName().equals("Flow")) {
            stream.getToken();
            container.setLayout(new FlowLayout());
        } else if (stream.peek().getName().equals("Grid")) {
            Double xDim = 0.0, yDim = 0.0, hGapDim = 0.0, vGapDim = 0.0;
            stream.getToken();

            if (!stream.getToken().getName().equals("LEFT_PAREN")) {  // next is (
                throw new ParseError("LINE: " + stream.lineno() + " -- Expected: An opening parenthesis '(' ");
            }

            if (stream.peek() instanceof Token && ((Token) stream.peek()).getD_value() != null) { // next is some double
                xDim = ((Token) stream.getToken()).getD_value();
            } else {
                throw new ParseError("LINE: " + stream.lineno() + " -- Expected: X-Dimension Value for 'Grid'; Got: " + stream.sval + " | " + String.valueOf(stream.nval));
            }

            if (!stream.getToken().getName().equals("COMMA")) {  // next is ,
                throw new ParseError("LINE: " + stream.lineno() + " -- Expected: a comma separating X-Dimension and Y-Dimension values.");
            }

            if (stream.peek() instanceof Token && ((Token) stream.peek()).getD_value() != null) { // next is some double
                yDim = ((Token) stream.getToken()).getD_value();
            } else {
                throw new ParseError("LINE: " + stream.lineno() + " -- Expected: Y-Dimension Value for 'Grid'; Got: " + stream.sval + " | " + String.valueOf(stream.nval));
            }

            if (stream.peek().getName().equals("COMMA")) {  // optional hgap and vgap
                stream.getToken();

                if (stream.peek() instanceof Token && ((Token) stream.peek()).getD_value() != null) { // next is some double
                    hGapDim = ((Token) stream.getToken()).getD_value();
                } else {
                    throw new ParseError("LINE: " + stream.lineno() + " -- Expected: H-Gap-Dimension Value for 'Grid'; Got: " + stream.sval + " | " + String.valueOf(stream.nval));
                }

                if (!stream.getToken().getName().equals("COMMA")) {  // next is ,
                    throw new ParseError("LINE: " + stream.lineno() + " -- Expected: a comma ',' separating H-Gap-Dimension and V-Gap-Dimension values.");
                }

                if (stream.peek() instanceof Token && ((Token) stream.peek()).getD_value() != null) { // next is some double
                    vGapDim = ((Token) stream.getToken()).getD_value();
                } else {
                    throw new ParseError("LINE: " + stream.lineno() + " -- Expected: V-Gap-Dimension Value for 'Grid'; Got: " + stream.sval + " | " + String.valueOf(stream.nval));
                }

                if (!stream.getToken().getName().equals("RIGHT_PAREN")) {  // just two things
                    throw new ParseError("LINE: " + stream.lineno() + " -- Expected: An closing parenthesis ')' ");
                }

            // we made it to here, so all 4 args to GridLayout are good to go
            container.setLayout(new GridLayout(xDim.intValue(), yDim.intValue(), hGapDim.intValue(), vGapDim.intValue()));

            } else if (stream.peek().getName().equals("RIGHT_PAREN")) {  // just two things
                container.setLayout(new GridLayout(xDim.intValue(), yDim.intValue()));
                stream.getToken();
            } else {
                throw new ParseError("LINE: " + stream.lineno() + " -- Expected: An closing parenthesis ')' or a comma ',' ");
            }
        } else {
            throw new ParseError("LINE: " + stream.lineno() + " -- Expected: 'Flow' or 'Grid'; Got: " + stream.sval);
        }
    }  // end getLayoutType

    /**
     * processes 'widgets' from the grammar
     * widgets ::=
     *     widget widgets |
     *     widget
     * @param container parent of jpanel and jframe to handle passing either one in here to add components
     * @throws ParseError
     */
    public void getWidgets(Container container) throws ParseError{
        // first recursive definition
        if (isWidget(stream.peek().getName())) {
            try {
                getWidget(container);
            }
            catch (ParseError e) {
                e.printStackTrace();
                System.exit(-1);
            }

            getWidgets(container);
        }
        else if (stream.peek().getName().equals("End.")) {
            stream.getToken();
        }
        else if (stream.peek().getName().equals("End")) {
            // pass, this is handled as validation in the call to Panel
        }
        else {
            throw new ParseError("LINE: " + stream.lineno() + " -- Expected: a Widget or 'End.'; Got: " + stream.sval);
        }
    }  // end getWidgets

    /**
     * processes 'widget' from the grammar
     *    widget ::=
     *        Button STRING ';' |
     *        Group radio_buttons End ';' |
     *        Label STRING ';' |
     *        Panel layout widgets End ';' |
     *        Textfield NUMBER ';'
     * @param container parent of jpanel and jframe to handle passing either one in here to add components
     * @throws ParseError
     */
    public void getWidget(Container container) throws ParseError{
        String buttonName = "", labelName = "";
        Double textFieldNumber = 0.0;

        if (stream.peek().getName().equals("Button")) {
            stream.getToken();

            if (stream.peek() instanceof Token && ((Token) stream.peek()).getS_value() != null) {  // button name
                buttonName = stream.getToken().getName();
            }
            else {
                throw new ParseError("LINE: " + stream.lineno() + " -- Bad argument to 'Button'.");
            }

            if (lineEnds()) {
                container.add(new JButton(buttonName));
            }
            else {
                throw new ParseError("LINE: " + stream.lineno() + " -- Missing ';'");
            }
        }  // end if Button
        else if (stream.peek().getName().equals("Group")) {
            stream.getToken();
            ButtonGroup group = new ButtonGroup();

            getRadioButtons(container, group);

            if (stream.peek().getName().equals("End")) {
                stream.getToken();
                if (lineEnds()) {
                    // TODO: this should probably happen somewhere else for Group keyword
                }
                else {
                    throw new ParseError("LINE: " + stream.lineno() + " -- Missing ';'");
                }
            }
            else {
                throw new ParseError("LINE: " + stream.lineno() + " -- Expected: 'End'; Got: " + stream.sval);
            }
        }  // end if Group
        else if (stream.peek().getName().equals("Label")) {
            stream.getToken();

            if (stream.peek() instanceof Token && ((Token) stream.peek()).getS_value() != null) {  // label name
                labelName = stream.getToken().getName();
            }
            else {
                throw new ParseError("LINE: " + stream.lineno() + " -- Bad argument to 'Label'.");
            }

            if (lineEnds()) {
                container.add(new JLabel(labelName));
            }
            else {
                throw new ParseError("LINE: " + stream.lineno() + " -- Missing ';'");
            }


        }  // end if Label
        else if (stream.peek().getName().equals("Panel")) {
            stream.getToken();
            JPanel panel = new JPanel();

            try {
                getLayout(panel);
            }
            catch (ParseError e) {
                e.printStackTrace();
                System.exit(-1);
            }

            getWidgets(panel);
            if (stream.peek().getName().equals("End")) {
                stream.getToken();
                if (lineEnds()) {
                    container.add(panel);
                }
                else {
                    throw new ParseError("LINE: " + stream.lineno() + " -- Missing ';'");
                }
            }
            else {
                throw new ParseError("LINE: " + stream.lineno() + " -- Expected: 'End'; Got: " + stream.sval);
            }
        }  // end if Panel
        else if (stream.peek().getName().equals("Textfield")) {
            stream.getToken();

            if (stream.peek() instanceof Token && ((Token) stream.peek()).getD_value() != null) {
                textFieldNumber = ((Token) stream.getToken()).getD_value();
            }
            else {
                throw new ParseError("LINE: " + stream.lineno() + " -- Bad argument to 'Textfield'.");
            }
            if (lineEnds()) {
                container.add(new TextField(textFieldNumber.intValue()));
            }
            else {
                throw new ParseError("LINE: " + stream.lineno() + " -- Missing ';'");
            }

        }  // end if Textfield
        else {
            throw new ParseError("LINE: " + stream.lineno() + " -- " + stream.sval + " is not a valid Widget.");
        }
    } // end getWidget

    /**
     * processes 'radio_buttons' from the grammar
     *    radio_buttons ::=
     *        radio_button radio_buttons |
     *        radio_button
     * @param container parent of jpanel and jframe to handle passing either one in here to add components
     * @param group ButtonGroup, what the radiobuttons get added to
     * @throws ParseError
     */
    public void getRadioButtons(Container container, ButtonGroup group) throws ParseError{
        // this is totally wrong
        //TODO: what did i mean by the above comment....???
        if (stream.peek().getName().equals("Radio")) {
            try {
                getRadioButton(container, group);
            }
            catch (ParseError e) {
                e.printStackTrace();
                System.exit(-1);
            }
            getRadioButtons(container, group);
        }
        else if (stream.peek().getName().equals("End.")) {
            stream.getToken();
        }
        else if (stream.peek().getName().equals("End")) {
            // pass, this is handled as validation in the call to Group
        }
        else {
            throw new ParseError("LINE: " + stream.lineno() + " -- Expected: 'Radio' or 'End.'; Got: " + stream.sval);
        }
    } // end getRadioButtons

    /**
     * processes 'radio_button' from the grammar
     *    radio_button ::=
     *        Radio STRING ';'
     * @param container parent of jpanel and jframe to handle passing either one in here to add components
     * @param group ButtonGroup, what the radiobuttons get added to
     * @throws ParseError
     */
    public void getRadioButton(Container container, ButtonGroup group) throws ParseError {
        stream.getToken();
        String buttonName = "";

        if (stream.peek() instanceof Token && ((Token) stream.peek()).getS_value() != null) {  // radiobutton name
            buttonName = stream.getToken().getName();
        }
        else {
            throw new ParseError("LINE: " + stream.lineno() + " -- Bad argument to 'Radio'.");
        }

        if (lineEnds()) {
            JRadioButton jrb = new JRadioButton(buttonName);
            group.add(jrb);
            container.add(jrb);
        }
        else {
            throw new ParseError("LINE: " + stream.lineno() + " -- Missing ';'");
        }
    }  // end getRadioButton

    /**
     * handles checking line ends for required semi-colon
     * @return boolean
     */
    private boolean lineEnds() {
        if (!stream.getToken().getName().equals("SEMI_COLON")) {
            return false;
        }
        return true;
    }  // end lineEnds helper

    /**
     * checks if a widget is of a valid type
     *     VALID CHOICES: Button, Group, Label, Panel, Textfield;
     * @param type name of the widget type
     * @return boolean
     */
    private boolean isWidget(String type) {
        try {
            widget.valueOf(type);
            return true;
        }
        catch (IllegalArgumentException e) {
            return false;
        }
    } // end isWidget helper

    /**
     * Sets up the base JFrame and kicks off the parser's grammar parsing functions
     * @throws ParseError
     */
    public void build() throws ParseError {
        if (stream.peek().getName().equals("Window")) {
            SwingUtilities.invokeLater(() -> {
                stream.getToken();
                window = new JFrame();
                window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                try {
                    getGui();
                }
                catch (ParseError e) {
                    e.printStackTrace();
                    System.exit(-1);
                }

                window.pack();
                window.setVisible(true);
            });
        }
        else {
            throw new ParseError("LINE: " + stream.lineno() + " -- Expected: 'Window'; Got: " + stream.sval);
        }
    } // end build

    public static void main(String[] args) {
        ArrayList<String> fileList = new ArrayList<>();
        fileList.add("input.txt");
        fileList.add("input2.txt");
        fileList.add("input3.txt");
        try {
            for (String st: fileList) {
                FileReader reader = new FileReader(st);
                Parser gb = new Parser(new Tokenizer(reader));
                gb.build();
            }
        }
        catch (IOException|ParseError e) {
            e.printStackTrace();
        }
    }  // end main
}  // end Parser

