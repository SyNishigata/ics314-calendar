import javax.swing.*;

/**
 * Created by Sy on 3/10/2016.
 */
public class NumericVerifier extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText();
        if (text.equals("")){
            return true;
        }
        else {
            try {
                Float.parseFloat(text);
                return true;
            } catch (NumberFormatException e) {
                System.err.println("Please enter only numbers in this field.");
                return false;
            }
        }
    }
}
