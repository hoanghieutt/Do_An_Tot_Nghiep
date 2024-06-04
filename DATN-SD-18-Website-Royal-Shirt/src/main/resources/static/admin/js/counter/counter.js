var orderTabId = 0;
var preTab = 0;
var currentTab = 0;

//Counter page
function addCounterPage() {
    $("#form-counter").css("display", "block");
    if (orderTabId < 5) {
        orderTabId++;
        renderButtonOrderTab(orderTabId);
    } else {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Tối đa 5 hóa đơn!'
        });
        return;
    }
}

function renderButtonOrderTab(orderTabId) {
    var buttonOrderTab = "<button class=\"btn btn-add-order\" data-order-tab-id='" + orderTabId + "'>Hóa đơn " + orderTabId + "</button>";
    $("#listCounter").append(buttonOrderTab);

    // Khi click vào button
    $('body').on('click', '.btn-add-order', function () {
        // Lấy giá trị của thuộc tính 'data-order-tab-id'
        currentTab = $(this).data('order-tab-id');

        // Lưu dữ liệu trước khi chuyển tab
        if (preTab != 0) {
            saveData(preTab);
            listProductCart = [];
        }

        // Lấy dữ liệu cho tab mới
        getData(currentTab);

        preTab = currentTab;

        // Xóa class 'btn-active' từ tất cả các button
        $('.btn-add-order').removeClass('btn-active');

        // Thêm class 'btn-active' cho button được click
        $(this).addClass('btn-active');
    });
    // Kích hoạt sự kiện click cho nút mới thêm vào
    $('.btn-add-order[data-order-tab-id="' + orderTabId + '"]').trigger('click');
}

var sttCounter = 1;
var listProductCart = [];
var totalPrice = 0;

//Show form customer
$(document).ready(function () {
    $('#showFormCustomer').click(function () {
        $('#CustomerModal').modal('show');
    });

    $('#closeFormCustomer').click(function () {
        $('#customerName').val('');
        $('#customerPhone').val('');
        $('#CustomerModal').modal('hide');
    });

    $('#CustomerModal').on('hidden.bs.modal', function () {
        $('#customerName').val('');
        $('#customerPhone').val('');
    });

    renderListCustomer();
});

function saveCustomer() {
    var customerName = $("#customerName").val().trim();
    var customerPhone = $("#customerPhone").val().trim();
    var status = 0;

    if (!checkInputCustomer(customerName, customerPhone)) {
        return;
    }

    var dataToSend = {
        name: customerName,
        phone: customerPhone,
        status: status
    }

    // Gửi yêu cầu AJAX
    $.ajax({
        type: "POST",
        url: "/admin/rest/customer/add",
        contentType: "application/json",
        data: JSON.stringify(dataToSend),
        success: function (response) {
            console.log("Lưu Khách hàng thành công!");

            renderListCustomer();

            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Lưu Khách hàng thành công!',
                didClose: function () {
                    $('#customerName').val('');
                    $('#customerPhone').val('');
                    $('#CustomerModal').modal('hide');
                }
            });

        },
        error: function (error) {
            console.error("Lỗi khi lưu Khách hàng:", error);

            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi lưu Khách hàng!'
            });
        }
    });
}

function checkInputCustomer(customerName, customerPhone) {
    // Kiểm tra xem các trường có rỗng không
    if (customerName === "" || customerPhone === "") {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Thông số kích thước không hợp lệ!'
        });
        return false;
    }

    //Check Kích thước
    var nameRegex = /^[a-zA-ZÀ-ỹ\s]+$/;
    if (!nameRegex.test(customerName)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Tên khách hàng chỉ được chứa chữ cái và khoảng trắng!'
        });
        return false;
    }

    var phoneRegex = /^0\d{9}$/;
    if (!phoneRegex.test(customerPhone)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Số điện thoại không hợp lệ!'
        });
        return false;
    }

    if (!checkDuplicateCustomer(customerPhone)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Số điện thoại đã tồn tại!'
        });
        return false;
    }

    return true;
}

// Check trùng Số điện thoại
function checkDuplicateCustomer(customerPhone) {
    var isDuplicatePhone;
    // Gửi yêu cầu AJAX để kiểm tra trùng Số điện thoại
    $.ajax({
        type: "POST",
        url: "/admin/rest/customer/checkDuplicatePhone",
        contentType: "application/json",
        data: JSON.stringify({phone: customerPhone}),
        async: false,
        success: function (response) {
            isDuplicatePhone = response.isDuplicatePhone;
        },
        error: function (error) {
            console.error("Lỗi khi kiểm tra trùng Số điện thoại:", error);
        }
    });
    return isDuplicatePhone;
}

