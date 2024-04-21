package com.bienvan.store.controller;


import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import com.bienvan.store.service.*;
import com.bienvan.store.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.bienvan.store.dto.*;
import com.bienvan.store.model.*;
import com.bienvan.store.payload.request.ProductInput;
import com.bienvan.store.payload.response.MessageResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    ICategoryService categoryServiceImpl;

    @Autowired
    IBrandService brandServiceImpl;

    @Autowired
    ColorService colorService;

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<?> getListProduct() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Optional<Product> optional = productService.getProductById(id);

        if (optional.isPresent()) {
            return ResponseEntity.ok(new MessageResponse(0, "Find product successfully", null));
        }
        return ResponseEntity.ok(new MessageResponse(1, "Find product failed", null));
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@ModelAttribute @Valid ProductInput productDto, BindingResult bindingResult)
            throws IOException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .ok(new MessageResponse(1, bindingResult.getAllErrors().get(0).getDefaultMessage(), null));
        }

        String fileType = productDto.getImage().getContentType();
        if (fileType != null && !fileType.startsWith("image/")) {
            return ResponseEntity.ok(new MessageResponse(1, "Vui lòng chọn tệp tin dạng hình ảnh", null));
        }

        if (fileType == null) {
            return ResponseEntity.ok(new MessageResponse(1, "Vui lòng thêm hình ảnh", null));
        }

        User checkUser = userService.getUserById(productDto.getUser_id());
        if (checkUser == null) {
            return ResponseEntity.ok(new MessageResponse(1, "Vui lòng chọn chính xác người thêm sản phẩm", null));
        }

        Category category = categoryServiceImpl.getCategoryById(productDto.getCategory_id());
        if (category == null) {
            return ResponseEntity.ok(new MessageResponse(1, "Vui lòng chọn loại sản phẩm", null));
        }
        Brand brand = brandServiceImpl.findById(productDto.getBrand_id());
        if (brand == null) {
            return ResponseEntity.ok(new MessageResponse(1, "Vui lòng chọn hãng", null));
        }

        Color color = colorService.findById(productDto.getColor_id());
        if (color == null) {
            return ResponseEntity.ok(new MessageResponse(1, "Vui lòng chọn màu", null));
        }

        User user = checkUser;
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setImage(productDto.getImage().getOriginalFilename());
        product.setUser(user);
        product.setCategory(category);
        product.setBrand(brand);
        product.setColor(color);
        Product created = productService.createProduct(product);

        String staticFolderPath = "src/main/resources/static/img/product/" + created.getId() + "/";
        Path folderPath = Paths.get(staticFolderPath);
        try {
            Files.createDirectories(folderPath);
            System.out.println("Folder created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to create folder.");
        }

        Path path = Paths.get(staticFolderPath + product.getImage());
        Files.write(path, productDto.getImage().getBytes());

        return ResponseEntity.ok(new MessageResponse(0, "Thêm sản phẩm thành công", null));
    }

    @PutMapping // update
    public ResponseEntity<?> updateCategory(@ModelAttribute @Valid ProductInput productDto,
            BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .ok(new MessageResponse(1, bindingResult.getAllErrors().get(0).getDefaultMessage(), null));
        }

        String fileType = productDto.getImage().getContentType();
        if (fileType != null && !fileType.startsWith("image/")) {
            return ResponseEntity.ok(new MessageResponse(1, "Vui lòng chọn tệp tin dạng hình ảnh", null));
        }

        if (fileType == null) {
            return ResponseEntity.ok(new MessageResponse(1, "Vui lòng thêm hình ảnh", null));
        }

        Optional<Product> optional = productService.getProductById(productDto.getId());

        if (optional.isPresent()) {

            // User user = userService.getUserById(productDto.getUser_id());
            // if (user == null) {
            // return ResponseEntity.ok(new MessageResponse(1, "Vui lòng chọn chính xác
            // người thêm sản phẩm", null));
            // }

            Category category = categoryServiceImpl.getCategoryById(productDto.getCategory_id());
            if (category == null) {
                return ResponseEntity.ok(new MessageResponse(1, "Vui lòng chọn loại sản phẩm", null));
            }

            Brand brand = brandServiceImpl.findById(productDto.getBrand_id());
            if (brand == null) {
                return ResponseEntity.ok(new MessageResponse(1, "Vui lòng chọn hãng", null));
            }

            Color color = colorService.findById(productDto.getColor_id());
            if (color == null) {
                return ResponseEntity.ok(new MessageResponse(1, "Vui lòng chọn màu", null));
            }

            Product existing = optional.get();
            existing.setName(productDto.getName());
            existing.setDescription(productDto.getDescription());
            existing.setPrice(productDto.getPrice());
            existing.setQuantity(productDto.getQuantity());
            existing.setImage(productDto.getImage().getOriginalFilename());
            // existing.setUser(user);
            existing.setCategory(category);
            existing.setBrand(brand);
            existing.setColor(color);

            productService.createProduct(existing);

            String staticFolderPath = "src/main/resources/static/img/product/" + existing.getId() + "/";
            Path folderPath = Paths.get(staticFolderPath);
            try {
                Files.createDirectories(folderPath);
                System.out.println("Folder created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to create folder.");
            }

            Path path = Paths.get(staticFolderPath + existing.getImage());
            Files.write(path, productDto.getImage().getBytes());

            return ResponseEntity.ok(new MessageResponse(0, "Cập nhật sản phẩm thành công", null));
        }
        return ResponseEntity.ok(new MessageResponse(1, "Không tìm thấy id sản phẩm này", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<Product> optional = productService.getProductById(id);

        if (optional.isPresent()) {
            Product product = optional.get();
            product.setDeleted(true);
            productService.createProduct(product);
            return ResponseEntity.ok(new MessageResponse(0, "Xóa sản phẩm thành công", null));

        }
        return ResponseEntity.ok(new MessageResponse(1, "Xóa sản phẩm thất bại", null));

    }

    // @GetMapping("/search") // tìm kiếm tên theo keyword
    // public ResponseEntity<?> searchUser(@RequestParam(name = "keyword", required
    // = false) String name) {
    // List<UserDto> users = userService.searchUser(name);
    // return ResponseEntity.ok(users);
    // }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("keyword") String keyword, Model model) {
        List<Product> products = productService.findByNameContaining(keyword);
        model.addAttribute("products", products);
        return "search_results";
    }
}
