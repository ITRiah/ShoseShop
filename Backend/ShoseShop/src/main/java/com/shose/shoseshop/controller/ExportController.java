package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.response.ProductStatisticResponse;
import com.shose.shoseshop.controller.response.StatisticResponse;
import com.shose.shoseshop.service.OrderService;
import com.shose.shoseshop.service.StatisticExportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/exports")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ExportController {
    OrderService orderService;
    StatisticExportService exportService;

    @GetMapping("/statistics")
    public ResponseEntity<byte[]> exportStatisticsToExcel(@RequestParam Long year) throws IOException {
        List<StatisticResponse> statistics = orderService.statistic(year);

        byte[] excelData = exportService.exportStatisticsToExcel(statistics);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "ThongKeDoanhThu" + year + ".xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }

    @GetMapping("/product-sales")
    public ResponseEntity<byte[]> exportStatisticsProductToExcel(@RequestParam Long year,
                                                                 @RequestParam Long month) throws IOException {
        List<ProductStatisticResponse> statistics = orderService.findProductSalesStatistic(month, year);
        byte[] excelData = exportService.exportStatisticsProductToExcel(statistics);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "ThongKeDoanhSoSanPham" + month + year + ".xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }
}
