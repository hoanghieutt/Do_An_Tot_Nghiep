//Show form
$(document).ready(function () {
    $('#showFormMaterial').click(function () {
        //Chỉnh sửa tên modal
        $('.modal-title').text("Thêm Chất Liệu");
        $('#MaterialModal').modal('show');
    });
    $('#closeFormMaterial').click(function () {
        $('#MaterialModal').modal('hide');
    });
});

//Add and update material
function saveMaterial() {
    // Lấy dữ liệu từ biểu mẫu
    var materialName = $("#materialName").val().trim();
    var currentTime = moment().format('YYYY-MM-DD');
    var status = 0;
    var materialId = $("#materialForm").attr("material-id-update");

    // Check thông tin
    if (!checkInputMaterial(materialName)) {
        return;
    }

    var dataToSend = {
        name: materialName,
        status: status,
    }

    // Nếu materialId tồn tại -> update, ngược lại -> add
    if (materialId) {
        dataToSend.id = materialId;
        dataToSend.updatedDate = currentTime;
    } else {
        // Kiểm tra trùng tên chất liệu
        if (!checkDuplicateMaterial(materialName)) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Tên chất liệu đã tồn tại!'
            });
            return false;
        }
        dataToSend.createdDate = currentTime;
    }

    var url = materialId ? "/admin/rest/material/update/" + materialId : "/admin/rest/material/add";

    var method = materialId ? "PUT" : "POST";

    // Gửi yêu cầu AJAX
    $.ajax({
        type: method,
        url: url,
        contentType: "application/json",
        data: JSON.stringify(dataToSend),
        success: function (response) {
            console.log("Lưu chất liệu thành công!");

            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Lưu chất liệu thành công!',
                didClose: function () {
                    location.reload();
                }
            });
        },
        error: function (error) {
            console.error("Lỗi khi lưu chất liệu:", error);

            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi lưu chất liệu!'
            });
        }
    });
}

// Check thông tin
function checkInputMaterial(materialName) {
    // Kiểm tra xem các trường có rỗng không
    if (materialName === "") {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng điền đầy đủ thông tin chất liệu!'
        });
        return false;
    }

    //Check Tên chất liệu
    var nameRegex = /^[a-zA-ZÀ-ỹ\s]+$/;
    if (!nameRegex.test(materialName)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Tên chất liệu chỉ được chứa chữ cái và khoảng trắng!'
        });
        return false;
    }

    return true;
}


// Check trùng Tên chất liệu
function checkDuplicateMaterial(materialName) {
    var isDuplicateName;
    // Gửi yêu cầu AJAX để kiểm tra trùng tên chất liệu
    $.ajax({
        type: "POST",
        url: "/admin/rest/material/checkDuplicateName",
        contentType: "application/json",
        data: JSON.stringify({name: materialName}),
        async: false,
        success: function (response) {
            isDuplicateName = response.isDuplicateName;
        },
        error: function (error) {
            console.error("Lỗi khi kiểm tra trùng tên chất liệu:", error);
        }
    });
    return isDuplicateName;
}


// Hàm để cập nhật biểu mẫu với dữ liệu chất liệu
function updateMaterialForm(element) {
    //Chỉnh sửa tên modal
    $('.modal-title').text("Chỉnh sửa Chất Liệu");

    var materialId = element.getAttribute("data-material-id");
    // Thêm thuộc tính để kiểm tra xem add hay update
    $('#materialForm').attr('material-id-update', materialId);
    // Thực hiện AJAX request để lấy dữ liệu chất liệu từ backend
    $.ajax({
        type: 'GET',
        url: '/admin/rest/material/formUpdate/' + materialId,
        success: function (material) {
            // Hiển thị hộp thoại modal
            $('#MaterialModal').modal('show');

            // Điền dữ liệu vào các trường biểu mẫu
            $('#materialName').val(material.name);

            // Lắng nghe sự kiện đóng modal
            $('#MaterialModal').on('hidden.bs.modal', function () {
                // Xóa thuộc tính material-id-update khi modal đóng
                $('#materialForm').removeAttr('material-id-update');
                // Làm mới input
                $('#materialName').val(null);
            });
        },
        error: function (error) {
            console.log('Error fetching material data:', error);
            // Xử lý lỗi nếu cần
        }
    });
}

//Set Status
function toggleStatus(checkbox) {
    var materialId = checkbox.getAttribute("data-material-id");
    // Gửi yêu cầu AJAX để cập nhật trạng thái của chất liệu
    //Sử dụng jQuery
    $.ajax({
        type: "POST",
        url: "/admin/rest/material/setStatus/" + materialId,
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