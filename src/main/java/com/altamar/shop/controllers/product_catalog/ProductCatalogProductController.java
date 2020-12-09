package com.altamar.shop.controllers.product_catalog;

import com.altamar.shop.models.dto.Response;
import com.altamar.shop.models.dto.product_catalog.ProductDTO;
import com.altamar.shop.service.internal.product_catalog.ProductCatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/api/v2/product-catalog/product")
public class ProductCatalogProductController {

    private final ProductCatalogService productCatalogService;

    public ProductCatalogProductController(ProductCatalogService productCatalogService) {
        this.productCatalogService = productCatalogService;
    }

    /**
     * @return List of ProductDto
     */
    @GetMapping
    public ResponseEntity<Response<List<ProductDTO>>> getAllProduct() {
        log.info("[ProductCatalogController] : Get all Products");
        return ok(Response.ok(productCatalogService.getAllProducts()));
    }

    /**
     * @param productId (Product)
     * @return ProductDto by product productId
     */
    @GetMapping("/{productId}")
    public ResponseEntity<Response<ProductDTO>> getProduct(@PathVariable("productId") Long productId) {
        log.info("[ProductCatalogController] : Getting product by productId {}", productId);
        final ProductDTO result = productCatalogService.getProduct(productId);
        log.info("[ProductCatalogController] : Got product by productId {}", productId);
        return ok(Response.ok(result));
    }

    /**
     * @param imageCode (new)
     * @return Product image in resource by imageCode
     */
    @GetMapping("/image/{imageCode}")
    public ResponseEntity<Resource> getProductImage(@PathVariable(value = "imageCode") String imageCode) {
        log.info("[ProductCatalogController] : Getting product image in resource by image code {}", imageCode);
        final Resource resource = productCatalogService.getProductImage(imageCode);
        log.info("[ProductCatalogController] : Got product image in resource by image code {}", imageCode);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    /**
     * @param productDTO (new)
     * @return status ok after adding new ProductDto to data base
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Response<ProductDTO>> addProduct(@RequestBody ProductDTO productDTO) {
        log.info("[ProductCatalogController] : Adding new product [{}]", productDTO.toString());
        final ProductDTO result = productCatalogService.addProduct(productDTO);
        log.info("[ProductCatalogController] : Product with id {} added", result.getId());
        return ok(Response.ok(result));
    }

    /**
     * @param productId     (Product)
     * @param multipartFile (new)
     * @return status ok after uploading and setting new image to Product by productId
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{productId}/image")
    public ResponseEntity<Response<ProductDTO>> uploadImage(@RequestParam("file") MultipartFile multipartFile,
                                                            @PathVariable("productId") Long productId) {
        log.info("[ProductCatalogController] : Uploading new image to product by productId {}", productId);
        final ProductDTO result = productCatalogService.uploadFile(multipartFile, productId);
        log.info("[ProductCatalogController] : Uploaded new image to product by productId {}", productId);
        return ok(Response.ok(result));
    }

    /**
     * @param productId (Product)
     * @return status ok after deleting from database products by array of id
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<Response<?>> delete(@RequestParam(name = "productId") Long productId) {
        log.info("[ProductCatalogController] : Deleting product by productId {}", productId);
        productCatalogService.deleteProduct(productId);
        log.info("[ProductCatalogController] : Product by productId was deleted {}", productId);
        return ok(Response.ok("OK"));
    }

    /**
     * @param productDTO (new)
     * @return updated ProductDto
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<Response<ProductDTO>> update(@RequestBody ProductDTO productDTO) {
        log.info("[ProductCatalogController] : Updating product [{}]", productDTO.toString());
        final ProductDTO result = productCatalogService.updateProduct(productDTO);
        log.info("[ProductCatalogController] : Product with id {} updated", result.getId());
        return ok(Response.ok(result));
    }

    /**
     * @param multipartFile (new)
     * @param productId            (Product)
     * @return status ok after uploading and updating image for Product by productId
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{productId}/image")
    public ResponseEntity<Response<ProductDTO>> updateImage(@RequestParam("file") MultipartFile multipartFile,
                                                            @PathVariable("productId") Long productId) {
        log.info("[ProductCatalogController] : Updating product image by productId {}", productId);
        final ProductDTO result = productCatalogService.updateProductPhoto(multipartFile, productId);
        log.info("[ProductCatalogController] : Product image with productId {} updated, new image {}", result.getId(), result.getImg());
        return ok(Response.ok(result));
    }
}
