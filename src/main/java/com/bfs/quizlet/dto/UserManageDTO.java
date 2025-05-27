package com.bfs.quizlet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserManageDTO {
    private Long userId;
    private String email;
    private String firstname;
    private String lastname;
    private Boolean isActive;
    private Boolean isAdmin;
}
