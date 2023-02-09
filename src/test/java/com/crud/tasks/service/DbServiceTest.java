package com.crud.tasks.service;

import com.crud.tasks.domain.Task;
import com.crud.tasks.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DbServiceTest {

    @Autowired
    private DbService dbService;

    @Mock
    TaskRepository taskRepository;

    @Test
    void getAllTasks(){
        //Given
        Task task1 = new Task(1L,"title1","1");
        Task task2 = new Task(2L,"title2","2");
        List<Task> list = new ArrayList<>();
        list.add(task1);
        list.add(task2);
        when(taskRepository.findAll()).thenReturn(list);
        //When
        List<Task> result = dbService.getAllTasks();
        //Then
        assertEquals(2,result.size());
        //Cleanup
        dbService.deleteById(task1.getId());
        dbService.deleteById(task2.getId());
    }

    @Test
    void saveTaskTest(){
        //Given
        Task task1 = new Task(2L,"test","test");
        when(taskRepository.save(task1)).thenReturn(task1);
        //When
        Task result = dbService.saveTask(task1);
        //Then
        assertEquals(task1.getId(),result.getId());
        assertEquals(task1.getContent(), result.getContent());
        assertEquals(task1.getTitle(), result.getTitle());

        dbService.deleteById(task1.getId());
    }

    @Test
    void getTaskTest() throws Exception{

        //Given
        Task task = new Task(null, "test task", "test");

        //When
        dbService.saveTask(task);

        //Then
        long id = task.getId();
        List<Task> tasks  = dbService.getAllTasks();
        Task findTask = dbService.getTask(id);

        assertEquals(task.getTitle(), findTask.getTitle());

        //Cleanup
        dbService.deleteById(id);

    }
}
