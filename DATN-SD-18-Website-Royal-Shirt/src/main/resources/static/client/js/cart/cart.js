//Danh sách id cartDetail được chọn để checkout
var listProductIdChoice = [];

// // delete cart-detail khi status product = 1 and quantity product_detail = 0 and status product_detail = 1
// $(document).ready(function() {
//     $.ajax({
//         type: "DELETE",
//         url: "/rest/cart-detail/deleteCartDetailByQuantityAndStatusProduct",
//         success: function(response) {
//             console.log(response);
//         },
//         error: function(xhr, status, error) {
//             console.error("Error: " + error);
//         }
//     });
// });


// Xử lý sự kiện khi checkbox thay đổi trạng thái
$('.cart-checkbox').change(function () {
    var cartDetailId = $(this).val(); // Lấy ID của cartDetail từ giá trị của checkbox

    // Kiểm tra xem checkbox có được chọn hay không
    if ($(this).is(':checked')) {
        // Nếu được chọn, thêm ID vào listProductIdChoice (nếu chưa có)
        if (!listProductIdChoice.includes(cartDetailId)) {
            listProductIdChoice.push(cartDetailId);
        }
    } else {
        // Nếu không được chọn, loại bỏ ID khỏi listProductIdChoice (nếu có)
        var index = listProductIdChoice.indexOf(cartDetailId);
        if (index !== -1) {
            listProductIdChoice.splice(index, 1);
        }
    }

    console.log('Danh sách được chọn:', listProductIdChoice);
});

function saveListProductToCheckout() {
    if (listProductIdChoice.length === 0) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng chọn sản phẩm!'
        });
        return;
    } else {
        var listProduct = JSON.parse(localStorage.getItem("listProductIdChoice")) || {};

        listProduct.listProductIdChoice = listProductIdChoice;

        // Lưu lại dữ liệu vào Local Storage
        localStorage.setItem("listProductIdChoice", JSON.stringify(listProduct));

        window.location.href = "/checkout";
    }
}

// Hàm tính tổng tiền và định dạng thành tiền tệ VND
function calculateTotal() {
    var total = 0;
    var checkboxes = document.getElementsByClassName('cart-checkbox');
    var quantityInputs = document.getElementsByClassName('plus-minus-box');
    var priceElements = document.getElementsByClassName('price');

    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            var quantity = parseInt(quantityInputs[i].value);
            var priceText = priceElements[i].textContent;
            // Thay thế tất cả các dấu chấm bằng chuỗi rỗng để giữ lại toàn bộ số tiền
            var price = parseFloat(priceText.replace(/\./g, ''));
            total += quantity * price;
        }
    }

    // Hiển thị tổng tiền trong thẻ có id là "sumPrice" với định dạng tiền tệ VND
    document.getElementById('sumPrice').textContent = formatCurrency(total);
}

// Thêm sự kiện onchange cho mỗi checkbox để gọi hàm tính tổng
document.addEventListener('DOMContentLoaded', function () {
    var checkboxes = document.getElementsByClassName('cart-checkbox');
    for (var i = 0; i < checkboxes.length; i++) {
        checkboxes[i].addEventListener('change', calculateTotal);
    }
});

function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(amount);
}

async function decrementQuantity(productId) {
    var quantity = parseInt($("#quantity_" + productId).val());
    if (quantity > 1) {
        // Gửi yêu cầu AJAX
        await $.ajax({
            type: "POST",
            url: "/cart-detail/decrement/" + productId,
            contentType: "application/json",
            success: function (response) {
                console.log("Giảm số lượng thành công! (" + productId + " - 1)");
                $("#quantity_" + productId).val(quantity - 1);
                calculateTotal();
            },
            error: function (error) {
                console.error("Lỗi khi giảm số lượng:", error);
            }
        });
    }
}

async function incrementQuantity(productId) {
    var quantity = parseInt($("#quantity_" + productId).val());
    var maxQuantity = parseInt($('#quantity_' + productId).attr('max'));
    if (quantity < maxQuantity) {
        // Gửi yêu cầu AJAX
        await $.ajax({
            type: "POST",
            url: "/cart-detail/increment/" + productId,
            contentType: "application/json",
            success: function (response) {
                console.log("Tăng số lượng thành công! (" + productId + " + 1)");
                $("#quantity_" + productId).val(quantity + 1);
                calculateTotal();
            },
            error: function (error) {
                console.error("Lỗi khi tăng số lượng:", error);
            }
        });
    } else {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vượt số lượng tồn sản phẩm!'
        });
        return;
    }
}

// delete cartDetail by id
function deleteCartDetailItem(button) {
    var cartDetailId = button.getAttribute("data-id");
    $.ajax({
        type: "DELETE",
        url: "/rest/cart-detail/" + cartDetailId,
        contentType: "application/json",
        success: function(response) {
            console.log("Đã xóa CartDetail id = " + cartDetailId + " khỏi giỏ hàng !");
            // Xóa sản phẩm khỏi giao diện người dùng
            $('[data-id="' + cartDetailId + '"]').closest('tr').remove();
            // Hiển thị thông báo thành công
            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Xóa sản phẩm khỏi giỏ hàng thành công!'
            });
        },
        error: function(xhr, status, error) {
            // Xử lý lỗi nếu có
            console.error(xhr.responseText);
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Đã xảy ra lỗi khi xóa sản phẩm khỏi giỏ hàng.'
            });
        }
    });
}

