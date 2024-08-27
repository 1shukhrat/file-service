package ru.saynurdinov.file_service.service;

import ru.saynurdinov.file_service.dto.CreateFileRequest;
import ru.saynurdinov.file_service.dto.FileCreatedResponse;
import ru.saynurdinov.file_service.dto.FileResponse;

import java.util.List;

public interface FileService {

    List<FileResponse> findAll(int page, int size, String sortBy);

    FileResponse findById(Integer id);

    FileCreatedResponse create(CreateFileRequest request);

}
