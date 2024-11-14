package com.poly.DtoEntity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter @Setter
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginDto {
	private String email;
	private String password;
}
