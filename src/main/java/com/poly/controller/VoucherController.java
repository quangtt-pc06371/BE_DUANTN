package com.poly.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.DtoEntity.VoucherDTO;
import com.poly.entity.VoucherEntity;
import com.poly.service.JwtSevice2;
import com.poly.service.VoucherBillService;

import io.jsonwebtoken.Claims;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/voucherbills")

public class VoucherController {

    @Autowired
    private VoucherBillService voucherBillService;

    @Autowired
    private JwtSevice2 jwtService;

    private boolean isShop(String token) {
        Claims claims = jwtService.parseToken(token);
        if (claims == null) {
            throw new SecurityException("Token không hợp lệ!");
        }
        List<String> roles = jwtService.getRolesFromToken(token);
        return roles != null && roles.contains("Shop");
    }

    @PostMapping("/add")
    public VoucherEntity addVoucherEntity(@RequestHeader("Authorization") String token,
                                          @RequestBody VoucherEntity voucherEntity) {
        if (!isShop(token.replace("Bearer ", ""))) {
            throw new SecurityException("Bạn không có quyền thêm Voucher!");
        }

        if (voucherEntity.getNgaybatdau().after(voucherEntity.getNgayHetHan())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày hết hạn.");
        }

        return voucherBillService.addVoucherEntity(voucherEntity);
    }

    @PutMapping("/{id}")
    public VoucherEntity updateVoucherEntity(@RequestHeader("Authorization") String token,
                                             @PathVariable int id,
                                             @RequestBody VoucherEntity voucherEntity) {
        if (!isShop(token.replace("Bearer ", ""))) {
            throw new SecurityException("Bạn không có quyền sửa Voucher!");
        }

        if (voucherEntity.getNgaybatdau().after(voucherEntity.getNgayHetHan())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày hết hạn.");
        }

        return voucherBillService.updateVoucherEntity(id, voucherEntity);
    }

    @PutMapping("/{id}/update-time")
    public VoucherEntity updateVoucherTime(@RequestHeader("Authorization") String token,
                                           @PathVariable int id,
                                           @RequestBody VoucherDTO updateTimeRequest) {
        if (!isShop(token.replace("Bearer ", ""))) {
            throw new SecurityException("Bạn không có quyền cập nhật thời gian của Voucher!");
        }

        if (updateTimeRequest.getNgaybatdau().after(updateTimeRequest.getNgayHetHan())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày hết hạn.");
        }

        VoucherEntity existingVoucher = voucherBillService.getVoucherById(id);
        if (existingVoucher == null) {
            throw new IllegalArgumentException("Voucher không tồn tại.");
        }

        existingVoucher.setNgaybatdau(updateTimeRequest.getNgaybatdau());
        existingVoucher.setNgayHetHan(updateTimeRequest.getNgayHetHan());

        return voucherBillService.updateVoucherEntity(id, existingVoucher);
    }

    @DeleteMapping("/{id}")
    public void deleteVoucherBill(@RequestHeader("Authorization") String token, @PathVariable int id) {
        if (!isShop(token.replace("Bearer ", ""))) {
            throw new SecurityException("Bạn không có quyền xoá Voucher!");
        }

        voucherBillService.deleteVoucherBill(id);
    }

    @GetMapping
    public List<VoucherEntity> getAllVoucherEntity(@RequestHeader("Authorization") String token) {
        if (!isShop(token.replace("Bearer ", ""))) {
            throw new SecurityException("Bạn không có quyền truy cập danh sách Voucher!");
        }

        return voucherBillService.getAllVoucherBills();
    }
}

