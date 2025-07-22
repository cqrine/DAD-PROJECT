
package Interface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class delivery extends JFrame {

    private JTable orderTable;
    private DefaultTableModel tableModel;

    public delivery() {
        setTitle("Delivery Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Delivery Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(255, 140, 0)); // Orange
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Table model and data
        String[] columns = {"Order ID", "Customer", "Address", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        orderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);

        // Add dummy rows
        addDummyOrders();

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(255, 228, 181)); // Lighter orange

        JButton markDeliveredBtn = new JButton("Mark as Delivered");
        JButton refreshBtn = new JButton("Refresh Orders");

        markDeliveredBtn.addActionListener(e -> markAsDelivered());
        refreshBtn.addActionListener(e -> refreshOrders());

        buttonPanel.add(markDeliveredBtn);
        buttonPanel.add(refreshBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addDummyOrders() {
        tableModel.setRowCount(0); // Clear existing rows
        tableModel.addRow(new Object[]{"1001", "Ali", "KL Sentral", "Shipped"});
        tableModel.addRow(new Object[]{"1002", "Siti", "Bangi", "Pending"});
        tableModel.addRow(new Object[]{"1003", "John", "Cyberjaya", "Delivered"});
        tableModel.addRow(new Object[]{"1004", "Mei", "Kajang", "Shipped"});
    }

    private void markAsDelivered() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.setValueAt("Delivered", selectedRow, 3);
            JOptionPane.showMessageDialog(this, "Order marked as Delivered.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order.");
        }
    }

    private void refreshOrders() {
        addDummyOrders();
        JOptionPane.showMessageDialog(this, "Orders refreshed.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new delivery().setVisible(true));
    }
}
