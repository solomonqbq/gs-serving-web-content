package com.bm;

import net.sf.ehcache.Cache;
import org.mapdb.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentMap;

@Controller
@CacheConfig(cacheNames = "p")
public class GreetingController {

    @Autowired
    private  CacheManager cacheManager;

    @Resource(name = "mapdb")
    private ConcurrentMap map;

    @Cacheable
    private Product getProduct(){
        System.out.println(map.get("something"));
        Product p = null;
        p=cacheManager.getCache("p").get("pp")==null?null:(Product) cacheManager.getCache("p").get("pp").get();
        if (p==null){
            p = createProduct();
            cacheManager.getCache("p").putIfAbsent("pp",p);
        }
            return p;
//        return createProduct();

    }

    @CachePut(value = "p",key = "pp")
    private Product createProduct() {
        return new Product();
    }

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute(getProduct());
        return "greeting";
    }

    @RequestMapping("/aa")
    public String aa(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute(createProduct());
        return "greeting";
    }

}
class Product implements Serializable{
    BigDecimal price = new BigDecimal(78.5);

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}