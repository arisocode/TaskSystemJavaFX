package gm.tasks.service;

import gm.tasks.domain.Task;

import java.util.List;

public interface ITaskService {
    public List<Task> getAllTask();
    public Task getTaskById(Integer id);
    public void saveTask(Task task);
    public void deleteTask(Integer id);
}
