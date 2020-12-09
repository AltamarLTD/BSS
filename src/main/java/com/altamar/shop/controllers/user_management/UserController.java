package com.altamar.shop.controllers.user_management;

import com.altamar.shop.models.dto.Response;
import com.altamar.shop.models.dto.user_management.Checkout;
import com.altamar.shop.models.dto.user_management.UserDTO;
import com.altamar.shop.service.internal.user_management.UserManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/api/v2/user-management/user")
public class UserController {

    private final UserManagementService userManagementService;

    public UserController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    /**
     * @param request (ip)
     * @return checkout entity by ip
     */
    @GetMapping("/checkout")
    public ResponseEntity<Response<Checkout>> checkout(HttpServletRequest request) {
        log.info("[UserController] : Getting checkout by ip");
        final Checkout result = userManagementService.checkout(request.getRemoteAddr());
        log.info("[UserController] : Got checkout by ip");
        return ok(Response.ok(result));
    }

    /**
     * @return List of UserDto
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Response<List<UserDTO>>> clientBase() {
        log.info("[UserController] : Get all users");
        return ok(Response.ok(userManagementService.getAllUser()));
    }

    /**
     * @param invoiceId invoice invoiceId
     * @return UserDto by Invoice invoiceId
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<Response<UserDTO>> getUserByInvoice(@PathVariable("invoiceId") Long invoiceId) {
        log.info("[UserController] : Get UserDto by invoice invoiceId {}", invoiceId);
        return ok(Response.ok(userManagementService.getUserByInvoiceId(invoiceId)));
    }
}
