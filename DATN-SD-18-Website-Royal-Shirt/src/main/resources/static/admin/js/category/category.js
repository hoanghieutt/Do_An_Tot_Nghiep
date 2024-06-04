//Show form
$(document).ready(function () {
    $('#showFormCategory').click(function () {
        $('.modal-title').text("Thêm Danh Mục");
        $('#CategoryModal').modal('show');
    });
    $('#closeFormCategory').click(function () {
        $('#CategoryModal').modal('hide');
    });
});

//Add and update category
function saveCategory() {
    // Lấy dữ liệu từ biểu mẫu
    var categoryName = $("#categoryName").val().trim();
    var currentTime = moment().format('YYYY-MM-DD');
    var status = 0;
    var categoryId = $("#categoryForm").attr("category-id-update");

    // Check thông tin
    if (!checkInputCategory(categoryName)) {
        return;
    }

    var dataToSend = {
        name: categoryName,
        status: status,
    }

    // Nếu categoryId tồn tại -> update, ngược lại -> add
    if (categoryId) {
        dataToSend.id = categoryId;
        dataToSend.updatedDate = currentTime;
    } else {
        // Kiểm tra trùng tên danh mục
        if (!checkDuplicateCategory(categoryName)) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Tên danh mục đã tồn tại!'
            });
            return false;
        }
        dataToSend.createdDate = currentTime;
    }

    var url = categoryId ? "/admin/rest/category/update/" + categoryId : "/admin/rest/category/add";

    var method = categoryId ? "PUT" : "POST";

    // Gửi yêu cầu AJAX
    $.ajax({
        type: method,
        url: url,
        contentType: "application/json",
        data: JSON.stringify(dataToSend),
        success: function (response) {
            console.log("Lưu danh mục thành công!");

            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Lưu danh mục thành công!',
                didClose: function () {
                    location.reload();
                }
            });
        },
        error: function (error) {
            console.error("Lỗi khi lưu danh mục:", error);

            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi lưu danh mục!'
            });
        }
    });
}

// Check thông tin
function checkInputCategory(categoryName) {
    // Kiểm tra xem các trường có rỗng không
    if (categoryName === "") {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng điền đầy đủ thông tin danh mục!'
        });
        return false;
    }

    //Check Tên danh mục
    var nameRegex = /^[a-zA-ZÀ-ỹ\s]+$/;
    if (!nameRegex.test(categoryName)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Tên danh mục chỉ được chứa chữ cái và khoảng trắng!'
        });
        return false;
    }

    return true;
}


// Check trùng Tên danh mục
function checkDuplicateCategory(categoryName) {
    var isDuplicateName;
    // Gửi yêu cầu AJAX để kiểm tra trùng tên danh mục
    $.ajax({
        type: "POST",
        url: "/admin/rest/category/checkDuplicateName",
        contentType: "application/json",
        data: JSON.stringify({name: categoryName}),
        async: false,
        success: function (response) {
            isDuplicateName = response.isDuplicateName;
        },
        error: function (error) {
            console.error("Lỗi khi kiểm tra trùng tên danh mục:", error);
        }
    });
    return isDuplicateName;
}


// Hàm để cập nhật biểu mẫu với dữ liệu danh mục
function updateCategoryForm(element) {
    //Chỉnh sửa tên modal
    $('.modal-title').text("Chỉnh sửa Danh Mục");

    var categoryId = element.getAttribute("data-category-id");
    // Thêm thuộc tính để kiểm tra xem add hay update
    $('#categoryForm').attr('category-id-update', categoryId);
    // Thực hiện AJAX request để lấy dữ liệu danh mục từ backend
    $.ajax({
        type: 'GET',
        url: '/admin/rest/category/formUpdate/' + categoryId,
        success: function (category) {
            // Hiển thị hộp thoại modal
            $('#CategoryModal').modal('show');

            // Điền dữ liệu vào các trường biểu mẫu
            $('#categoryName').val(category.name);

            // Lắng nghe sự kiện đóng modal
            $('#CategoryModal').on('hidden.bs.modal', function () {
                // Xóa thuộc tính category-id-update khi modal đóng
                $('#categoryForm').removeAttr('category-id-update');
                // Làm mới input
                $('#categoryName').val(null);
            });
        },
        error: function (error) {
            console.log('Error fetching category data:', error);
            // Xử lý lỗi nếu cần
        }
    });
}

//Set Status
function toggleStatus(checkbox) {
    var categoryId = checkbox.getAttribute("data-category-id");
    // Gửi yêu cầu AJAX để cập nhật trạng thái của danh mục
    //Sử dụng jQuery
    $.ajax({
        type: "POST",
        url: "/admin/rest/category/setStatus/" + categoryId,
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