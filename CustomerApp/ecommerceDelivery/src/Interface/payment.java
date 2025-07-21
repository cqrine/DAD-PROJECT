package Interface;

import shared.ApiClient;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class payment extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtStatus;
    private JTextArea textAmount;
    private JTextField txtOrderId;
    private ApiClient apiClient;

    public payment(ApiClient client, double totalAmount, List<String> cartItems, String orderId) {
        this.apiClient = client;

        setTitle("Payment Page");
        setBounds(100, 100, 800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(243, 112, 33));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Payment");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 50));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(10, 10, 760, 78);
        contentPane.add(lblTitle);

        JLabel lblOrderId = new JLabel("Order ID          :");
        lblOrderId.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        lblOrderId.setForeground(Color.WHITE);
        lblOrderId.setBounds(200, 120, 200, 30);
        contentPane.add(lblOrderId);

        JLabel lblAmount = new JLabel("Total Amount      : RM");
        lblAmount.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        lblAmount.setForeground(Color.WHITE);
        lblAmount.setBounds(200, 170, 200, 30);
        contentPane.add(lblAmount);

        JLabel lblStatus = new JLabel("Status                 :");
        lblStatus.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setBounds(200, 220, 200, 30);
        contentPane.add(lblStatus);

        JLabel lblMethod = new JLabel("Payment Method:");
        lblMethod.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        lblMethod.setForeground(Color.WHITE);
        lblMethod.setBounds(200, 270, 200, 30);
        contentPane.add(lblMethod);

        txtOrderId = new JTextField(orderId);
        txtOrderId.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        txtOrderId.setBounds(400, 120, 150, 30);
        txtOrderId.setEditable(false);
        contentPane.add(txtOrderId);

        textAmount = new JTextArea(String.format("%.2f", totalAmount));
        textAmount.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        textAmount.setBounds(400, 170, 150, 30);
        textAmount.setEditable(false);
        contentPane.add(textAmount);

        txtStatus = new JTextField("Pending");
        txtStatus.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        txtStatus.setBounds(400, 220, 150, 30);
        contentPane.add(txtStatus);

        JList<String> methodList = new JList<>(new String[]{"TnG e-Wallet", "FPX Online Banking", "Visa Card", "Cash"});
        methodList.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        methodList.setBounds(400, 270, 150, 90);
        contentPane.add(methodList);

        JButton btnConfirm = new JButton("Confirm");
        btnConfirm.setFont(new Font("Consolas", Font.PLAIN, 18));
        btnConfirm.setBounds(460, 390, 120, 35);
        contentPane.add(btnConfirm);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Consolas", Font.PLAIN, 18));
        btnBack.setBounds(220, 390, 120, 35);
        contentPane.add(btnBack);

        btnConfirm.addActionListener(e -> {
            String method = methodList.getSelectedValue();
            if (method == null) {
                JOptionPane.showMessageDialog(this, "Please select a payment method.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                JSONObject response = apiClient.updatePaymentStatus(orderId, method);
                if (response.getBoolean("success")) {
                    JOptionPane.showMessageDialog(this, "Payment successful via " + method + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    receipt receiptWindow = new receipt(apiClient, totalAmount, cartItems, orderId, method);
                    receiptWindow.setVisible(true); // show the receipt window
                } else {
                    JOptionPane.showMessageDialog(this, "Payment failed: " + response.getString("message"), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error connecting to server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBack.addActionListener(e -> dispose());
    }
}
