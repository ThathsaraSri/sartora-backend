package com.sartora.Sartora_backend.repository;

import com.sartora.Sartora_backend.entity.LocalUser;
import com.sartora.Sartora_backend.entity.WebOrder;
import org.apache.catalina.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface WebOrderRepository extends ListCrudRepository <WebOrder, Long>{
    List<WebOrder> findByUser (LocalUser user);


}