function renderListCustomer() {
    // Xóa tất cả các option hiện tại trong select
    $('#select-customer')[0].selectize && $('#select-customer')[0].selectize.destroy();
    $('#select-customer').empty();

    $.ajax({
        method: 'GET',
        url: '/admin/rest/customer', // Thay thế ĐƯỜNG_DẪN_API bằng đường dẫn thực tế của bạn
        contentType: "application/json",
        success: function (data) {
            console.log("Lấy danh sách Khách hàng thành công!");
            // Append các option từ dữ liệu API

            $('#select-customer').append('<option value="" disabled selected hidden>--- Chọn khách hàng ---</option>');

            $.each(data, function (index, customer) {
                $('#select-customer').append('<option value="' + customer.id + '">' + customer.name + '</option>');
            });

            $('#select-customer').selectize();
        },
        error: function (error) {
            console.error('Lỗi khi gọi API getListCustomer: ', error);
        }
    });
}

function search() {
    var keyword = $('#myInput').val();
    var dataTable = $('#sampleTable').DataTable();
    dataTable.search(keyword).draw();
}

function addToCart(button) {
    var productDetailId = $(button).data("product-detail-id");
    var productName = $(button).closest("tr").find(".ten_san_pham").text();
    var productImage = $(button).closest("tr").find(".anh img").attr("src");
    var productColor = $(button).closest("tr").find(".mau_sac").text();
    var productSize = $(button).closest("tr").find(".kich_thuoc").text();
    var productQuantity = $(button).closest("tr").find(".so_luong").text();
    var productPrice = $(button).closest("tr").find(".gia_ban").text();

    if (productQuantity <= 0) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Sản phẩm đã hết hàng!'
        });
        return;
    }

    // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
    var existingRow = $("#cartTable").find("tr[data-product-detail-id='" + productDetailId + "']");

    if (existingRow.length > 0) {
        // Tăng số lượng nếu đã có trong giỏ hàng
        var quantity = parseInt(existingRow.find(".so_luong input").val()) + 1;

        if (!checkQuantityAddToCart(quantity, productQuantity)) {
            return;
        }

        existingRow.find(".so_luong input").val(quantity);
        existingRow.find(".so_luong input").attr("previous-quantity", quantity);
        // Cập nhật số lượng trong listProductCart
        var existingProduct = listProductCart.find(function (product) {
            return product.id === productDetailId;
        });

        if (existingProduct) {
            existingProduct.quantity = quantity;
        }
    } else {
        var quantity = 1;
        // Thêm dòng mới vào bảng giỏ hàng
        var newRow = "<tr data-product-detail-id='" + productDetailId + "'>" +
            "<td class='stt'>" + sttCounter++ + "</td>" +
            "<td class='ten_san_pham'>" + productName + "</td>" +
            "<td class='anh'><img src='" + productImage + "' alt='Hình ảnh sản phẩm'></td>" +
            "<td class='mau_sac'>" + productColor + "</td>" +
            "<td class='kich_thuoc'>" + productSize + "</td>" +
            "<td class='so_luong' data-quantity='" + quantity + "'><input class='input_so_luong' type='number' value='" + quantity + "' min='1' onchange='updateQuantityAddToCart(this," + productQuantity + ")' previous-quantity='" + quantity + "'></td>" +
            "<td class='gia_ban' id='gia_ban'>" + productPrice + "</td>" +
            "<td class='tinh_nang'><button class='btn btn-danger remove-button' onclick='removeFromCart(this)'><i class='fa-solid fa-minus fa-2xl remove-icon'></i></button></td>" +
            "</tr>";
        $("#listCart").append(newRow);
        var productCart = {
            id: productDetailId,
            name: productName,
            image: productImage,
            color: productColor,
            size: productSize,
            quantity: quantity,
            productQuantity: productQuantity,
            price: parseFloat(productPrice.replace(/\./g, ''))
        }

        listProductCart.push(productCart);
    }

    saveData(currentTab);
    getTotalPrice();
    console.log(listProductCart);
}

