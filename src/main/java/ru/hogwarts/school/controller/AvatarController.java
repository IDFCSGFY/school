package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarServiceImpl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class AvatarController {

    private final AvatarServiceImpl avatarService;

    public AvatarController(AvatarServiceImpl avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        avatarService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}/avatar/formated")
    public ResponseEntity<byte[]> downloadFormatedAvatar(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatarByStudentId(id);
        if (avatar == null) {
            System.out.println("аватар нулл");
        } else {
            System.out.println("аватар не нулл");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getFileSize());

        System.out.println("дошел до ретерна");
        return ResponseEntity.status(200).headers(headers).body(avatar.getData());
    }

    @GetMapping("{id}/avatar")
    public void downloadOriginalAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatarByStudentId(id);
        Path path = Path.of(avatar.getFilePath());

        try(InputStream is = Files.newInputStream(path);
            OutputStream os = response.getOutputStream();
            BufferedInputStream bis = new BufferedInputStream(is, 1024);
            BufferedOutputStream bos = new BufferedOutputStream(os, 1024)){

            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            bis.transferTo(bos);
        }
    }
}
