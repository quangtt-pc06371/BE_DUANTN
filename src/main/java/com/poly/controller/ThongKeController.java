package com.poly.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.repository.CTDonHangRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/thong-ke")
public class ThongKeController {

    @Autowired
    private CTDonHangRepository thongKeRepository;

    @GetMapping
    public ResponseEntity<Double> thongKeTuChon(
            @RequestParam Integer shopId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        Double total = thongKeRepository.thongKeTuChon(shopId, startDate, endDate);
        return ResponseEntity.ok(total);
    }
    @GetMapping("/nam")
    public ResponseEntity<Double> thongKeTheoNam(
        @RequestParam("shopId") Integer shopId,
        @RequestParam("year") Integer year
    ) {
        Double result = thongKeRepository.thongKeTheoNam(shopId, year);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/nam-thang")
    public ResponseEntity<Double> thongKeTheoNamVaThang(
        @RequestParam("shopId") Integer shopId,
        @RequestParam("year2") Integer year2,
        @RequestParam("month") Integer month
    ) {
        Double result = thongKeRepository.thongKeTheoNamVaThang(shopId, year2, month);
        return ResponseEntity.ok(result);
    }
}
