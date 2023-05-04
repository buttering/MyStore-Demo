package com.csu.mypetstore.api.controller;

import com.csu.mypetstore.api.dao.Address;
import com.csu.mypetstore.api.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("address")
public class AddressController {
    @Autowired
    AddressMapper addressMapper;

    @GetMapping("all")
    public List<Address> getAllAddress(){
        return addressMapper.selectList(null);
    }
}
