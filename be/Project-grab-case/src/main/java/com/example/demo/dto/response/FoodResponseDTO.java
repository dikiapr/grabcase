package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodResponseDTO {
    private Integer foodId;
    private Integer price;
    private String foodName;
    private String location;
    private String imageFilename;
}
