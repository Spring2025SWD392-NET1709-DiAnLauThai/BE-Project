package com.be.back_end.service.CloudinaryService;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ICloudinaryService {
     String uploadFile(MultipartFile file);
     String uploadZipFile(MultipartFile file);
}
