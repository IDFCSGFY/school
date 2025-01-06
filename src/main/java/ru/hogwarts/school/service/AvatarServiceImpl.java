package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarServiceImpl implements AvatarService {

    @Value("${avatar.img.dir.path}")
    private String avatarsDir;

    private final StudentServiceImpl studentService;
    private final AvatarRepository repository;

    public AvatarServiceImpl(StudentServiceImpl studentService, AvatarRepository repository) {
        this.studentService = studentService;
        this.repository = repository;
    }

    @Override
    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {

        Student student = studentService.findById(studentId);
        Path path = Path.of(avatarsDir, studentId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(path.getParent());
        Files.deleteIfExists(path);
        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(path, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
            bis.transferTo(bos);
        }

        Avatar avatar = repository.findAvatarByStudentId(studentId).orElse(new Avatar());
        avatar.setFilePath(path.toString());
        avatar.setMediaType(file.getContentType());
        avatar.setFileSize(file.getSize());
        avatar.setData(generateFormattedAvatar(file));
        avatar.setStudent(student);

        repository.save(avatar);
    }

    @Override
    public ArrayList<Avatar> findAll(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return new ArrayList<>(repository.findAll(pageRequest).getContent());
    }

    private byte[] generateFormattedAvatar(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is, 1024);
        ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            BufferedImage origImg = ImageIO.read(bis);
            BufferedImage formattedImg = new BufferedImage(200, 200, origImg.getType());
            Graphics2D graphics = formattedImg.createGraphics();
            graphics.drawImage(origImg,0,0, 200, 200, null);
            graphics.dispose();

            ImageIO.write(formattedImg, getExtension(file.getOriginalFilename()), baos);

            return baos.toByteArray();
        }
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.indexOf(".") + 1);
    }

    public Avatar findAvatarByStudentId(long studentId) {
        return repository.findAvatarByStudentId(studentId).orElse(null);
    }
}
