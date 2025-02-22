package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exceptions.NotFoundPage;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    private final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Value("${path.to.avatars.folder}")
    private String avatarDir;

    private final StudentService studentService;
    private final AvatarRepository avatarRepository;

    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    public void uploadAvatar(Long StudentId, MultipartFile file) throws IOException {
        logger.info("Uploading avatar");
        Student student = studentService.getStudent(StudentId);

        Path filePath = Path.of(avatarDir, StudentId + "." +
                getExtension(Objects.requireNonNull(file.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream inputStream = file.getInputStream();
             OutputStream outputStream = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 1024)) {
            bufferedInputStream.transferTo(bufferedOutputStream);
        }

        Avatar avatar = findAvatar(StudentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(generateAvatar(filePath));
        avatarRepository.save(avatar);

    }

    public File getAvatarFromDirectory(Long StudentId){
        logger.info("get avatar from directory");
        File folder = new File(avatarDir);
        File[] files = folder.listFiles();
        assert files != null;
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile() &&
                    fileName.substring(0, file.getName().
                            lastIndexOf(".")).equals(StudentId.toString())) {
                return file;
            }
        }
        return null;
    }

    public Avatar findAvatar(Long StudentID){
        return avatarRepository.findByStudent_Id(StudentID).orElse(new Avatar());
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
            ImageIO.write(avatar, getExtension(path.getFileName().toString()), byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public List<Avatar> getAvatar(int page, int size) {
        logger.info("get avatar");
        if (page < 1 || size < 1) {
            throw new NotFoundPage();
        }
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}