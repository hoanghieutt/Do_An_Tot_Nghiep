var listUrlImage = [];

//Show form
$(document).ready(function () {
    $('#showFormProduct').click(function () {
        $('#ProductModal').modal('show');
    });
    $('#closeFormProduct').click(function () {
        $('#ProductModal').modal('hide');
    });
});

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

function readURL(input) {
    if (input.files && input.files.length > 0) {
        for (var i = 0; i < input.files.length; i++) {
            var reader = new FileReader();

            reader.onload = function (e) {
                // Kiểm tra xem ảnh đã tồn tại trong mảng listUrlImage chưa
                var isDuplicate = listUrlImage.some(function (img) {
                    return img === e.target.result;
                });

                if (!isDuplicate) {
                    // Tạo một container cho ảnh và nút xóa
                    var imageContainer = $(
                        '<div class="image-product-container">' +
                        '   <img src="' + e.target.result + '" alt="Thumb image" class="thumbimage"/>' +
                        '   <a class="removeimg" href="javascript:" style="display: inline"></a>' +
                        '</div>'
                    );

                    // Thêm container vào thumbbox
                    $("#thumbbox").append(imageContainer);

                    // Lưu đường dẫn ảnh vào biến listUrlImage
                    listUrlImage.push(e.target.result);

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
                }
            };
            reader.readAsDataURL(input.files[i]);
        }
    }

    console.log(listUrlImage);
    // Hiển thị các phần tử khi có ảnh
    $(".Choicefile").css('background', '#14142B');
}


$(document).ready(function () {
    $(".Choicefile").bind('click', function () {
        $("#uploadfile").click();
    });
})

//Set Status
function toggleStatus(checkbox) {
    var productId = checkbox.getAttribute("data-product-id");
    // Gửi yêu cầu AJAX để cập nhật trạng thái của danh mục
    //Sử dụng jQuery
    $.ajax({
        type: "POST",
        url: "/admin/rest/product/setStatus/" + productId,
        success: function (response) {
            // Xử lý thành công, nếu cần
            console.log("Cập nhật trạng thái thành công");

            // Hiển thị thông báo thành công sử dụng SweetAlert2
            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Trạng thái đã được cập nhật thành công!'
            });
        },
        error: function (error) {
            // Xử lý lỗi, nếu cần
            console.error("Lỗi khi cập nhật trạng thái");

            // Hiển thị thông báo thất bại sử dụng SweetAlert2
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi cập nhật trạng thái.'
            });
        }
    });
}

// Hàm để cập nhật biểu mẫu với dữ liệu Sản phẩm
function updateProductForm(element) {
    // Sự kiện khi click sửa
    $('#thumbbox').empty();
    $("#myfileupload").html('<input type="file" id="uploadfile" name="ImageUpload" multiple onchange="readURL(this)"/>');
    listUrlImage = [];

    var productId = element.getAttribute("data-product-id");

    // Thêm thuộc tính để update
    $('#ProductModal').attr('product-id-update', productId);

    // Thực hiện AJAX request để lấy dữ sản phẩm mục từ backend
    $.ajax({
        type: 'GET',
        url: '/admin/rest/product/formUpdate/' + productId,
        success: function (product) {
            // Lấy dữ liệu ảnh
            getListURL(product.id);

            // Hiển thị hộp thoại modal
            $('#ProductModal').modal('show');

            // Điền dữ liệu vào các trường biểu mẫu
            $('#product-name').val(product.name);
            $('#product-category').val(product.category.id);
            $('#product-brand').val(product.brand.id);
            $('#product-material').val(product.material.id);
            CKEDITOR.instances['product-description'].setData(product.description);
        },
        error: function (error) {
            console.log('Error fetching product data:', error);
            // Xử lý lỗi nếu cần
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

function save() {
    var productId = $('#ProductModal').attr("product-id-update");
    var productName = $("#product-name").val();
    var productCategory = $("#product-category").val();
    var productBrand = $("#product-brand").val();
    var productMaterial = $("#product-material").val();
    var productDescription = CKEDITOR.instances['product-description'].getData();
    var productStatus = 0;

    var dataToSend = {
        name: productName,
        description: productDescription,
        categoryId: productCategory,
        brandId: productBrand,
        materialId: productMaterial,
        status: productStatus
    }
    console.log(dataToSend);

    // Gửi yêu cầu AJAX
    $.ajax({
        type: "PUT",
        url: "/admin/rest/product/update/" + productId,
        contentType: "application/json",
        data: JSON.stringify(dataToSend),
        success: function (response) {
            console.log("Lưu Sản phẩm thành công!");

            saveImage(productId);

            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Lưu Sản phẩm thành công!',
                didClose: function () {
                    window.location.href = "/admin/product";
                }
            });
        },
        error: function (error) {
            console.error("Lỗi khi lưu Sản phẩm:", error);

            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi lưu Sản phẩm!'
            });
        }
    });
}

function saveImage(productId) {
    // Clear tất cả sản phẩm có id = productId
    $.ajax({
        type: "DELETE",
        url: "/admin/rest/image/deleteAll/" + productId,
        success: function (response) {
            console.log("Xóa toàn bộ Ảnh tương ứng thành công!");

            for (var i = 0; i < listUrlImage.length; i++) {
                var dataToSend = {
                    productId: productId,
                    urlImage: listUrlImage[i]
                }
                // Gửi yêu cầu AJAX
                $.ajax({
                    type: "POST",
                    url: "/admin/rest/image/add",
                    contentType: "application/json",
                    data: JSON.stringify(dataToSend),
                    async: false,
                    success: function (response) {
                        console.log("Lưu Ảnh thành công!");
                    },
                    error: function (error) {
                        console.error("Lỗi khi lưu Ảnh:", error);
                        return false;
                    }
                });
            }
        },
        error: function (error) {
            console.error("Lỗi khi xóa Ảnh:", error);
            return false;
        }
    });
}

function getListProductDetail(button) {
    var productId = button.getAttribute('data-product-id');
    window.location.href = "/admin/product-detail/" + productId;
}