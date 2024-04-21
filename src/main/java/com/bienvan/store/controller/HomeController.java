package com.bienvan.store.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import com.bienvan.store.service.*;
import com.bienvan.store.service.oderService.OrderServiceSQL;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bienvan.store.model.Brand;
import com.bienvan.store.model.Category;
import com.bienvan.store.model.Color;
import com.bienvan.store.model.PasswordResetToken;
import com.bienvan.store.model.Product;
import com.bienvan.store.model.Review;
import com.bienvan.store.model.User;
import com.bienvan.store.repository.PasswordResetTokenRepository;

@Controller
public class HomeController {
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
    IBrandService brandServiceImpl;

    @Autowired
    ColorService colorService;

    @Autowired
    EmailService emailService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @GetMapping(value = { "", "/", "/home" })
    public String getHomePage(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        List<Product> ps = productService.getProductsForHomePage();
        List<Category> categories = categoryServiceImpl.findAll();
        List<Brand> brands = brandServiceImpl.findAll();
        List<Color> colors = colorService.getColors();
        // int pageNumber = (page > 0) ? (page - 1) : 0; // Điều chỉnh số trang
        // Pageable pageable = PageRequest.of(pageNumber, size);
        // Page<Product> products = productService.findAll(pageable);
        // model.addAttribute("products", ps);
        // model.addAttribute("ps", ps);

        session.setAttribute("brands", brands);
        session.setAttribute("colors", colors);
        session.setAttribute("products", ps);
        session.setAttribute("categories", categories);

        return "index";
    }

    @PostMapping(value = { "", "/", "/home" })
    public String searchProducts(@RequestParam(required = false) String color,
            @RequestParam(required = false, defaultValue = "0-999999999") String priceRange,
            @RequestParam(name = "category", required = false) Long categoryId,
            @RequestParam(required = false) String brand, HttpSession session) {
        Brand byBrand = null;
        Color byColor = null;
        if (color.isEmpty()) {
            color = null;
        } else {
            byColor = colorService.findByName(color);
        }
        if (brand.isEmpty()) {
            brand = null;
        } else {
            byBrand = brandServiceImpl.findByName(brand);
        }

        String[] arr = priceRange.split("-");
        Double minPrice = Double.parseDouble(arr[0]);
        Double maxPrice = Double.parseDouble(arr[1]);
        List<Product> products = new ArrayList<>();
        if (categoryId != null) {
            Category category = categoryServiceImpl.findById(categoryId);
            if (category != null) {
                products = productService.search(minPrice, maxPrice, byBrand, byColor, category);
            }
        } else {
            products = productService.search(minPrice, maxPrice, byBrand, byColor, null);
        }

        session.setAttribute("products", products);

        return "index";
    }

    @GetMapping(value = { "/profile" })
    public String getProfilePage(Model model, HttpSession session) {
        if ((Long) session.getAttribute("id") == null) {
            return "404";
        }
        // List<UserDto> userList = userService.getAllUsers();
        // model.addAttribute("users", userList);
        return "profile";
    }

    @PostMapping(value = { "/profile" })
    public String postProfilePage(@RequestParam("password") String pw, @RequestParam("new-password") String npw,
            Model model, HttpSession session) {
        if ((Long) session.getAttribute("id") == null) {
            return "404";
        }
        User user = userService.getUserById((Long) session.getAttribute("id"));
        if (userService.checkLogin(user.getEmail(), pw)) {
            user.setPassword(passwordEncoder.encode(npw));
            userService.createUser(user);
            model.addAttribute("success", "Đã đổi password thành công");
        } else {
            model.addAttribute("error", "Password sai");
        }
        return "profile";
    }

    // @PutMapping(value = { "/profile" })
    // public String putProfilePage(@RequestParam("name") String name, @RequestParam("gender") String gender,
    //         Model model, HttpSession session) {
    //     if ((Long) session.getAttribute("id") == null) {
    //         return "404";
    //     }
    //     User user = userService.getUserById((Long) session.getAttribute("id"));
    //     if (name != null && gender != null) {
    //         user.setGender(gender);
    //         user.setName(name);
    //         model.addAttribute("success", "Đã thông tin thành công");
    //     } else {
    //         model.addAttribute("error", "Lỗi ròi");
    //     }
    //     return "profile";
    // }

