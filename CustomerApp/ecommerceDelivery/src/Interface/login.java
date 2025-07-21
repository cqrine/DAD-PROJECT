package Interface;

import java.awt.*;
import javax.swing.*;
import org.json.JSONObject;
import shared.ApiClient;

public class login {

    private JFrame frame;
    private JTextField textField;        // For username
    private JPasswordField textField_1;  // For password (corrected type)

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                login window = new login();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public login() {
        initialize();
    }

    public void showWindow() {
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("E-Commerce Delivery System");
        frame.setBounds(100, 100, 800, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(243, 112, 33));
        contentPane.setLayout(null);
        frame.setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Pantas Express");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 70));
        lblTitle.setBounds(100, 50, 600, 100);
        contentPane.add(lblTitle);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Comic Sans", Font.PLAIN, 24));
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setBounds(206, 210, 177, 55);
        contentPane.add(lblUsername);

        textField = new JTextField();
        textField.setBounds(332, 222, 211, 33);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Comic Sans", Font.PLAIN, 24));
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setBounds(206, 275, 177, 55);
        contentPane.add(lblPassword);

        textField_1 = new JPasswordField(); // Corrected declaration
        textField_1.setColumns(10);
        textField_1.setBounds(332, 281, 211, 33);
        contentPane.add(textField_1);

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Consolas", Font.PLAIN, 18));
        btnLogin.setBounds(259, 348, 159, 45);
        contentPane.add(btnLogin);

        btnLogin.addActionListener(e -> {
            String username = textField.getText().trim();
            String password = new String(textField_1.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both username and password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                System.out.println("Sending login request -> Username: " + username + ", Password: " + password);

                JSONObject response = ApiClient.login(username, password);
                System.out.println("Login response: " + response);

                if (response != null && response.getBoolean("success")) {
                    String token = response.getString("token");
                    ApiClient apiClient = new ApiClient(token);

                    // Open product window after login
                    product productWindow = new product(apiClient);
                    productWindow.showWindow();
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Login failed: " + response.getString("message"), "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Login failed due to an error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnSignUp = new JButton("SIGN UP");
        btnSignUp.setFont(new Font("Consolas", Font.PLAIN, 18));
        btnSignUp.setBounds(440, 348, 159, 45);
        contentPane.add(btnSignUp);

        btnSignUp.addActionListener(e -> {
            register registerWindow = new register();
            registerWindow.showWindow();
            frame.dispose();
        });

        JButton btnExit = new JButton("EXIT");
        btnExit.setFont(new Font("Consolas", Font.PLAIN, 18));
        btnExit.setBounds(365, 400, 159, 39);
        contentPane.add(btnExit);
        btnExit.addActionListener(e -> frame.dispose());
    }
}
