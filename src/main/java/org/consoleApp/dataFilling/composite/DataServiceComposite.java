package org.consoleApp.dataFilling.composite;

import org.consoleApp.services.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataServiceComposite implements Services {
    private final List<Services> services;

    @Autowired
    public DataServiceComposite(List<Services> services) {
        this.services = services;
    }

    @Override
    public void generateDataAndPopulateDB() {
        services.forEach(Services::generateDataAndPopulateDB);
    }
}
