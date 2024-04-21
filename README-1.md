# Project Java Technology

## 1. Sơ lược tổng quan về hệ thống

### version:
+ Visual Studio Code
+ JDK 17
+ Spring 2.7.10

### Build Web Service Using:
+ backend: Spring Boot 2.7.10
+ frontend: Thymeleaf, only html, css, awesome, bootstrap
+ spring security
+ spring data jpa
+ mysql database
+ lombok

### Mô tả:
+ Web bán giầy, đối tượng sử dụng :
    + Quản lý: CRUD tài khoản, sản phẩm, danh mục sản phẩm trong hệ thống.
    + Khách hàng: xem, tìm kiếm sản phẩm, tạo giỏ hàng, thanh toán hóa đơn.

+ Mô hình và phương pháp phát triển:
    + Sử dụng mô hình MVC để dễ phát triển và bảo trì.

+ Cấu trúc mã:
    + Project sẽ chia các package với các tính năng tương ứng, ví dụ như package controler sẽ có nhiệm vụ xử lý các logic hệ thống, package config có nhiệm vụ cấu hình các cài đặt cho hệ thống,...

### Tính năng:
+ phân quyền admin, user
+ bảo mật: có một số trang người dùng đăng nhập rồi mới cho vào
+ quản lí sản phẩm, loại sản phẩm, tài khoản(CRUD)
+ xem sản phẩm, xem chi tiết
+ quản lí giỏ hàng (thêm, xóa, sửa)
+ tạo đơn hàng
+ tìm kiếm sản phẩm
+ phân trang
#### Admin section:
+ quản lý sản phẩm
+ quản lý danh mục sản phẩm
+ quản lý tài khoản
#### User section:
+ xem trang sản phẩm
+ tìm kiếm sản phẩm
+ xem chi tiết sản phẩm
+ quản lý giỏ hàng

### Các tính năng sẽ phát triển trong tương lai:
+ quên mật khẩu
+ thanh toán trực tuyến