package com.indhawk.billable.billablews.controllers;

import com.indhawk.billable.billablews.auth.HasRoles;
import com.indhawk.billable.billablews.services.FileService;
import com.indhawk.billable.billablews.utils.SecurityContextHelper;
import com.indhawk.billable.models.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/files")
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload/file")
    //@HasRoles(roles={"SUPER_USER", "ADMIN_USER"})
    public boolean uploadFile(@RequestParam MultipartFile file) {
        return fileService.uploadFile(file, SecurityContextHelper.getCurrentUserOrgId());
    }

    @GetMapping
    public File getFile() {
        return fileService.getFileByOrgId(SecurityContextHelper.getCurrentUserOrgId());
    }



}
