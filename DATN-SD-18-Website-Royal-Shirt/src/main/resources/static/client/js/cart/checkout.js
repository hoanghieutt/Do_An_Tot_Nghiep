var listProductIdChoice = [];
var listProduct = [];
var totalPrice = 0;
var shipCost = 0;
var shopId;

// Địa chỉ
$(document).ready(function () {
    // Gọi API để lấy dữ liệu tỉnh/thành phố
    $.ajax({
        url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province',
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Token': 'a76df0d2-77a1-11ee-b1d4-92b443b7a897'
        },
        success: function (data) {
            if (data.code === 200) {
                const select = $('#provinceSelect');
                data.data.forEach(province => {
                    const option = $('<option>').val(province.ProvinceID).text(province.ProvinceName);
                    select.append(option);
                });
            } else {
                console.error("Failed to fetch province data");
            }
        },
        error: function (xhr, status, error) {
            console.error("Error fetching province data:", error);
        }
    });

    // Gọi API để lấy dữ liệu quận/huyện khi thay đổi tỉnh/thành phố
    $('#provinceSelect').change(function () {
        if ($('#provinceSelect').val() === "") {
            $('#districtSelect').empty();
            $('#districtSelect').append('<option value="">Chọn huyện</option>');

            $('#wardSelect').empty();
            $('#wardSelect').append('<option value="">Chọn xã phường</option>');
            return;
        }
        const provinceID = $(this).val();
        $.ajax({
            url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district',
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Token': 'a76df0d2-77a1-11ee-b1d4-92b443b7a897'
            },
            data: {
                province_id: provinceID
            },
            success: function (data) {
                if (data.code === 200) {
                    const select = $('#districtSelect');
                    select.empty();
                    select.append('<option value="">Chọn huyện</option>'); // Thêm dòng này để không tự động chọn
                    data.data.forEach(district => {
                        const option = $('<option>').val(district.DistrictID).text(district.DistrictName);
                        select.append(option);
                    });
                } else {
                    console.error("Failed to fetch district data");
                }
            },
            error: function (xhr, status, error) {
                console.error("Error fetching district data:", error);
            }
        });

        // Reset dropdown xã/phường khi chọn tỉnh/thành phố mới
        $('#wardSelect').empty().append('<option value="">Chọn xã phường</option>');
    });

    // Gọi API để lấy dữ liệu phường/xã khi thay đổi quận/huyện
    $('#districtSelect').change(function () {
        if ($('#districtSelect').val() === "") {
            $('#wardSelect').empty();
            $('#wardSelect').append('<option value="">Chọn xã phường</option>');
            return;
        }
        const districtID = $(this).val();
        $.ajax({
            url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=' + districtID,
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Token': 'a76df0d2-77a1-11ee-b1d4-92b443b7a897'
            },
            success: function (data) {
                if (data.code === 200) {
                    const select = $('#wardSelect');
                    select.empty();
                    select.append('<option value="">Chọn xã phường</option>'); // Thêm dòng này để không tự động chọn
                    data.data.forEach(ward => {
                        const option = $('<option>').val(ward.WardCode).text(ward.WardName);
                        select.append(option);
                    });
                } else {
                    console.error("Failed to fetch ward data");
                }
            },
            error: function (xhr, status, error) {
                console.error("Error fetching ward data:", error);
            }
        });
    });


    // Gọi API để tạo cửa hàng mới
    $.ajax({
        url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shop/register',
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Token': 'a76df0d2-77a1-11ee-b1d4-92b443b7a897'
        },
        data: JSON.stringify({
            district_id: 1485,
            ward_code: "1A0606",
            name: "Royal-Shirt",
            address: "Dương Quảng Hàm",
            phone: "0387029362"
        }),
        success: function (data) {
            if (data.code === 200) {
                shopId = data.data.shop_id;
                console.log("ID cửa hàng :", shopId);
            } else {
                console.error("Failed to create store:", data.message);
            }
        },
        error: function (xhr, status, error) {
            console.error("Error creating store:", error);
        }
    });

    // Tính phí vận chuyển
    $('#wardSelect').change(function () {
        const toWardCode = $(this).val();
        const toDistrictId = parseInt($('#districtSelect').val());
        const toProvince = $('#provinceSelect').val();

        console.log("Id Shop : ", shopId);
        console.log("Phường : ", toWardCode);
        console.log("Quận : ", toDistrictId);
        console.log("Thành phố : ", toProvince);

        if (toProvince && toWardCode && toDistrictId) {

            // Gọi API để tính thời gian giao hàng dự kiến
            async function getDeliveryTime() {
                try {
                    const response = await fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/leadtime', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'Token': 'a76df0d2-77a1-11ee-b1d4-92b443b7a897'
                        },
                        body: JSON.stringify({
                            shop_id: shopId,
                            from_district_id: 1485,
                            from_ward_code: "1A0606",
                            to_district_id: toDistrictId,
                            to_ward_code: toWardCode,
                            service_id: 53320
                        })
                    });
                    const data = await response.json();
                    if (data.code === 200) {
                        const deliveryTimestamp = data.data.leadtime;
                        const deliveryDate = new Date(deliveryTimestamp * 1000);
                        const day = deliveryDate.getDate();
                        const month = deliveryDate.getMonth() + 1;
                        const year = deliveryDate.getFullYear();
                        const formattedDeliveryDate = `${day < 10 ? '0' + day : day}/${month < 10 ? '0' + month : month}/${year}`;
                        $('#deliveryTime').text(formattedDeliveryDate);
                        console.log("Thời gian giao dự kiến cho địa chỉ đã chọn:", deliveryTime);
                    } else {
                        console.error("Failed to get shipping fee:", data.message);
                    }
                } catch (error) {
                    console.error("Error getting shipping fee:", error);
                }
            }

            getDeliveryTime();

            // Gọi API để tính phí vận chuyển
            $.ajax({
                url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee',
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Token': 'a76df0d2-77a1-11ee-b1d4-92b443b7a897'
                },
                data: JSON.stringify({
                    shop_id: shopId,
                    service_id: 53320,
                    from_district_id: 1485,
                    to_district_id: toDistrictId,
                    to_ward_code: toWardCode,
                    height: 20,
                    length: 30,
                    width: 40,
                    insurance_value: 0,
                    coupon: null,
                    weight: 3000,
                    items: [
                        {
                            name: "TEST1",
                            quantity: 1,
                            height: 200,
                            weight: 1000,
                            width: 200,
                            length: 200
                        }
                    ]
                }),
                success: function (data) {
                    if (data.code === 200) {
                        var shippingFee = data.data.total;
                        shipCost = shippingFee;
                        var formattedShippingFee = formatPrice(shippingFee);
                        $('#shippingFee').text(formattedShippingFee);
                        console.log("Phí vận chuyển cho địa chỉ đã chọn:", shippingFee);
                        getTotalPriceAndShip();
                    } else {
                        console.error("Failed to get shipping fee:", data.message);
                    }
                },
                error: function (xhr, status, error) {
                    console.error("Error getting shipping fee:", error);
                }
            });

        }
    });


});


