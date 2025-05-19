package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.TaskEntity;

import java.util.List;

public interface TaskRepositoryInt {

    TaskEntity save(TaskEntity task);

    TaskEntity findById(int id);

    List<TaskEntity> findAll();

    boolean deleteById(int id);

    boolean update(TaskEntity task);


}
