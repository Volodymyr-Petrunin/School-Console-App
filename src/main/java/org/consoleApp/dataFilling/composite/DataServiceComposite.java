package org.consoleApp.dataFilling.composite;

import org.consoleApp.services.DataFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataServiceComposite implements DataFiller {
    private final List<DataFiller> dataFillers;

    @Autowired
    public DataServiceComposite(List<DataFiller> dataFillers) {
        this.dataFillers = dataFillers;
    }

    @Override
    public void generateDataAndPopulateDB() {
        dataFillers.forEach(DataFiller::generateDataAndPopulateDB);
    }
}
