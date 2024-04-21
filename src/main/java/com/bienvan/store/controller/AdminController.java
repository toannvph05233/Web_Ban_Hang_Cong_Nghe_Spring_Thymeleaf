package com.bienvan.store.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.bienvan.store.service.*;
import com.bienvan.store.service.oderService.OrderServiceSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bienvan.store.dto.UserDto;
import com.bienvan.store.model.Brand;
import com.bienvan.store.model.Category;
import com.bienvan.store.model.Color;
import com.bienvan.store.model.Order;
import com.bienvan.store.model.OrderItem;
import com.bienvan.store.model.Product;
import com.bienvan.store.model.Role;
import com.bienvan.store.model.User;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    ProductService productService;

    @Autowired
    ICategoryService categoryServiceImpl;

    @Autowired
    IBrandService brandServiceImpl;

    @Autowired
    ColorService colorService;

    @Autowired
    OrderServiceSQL orderServiceSQL;

    @Autowired
    OrderItemService orderItemService;

    @GetMapping(value = { "/dashboard", "" })
    public String getAdminPage(Model model, HttpSession session) {
        if (session.getAttribute("ROLE_ADMIN") == null) {
            return "404";
        }
        List<Product> products = productService.getProducts();
        List<Order> orders = orderServiceSQL.getAllOrders();
        List<UserDto> users = userService.getAllUsers();
        double totalYear = 0;
        int pending = 0;
        int trading = 0;
        int delivered = 0;
        int canceled = 0;

        for (Order o : orders) {
            if (o.getStatus().toString().equals("Đang chờ")) {
                pending++;
            }
            if (o.getStatus().toString().equals("Đang giao")) {
                trading++;
            }
            if (o.getStatus().toString().equals("Đã giao")) {
                delivered++;
                totalYear += o.getTotal();
                // System.out.println(totalYear);
            }
            if (o.getStatus().toString().equals("Đã hủy")) {
                canceled++;
            }
        }

        model.addAttribute("totalYear", totalYear);
        model.addAttribute("pending", pending);
        model.addAttribute("trading", trading);
        model.addAttribute("delivered", delivered);
        model.addAttribute("canceled", canceled);
        model.addAttribute("totalUser", users.size());
        model.addAttribute("totalOrder", orders.size());
        model.addAttribute("totalProduct", products.size());

        return "/admin/dashboard";
    }

    @GetMapping(value = { "/user-manager" })
    public String getUserManager(ModelMap model) {
        List<User> users = userService.findAll();
        List<Role> roles = roleService.findAll();

        model.addAttribute("users", users);
        model.addAttribute("roles", roles);

        return "/admin/user-manager";
    }

    @GetMapping(value = { "/category-manager" })
    public String getCategoryManager(ModelMap model) {
        List<Category> list = categoryServiceImpl.findAll();

        model.addAttribute("categories", list);
        return "/admin/category-manager";
    }

    @GetMapping(value = { "/product-manager" })
    public String getProductManager(ModelMap model) {
        List<Product> list = productService.getProductsForHomePage();
        List<Category> categories = categoryServiceImpl.findAll();
        List<Brand> brands = brandServiceImpl.findAll();
        List<Color> colors = colorService.getColors();

        model.addAttribute("products", list);
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("colors", colors);

        return "/admin/product-manager";
    }

    @GetMapping(value = { "/order-manager" })
    public String getOrderManager(ModelMap model) {
        List<OrderItem> orderItems = orderItemService.getAllOrderItems();
        List<Order> orders = orderServiceSQL.getAllOrders();

        model.addAttribute("orderItems", orderItems);
        model.addAttribute("orders", orders);
        return "/admin/order-manager";
    }

    @GetMapping("/order-detail/{id}")
    public String orderDetail(@PathVariable Long id, HttpSession session, Model model) {

        Order order = orderServiceSQL.getOrderById(id).get();

        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrder(order);

        model.addAttribute("orderItems", orderItems);
        model.addAttribute("order", order);

        return "/admin/order-detail";
    }
}
