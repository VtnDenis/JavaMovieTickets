package service;

import model.Discount;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DiscountService {
    private static final String DISCOUNT_FILE_PATH = "discounts.txt";

    public List<Discount> getAllDiscounts() {
        List<Discount> discounts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DISCOUNT_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Ignorer les lignes vides et commentaires
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    try {
                        String code = parts[0];
                        String description = parts[1];
                        double percentage = Double.parseDouble(parts[2]);
                        boolean active = Boolean.parseBoolean(parts[3]);

                        Discount discount = new Discount(code, description, percentage, active);
                        discounts.add(discount);
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing numeric value in line: " + line);
                    }
                } else {
                    System.out.println("Invalid format in line: " + line);
                }
            }
        } catch (IOException e) {
            // If file doesn't exist, create it
            try {
                new File(DISCOUNT_FILE_PATH).createNewFile();
            } catch (IOException ex) {
                System.out.println("Error creating discounts file: " + ex.getMessage());
            }
        }
        return discounts;
    }

    public void saveDiscounts(List<Discount> discounts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DISCOUNT_FILE_PATH))) {
            for (Discount discount : discounts) {
                writer.write(discount.getCode() + "," + discount.getDescription() + "," +
                             discount.getPercentage() + "," + discount.isActive());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing discounts file: " + e.getMessage());
        }
    }

    public void addDiscount(Discount discount) {
        List<Discount> discounts = getAllDiscounts();
        discounts.add(discount);
        saveDiscounts(discounts);
    }

    public void updateDiscount(Discount updatedDiscount) {
        List<Discount> discounts = getAllDiscounts();
        for (int i = 0; i < discounts.size(); i++) {
            if (discounts.get(i).getCode().equals(updatedDiscount.getCode())) {
                discounts.set(i, updatedDiscount);
                break;
            }
        }
        saveDiscounts(discounts);
    }

    public void deleteDiscount(String code) {
        List<Discount> discounts = getAllDiscounts();
        discounts.removeIf(discount -> discount.getCode().equals(code));
        saveDiscounts(discounts);
    }

    public Discount getDiscountByCode(String code) {
        List<Discount> discounts = getAllDiscounts();
        for (Discount discount : discounts) {
            if (discount.getCode().equals(code)) {
                return discount;
            }
        }
        return null;
    }
}