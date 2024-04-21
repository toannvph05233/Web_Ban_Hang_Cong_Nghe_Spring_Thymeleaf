package com.bienvan.store.command_Command;

import com.bienvan.store.model.Order;

public interface OrderCommand {
//    Controller nhận yêu cầu HTTP để tạo mới đơn hàng và
//    tạo ra một Command tương ứng để thực hiện thao tác tạo
//    đơn hàng. Các Command được thực hiện bởi các lớp cụ
//    thể như CreateOrderCommand, mỗi lớp có thể triển khai
//    một loạt các thao tác cụ thể. Điều này giúp tách biệt
//    logic xử lý yêu cầu và logic thực thi, tạo ra mã dễ bảo
//    trì và tái sử dụng.
    Order execute();
}

