//package com.poly;
//
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Service;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//
//import jakarta.annotation.PostConstruct;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//
//@Configuration
//public class FirebaseConfig {
//
//	
//	@PostConstruct
//	public void initialize() {
//	    try {
//	        FileInputStream serviceAccount = new FileInputStream("src/main/resources/duantotnghiep-940ce-firebase-adminsdk.json");
//
//	        FirebaseOptions options = new FirebaseOptions.Builder()
//	            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//	            .setStorageBucket("duantotnghiep-940ce.appspot.com") // Thay thế bằng tên bucket của bạn
//	            .build();
//
//	        if (FirebaseApp.getApps().isEmpty()) {
//	            FirebaseApp.initializeApp(options);
//	            System.out.println("Firebase app initialized successfully.");
//	        }
//
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	        System.out.println("Failed to initialize Firebase.");
//	    }
//	}
//	
//	
////  
////	  @Bean
////	    public FirebaseApp initializeFirebase() throws IOException {
////	        FileInputStream serviceAccount = new FileInputStream("src/main/resources/duantotnghiep-940ce-firebase-adminsdk.json");
////
////	        FirebaseOptions options = new FirebaseOptions.Builder()
////	            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
////	            .setStorageBucket("duantotnghiep-940ce.appspot.com")  // Thay bằng ID bucket của bạn
////	            .build();
////
////	        return FirebaseApp.initializeApp(options);
////	    }
//}
