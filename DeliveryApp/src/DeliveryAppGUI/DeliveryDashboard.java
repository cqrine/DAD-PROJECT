package DeliveryAppGUI;

import SharedApi.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DeliveryDashboard extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private String token;
    private int userId;
    private JComboBox<String> filterDropdown;

    public DeliveryDashboard(String token, int userId) {
        this.token = token;
        this.userId = userId;

        setTitle("Assigned Deliveries ‑ Pantas Express");
        setSize(800, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(243, 112, 33));
        getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("Assigned Deliveries");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 50));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(10, 20, 760, 50);
        getContentPane().add(lblTitle);

        // Filter dropdown
        filterDropdown = new JComboBox<>(new String[]{"Pending", "Delivered", "All"});
        filterDropdown.setFont(new Font("Consolas", Font.PLAIN, 16));
        filterDropdown.setBounds(50, 80, 200, 30);
        getContentPane().add(filterDropdown);

        model = new DefaultTableModel(new String[]{"Delivery ID", "Order ID", "Total", "Address", "Status"}, 0);
        table = new JTable(model);
        table.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        table.setRowHeight(24);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 120, 680, 300);
        getContentPane().add(scrollPane);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font("Consolas", Font.PLAIN, 18));
        refreshBtn.setBounds(100, 440, 160, 40);
        getContentPane().add(refreshBtn);

        JButton markBtn = new JButton("Mark as Delivered");
        markBtn.setFont(new Font("Consolas", Font.PLAIN, 18));
        markBtn.setBounds(280, 440, 220, 40);
        getContentPane().add(markBtn);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Consolas", Font.PLAIN, 18));
        logoutBtn.setBounds(520, 440, 120, 40);
        getContentPane().add(logoutBtn);

        // Event handlers
        refreshBtn.addActionListener(e -> loadDeliveries());
        markBtn.addActionListener(e -> markSelectedAsDelivered());
        logoutBtn.addActionListener(e -> {
            dispose();
            new DeliveryLogin().showWindow();
        });
        filterDropdown.addActionListener(e -> loadDeliveries());

        loadDeliveries();
        setVisible(true);
    }

    private void loadDeliveries() {
        try {
            String selectedStatus = filterDropdown.getSelectedItem().toString().toLowerCase();
            JSONObject response = ApiClient.getJsonWithAuth("/deliveries/assigned.php?status=" + selectedStatus, token);

            if (response.getBoolean("success")) {
                model.setRowCount(0);
                JSONArray deliveries = response.getJSONArray("deliveries");
                for (int i = 0; i < deliveries.length(); i++) {
                    JSONObject d = deliveries.getJSONObject(i);
                    model.addRow(new Object[]{
                            d.getInt("delivery_id"),
                            d.getInt("order_id"),
                            d.getDouble("total_amount"),
                            d.optString("delivery_address", "N/A"),
                            d.getString("delivery_status")
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to load deliveries.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void markSelectedAsDelivered() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a row first.");
            return;
        }

        int deliveryId = (int) model.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Mark as delivered?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                JSONObject body = new JSONObject();
                body.put("delivery_id", deliveryId);
                body.put("status", "delivered");

                JSONObject response = ApiClient.sendJsonWithAuth("/deliveries/update_status.php", "PATCH", body, token);

                if (response.getBoolean("success")) {
                    JOptionPane.showMessageDialog(this, "Delivery marked as delivered.");
                    loadDeliveries();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed: " + response.getString("message"));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
}
