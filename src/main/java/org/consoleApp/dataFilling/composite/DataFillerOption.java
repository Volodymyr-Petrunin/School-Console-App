package org.consoleApp.dataFilling.composite;

import org.consoleApp.dataFilling.DataFiller;

import java.util.List;

public class DataFillerOption implements DataFiller {
    private List<DataFiller> dataFillers;

    public DataFillerOption(List<DataFiller> dataFillers) {
        this.dataFillers = dataFillers;
    }

    @Override
    public void fillData() {
        for (DataFiller dataFiller : dataFillers){
            dataFiller.fillData();
        }
    }
}
