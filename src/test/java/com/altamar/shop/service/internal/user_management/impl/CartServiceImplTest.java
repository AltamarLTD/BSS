package com.altamar.shop.service.internal.user_management.impl;

import com.altamar.shop.entity.product_catalog.Product;
import com.altamar.shop.entity.user_management.Cart;
import com.altamar.shop.entity.user_management.CartItem;
import com.altamar.shop.models.dto.user_management.CartDTO;
import com.altamar.shop.models.exсeptions.NotFoundException;
import com.altamar.shop.models.exсeptions.ValidationException;
import com.altamar.shop.repository.product_catalog.ProductRepository;
import com.altamar.shop.repository.user_management.CartItemRepository;
import com.altamar.shop.repository.user_management.CartRepository;
import com.altamar.shop.service.internal.product_catalog.impl.ProductCatalogServiceImpl;
import com.altamar.shop.service.internal.user_management.impl.CartServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductCatalogServiceImpl productCatalogService;
    @InjectMocks
    private CartServiceImpl cartService;

    @Before
    public void init(){
        cartService = new CartServiceImpl(cartRepository, cartItemRepository, productCatalogService);
    }

    @Test
    public void createCart_returnValidationExceptionDueToNotValidIp() {
        // given
        final String ip = "0.0.0.";

        // expected
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> cartService.createCart(ip))
                .withMessage("Ip is not valid.");
    }

    @Test
    public void createCart_returnSavedCreatedCartDto() {
        // given
        final String ip = "109.86.22.222";
        final Cart givenCart = Cart.builder()
                .sum(0.0)
                .ip(ip)
                .cartItems(new HashSet<>())
                .build();
        when(cartRepository.save(any())).thenReturn(givenCart);

        // when
        final CartDTO expectedCart = cartService.createCart(ip);

        // expected
        assertThat(givenCart.toDto()).isEqualTo(expectedCart);
    }

    @Test
    public void createCart_returnFoundCartDtoIfCartExistByIpFromArgument() {
        // given
        final String ip = "109.86.22.222";
        final Cart givenCart = Cart.builder()
                .sum(0.0)
                .ip(ip)
                .cartItems(new HashSet<>())
                .build();
        when(cartRepository.findByIp(ip)).thenReturn(givenCart);
        when(cartService.isCartExistsByIp(ip)).thenReturn(true);

        // when
        final CartDTO expectedCart = cartService.createCart(ip);

        // expected
        assertThat(givenCart.toDto()).isEqualTo(expectedCart);
    }

    @Test
    public void getCart_returnValidationExceptionDueToNotValidIp() {
        // given
        final String ip = "0.0.0.";

        // expected
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> cartService.getCart(ip))
                .withMessage("Ip is not valid.");
    }

    @Test
    public void getCart_findCartByIp_returnNotFoundExceptionDueToNonExistingCartByIpFromArgument() {
        // given
        final String ip = "109.86.22.222";

        // expected
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> cartService.getCart(ip))
                .withMessage("Cart does not exits by ip");
    }

    //// TODO: 24.02.2021 bug
    @Test
    public void getCart_findCartByIp_returnFoundCartDto() {
        // given
        final String ip = "109.86.22.222";
        final Cart givenCart = Cart.builder()
                .sum(0.0)
                .ip(ip)
                .cartItems(new HashSet<>())
                .build();
        when(cartRepository.findByIp(ip)).thenReturn(givenCart);
        when(cartService.isCartExistsByIp(ip)).thenReturn(true);

        // when
        final CartDTO expectedCart = cartService.getCart(ip);

        // expected
        assertThat(givenCart.toDto()).isEqualTo(expectedCart);
    }

    @Test
    public void updateCart_returnValidationExceptionDueToNotValidIp() {
        // given
        final String ip = "0.0.0.";
        final Long id = 1L;
        final int quantity = 1;

        // expected
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> cartService.updateCart(ip, id, quantity))
                .withMessage("Ip is not valid.");
    }

    @Test
    public void updateCart_returnIllegalArgumentExceptionDueToNegativeValueOfId() {
        // given
        final String ip = "109.86.22.222";
        final Long negativeId = -1L;
        final int quantity = 1;

        // expected
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> cartService.updateCart(ip, negativeId, quantity))
                .withMessage("Negative value of productId or quantity");
    }

    @Test
    public void updateCart_returnIllegalArgumentExceptionDueToNegativeValueOfQuantity() {
        // given
        final String ip = "109.86.22.222";
        final Long id = 1L;
        final int negativeQuantity = -1;

        // expected
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> cartService.updateCart(ip, id, negativeQuantity))
                .withMessage("Negative value of productId or quantity");
    }

    @Test
    public void updateCart_returnNotFoundExceptionDueToNonExistingCartByIpFromArgument() {
        // given
        final String ip = "109.86.22.224";
        final Long id = 1L;
        final int quantity = 1;

        // expected
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> cartService.updateCart(ip, id, quantity))
                .withMessage("Cart does not exits by ip");
    }

    @Test
    public void updateCart_returnNotFoundExceptionDueToNonExistingCartItemByProductIdAndCartId() {
        // given
        final Long productId = 1L;
        final int quantity = 1;
        final String ip = "109.86.22.222";
        final Cart givenCart = Cart.builder()
                .sum(0.0)
                .ip(ip)
                .cartItems(new HashSet<>())
                .build();
        when(cartRepository.findByIp(ip)).thenReturn(givenCart);
        when(cartService.isCartExistsByIp(ip)).thenReturn(true);


        // expected
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> cartService.updateCart(ip, productId, quantity))
                .withMessage(String.format("Cart item does not exits by productId %s", productId));
    }

    @Test
    public void updateCart_returnChangedSumOfCartDueToInputQuantityFromArgumentValueMoreThanOne() {
        // given
        final String ip = "109.86.22.224";
        final Long id = 1L;
        final int quantity = 2;
        final Cart cart = Cart.builder()
                .id(10L)
                .ip(ip)
                .cartItems(new HashSet<>())
                .build();
        final Product product = Product.builder()
                .id(1L)
                .pricePack(30.0)
                .build();
        final CartItem cartItem = CartItem.builder()
                .amount(30.0)
                .quantity(1)
                .product(product)
                .build();
        cart.addCartItem(cartItem);
        cartRepository.save(cart);
        when(cartRepository.findByIp(ip)).thenReturn(cart);
        when(cartService.isCartExistsByIp(ip)).thenReturn(true);
        when(cartItemRepository.findByProduct_Id(10L,1L)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(cart)).thenReturn(cart);

        // when
        final CartDTO expectedCart = cartService.updateCart(ip, id, quantity);

        // expected
        assertThat(product.getPricePack() * quantity).isEqualTo(expectedCart.getSum());
    }

    //// TODO: 24.02.2021 not finished return cartItem
    @Test
    public void updateCart_deleteCartItem_returnNullCartItemDueToZeroQuantityFromArgument() {
        final String ip = "109.86.22.224";
        final Long id = 1L;
        final int quantity = 0;
        final Cart cart = Cart.builder()
                .id(10L)
                .ip(ip)
                .cartItems(new HashSet<>())
                .build();
        final Product product = Product.builder()
                .id(1L)
                .pricePack(30.0)
                .build();
        final CartItem cartItem = CartItem.builder()
                .amount(30.0)
                .quantity(1)
                .product(product)
                .build();
        cart.addCartItem(cartItem);
        cartRepository.save(cart);
        when(cartRepository.findByIp(ip)).thenReturn(cart);
        when(cartService.isCartExistsByIp(ip)).thenReturn(true);
        when(cartItemRepository.findByProduct_Id(10L,1L)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(cart)).thenReturn(cart);

        // when
        final CartDTO expectedCart = cartService.updateCart(ip, id, quantity);
        System.out.println(expectedCart.getCartItems());

        // expected
        assertThat(expectedCart.getCartItems()).doesNotContain(cartItem.toDto());
    }

    @Test
    public void addToCart_inputNegativeValueOfId() {
        // given
        final Long id = -1L;

        // expected
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> cartService.addToCart("", id))
                .withMessage("Negative value of id");
    }

    @Test
    public void addToCart_findCartByIp_returnValidationExceptionDueToNotValidIp() {
        // given
        final Long id = 1L;
        final String ip = "0.0.0.";

        // expected
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> cartService.addToCart(ip, id))
                .withMessage("Ip is not valid.");
    }

    @Test
    public void addToCart_findCartByIp_returnNotFoundExceptionDueToNonExistingCartByIpFromArgument() {
        // given
        final Long id = 1L;
        final String ip = "109.86.22.222";

        // expected
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> cartService.addToCart(ip, id))
                .withMessage("Cart does not exits by ip");
    }

    @Test
    public void addToCart_addCartItem_testLinkingCartAndCartItemByOneToMany() {
        // given
        final String ip = "109.86.22.222";
        final Cart cartGiven = Cart.builder()
                .id(5L)
                .ip(ip)
                .cartItems(new HashSet<>())
                .build();
        final Product product = Product.builder()
                .id(3L)
                .pricePack(20.0)
                .build();
        final CartItem cartItem = CartItem.builder()
                .quantity(1)
                .amount(20.0)
                .product(product)
                .cart(cartGiven)
                .build();

        final Optional<Product> optionalProduct = Optional.of(product);
        when(cartRepository.save(cartGiven)).thenReturn(cartGiven);
        when(productRepository.findById(3L)).thenReturn(optionalProduct);
        when(cartService.isCartExistsByIp(ip)).thenReturn(true);
        when(cartRepository.findByIp(ip)).thenReturn(cartGiven);


        // when
        cartGiven.addCartItem(cartItem);
        cartRepository.save(cartGiven);
        final CartDTO cartExpected = cartService.addToCart(ip, 3L);

        // expected
        assertThat(cartExpected.getCartItems()).contains(cartItem.toDto());
    }

    @Test
    public void addToCart_CartUtils_testCalculateAmountOfCart() {
        // given
        final String ip = "109.86.22.222";
        final Cart cartGiven = Cart.builder()
                .id(5L)
                .ip(ip)
                .cartItems(new HashSet<>())
                .build();
        final Product product1 = Product.builder()
                .id(3L)
                .pricePack(20.0)
                .build();
        final Product product2 = Product.builder()
                .id(4L)
                .pricePack(40.0)
                .build();

        final Optional<Product> optionalProduct1 = Optional.of(product1);
        final Optional<Product> optionalProduct2 = Optional.of(product2);
        when(productRepository.findById(3L)).thenReturn(optionalProduct1);
        when(productRepository.findById(4L)).thenReturn(optionalProduct2);
        when(cartRepository.save(cartGiven)).thenReturn(cartGiven);
        when(cartService.isCartExistsByIp(ip)).thenReturn(true);
        when(cartRepository.findByIp(ip)).thenReturn(cartGiven);

        // when
        cartService.addToCart(ip, 3L);
        final CartDTO cartExpected = cartService.addToCart(ip, 4L);

        // expected
        assertThat(product1.getPricePack() + product2.getPricePack()).isEqualTo(cartExpected.getSum());

    }

    @Test
    public void delete_returnValidationExceptionDueToNotValidIp() {
        // given
        final String notValidIp = "0.0.0.";
        final Long id = 1L;

        // expected
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> cartService.deleteItem(notValidIp, id))
                .withMessage("Ip is not valid.");
    }

    @Test
    public void delete_returnIllegalArgumentExceptionDueToNegativeValueOfId() {
        // given
        final String ip = "109.86.22.222";
        final Long negativeId = -1L;

        // expected
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> cartService.deleteItem(ip, negativeId))
                .withMessage("Negative value of id");
    }

    @Test
    public void delete_returnNotFoundExceptionDueToNonExistingCartByIpFromArgument() {
        // given
        final String ip = "109.86.22.224";
        final Long id = 1L;

        // expected
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> cartService.deleteItem(ip, id))
                .withMessage("Cart does not exits by ip");
    }

    //// TODO: 24.02.2021 not delete
    @Test
    public void delete_returnNullCartItemDueToDeleteByProductId() {
        final String ip = "109.86.22.224";
        final Cart cart = Cart.builder()
                .id(10L)
                .ip(ip)
                .cartItems(new HashSet<>())
                .build();
        final Product product = Product.builder()
                .id(1L)
                .pricePack(30.0)
                .build();
        final CartItem cartItem = CartItem.builder()
                .amount(30.0)
                .quantity(1)
                .product(product)
                .build();
        cart.addCartItem(cartItem);
        cartRepository.save(cart);
        when(cartRepository.findByIp(ip)).thenReturn(cart);
        when(cartService.isCartExistsByIp(ip)).thenReturn(true);
        when(cartItemRepository.findByProduct_Id(10L,1L)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(cart)).thenReturn(cart);

        // when
        final CartDTO expectedCart = cartService.deleteItem(ip, 1L);
        System.out.println(expectedCart.getCartItems());

        // expected
        assertThat(expectedCart.getCartItems()).doesNotContain(cartItem.toDto());
    }

    //// TODO: 24.02.2021 not delete
    @Test
    public void delete_deleteOnlyOneCartItem_returnNullCartItemDueToDeleteByProductId() {
        final String ip = "109.86.22.224";
        final Cart cart = Cart.builder()
                .id(10L)
                .ip(ip)
                .cartItems(new HashSet<>())
                .build();
        final Product product1 = Product.builder()
                .id(1L)
                .pricePack(30.0)
                .build();
        final Product product2 = Product.builder()
                .id(2L)
                .pricePack(40.0)
                .build();
        final CartItem cartItem1 = CartItem.builder()
                .amount(30.0)
                .quantity(1)
                .product(product1)
                .build();
        final CartItem cartItem2 = CartItem.builder()
                .amount(40.0)
                .quantity(1)
                .product(product2)
                .build();
        cart.addCartItem(cartItem1);
        cart.addCartItem(cartItem2);
        cartRepository.save(cart);
        when(cartRepository.findByIp(ip)).thenReturn(cart);
        when(cartService.isCartExistsByIp(ip)).thenReturn(true);
        when(cartItemRepository.findByProduct_Id(10L,1L)).thenReturn(Optional.of(cartItem1));
        when(cartRepository.save(cart)).thenReturn(cart);

        // when
        final CartDTO expectedCart = cartService.deleteItem(ip, 1L);

        // expected
        assertThat(expectedCart.getCartItems()).doesNotContain(cartItem1.toDto());
        assertThat(expectedCart.getCartItems()).contains(cartItem2.toDto());
    }

    @Test
    public void deleteAll_returnValidationExceptionDueToNotValidIp() {
        // given
        final String notValidIp = "0.0.0.";

        // expected
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> cartService.deleteAllItems(notValidIp))
                .withMessage("Ip is not valid.");
    }

    @Test
    public void deleteAll_returnEmptySetOfCartItems() {
        final String ip = "109.86.22.224";
        final Cart cart = Cart.builder()
                .ip(ip)
                .cartItems(new HashSet<>())
                .build();
        final Product product1 = Product.builder()
                .id(1L)
                .pricePack(30.0)
                .build();
        final Product product2 = Product.builder()
                .id(2L)
                .pricePack(40.0)
                .build();
        final CartItem cartItem1 = CartItem.builder()
                .amount(30.0)
                .quantity(1)
                .product(product1)
                .build();
        final CartItem cartItem2 = CartItem.builder()
                .amount(40.0)
                .quantity(1)
                .product(product2)
                .build();
        cart.addCartItem(cartItem1);
        cart.addCartItem(cartItem2);
        cartRepository.save(cart);
        when(cartRepository.findByIp(ip)).thenReturn(cart);
        when(cartService.isCartExistsByIp(ip)).thenReturn(true);
        when(cartRepository.save(cart)).thenReturn(cart);

        // when
        final CartDTO expectedCart = cartService.deleteAllItems(ip);

        // expected
        assertThat(expectedCart.getCartItems()).contains();
    }
}