$(document).ready(function () {
    var listProduct = JSON.parse(localStorage.getItem("listProductIdChoice"));

    if (listProduct) {
        listProductIdChoice = listProduct.listProductIdChoice;
    }

    console.log(listProductIdChoice);
    renderListProductChoice();
});

$(window).on("beforeunload", function () {
    // Xóa mục "listProductIdChoice" khỏi localStorage trước khi đóng trang
    localStorage.removeItem("listProductIdChoice");
});

async function renderListProductChoice() {
    for (var i = 0; i < listProductIdChoice.length; i++) {
        await $.ajax({
            type: "GET",
            url: "/cart-detail/" + listProductIdChoice[i],
            contentType: "application/json",
            success: function (response) {
                console.log("Lấy danh sách sản phẩm thành công!");

                var productDetailId = response.productDetailId;
                var productName = "" + response.productName + "<br>" + "[" + response.colorName + "-" + response.sizeName + "]";
                var quantity = response.quantity;
                var price = response.discount === 0 ? response.price : response.price / 100 * (100 - response.discount);
                var priceFomat = formatPrice(price);

                // Tạo HTML string chứa <tr> và các <td>
                var rowHTML = "<tr>" +
                    "<td class='product'>" + productName + "</td>" +
                    "<td class='quantity'>" + quantity + "</td>" +
                    "<td class='price'>" + priceFomat + "</td>" +
                    "</tr>";

                // Thêm HTML string vào tbody
                $("#listProduct").append(rowHTML);

                var productDetail = {
                    id: productDetailId,
                    name: productName,
                    quantity: quantity,
                    price: price
                }

                listProduct.push(productDetail);
                console.log(listProduct);
            },
            error: function (error) {
                console.error("Lỗi khi lấy danh sách sản phẩm:", error);
            }
        });
    }

    getTotalPrice();
}

function getTotalPrice() {
    // Lặp qua mỗi sản phẩm trong listProduct và tính tổng giá trị
    for (var i = 0; i < listProduct.length; i++) {
        totalPrice += listProduct[i].price * listProduct[i].quantity;
    }

    var formattedPrice = formatPrice(totalPrice);

    // Hiển thị tổng giá trị đã được định dạng trên giao diện người dùng
    $("#totalPriceProducts").text(formattedPrice);
    console.log(totalPrice);
}

function getTotalPriceAndShip(){
    var totalPriceAndShip = totalPrice+shipCost;
    var formattedPrice = formatPrice(totalPriceAndShip);
    $("#totalPrice").text(formattedPrice);
}

