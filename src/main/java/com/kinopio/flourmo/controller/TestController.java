package com.kinopio.flourmo.controller;

import com.kinopio.flourmo.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600) //支持跨域
@RestController
@RequestMapping(path = "/test", produces = {"application/json;charset=UTF-8"})
public class TestController {

     @Autowired
     private UnitService unitService;

     @GetMapping("/unit")
     public String getUnitName(@RequestParam(value = "unitCode", required = true) String unitCode) {
         return unitService.getUnitName(unitCode);
     }

}
