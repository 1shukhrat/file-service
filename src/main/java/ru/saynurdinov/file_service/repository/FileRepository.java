package ru.saynurdinov.file_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.saynurdinov.file_service.entity.File;


@Repository
public interface FileRepository extends JpaRepository<File, Integer> {

    Page<File> findAll(Pageable pageable);
}
