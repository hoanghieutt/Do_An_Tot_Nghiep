var quantityCartDetail;
var quantityProductDetail;
var productDetailId;

$(document).ready(function () {
    showQuantityAndPrice();
});

document.addEventListener("DOMContentLoaded", function() {
    var quantityInput = document.getElementById('quantity');

    // Bắt sự kiện khi giá trị của input thay đổi
    quantityInput.addEventListener('change', function() {
        var quantityValue = parseInt(quantityInput.value);

        // Kiểm tra xem số lượng có lớn hơn 0 không
        if (quantityValue < 1) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Số lượng phải lớn hơn 1.'
            });
            this.value = 1;
            return;
        }
        // Kiểm tra xem giá trị nhập vào có phải là số không
        if (!(/^\d*$/.test(quantityValue))) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Vui lòng chỉ nhập các ký tự số.'
            });
            this.value = 1;
            return;
        }
    });
});

function showQuantityAndPrice() {
    getProductDetail()
    .then(function(productDetail) {
        $("#showQuantity").text(productDetail.quantity);
        // var formattedPrice = productDetail.price.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
        // $("#price").text(formattedPrice);
    })
    .catch(function(error) {
        console.log(error);
    });

    var productId = $("#addToCartBtn").attr("data-id");
    var colorId = $("#colorSelect").val();
    var sizeId = $("#sizeSelect").val();

    var sendData = {
        productId: productId,
        colorId: colorId,
        sizeId: sizeId
    };

     $.ajax({
        type: 'GET',
        url: '/getPriceByProductId',
        contentType: 'application/json',
        data: sendData,
        success: function(response) {
            var formattedPrice = response.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
            $("#price").text(formattedPrice);
        },
        error: function(xhr, status, error) {
            console.log(error);
        }
    });
}

function getProductDetail() {
    var productId = $("#addToCartBtn").attr("data-id");
    var colorId = $("#colorSelect").val();
    var sizeId = $("#sizeSelect").val();

    var sendData = {
        productId: productId,
        colorId: colorId,
        sizeId: sizeId
    };

    return new Promise(function(resolve, reject) {
        $.ajax({
            type: 'GET',
            url: '/showQuantity',
            contentType: 'application/json',
            data: sendData,
            success: function(response) {
                resolve(response);
            },
            error: function(xhr, status, error) {
                reject("Lỗi số lượng");
            }
        });
    });
}

async function checkAddToCart(quantity) {
    try {
        var productDetail = await getProductDetail();
        var sendData = {
            productDetailId: productDetail.id
        };

        var response = await $.ajax({
            type: 'GET',
            url: '/quantityCartDetail',
            contentType: 'application/json',
            data: sendData
        });

        var quantityCartDetail = response;
        console.log("quantityCart = " + quantityCartDetail);

        if ((quantity + quantityCartDetail) > productDetail.quantity) {
            throw "Số lượng vượt quá số lượng tối đa!";
        } else {
            return true;
        }
    } catch (error) {
        throw error;
    }
}

async function addToCart(button) {
    try {
        var id = button.getAttribute("data-id");
        var colorId = document.getElementById('colorSelect').value;
        var sizeId = document.getElementById('sizeSelect').value;
        var quantity = document.getElementById('quantity').value;

        var maxQuantity = parseInt(document.getElementById('quantity').getAttribute('max'));

        if (!colorId || !sizeId || !quantity) {
            throw 'Vui lòng chọn màu sắc, kích thước và nhập số lượng.';
        }

        if (isNaN(quantity) || quantity > maxQuantity) {
            throw 'Số lượng vượt quá số lượng tối đa cho phép.';
        }

        await checkAddToCart(parseInt(quantity));

        var cartDetailDTO = {
            productDetail: {
                color: { id: colorId },
                size: { id: sizeId }
            },
            quantity: quantity
        };

        await $.ajax({
            type: 'POST',
            url: '/add-to-cart/' + id,
            contentType: 'application/json',
            data: JSON.stringify(cartDetailDTO),
            success: function(response) {
                Swal.fire({
                    icon: 'success',
                    title: 'Thành công!',
                    text: 'Sản phẩm đã được thêm vào giỏ hàng thành công!'
                });
            },
            error: function(xhr, status, error) {
                Swal.fire({
                    icon: 'error',
                    title: 'Thất bại!',
                    text: 'Không thể thêm sản phẩm vào giỏ hàng.'
                });
            }
        });
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: error
        })
    }
}
