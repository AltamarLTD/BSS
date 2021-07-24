package com.altamar.shop.service.internal.user_management.impl;

import com.altamar.shop.entity.product_catalog.Product;
import com.altamar.shop.entity.user_management.Cart;
import com.altamar.shop.entity.user_management.CartItem;
import com.altamar.shop.models.dto.product_catalog.ProductDTO;
import com.altamar.shop.models.dto.user_management.CartDTO;
import com.altamar.shop.models.exсeptions.NotFoundException;
import com.altamar.shop.models.exсeptions.ValidationException;
import com.altamar.shop.repository.user_management.CartItemRepository;
import com.altamar.shop.repository.user_management.CartRepository;
import com.altamar.shop.service.internal.product_catalog.ProductCatalogService;
import com.altamar.shop.service.internal.user_management.CartService;
import com.altamar.shop.utils.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;

import static com.altamar.shop.utils.CartUtils.calculateSumOfCart;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductCatalogService productCatalogService;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductCatalogService productCatalogService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productCatalogService = productCatalogService;
    }

    @PostConstruct
    public void init() {
        log.info("[WebConfiguration] : CartServiceImpl have been initialized");
    }

    @Transactional
    @Override
    public CartDTO createCart(String ip) {
//        if (Validator.ipIsNotValid(ip)) {
//            throw new ValidationException("Ip is not valid.");
//        }
        log.info("[CartServiceImpl] : Ip is valid");
        if (isCartExistsByIp(ip)) {
            final Cart cart = Cart.builder()
                    .ip(ip)
                    .sum(0.0)
                    .cartItems(new HashSet<>())
                    .build();
            final CartDTO cartDTO = cartRepository.save(cart).toDto();
            log.info("[CartServiceImpl] : Cart was created and saved successfully");
            return cartDTO;
        }
        final CartDTO cartDTO = cartRepository.findByIp(ip).toDto();
        log.info("[CartServiceImpl] : Cart was found successfully");
        return cartDTO;
    }

    /**
     * @param ip user ip
     * @return founded CartDto by ip from argument
     */
    @Override
    public CartDTO getCart(String ip) {
        final Cart cart = findCartByIp(ip);
        calculateSumOfCart(cart);
        final Cart cart1 = cartRepository.save(cart);
        log.info("[CartServiceImpl] : Cart was found successfully");
        return cart1.toDto();
    }

    /**
     * This method validate ip and productId
     * If ip or productId not valid, throw ValidationException
     * Find Cart by ip from argument
     * if cart was not found in database, throw NotFoundException
     * Find Product by productId from argument
     * if Product was not found in database, throw NotFoundException
     * Add founded Product to the cart and calculate sum
     *
     * @param ip        user
     * @param productId product
     * @return updatedCartDto
     */
    @Transactional
    @Override
    public CartDTO addToCart(String ip, Long productId) {
        Validator.validateIds(productId);
        final Cart cart = findCartByIp(ip);
        log.info("[CartServiceImp] : Cart was founded successfully");
        final ProductDTO productDTO = productCatalogService.getProduct(productId);
        if (productDTO == null) {
            throw new NotFoundException(String.format("Product does not exits by productId %s", productId));
        }

        final Product product = productDTO.toEntity();
        if (cartItemRepository.existsByProduct_IdAndCart_Id(productId, cart.getId())) {
            log.info("[CartServiceImp] : Product with id {} already exist in the cart by ip {}. Updating", productId, ip);
            Optional<CartItem> cartItemOptional = cartItemRepository.findByProduct_Id(cart.getId(), productId);
            if (!cartItemOptional.isPresent()) {
                throw new NotFoundException(String.format("CartItem does not exits by productId %s", productId));
            }
            final CartItem cartItem = cartItemOptional.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItem.setAmount(cartItem.getAmount() + product.getPricePack());
            log.info("[CartServiceImpl] : Calculate new sum of cart");
            calculateSumOfCart(cart);
            log.info("[CartServiceImpl] : Calculated new sum of cart");
            log.info("[CartServiceImpl] : Product with id {} was updated successfully in the cart", productId);
            final CartDTO cartDTO = cartRepository.save(cart).toDto();
            log.info("[CartServiceImpl] : Cart was updated and saved successfully");
            return cartDTO;
        }

        final CartItem cartItem = CartItem.builder()
                .product(product)
                .quantity(1)
                .amount(product.getPricePack())
                .build();
        cart.addCartItem(cartItem);
        cartItem.setProduct(product);
        log.info("[CartServiceImp] : Product with id {} was added to the cart successfully", productId);
        log.info("[CartServiceImpl] : Calculate new sum of cart");
        calculateSumOfCart(cart);
        log.info("[CartServiceImpl] : Calculated new sum of cart");
        final CartDTO cartDTO = cartRepository.save(cart).toDto();
        log.info("[CartServiceImpl] : Cart was updated and saved successfully");
        return cartDTO;
    }

    /**
     * This method validate id, quantity, ip
     * If id or quantity not valid, throw IllegalArgumentException
     * if ip not valid, throw ValidationException
     * Find cart by ip in database
     * if cart was not found, throw NotFoundException
     * Find cartItem by product id from argument
     * if cartItem was not found, throw notFoundException
     * If quantity from argument equals 0, delete cartItem
     * Else set new quantity and calculate new sum for cart
     *
     * @param ip        user
     * @param productId product id
     * @param quantity  new quantity
     * @return updated CartDto
     */
    @Transactional
    @Override
    public CartDTO updateCart(String ip, Long productId, int quantity) {
        if (productId < 0 || quantity < 0) {
            throw new IllegalArgumentException("Negative value of productId or quantity");
        }
        final Cart cart = findCartByIp(ip);
        log.info("[CartServiceImpl] : Cart was found successfully");
        final Optional<CartItem> cartItemOptional = cartItemRepository.findByProduct_Id(cart.getId(), productId);
        log.info("[CartServiceImpl] : CartItem with product id {} was found successfully", productId);
        if (!cartItemOptional.isPresent()) {
            throw new NotFoundException(String.format("Cart item does not exits by productId %s", productId));
        }
        final CartItem cartItem = cartItemOptional.get();

        if (quantity == 0) {
            log.info("[CartServiceImpl] : Setting CartItem with product id {} quantity 0", productId);
            cart.deleteCartItem(cartItem);
            cartItemRepository.delete(cartItem);
            log.info("[CartServiceImpl] : CartItem with product id {} was deleted successfully", productId);
        } else {
            log.info("[CartServiceImpl] : Setting To CartItem with product id {} quantity {}", productId, quantity);
            cartItem.setQuantity(quantity);
            cartItem.setAmount(quantity * cartItem.getProduct().getPricePack());
            log.info("[CartServiceImpl] : Set To CartItem with product id {} new quantity {}", productId, quantity);
        }

        calculateSumOfCart(cart);
        log.info("[CartServiceImpl] : Calculate new sum of cart");
        final CartDTO cartDTO = cartRepository.save(cart).toDto();
        log.info("[CartServiceImpl] : Cart with ip {} was updated and saved successfully", ip);
        return cartDTO;
    }

    /**
     * This method validate id, ip
     * If id not valid, throw IllegalArgumentException
     * if ip not valid, throw ValidationException
     * Find cart by ip in database
     * if cart was not found, throw NotFoundException
     * Find cartItem by product id from argument
     * if cartItem was not found, throw notFoundException
     * Delete founded CartItem and calculate new sum for cart
     *
     * @param ip        user
     * @param productId product id
     * @return updated CartDto
     */
    @Transactional
    @Override
    public CartDTO deleteItem(String ip, Long productId) {
        Validator.validateIds(productId);
        final Cart cart = findCartByIp(ip);
        log.info("[CartServiceImpl] : Cart was found successfully");
        final Optional<CartItem> cartItemOptional = cartItemRepository.findByProduct_Id(cart.getId(), productId);
        log.info("[CartServiceImpl] : CartItem with product id {} was found successfully", productId);
        if (!cartItemOptional.isPresent()) {
            throw new NotFoundException(String.format("Cart item does not exits by productId %s", productId));
        }

        final CartItem cartItem = cartItemOptional.get();
        cart.deleteCartItem(cartItem);
        cartItemRepository.delete(cartItem);
        log.info("[CartServiceImpl] : CartItem with product id {} was deleted successfully", productId);
        calculateSumOfCart(cart);
        log.info("[CartServiceImpl] : Calculate new sum of cart");
        final CartDTO savedCart = cartRepository.save(cart).toDto();
        log.info("[CartServiceImpl] : Cart was updated and saved successfully");
        return savedCart;
    }

    /**
     * This method validate ip
     * if ip not valid, throw ValidationException
     * Find cart by ip in database
     * if cart was not found, throw NotFoundException
     * Delete all CartItems and calculate new sum for cart
     *
     * @param ip user
     * @return updated CartDto
     */
    @Transactional
    @Override
    public CartDTO deleteAllItems(String ip) {
        final Cart cart = findCartByIp(ip);
        log.info("[CartServiceImpl] : Cart was founded successfully");
        cartItemRepository.deleteAllByCart_Id(cart.getId());
        cart.deleteAllCartItems();
        log.info("[CartServiceImpl] : All CartItem was deleted successfully");
        cart.setSum(0.0);
        final CartDTO savedCart = cartRepository.save(cart).toDto();
        log.info("[CartServiceImpl] : Cart was updated and saved successfully");
        return savedCart;
    }

    @Override
    public void deleteCartById(Long cartId) {
        Validator.validateIds(cartId);
        cartRepository.deleteById(cartId);
    }

    /**
     * This method validate ip
     * if ip not valid, throw ValidationException
     * Find cart by ip in database
     * if cart was not found, throw NotFoundException
     *
     * @param ip user
     * @return founded CartDto
     */
    private Cart findCartByIp(String ip) {
        if (isCartExistsByIp(ip)) {
            throw new NotFoundException(String.format("Cart does not exits by ip %s", ip));
        }
        log.info("[CartServiceImpl] : Cart exist");
        return cartRepository.findByIp(ip);
    }

    /**
     * This method check if cart exist by ip from argument
     * Validate ip
     * If ip not valid, throw ValidationException
     *
     * @param ip user
     * @return true if exist
     */
    public boolean isCartExistsByIp(String ip) {
        if (Validator.ipIsNotValid(ip)) {
            throw new ValidationException("Ip is not valid.");
        }
        log.info("[CartServiceImpl] : Ip is valid");
        return !cartRepository.existsCartByIp(ip);
    }

}
