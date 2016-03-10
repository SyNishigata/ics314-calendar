import javax.swing.*;

/**
 * Created by Sy on 3/10/2016.
 */
public class StringVerifier extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText();
        if (!text.equals("")){
            return true;
        }
        else{
            System.err.println("Please fill this field out.");
            return false;
        }
    }
}