package com.poly.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "NGUOIDUNG")
public class NguoiDungEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NGUOIDUNG")
    private int id;

    @Column(name = "MATKHAU", nullable = false)
    private String matKhau;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "HOTEN", nullable = false)
    private String hoTen;

    @Column(name = "SDT", nullable = false)
    private String sdt;

    @Column(name = "DIACHI", nullable = false)
    private String diaChi; // Chuyển đổi kiểu int sang String để lưu địa chỉ.

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "VAITRO", referencedColumnName = "ID_VAITRO", nullable = true)
//    private VaitroEntity vaiTro; // Tham chiếu đến vai trò

    @Column(name = "ANH")
    private String anh; // Đường dẫn tới hình ảnh người dùng

    @Column(name = "CMND")
    private String cmnd; // Số CMND

    @OneToMany(mappedBy = "nguoiDung", fetch = FetchType.LAZY)
    private List<ShopEntity> shops; // Danh sách các shop của người dùng

    // Constructor
    public NguoiDungEntity() {
        // Khởi tạo nếu cần thiết
    }
}
