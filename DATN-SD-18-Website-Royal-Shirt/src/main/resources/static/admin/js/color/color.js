//Show form
$(document).ready(function () {
    $('#showFormColor').click(function () {
        //Chỉnh sửa tên modal
        $('.modal-title').text("Thêm Màu Sắc");
        $('#ColorModal').modal('show');
    });
    $('#closeFormColor').click(function () {
        $('#ColorModal').modal('hide');
    });
});

//Add and update color
function saveColor() {
    // Lấy dữ liệu từ biểu mẫu
    var colorName = $("#colorName").val().trim();
    var currentTime = moment().format('YYYY-MM-DD');
    var status = 0;
    var colorId = $("#colorForm").attr("color-id-update");

    // Check thông tin
    if (!checkInputColor(colorName)) {
        return;
    }

    var dataToSend = {
        name: colorName,
        status: status,
    }

    // Nếu colorId tồn tại -> update, ngược lại -> add
    if (colorId) {
        dataToSend.id = colorId;
        dataToSend.updatedDate = currentTime;
    } else {
        // Kiểm tra trùng tên màu sắc
        if (!checkDuplicateColor(colorName)) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Tên màu sắc đã tồn tại!'
            });
            return false;
        }
        dataToSend.createdDate = currentTime;
    }

    var url = colorId ? "/admin/rest/color/update/" + colorId : "/admin/rest/color/add";

    var method = colorId ? "PUT" : "POST";

    // Gửi yêu cầu AJAX
    $.ajax({
        type: method,
        url: url,
        contentType: "application/json",
        data: JSON.stringify(dataToSend),
        success: function (response) {
            console.log("Lưu màu sắc thành công!");

            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Lưu màu sắc thành công!',
                didClose: function () {
                    location.reload();
                }
            });
        },
        error: function (error) {
            console.error("Lỗi khi lưu màu sắc:", error);

            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi lưu màu sắc!'
            });
        }
    });
}

// Check thông tin
function checkInputColor(colorName) {
    // Kiểm tra xem các trường có rỗng không
    if (colorName === "") {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng điền đầy đủ thông tin màu sắc!'
        });
        return false;
    }

    //Check Tên màu sắc
    var nameRegex = /^[a-zA-ZÀ-ỹ\s]+$/;
    if (!nameRegex.test(colorName)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Tên màu sắc chỉ được chứa chữ cái và khoảng trắng!'
        });
        return false;
    }

    return true;
}


// Check trùng Tên màu sắc
function checkDuplicateColor(colorName) {
    var isDuplicateName;
    // Gửi yêu cầu AJAX để kiểm tra trùng tên màu sắc
    $.ajax({
        type: "POST",
        url: "/admin/rest/color/checkDuplicateName",
        contentType: "application/json",
        data: JSON.stringify({name: colorName}),
        async: false,
        success: function (response) {
            isDuplicateName = response.isDuplicateName;
        },
        error: function (error) {
            console.error("Lỗi khi kiểm tra trùng tên màu sắc:", error);
        }
    });
    return isDuplicateName;
}


// Hàm để cập nhật biểu mẫu với dữ liệu màu sắc
function updateColorForm(element) {
    //Chỉnh sửa tên modal
    $('.modal-title').text("Chỉnh sửa Màu Sắc");

    var colorId = element.getAttribute("data-color-id");
    // Thêm thuộc tính để kiểm tra xem add hay update
    $('#colorForm').attr('color-id-update', colorId);
    // Thực hiện AJAX request để lấy dữ liệu màu sắc từ backend
    $.ajax({
        type: 'GET',
        url: '/admin/rest/color/formUpdate/' + colorId,
        success: function (color) {
            // Hiển thị hộp thoại modal
            $('#ColorModal').modal('show');

            // Điền dữ liệu vào các trường biểu mẫu
            $('#colorName').val(color.name);

            // Lắng nghe sự kiện đóng modal
            $('#ColorModal').on('hidden.bs.modal', function () {
                // Xóa thuộc tính color-id-update khi modal đóng
                $('#colorForm').removeAttr('color-id-update');
                // Làm mới input
                $('#colorName').val(null);
            });
        },
        error: function (error) {
            console.log('Error fetching color data:', error);
            // Xử lý lỗi nếu cần
        }
    });
}

//Set Status
function toggleStatus(checkbox) {
    var colorId = checkbox.getAttribute("data-color-id");
    // Gửi yêu cầu AJAX để cập nhật trạng thái của màu sắc
    //Sử dụng jQuery
    $.ajax({
        type: "POST",
        url: "/admin/rest/color/setStatus/" + colorId,
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