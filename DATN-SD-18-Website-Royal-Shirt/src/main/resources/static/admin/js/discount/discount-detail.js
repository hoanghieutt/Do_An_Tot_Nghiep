var initialDiscountName = $("#discountName").val().trim();
//Show form
$(document).ready(function () {
    $('#sampleTableNoneDiscount').dataTable();

    $('#showFormNoneDiscount').click(function () {
        var discountId = $("#discountForm").attr("discount-id");
        $.ajax({
            type: "GET",
            url: "/admin/rest/discount/" + discountId,
            contentType: "application/json",
            success: function (response) {
                var discountEndDate = moment(response.endDate, 'YYYY-MM-DD');
                var currentDate = moment();

                if (currentDate.isSame(discountEndDate, 'day') || currentDate.isBefore(discountEndDate, 'day')) {
                    $('#NoneDiscountModal').modal('show');
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi!',
                        text: 'Giảm giá đã hết hạn sử dụng!'
                    });
                }
            },
            error: function (error) {
                console.error("Lỗi khi lấy thông tin giảm giá:", error);
            }
        });
    });

    $('#closeFormNoneDiscount').click(function () {
        $('#NoneDiscountModal').modal('hide');
    });
});

//Danh sách id cartDetail được chọn để checkout
var listProductIdAddDiscount = [];

// Xử lý sự kiện khi checkbox thay đổi trạng thái
$('.cart-checkbox-add').change(function () {
    var productId = $(this).val(); // Lấy ID của cartDetail từ giá trị của checkbox

    // Kiểm tra xem checkbox có được chọn hay không
    if ($(this).is(':checked')) {
        // Nếu được chọn, thêm ID vào listProductIdChoice (nếu chưa có)
        if (!listProductIdAddDiscount.includes(productId)) {
            listProductIdAddDiscount.push(productId);
        }
    } else {
        // Nếu không được chọn, loại bỏ ID khỏi listProductIdChoice (nếu có)
        var index = listProductIdAddDiscount.indexOf(productId);
        if (index !== -1) {
            listProductIdAddDiscount.splice(index, 1);
        }
    }
    console.log('Danh sách thêm giảm giá:', listProductIdAddDiscount);
});

function setDiscountAll() {
    if (listProductIdAddDiscount.length === 0) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng chọn sản phẩm!'
        });
        return;
    }
    for (var i = 0; i < listProductIdAddDiscount.length; i++) {
        var discountId = $("#discountForm").attr("discount-id");
        var productId = listProductIdAddDiscount[i];

        var dataToSend = {
            discountId: discountId,
            productId: productId
        }

        // Gửi yêu cầu AJAX
        $.ajax({
            type: "PUT",
            url: "/admin/rest/discount/setDiscount",
            contentType: "application/json",
            data: JSON.stringify(dataToSend),
            success: function (response) {
                console.log("Áp dụng giảm giá thành công!");
            },
            error: function (error) {
                console.error("Lỗi khi áp dụng giảm giá:", error);

                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: 'Có lỗi xảy ra khi áp dụng giảm giá!'
                });
                return;
            }
        });
    }
    Swal.fire({
        icon: 'success',
        title: 'Thành công!',
        text: 'Áp dụng giảm giá thành công!',
        didClose: function () {
            location.reload();
        }
    });
}

//Danh sách id cartDetail được chọn để checkout
var listProductIdRemoveDiscount = [];

// Xử lý sự kiện khi checkbox thay đổi trạng thái
$('.cart-checkbox-remove').change(function () {
    var productId = $(this).val(); // Lấy ID của cartDetail từ giá trị của checkbox

    // Kiểm tra xem checkbox có được chọn hay không
    if ($(this).is(':checked')) {
        // Nếu được chọn, thêm ID vào listProductIdChoice (nếu chưa có)
        if (!listProductIdRemoveDiscount.includes(productId)) {
            listProductIdRemoveDiscount.push(productId);
        }
    } else {
        // Nếu không được chọn, loại bỏ ID khỏi listProductIdChoice (nếu có)
        var index = listProductIdRemoveDiscount.indexOf(productId);
        if (index !== -1) {
            listProductIdRemoveDiscount.splice(index, 1);
        }
    }
    console.log('Danh sách gỡ giảm giá:', listProductIdRemoveDiscount);
});

function removeDiscountAll() {
    if (listProductIdRemoveDiscount.length === 0) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng chọn sản phẩm!'
        });
        return;
    }

    Swal.fire({
        title: 'Bạn có chắc chắn muốn gỡ giảm giá?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Đồng ý',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            for (var i = 0; i < listProductIdRemoveDiscount.length; i++) {
                // Nếu người dùng nhấn Đồng ý
                var productId = listProductIdRemoveDiscount[i];

                // Gửi yêu cầu AJAX
                $.ajax({
                    type: "PUT",
                    url: "/admin/rest/discount/removeDiscountFromProduct/" + productId,
                    contentType: "application/json",
                    success: function (response) {
                        console.log("Gỡ giảm giá thành công!");
                    },
                    error: function (error) {
                        console.error("Lỗi khi gỡ giảm giá:", error);

                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi!',
                            text: 'Có lỗi xảy ra khi gỡ giảm giá!'
                        });
                        return;
                    }
                });
            }

            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Gỡ giảm giá thành công!',
                didClose: function () {
                    location.reload();
                }
            });
        }
    });
}

