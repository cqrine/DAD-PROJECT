package Interface;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import org.json.JSONArray;
import org.json.JSONObject;
import shared.ApiClient;

public class product {
    private final ApiClient api;
    private JFrame frame;
    private JTable productTable;
    private final List<Object[]> cart = new ArrayList<>();

    public product(ApiClient api) {
        this(api, false); // default: do not reset cart
    }

    // constructor with reset option
    public product(ApiClient api, boolean resetCart) {
        this.api = api;
        if (resetCart) cart.clear(); // clear cart if needed
        initialize();
    }


    public void showWindow() {
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame("Available Products ‑ Pantas Express");
        frame.setBounds(100, 100, 800, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(new Color(243, 112, 33));

        JLabel lblTitle = new JLabel("Product List");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 50));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(10, 20, 760, 50);
        frame.getContentPane().add(lblTitle);

        JLabel lblImage = new JLabel();
        lblImage.setBounds(50, 100, 120, 120);
        lblImage.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        frame.getContentPane().add(lblImage);

        JLabel lblProductName = new JLabel("Select a product");
        lblProductName.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        lblProductName.setForeground(Color.WHITE);
        lblProductName.setBounds(190, 100, 500, 25);
        frame.getContentPane().add(lblProductName);

        JLabel lblDescription = new JLabel("Description will appear here");
        lblDescription.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        lblDescription.setForeground(Color.WHITE);
        lblDescription.setBounds(190, 130, 550, 25);
        frame.getContentPane().add(lblDescription);

        String[] columnNames = {"Product ID", "Product Name", "Price (RM)"};
        Object[][] data = new Object[0][3];

        try {
            JSONArray products = api.getProducts();
            data = new Object[products.length()][3];

            for (int i = 0; i < products.length(); i++) {
                JSONObject product = products.getJSONObject(i);
                data[i][0] = product.getString("product_id");
                data[i][1] = product.getString("name");
                data[i][2] = product.getDouble("price");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame,
                "Failed to load products: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }

        productTable = new JTable(data, columnNames);
        productTable.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        productTable.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBounds(50, 240, 680, 180);
        frame.getContentPane().add(scrollPane);

        productTable.getSelectionModel().addListSelectionListener(e -> {
            int row = productTable.getSelectedRow();
            if (row >= 0) {
                lblProductName.setText("Product: " + productTable.getValueAt(row, 1));
                lblDescription.setText("Description: " + productTable.getValueAt(row, 1) + " details");

                String[] exts = {".jpg", ".jpeg"};
                ImageIcon icon = null;
                for (String ext : exts) {
                    String path = "src/Reference/" + (row + 1) + ext;
                    if (new java.io.File(path).exists()) {
                        icon = new ImageIcon(path);
                        break;
                    }
                }

                if (icon != null) {
                    Image scaledImage = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    lblImage.setIcon(new ImageIcon(scaledImage));
                    lblImage.setText("");
                } else {
                    lblImage.setIcon(null);
                    lblImage.setText("No Image");
                }
            }
        });

        JButton btnAddToCart = new JButton("Add to Cart");
        btnAddToCart.setFont(new Font("Consolas", Font.PLAIN, 18));
        btnAddToCart.setBounds(120, 450, 160, 40);
        frame.getContentPane().add(btnAddToCart);

        JButton btnViewCart = new JButton("View Cart");
        btnViewCart.setFont(new Font("Consolas", Font.PLAIN, 18));
        btnViewCart.setBounds(310, 450, 160, 40);
        frame.getContentPane().add(btnViewCart);

        JButton btnClose = new JButton("Close");
        btnClose.setFont(new Font("Consolas", Font.PLAIN, 18));
        btnClose.setBounds(500, 450, 160, 40);
        frame.getContentPane().add(btnClose);

        btnAddToCart.addActionListener(e -> {
            int row = productTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a product first.");
                return;
            }
            Object[] selected = {
                productTable.getValueAt(row, 0),
                productTable.getValueAt(row, 1),
                productTable.getValueAt(row, 2)
            };
            cart.add(selected);
            JOptionPane.showMessageDialog(frame, selected[1] + " added to cart.");
        });

        btnViewCart.addActionListener(e -> openCartWindow());
        btnClose.addActionListener(e -> frame.dispose());
    }

    private void openCartWindow() {
        JFrame cartFrame = new JFrame("My Cart");
        cartFrame.setBounds(150, 150, 500, 400);
        cartFrame.getContentPane().setLayout(null);
        cartFrame.getContentPane().setBackground(new Color(243, 112, 33));

        JLabel lblCart = new JLabel("Items in Cart");
        lblCart.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblCart.setForeground(Color.WHITE);
        lblCart.setBounds(150, 10, 250, 30);
        cartFrame.getContentPane().add(lblCart);

        String[] cartCols = {"Product ID", "Product Name", "Price (RM)"};
        Object[][] cartData = cart.toArray(new Object[0][0]);
        JTable cartTable = new JTable(cartData, cartCols);
        cartTable.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
        cartTable.setRowHeight(24);

        JScrollPane cartScroll = new JScrollPane(cartTable);
        cartScroll.setBounds(30, 60, 430, 180);
        cartFrame.getContentPane().add(cartScroll);

        double total = cart.stream().mapToDouble(item -> Double.parseDouble(item[2].toString())).sum();

        JLabel lblTotal = new JLabel(String.format("Total: RM %.2f", total));
        lblTotal.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        lblTotal.setForeground(Color.WHITE);
        lblTotal.setBounds(30, 250, 200, 25);
        cartFrame.getContentPane().add(lblTotal);

        JButton btnCheckout = new JButton("Checkout");
        btnCheckout.setFont(new Font("Consolas", Font.PLAIN, 14));
        btnCheckout.setBounds(330, 250, 120, 30);
        cartFrame.getContentPane().add(btnCheckout);

        JButton btnRemove = new JButton("Remove");
        btnRemove.setFont(new Font("Consolas", Font.PLAIN, 14));
        btnRemove.setBounds(200, 250, 120, 30);
        cartFrame.getContentPane().add(btnRemove);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Consolas", Font.PLAIN, 14));
        btnBack.setBounds(200, 290, 120, 30);
        cartFrame.getContentPane().add(btnBack);

        btnBack.addActionListener(e -> cartFrame.dispose());

        btnRemove.addActionListener(e -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(cartFrame, "Please select an item to remove.");
                return;
            }
            cart.remove(selectedRow);
            cartFrame.dispose();
            openCartWindow(); // Refresh view
        });

        btnCheckout.addActionListener(e -> {
            try {
                JSONArray itemsArray = new JSONArray();
                for (Object[] item : cart) {
                    JSONObject obj = new JSONObject();
                    obj.put("product_id", item[0]);
                    obj.put("quantity", 1); // default 1
                    itemsArray.put(obj);
                }

                JSONObject orderData = new JSONObject();
                orderData.put("items", itemsArray);

                JSONObject response = api.createOrder(orderData);
                System.out.println("Create order response: " + response);

                if (response.getBoolean("success")) {
                    String orderId = response.getString("order_id");
                    double cartTotal = response.getDouble("total_amount");

                    List<String> itemNames = new ArrayList<>();
                    for (Object[] item : cart) {
                        itemNames.add(item[1].toString());
                    }

                    cartFrame.dispose();
                    frame.dispose(); // close main window
                    new payment(api, total, itemNames, orderId).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(cartFrame, "Checkout failed: " + response.getString("message"), "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(cartFrame, "Error during checkout: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cartFrame.setVisible(true);
    }
}