function formatPrice(price) {
    return new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(price);
}

async function saveOrder() {
    var addressDetail = $("#address").val();
    var province = $("#provinceSelect option:selected").text();
    var district = $("#districtSelect option:selected").text();
    var ward = $("#wardSelect option:selected").text();

    var userName = $("#username").val();
    var phone = $("#phone").val();
    var address = addressDetail + ", " + ward + ", " + district + ", " + province;
    var note = $("#note").val();
    var shopping = $("input[name='paymentMethod']:checked").siblings("label").text();
    var paymentMethod = $("input[name='paymentMethod']:checked").val();
    var status = 1;

    var provinceValue = $("#provinceSelect").val();
    var districtValue = $("#districtSelect").val();
    var wardValue = $("#wardSelect").val();

    if (!checkInputSaveOrder(userName, phone, addressDetail, provinceValue, districtValue, wardValue, totalPrice, shopping)) {
        return;
    }

    var dataToSend = {
        userName: userName,
        phone: phone,
        address: address,
        note: note,
        totalPrice: totalPrice,
        shipCost: shipCost,
        shopping: shopping,
        status: status
    }

    var confirmOrder = await Swal.fire({
        title: "Xác nhận",
        text: "Bạn có chắc chắn muốn đặt hàng không?",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Đồng ý",
        cancelButtonText: "Hủy"
    });

    if (!confirmOrder.isConfirmed) {
        return;
    }

    // Gửi yêu cầu AJAX
    await $.ajax({
        type: "POST",
        url: "/order/add",
        contentType: "application/json",
        data: JSON.stringify(dataToSend),
        success: function (response) {
            console.log("Lưu hóa đơn thành công!");

            var orderId = response.id;

            if (!saveOrderDetail(orderId)) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: 'Có lỗi xảy ra khi lưu danh sách hóa đơn chi tiết!'
                });
                return;
            }

            if (!deleteCartDetailAfterCheckout()) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: 'Có lỗi xảy ra khi xóa sản phẩm khỏi giỏ hàng!'
                });
                return;
            }

            if (paymentMethod === "vnPay") {
                window.location.href = "/payment/create-payment/" + orderId;
            } else {
                Swal.fire({
                    icon: 'success',
                    title: 'Thành công!',
                    text: 'Đặt hàng thành công!',
                    didClose: function () {
                        window.location.href = "/myOrder";
                    }
                });
            }
        },
        error: function (error) {
            console.error("Lỗi khi lưu hóa đơn:", error);

            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi đặt hàng!'
            });
        }
    });
}

async function saveOrderDetail(orderId) {
    for (var i = 0; i < listProduct.length; i++) {
        var dataToSend = {
            orderId: orderId,
            productDetailId: listProduct[i].id,
            quantity: listProduct[i].quantity,
            price: listProduct[i].price,
            status: 0
        }

        // Gửi yêu cầu AJAX
        await $.ajax({
            type: "POST",
            url: "/order-detail/add",
            contentType: "application/json",
            data: JSON.stringify(dataToSend),
            success: function (response) {
                console.log("Lưu hóa đơn chi tiết thành công!");
            },
            error: function (error) {
                console.error("Lỗi khi lưu hóa đơn:", error);
                return false;
            }
        });
    }
    return true;
}

async function deleteCartDetailAfterCheckout(){
    for (var i = 0; i < listProduct.length; i++) {
        // Gửi yêu cầu AJAX
        await $.ajax({
            type: "DELETE",
            url: "/rest/cart-detail/deleteAfterCheckout/" + listProduct[i].id,
            contentType: "application/json",
            success: function (response) {
                console.log("Xóa sản phẩm khỏi giỏ hàng thành công!");
            },
            error: function (error) {
                console.error("Lỗi khi xóa sản phẩm khỏi giỏ hàng:", error);
                return false;
            }
        });
    }
    return true;
}

function checkInputSaveOrder(name, phone, addressDetail, provinceValue, districtValue, wardValue, totalPrice, shopping) {
    // Regex cho tên không chứa số hoặc ký tự đặc biệt
    var nameRegex = /^[a-zA-ZÀ-ỹ\s]+$/;
    // Regex cho số điện thoại
    var phoneRegex = /^0\d{9}$/;


    if (name === "" ||
        phone === "" ||
        addressDetail === "" ||
        provinceValue === "" ||
        districtValue === "" ||
        wardValue === "" ||
        totalPrice == 0 ||
        shopping === "") {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng nhập đầy đủ thông tin!'
        });
        return false;
    } else if (!nameRegex.test(name)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Tên không được chứa số hoặc ký tự đặc biệt!'
        });
        return false;
    } else if (!phoneRegex.test(phone)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Số điện thoại không hợp lệ!'
        });
        return false;
    } else
        return true;
}