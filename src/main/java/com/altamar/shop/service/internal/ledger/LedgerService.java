package com.altamar.shop.service.internal.ledger;

import com.altamar.shop.models.dto.ledger.InvoiceDTO;
import com.altamar.shop.models.dto.ledger.InvoiceStatusDTO;
import com.altamar.shop.models.dto.user_management.UserDTO;
import com.altamar.shop.models.enums.InvoiceStatusEnum;

import java.util.List;

public interface LedgerService {

    InvoiceDTO createInvoice(String ip, UserDTO userDTO);

    List<InvoiceDTO> getAllInvoices();

    List<InvoiceDTO> getClientOrderHistory(Long id);

    List<InvoiceStatusDTO> getAllStatuses();

    InvoiceDTO updateInvoiceStatus(Long id, InvoiceStatusEnum invoiceStatus);

}
