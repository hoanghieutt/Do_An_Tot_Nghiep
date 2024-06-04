var productId = $('#product-id').attr("data-product-id");

//Show form
$(document).ready(function () {
    $("#div-quantity-color").hide();
    $("#div-quantity-size").hide();
    //Mở form add
    $('#showFormProductDetailAdd').click(function () {
        $('#AddProductDetailModal').modal('show');
    });

    //Đóng form add
    $('#closeFormProductDetailAdd').click(function () {
        $('#AddProductDetailModal').modal('hide');
    });
    //Đóng form update
    $('#closeFormProductDetailUpdate').click(function () {
        $('#UpdateProductDetailModal').modal('hide');
    });

    getListURL(productId);

});

//Lấy danh sách size add
function getSize(selectElement) {
    $("#select-size").empty();
    var colorId = selectElement.value;

    var dataToSend = {
        productId: productId,
        colorId: colorId
    }

    $.ajax({
        type: "GET",
        url: "/admin/rest/product-detail/getListSizeAddProductDetail",
        contentType: "application/json",
        data: dataToSend,
        success: function (response) {
            console.log("Lấy danh sách Size thành công!");
            renderListSize(response);
        },
        error: function (error) {
            console.error("Lỗi khi lấy danh sách Size:", error);
            return false;
        }
    });
}

//Render danh sách size add
function renderListSize(listSize) {
    for (var i = 0; i < listSize.length; i++) {

        var value = listSize[i].id;

        var name = listSize[i].name;

        var sizeSelect = $(
            '<option value="' + value + '">' + name + '</option>'
        )

        $(sizeSelect).appendTo("#select-size");

    }
}

//Lấy số lượng thông qua màu
function getQuantityByColor(selectElement) {

    var colorId = selectElement.value;

    var dataToSend = {
        productId: productId,
        colorId: colorId
    }

    $.ajax({
        type: "GET",
        url: "/admin/rest/product/quantityByColorId",
        contentType: "application/json",
        data: dataToSend,
        success: function (response) {
            console.log("Lấy số lượng Màu sắc thành công!");
            if (response === "") {
                $("#label-quantity-color").text(0);
            } else {
                $("#label-quantity-color").text(response);
            }

            $("#div-quantity-color").show();

        },
        error: function (error) {
            console.error("Lỗi khi lấy số lượng Màu sắc:", error);
            return false;
        }
    });
}

//Lấy số lượng thông qua màu
function getQuantityBySize(selectElement) {

    var sizeId = selectElement.value;

    var dataToSend = {
        productId: productId,
        sizeId: sizeId
    }

    $.ajax({
        type: "GET",
        url: "/admin/rest/product/quantityBySizeId",
        contentType: "application/json",
        data: dataToSend,
        success: function (response) {
            console.log("Lấy số lượng Kích thước thành công!");

            if (response === "") {
                $("#label-quantity-size").text(0);
            } else {
                $("#label-quantity-size").text(response);
            }

            $("#div-quantity-size").show();

        },
        error: function (error) {
            console.error("Lỗi khi lấy số lượng Kích thước:", error);
            return false;
        }
    });
}

function getListURL(productId) {
    listUrlImage = [];
    $.ajax({
        type: "GET",
        url: "/admin/rest/image/findByProductId/" + productId,
        success: function (response) {
            console.log("Lấy ảnh thành công!");
            for (var i = 0; i < response.length; i++) {
                listUrlImage.push(response[i].urlImage);
            }
            // Gọi hàm writeURL để hiển thị ảnh
            writeURL();
        },
        error: function (error) {
            console.log('Error fetching image data:', error);
            // Xử lý lỗi nếu cần
        }
    })
}

function writeURL() {
    // Lặp qua mảng listUrlImage và hiển thị từng ảnh
    listUrlImage.forEach(function (url) {
        var imageContainer = $(
            '<div class="image-product-container">' +
            '   <img src="' + url + '" alt="Thumb image" class="thumbimage"/>' +
            '   <a class="removeimg" href="javascript:" style="display: inline"></a>' +
            '</div>'
        );

        // Thêm container vào thumbbox
        $("#thumbbox").append(imageContainer);

        // Sự kiện click cho nút xóa
        imageContainer.find(".removeimg").on("click", function () {
            var removedImage = $(this).closest(".image-product-container").find("img").attr("src");
            // Xóa ảnh khỏi biến listUrlImage
            listUrlImage = listUrlImage.filter(function (img) {
                return img !== removedImage;
            });
            $(this).closest(".image-product-container").remove();
            console.log(listUrlImage);
            $("#myfileupload").html('<input type="file" id="uploadfile" name="ImageUpload" multiple onchange="readURL(this)"/>');
            $('.Choicefile').css('background', '#14142B');
        });
    });
    console.log('writeURL');
}

