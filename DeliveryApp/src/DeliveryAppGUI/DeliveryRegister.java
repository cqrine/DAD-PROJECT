package DeliveryAppGUI;

import javax.swing.*;
import java.awt.*;
import org.json.JSONObject;
import SharedApi.ApiClient;

public class DeliveryRegister {

    private JFrame frame;
    private JTextField txtEmail, txtUsername, txtPhone;
    private JPasswordField txtPassword, txtConfirmPassword;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                DeliveryRegister window = new DeliveryRegister();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void showWindow() {
        frame.setVisible(true);
    }

    public DeliveryRegister() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Staff Registration - Pantas Express Delivery");
        frame.setSize(800, 700);             
        frame.setLocationRelativeTo(null);         
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(243, 112, 33));
        panel.setLayout(null);
        frame.setContentPane(panel);

        JLabel lblTitle = new JLabel("Sign Up");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 70));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(150, 40, 500, 70);
        panel.add(lblTitle);

        Font labelFont = new Font("Comic Sans MS", Font.PLAIN, 20);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(180, 170, 150, 30);
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setFont(labelFont);
        panel.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(340, 170, 250, 30);
        panel.add(txtEmail);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(180, 220, 150, 30);
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setFont(labelFont);
        panel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(340, 220, 250, 30);
        panel.add(txtUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(180, 270, 150, 30);
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(labelFont);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(340, 270, 250, 30);
        panel.add(txtPassword);

        JLabel lblConfirm = new JLabel("Confirm Password:");
        lblConfirm.setBounds(140, 320, 190, 30);
        lblConfirm.setForeground(Color.WHITE);
        lblConfirm.setFont(labelFont);
        panel.add(lblConfirm);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(340, 320, 250, 30);
        panel.add(txtConfirmPassword);

        JLabel lblPhone = new JLabel("Phone No:");
        lblPhone.setBounds(180, 370, 150, 30);
        lblPhone.setForeground(Color.WHITE);
        lblPhone.setFont(labelFont);
        panel.add(lblPhone);

        txtPhone = new JTextField();
        txtPhone.setBounds(340, 370, 250, 30);
        panel.add(txtPhone);

        JButton btnConfirm = new JButton("CONFIRM");
        btnConfirm.setFont(new Font("Consolas", Font.PLAIN, 18));
        btnConfirm.setBounds(260, 460, 140, 40);
        panel.add(btnConfirm);

        JButton btnReturn = new JButton("RETURN");
        btnReturn.setFont(new Font("Consolas", Font.PLAIN, 18));
        btnReturn.setBounds(420, 460, 140, 40);
        panel.add(btnReturn);

        btnConfirm.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            String confirm = new String(txtConfirmPassword.getPassword()).trim();
            String phone = txtPhone.getText().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                JSONObject response = ApiClient.register(username, email, password, phone, "delivery");
                if (response.getBoolean("success")) {
                    JOptionPane.showMessageDialog(frame, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new DeliveryLogin().showWindow();
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Account was not created as a delivery role.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnReturn.addActionListener(e -> {
            new DeliveryLogin().showWindow();
            frame.dispose();
        });
    }
}
