package com.be.back_end.service.CloudinaryService;

import com.cloudinary.ArchiveParams;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

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
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "zip_folder",   // Store in "zip_folder"
                            "resource_type", "raw",   // Ensure ZIP file compatibility
                            "format", "zip"           // Force ZIP extension in URL
                    )
            );
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload ZIP file to Cloudinary", e);
        }
    }



}
