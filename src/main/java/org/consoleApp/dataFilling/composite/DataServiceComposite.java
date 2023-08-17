package org.consoleApp.dataFilling.composite;

import org.consoleApp.services.ServicesDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataServiceComposite implements ServicesDAOImpl {
    private final List<ServicesDAOImpl> services;

    @Autowired
    public DataServiceComposite(List<ServicesDAOImpl> services) {
        this.services = services;
    }

    @Override
    public void generateDataAndPopulateDB() {
        services.forEach(ServicesDAOImpl::generateDataAndPopulateDB);
    }
}
