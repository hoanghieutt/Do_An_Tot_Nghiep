//Show form
$(document).ready(function () {
    $('#showFormSize').click(function () {
        //Chỉnh sửa tên modal
        $('.modal-title').text("Thêm Kích Thước");
        $('#SizeModal').modal('show');
    });
    $('#closeFormSize').click(function () {
        $('#SizeModal').modal('hide');
    });
});

//Add and update size
function saveSize() {
    // Lấy dữ liệu từ biểu mẫu
    var sizeName = $("#sizeName").val().trim();
    var sizeShirtLength = $("#sizeShirtLength").val().trim();
    var sizeShirtWidth = $("#sizeShirtWidth").val().trim();
    var currentTime = moment().format('YYYY-MM-DD');
    var status = 0;
    var sizeId = $("#sizeForm").attr("size-id-update");

    // Check thông tin
    if (!checkInputSize(sizeName,sizeShirtLength,sizeShirtWidth)) {
        return;
    }

    var dataToSend = {
        name: sizeName,
        status: status,
        shirtLength: sizeShirtLength,
        shirtWidth: sizeShirtWidth
    }

    // Nếu sizeId tồn tại -> update, ngược lại -> add
    if (sizeId) {
        dataToSend.id = sizeId;
        dataToSend.updatedDate = currentTime;
    } else {
        // Kiểm tra trùng kích thước
        if (!checkDuplicateSize(sizeName)) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Kích thước đã tồn tại!'
            });
            return false;
        }
        dataToSend.createdDate = currentTime;
    }

    var url = sizeId ? "/admin/rest/size/update/" + sizeId : "/admin/rest/size/add";

    var method = sizeId ? "PUT" : "POST";

    // Gửi yêu cầu AJAX
    $.ajax({
        type: method,
        url: url,
        contentType: "application/json",
        data: JSON.stringify(dataToSend),
        success: function (response) {
            console.log("Lưu kích thước thành công!");

            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Lưu kích thước thành công!',
                didClose: function () {
                    location.reload();
                }
            });
        },
        error: function (error) {
            console.error("Lỗi khi lưu kích thước:", error);

            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi lưu kích thước!'
            });
        }
    });
}

// Check thông tin
function checkInputSize(sizeName,sizeShirtLength,sizeShirtWidth) {
    // Kiểm tra xem các trường có rỗng không
    if (sizeName === "" || sizeShirtLength === "" || sizeShirtWidth === "") {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng điền đầy đủ thông tin kích thước!'
        });
        return false;
    }

    //Check Kích thước
    var nameRegex = /^[a-zA-ZÀ-ỹ\d\s]+$/;
    if (!nameRegex.test(sizeName)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Kích thước không được chứa ký tự đặc biệt!'
        });
        return false;
    }

    var lengAndWidthRegex = /^\d+$/;
    if(sizeShirtLength <= 0 || sizeShirtWidth <= 0){
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Thông số kích thước không hợp lệ!'
        });
        return false;
    }
    if (!lengAndWidthRegex.test(sizeShirtLength) || !lengAndWidthRegex.test(sizeShirtWidth)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Thông số kích thước không được chứa chữ cái và ký tự đặc biệt!'
        });
        return false;
    }

    return true;
}


// Check trùng Kích thước
function checkDuplicateSize(sizeName) {
    var isDuplicateName;
    // Gửi yêu cầu AJAX để kiểm tra trùng kích thước
    $.ajax({
        type: "POST",
        url: "/admin/rest/size/checkDuplicateName",
        contentType: "application/json",
        data: JSON.stringify({name: sizeName}),
        async: false,
        success: function (response) {
            isDuplicateName = response.isDuplicateName;
        },
        error: function (error) {
            console.error("Lỗi khi kiểm tra trùng kích thước:", error);
        }
    });
    return isDuplicateName;
}


// Hàm để cập nhật biểu mẫu với dữ liệu kích thước
function updateSizeForm(element) {
    //Chỉnh sửa tên modal
    $('.modal-title').text("Chỉnh sửa Kích Thước");

    var sizeId = element.getAttribute("data-size-id");
    // Thêm thuộc tính để kiểm tra xem add hay update
    $('#sizeForm').attr('size-id-update', sizeId);
    // Thực hiện AJAX request để lấy dữ liệu kích thước từ backend
    $.ajax({
        type: 'GET',
        url: '/admin/rest/size/formUpdate/' + sizeId,
        success: function (size) {
            // Hiển thị hộp thoại modal
            $('#SizeModal').modal('show');

            // Điền dữ liệu vào các trường biểu mẫu
            $('#sizeName').val(size.name);
            $('#sizeShirtLength').val(size.shirtLength);
            $('#sizeShirtWidth').val(size.shirtWidth);

            // Lắng nghe sự kiện đóng modal
            $('#SizeModal').on('hidden.bs.modal', function () {
                // Xóa thuộc tính size-id-update khi modal đóng
                $('#sizeForm').removeAttr('size-id-update');
                // Làm mới input
                $('#sizeName').val(null);
            });
        },
        error: function (error) {
            console.log('Error fetching size data:', error);
            // Xử lý lỗi nếu cần
        }
    });
}

//Set Status
function toggleStatus(checkbox) {
    var sizeId = checkbox.getAttribute("data-size-id");
    // Gửi yêu cầu AJAX để cập nhật trạng thái của kích thước
    //Sử dụng jQuery
    $.ajax({
        type: "POST",
        url: "/admin/rest/size/setStatus/" + sizeId,
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