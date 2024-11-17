package ui.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private boolean succeeded;

    public LoginDialog(Frame parent) {
        super(parent, "Login", true);
        
        // Create panel for components
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;

        // Username label and field
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        cs.insets = new Insets(10, 10, 5, 5);
        panel.add(new JLabel("Username: "), cs);

        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        cs.insets = new Insets(10, 0, 5, 10);
        usernameField = new JTextField(20);
        panel.add(usernameField, cs);

        // Password label and field
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        cs.insets = new Insets(5, 10, 10, 5);
        panel.add(new JLabel("Password: "), cs);

        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        cs.insets = new Insets(5, 0, 10, 10);
        passwordField = new JPasswordField(20);
        panel.add(passwordField, cs);

        // Button panel
        JPanel buttonPanel = new JPanel();
        loginButton = new JButton("Login");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        // Add button panel to main panel
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 3;
        cs.insets = new Insets(5, 10, 10, 10);
        panel.add(buttonPanel, cs);

        // Add action listeners
        loginButton.addActionListener(e -> {
            if (authenticate(usernameField.getText(), new String(passwordField.getPassword()))) {
                succeeded = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginDialog.this,
                    "Invalid username or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
                usernameField.setText("");
                passwordField.setText("");
                succeeded = false;
            }
        });

        cancelButton.addActionListener(e -> {
            succeeded = false;
            dispose();
        });

        // Window listener to handle closing
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                succeeded = false;
                dispose();
            }
        });

        // Key listener for Enter key
        KeyListener keyListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        };
        usernameField.addKeyListener(keyListener);
        passwordField.addKeyListener(keyListener);

        getContentPane().add(panel);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private boolean authenticate(String username, String password) {
        // For testing purposes - replace with actual authentication
        return "admin".equals(username) && "admin".equals(password);
    }

    public boolean isSucceeded() {
        return succeeded;
    }
} 