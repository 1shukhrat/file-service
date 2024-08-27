package ru.saynurdinov.file_service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.saynurdinov.file_service.dto.CreateFileRequest;
import ru.saynurdinov.file_service.dto.FileCreatedResponse;
import ru.saynurdinov.file_service.dto.FileResponse;
import ru.saynurdinov.file_service.entity.File;
import ru.saynurdinov.file_service.exception.FileNotFoundException;
import ru.saynurdinov.file_service.repository.FileRepository;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public List<FileResponse> findAll(int page, int size, String sortBy) {
        Pageable pageable= PageRequest.of(page, size, Sort.by(sortBy));
        return fileRepository.findAll(pageable).getContent()
                .stream().map(file -> new FileResponse
                        (
                                file.getTitle(),
                                file.getDescription(),
                                file.getCreationDate(),
                                Base64.getEncoder().encodeToString(file.getData())
                        )).toList();
    }

    @Override
    public FileResponse findById(Integer id) {
        Optional<File> optionalFile = fileRepository.findById(id);
        if (optionalFile.isPresent()) {
            File file = optionalFile.get();
            return new FileResponse(
                    file.getTitle(),
                    file.getDescription(),
                    file.getCreationDate(),
                    Base64.getEncoder().encodeToString(file.getData())
            );
        } else {
            throw new FileNotFoundException("File with id %d not found".formatted(id));
        }
    }

    @Override
    public FileCreatedResponse create(CreateFileRequest request) {
        File newFile = File.builder()
                .title(request.title())
                .description(request.description())
                .creationDate(request.creationDate())
                .data(Base64.getDecoder().decode(request.base64Data()))
                .build();
        File createdFile = fileRepository.save(newFile);
        return new FileCreatedResponse("File was created", createdFile.getId());
    }

}
