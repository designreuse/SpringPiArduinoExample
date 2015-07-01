package com.master.controller;

import com.master.service.DataStorageService;
import com.master.service.I2CService;
import com.master.utils.StateDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Daniel Pardo Ligorred
 */
@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private DataStorageService dataStorageService;
    @Autowired
    private I2CService i2CService;

    /**
     * Show home page.
     *
     * @param model
     * @return
     */
    @RequestMapping
    public String showHome(ModelMap model) {

        int[] data = this.i2CService.retrieveInfo();

        model.addAttribute("stateCode", data[0]);
        model.addAttribute("stateMessage", StateDecoder.getStatusPhrase(data[0]));

        model.addAttribute("waterTempValue", data[1]);
        model.addAttribute("humidityValue", data[2]);

        String plottyUrl = this.dataStorageService.getStringValue("plottyUrl");

        if (!plottyUrl.isEmpty()) {

            model.addAttribute("plottyLast", this.dataStorageService.getStringValue("plottyLast"));
            model.addAttribute("plottyUrl", plottyUrl);
        }

        model.addAttribute("log", this.dataStorageService.getLog());

        return "home";
    }

}