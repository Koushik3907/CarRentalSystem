import java.sql.*;

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
    public static void addCustomer(Customer customer) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Customer (customerId, name) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, customer.getCustomerId());
                stmt.setString(2, customer.getName());
                stmt.executeUpdate();
                System.out.println("Customer added successfully.");
            }
        }
    }
}

