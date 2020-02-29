package ru.kershov.blogapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProfileDTO {
    public String photo;
    public boolean removePhoto;
    public String name;
    public String email;
    public String password;
}
