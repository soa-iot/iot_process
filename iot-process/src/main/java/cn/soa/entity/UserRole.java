package cn.soa.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Validated
public class UserRole implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String orgID;
	private String username;
	private String userID;
	private List<Role> roles;
	
}
