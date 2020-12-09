package com.altamar.shop.controllers.access;

import com.altamar.shop.models.dto.Response;
import com.altamar.shop.models.dto.aceess.CredentialDto;
import com.altamar.shop.models.dto.aceess.TokenDto;
import com.altamar.shop.service.internal.authorization.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/api/v2/access")
public class AccessController {

    private final AuthorizationService authorizationService;

    public AccessController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    /**
     * @param credential CredentialDto contains username and password
     * @return Token
     */
    @PostMapping("/auth")
    public ResponseEntity<Response<TokenDto>> auth(@RequestBody CredentialDto credential, HttpServletRequest request) {
        log.info("[AccessController] : Authorization...");
        final TokenDto authorize = authorizationService.authorize(credential, request.getRemoteAddr());
        log.info("[AccessController] : Authorized");
        return ok(Response.ok(authorize));
    }

}
