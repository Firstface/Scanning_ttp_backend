package com.example.hivesampling.repository;

import com.example.hivesampling.model.TaskContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InMemoryTaskRepositoryTest {

    private InMemoryTaskRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTaskRepository();
    }

    @Test
    void saveAndFindById_ShouldWork() {
        TaskContext task = new TaskContext();
        task.setTaskId("test-1");
        task.setDatabaseName("demo_db");

        repository.save(task);
        Optional<TaskContext> found = repository.findById("test-1");

        assertTrue(found.isPresent());
        assertEquals("demo_db", found.get().getDatabaseName());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Optional<TaskContext> found = repository.findById("non-existent");
        assertFalse(found.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllSavedTasks() {
        TaskContext task1 = new TaskContext();
        task1.setTaskId("1");
        TaskContext task2 = new TaskContext();
        task2.setTaskId("2");

        repository.save(task1);
        repository.save(task2);

        assertEquals(2, repository.findAll().size());
    }

    @Test
    void findActiveTasks_ShouldFilterCorrectly() {
        TaskContext runningTask = new TaskContext();
        runningTask.setTaskId("running");
        repository.save(runningTask);

        assertEquals(1, repository.findActiveTasks().size());
    }
}