    @GetMapping(value = { "/login" })
    public String login(Model model) {
        // List<UserDto> userList = userService.getAllUsers();
        // model.addAttribute("users", userList);
        return "login";
    }

    @GetMapping(value = { "/product-detail/{id}" })
    public String getProductDetailPage(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id).orElse(null);
        List<Review> reviews = reviewService.findByProduct(product);

        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("reviews", reviews);
            return "product-detail";
        } else {
            // Xử lý trường hợp không tìm thấy sản phẩm
            // Trở về trang chính hoặc trang thông báo lỗi tùy ý
            return "redirect:/";
        }
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model, HttpSession session) {
        if ((String) session.getAttribute("email") != null) {
            return "redirect:/";
        }
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPasswordForm(
            @RequestParam("email") String email,
            RedirectAttributes redirectAttributes) {

        User user = userService.findByEmail(email);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Email không tồn tại.");
            return "redirect:/forgot-password";
        }

        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setToken(RandomStringUtils.randomAlphanumeric(20));
        token.setExpirationDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60)); // 1 hour

        passwordResetTokenRepository.save(token);

        String resetPasswordUrl = "http://localhost:8080/reset-password?token=" + token.getToken();

        // Send email to user
        emailService.sendEmail(user.getEmail(), "Đặt lại mật khẩu",
                "Vui lòng truy cập vào liên kết sau để đặt lại mật khẩu của bạn: " + resetPasswordUrl);

        redirectAttributes.addFlashAttribute("success",
                "Chúng tôi đã gửi liên kết đặt lại mật khẩu cho bạn qua email.");

        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(
            @RequestParam("token") String token,
            Model model, HttpSession session) {
        if ((String) session.getAttribute("email") != null) {
            return "redirect:/";
        }
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if (passwordResetToken == null || passwordResetToken.isExpired()) {
            model.addAttribute("error", "Token đã hết hạn!");
            return "reset-password";
        }

        model.addAttribute("token", token);

        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPasswordForm(
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam("token") String token,
            RedirectAttributes redirectAttributes, Model model) {

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if (passwordResetToken == null) {
            model.addAttribute("error", "passwordResetToken = null");
            return "reset-password";
        }

        if (passwordResetToken.isExpired()) {
            model.addAttribute("error", "Token đã hết hạn!");
            return "reset-password";
        }

        User user = passwordResetToken.getUser();

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu và mật khẩu xác nhận không khớp.");
            return "reset-password";
        }

        user.setPassword(passwordEncoder.encode(password));
        userService.createUser(user);

        redirectAttributes.addFlashAttribute("success", "Mật khẩu đã được đặt lại thành công.");
        return "redirect:/forgot-password";
    }

    public List<Product> geProductsByIsDeletedFalse(List<Product> products) {
        return products.stream()
                .filter(product -> !product.isDeleted())
                .collect(Collectors.toList());
    }

    @GetMapping("/category={id}")
    public String CategoryPage(@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "8") int size,
            @PathVariable Long id,
            Model model) {
        Category category = categoryServiceImpl.findById(id);
        Page<Product> productPage = getPageOfProducts(page, size, category);
        model.addAttribute("products", geProductsByIsDeletedFalse(productPage.getContent()));
        model.addAttribute("category", category);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "category-page";
    }

    private Page<Product> getPageOfProducts(int page, int size, Category category) {
        return productService.findProductsByCategory(category, PageRequest.of(page, size));
    }

    @GetMapping("/brand={id}")
    public String BrandPage(@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "8") int size, @PathVariable Long id, Model model) {
        Brand brand = brandServiceImpl.findById(id);
        Page<Product> productPage = productService.findProductsByBrand(brand, PageRequest.of(page, size));
        model.addAttribute("products", geProductsByIsDeletedFalse(productPage.getContent()));
        model.addAttribute("brand", brand);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "brand-page";
    }
}
