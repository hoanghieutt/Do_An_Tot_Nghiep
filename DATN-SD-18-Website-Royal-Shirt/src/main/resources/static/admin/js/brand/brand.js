//Show form
$(document).ready(function () {
    $('#showFormBrand').click(function () {
        $('.modal-title').text("Thêm Thương Hiệu");
        $('#BrandModal').modal('show');
    });
    $('#closeFormBrand').click(function () {
        $('#BrandModal').modal('hide');
    });
});

//Add and update brand
function saveBrand() {
    // Lấy dữ liệu từ biểu mẫu
    var brandName = $("#brandName").val().trim();
    var currentTime = moment().format('YYYY-MM-DD');
    var status = 0;
    var brandId = $("#brandForm").attr("brand-id-update");

    // Check thông tin
    if (!checkInputBrand(brandName)) {
        return;
    }

    var dataToSend = {
        name: brandName,
        status: status,
    }

    // Nếu brandId tồn tại -> update, ngược lại -> add
    if (brandId) {
        dataToSend.id = brandId;
        dataToSend.updatedDate = currentTime;
    } else {
        // Kiểm tra trùng tên thương hiệu
        if (!checkDuplicateBrand(brandName)) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Tên thương hiệu đã tồn tại!'
            });
            return false;
        }
        dataToSend.createdDate = currentTime;
    }

    var url = brandId ? "/admin/rest/brand/update/" + brandId : "/admin/rest/brand/add";

    var method = brandId ? "PUT" : "POST";

    // Gửi yêu cầu AJAX
    $.ajax({
        type: method,
        url: url,
        contentType: "application/json",
        data: JSON.stringify(dataToSend),
        success: function (response) {
            console.log("Lưu thương hiệu thành công!");

            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Lưu thương hiệu thành công!',
                didClose: function () {
                    location.reload();
                }
            });
        },
        error: function (error) {
            console.error("Lỗi khi lưu thương hiệu:", error);

            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi lưu thương hiệu!'
            });
        }
    });
}

// Check thông tin
function checkInputBrand(brandName) {
    // Kiểm tra xem các trường có rỗng không
    if (brandName === "") {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng điền đầy đủ thông tin thương hiệu!'
        });
        return false;
    }

    //Check Tên thương hiệu
    var nameRegex = /^[a-zA-ZÀ-ỹ\s]+$/;
    if (!nameRegex.test(brandName)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Tên thương hiệu chỉ được chứa chữ cái và khoảng trắng!'
        });
        return false;
    }

    return true;
}


// Check trùng Tên thương hiệu
function checkDuplicateBrand(brandName) {
    var isDuplicateName;
    // Gửi yêu cầu AJAX để kiểm tra trùng tên thương hiệu
    $.ajax({
        type: "POST",
        url: "/admin/rest/brand/checkDuplicateName",
        contentType: "application/json",
        data: JSON.stringify({name: brandName}),
        async: false,
        success: function (response) {
            isDuplicateName = response.isDuplicateName;
        },
        error: function (error) {
            console.error("Lỗi khi kiểm tra trùng tên thương hiệu:", error);
        }
    });
    return isDuplicateName;
}


// Hàm để cập nhật biểu mẫu với dữ liệu thương hiệu
function updateBrandForm(element) {
    //Chỉnh sửa tên modal
    $('.modal-title').text("Chỉnh sửa Thương Hiệu");

    var brandId = element.getAttribute("data-brand-id");
    // Thêm thuộc tính để kiểm tra xem add hay update
    $('#brandForm').attr('brand-id-update', brandId);
    // Thực hiện AJAX request để lấy dữ liệu thương hiệu từ backend
    $.ajax({
        type: 'GET',
        url: '/admin/rest/brand/formUpdate/' + brandId,
        success: function (brand) {
            // Hiển thị hộp thoại modal
            $('#BrandModal').modal('show');

            // Điền dữ liệu vào các trường biểu mẫu
            $('#brandName').val(brand.name);

            // Lắng nghe sự kiện đóng modal
            $('#BrandModal').on('hidden.bs.modal', function () {
                // Xóa thuộc tính brand-id-update khi modal đóng
                $('#brandForm').removeAttr('brand-id-update');
                // Làm mới input
                $('#brandName').val(null);
            });
        },
        error: function (error) {
            console.log('Error fetching brand data:', error);
            // Xử lý lỗi nếu cần
        }
    });
}

//Set Status
function toggleStatus(checkbox) {
    var brandId = checkbox.getAttribute("data-brand-id");
    // Gửi yêu cầu AJAX để cập nhật trạng thái của thương hiệu
    //Sử dụng jQuery
    $.ajax({
        type: "POST",
        url: "/admin/rest/brand/setStatus/" + brandId,
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