// --- delivery.java ---
package Interface;

import shared.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class delivery extends JFrame {
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private ApiClient apiClient;

    public delivery(ApiClient client) {
        this.apiClient = client;
        setTitle("Delivery Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Delivery Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(255, 140, 0));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        String[] columns = {"Order ID", "Customer", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        orderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(255, 228, 181));

        JButton markDeliveredBtn = new JButton("Mark as Delivered");
        JButton refreshBtn = new JButton("Refresh Orders");

        markDeliveredBtn.addActionListener(e -> markAsDelivered());
        refreshBtn.addActionListener(e -> loadOrdersFromAPI());

        buttonPanel.add(markDeliveredBtn);
        buttonPanel.add(refreshBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        loadOrdersFromAPI();
    }

    private void loadOrdersFromAPI() {
        try {
            // Note: This would need to be implemented in ApiClient for delivery personnel
            // For now, showing placeholder message
            tableModel.setRowCount(0);
            tableModel.addRow(new Object[]{"001", "Customer A", "Pending"});
            tableModel.addRow(new Object[]{"002", "Customer B", "Shipped"});
            
            JOptionPane.showMessageDialog(this, 
                "Delivery module requires additional API endpoints.\nShowing sample data.", 
                "Info", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading orders from server.");
        }
    }

    private void markAsDelivered() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            String orderId = tableModel.getValueAt(selectedRow, 0).toString();
            try {
                // Update the table locally (would need proper API integration)
                tableModel.setValueAt("Delivered", selectedRow, 2);
                JOptionPane.showMessageDialog(this, "Order marked as Delivered (local update).");
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating delivery status.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Would need proper authentication for delivery personnel
            ApiClient client = new ApiClient("dummy-token");
            new delivery(client).setVisible(true);
        });
    }
}