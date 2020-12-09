package com.altamar.shop.service.internal.ledger.impl;

import com.altamar.shop.entity.ledger.Invoice;
import com.altamar.shop.entity.ledger.InvoiceProduct;
import com.altamar.shop.entity.ledger.InvoiceStatus;
import com.altamar.shop.entity.user_management.Cart;
import com.altamar.shop.entity.user_management.User;
import com.altamar.shop.models.dto.ledger.InvoiceDTO;
import com.altamar.shop.models.dto.ledger.InvoiceStatusDTO;
import com.altamar.shop.models.dto.user_management.CartDTO;
import com.altamar.shop.models.dto.user_management.UserDTO;
import com.altamar.shop.models.enums.InvoiceStatusEnum;
import com.altamar.shop.models.exсeptions.NotFoundException;
import com.altamar.shop.repository.ledger.InvoiceRepository;
import com.altamar.shop.repository.ledger.InvoiceStatusRepository;
import com.altamar.shop.service.internal.ledger.LedgerService;
import com.altamar.shop.service.internal.user_management.CartService;
import com.altamar.shop.service.internal.user_management.UserManagementService;
import com.altamar.shop.utils.DateUtils;
import com.altamar.shop.utils.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LedgerServiceImpl implements LedgerService {

    private final UserManagementService userManagementService;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceStatusRepository invoiceStatusRepository;
    private final CartService cartService;

    public LedgerServiceImpl(InvoiceRepository invoiceRepository,
                             InvoiceStatusRepository invoiceStatusRepository,
                             UserManagementService userManagementService,
                             CartService cartService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceStatusRepository = invoiceStatusRepository;
        this.userManagementService = userManagementService;
        this.cartService = cartService;
    }

    @PostConstruct
    public void init() {
        log.info("[WebConfiguration] : LedgerServiceImpl have been initialized");
    }

    @Override
    public InvoiceDTO createInvoice(String ip, UserDTO userDTO) {
        final UserDTO user = userManagementService.addDevice(ip, userDTO);
        if (user == null) {
            throw new NotFoundException(String.format("User does not exits by id %s", userDTO.getId()));
        }
        final CartDTO cartDTO = cartService.getCart(ip);
        if (cartDTO == null) {
            throw new NotFoundException(String.format("Cart does not exits by ip %s", ip));
        }
        final InvoiceStatusEnum defaultStatus = InvoiceStatusEnum.INVOICE_STATUS_PROCESSING;
        final Optional<InvoiceStatus> invoiceStatusOptional = invoiceStatusRepository.findByStatus(defaultStatus.getName());
        if (!invoiceStatusOptional.isPresent()) {
            throw new NotFoundException("InvoiceStatus not found");
        }

        final InvoiceStatus processingInvoiceStatus = invoiceStatusOptional.get();
        log.info("[LedgerServiceImpl] : Invoice status {} was found successfully", processingInvoiceStatus);

        final Set<InvoiceProduct> invoiceProducts = cartDTO.getCartItems().stream()
                .filter(Objects::nonNull)
                .map(cartItem -> InvoiceProduct.builder()
                        .product(cartItem.toEntity().getProduct())
                        .quantity(cartItem.getQuantity())
                        .build())
                .collect(Collectors.toSet());
        log.info("[LedgerServiceImpl] : Fill list of invoiceProduct from cartDTO successfully");

        final Invoice invoice = Invoice.builder()
                .date(DateUtils.generateCurrentDate(new Date()))
                .sum(cartDTO.getSum())
                .invoiceProducts(invoiceProducts)
                .invoiceStatus(processingInvoiceStatus)
                .user(user.toEntity())
                .build();
        //invoiceProducts.forEach(invoice::addInvoiceProduct).;
        invoiceProducts.forEach(invoiceProduct ->
                invoiceProduct.setInvoice(invoice)
        );

        log.info("[LedgerServiceImpl] : Formatted new invoice for user");
        final Invoice saved = invoiceRepository.save(invoice);
        log.info("[LedgerServiceImpl] : Invoice for user was saved ");
        cartService.deleteCartById(cartDTO.getId());
        log.info("[LedgerServiceImpl] : Cart was deleted ");
        return saved.toDto();
    }

    /**
     * @return all List of InvoiceDto from database
     */
    @Override
    public List<InvoiceDTO> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .filter(Objects::nonNull)
                .map(Invoice::toDto)
                .collect(Collectors.toList());
    }

    /**
     * This method validate input product id
     * Find User entity in database
     * If User was not found in database, throw NotFoundException
     *
     * @param id user id
     * @return returns List of InvoicesDto taken from the founded User
     */
    @Override
    public List<InvoiceDTO> getClientOrderHistory(Long id) {
        Validator.validateIds(id);
        return invoiceRepository.getAllInvoicesByUserId(id).stream()
                .filter((Objects::nonNull))
                .map(Invoice::toDto)
                .collect(Collectors.toList());
    }

    /**
     * @return List of InvoiceStatusesDto from database
     */
    @Override
    public List<InvoiceStatusDTO> getAllStatuses() {
        return invoiceStatusRepository.findAll().stream()
                .filter(Objects::nonNull)
                .map(InvoiceStatus::toDto)
                .collect(Collectors.toList());
    }

    /**
     * This method validate input invoice id
     * Find Invoice entity in database
     * If Invoice was not found in database, throw NotFoundException
     * Find InvoiceStatus entity by status from argument in database
     * If InvoiceStatus was not found in database, throw NotFoundException
     * Set for founded Invoice new InvoiceStatus
     *
     * @param id     invoice id
     * @param status new updatable status
     * @return updated and saved InvoiceDto
     */
    @Transactional
    @Override
    public InvoiceDTO updateInvoiceStatus(Long id, InvoiceStatusEnum status) {
        Validator.validateIds(id);
        final Optional<Invoice> invoiceOptional = invoiceRepository.findById(id);
        log.info("[LedgerServiceImpl] : Invoice was found by id {}", id);
        if (!invoiceOptional.isPresent()) {
            throw new NotFoundException(String.format("Invoice by id %s not found", id));
        }
        final Optional<InvoiceStatus> invoiceStatus = invoiceStatusRepository.findByStatus(status.getName());
        log.info("[LedgerServiceImpl] : InvoiceStatus was found by status {}", status);
        if (!invoiceStatus.isPresent()) {
            throw new NotFoundException("Updatable invoice status absent in data base");
        }
        final Invoice invoice = invoiceOptional.get();
        invoice.setInvoiceStatus(invoiceStatus.get());
        log.info("[LedgerServiceImpl] : To Invoice with id {} was installed new status {}", id, status);
        final InvoiceDTO savedInvoice = invoiceRepository.save(invoice).toDto();
        log.info("[LedgerServiceImpl] : Invoice saved");
        return savedInvoice;
    }
}
