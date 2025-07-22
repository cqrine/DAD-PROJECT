package DeliveryAppGUI;

import javax.swing.*;
import java.awt.*;
import org.json.JSONObject;
import SharedApi.ApiClient;

public class DeliveryLogin {

    private JFrame frame;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                DeliveryLogin window = new DeliveryLogin();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public DeliveryLogin() {
        initialize();
    }

    public void showWindow() {
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame("Staff Login - Pantas Express Delivery");
        frame.setBounds(100, 100, 800, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
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

        txtUsername = new JTextField();
        txtUsername.setBounds(332, 222, 211, 33);
        contentPane.add(txtUsername);
        txtUsername.setColumns(10);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Comic Sans", Font.PLAIN, 24));
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setBounds(206, 275, 177, 55);
        contentPane.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(332, 281, 211, 33);
        contentPane.add(txtPassword);

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Consolas", Font.PLAIN, 18));
        btnLogin.setBounds(259, 348, 159, 45);
        contentPane.add(btnLogin);

        JButton btnSignUp = new JButton("SIGN UP");
        btnSignUp.setFont(new Font("Consolas", Font.PLAIN, 18));
        btnSignUp.setBounds(440, 348, 159, 45);
        contentPane.add(btnSignUp);

        JButton btnExit = new JButton("EXIT");
        btnExit.setFont(new Font("Consolas", Font.PLAIN, 18));
        btnExit.setBounds(365, 400, 159, 39);
        contentPane.add(btnExit);

        // Actions

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both username and password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                JSONObject response = ApiClient.login(username, password);
                System.out.println("Raw login response: " + response);

                if (response != null && response.getBoolean("success")) {
                    String role = response.getString("role");

                    if ("delivery".equals(role)) {
                        String token = response.getString("token");
                        int userId = response.getInt("user_id");

                        DeliveryDashboard dashboard = new DeliveryDashboard(token, userId);
                        dashboard.setVisible(true);
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Access denied: Not a delivery staff.", "Role Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Login failed: " + response.getString("message"), "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Login failed due to an error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
 

        btnSignUp.addActionListener(e -> {
            new DeliveryRegister().showWindow();
            frame.dispose();
        });

        btnExit.addActionListener(e -> frame.dispose());
    }
}
