package com.altamar.shop.controllers.user_management;

import com.altamar.shop.models.dto.Response;
import com.altamar.shop.models.dto.user_management.CartDTO;
import com.altamar.shop.service.internal.user_management.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/api/v2/user-management/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * @param request (ip)
     * @return CartDto entity founded by ip
     */
    @GetMapping
    public ResponseEntity<Response<CartDTO>> cart(HttpServletRequest request) {
        log.info("[CartController] : Getting cartDto");
        final CartDTO result = cartService.getCart(request.getRemoteAddr());
        log.info("[CartController] : Got cartDto by ip");
        return ok(Response.ok(result));
    }

    /**
     * @param request (ip)
     * @return created CartDto
     */
    @PostMapping
    public ResponseEntity<Response<CartDTO>> createCart(HttpServletRequest request) {
        log.info("[CartController] : Creating cartDto");
        final CartDTO result = cartService.createCart(request.getRemoteAddr());
        log.info("[CartController] : Created cartDto by ip");
        return ok(Response.ok(result));
    }

    /**
     * @param productId      (Product)
     * @param request (ip)
     * @return status ok after adding Product by productId to the Cart founded by ip
     */
    @PostMapping("/product/{productId}")
    public ResponseEntity<Response<CartDTO>> add(HttpServletRequest request, @PathVariable(name = "productId") Long productId) {
        log.info("[CartController] : Adding product by productId {} to the cart by ip", productId);
        final CartDTO result = cartService.addToCart(request.getRemoteAddr(), productId);
        log.info("[CartController] : Product by productId {} was added to the cart by ip", productId);
        return ResponseEntity.ok(Response.ok(result));
    }

    /**
     * @param request  (ip)
     * @param quantity (new)
     * @return CartDto founded by ip  with updated sum and quantity
     */
    @PutMapping
    public ResponseEntity<Response<CartDTO>> updateCart(HttpServletRequest request,
                                                        @RequestParam(name = "quantity") int quantity,
                                                        @RequestParam(name = "productId") Long productId) {
        log.info("[CartController] : Updating cart");
        final CartDTO result = cartService.updateCart(request.getRemoteAddr(), productId, quantity);
        log.info("[CartController] : Cart by ip was updated to new quantity {} by product productId {}", quantity, productId);
        return ok(Response.ok(result));
    }


    /**
     * @param productId     (Product)
     * @param request       (ip)
     * @return status ok after deleting CartItem by product productId from Cart founded by ip
     */
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Response<?>> delete(HttpServletRequest request, @PathVariable("productId") Long productId) {
        log.info("[CartController] : Delete Cart Item from cart by ip, by product productId {}", productId);
        cartService.deleteItem(request.getRemoteAddr(), productId);
        return ResponseEntity.ok(Response.ok("Ok"));
    }

    /**
     * @param request (ip)
     * @return status ok after deleting all CartItems from Cart founded by ip
     */
    @DeleteMapping("/products")
    public ResponseEntity<Response<?>> deleteAll(HttpServletRequest request) {
        log.info("[CartController] : Delete all Cart Item from cart by ip");
        cartService.deleteAllItems(request.getRemoteAddr());
        return ResponseEntity.ok(Response.ok("Ok"));
    }
}
