//Show form
$(document).ready(function () {
    // Hiển thị modal chức vụ khi click
    $('#showModalRole').click(function () {
        $('.modal-title').text("Thêm Chức Vụ");
        $('#roleModal').modal('show');
    });

    // Đóng modal chức vụ khi click
    $('#closeModalRole').click(function () {
        $('#roleModal').modal('hide');
    });
});

// save role
function saveRole() {
    var roleName = $("#roleName").val().trim();

    // Check thông tin
    if (roleName === "") {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng điền đầy đủ thông tin!'
        });
        return false;
    }
    //Check Tên thương hiệu
    var nameRoleRegex = /^[a-zA-ZÀ-ỹ\s]+$/;
    if (!nameRoleRegex.test(roleName)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Tên chỉ được chứa chữ cái và khoảng trắng!'
        });
        return false;
    }

    //     Kiểm tra trùng tên danh mục
    if (!validateDuplicateRoleName(roleName)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Tên chức vụ đã tồn tại!'
        });
        return false;
    }

    // Gửi yêu cầu AJAX
    $.ajax({
        type: "POST",
        url: "/admin/rest/roles/store",
        contentType: "application/json",
        data: JSON.stringify({
            name: roleName
        }),
        success: function(response) {
            Swal.fire({
                title: "Thành công!",
                icon: "success",
                text: "Lưu chức vụ thành công!",
                didClose: function () {
                    location.reload();
                }
            });

        },
        error: function(error) {
            Swal.fire({
                title: "Thất bại!",
                icon: "error",
                text: "Đã xảy ra lỗi khi lưu chức vụ!"
            });
        }
    });
}

// validate trùng name role
function validateDuplicateRoleName(roleName) {
    var existsRoleName;
    // Gửi yêu cầu AJAX để kiểm tra trùng roleName
    $.ajax({
        type: "POST",
        url: "/admin/rest/roles/validateDuplicateRoleName",
        contentType: "application/json",
        data: JSON.stringify({
            name: roleName
        }),
        async: false,
        success: function (response) {
            existsRoleName = response.existsRoleName;
        },
        error: function (error) {
            console.error("Lỗi khi kiểm tra trùng tên Chức vụ:", error);
        }
    });
    return existsRoleName;
}