function updateProductDetailForm(element) {
    var productDetailId = element.getAttribute("data-product-detail-id")
    var productDetailColor = element.getAttribute("data-product-detail-color")
    var productDetailSize = element.getAttribute("data-product-detail-size")

    // Thêm thuộc tính productDetailId vào modal để update
    $('#UpdateProductDetailModal').attr('product-detail-id-update', productDetailId);

    // Thực hiện AJAX request để lấy dữ sản phẩm mục từ backend
    $.ajax({
        type: 'GET',
        url: '/admin/rest/product-detail/formUpdate/' + productDetailId,
        success: function (productDetail) {
            // Hiển thị hộp thoại modal
            $('#UpdateProductDetailModal').modal('show');

            // Điền dữ liệu vào các trường biểu mẫu
            $('#product-detail-color').text("Màu sắc: " + productDetailColor);
            $('#product-detail-size').text("Kích thước: " + productDetailSize);
            // $('#product-detail-weight').val(productDetail.weight);
            $('#product-detail-quantity').val(productDetail.quantity);
            $('#product-detail-price').val(productDetail.price);
        },
        error: function (error) {
            console.log('Error fetching productDetail data:', error);
            // Xử lý lỗi nếu cần
        }
    });
}

function saveUpdate() {
    var productDetailId = $('#UpdateProductDetailModal').attr("product-detail-id-update");
    // var productDetailWeight = $('#product-detail-weight').val();
    var productDetailQuantity = $("#product-detail-quantity").val();
    var productDetailPrice = $("#product-detail-price").val();
    var productDetailStatus = 0;

    if (productDetailQuantity < 0 || productDetailPrice <= 0) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Thông tin không hợp lệ!'
        });
        return;
    }

    var dataToSend = {
        id: productDetailId,
        // weight: productDetailWeight,
        quantity: productDetailQuantity,
        price: productDetailPrice,
        status: productDetailStatus
    }

    // Gửi yêu cầu AJAX
    $.ajax({
        type: "PUT",
        url: "/admin/rest/product-detail/update/" + productDetailId,
        contentType: "application/json",
        data: JSON.stringify(dataToSend),
        success: function (response) {
            console.log("Lưu Biến Thể thành công!");

            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Lưu Biến Thể thành công!',
                didClose: function () {
                    location.reload();
                }
            });
        },
        error: function (error) {
            console.error("Lỗi khi lưu Biến Thể:", error);

            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi lưu Biến Thể!'
            });
        }
    });
}

function saveAdd() {
    var productDetailColor = $('#select-color').val();
    var productDetailSize = $('#select-size').val();
    // var productDetailWeight = $('#add-product-detail-weight').val();
    var productDetailQuantity = $("#add-product-detail-quantity").val();
    var productDetailPrice = $("#add-product-detail-price").val();
    var productDetailStatus = 0;

    if (productDetailColor == null || productDetailSize == null || productDetailQuantity == 0 || productDetailPrice == 0) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng nhập đầy đủ thông tin!!'
        });
        return;
    }

    var dataToSend = {
        productId: productId,
        colorId: productDetailColor,
        sizeId: productDetailSize,
        // weight: productDetailWeight,
        quantity: productDetailQuantity,
        price: productDetailPrice,
        status: productDetailStatus
    }

    // Gửi yêu cầu AJAX
    $.ajax({
        type: "POST",
        url: "/admin/rest/product-detail/add",
        contentType: "application/json",
        data: JSON.stringify(dataToSend),
        success: function (response) {
            console.log("Lưu Biến Thể thành công!");

            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Lưu Biến Thể thành công!',
                didClose: function () {
                    location.reload();
                }
            });
        },
        error: function (error) {
            console.error("Lỗi khi lưu Biến Thể:", error);

            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi lưu Biến Thể!'
            });
        }
    });
}