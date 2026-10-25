package controller;

import java.util.ArrayList;
import java.util.List;
import model.Task;
import model.TaskManager;
import ui.MainForm;

/** Connects the form to the data model. */
public class TaskController {
    private final TaskManager model;
    private MainForm view;

    public TaskController(TaskManager model) { this.model = model; }
    public void setView(MainForm view) { this.view = view; }

    public boolean handleAddTask(String name, String payment, String status, String enter, String leave) {
        if (isBlank(name) || isBlank(payment) || isBlank(status) || isBlank(enter) || isBlank(leave)) return false;
        Task task = new Task();
        task.setName(name.trim()); task.setPayment(payment.trim()); task.setStatus(status.trim());
        task.setEnter(enter.trim()); task.setLeave(leave.trim());
        model.addTask(task);
        boolean saved = model.saveTasks() == 0;
        refreshView();
        return saved;
    }

    public boolean handleUpdateTask(String id, String name, String payment, String status, String enter, String leave) {
        if (isBlank(id) || isBlank(name) || isBlank(payment) || isBlank(status) || isBlank(enter) || isBlank(leave)) return false;
        Task task = model.findTask(id);
        if (task == null) return false;
        task.setName(name.trim()); task.setPayment(payment.trim()); task.setStatus(status.trim());
        task.setEnter(enter.trim()); task.setLeave(leave.trim());
        model.setTask(task);
        boolean saved = model.saveTasks() == 0;
        refreshView();
        return saved;
    }

    public boolean handleDeleteTask(String id) {
        if (model.deleteTask(id) == null) return false;
        boolean saved = model.saveTasks() == 0;
        refreshView();
        return saved;
    }

    public List<Task> getAllTask() { return new ArrayList<>(model.findAll().values()); }
    public Task getTaskDetails(String id) { return model.findTask(id); }
    private void refreshView() { if (view != null) view.refreshTaskList(); }
    private static boolean isBlank(String value) { return value == null || value.trim().isEmpty(); }
}
