package com.altamar.shop.service.internal.product_catalog;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String saveFile(MultipartFile multipartFile);

    Resource getFileInResource(String filename);

    void deleteFile(String filename);

}
