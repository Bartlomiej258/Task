package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;
    @Mock
    private DbService service;

    @Mock
    private TaskMapper taskMapper;

    @Test
    void getTasksTest() {

        //Given
        List<Task> taskNewList =
                List.of(new Task(1L, "Title", "description"));
        List<TaskDto> taskDtoNewList =
                List.of(new TaskDto(1L, "Title1", "description"));

        when(taskMapper.mapToTaskDtoList(taskNewList)).thenReturn(taskDtoNewList);
        when(service.getAllTasks()).thenReturn(taskNewList);

        //When
        ResponseEntity<List<TaskDto>> tasksResponseList = taskController.getTasks();

        //Then
        assertThat(tasksResponseList.getBody()).isNotNull();
        assertEquals(tasksResponseList.getBody().get(0).getId(), 1L);
    }


    @Test
    void getTaskTest() throws Exception {

        //Given
        Task taskNew = new Task(1L, "Title", "description");
        TaskDto taskDtoNew = new TaskDto(1L, "Title1", "description");

        when(taskMapper.mapToTaskDto(taskNew)).thenReturn(taskDtoNew);
        when(service.getTask(taskNew.getId())).thenReturn(taskNew);

        //When
        ResponseEntity<TaskDto> tasksResponse = taskController.getTask(taskNew.getId());

        //Then
        assertEquals(tasksResponse.getBody().getTitle(), "Title1");
        assertEquals(tasksResponse.getBody().getId(), 1L);
    }

    @Test
    void deleteTask() {

        //Given
        Task taskNew = new Task(1L, "Title", "description");
        service.saveTask(taskNew);
        long id = taskNew.getId();

        //When
        ResponseEntity<Void> delete = taskController.deleteTask(id);

        //Then
        assertEquals(200, delete.getStatusCode().value());

        //Clean up
        service.deleteById(id);
    }
}