package com.altamar.shop.service.internal.product_catalog.impl;

import com.altamar.shop.entity.product_catalog.Product;
import com.altamar.shop.models.dto.product_catalog.ProductDTO;
import com.altamar.shop.models.exсeptions.NotFoundException;
import com.altamar.shop.repository.product_catalog.ProductRepository;
import com.altamar.shop.service.internal.product_catalog.FileService;
import com.altamar.shop.service.internal.product_catalog.ProductCatalogService;
import com.altamar.shop.utils.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductCatalogServiceImpl implements ProductCatalogService {

    private final ProductRepository productRepository;
    private final FileService fileService;

    public ProductCatalogServiceImpl(ProductRepository productRepository,
                                     FileService fileService) {
        this.productRepository = productRepository;
        this.fileService = fileService;
    }

    @PostConstruct
    public void init() {
        log.info("[WebConfiguration] : ProductCatalogServiceImpl have been initialized");
    }

    /**
     * @return all List of ProductDto from database
     */
    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .filter(Objects::nonNull)
                .map(Product::toDto)
                .collect(Collectors.toList());
    }

    /**
     * This method validate input product id
     * Find Product entity in database
     * If product was not found in database, throw NotFoundException
     *
     * @param productId product
     * @return productDto
     */
    @Override
    public ProductDTO getProduct(Long productId) {
        Validator.validateIds(productId);
        final Optional<Product> product = productRepository.findById(productId);
        log.info("[ProductCatalogServiceImpl] : Product with id {} was found successfully", productId);
        if (!product.isPresent()) {
            throw new NotFoundException(String.format("Product by id %s not found", productId));
        }
        return product.get().toDto();
    }

    /**
     * @param filename image name
     * @return product image in resource by fileCode
     */
    @Override
    public Resource getProductImage(String filename) {
        return fileService.getFileInResource(filename);
    }

    /**
     * This method validate input entity and save to database.
     *
     * @param productDTO (name, country, pack, date, priceKg, pricePack, img, description)
     * @return saved new ProductDto to database
     */
    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        Validator.dtoValidator(productDTO);
        log.info("[ProductCatalogServiceImpl] : Product is valid");
        final Product save = productRepository.save(productDTO.toEntity());
        log.info("[ProductCatalogServiceImpl] : Product saved");
        return save.toDto();
    }

    /**
     * This method validate input product id
     * Find Product entity in database and set new image through file service
     * If product was not found in database, throw NotFoundException
     *
     * @param multipartFile input file in jpg or jpeg format
     * @param productId     id of product
     * @return saved updated Product to database
     */
    @Transactional
    @Override
    public ProductDTO uploadFile(MultipartFile multipartFile, Long productId) {
        Validator.validateIds(productId);
        final Optional<Product> product = productRepository.findById(productId);
        log.info("[ProductCatalogServiceImpl] : Product was found by id {}", productId);
        if (!product.isPresent()) {
            throw new NotFoundException(String.format("Product by id %s not found", productId));
        }
        product.get().setImgCode(fileService.saveFile(multipartFile));
        log.info("[AdminServiceImpl] : To Product with id {} was installed new image", productId);
        final ProductDTO savedProduct = productRepository.save(product.get()).toDto();
        log.info("[AdminServiceImpl] : Product saved");
        return savedProduct;
    }

    /**
     * This method validate input product array of id
     * Delete product entities by id from argument
     *
     * @param id product ids
     */
    @Transactional
    @Override
    public void deleteProduct(Long id) {
        Validator.validateIds(id);
        if (!productRepository.existsById(id)) {
            throw new NotFoundException(String.format("Product by %s id was not found", id));
        }
        productRepository.deleteById(id);
        log.info("[ProductCatalogServiceImpl] : Product with id {} was deleted", id);
    }

    /**
     * This method validate input ProductDto
     * If product not exist in database, throw NotFoundException
     *
     * @param productDTO (name, country, pack, date, priceKg, pricePack, img, description)
     * @return saved and updated productDto
     */
    @Transactional
    @Override
    public ProductDTO updateProduct(ProductDTO productDTO) {
        Validator.dtoValidator(productDTO);
        log.info("[ProductCatalogServiceImpl] : Product is valid");
        if (!productRepository.existsById(productDTO.getId())) {
            throw new NotFoundException(String.format("Product by id %s not found", productDTO.getId()));
        }
        log.info("[ProductCatalogServiceImpl] : Product by id {} exist", productDTO.getId());
        final Product product = productDTO.toEntity();
        final ProductDTO savedProduct = productRepository.save(product).toDto();
        log.info("[ProductCatalogServiceImpl] : Product saved");
        return savedProduct;
    }

    /**
     * This method validate input product id
     * Find Product entity in database and set new image through file service
     * If product was not found in database, throw NotFoundException
     * Delete previous deprecated image
     *
     * @param multipartFile new image
     * @param id            product id
     * @return saved updated ProductDto to database
     */
    @Transactional
    @Override
    public ProductDTO updateProductPhoto(MultipartFile multipartFile, Long id) {
        Validator.validateIds(id);
        final Optional<Product> productOptional = productRepository.findById(id);
        log.info("[ProductCatalogServiceImpl] : Product was found by id {}", id);
        if (!productOptional.isPresent()) {
            throw new NotFoundException(String.format("Product by id %s not found", id));
        }
        final Product product = productOptional.get();
        fileService.deleteFile(product.getImgCode());
        log.info("[ProductCatalogServiceImpl] : Old image was deleted");
        product.setImgCode(fileService.saveFile(multipartFile));
        log.info("[ProductCatalogServiceImpl] : To Product with id {} was installed new image", id);
        final ProductDTO savedProduct = productRepository.save(product).toDto();
        log.info("[ProductCatalogServiceImpl] : Product saved");
        return savedProduct;
    }

}
