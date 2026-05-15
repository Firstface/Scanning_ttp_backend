package com.example.hivesampling.repository;

import com.example.hivesampling.model.TaskContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskRepositoryTest {

    private InMemoryTaskRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTaskRepository();
    }

    @Test
    void saveAndFindById_ShouldWork() {
        TaskContext task = new TaskContext();
        task.taskId = "test-1";
        task.databaseName = "demo_db";

        repository.save(task);
        Optional<TaskContext> found = repository.findById("test-1");

        assertTrue(found.isPresent());
        assertEquals("demo_db", found.get().databaseName);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Optional<TaskContext> found = repository.findById("non-existent");
        assertFalse(found.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllSavedTasks() {
        TaskContext task1 = new TaskContext();
        task1.taskId = "1";
        TaskContext task2 = new TaskContext();
        task2.taskId = "2";

        repository.save(task1);
        repository.save(task2);

        assertEquals(2, repository.findAll().size());
    }

    @Test
    void saveDuplicate_ShouldOverride() {
        TaskContext task1 = new TaskContext();
        task1.taskId = "duplicate";
        task1.databaseName = "first";

        TaskContext task2 = new TaskContext();
        task2.taskId = "duplicate";
        task2.databaseName = "second";

        repository.save(task1);
        repository.save(task2);

        assertEquals(1, repository.findAll().size());
        assertEquals("second", repository.findById("duplicate").get().databaseName);
    }

    @Test
    void findAll_EmptyInitially() {
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void save_ShouldPersistAllFields() {
        TaskContext task = new TaskContext();
        task.taskId = "full-task";
        task.databaseName = "test_db";
        task.tableName = "test_table";
        task.targetSampleRows = 1000L;

        repository.save(task);
        TaskContext found = repository.findById("full-task").get();

        assertEquals("test_db", found.databaseName);
        assertEquals("test_table", found.tableName);
        assertEquals(1000L, found.targetSampleRows);
    }

    @Test
    void findById_CaseSensitive() {
        TaskContext task = new TaskContext();
        task.taskId = "CaseSensitive";
        repository.save(task);

        assertFalse(repository.findById("casesensitive").isPresent());
        assertTrue(repository.findById("CaseSensitive").isPresent());
    }
}
