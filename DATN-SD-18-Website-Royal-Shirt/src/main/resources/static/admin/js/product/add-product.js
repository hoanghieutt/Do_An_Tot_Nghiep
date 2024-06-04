var selectColor, selectSize;
var listUrlImage = [];
var listProductDetail = [];

$(document).ready(function () {
    //Mở form add
    $('#quick_edit').click(function () {
        if (listProductDetail.length === 0) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Danh sách biến thể trống!'
            });
        } else {
            $('.modal-title').text("Chỉnh Sửa Nhanh");
            $('#QuickEditModal').modal('show');
        }
    });

    //Đóng form add
    $('#saveQuickEditModal').click(function () {
        $('#QuickEditModal').modal('hide');
    });

    // Khởi tạo Select màu sắc và kích thước
    selectColor = $('#select-colors').selectize({
        maxItems: null,
        valueField: 'id',
        labelField: 'title',
        searchField: 'title',
        create: false
    });

    selectSize = $('#select-sizes').selectize({
        maxItems: null,
        valueField: 'id',
        labelField: 'title',
        searchField: 'title',
        create: false
    });

    // Gọi API để lấy thông tin từ server
    $.ajax({
        url: '/admin/rest/add-product',
        type: 'GET',
        success: function (data) {
            // Tạo options màu sắc từ danh sách màu trong productListResponse
            var colorOptions = data.colors.map(function (color) {
                return {id: color.id, title: color.name};
            });

            // Tạo options kích thước từ kích thước trong productListResponse
            var sizeOptions = data.sizes.map(function (size) {
                return {id: size.id, title: size.name};
            });

            // Cập nhật options của màu sắc
            selectColor[0].selectize.clearOptions();
            selectColor[0].selectize.addOption(colorOptions);
            selectColor[0].selectize.refreshItems();

            // Cập nhật options của kích thước
            selectSize[0].selectize.clearOptions();
            selectSize[0].selectize.addOption(sizeOptions);
            selectSize[0].selectize.refreshItems();
        },
        error: function (err) {
            console.error('Error call API:', err);
        }
    });

});

function quickEdit() {
    var quantity = $("#quick-edit-quantity").val();
    var price = $("#quick-edit-price").val();

    if (quantity == 0 || price == 0) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng nhập đầy đủ thông tin!!'
        });
        return;
    }

    // Đặt giá mới cho tất cả các ô input có class "gia_ban"
    $(".gia_ban").each(function(){
        $(this).val(price);
    });

    // Đặt số lượng mới cho tất cả các ô input có class "so_luong"
    $(".so_luong").each(function(){
        $(this).val(quantity);
    });

    for (var i = 0; i < listProductDetail.length; i++) {
        // Lấy giá trị mới từ input tương ứng và cập nhật vào productDetail
        listProductDetail[i].quantity = quantity;
        listProductDetail[i].price = price;
    }

    console.log(listProductDetail);

    //Đóng form add
    $('#closeQuickEditModal').click(function () {
        $('#QuickEditModal').modal('hide');
    });

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
                        $('.Choicefile').css('cursor', 'pointer');
                    });
                }
            };
            reader.readAsDataURL(input.files[i]);
        }
    }

    console.log(listUrlImage);
    // Hiển thị các phần tử khi có ảnh
    $(".Choicefile").css('background', '#14142B');
    $(".Choicefile").css('cursor', 'default');
}

$(document).ready(function () {
    $(".Choicefile").bind('click', function () {
        $("#uploadfile").click();
    });
})

function checkInputShowList(productName, productCategory, productBrand, productMaterial, productColor, productSize, productDescription) {
    if (productName.trim() === "") {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng nhập Tên sản phẩm!'
        });
        return false;
    }
    if (!checkDuplicateProduct(productName)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Tên Sản phẩm đã tồn tại!'
        });
        return false;
    }
    if (productCategory === null) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng chọn Danh mục!'
        });
        return false;
    }
    if (productBrand === null) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng chọn Thương hiệu!'
        });
        return false;
    }
    if (productMaterial === null) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng chọn Chất liệu!'
        });
        return false;
    }
    if (productColor.length === 0) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng chọn Màu sắc!'
        });
        return false;
    }
    if (productSize.length === 0) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng chọn Kích thước!'
        });
        return false;
    }

    if (listUrlImage.length === 0) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng nhập chọn Ảnh!'
        });
        return false;
    }

    if (productDescription.trim() === "") {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng nhập Mô tả sản phẩm!'
        });
        return false;
    }
    return true;
}

