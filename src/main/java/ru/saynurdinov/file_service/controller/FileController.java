package ru.saynurdinov.file_service.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.saynurdinov.file_service.dto.CreateFileRequest;
import ru.saynurdinov.file_service.dto.FileCreatedResponse;
import ru.saynurdinov.file_service.dto.FileResponse;
import ru.saynurdinov.file_service.service.FileService;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public ResponseEntity<List<FileResponse>> findAll(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "creationDate") String sort) {
        return ResponseEntity.ok(fileService.findAll(page, size, sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(fileService.findById(id));
    }

    @PostMapping
    public ResponseEntity<FileCreatedResponse> create(@RequestBody @Valid CreateFileRequest request) {
        return ResponseEntity.ok(fileService.create(request));
    }

}
