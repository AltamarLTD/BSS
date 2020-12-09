package com.altamar.shop.service.internal.user_management;

import com.altamar.shop.entity.ledger.Invoice;
import com.altamar.shop.entity.user_management.User;
import com.altamar.shop.models.dto.ledger.InvoiceDTO;
import com.altamar.shop.models.dto.user_management.Checkout;
import com.altamar.shop.models.dto.user_management.UserDTO;

import java.util.List;


public interface UserManagementService {

    Checkout checkout(String ip);

    UserDTO addDevice(String ip, UserDTO userDTO);

    UserDTO getUserByInvoiceId(Long invoiceId);

    List<UserDTO> getAllUser();

}
