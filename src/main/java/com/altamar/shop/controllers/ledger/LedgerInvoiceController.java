package com.altamar.shop.controllers.ledger;

import com.altamar.shop.models.dto.Response;
import com.altamar.shop.models.dto.ledger.InvoiceDTO;
import com.altamar.shop.models.dto.ledger.InvoiceStatusDTO;
import com.altamar.shop.models.dto.user_management.UserDTO;
import com.altamar.shop.models.enums.InvoiceStatusEnum;
import com.altamar.shop.service.internal.ledger.LedgerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/api/v2/ledger/invoice")
public class LedgerInvoiceController {

    private final LedgerService ledgerService;

    public LedgerInvoiceController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    /**
     * @param request (ip)
     * @param userDTO (new)
     * @return formatted InvoiceDto with products from cart founded by ip
     */
    @PostMapping("/checkout/confirm")
    public ResponseEntity<Response<InvoiceDTO>> confirm(HttpServletRequest request, @RequestBody UserDTO userDTO) {
        log.info("[LedgerController] : Formatting and confirming new invoice from cart by ip");
        final InvoiceDTO result = ledgerService.createInvoice(request.getRemoteAddr(), userDTO);
        log.info("[LedgerController] : Formatted and confirmed new invoice from cart by ip. Sent notification by client's email");
        return ok(Response.ok(result));
    }

    /**
     * @return List of InvoiceDto
     */
    @GetMapping
    public ResponseEntity<Response<List<InvoiceDTO>>> allInvoiceBase() {
        log.info("[LedgerController] : Get all Invoices");
        return ok(Response.ok(ledgerService.getAllInvoices()));
    }

    /**
     * @param userId (User)
     * @return List of InvoicesDto by User userId
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Response<List<InvoiceDTO>>> clientInvoiceBase(@PathVariable("userId") Long userId) {
        log.info("[LedgerController] : Get invoice by user userId");
        return ok(Response.ok(ledgerService.getClientOrderHistory(userId)));
    }

    /**
     * @return List of InvoiceStatusesDto
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/status")
    public ResponseEntity<Response<List<InvoiceStatusDTO>>> getAllInvoiceStatuses() {
        log.info("[LedgerController] : Get all InvoiceStatuses");
        return ok(Response.ok(ledgerService.getAllStatuses()));
    }

    /**
     * @param id     (Invoice)
     * @param status (new)
     * @return InvoiceDto, with updated InvoiceStatusDto by id of Invoice and new status
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{invoiceId}/status")
    public ResponseEntity<Response<InvoiceDTO>> updateStatus(@PathVariable(name = "invoiceId") Long id,
                                                             @RequestParam(name = "invoiceStatus") String status) {
        log.info("[LedgerController] : Updating invoice by Invoice id = {}", id);
        final InvoiceDTO result = ledgerService.updateInvoiceStatus(id, InvoiceStatusEnum.value(status));
        log.info("[LedgerController] : Updated invoice by Invoice id = {}, to new status {}", id, status);
        return ok(Response.ok(result));
    }
}
