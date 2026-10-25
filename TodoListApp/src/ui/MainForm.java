package ui;

import controller.TaskController;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import model.Task;

/** Main window retaining the original Name, Payment, Status, Enter and Leave fields. */
public class MainForm extends JFrame {
    private final TaskController controller;
    private final TaskTableModel tableModel = new TaskTableModel();
    private final JTable table = new JTable(tableModel);
    private final JTextField nameField = new JTextField(22);
    private final JTextField paymentField = new JTextField(22);
    private final JTextField statusField = new JTextField(22);
    private final JTextField enterField = new JTextField(22);
    private final JTextField leaveField = new JTextField(22);
    private final JButton saveButton = new JButton("Save");
    private final JButton deleteButton = new JButton("Delete");
    private final JButton clearButton = new JButton("Clear");
    private String selectedId;

    public MainForm(TaskController controller) {
        this.controller = controller;
        setTitle("Border List Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 520);
        setLocationRelativeTo(null);
        buildUi();
        refreshTaskList();
    }

    private void buildUi() {
        JPanel fields = new JPanel(new GridBagLayout());
        String[] labels = {"Name", "Payment", "Status", "Enter", "Leave"};
        JTextField[] inputs = {nameField, paymentField, statusField, enterField, leaveField};
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 8, 5, 8);
        c.anchor = GridBagConstraints.WEST;
        for (int i = 0; i < labels.length; i++) {
            c.gridx = 0; c.gridy = i; c.weightx = 0; c.fill = GridBagConstraints.NONE;
            fields.add(new JLabel(labels[i] + ":"), c);
            c.gridx = 1; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
            fields.add(inputs[i], c);
        }
        JPanel buttons = new JPanel();
        buttons.add(saveButton); buttons.add(deleteButton); buttons.add(clearButton);
        c.gridx = 0; c.gridy = labels.length; c.gridwidth = 2; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        fields.add(buttons, c);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(fields, BorderLayout.EAST);

        saveButton.addActionListener(e -> saveRecord());
        deleteButton.addActionListener(e -> deleteRecord());
        clearButton.addActionListener(e -> clearForm());
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showSelectedRecord();
        });
        deleteButton.setEnabled(false);
    }

    public void refreshTaskList() {
        String keepId = selectedId;
        tableModel.setTasks(controller.getAllTask());
        if (keepId != null) {
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                if (keepId.equals(tableModel.getTaskId(row))) {
                    int viewRow = table.convertRowIndexToView(row);
                    if (viewRow >= 0) table.setRowSelectionInterval(viewRow, viewRow);
                    return;
                }
            }
        }
    }

    private void saveRecord() {
        String name = nameField.getText();
        String payment = paymentField.getText();
        String status = statusField.getText();
        String enter = enterField.getText();
        String leave = leaveField.getText();
        if (name.isBlank() || payment.isBlank() || status.isBlank() || enter.isBlank() || leave.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please complete all fields.", "Missing information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean ok = selectedId == null
                ? controller.handleAddTask(name, payment, status, enter, leave)
                : controller.handleUpdateTask(selectedId, name, payment, status, enter, leave);
        if (ok) {
            JOptionPane.showMessageDialog(this, selectedId == null ? "Record added successfully." : "Record updated successfully.");
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "The record could not be saved. Please try again.", "Save error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showSelectedRecord() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) return;
        int modelRow = table.convertRowIndexToModel(viewRow);
        selectedId = tableModel.getTaskId(modelRow);
        Task task = controller.getTaskDetails(selectedId);
        if (task == null) return;
        nameField.setText(task.getName());
        paymentField.setText(task.getPayment());
        statusField.setText(task.getStatus());
        enterField.setText(task.getEnter());
        leaveField.setText(task.getLeave());
        saveButton.setText("Update");
        deleteButton.setEnabled(true);
    }

    private void deleteRecord() {
        if (selectedId == null) return;
        int answer = JOptionPane.showConfirmDialog(this, "Delete the selected record?", "Confirm deletion", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            if (controller.handleDeleteTask(selectedId)) {
                clearForm();
                JOptionPane.showMessageDialog(this, "Record deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "The record could not be deleted.", "Delete error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        selectedId = null;
        nameField.setText(""); paymentField.setText(""); statusField.setText("");
        enterField.setText(""); leaveField.setText("");
        table.clearSelection();
        saveButton.setText("Save");
        deleteButton.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            model.TaskManager manager = new model.TaskManager();
            manager.loadTasks();
            TaskController controller = new TaskController(manager);
            MainForm form = new MainForm(controller);
            controller.setView(form);
            form.setVisible(true);
        });
    }
}
