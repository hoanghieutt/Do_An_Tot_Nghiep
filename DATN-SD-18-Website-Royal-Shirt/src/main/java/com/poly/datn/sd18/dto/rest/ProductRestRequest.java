package com.poly.datn.sd18.dto.rest;

import com.poly.datn.sd18.entity.Color;
import com.poly.datn.sd18.entity.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ProductRestRequest {
    private List<Color> colors;
    private List<Size> sizes;
}