function renderListProductCart(){
    $("#listCart").empty();

    for (var i = 0; i < listProductCart.length; i++) {
        // Định dạng số và hiển thị dấu phân cách hàng nghìn
        var priceFormat = listProductCart[i].price.toFixed(0).replace(/\d(?=(\d{3})+$)/g, "$&.");

        var newRow = "<tr data-product-detail-id='" + listProductCart[i].id + "'>" +
            "<td class='stt'>" + (i+1) + "</td>" +
            "<td class='ten_san_pham'>" + listProductCart[i].name + "</td>" +
            "<td class='anh'><img src='" + listProductCart[i].image + "' alt='Hình ảnh sản phẩm'></td>" +
            "<td class='mau_sac'>" + listProductCart[i].color + "</td>" +
            "<td class='kich_thuoc'>" + listProductCart[i].size + "</td>" +
            "<td class='so_luong' data-quantity='" + listProductCart[i].quantity + "'><input class='input_so_luong' type='number' value='" + listProductCart[i].quantity + "' min='1' onchange='updateQuantityAddToCart(this," + listProductCart[i].productQuantity + ")' previous-quantity='" + listProductCart[i].quantity + "'></td>" +
            "<td class='gia_ban' id='gia_ban'>" + priceFormat + "</td>" +
            "<td class='tinh_nang'><button class='btn btn-danger remove-button' onclick='removeFromCart(this)'><i class='fa-solid fa-minus fa-2xl remove-icon'></i></button></td>" +
            "</tr>";
        $("#listCart").append(newRow);
    }

    getTotalPrice();
}

function checkQuantityAddToCart(quantity, productQuantity) {
    if (quantity > productQuantity) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vượt số lượng sản phẩm!'
        });
        return false;
    }
    if (quantity <= 0 ){
        swal.fire({
            icon:'error',
            title:'lỗi',
            text:'số lượng phải lớn hơn 0'
        });
        return false;
    }undefined
    if (quantity==null ){
            swal.fire({
                icon:'error',
                title:'lỗi',
                text:'không được bỏ trống'
            });
            return false;
        }
    if (isNaN(quantity) ){
                swal.fire({
                    icon:'error',
                    title:'lỗi',
                    text:'không được bỏ trống '
                });
                return false;
            }

    return true;
}

function updateQuantityAddToCart(input, productQuantity) {
    var row = $(input).closest("tr");
    var productDetailId = row.data("product-detail-id");
    var newQuantity = parseInt($(input).val());

    var previousQuantity = row.find(".so_luong input").attr("previous-quantity");

    if (!checkQuantityAddToCart(newQuantity, productQuantity)) {
        // Đặt lại giá trị trường nhập liệu về số lượng trước nếu kiểm tra thất bại
        $(input).val(previousQuantity);
        return;
    }

    row.find(".so_luong input").attr("previous-quantity", newQuantity);

    // Cập nhật số lượng trong giỏ hàng
    row.find(".so_luong input").val(newQuantity);

    // Cập nhật số lượng trong listProductCart
    var existingProductIndex = listProductCart.findIndex(function (product) {
        return product.id === productDetailId;
    });

    if (existingProductIndex !== -1) {
        listProductCart[existingProductIndex].quantity = newQuantity;
    }

    getTotalPrice();
    console.log(listProductCart);
}

function removeFromCart(button) {
    var row = $(button).closest("tr");
    var productDetailId = row.data("product-detail-id");

    // Xóa sản phẩm khỏi giỏ hàng
    row.remove();

    // Xóa sản phẩm khỏi listProductCart
    listProductCart = listProductCart.filter(function (product) {
        return product.id !== productDetailId;
    });

    saveData(currentTab);
    getTotalPrice();
    console.log(listProductCart);
}

function getTotalPrice() {
    // Lặp qua từng sản phẩm trong listProductCart và tính tổng giá
    totalPrice = listProductCart.reduce(function (accumulator, product) {
        return accumulator + (product.price * product.quantity);
    }, 0);

    console.log("Tong tien: " + totalPrice);

    // Định dạng số và hiển thị dấu phân cách hàng nghìn
    var formattedPrice = totalPrice.toFixed(0).replace(/\d(?=(\d{3})+$)/g, "$&.");
    $("#tong-cong-thanh-toan").text(formattedPrice);

    tinhNo();
}

function tinhNo() {
    var tienNo = $("#khach-hang-dua-tien").val() - totalPrice;
    console.log("Tong tien: " + totalPrice);
    console.log("Tien khach tra: " + $("#khach-hang-dua-tien").val());
    console.log("Tien no: " + tienNo);
    console.log(tienNo);

    // Định dạng số và hiển thị dấu phân cách hàng nghìn
    var tienNoFormat = tienNo.toFixed(0).replace(/\d(?=(\d{3})+$)/g, "$&.");

    if (tienNo < 0) {
        $("#khach-hang-con-thieu").text(tienNoFormat);
        $("#tra-lai-khach").text(0);
    } else {
        $("#khach-hang-con-thieu").text(0);
        $("#tra-lai-khach").text(tienNoFormat);
    }
    saveData(currentTab);
}

