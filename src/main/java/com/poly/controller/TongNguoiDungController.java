package com.poly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.service.TongNguoiDungService;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/tongnguoidung")
public class TongNguoiDungController {

    @Autowired
    private TongNguoiDungService tongNguoiDungService;

    @GetMapping("/count")
    public ResponseEntity<Long> countUsers() {
        long totalUsers = tongNguoiDungService.countUsers();
        return ResponseEntity.ok(totalUsers);
    }
}

