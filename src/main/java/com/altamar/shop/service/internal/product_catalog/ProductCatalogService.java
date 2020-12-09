package com.altamar.shop.service.internal.product_catalog;


import com.altamar.shop.models.dto.product_catalog.ProductDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductCatalogService {

    ProductDTO getProduct(Long productId);

    ProductDTO addProduct(ProductDTO productDTO);

    ProductDTO uploadFile(MultipartFile multipartFile, Long id);

    ProductDTO updateProduct(ProductDTO productDTO);

    ProductDTO updateProductPhoto(MultipartFile multipartFile, Long id);

    List<ProductDTO> getAllProducts();

    Resource getProductImage(String filename);

    void deleteProduct(Long id);

}