async function saveOrder() {
    if (listProductCart.length === 0) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Giỏ hàng trống!'
        });
        return;
    }

    if ($("#khach-hang-dua-tien").val() <= 0) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Tiền khách đưa không hợp lệ!'
            });
            return;
        }

    if (!await checkQuantitySaveOrderDetail()) {
        return;
    }

    var staffId = $("#select-staff").val();
    var customerId = $("#select-customer").val();
    var note = $("#input-note").val();
    var shopping = $("#select-shopping").val()
    var status = 5;
    var successDate = moment().format("YYYY-MM-DDTHH:mm:ss");

    if (!checkInputSaveOrder(staffId, customerId, totalPrice, shopping)) {
        return;
    }

    var dataToSend = {
        staffId: staffId,
        customerId: customerId,
        totalPrice: totalPrice,
        shipCost: 0,
        note: note,
        shopping: shopping,
        status: status,
        successDate: successDate,
        type: false
    }

    // Gửi yêu cầu AJAX
    $.ajax({
        type: "POST",
        url: "/admin/rest/counter/addOrder",
        contentType: "application/json",
        data: JSON.stringify(dataToSend),
        success: function (response) {
            console.log("Lưu Hóa đơn thành công!");

            var orderId = response.id;

            if (!saveOrderDetail(orderId)) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: 'Có lỗi xảy ra khi lưu Danh sách hóa đơn chi tiết!'
                });
                return;
            }

            if (!updateQuantitySaveOrderDetail()) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: 'Có lỗi xảy ra khi cập nhật số lượng!'
                });
                return;
            }

            clearOrder();

            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Lưu Hóa đơn thành công!',
                didClose: function () {
                printBill(orderId)
                    // window.location.href = "/admin/product";
                }
            });
        },
        error: function (error) {
            console.error("Lỗi khi lưu Hóa đơn:", error);

            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi lưu Hóa đơn!'
            });
        }
    });


}

function checkInputSaveOrder(staffId, customerId, totalPrice, shopping) {
    if (staffId === "" || customerId === "" || totalPrice === "" || shopping === "") {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng chọn đầy đủ thông tin!'
        });
        return false;
    }

    if (!checkPrice()) {
        return false;
    }

    return true;
}

function checkPrice() {
    var tienNo = $("#khach-hang-dua-tien").val() - totalPrice;
    if (tienNo < 0) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Khách hàng chưa trả đủ tiền!'
        });
        return false;
    }
    return true;
}

function saveOrderDetail(orderId) {
    for (var i = 0; i < listProductCart.length; i++) {
        var dataToSend = {
            orderId: orderId,
            productDetailId: listProductCart[i].id,
            quantity: listProductCart[i].quantity,
            price: listProductCart[i].price,
            status: 0
        }

        // Gửi yêu cầu AJAX
        $.ajax({
            type: "POST",
            url: "/admin/rest/counter/addOrderDetail",
            contentType: "application/json",
            data: JSON.stringify(dataToSend),
            success: function (response) {
                console.log("Lưu Hóa đơn chi tiết thành công!");
            },
            error: function (error) {
                console.error("Lỗi khi lưu Hóa đơn chi tiết:", error);
                return false;
            }
        });
    }
    return true;
}

async function checkQuantitySaveOrderDetail() {
    for (let i = 0; i < listProductCart.length; i++) {
        var dataToSend = {
            idProductDetail: listProductCart[i].id,
            quantity: listProductCart[i].quantity
        }

        try {
            // Sử dụng async/await để chờ yêu cầu AJAX hoàn thành
            await $.ajax({
                type: "GET",
                url: "/admin/rest/counter/checkQuantity",
                contentType: "application/json",
                data: dataToSend,
                success: function (response) {
                    console.log("Kiểm tra số lượng thành công!");
                    if (response !== "") {
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi!',
                            text: listProductCart[i].name + " [" + listProductCart[i].color + " " + listProductCart[i].size + "] " + ' vượt quá số lượng!'
                        });
                        // Nếu có lỗi, nên sử dụng throw để kết thúc hàm và ngăn chặn việc tiếp tục
                        throw new Error("Lỗi khi kiểm tra số lượng");
                    }
                },
                error: function (error) {
                    console.error("Lỗi khi kiểm tra số lượng:", error);
                    throw new Error("Lỗi khi kiểm tra số lượng");
                }
            });
        } catch (error) {
            // Xử lý lỗi nếu cần thiết
            return false;
        }
    }
    return true;
}

