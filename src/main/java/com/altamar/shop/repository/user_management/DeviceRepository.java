package com.altamar.shop.repository.user_management;


import com.altamar.shop.entity.user_management.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    @Query("select d from Device d where d.ip = ?1")
    Device getByIp(String ip);

    boolean existsByIp(String ip);

}
