package org.consoleApp.launch;

import jakarta.annotation.PostConstruct;
import org.consoleApp.dataFilling.composite.DataServiceComposite;
import org.consoleApp.menu.composite.MenuComposite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LaunchApp {
    private final DataServiceComposite serviceComposite;
    private final MenuComposite menuComposite;

    @Autowired
    public LaunchApp(DataServiceComposite serviceComposite, MenuComposite menuComposite) {
        this.serviceComposite = serviceComposite;
        this.menuComposite = menuComposite;
    }

    @PostConstruct
    public void launch(){
        serviceComposite.generateDataAndPopulateDB();

        menuComposite.execute();
    }
}