// Hàm để lấy danh sách sản phẩm chi tiết từ các mục đã chọn
function showListProductDetail() {
    // Clear listProductDetail
    listProductDetail = [];

    // Lấy giá trị của ô nhập liệu Tên sản phẩm
    var productName = $("#input-product-name").val();

    var productCategory = $("#select-category").val();

    var productBrand = $("#select-brand").val();

    var productMaterial = $("#select-material").val();

    var productDescription = CKEDITOR.instances['input-product-description'].getData();

    // Lấy thông tin chi tiết về các mục đã chọn từ dropdown màu sắc
    var selectedColorItems = selectColor[0].selectize.items.map(function (item) {
        var option = selectColor[0].selectize.getItem(item);
        return {id: item, name: option.text()};
    });

    // Lấy thông tin chi tiết về các mục đã chọn từ dropdown kích thước
    var selectedSizeItems = selectSize[0].selectize.items.map(function (item) {
        var option = selectSize[0].selectize.getItem(item);
        return {id: item, name: option.text()};
    });

    if (!checkInputShowList(productName, productCategory, productBrand, productMaterial, selectedColorItems, selectedSizeItems, productDescription)) {
        return;
    } else {
        renderListProductDetail(selectedColorItems, selectedSizeItems, 10, 1000000);
    }
}

function renderListProductDetail(selectedColorItems, selectedSizeItems, quantity, price) {
    // Biến đếm STT
    var sttCounter = 1;

    // Lấy giá trị của ô nhập liệu Tên sản phẩm
    var productName = $("#input-product-name").val();

    var listProductDetailShow = $("#listProductDetail");

    // Xóa tất cả các phần tử trong tbody
    listProductDetailShow.empty();

    // Gọi hàm khác hoặc thực hiện các xử lý khác với listColor và listSize ở đây
    for (var i = 0; i < selectedColorItems.length; i++) {
        for (var j = 0; j < selectedSizeItems.length; j++) {

            // Tạo một biến productDetail để giữ các thuộc tính
            var productDetail = {
                productId: null,
                quantity: quantity,
                price: price,
                colorId: selectedColorItems[i].id, // Giá color id hiện tại
                sizeId: selectedSizeItems[j].id // Giá size id hiện tại
            };

            // Thêm productDetail vào listProductDetail
            listProductDetail.push(productDetail);

            var productDetailRow = $(
                '<tr>\n' +
                '    <td class="stt">' + sttCounter + '</td>\n' +
                '    <td class="ten_danh_muc">' + productName + '</td>\n' +
                '    <td class="mau_sac">' + selectedColorItems[i].name + '</td>\n' +
                '    <td class="kich_thuoc">' + selectedSizeItems[j].name + '</td>\n' +
                // '    <td class="trong_luong" id="trong_luong"><input class="product-detail-input trong_luong" type="number" value="' + productDetail.weight + '" min="0"></td>\n' +
                '    <td class="so_luong"><input class="product-detail-input so_luong" type="number" value="' + productDetail.quantity + '" min="0"></td>\n' +
                '    <td class="gia_ban" id="gia_ban"><input class="product-detail-input gia_ban" type="number" value="' + productDetail.price + '" min="0"></td>\n' +
                '    <td class="table-td-center tinh_nang">\n' +
                '        <button class="btn btn-primary btn-sm trash" type="button" title="Xóa">' +
                '            <i class="fas fa-trash-alt"></i>\n' +
                '        </button>\n' +
                '    </td>' +
                '</tr>'
            );

            // Thêm productDetail vào dữ liệu của hàng
            productDetailRow.data('productDetail', productDetail);

            // Thêm hàng vào bảng
            $(productDetailRow).appendTo("#listProductDetail");

            // Tăng biến đếm STT
            sttCounter++;

            // Thêm sự kiện onchange cho các ô nhập liệu
            $(productDetailRow).find('.product-detail-input').on('input', function () {
                // Lấy productDetail từ dữ liệu của hàng
                var row = $(this).closest('tr');
                var productDetail = row.data('productDetail');

                // Cập nhật thuộc tính tương ứng của productDetail
                // if ($(this).hasClass('trong_luong')) {
                //     productDetail.weight = $(this).val();
                // } else
                if ($(this).hasClass('so_luong')) {
                    productDetail.quantity = $(this).val();
                } else if ($(this).hasClass('gia_ban')) {
                    productDetail.price = $(this).val();
                }

                // Lưu lại productDetail vào dữ liệu của hàng
                row.data('productDetail', productDetail);

                // Cập nhật thông tin trong listProductDetail
                var indexInList = row.index(); // Lấy index của hàng trong bảng
                listProductDetail[indexInList] = productDetail;

                console.log(listProductDetail);
            });

            // Thêm sự kiện click cho nút xóa
            $(productDetailRow).find('.trash').click(function () {
                var indexInList = $(this).closest('tr').index(); // Lấy index của hàng trong bảng
                listProductDetail.splice(indexInList, 1); // Xóa productDetail tương ứng khỏi listProductDetail

                $(this).closest('tr').remove(); // Xóa hàng chứa nút xóa được click
                updateSTT(); // Cập nhật lại giá trị STT

                console.log(listProductDetail);
            });
        }
    }
    console.log(listProductDetail[0].quantity);
}

