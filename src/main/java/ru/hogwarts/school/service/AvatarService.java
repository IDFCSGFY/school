package ru.hogwarts.school.service;

import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;

import java.io.IOException;
import java.util.ArrayList;

public interface AvatarService {

    void uploadAvatar(Long studentId, MultipartFile file) throws IOException;

    ArrayList<Avatar> findAll(int pageNumber, int pageSize);
}