function saveDiscount() {
    // Lấy dữ liệu từ biểu mẫu
    var discountId = $("#discountForm").attr("discount-id");
    var discountName = $("#discountName").val().trim();
    var discountDiscount = $("#discountDiscount").val();
    var discountStartDate = $("#discountStartDate").val();
    var discountEndDate = $("#discountEndDate").val();
    var currentTime = moment().format('YYYY-MM-DD');
    var status = 0;

    // Check thông tin
    if (!checkInputDiscount(discountName, discountDiscount, discountStartDate, discountEndDate)) {
        return;
    }

    var dataToSend = {
        name: discountName,
        discount: discountDiscount,
        startDate: discountStartDate,
        endDate: discountEndDate,
        status: status,
        updatedDate: currentTime
    }

    if (initialDiscountName !== discountName) {
        // Kiểm tra trùng tên Giảm Giá
        if (!checkDuplicateDiscount(discountName)) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Tên giảm giá đã tồn tại!'
            });
            return false;
        }
    }

    // Gửi yêu cầu AJAX
    $.ajax({
        type: "PUT",
        url: "/admin/rest/discount/update/" + discountId,
        contentType: "application/json",
        data: JSON.stringify(dataToSend),
        success: function (response) {
            console.log("Lưu giảm giá thành công!");

            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Lưu giảm giá thành công!',
                didClose: function () {
                    location.reload();
                }
            });
        },
        error: function (error) {
            console.error("Lỗi khi lưu giảm giá:", error);

            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi lưu giảm giá!'
            });
        }
    });
}

// Check thông tin
function checkInputDiscount(discountName, discountDiscount, discountStartDate, discountEndDate) {
    // Kiểm tra xem các trường có rỗng không
    if (discountName === "" || discountDiscount === "" || discountStartDate === "" || discountEndDate === "") {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng điền đầy đủ thông tin giảm giá!'
        });
        return false;
    }

    var numberRegex = /^\d+$/;
    if (!numberRegex.test(discountDiscount)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Phần Trăm Giảm không được chứa chữ cái và ký tự đặc biệt!'
        });
        return false;
    }
    if (discountDiscount <= 0 || discountDiscount >= 100) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Phần Trăm Giảm không hợp lệ!'
        });
        return false;
    }
    if (discountStartDate > discountEndDate) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Thời gian không hợp lệ!'
        });
        return false;
    }
    return true;
}

// Check trùng Tên giảm giá
function checkDuplicateDiscount(discountName) {
    var isDuplicateName;
    // Gửi yêu cầu AJAX để kiểm tra trùng tên giảm giá
    $.ajax({
        type: "POST",
        url: "/admin/rest/discount/checkDuplicateName",
        contentType: "application/json",
        data: JSON.stringify({name: discountName}),
        async: false,
        success: function (response) {
            isDuplicateName = response.isDuplicateName;
        },
        error: function (error) {
            console.error("Lỗi khi kiểm tra trùng tên giảm giá:", error);
        }
    });
    return isDuplicateName;
}

function setDiscount(button) {
    var discountId = $("#discountForm").attr("discount-id");
    var productId = button.getAttribute("data-product-id");

    console.log("discountId: " + discountId);
    console.log("productId: " + productId);

    var dataToSend = {
        discountId: discountId,
        productId: productId
    }

    // Gửi yêu cầu AJAX
    $.ajax({
        type: "PUT",
        url: "/admin/rest/discount/setDiscount",
        contentType: "application/json",
        data: JSON.stringify(dataToSend),
        success: function (response) {
            console.log("Áp dụng giảm giá thành công!");

            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Áp dụng giảm giá thành công!',
                didClose: function () {
                    location.reload();
                }
            });
        },
        error: function (error) {
            console.error("Lỗi khi áp dụng giảm giá:", error);

            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi áp dụng giảm giá!'
            });
        }
    });
}

function removeDiscountFromProduct(button) {
    Swal.fire({
        title: 'Bạn có chắc chắn muốn gỡ giảm giá?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Đồng ý',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            // Nếu người dùng nhấn Đồng ý
            var productId = button.getAttribute("data-product-id");

            // Gửi yêu cầu AJAX
            $.ajax({
                type: "PUT",
                url: "/admin/rest/discount/removeDiscountFromProduct/" + productId,
                contentType: "application/json",
                success: function (response) {
                    console.log("Gỡ giảm giá thành công!");

                    Swal.fire({
                        icon: 'success',
                        title: 'Thành công!',
                        text: 'Gỡ giảm giá thành công!',
                        didClose: function () {
                            location.reload();
                        }
                    });
                },
                error: function (error) {
                    console.error("Lỗi khi gỡ giảm giá:", error);

                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi!',
                        text: 'Có lỗi xảy ra khi gỡ giảm giá!'
                    });
                }
            });
        }
    });
}