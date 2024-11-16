package com.poly;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.poly.entity.TaiKhoanEntity;
import com.poly.service.taiKhoanService;
@Service
public class loginggconifg {
	@Autowired
	private  taiKhoanService taikhoansevice;
	public TaiKhoanEntity registerOrLogin(String idTokenString) throws GeneralSecurityException, IOException {
	    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
	        .setAudience(Collections.singletonList("YOUR_GOOGLE_CLIENT_ID"))
	        .build();

	    GoogleIdToken idToken = verifier.verify(idTokenString);
	    if (idToken != null) {
	        GoogleIdToken.Payload payload = idToken.getPayload();
	        String email = payload.getEmail();
	        String name = (String) payload.get("name");

	        return taikhoansevice.registerOrLoginWithGoogle(email, name);
	    } else {
	        throw new RuntimeException("Token không hợp lệ");
	    }
	}
}
