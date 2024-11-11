import java.sql.SQLException;
import java.util.Scanner;

public class CarRentalSystem {

    public void menu(boolean isAdmin) {
        Scanner scanner = new Scanner(System.in);

        if (isAdmin) {
            // Admin Menu
            while (true) {
                System.out.println("===== Admin Menu =====");
                System.out.println("1. Add a Vehicle");
                System.out.println("2. Update Vehicle Availability");
                System.out.println("3. List Rentals by Customer");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                try {
                    if (choice == 1) {
                        System.out.println("\n== Add a New Vehicle ==\n");
                        System.out.print("Enter vehicle ID: ");
                        String vehicleId = scanner.nextLine();
                        System.out.print("Enter vehicle brand: ");
                        String brand = scanner.nextLine();
                        System.out.print("Enter vehicle model: ");
                        String model = scanner.nextLine();
                        System.out.print("Enter base price per day: ");
                        double basePricePerDay = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline

                        Vehicle.addVehicle(vehicleId, brand, model, basePricePerDay);
                    } else if (choice == 2) {
                        System.out.println("\n== Update Vehicle Availability ==\n");
                        System.out.print("Enter vehicle ID: ");
                        String vehicleId = scanner.nextLine();

                        System.out.print("Set availability (true for available, false for unavailable): ");
                        boolean availability = scanner.nextBoolean();
                        scanner.nextLine(); // Consume newline

                        Vehicle.updateVehicleAvailability(vehicleId, availability);
                    } else if (choice == 3) {
                        System.out.println("\n== List Rentals by Customer ==\n");
                        System.out.print("Enter the customer ID: ");
                        String customerId = scanner.nextLine();
                        Rental.listRentalsByCustomer(customerId);
                    } else if (choice == 4) {
                        break;
                    } else {
                        System.out.println("Invalid choice. Please enter a valid option.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } else {
            // User Menu
            while (true) {
                System.out.println("===== User Menu =====");
                System.out.println("1. View Available Vehicles");
                System.out.println("2. Rent a Vehicle");
                System.out.println("3. Return a Vehicle");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                try {
                    if (choice == 1) {
                        System.out.println("\n== Available Vehicles ==\n");
                        Vehicle.listAvailableVehicles();
                    } else if (choice == 2) {
                        System.out.println("\n== Rent a Vehicle ==\n");
                        System.out.print("Enter the vehicle ID you want to rent: ");
                        String vehicleId = scanner.nextLine();

                        Vehicle selectedVehicle = Vehicle.getVehicleById(vehicleId);
                        if (selectedVehicle != null && selectedVehicle.isAvailable()) {
                            System.out.print("Enter your name: ");
                            String customerName = scanner.nextLine();

                            System.out.print("Enter the number of days for rental: ");
                            int rentalDays = scanner.nextInt();
                            scanner.nextLine(); // Consume newline

                            Customer newCustomer = new Customer("CUS" + System.currentTimeMillis(), customerName);
                            Customer.addCustomer(newCustomer);

                            double totalPrice = selectedVehicle.calculatePrice(rentalDays);
                            System.out.println("\n== Rental Information ==\n");
                            System.out.println("Customer ID: " + newCustomer.getCustomerId());
                            System.out.println("Customer Name: " + newCustomer.getName());
                            System.out.println("Vehicle: " + selectedVehicle.getBrand() + " " + selectedVehicle.getModel());
                            System.out.println("Rental Days: " + rentalDays);
                            System.out.printf("Total Price: $%.2f%n", totalPrice);

                            System.out.print("\nConfirm rental (Y/N): ");
                            String confirm = scanner.nextLine();

                            if (confirm.equalsIgnoreCase("Y")) {
                                Rental rental = new Rental(selectedVehicle, newCustomer, rentalDays);
                                Rental.addRental(rental);
                                Vehicle.updateVehicleAvailability(vehicleId, false);
                                System.out.println("\nVehicle rented successfully.");
                            } else {
                                System.out.println("\nRental canceled.");
                            }
                        } else {
                            System.out.println("\nInvalid vehicle selection or vehicle not available for rent.");
                        }
                    } else if (choice == 3) {
                        System.out.println("\n== Return a Vehicle ==\n");
                        System.out.print("Enter the vehicle ID you want to return: ");
                        String vehicleId = scanner.nextLine();

                        Vehicle vehicleToReturn = Vehicle.getVehicleById(vehicleId);
                        if (vehicleToReturn != null && !vehicleToReturn.isAvailable()) {
                            Rental.returnVehicle(vehicleId);
                            Vehicle.updateVehicleAvailability(vehicleId, true);
                            System.out.println("Vehicle returned successfully.");
                        } else {
                            System.out.println("Invalid vehicle ID or vehicle is not rented.");
                        }
                    } else if (choice == 4) {
                        break;
                    } else {
                        System.out.println("Invalid choice. Please enter a valid option.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }

        System.out.println("\nThank you for using the Vehicle Rental System!");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Simple login to differentiate between Admin and User
        System.out.println("Login as:");
        System.out.println("1. Admin");
        System.out.println("2. User");
        System.out.print("Enter your choice: ");
        int userType = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        boolean isAdmin = (userType == 1);

        CarRentalSystem rentalSystem = new CarRentalSystem();
        rentalSystem.menu(isAdmin);
    }
}
