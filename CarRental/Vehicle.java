import java.sql.*;

class Vehicle {
    private String vehicleId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Vehicle(String vehicleId, String brand, String model, double basePricePerDay, boolean isAvailable) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = isAvailable;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

   
    public static void addVehicle(String vehicleId, String brand, String model, double basePricePerDay) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Vehicle (vehicleId, brand, model, basePricePerDay, isAvailable) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, vehicleId);
                stmt.setString(2, brand);
                stmt.setString(3, model);
                stmt.setDouble(4, basePricePerDay);
                stmt.setBoolean(5, true); 
                stmt.executeUpdate();
                System.out.println("Vehicle added successfully.");
            }
        }
    }

   
    public static void updateVehicleAvailability(String vehicleId, boolean available) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE Vehicle SET isAvailable = ? WHERE vehicleId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setBoolean(1, available);
                stmt.setString(2, vehicleId);
                stmt.executeUpdate();
                System.out.println("Vehicle availability updated successfully.");
            }
        }
    }

   
    public static void listAvailableVehicles() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Vehicle WHERE isAvailable = true";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                System.out.println("Available Vehicles:");
                while (rs.next()) {
                    System.out.println("Vehicle ID: " + rs.getString("vehicleId"));
                    System.out.println("Brand: " + rs.getString("brand"));
                    System.out.println("Model: " + rs.getString("model"));
                    System.out.println("Base Price per Day: $" + rs.getDouble("basePricePerDay"));
                    System.out.println("-------------------------------");
                }
            }
        }
    }

   
    public static Vehicle getVehicleById(String vehicleId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Vehicle WHERE vehicleId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, vehicleId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new Vehicle(
                            rs.getString("vehicleId"),
                            rs.getString("brand"),
                            rs.getString("model"),
                            rs.getDouble("basePricePerDay"),
                            rs.getBoolean("isAvailable")
                        );
                    } else {
                        System.out.println("Vehicle with ID " + vehicleId + " not found.");
                    }
                }
            }
        }
        return null;
    }
}
