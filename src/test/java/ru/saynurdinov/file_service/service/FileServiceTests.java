package ru.saynurdinov.file_service.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.saynurdinov.file_service.dto.CreateFileRequest;
import ru.saynurdinov.file_service.dto.FileCreatedResponse;
import ru.saynurdinov.file_service.dto.FileResponse;
import ru.saynurdinov.file_service.entity.File;
import ru.saynurdinov.file_service.exception.FileNotFoundException;
import ru.saynurdinov.file_service.repository.FileRepository;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

public class FileServiceTests {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileServiceImpl fileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findAll_ReturnsListOfFiles() {
        File file1 = File.builder()
                .title("File 1")
                .description("Description 1")
                .creationDate(LocalDateTime.of(2023, 8, 26, 10, 0))
                .data("Data 1".getBytes())
                .build();

        File file2 = File.builder()
                .title("File 2")
                .description("Description 2")
                .creationDate(LocalDateTime.of(2023, 8, 26, 12, 0))
                .data("Data 2".getBytes())
                .build();

        File file3 = File.builder()
                .title("File 3")
                .description("Description 3")
                .creationDate(LocalDateTime.of(2023, 8, 26, 11, 0))
                .data("Data 3".getBytes())
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("creationDate"));
        when(fileRepository.findAll(eq(pageable))).thenReturn(new PageImpl<>(List.of(file1, file3, file2)));

        List<FileResponse> fileResponses = fileService.findAll(0, 10, "creationDate");

        assertEquals(3, fileResponses.size());
        assertEquals("File 1", fileResponses.get(0).title());
        assertEquals("File 3", fileResponses.get(1).title());
        assertEquals("File 2", fileResponses.get(2).title());

        verify(fileRepository, times(1)).findAll(pageable);
    }

    @Test
    void findById_ValidIdProvided_ReturnsFile() {
        File file = File.builder()
                .id(1)
                .title("Test Title")
                .description("Test Description")
                .creationDate(LocalDateTime.now())
                .data("Test Data".getBytes())
                .build();

        when(fileRepository.findById(1)).thenReturn(Optional.of(file));

        FileResponse fileResponse = fileService.findById(1);

        assertEquals("Test Title", fileResponse.title());
        assertEquals("Test Description", fileResponse.description());
        assertEquals(Base64.getEncoder().encodeToString("Test Data".getBytes()), fileResponse.base64Data());

        verify(fileRepository, times(1)).findById(1);
    }

    @Test
    void findById_InvalidIdProvided_ThrowsFileNotFoundException() {
        when(fileRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(FileNotFoundException.class, () -> fileService.findById(1));

        verify(fileRepository, times(1)).findById(1);
    }

    @Test
    void create_ReturnsIdOfCreatedFile() {
        CreateFileRequest request = new CreateFileRequest("Test Title", "Test Description", LocalDateTime.now(), Base64.getEncoder().encodeToString("Test Data".getBytes()));
        File file = File.builder()
                .id(1)
                .title(request.title())
                .description(request.description())
                .creationDate(request.creationDate())
                .data(Base64.getDecoder().decode(request.base64Data()))
                .build();

        when(fileRepository.save(any(File.class))).thenReturn(file);

        FileCreatedResponse response = fileService.create(request);

        assertEquals("File was created", response.responseMessage());
        assertEquals(1, response.fileId());

        verify(fileRepository, times(1)).save(any(File.class));
    }
}
