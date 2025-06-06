package service;

import model.Customer;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    private static final String CUSTOMER_FILE_PATH = "customers.txt";

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Ignorer les lignes vides et commentaires
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    try {
                        boolean isStudent = Boolean.parseBoolean(parts[2]);
                        Customer customer = new Customer(parts[0], parts[1], isStudent);
                        customers.add(customer);
                    } catch (Exception e) {
                        System.out.println("Error parsing line: " + line + " - " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid format in line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading customers file: " + e.getMessage());
        }
        return customers;
    }

    public Customer getCustomerByUsername(String username) {
        List<Customer> customers = getAllCustomers();
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                return customer;
            }
        }
        return null;
    }

    public void saveCustomers(List<Customer> customers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_FILE_PATH))) {
            for (Customer customer : customers) {
                writer.write(customer.getUsername() + "," + customer.getFullName() + "," + customer.isStudent());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing customers file: " + e.getMessage());
        }
    }

    public void addCustomer(Customer customer) {
        List<Customer> customers = getAllCustomers();
        customers.add(customer);
        saveCustomers(customers);
    }

    public void updateCustomer(Customer updatedCustomer) {
        List<Customer> customers = getAllCustomers();
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getUsername().equals(updatedCustomer.getUsername())) {
                customers.set(i, updatedCustomer);
                break;
            }
        }
        saveCustomers(customers);
    }

    public void deleteCustomer(String username) {
        List<Customer> customers = getAllCustomers();
        customers.removeIf(customer -> customer.getUsername().equals(username));
        saveCustomers(customers);
    }
}
