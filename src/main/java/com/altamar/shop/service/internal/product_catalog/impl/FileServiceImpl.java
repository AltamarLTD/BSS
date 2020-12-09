package com.altamar.shop.service.internal.product_catalog.impl;

import com.altamar.shop.models.exсeptions.DeleteFileException;
import com.altamar.shop.models.exсeptions.FileUploadException;
import com.altamar.shop.models.exсeptions.InvalidFormatException;
import com.altamar.shop.models.exсeptions.NotFoundException;
import com.altamar.shop.service.internal.product_catalog.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Value("${file.image.storage}")
    private String root;

    public FileServiceImpl() {

    }

    @PostConstruct
    public void init() {
        log.info("[WebConfiguration] : FileServiceImpl have been initialized with param {}", root);
    }

    /**
     * This method check if filename of input multipart file is empty, throw InvalidFormatException
     * If fileType not jpg or jpeg, throw InvalidFormatException
     * Generate new filename though UUID
     * Save input file to file storage
     * If during saving catch IOException, throw FileUploadException
     *
     * @param multipartFile new image
     * @return generated Filename
     */
    @Override
    public String saveFile(MultipartFile multipartFile) {
        final String filename = multipartFile.getOriginalFilename();
        if (filename == null || filename.length() == 0) {
            throw new InvalidFormatException("Empty filename.");
        }
        log.info("[FileServiceImpl] : Filename is not empty");

        final String originalFilename = StringUtils.cleanPath(filename);
        final String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (!(fileType.equals(".jpg") || fileType.equals(".jpeg"))) {
            throw new InvalidFormatException("Invalid format. File should be in jpg or jpeg.");
        }
        log.info("[FileServiceImpl] : File type is valid");

        final String generatedFilename = UUID.randomUUID() + fileType;

        final String absolutePath = root + File.separator + generatedFilename;

        try (FileOutputStream fileOutputStream = new FileOutputStream(absolutePath)) {
            fileOutputStream.write(multipartFile.getBytes());
            log.info("[FileServiceImpl] : File was wrote to the folder by path {}, with new generated filename {}", absolutePath, generatedFilename);
        } catch (IOException e) {
            throw new FileUploadException(e.getMessage());
        }
        return generatedFilename;
    }

    /**
     * This method check if filename is empty, throw InvalidFormatException
     * Get file by path
     *
     * @param filename name of image
     * @return founded file in resource
     * if file was not found throw NotFoundException
     */
    @Override
    public Resource getFileInResource(String filename) {
        if (filename.length() == 0) {
            throw new InvalidFormatException("Empty name of file");
        }
        log.info("[FileServiceImpl] : Filename is not empty");
        final String absolutePath = root + File.separator + filename;
        final Resource resource = new FileSystemResource(absolutePath);
        if (!resource.exists()) {
            throw new NotFoundException(String.format("File by %s was not found", filename));
        } else {
            log.info("[FileServiceImpl] : File by path {} exists", absolutePath);
            return resource;
        }
    }

    /**
     * This method check if filename is empty, throw InvalidFormatException
     * Get file by path
     * Delete file by path from file storage
     *
     * @param filename name of image
     */
    @Override
    public void deleteFile(String filename) {
        if (filename.length() == 0) {
            throw new InvalidFormatException("Empty name of file");
        }
        log.info("[FileServiceImpl] : Filename is not empty");
        final String absolutePath = root + File.separator + filename;
        final Path filePath = Paths.get(absolutePath);
        final boolean delete = filePath.toFile().delete();
        log.info("[FileServiceImpl] : File by path {} was successfully deleted", absolutePath);
        if (!delete) {
            throw new DeleteFileException("File was not deleted.");
        }
    }

}
