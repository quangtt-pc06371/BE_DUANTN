package com.poly.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartStatusRequest {
	 private List<Integer> idDetail;
     private boolean newStatus = true;
}
