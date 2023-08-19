package org.consoleApp.dataFilling.composite;

import org.consoleApp.services.DataFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataServiceComposite implements DataFiller {
    private final List<DataFiller> services;

    @Autowired
    public DataServiceComposite(List<DataFiller> services) {
        this.services = services;
    }

    @Override
    public void generateDataAndPopulateDB() {
        services.forEach(DataFiller::generateDataAndPopulateDB);
    }
}
