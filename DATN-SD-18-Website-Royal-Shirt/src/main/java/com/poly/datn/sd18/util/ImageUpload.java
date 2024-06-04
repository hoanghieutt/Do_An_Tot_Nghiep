package com.poly.datn.sd18.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUpload {
    private final String UPLOAD_FOLDER =
            "C:\\Users\\truon\\Documents\\DATN-SD-18-Website-Royal-Shirt\\DATN-SD-18-Website-Royal-Shirt\\src\\main\\resources\\static\\images";

    public boolean uploadImage(MultipartFile multipartFile) {
        boolean isUpload = false;
        try {
            Files.copy(multipartFile.getInputStream(),
                    Paths.get(UPLOAD_FOLDER + File.separator,
                            multipartFile.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING
            );
            isUpload = true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return isUpload;
    }

    public boolean checkExisted(MultipartFile uploadFile) {
        boolean isExisted = false;
        try {
            File file = new File(UPLOAD_FOLDER + "\\" + uploadFile.getOriginalFilename());
            isExisted = file.exists();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return isExisted;
    }
}