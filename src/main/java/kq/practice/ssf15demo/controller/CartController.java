package kq.practice.ssf15demo.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kq.practice.ssf15demo.model.Item;

@Controller
@RequestMapping(path = "/cart")
public class CartController {

    @Autowired
    @Qualifier("myredis")
    private RedisTemplate<String, String> template;

    public List<Item> getCart(HttpSession sess) {
        List<Item> cart = (List<Item>) sess.getAttribute("cart");
        if (null == cart) {
            cart = new LinkedList<>();
            sess.setAttribute("cart", cart);
        }
        return cart;
    }

    @GetMapping
    public ModelAndView getUserCart(@RequestParam String name, HttpSession sess) {

        ModelAndView mav = new ModelAndView("cart");
        HashOperations<String, String, String> hashOps = template.opsForHash();
        List<Item> cart = getCart(sess);

        if (template.hasKey(name)) {
            Map<String, String> list = hashOps.entries(name);
            for (String str : list.keySet()) {
                cart.add(new Item(str, Integer.parseInt(list.get(str))));
                System.out.println("%s, %s".formatted(str, list.get(str)));
            }
        } else {
            System.out.println("Creating new cart in redis");
        }

        mav.addObject("item", new Item());
        mav.addObject("cart", cart);
        sess.setAttribute("name", name);

        mav.setViewName("cart");

        return mav;
    }

    @PostMapping
    public ModelAndView postCart(@Valid @ModelAttribute Item item, BindingResult binding, HttpSession sess) {
        System.out.println(item);
        System.out.println(binding.hasErrors());

        ModelAndView mav = new ModelAndView("cart");
        List<Item> cart = getCart(sess);

        if (binding.hasErrors()) {
            mav.setStatus(HttpStatusCode.valueOf(400));
            mav.addObject("cart", cart);

            return mav;
        }

        String name = (String) sess.getAttribute("name");
        cart.add(item);

        HashOperations<String, String, String> hashOps = template.opsForHash();
        hashOps.put(name, item.getName(), item.getQuantity().toString());

        mav.addObject("item", new Item());
        mav.addObject("cart", cart);

        mav.setStatus(HttpStatusCode.valueOf(200));
        return mav;
    }

    @PostMapping(path = "/checkout")
    public String postCartCheckout(HttpSession sess) {
        // ModelAndView mav = new ModelAndView("cart");

        List<Item> cart = getCart(sess);
        System.out.printf("Checking out cart: %s\n", cart);

        String name = (String) sess.getAttribute("name");
        HashOperations<String, String, String> hashOps = template.opsForHash();
        for (String item : hashOps.entries(name).keySet()) {
            hashOps.delete(name, item);
        }

        sess.invalidate();

        // mav.addObject("item", new Item());
        // mav.setStatus(HttpStatusCode.valueOf(200));

        // mav.setViewName("index");

        return "redirect:/";
    }
}
