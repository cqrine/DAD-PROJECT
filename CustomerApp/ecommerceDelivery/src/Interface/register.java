package Interface;

import javax.swing.*;
import java.awt.*;
import org.json.JSONObject;
import shared.ApiClient;

public class register {

    private JFrame frame;
    private JTextField txtEmail, txtUsername, txtPassword, txtConfirmPassword, txtPhone;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                register window = new register();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void showWindow() {
        frame.setVisible(true);
    }

    public register() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Register - Pantas Express");
        frame.setBounds(100, 100, 800, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(243, 112, 33));
        contentPane.setLayout(null);
        frame.setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Sign Up");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 70));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(10, 40, 760, 70);
        contentPane.add(lblTitle);

        Font labelFont = new Font("Comic Sans MS", Font.PLAIN, 20);
        Color white = Color.WHITE;

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(labelFont);
        lblEmail.setForeground(white);
        lblEmail.setBounds(180, 170, 150, 30);
        contentPane.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(340, 170, 250, 30);
        contentPane.add(txtEmail);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(labelFont);
        lblUsername.setForeground(white);
        lblUsername.setBounds(180, 220, 150, 30);
        contentPane.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(340, 220, 250, 30);
        contentPane.add(txtUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(labelFont);
        lblPassword.setForeground(white);
        lblPassword.setBounds(180, 270, 150, 30);
        contentPane.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(340, 270, 250, 30);
        contentPane.add(txtPassword);

        JLabel lblConfirm = new JLabel("Confirm Password:");
        lblConfirm.setFont(labelFont);
        lblConfirm.setForeground(white);
        lblConfirm.setBounds(140, 320, 190, 30);
        contentPane.add(lblConfirm);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(340, 320, 250, 30);
        contentPane.add(txtConfirmPassword);

        JLabel lblPhone = new JLabel("Phone No:");
        lblPhone.setFont(labelFont);
        lblPhone.setForeground(white);
        lblPhone.setBounds(180, 370, 150, 30);
        contentPane.add(lblPhone);

        txtPhone = new JTextField();
        txtPhone.setBounds(340, 370, 250, 30);
        contentPane.add(txtPhone);

        JButton btnConfirm = new JButton("CONFIRM");
        btnConfirm.setFont(new Font("Consolas", Font.PLAIN, 18));
        btnConfirm.setBounds(260, 460, 140, 40);
        contentPane.add(btnConfirm);

        btnConfirm.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText().trim();
            String confirmPassword = txtConfirmPassword.getText().trim();
            String phone = txtPhone.getText().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JSONObject response = ApiClient.register(username, email, password, phone);
            if (response.getBoolean("success")) {
                JOptionPane.showMessageDialog(frame, "Your account has been successfully created!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                login loginWindow = new login();
                loginWindow.showWindow();
            } else {
                JOptionPane.showMessageDialog(frame, response.getString("message"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnReturn = new JButton("RETURN");
        btnReturn.setFont(new Font("Consolas", Font.PLAIN, 18));
        btnReturn.setBounds(420, 460, 140, 40);
        contentPane.add(btnReturn);

        btnReturn.addActionListener(e -> {
            frame.dispose();
            login loginWindow = new login();
            loginWindow.showWindow();
        });
    }
}
