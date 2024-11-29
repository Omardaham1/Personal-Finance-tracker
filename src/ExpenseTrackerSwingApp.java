import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpenseTrackerSwingApp {

    private JFrame frame;
    private JTextField descriptionField;
    private JTextField amountField;
    private JTextField dateField;
    private JComboBox<String> categoryComboBox;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private ExpenseManager expenseManager;
    private Expense selectedExpense;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ExpenseTrackerSwingApp window = new ExpenseTrackerSwingApp();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ExpenseTrackerSwingApp() {
        expenseManager = new ExpenseManager();
        initialize();
    }

   private void initialize() {
    // Main frame setup
    frame = new JFrame("Expense Tracker");
    frame.setBounds(100, 100, 800, 600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setIconImage(new ImageIcon("images/circle_background_.jpg").getImage()); // Set app icon

    // Main panel
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout(10, 10));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    frame.getContentPane().add(mainPanel);

    // Title label
    JLabel titleLabel = new JLabel("Expense Tracker", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
    titleLabel.setForeground(new Color(51, 51, 51));
    mainPanel.add(titleLabel, BorderLayout.NORTH);

    // Input panel
    JPanel inputPanel = new JPanel();
    GroupLayout layout = new GroupLayout(inputPanel);
    inputPanel.setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);

    JLabel descriptionLabel = new JLabel("Description:");
    JLabel amountLabel = new JLabel("Amount:");
    JLabel categoryLabel = new JLabel("Category:");
    JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");

    descriptionField = new JTextField(20);
    amountField = new JTextField(20);
    categoryComboBox = new JComboBox<>(new String[]{
        "Food", "Transport", "Entertainment", "Utilities", "Health", "Education",
        "Housing", "Travel", "Miscellaneous", "Other"
    });
    dateField = new JTextField(20);
    dateField.setText(getCurrentDate());

    layout.setHorizontalGroup(
        layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(descriptionLabel)
                .addComponent(amountLabel)
                .addComponent(categoryLabel)
                .addComponent(dateLabel))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(descriptionField)
                .addComponent(amountField)
                .addComponent(categoryComboBox)
                .addComponent(dateField))
    );

    layout.setVerticalGroup(
        layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(descriptionLabel)
                .addComponent(descriptionField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(amountLabel)
                .addComponent(amountField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(categoryLabel)
                .addComponent(categoryComboBox))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(dateLabel)
                .addComponent(dateField))
    );

    mainPanel.add(inputPanel, BorderLayout.CENTER);

    // Button panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
    addButton = new JButton("Add Expense");
    editButton = new JButton("Edit Expense");
    deleteButton = new JButton("Delete Expense");

    addButton.setPreferredSize(new Dimension(120, 40));
    editButton.setPreferredSize(new Dimension(120, 40));
    deleteButton.setPreferredSize(new Dimension(120, 40));

    buttonPanel.add(addButton);
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);

    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    // Expense table
    tableModel = new DefaultTableModel(new Object[]{"Category", "Description", "Amount", "Date"}, 0);
    expenseTable = new JTable(tableModel);
    expenseTable.setRowHeight(25);
    expenseTable.setFont(new Font("Arial", Font.PLAIN, 14));
    expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    JScrollPane tableScrollPane = new JScrollPane(expenseTable);
    mainPanel.add(tableScrollPane, BorderLayout.EAST);

    // Event handlers
    addButton.addActionListener(e -> handleAddExpense());
    editButton.addActionListener(e -> handleEditExpense());
    deleteButton.addActionListener(e -> handleDeleteExpense());

    expenseTable.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            int selectedRow = expenseTable.getSelectedRow();
            if (selectedRow != -1) {
                selectedExpense = expenseManager.getExpenses().get(selectedRow);
                descriptionField.setText(selectedExpense.getDescription());
                amountField.setText(String.valueOf(selectedExpense.getAmount()));
                dateField.setText(selectedExpense.getDate());
                categoryComboBox.setSelectedItem(selectedExpense.getCategory());
            }
        }
    });

    updateExpenseTable();
}


    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    private void handleAddExpense() {
        String description = descriptionField.getText();
        String category = (String) categoryComboBox.getSelectedItem();
        String date = dateField.getText();
        double amount = Double.parseDouble(amountField.getText());

        if (!description.isEmpty() && category != null && !date.isEmpty() && amount > 0) {
            Expense expense = new Expense(category, description, amount, date);
            expenseManager.addExpense(expense);
            updateExpenseTable();
        } else {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields correctly.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleEditExpense() {
        if (selectedExpense != null) {
            String description = descriptionField.getText();
            String category = (String) categoryComboBox.getSelectedItem();
            String date = dateField.getText();
            double amount = Double.parseDouble(amountField.getText());

            if (!description.isEmpty() && category != null && !date.isEmpty() && amount > 0) {
                selectedExpense.setDescription(description);
                selectedExpense.setCategory(category);
                selectedExpense.setAmount(amount);
                selectedExpense.setDate(date);
                updateExpenseTable();
            } else {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields correctly.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleDeleteExpense() {
        if (selectedExpense != null) {
            expenseManager.deleteExpense(selectedExpense);
            updateExpenseTable();
        }
    }

    private void updateExpenseTable() {
        tableModel.setRowCount(0);
        for (Expense expense : expenseManager.getExpenses()) {
            tableModel.addRow(new Object[]{expense.getCategory(), expense.getDescription(),
                expense.getAmount(), expense.getDate()});
        }
    }
}
