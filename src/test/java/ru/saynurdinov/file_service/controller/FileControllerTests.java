package ru.saynurdinov.file_service.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.saynurdinov.file_service.dto.CreateFileRequest;
import ru.saynurdinov.file_service.dto.FileCreatedResponse;
import ru.saynurdinov.file_service.dto.FileResponse;
import ru.saynurdinov.file_service.exception.FileNotFoundException;
import ru.saynurdinov.file_service.service.FileService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FileController.class)
@AutoConfigureMockMvc
public class FileControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void findAll_ValidParams_ReturnsFiles() throws Exception {

        List<FileResponse> files = List.of(
                new FileResponse("File 1", "Description 1", LocalDateTime.now(), "base64Data1"),
                new FileResponse("File 2", "Description 2", LocalDateTime.now(), "base64Data2")
        );

        when(fileService.findAll(anyInt(), anyInt(), anyString())).thenReturn(files);

        mockMvc.perform(get("/api/files")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "creationDate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(files.size()))
                .andExpect(jsonPath("$[0].title").value("File 1"))
                .andExpect(jsonPath("$[1].title").value("File 2"));
    }

    @Test
    void findAll_InvalidParams_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/files")
                        .param("page", "invalid")
                        .param("size", "invalid")
                        .param("sort", "invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById_ValidParams_ReturnsFile() throws Exception {

        int fileId = 1;
        FileResponse fileResponse = new FileResponse("File 1", "Description 1", LocalDateTime.now(), "base64Data");

        when(fileService.findById(fileId)).thenReturn(fileResponse);

        mockMvc.perform(get("/api/files/{id}", fileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("File 1"))
                .andExpect(jsonPath("$.description").value("Description 1"));
    }

    @Test
    void findById_ValidParams_ReturnsNotFound() throws Exception {
        int fileId = 1;
        when(fileService.findById(fileId)).thenThrow(new FileNotFoundException("File not found"));

        mockMvc.perform(get("/api/files/{id}", fileId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("File not found"));
    }

    @Test
    void findById_InvalidParams_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/files/invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_ValidRequestBody_ReturnsCreatedFile() throws Exception {
        CreateFileRequest request = new CreateFileRequest("File 1", "Description 1", LocalDateTime.now(), "base64Data");
        FileCreatedResponse response = new FileCreatedResponse("File was created", 1);

        when(fileService.create(any(CreateFileRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/files")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("File was created"))
                .andExpect(jsonPath("$.fileId").value(1));
    }

    @Test
    void create_InvalidRequestBody_ReturnsBadRequest() throws Exception {
        CreateFileRequest invalidRequest = new CreateFileRequest("", "Description", LocalDateTime.now(), "base64Data");

        mockMvc.perform(post("/api/files")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }


}
