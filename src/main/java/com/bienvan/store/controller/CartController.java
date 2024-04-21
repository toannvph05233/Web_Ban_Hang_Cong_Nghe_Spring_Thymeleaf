package com.bienvan.store.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.bienvan.store.service.*;
import com.bienvan.store.service.oderService.OrderServiceSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bienvan.store.model.Cart;
import com.bienvan.store.model.Logger;
import com.bienvan.store.model.Order;
import com.bienvan.store.model.OrderItem;
import com.bienvan.store.model.Product;
import com.bienvan.store.model.Review;
import com.bienvan.store.model.User;
import com.bienvan.store.payload.response.MessageResponse;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    ICategoryService categoryServiceImpl;

    @Autowired
    OrderServiceSQL orderServiceSQL;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    ReviewService reviewService;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        // if(session.getAttribute("email") == null){
        // model.addAttribute("errMsg", "Vui lòng đăng nhập!");
        // return "index";
        // }
        Logger l = Logger.getInstance();
        l.log("success");

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        model.addAttribute("cart", cart);
        List<Product> products = productService.getProducts();

        Map<Long, Product> productMap = new HashMap<>();

        for (Product product : products) {
            productMap.put(product.getId(), product);
        }
        model.addAttribute("productMap", productMap);

        return "cart";
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(Product item, HttpSession session) {
        Map<String, Object> res = new HashMap<>();

        Logger l = Logger.getInstance();
        l.remove();

        Product product = productService.getProductById(item.getId()).get();
        if (product != null) {
            product.setQuantity(item.getQuantity());
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                cart = new Cart();
            }
            cart.addItem(product);
            session.setAttribute("cart", cart);
            res.put("code", 0);
            res.put("message", "Thêm sản phẩm vào giỏ hàng thành công");
            res.put("sizeCart", cart.getCartItems().size());
        } else {
            // @ModelAttribute("item")
            res.put("code", 1);
            res.put("message", "Thêm sản phẩm vào giỏ hàng thất bại");
        }

        return ResponseEntity.ok(res);
    }

    @GetMapping("/delete/{id}")
    public String deleteInCart(@PathVariable Long id, HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");
        cart.removeItem(id);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");

        return "redirect:/cart";
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCart(@RequestBody List<Product> cartItemUpdates, HttpSession session) {
        Map<String, Object> res = new HashMap<>();
        Cart cart = (Cart) session.getAttribute("cart");
        for (Product update : cartItemUpdates) {
            Long productId = update.getId();
            int newQuantity = update.getQuantity();

            Product product = cart.getItemById(productId);
            if (product != null) {
                if (newQuantity > 0) {
                    product.setQuantity(newQuantity);
                } else {
                    // cart.removeItem(productId);
                }
            }
        }
        res.put("code", 0);
        res.put("message", "Cập nhật giỏ hàng thành công");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> payCart(@RequestBody Order info, HttpSession session, Model model) {

        if ((Long) session.getAttribute("id") == null) {
            return ResponseEntity.ok(new MessageResponse(1, "Vui lòng đăng nhập", null));
        }
        if (info.getAddress().isEmpty() || info.getName().isEmpty() || info.getPhone().isEmpty()) {
            return ResponseEntity.ok(new MessageResponse(1, "Vui lòng điền đầy đủ thông tin", null));
        }

        Cart cart = (Cart) session.getAttribute("cart");

        User buyer = userService.getUserById((Long) session.getAttribute("id"));

        Order order = new Order();
        order.setStatus("Đang chờ");
        order.setPhone(info.getPhone());
        order.setName(info.getName());
        order.setAddress(info.getAddress());
        order.setCreate_at(new Date(System.currentTimeMillis()));
        order.setTotal(cart.calculateTotalPrice());
        order.setUserId(buyer);
        order.setPayment_method("COD");

        orderServiceSQL.createOrder(order);
        // Create an OrderItem
        for (Map.Entry<Long, Product> entry : cart.getCartItems().entrySet()) {
            Long key = entry.getKey();
            Product value = entry.getValue();
            OrderItem orderItem = new OrderItem();
            orderItem.setPurchase_price(value.getPrice());
            orderItem.setQuantity(value.getQuantity());

            Product product = productService.getProductById(key).get();
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItemService.createOrderItem(orderItem);
        }

        List<Order> orders = orderServiceSQL.getAllOrders();

        model.addAttribute("orders", orders);

        session.removeAttribute("cart");

        return ResponseEntity.ok(new MessageResponse(0, "Thanh toán thành công", null));
    }

    @GetMapping("/check")
    public String checkCart(HttpSession session, Model model) {
        Long idU = (Long) session.getAttribute("id");
        if ((Long) session.getAttribute("id") == null) {
            return "404";
        }
        // User userEntity = userService.getUserById(Long.parseLong("1")).get();
        // System.out.println(userEntity.getName());

        List<Order> orders = orderServiceSQL.getAllOrders();

        List<Order> ordersByidU = new ArrayList<>();

        for (Order order : orders) {
            if (order.getUserId() != null && order.getUserId().getId() == idU) {
                ordersByidU.add(order);
            }
        }

        model.addAttribute("orders", orders);

        return "order-check";
    }

    @GetMapping("/order-detail/{id}")
    public String orderDetail(@PathVariable Long id, HttpSession session, Model model) {

        Order order = orderServiceSQL.getOrderById(id).get();

        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrder(order);

        model.addAttribute("orderItems", orderItems);
        model.addAttribute("order", order);

        return "order-detail";
    }

    @GetMapping("/review/{id}")
    public String orderReview(@PathVariable Long id, HttpSession session, Model model) {
        return "order-review";
    }

    @PostMapping("/review/{id}")
    public ResponseEntity<?> oReview(@PathVariable Long id, @RequestParam("rating") int rating,
            @RequestParam("comment") String comment, HttpSession session, Model model) {
        Long idU = (Long) session.getAttribute("id");
        if (idU == null) {
            return ResponseEntity.ok(new MessageResponse(1, "Vui lòng login", null));
        }

        Order order = orderServiceSQL.getOrderById(id).get();
        order.setReview(true);
        orderServiceSQL.createOrder(order);
        User user = userService.getUserById(idU);
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrder(order);
        for (OrderItem orderItem : orderItems) {
            Review review = new Review();
            review.setComment(comment);
            review.setRating(rating);
            review.setCreate_at(new Date(System.currentTimeMillis()));
            review.setProduct(orderItem.getProduct());
            review.setUser(user);
            reviewService.add(review);
        }

        return ResponseEntity.ok(new MessageResponse(0, "Đánh giá thành công", null));
    }
}
