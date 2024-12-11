package ru.hogwarts.school.service;

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
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarService {
    private final StudentService studentService;
    private final AvatarRepository avatarRepository;
    private final String pathToAvatarDir;


    public AvatarService(StudentService studentService, AvatarRepository avatarRepository, String pathToAvatarDir) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
        this.pathToAvatarDir = pathToAvatarDir;
    }

    private String getFiletype(String filename) {
        String res = filename.substring(filename.lastIndexOf(".") + 1);
        return res;
    }

    private Avatar getAvatar(Long studentID){
        return avatarRepository.findByStudentID(studentID).orElse(new Avatar());
    }

    private byte[] generateAvatar(Path path) throws IOException {
        try (InputStream inputStream = Files.newInputStream(path);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bufferedInputStream);
            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage avatar = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = avatar.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();
            ImageIO.write(avatar, getFiletype(path.getFileName().toString()), byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }

    public void uploadAvatar(Long studentID, MultipartFile file) throws IOException {
        Student student = studentService.getStudent(studentID);

        Path filePath = Path.of(pathToAvatarDir, studentID + "." +
                getFiletype(Objects.requireNonNull(file.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream inputStream = file.getInputStream();
            OutputStream outputStream = Files.newOutputStream(filePath, CREATE_NEW);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 1024)) {
            bufferedInputStream.transferTo(bufferedOutputStream);
        }
        Avatar avatar = getAvatar(studentID);
        avatar.setStudent(student);
        avatar.setId(studentID);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());

    }
}

