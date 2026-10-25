package ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Task;

/** Table model for the five form fields plus the record ID. */
public class TaskTableModel extends AbstractTableModel {
    private final String[] columns = {"Bid", "Name", "Payment", "Status", "Enter", "Leave"};
    private List<Task> tasks = new ArrayList<>();

    @Override public int getRowCount() { return tasks.size(); }
    @Override public int getColumnCount() { return columns.length; }
    @Override public String getColumnName(int column) { return columns[column]; }
    @Override public Object getValueAt(int row, int column) {
        Task task = tasks.get(row);
        return switch (column) {
            case 0 -> task.getBid();
            case 1 -> task.getName();
            case 2 -> task.getPayment();
            case 3 -> task.getStatus();
            case 4 -> task.getEnter();
            case 5 -> task.getLeave();
            default -> null;
        };
    }
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks == null ? new ArrayList<>() : new ArrayList<>(tasks);
        fireTableDataChanged();
    }
    public String getTaskId(int row) { return tasks.get(row).getBid(); }
}
