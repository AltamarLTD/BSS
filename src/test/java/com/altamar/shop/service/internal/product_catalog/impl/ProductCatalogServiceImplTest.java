package com.altamar.shop.service.internal.product_catalog.impl;

import com.altamar.shop.entity.product_catalog.Product;
import com.altamar.shop.models.dto.product_catalog.ProductDTO;
import com.altamar.shop.models.exсeptions.NotFoundException;
import com.altamar.shop.repository.product_catalog.ProductRepository;
import com.altamar.shop.service.internal.product_catalog.impl.FileServiceImpl;
import com.altamar.shop.service.internal.product_catalog.impl.ProductCatalogServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductCatalogServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductCatalogServiceImpl productCatalogService;

    @InjectMocks
    private FileServiceImpl fileService;

    @Before
    public void init(){
        productCatalogService = new ProductCatalogServiceImpl(productRepository, fileService);
    }

    @Test
    public void getAllProductsTest() {
        // given
        when(productRepository.findAll())
                .thenReturn(Arrays.asList(
                        Product.builder()
                                .name("Product1")
                                .country("Ukraine")
                                .date("24/05/2002")
                                .pack("45kg")
                                .priceKg(12.01)
                                .pricePack(540.0)
                                .imgCode("111")
                                .description("11111111111111")
                                .id(1L)
                                .build())
                );

        // when
        final List<ProductDTO> allProducts = productCatalogService.getAllProducts();

        // expected
        assertThat(allProducts)
                .contains(
                        ProductDTO.of(
                                1L,
                                "Product1",
                                "Ukraine",
                                "45kg",
                                "24/05/2002",
                                12.01,
                                540.0,
                                "111",
                                "11111111111111")

                );
    }

    @Test
    public void getCurrentProduct_inputNegativeValueOfId() {
        // given
        final Long id = -1L;

        // expected
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> productCatalogService.getProduct(id))
                .withMessage("Negative value of id");
    }

    @Test
    public void getCurrentProduct_returnNullProductFromDataBase() {
        // given
        final Long id = 0L;

        // expected
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> productCatalogService.getProduct(id))
                .withMessage(String.format("Product by id %s not found", id));
    }

    @Test
    public void getCurrentProduct_returnProductDto() {
        // given
        final Product givenProduct = Product.builder()
                .name("Product1")
                .country("Ukraine")
                .date("24/05/2002")
                .pack("45kg")
                .priceKg(12.01)
                .pricePack(540.0)
                .imgCode("111")
                .id(1L)
                .build();

        final Optional<Product> product = Optional.of(givenProduct);
        when(productRepository.findById(1L)).thenReturn(product);

        // when
        final ProductDTO expectedProduct = productCatalogService.getProduct(1L);

        // expected
        assertThat(expectedProduct).isEqualTo(givenProduct.toDto());
    }

    @Test
    public void addProduct_returnValidationExceptionDueToNotValidInputDto() {
        // given
        final ProductDTO productDTO = ProductDTO.of(
                1L,
                "k",
                "Amerika",
                "20kg",
                "24/06/2020",
                21.5,
                500.0,
                "123",
                "1111111111111");


        // expected
        assertThatExceptionOfType(javax.validation.ValidationException.class)
                .isThrownBy(() -> productCatalogService.addProduct(productDTO))
                .withMessage("'name' size must be from 2 to 50 chars");
    }

    @Test
    public void addProduct_returnSavedNewProductDto() {
        // given
        final ProductDTO validProductDTO = ProductDTO.of(
                1L,
                "Kambala",
                "Amerika",
                "20kg",
                "24/06/2020",
                21.5,
                500.0,
                "123",
                "111111111111");
        when(productRepository.save(any())).thenReturn(validProductDTO.toEntity());

        // when
        final ProductDTO expectedSavedProduct = productCatalogService.addProduct(validProductDTO);

        // expected
        assertThat(validProductDTO).isEqualTo(expectedSavedProduct);
    }

    @Test
    public void updateProduct_returnValidationExceptionDueToNotValidInputDto() {
        // given
        final ProductDTO productDTO = ProductDTO.of(
                1L,
                "k",
                "Amerika",
                "20kg",
                "24/06/2020",
                21.5,
                500.0,
                "123",
                "11111111111");


        // expected
        assertThatExceptionOfType(javax.validation.ValidationException.class)
                .isThrownBy(() -> productCatalogService.updateProduct(productDTO))
                .withMessage("'name' size must be from 2 to 50 chars");
    }

    @Test
    public void updateProduct_returnNotFoundExceptionDueToNonExistingProductByIdFromArgument() {
        // given
        final ProductDTO productDTO = ProductDTO.of(
                1L,
                "Kambala",
                "Amerika",
                "20kg",
                "24/06/2020",
                21.5,
                500.0,
                "123",
                "1111111111111");

        // expected
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> productCatalogService.updateProduct(productDTO))
                .withMessage(String.format("Product by id %s not found", productDTO.getId()));
    }

    @Test
    public void updateProduct_returnUpdatedProductDto() {
        // given
        final ProductDTO productFromDB = ProductDTO.of(
                1L,
                "Kambala",
                "Amerika",
                "20kg",
                "24/06/2020",
                21.5,
                500.0,
                "123",
                "111111111111");
        final ProductDTO newProduct = ProductDTO.of(
                1L,
                "Skumbria",
                "Island",
                "15kg",
                "25/03/2020",
                31.5,
                400.0,
                "129",
                "11111111111");
        productRepository.save(productFromDB.toEntity());
        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.save(any())).thenReturn(newProduct.toEntity());

        // when
        final ProductDTO updatedProduct = productCatalogService.updateProduct(newProduct);

        // expected
        assertThat(newProduct).isEqualTo(updatedProduct);
        assertThat(productFromDB).isNotEqualTo(updatedProduct);
    }

    @Test
    public void deleteProducts_returnIllegalArgumentExceptionDueToNegativeValueOfId() {
        // given
        final Long id = -1L;

        // expected
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> productCatalogService.deleteProduct(id))
                .withMessage("Negative value of id");
    }

    @Test
    public void deleteProduct_NullProductFromDataBase(){
        // given
        final Long id = 1L;

        // expected
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> productCatalogService.deleteProduct(id))
                .withMessage(String.format("Product by %s id was not found", id));
    }

    //// TODO: 24.02.2021 not finished
    @Test
    public void deleteProduct_checkIfProductWasDeleted(){
        // given
        final Long id = 1L;

        // expected
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> productCatalogService.deleteProduct(id))
                .withMessage(String.format("Product by %s id was not found", id));
    }

    @Test
    public void uploadFile_returnIllegalArgumentExceptionDueToNegativeValueOfId() {
        // given
        final Long id = -1L;

        // expected
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> productCatalogService.uploadFile(null, id))
                .withMessage("Negative value of id");
    }

    @Test
    public void uploadFile_returnNotFoundExceptionDueToNonExistingInvoiceStatusFromDataBase() {
        // given
        final Long id = 1L;

        // expected
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> productCatalogService.uploadFile(null, id))
                .withMessage(String.format("Product by id %s not found", id));
    }

    @Test
    public void updateProductPhoto_returnIllegalArgumentExceptionDueToNegativeValueOfId() {
        // given
        final Long id = -1L;

        // expected
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> productCatalogService.updateProductPhoto(null, id))
                .withMessage("Negative value of id");
    }

    @Test
    public void updateProductPhoto_returnNotFoundExceptionDueToNonExistingInvoiceStatusFromDataBase() {
        // given
        final Long id = 1L;

        // expected
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> productCatalogService.updateProductPhoto(null, id))
                .withMessage(String.format("Product by id %s not found", id));
    }
}
