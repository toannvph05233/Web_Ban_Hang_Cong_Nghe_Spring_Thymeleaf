// Lấy đường dẫn hiện tại
const currentPath = window.location.pathname;

// Kiểm tra xem đường dẫn hiện tại có phù hợp với li nào trên thanh điều hướng
if (currentPath === '/admin' || currentPath === '/admin/dashboard') {
    document.getElementById('dashboard').classList.add('active');
} else if (currentPath.includes("/admin/order")) {
    document.getElementById('order-list').classList.add('active');
}else if (currentPath === '/admin/product-manager') {
    document.getElementById('product-list').classList.add('active');
}else if (currentPath === '/admin/category-manager') {
    document.getElementById('category-list').classList.add('active');
}else if (currentPath === '/admin/user-manager') {
    document.getElementById('user-list').classList.add('active');
}
