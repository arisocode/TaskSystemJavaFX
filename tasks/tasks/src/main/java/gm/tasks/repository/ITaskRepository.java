package gm.tasks.repository;

import gm.tasks.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<Task, Integer> {
}
