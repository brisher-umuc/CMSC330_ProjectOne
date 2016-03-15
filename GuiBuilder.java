import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;

/**
 * File: GuiBuilder
 * Date: 3/13/16
 * Author: ben risher
 * Purpose:
 */
public class GuiBuilder {
    private Tokenizer stream;
    private JFrame window;
    public enum widget {
        Button, Group, Label, Panel, Textfield;
    }

    public GuiBuilder(Tokenizer st) {
        stream = st;
    }

    public void getGui() {
        Double xDimension = 0.0, yDimension = 0.0;

        // if string is next, set frame title to that string
        if (stream.peek() instanceof Token && ((Token) stream.peek()).getS_value() != null) {  // string for window is good
            window.setTitle(stream.getToken().getName());
        }
        else {
            System.out.println("You suck Window Name");
            System.exit(-1);
        }

        if (!stream.getToken().getName().equals("LEFT_PAREN")) {  // next is (
            System.out.println("You suck Window LEFT_PAREN");
            System.exit(-1);
        }

        if (stream.peek() instanceof Token && ((Token) stream.peek()).getD_value() != null) { // next is some double
            xDimension = ((Token) stream.getToken()).getD_value();
        }
        else {
            System.out.println("You suck Window xDimension");
            System.exit(-1);

        }

        if (!stream.getToken().getName().equals("COMMA")) {  // next is ,
            System.out.println("You suck Window COMMA");
            System.exit(-1);
        }

        if (stream.peek() instanceof Token && ((Token) stream.peek()).getD_value() != null) { // next is some double
            yDimension = ((Token) stream.getToken()).getD_value();
        }
        else {
            System.out.println("You suck Window yDimension");
            System.exit(-1);
        }

        if (!stream.getToken().getName().equals("RIGHT_PAREN")) {  // next is )
            System.out.println("You suck Window RIGHT_PAREN");
            System.exit(-1);
        }

        // if we made it this far, we got (DOUBLE, DOUBLE) and can assign those values as the size of the frame
        //window.setPreferredSize(new Dimension(xDimension.intValue(), yDimension.intValue()));
        window.setPreferredSize(new Dimension(xDimension.intValue(), yDimension.intValue()));

        getLayout(window);
        getWidgets(window);
    }

    public void getLayout(Container container) {
        if (!stream.getToken().getName().equals("Layout")) {
            System.out.println("You suck Layout");
            System.exit(-1);
        }

        // got Layout keyword, moving on
        getLayoutType(container);

        if (!stream.getToken().getName().equals("COLON")) {
            System.out.println("You suck Layout COLON");
            System.exit(-1);
        }
    }

    public void getLayoutType(Container container) {
        if (stream.peek().getName().equals("Flow")) {
            stream.getToken();
            container.setLayout(new FlowLayout());
        }
        else if (stream.peek().getName().equals("Grid")) {
            Double xDim = 0.0, yDim = 0.0, hGapDim = 0.0, vGapDim = 0.0;
            stream.getToken();

            if (!stream.getToken().getName().equals("LEFT_PAREN")) {  // next is (
                System.out.println("You suck LayoutType LEFT_PAREN");
                System.exit(-1);
            }

            if (stream.peek() instanceof Token && ((Token) stream.peek()).getD_value() != null) { // next is some double
                xDim = ((Token) stream.getToken()).getD_value();
            }
            else {
                System.out.println("You suck LayoutType xDim");
                System.exit(-1);
            }

            if (!stream.getToken().getName().equals("COMMA")) {  // next is ,
                System.out.println("You suck LayoutType COMMA");
                System.exit(-1);
            }

            if (stream.peek() instanceof Token && ((Token) stream.peek()).getD_value() != null) { // next is some double
                yDim = ((Token) stream.getToken()).getD_value();
            }
            else {
                System.out.println("You suck LayoutType yDim");
                System.exit(-1);
            }

            if (stream.peek().getName().equals("COMMA")) {  // optional hgap and vgap
                stream.getToken();

                if (stream.peek() instanceof Token && ((Token) stream.peek()).getD_value() != null) { // next is some double
                    hGapDim = ((Token) stream.getToken()).getD_value();
                }
                else {
                    System.out.println("You suck LayoutType hGapDim");
                    System.exit(-1);
                }

                if (!stream.getToken().getName().equals("COMMA")) {  // next is ,
                    System.out.println("You suck LayoutType optional COMMA");
                    System.exit(-1);
                }

                if (stream.peek() instanceof Token && ((Token) stream.peek()).getD_value() != null) { // next is some double
                    vGapDim = ((Token) stream.getToken()).getD_value();
                }
                else {
                    System.out.println("You suck LayoutType vGapDim");
                    System.exit(-1);
                }

                if (!stream.getToken().getName().equals("RIGHT_PAREN")) {  // just two things
                    System.out.println("You suck LayoutType optional RIGHT_PAREN");
                    System.exit(-1);
                }
                // we made it to here, so all 4 args to GridLayout are good to go
                container.setLayout(new GridLayout(xDim.intValue(), yDim.intValue(), hGapDim.intValue(), vGapDim.intValue()));
            }
            else if (stream.peek().getName().equals("RIGHT_PAREN")) {  // just two things
                container.setLayout(new GridLayout(xDim.intValue(), yDim.intValue()));
                stream.getToken();
            }
            else {
                System.out.println("You suck LayoutType COMMA or RIGHT_PAREN");
                System.exit(-1);
            }

        }
        else {
            System.out.println("You suck LayoutType");
            System.exit(-1);
        }
    }

