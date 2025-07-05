package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.UserFilterRequest;
import com.shose.shoseshop.service.ExcelExportService;
import com.shose.shoseshop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

@RestController
@RequestMapping("/api/v1/download")
@RequiredArgsConstructor
@Log4j2
public class ExcelExportController {

    public static final String ATTACHMENT = "attachment";
    private final ExcelExportService excelExportService;
    private final UserService userService;


    @PostMapping
    public ResponseEntity<byte[]> exportOtInsourceRequest(@PageableDefault Pageable pageable) {
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
//            request.setPageSize(DEFAULT_SIZE_EXPORT);
//            request.setPageNumber(DEFAULT_PAGE);

            excelExportService.exportUser(outStream, userService.getAll(pageable, new UserFilterRequest()).getContent());

            byte[] content = outStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData(ATTACHMENT, "User");
            headers.setContentLength(content.length);

            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

