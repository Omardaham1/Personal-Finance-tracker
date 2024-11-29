
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseManager {

    private List<Expense> expenses;
    private final String FILE_PATH = "expenses.txt"; // Path to the expense data file

    public ExpenseManager() {
        expenses = new ArrayList<>();
        loadExpensesFromFile(); // Load expenses when the application starts
        getTotalExpenses();
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        saveExpensesToFile(); // Save every time a new expense is added
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public double getTotalExpenses() {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }

    public void deleteExpense(Expense expense) {
        expenses.remove(expense);
        saveExpensesToFile(); // Save after deletion
    }

    public void updateExpense(Expense oldExpense, Expense newExpense) {
        int index = expenses.indexOf(oldExpense);
        if (index != -1) {
            expenses.set(index, newExpense);
            saveExpensesToFile(); // Save after updating
        }
    }

    private void loadExpensesFromFile() {
        try ( BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String category = parts[0];
                    String description = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    String date = parts[3];
                    expenses.add(new Expense(category, description, amount, date));
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file reading exceptions
        }
    }

    private void saveExpensesToFile() {
        try ( BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Expense expense : expenses) {
                writer.write(expense.getCategory() + ","
                        + expense.getDescription() + ","
                        + expense.getAmount() + ","
                        + expense.getDate());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file writing exceptions
        }
    }
}