    public void getWidgets(Container container) {
        // first recursive definition
        if (isWidget(stream.peek().getName())) {
            getWidget(container);
            getWidgets(container);
        }
        else if (stream.peek().getName().equals("End.")) {
            stream.getToken();
            System.out.println("Processed");
        }
        else if (stream.peek().getName().equals("End")) {
            // pass, this is handled as validation in the call to Panel
        }
        else {
            System.out.println("Expected a Widget or End. - Got: " + stream.peek().getName());
        }
    }

    public void getWidget(Container container) {
        String buttonName = "", labelName = "";
        Double textFieldNumber = 0.0;

        if (stream.peek().getName().equals("Button")) {
            stream.getToken();

            if (stream.peek() instanceof Token && ((Token) stream.peek()).getS_value() != null) {  // button name
                buttonName = stream.getToken().getName();
            }
            else {
                System.out.println("Button argument not good");
                System.exit(-1);
            }

            if (lineEnds()) {
                container.add(new JButton(buttonName));
            }
        }  // end if Button
        else if (stream.peek().getName().equals("Group")) {
            stream.getToken();
            //TODO: implement Group
        }  // end if Group
        else if (stream.peek().getName().equals("Label")) {
            stream.getToken();

            if (stream.peek() instanceof Token && ((Token) stream.peek()).getS_value() != null) {  // label name
                labelName = stream.getToken().getName();
            }
            else {
                System.out.println("Label argument not good");
                System.exit(-1);
            }

            if (lineEnds()) {
                container.add(new JLabel(labelName));
            }

        }  // end if Label
        else if (stream.peek().getName().equals("Panel")) {
            stream.getToken();
            JPanel panel = new JPanel();
            getLayout(panel);
            getWidgets(panel);
            if (stream.peek().getName().equals("End")) {
                stream.getToken();
                if (lineEnds()) {
                    container.add(panel);
                }
            }
            else {
                System.out.println("Expected 'End' - Got: " + stream.getToken().getName());
                System.exit(-1);
            }

        }  // end if Panel
        else if (stream.peek().getName().equals("Textfield")) {
            stream.getToken();

            if (stream.peek() instanceof Token && ((Token) stream.peek()).getD_value() != null) {
                textFieldNumber = ((Token) stream.getToken()).getD_value();
            }
            else {
                System.out.println("You suck Textfield argument");
                System.exit(-1);
            }
            if (lineEnds()) {
                container.add(new TextField(textFieldNumber.intValue()));
            }
        }  // end if Textfield
        else {
            System.out.println("You suck not a valid widget");
            System.exit(-1);
        }
    }

    // helper functions

    private boolean lineEnds() {
        if (!stream.getToken().getName().equals("SEMI_COLON")) {
            System.out.println("Missing ';'");
            return false;
        }
        return true;
    }

    private boolean isWidget(String type) {
        try {
            widget.valueOf(type);
            return true;
        }
        catch (IllegalArgumentException e) {
            return false;
        }
    }

    // pseudo-main()
    public void build() throws ParseError, IOException {
        if (stream.peek().getName().equals("Window")) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    stream.getToken();
                    window = new JFrame();
                    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    getGui();
                    window.pack();
                    window.setVisible(true);
                }
            });
        }
        else {
            throw new ParseError("Expected: Window; Got: " + stream.getToken());
        }

    }
    public static void main(String[] args) {
        String filename = "input.txt";
        try {
            FileReader reader = new FileReader(filename);
            GuiBuilder gb = new GuiBuilder(new Tokenizer(reader));
            gb.build();

        }
        catch (IOException|ParseError e) {
            e.printStackTrace();
        }
    }

}

