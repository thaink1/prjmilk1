package com.example.demo.mapper;

import com.example.demo.dto.ImageResponse;
import com.example.demo.dto.ProductResponse;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Component
public class ProductMapper {
    public ProductResponse toProductResponse(Object[] row, List<Object[]> imageRows) {
        ProductResponse res = new ProductResponse();

        res.setProductId(((Number) row[0]).longValue());
        res.setProductName((String) row[1]);
        res.setCapacity(((Number) row[2]).intValue());
        res.setWeightGr(((Number) row[3]).intValue());
        res.setPrice(((Number) row[4]).floatValue());
        res.setStockQuantity(((Number) row[5]).intValue());
        res.setCreatedDate((Date) row[6]);
        res.setExpiredDate((Date) row[7]);
        res.setCategoryName((String) row[8]);
        res.setBrandName((String) row[9]);
        res.setDistributorName((String) row[10]);

        List<ImageResponse> images = imageRows.stream()
                .map(img -> new ImageResponse(
                        ((Number) img[0]).longValue(),
                        (String) img[1],
                        (Boolean) img[2],
                        img[3] != null ? ((Timestamp) img[3]).toLocalDateTime() : null
                ))
                .toList();

        res.setImages(images);
        return res;
    }
}
