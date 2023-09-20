package com.indhawk.billable.billablews.services;

import com.indhawk.billable.billablews.dao.FileDao;
import com.indhawk.billable.models.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class FileService {

    @Autowired
    private FileDao fileDao;

    public boolean uploadFile(MultipartFile file, int orgId) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            File f = File.builder().fileName(fileName)
                    .fileType(file.getContentType())
                    .bytes(file.getBytes())
                    .orgId(orgId)
                    .build();
            return fileDao.saveFile(f);
        } catch (IOException e) {
            log.error("Failed to upload file", e);
        }
        return false;
    }

    public File getFileByOrgId(int orgId) {
        return fileDao.getFileByOrgId(orgId);
    }
}
