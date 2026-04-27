package com.printing_shop.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Upload image to Cloudinary and return secure URL
     */
    public String uploadImage(MultipartFile file, String folderName) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        Map<String, Object> options = ObjectUtils.asMap(
            "folder", folderName != null ? folderName : "printing/banner",  // default folder
            "resource_type", "auto"   // supports image, raw, video etc.
        );

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

        return (String) uploadResult.get("secure_url");   // This is the URL you save in DB
    }

    /**
     * Optional: Delete image from Cloudinary (using public_id)
     */
    public void deleteImage(String publicId) throws Exception {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}