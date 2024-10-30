package com.poly.service;

import com.google.firebase.cloud.StorageClient;
import com.google.cloud.storage.Blob;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.google.cloud.storage.BlobInfo;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

@Service
public class FirebaseService {


public String uploadFile(MultipartFile file) throws IOException {
    // Tạo tên file ngẫu nhiên để tránh trùng lặp
    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

    // Tải file lên Firebase Storage
    Blob blob = StorageClient.getInstance().bucket().create(fileName, file.getInputStream(), file.getContentType());

    // Lấy metadata từ file vừa tải lên
//    BlobInfo blobInfo = blob.getStorage().get(blob.getBlobId());
//    String downloadToken = blobInfo.getMetadata().get("firebaseStorageDownloadTokens");

    // Trả về URL truy cập ảnh
    return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                          StorageClient.getInstance().bucket().getName(), URLEncoder.encode(fileName, "UTF-8")  );
}
//    public String uploadFile(MultipartFile file) throws IOException {
//        // Tạo tên file ngẫu nhiên để tránh trùng lặp
//        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//
//        // Tải file lên Firebase Storage
//        Blob blob = StorageClient.getInstance().bucket().create(fileName, file.getInputStream(), file.getContentType());
//
//        // Trả về URL truy cập ảnh
////        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
////                StorageClient.getInstance().bucket().getName(), fileName);
////        return String.format("https://firebasestorage.googleapis.com/v0/b/duantotnghiep-940ce.appspot.com/o/%s?alt=media",
////        		      StorageClient.getInstance().bucket().getName(), fileName);
////    }
//        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
//    		      StorageClient.getInstance().bucket().getName(), URLEncoder.encode(fileName, "UTF-8"));
//    }
}

