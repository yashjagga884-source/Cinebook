package util;

import model.Customer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Plain CSV persistence for registered customer accounts. */
public class FileCustomerRepository {
    private static final String HEADER = "customer_id,name,email,password";
    private final Path file;

    public FileCustomerRepository(Path file) throws IOException {
        this.file = file.toAbsolutePath().normalize();
        Path parent = this.file.getParent();
        if (parent != null) Files.createDirectories(parent);
        if (Files.notExists(this.file) || Files.size(this.file) == 0) {
            Files.writeString(this.file, HEADER + System.lineSeparator(), StandardCharsets.UTF_8);
        }
    }

    public synchronized List<Customer> loadAll() throws IOException {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        List<Customer> customers = new ArrayList<>();
        for (int lineNumber = 1; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            if (line.isBlank()) continue;
            List<String> fields;
            try {
                fields = parseCsv(line);
            } catch (IllegalArgumentException ex) {
                throw new IOException("Malformed CSV at line " + (lineNumber + 1) + ".", ex);
            }
            if (fields.size() != 4) {
                throw new IOException("Expected 4 CSV fields at line " + (lineNumber + 1) + ".");
            }
            customers.add(new Customer(
                    fields.get(0),
                    fields.get(1),
                    fields.get(2).toLowerCase(Locale.ROOT),
                    fields.get(3)
            ));
        }
        return customers;
    }

    public synchronized void save(Customer customer) throws IOException {
        for (Customer existing : loadAll()) {
            if (existing.getEmail().equalsIgnoreCase(customer.getEmail())) {
                throw new IOException("Customer email already exists in the CSV file.");
            }
        }
        String row = String.join(",",
                escapeCsv(customer.getUserId()),
                escapeCsv(customer.getName()),
                escapeCsv(customer.getEmail()),
                escapeCsv(customer.getPassword()));
        String oldContent = Files.readString(file, StandardCharsets.UTF_8);
        String separator = oldContent.endsWith("\n") || oldContent.endsWith("\r") ? "" : System.lineSeparator();
        String newContent = oldContent + separator + row + System.lineSeparator();
        Path parent = file.getParent();
        Path temporaryFile = Files.createTempFile(parent, "customers-", ".tmp");
        try {
            Files.writeString(temporaryFile, newContent, StandardCharsets.UTF_8);
            try {
                Files.move(temporaryFile, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(temporaryFile, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }

    private static String escapeCsv(String value) {
        String safe = value == null ? "" : value;
        if (safe.contains(",") || safe.contains("\"") || safe.contains("\n") || safe.contains("\r")) {
            return "\"" + safe.replace("\"", "\"\"") + "\"";
        }
        return safe;
    }

    private static List<String> parseCsv(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (quoted) {
                if (ch == '"' && i + 1 < line.length() && line.charAt(i + 1) == '"') { value.append('"'); i++; }
                else if (ch == '"') quoted = false;
                else value.append(ch);
            } else if (ch == ',') {
                values.add(value.toString()); value.setLength(0);
            } else if (ch == '"' && value.length() == 0) quoted = true;
            else value.append(ch);
        }
        if (quoted) throw new IllegalArgumentException("Unclosed quoted CSV field.");
        values.add(value.toString());
        return values;
    }
}
