package likeabaos.tools.util.scripts.gui;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SimpleCredentialsPrompt {
    public static String[] show(String title) {
	JLabel unameLabel = new JLabel("Username:");
	JTextField unameField = new JTextField();

	JLabel passwordLabel = new JLabel("Password:");
	JPasswordField passwordField = new JPasswordField();

	int result = JOptionPane.showConfirmDialog(null,
		new Object[] { unameLabel, unameField, passwordLabel, passwordField }, title,
		JOptionPane.OK_CANCEL_OPTION);

	String[] creds = null;
	if (result == JOptionPane.OK_OPTION) {
	    creds = new String[2];
	    creds[0] = unameField.getText();
	    creds[1] = new String(passwordField.getPassword());
	}
	return creds;
    }

    public static String[] show() {
	return show("Username & Password?");
    }
}
