package org.consoleApp.dataFilling.composite;

import org.consoleApp.services.ServicesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataServiceComposite implements ServicesDAO {
    private final List<ServicesDAO> services;

    @Autowired
    public DataServiceComposite(List<ServicesDAO> services) {
        this.services = services;
    }

    @Override
    public void generateDataAndPopulateDB() {
        services.forEach(ServicesDAO::generateDataAndPopulateDB);
    }
}
