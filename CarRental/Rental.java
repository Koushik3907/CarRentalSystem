import java.sql.*;

class Rental {
    private String vehicleId;
    private Customer customer;
    private int days;

    public Rental(Vehicle vehicle, Customer customer, int days) {
        this.vehicleId = vehicle.getVehicleId();
        this.customer = customer;
        this.days = days;
    }

 
    public static void addRental(Rental rental) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Rental (vehicleId, customerId, days) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, rental.vehicleId);
                stmt.setString(2, rental.customer.getCustomerId());
                stmt.setInt(3, rental.days);
                stmt.executeUpdate();
                System.out.println("Rental added successfully.");
            }
        }
    }

   
    public static void listRentalsByCustomer(String customerId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Rental WHERE customerId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, customerId);
                try (ResultSet rs = stmt.executeQuery()) {
                    System.out.println("Rentals for Customer ID: " + customerId);
                    while (rs.next()) {
                        System.out.println("Rental ID: " + rs.getInt("rentalId"));
                        System.out.println("Vehicle ID: " + rs.getString("vehicleId"));
                        System.out.println("Rental Days: " + rs.getInt("days"));
                        System.out.println("-------------------------------");
                    }
                }
            }
        }
    }

  
    public static void returnVehicle(String vehicleId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM Rental WHERE vehicleId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, vehicleId);
                int rowsDeleted = stmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Rental record removed. Vehicle returned successfully.");
                } else {
                    System.out.println("Rental record not found for Vehicle ID: " + vehicleId);
                }
            }
        }
    }
}
