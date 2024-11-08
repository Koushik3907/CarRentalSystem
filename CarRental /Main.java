import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Base class representing a general Vehicle
class Vehicle {
    private String vehicleId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Vehicle(String vehicleId, String brand, String model, double basePricePerDay) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
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

    public void rent() {
        isAvailable = false;
    }

    public void returnVehicle() {
        isAvailable = true;
    }
}

// Car class inherits from Vehicle
class Car extends Vehicle {
    public Car(String carId, String brand, String model, double basePricePerDay) {
        super(carId, brand, model, basePricePerDay);
    }
}

// Customer class
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
}

// Rental class to associate a Vehicle with a Customer
class Rental {
    private Vehicle vehicle;
    private Customer customer;
    private int days;

    public Rental(Vehicle vehicle, Customer customer, int days) {
        this.vehicle = vehicle;
        this.customer = customer;
        this.days = days;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

// CarRentalSystem class
class CarRentalSystem {
    private List<Vehicle> vehicles;
    private List<Customer> customers;
    private List<Rental> rentals;

    public CarRentalSystem() {
        vehicles = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, int days) {
        if (vehicle.isAvailable()) {
            vehicle.rent();
            rentals.add(new Rental(vehicle, customer, days));
        } else {
            System.out.println("Vehicle is not available for rent.");
        }
    }

    public void returnVehicle(Vehicle vehicle) {
        vehicle.returnVehicle();
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getVehicle() == vehicle) {
                rentalToRemove = rental;
                break;
            }
        }
        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
        } else {
            System.out.println("Vehicle was not rented.");
        }
    }

    public void listRentalsByCustomer(String customerId) {
        boolean found = false;
        for (Rental rental : rentals) {
            if (rental.getCustomer().getCustomerId().equals(customerId)) {
                found = true;
                System.out.println("Customer: " + rental.getCustomer().getName());
                System.out.println("Vehicle: " + rental.getVehicle().getBrand() + " " + rental.getVehicle().getModel());
                System.out.println("Rental Days: " + rental.getDays());
                System.out.printf("Total Price: $%.2f%n", rental.getVehicle().calculatePrice(rental.getDays()));
                System.out.println("----------------------------");
            }
        }
        if (!found) {
            System.out.println("No rentals found for customer ID: " + customerId);
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===== Vehicle Rental System =====");
            System.out.println("1. Rent a Vehicle");
            System.out.println("2. Return a Vehicle");
            System.out.println("3. List Rentals by Customer");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.println("\n== Rent a Vehicle ==\n");

                System.out.println("\nAvailable Vehicles:");
                for (Vehicle vehicle : vehicles) {
                    if (vehicle.isAvailable()) {
                        System.out.println(vehicle.getVehicleId() + " - " + vehicle.getBrand() + " " + vehicle.getModel());
                    }
                }

                System.out.print("\nEnter the vehicle ID you want to rent: ");
                String vehicleId = scanner.nextLine();

                // Find the selected vehicle with case-insensitive matching
                Vehicle selectedVehicle = null;
                for (Vehicle vehicle : vehicles) {
                    if (vehicle.getVehicleId().equalsIgnoreCase(vehicleId) && vehicle.isAvailable()) {
                        selectedVehicle = vehicle;
                        break;
                    }
                }

                if (selectedVehicle != null) {
                    System.out.print("Enter your name: ");
                    String customerName = scanner.nextLine();

                    System.out.print("Enter the number of days for rental: ");
                    int rentalDays = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
                    addCustomer(newCustomer);

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
                        rentVehicle(selectedVehicle, newCustomer, rentalDays);
                        System.out.println("\nVehicle rented successfully.");
                    } else {
                        System.out.println("\nRental canceled.");
                    }
                } else {
                    System.out.println("\nInvalid vehicle selection or vehicle not available for rent.");
                }
            } else if (choice == 2) {
                System.out.println("\n== Return a Vehicle ==\n");
                System.out.print("Enter the vehicle ID you want to return: ");
                String vehicleId = scanner.nextLine();

                Vehicle vehicleToReturn = null;
                for (Vehicle vehicle : vehicles) {
                    if (vehicle.getVehicleId().equalsIgnoreCase(vehicleId) && !vehicle.isAvailable()) {
                        vehicleToReturn = vehicle;
                        break;
                    }
                }

                if (vehicleToReturn != null) {
                    Customer customer = null;
                    for (Rental rental : rentals) {
                        if (rental.getVehicle() == vehicleToReturn) {
                            customer = rental.getCustomer();
                            break;
                        }
                    }

                    if (customer != null) {
                        returnVehicle(vehicleToReturn);
                        System.out.println("Vehicle returned successfully by " + customer.getName());
                    } else {
                        System.out.println("Vehicle was not rented or rental information is missing.");
                    }
                } else {
                    System.out.println("Invalid vehicle ID or vehicle is not rented.");
                }
            } else if (choice == 3) {
                System.out.println("\n== List Rentals by Customer ==\n");
                System.out.print("Enter the customer ID: ");
                String customerId = scanner.nextLine();
                listRentalsByCustomer(customerId);
            } else if (choice == 4) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

        System.out.println("\nThank you for using the Vehicle Rental System!");
    }
}

public class Main {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        Car car1 = new Car("C001", "Toyota", "Camry", 60.0);
        Car car2 = new Car("C002", "Honda", "Accord", 70.0);
        Car car3 = new Car("C003", "Mahindra", "Thar", 150.0);
        rentalSystem.addVehicle(car1);
        rentalSystem.addVehicle(car2);
        rentalSystem.addVehicle(car3);

        rentalSystem.menu();
    }
}
