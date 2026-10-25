package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.IdGenerator;

/** Stores records and persists them to a local serialized file. */
public class TaskManager {
    private Map<String, Task> tasks = new HashMap<>();
    private static final String FILE_PATH = "taskslist.ser";
    private static final Logger LOGGER = Logger.getLogger(TaskManager.class.getName());

    public void addTask(Task task) {
        if (task == null) throw new IllegalArgumentException("Task cannot be null.");
        String id;
        do {
            id = IdGenerator.generateId(java.time.Year.now().getValue());
        } while (tasks.containsKey(id));
        task.setBid(id);
        tasks.put(id, task);
    }

    public Task deleteTask(String id) { return tasks.remove(id); }
    public Task findTask(String id) { return tasks.get(id); }
    public Map<String, Task> findAll() { return tasks; }

    public void setTask(Task task) {
        if (task != null && task.getBid() != null && tasks.containsKey(task.getBid())) {
            tasks.put(task.getBid(), task);
        }
    }

    public void loadTasks() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object data = in.readObject();
            if (data instanceof Map<?, ?> loaded) {
                Map<String, Task> valid = new HashMap<>();
                for (Map.Entry<?, ?> entry : loaded.entrySet()) {
                    if (entry.getKey() instanceof String key && entry.getValue() instanceof Task value) {
                        valid.put(key, value);
                    }
                }
                tasks = valid;
            }
        } catch (java.io.FileNotFoundException ex) {
            // First run: there is no saved file yet.
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.log(Level.WARNING, "Could not load saved records. Starting with an empty list.", ex);
            tasks = new HashMap<>();
        }
    }

    /** Returns 0 on success and 1 if saving failed. */
    public int saveTasks() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(tasks);
            return 0;
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not save records.", ex);
            return 1;
        }
    }
}
