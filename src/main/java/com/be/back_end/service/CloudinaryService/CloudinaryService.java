package com.be.back_end.service.CloudinaryService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

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

    /*@Override
    public String uploadZipFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Invalid file: File is empty or null.");
        }

        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getInputStream(),
                    ObjectUtils.asMap(
                            "resource_type", "raw",
                            "type", "authenticated"
                    )
            );

            String publicId = Objects.toString(uploadResult.get("public_id"), "");

            return cloudinary.signUrl(
                    publicId,
                    ObjectUtils.asMap(
                            "resource_type", "raw",
                            "type", "authenticated",
                            "expire_at", System.currentTimeMillis() / 1000 + 3600 // 1-hour expiry
                    )
            );

        } catch (IOException e) {
            throw new RuntimeException("File upload failed due to I/O error: " + e.getMessage(), e);
        }
    }*/










}
