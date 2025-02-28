package com.be.back_end.service.CloudinaryService;

import org.springframework.web.multipart.MultipartFile;

public interface ICloudinaryService {
    public String uploadFile(MultipartFile file);
}
