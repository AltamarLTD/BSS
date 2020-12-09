package com.altamar.shop.service.internal.user_management.impl;

import com.altamar.shop.entity.user_management.Device;
import com.altamar.shop.entity.user_management.User;
import com.altamar.shop.models.dto.user_management.CartDTO;
import com.altamar.shop.models.dto.user_management.Checkout;
import com.altamar.shop.models.dto.user_management.UserDTO;
import com.altamar.shop.models.exсeptions.NotFoundException;
import com.altamar.shop.models.exсeptions.ValidationException;
import com.altamar.shop.repository.user_management.DeviceRepository;
import com.altamar.shop.repository.user_management.UserRepository;
import com.altamar.shop.service.internal.user_management.CartService;
import com.altamar.shop.service.internal.user_management.UserManagementService;
import com.altamar.shop.utils.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserManagementServiceImpl implements UserManagementService {

    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final CartService cartService;

    public UserManagementServiceImpl(UserRepository userRepository,
                                     DeviceRepository deviceRepository,
                                     CartService cartService
    ) {
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.cartService = cartService;
    }

    @PostConstruct
    public void init() {
        log.info("[WebConfiguration] : UserManagementServiceImpl have been initialized");
    }

    /**
     * This method validate ip
     * If ip not valid, throw ValidationException
     * Find Cart by ip from argument
     * if cart was not found in database, throw NotFoundException
     * If User not exist in database by ip from argument return checkout without UserDTO
     * If User exist in database by ip from argument return checkout with CartDTO and UserDto
     *
     * @param ip user
     * @return Checkout
     */
    @Transactional
    @Override
    public Checkout checkout(String ip) {
        final CartDTO cart = cartService.getCart(ip);
        if (cart == null) {
            throw new NotFoundException("Cart was not found");
        }
        log.info("[UserManagementServiceImpl] : Cart was found successfully");
        if (!deviceRepository.existsByIp(ip)) {
            log.info("[UserManagementServiceImpl] : User not exists. Return checkout without user");
            return Checkout.builder()
                    .cart(cart)
                    .build();
        }
        log.info("[UserManagementServiceImpl] : User exists. Return checkout with user");
        final UserDTO user = deviceRepository.getByIp(ip).getUser().toDto();
        if (user == null) {
            throw new NotFoundException("User was not found");
        }
        return Checkout.builder()
                .cart(cart)
                .user(user)
                .build();
    }

    @Override
    public UserDTO addDevice(String ip, UserDTO userDTO) {
        if (!validation(userDTO.getEmail(), userDTO.getPhone())) {
            throw new ValidationException("Email or phone not valid");
        }
        log.info("[UserManagementServiceImpl] : User's email and phone is valid");
        Validator.dtoValidator(userDTO);
        log.info("[UserManagementServiceImpl] : UserDto is valid");
        if (!isDeviceExist(ip) && !isUserExist(userDTO.getEmail(), userDTO.getPhone())) {
            log.info("[UserManagementServiceImpl] : User not exists. Create new User.");
            final User user = userDTO.toEntity();
            user.addIP(Device.builder()
                    .ip(ip)
                    .build());
            return userRepository.save(user).toDto();
        }

        final Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
        log.info("[UserManagementServiceImpl] : User found successfully");
        if (!userOptional.isPresent()) {
            throw new NotFoundException("User not found.");
        }
        final User user = userOptional.get();
        if (!isDeviceExist(ip)) {
            log.info("[UserManagementServiceImpl] : User does not have ip in data base. Adding...");
            user.addIP(Device.builder()
                    .ip(ip)
                    .build());
        }
        return userRepository.save(user).toDto();
    }

    /**
     * This method validate input product invoiceId
     * Find Invoice entity in database
     * If invoice was not found in database, throw NotFoundException
     *
     * @param invoiceId invoice invoiceId
     * @return UserDto by invoice invoiceId from argument
     */
    @Override
    public UserDTO getUserByInvoiceId(Long invoiceId) {
        Validator.validateIds(invoiceId);
        final User user = userRepository.getUserByInvoiceId(invoiceId);
        if (user == null) {
            throw new NotFoundException(String.format("User by invoice invoiceId %s not found", invoiceId));
        }
        return user.toDto();
    }

    /**
     * @return all List of UserDto from database
     */
    @Override
    public List<UserDTO> getAllUser() {
        return userRepository.findAll().stream()
                .filter(Objects::nonNull)
                .map(User::toDto)
                .collect(Collectors.toList());
    }

    /**
     * This method validate input email and phone
     *
     * @param email any email
     * @param phone any phone
     * @return true if email and phone valid
     * @throws ValidationException if email or phone not valid
     */
    private boolean validation(String email, String phone) throws ValidationException {
        return Validator.emailIsValid(email) && Validator.numberIsValid(phone);
    }

    private boolean isUserExist(String email, String phone) {
        return userRepository.existsByEmail(email) && userRepository.existsByPhone(phone);
    }

    private boolean isDeviceExist(String ip) {
        return deviceRepository.existsByIp(ip);
    }

}