function updateQuantitySaveOrderDetail() {
    for (let i = 0; i < listProductCart.length; i++) {
        var dataToSend = {
            idProductDetail: listProductCart[i].id,
            quantity: listProductCart[i].quantity
        }
        // Gửi yêu cầu AJAX
        $.ajax({
            type: "PUT",
            url: "/admin/rest/counter/updateQuantity",
            contentType: "application/json",
            data: JSON.stringify(dataToSend),
            success: function (response) {
                console.log("Cập nhật số lượng thành công!");
            },
            error: function (error) {
                console.error("Lỗi khi cập nhật số lượng:", error);
                return false;
            }
        });
    }
    return true;
}

// Lưu trữ dữ liệu vào Local Storage
function saveData(orderTabId) {
    var staffId = $("#select-staff").val();
    var customerId = $("#select-customer").val();
    var khachHangDuaTien = $("#khach-hang-dua-tien").val();

    // Lấy dữ liệu hiện tại từ Local Storage
    var existingData = JSON.parse(localStorage.getItem(orderTabId)) || {};

    // Cập nhật thông tin giỏ hàng, khách hàng, và nhân viên
    existingData.listProductCart = listProductCart;
    existingData.customerId = customerId;
    existingData.staffId = staffId;
    existingData.khachHangDuaTien = khachHangDuaTien;

    // Lưu lại dữ liệu vào Local Storage
    localStorage.setItem(orderTabId, JSON.stringify(existingData));
}

// Lấy dữ liệu từ Local Storage
function getData(orderTabId) {
    var storedData = JSON.parse(localStorage.getItem(orderTabId));

    if (storedData) {
        listProductCart = storedData.listProductCart;
        renderListProductCart();
        $("#select-customer")[0].selectize.setValue(storedData.customerId);
        $("#select-staff").val(storedData.staffId);
        $("#khach-hang-dua-tien").val(storedData.khachHangDuaTien);
    }
}

function clearOrder(){
    localStorage.removeItem(currentTab);
    listProductCart = [];
    renderListProductCart();
    $("#select-customer")[0].selectize.setValue(null);
    $("#khach-hang-dua-tien").val(0);
    getTotalPrice();
}
function formatToVND(amount) {
  const formatter = new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
    minimumFractionDigits: 0, // Set to 0 to display whole numbers
  });
  return formatter.format(amount);
}
function printBill(id) {
  axios
    .get(`/admin/rest/counter/bill/${id}`)
    .then((response) => {
      const billData = response.data;
      console.log(billData);
      $("#bill #hoadoncode").text(billData.order.id);
      $("#bill #customername").text(billData.order.customer.name);
      $("#bill #phonenumber").text(billData.order.customer.phone);
      $("#bill #fulladdress").text(billData.order.address);
      // Update employee information
      $("#bill #employename").text(billData.order.staff?.name);
//      $("#bill #orderstatus").html(getStatusBadge(billData.order.status)); // Assuming orderstatus is a property in your billData
      $("#bill #ordertype").text("Tại quầy");
      // Assuming ordertype is a property in your billData
      // if (billData.loaiDon === 0) {
      //   $("#bill #ordertype").text("tại quầy");
      // } else if (billData.loaiDon === 1) {
      //   $("#bill #ordertype").text("Online");
      // }
      // Update table with product details
      const productsTable = $("#bill #sanphaminbill");
      productsTable.empty(); // Clear existing rows
      billData.order_details.forEach((product, index) => {
        const row = `<tr>
                  <th scope="row">${index + 1}</th>
                  <td>${product.productDetail.product.name}</td>
                  <td>${product.quantity}</td>
                  <td>${formatToVND(product.price)}</td>
                  <td>${formatToVND(
                    product.quantity * product.price
                  )}</td>
                </tr>`;
        productsTable.append(row);
      });
      $("#bill #note").text(billData.note);
      $("#bill #total-products").text(formatToVND(billData.order.totalPrice));
      // $("#bill #discount").text(formatToVND(billData.reduction_amount));
      // $("#bill #shipping-cost").text(formatToVND(billData.money_ship));
      $("#bill #total-price").text(formatToVND(billData.order.totalPrice));
      $("#billPrint").modal("show");
    })
    .catch((error) => {
      console.error("Error fetching bill data:", error);
    });
  // Update customer information
}