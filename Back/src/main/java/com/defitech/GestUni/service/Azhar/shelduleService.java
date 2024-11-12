package com.defitech.GestUni.service.Azhar;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class shelduleService {

    @Autowired
    private SeanceServices seanceServices;
    @Autowired
    private PermissionService permissionService;

    @PostConstruct
    public void init() {
        seanceServices.checkAndCloseSeances();
        permissionService.updatePermissionsAutomatically();
    }

}
