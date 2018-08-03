package com.itheima.web.controller;

import com.itheima.domain.ModelPage;
import com.itheima.domain.Product;
import com.itheima.service.JDService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
public class JDContoller {
    @Resource
    private JDService jdService;

    @RequestMapping("/jd")
    public String selectItem(String queryString,
                             String catalog_name,
                             String price,
                             @RequestParam(defaultValue = "1") String sort,
                             @RequestParam(defaultValue = "1",value = "page") Integer curPage,
                             @RequestParam(defaultValue = "30") Integer curCount,
                             Model model){
        try {
            ModelPage<Product> select = jdService.select(queryString, catalog_name, price, sort, curPage, curCount);
            model.addAttribute("queryString",queryString);
            model.addAttribute("catalog_name",catalog_name);
            model.addAttribute("price",price);
            model.addAttribute("sort",sort);
            model.addAttribute("result",select);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "product_list";
    }
}
