package org.consoleApp.dataFilling.composite;

import org.consoleApp.dataFilling.DataFiller;

import java.util.List;

public class DataFillerComposite implements DataFiller {
    private List<DataFiller> dataFillers;

    public DataFillerComposite(List<DataFiller> dataFillers) {
        this.dataFillers = dataFillers;
    }

    @Override
    public void fillData() {
        for (DataFiller dataFiller : dataFillers){
            dataFiller.fillData();
        }
    }
}