// Hàm để cập nhật lại giá trị STT cho tất cả các hàng trong bảng
function updateSTT() {
    $('#listProductDetail tr').each(function (index, row) {
        $(row).find('.stt').text(index + 1);
    });
}

function save() {
    if (listProductDetail.length === 0) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Danh sách biến thể trống!'
        });
        return;
    }

    var productName = $("#input-product-name").val();

    var productCategory = $("#select-category").val();

    var productBrand = $("#select-brand").val();

    var productMaterial = $("#select-material").val();

    var productDescription = CKEDITOR.instances['input-product-description'].getData();

    var productStatus = 0;

    var dataToSend = {
        name: productName,
        description: productDescription,
        categoryId: productCategory,
        brandId: productBrand,
        materialId: productMaterial,
        status: productStatus
    }

    // Gửi yêu cầu AJAX
    $.ajax({
        type: "POST",
        url: "/admin/rest/product/add",
        contentType: "application/json",
        data: JSON.stringify(dataToSend),
        success: function (response) {
            console.log("Lưu Sản phẩm thành công!");
            var productId = response.id;
            if (!saveImage(productId)) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: 'Có lỗi xảy ra khi lưu ảnh!'
                });
                return;
            }
            if (!saveProductDetail(productId)) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: 'Có lỗi xảy ra khi lưu Danh sách biến thể!'
                });
                return;
            }

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

// Check trùng Tên danh mục
function checkDuplicateProduct(productName) {
    var isDuplicateName;
    // Gửi yêu cầu AJAX để kiểm tra trùng tên danh mục
    $.ajax({
        type: "POST",
        url: "/admin/rest/add-product/checkDuplicateName",
        contentType: "application/json",
        data: JSON.stringify({name: productName}),
        async: false,
        success: function (response) {
            console.log("Kiểm tra trùng tên Sản phẩm thành công!");
            isDuplicateName = response.isDuplicateName;
        },
        error: function (error) {
            console.error("Lỗi khi kiểm tra trùng tên Sản phẩm:", error);
        }
    });
    return isDuplicateName;
}

function saveImage(productId) {
    for (var i = 0; i < listUrlImage.length; i++) {
        var dataToSend = {
            productId: productId,
            urlImage: listUrlImage[i],
            status: 0
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
    return true;
}

function saveProductDetail(productId) {
    for (var i = 0; i < listProductDetail.length; i++) {
        var dataToSend = {
            productId: productId,
            quantity: listProductDetail[i].quantity,
            price: listProductDetail[i].price,
            // weight: listProductDetail[i].weight,
            colorId: listProductDetail[i].colorId,
            sizeId: listProductDetail[i].sizeId,
            status: 0
        }
        // Gửi yêu cầu AJAX
        $.ajax({
            type: "POST",
            url: "/admin/rest/product-detail/add",
            contentType: "application/json",
            data: JSON.stringify(dataToSend),
            success: function (response) {
                console.log("Lưu Danh sách biến thể thành công!");
            },
            error: function (error) {
                console.error("Lỗi khi lưu sách biến thể:", error);
                return false;
            }
        });
    }
    return true;
}





