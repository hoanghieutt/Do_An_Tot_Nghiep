package com.poly.datn.sd18.service;

import com.poly.datn.sd18.dto.request.ImageRequest;
import com.poly.datn.sd18.entity.Image;

import java.util.List;

public interface ImageService {
    List<Image> getALlByProductId(Integer id);

    Image add(ImageRequest imageRequest);

    String deleteAllByProductId(Integer productId);
}
