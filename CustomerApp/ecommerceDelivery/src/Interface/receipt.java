package Interface;

import javax.swing.*;
import shared.ApiClient;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class receipt extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private ApiClient apiClient;

    public receipt(ApiClient client, double totalAmount, List<String> items, String orderId, String paymentMethod) {
        this.apiClient = client;
        setTitle("Receipt");
        setBounds(100, 100, 500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 253, 208));
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        JLabel title = new JLabel("Receipt", SwingConstants.CENTER);
        title.setFont(new Font("Courier", Font.BOLD, 38));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        contentPane.add(title, BorderLayout.NORTH);

        JTextArea receiptArea = new JTextArea();
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        receiptArea.setEditable(false);

        StringBuilder receiptText = new StringBuilder();

        Random rand = new Random();
        int receiptNumber = 100000 + rand.nextInt(900000);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        receiptText.append("Receipt No   : ").append(receiptNumber).append("\n");
        receiptText.append("Date & Time  : ").append(timestamp).append("\n");
        receiptText.append("Order ID     : ").append(orderId).append("\n");
        receiptText.append("Paid via     : ").append(paymentMethod).append("\n");
        receiptText.append("====================================\n");
        receiptText.append("Items Ordered:\n");

        int count = 1;
        for (String item : items) {
            receiptText.append(String.format("%2d. %s\n", count++, item));
        }

        receiptText.append("------------------------------------\n");
        receiptText.append(String.format("Total Amount : RM %.2f\n", totalAmount));
        receiptText.append("====================================\n");
        receiptText.append("Thank you for your order!\n");

        receiptArea.setText(receiptText.toString());

        JScrollPane scrollPane = new JScrollPane(receiptArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JButton btnClose = new JButton("Close");
        btnClose.setFont(new Font("Rockwell", Font.BOLD, 16));
        btnClose.addActionListener(e -> dispose());

        JButton btnBackToProducts = new JButton("Back to Products");
        btnBackToProducts.setFont(new Font("Rockwell", Font.BOLD, 16));
        btnBackToProducts.addActionListener(e -> {
            new product(apiClient, true).showWindow(); // Start new order with empty cart
            dispose(); // Close receipt window
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(255, 253, 208));
        bottomPanel.add(btnBackToProducts);
        bottomPanel.add(btnClose);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
