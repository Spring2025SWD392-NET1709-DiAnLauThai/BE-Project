package com.be.back_end.service.CloudinaryService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class CloudinaryService implements ICloudinaryService{

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Cloudinary: " + e.getMessage(), e);
        }
    }

    @Override
    public String uploadZipFile(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "upload_preset", "unsigned_zip_upload",
                            "resource_type", "raw"
                    )
            );
            String originalUrl = uploadResult.get("secure_url").toString();
            return originalUrl + ".zip";
        } catch (IOException e) {
            throw new RuntimeException("File upload failed due to I/O error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during file upload: " + e.getMessage(), e);
        }
    }










}
