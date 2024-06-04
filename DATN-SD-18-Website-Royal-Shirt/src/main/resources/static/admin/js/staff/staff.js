
//Show form
$(document).ready(function () {
    $('#showModalStaff').click(function () {
        $('.modal-title').text("Thêm Nhân Viên");
        $('#modalStaff').modal('show');
    });
    $('#closeFormStaff').click(function () {
        $('#modalStaff').modal('hide');
    });
});

var urlImage;

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            $("#thumbimage").attr('src', e.target.result);
            urlImage = e.target.result;

            // Tạo một container cho ảnh và nút xóa
            var imageContainer = $(
                '<div class="image-product-container">' +
                '   <img src="' + e.target.result + '" alt="Thumb image" height="150" width="100" id="thumbimage"/>' +
                '   <a class="removeimg" href="javascript:" style="display: inline"></a>' +
                '</div>'
            );

            // Thêm container vào thumbbox
            $("#thumbbox").empty();
            $("#thumbbox").append(imageContainer);

            // Sự kiện click cho nút xóa
            imageContainer.find(".removeimg").on("click", function () {
                // Xóa ảnh khỏi biến listUrlImage
                $("#thumbimage").attr('src', '').hide();
                urlImage = null;

                $(this).closest(".image-product-container").remove();

                 $("#myfileupload").html('<input type="file" id="uploadfile" name="ImageUpload" multiple onchange="readURL(this)"/>');
                 $('.Choicefile').css({
                    'background': '#14142B',
                    'cursor': 'pointer'
                 });
            });
        }
        reader.readAsDataURL(input.files[0]);
    }
}

function writeURL(url) {
    // Tạo một container cho ảnh và nút xóa
    var imageContainer = $(
        '<div class="image-product-container">' +
        '   <img src="' + url + '" alt="Thumb image" height="150" width="100" id="thumbimage"/>' +
        '   <a class="removeimg" href="javascript:" style="display: inline"></a>' +
        '</div>'
    );

    // Thêm container vào thumbbox
    $("#thumbbox").empty();
    $("#thumbbox").append(imageContainer);

    // Sự kiện click cho nút xóa
    imageContainer.find(".removeimg").on("click", function () {
        // Xóa ảnh khỏi biến urlImage
        urlImage = null;

        $(this).closest(".image-product-container").remove();

        $("#myfileupload").html('<input type="file" id="uploadfile" name="ImageUpload" multiple onchange="readURL(this);"/>');
        $('.Choicefile').css({
            'background': '#14142B',
            'cursor': 'pointer'
        });
    });
}

$(document).ready(function () {
    $(".Choicefile").bind('click', function () {
        $("#uploadfile").click();
    });
})

// set status staff
function setStatusStaff(button) {
    var staffId = button.getAttribute("data-id");

    Swal.fire({
        title: "Xác nhận",
        text: "Bạn có chắc chắn muốn thay đổi trạng thái của nhân viên này?",
        icon: "warning",
        showCancelButton: true, // Hiển thị nút Hủy
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Đồng ý",
        cancelButtonText: "Hủy"
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                type: "POST",
                url: "/admin/rest/staffs/setStatus/" + staffId,
                success: function (response) {
                    Swal.fire({
                        title: "Thành công!",
                        text: "Thay đổi trạng thái thành công!",
                        icon: "success",
                    }).then(() => {
                        // Reload trang sau khi thành công
                        location.reload();
                    });
                },
                error: function () {
                    Swal.fire({
                        title: "Lỗi!",
                        text: "Đã xảy ra lỗi khi thay đổi trạng thái!",
                        icon: "error",
                    });
                }
            });
        }
    });
}

// save or update staff
function saveOrUpdateStaff() {
    var name = $("#name").val().trim();
    var email = $("#email").val().trim();
    var phone = $("#phone").val().trim();
    var address = $("#address").val().trim();
    var role = $("#role").val();
    var password =  $("#password").val().trim();
    var currentTime = moment().format('YYYY-MM-DD');
    var staffId = $("#staffForm").attr("staff-id-update");

    var dataSend = {
        name: name,
        email: email,
        phone: phone,
        address: address,
        password: password,
        roleId: role,
        avatar: urlImage
    }
    // Check thông tin
    if (!validateInputStaff(name, email, phone, address, password)) {
        return;
    }

    // Nếu  idStaff tồn tại -> update, ngược lại -> create
    if (staffId) {
        dataSend.id = staffId;
        dataSend.updatedDate = currentTime;
    } else {
        // Kiểm tra trùng email
        if (!validateDuplicateEmail(email)) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Email đã tồn tại!'
            });
            return false;
        }
        dataSend.createdDate = currentTime;
    }

    var url = staffId ? "/admin/rest/staffs/update/" + staffId : "/admin/rest/staffs/store";
    var method = staffId ? "PUT" : "POST";

    // Gửi yêu cầu AJAX
    $.ajax({
        type: method,
        url: url,
        contentType: "application/json",
        data: JSON.stringify(dataSend),
        success: function (response) {
            console.log("Lưu nhân viên thành công!");
            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Lưu nhân viên thành công!',
                didClose: function () {
                    window.location.href = "/admin/staffs";
                }
            });
        },
        error: function (error) {
            console.error("Lỗi khi lưu nhân viên:", error);
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi lưu nhân viên!'
            });
        }
    });
}

//validate input form staff
function validateInputStaff(name, email, phone, address, password) {
    // Kiểm tra xem các trường có rỗng không
    if (name === "" || email === "" || phone === "" || address === "" || password === "") {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng điền đầy đủ thông tin!'
        });
        return false;
    }
    //Check Tên thương hiệu
    var nameRegex = /^[a-zA-ZÀ-ỹ\s]+$/;
    if (!nameRegex.test(name)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Tên nhân viên không hợp lệ!'
        });
        return false;
    }
    // Kiểm tra định dạng email
    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Email không hợp lệ!'
        });
        return false;
    }
    // Kiểm tra định dạng số điện thoại
    var phoneRegex = /^(0|\+84)\d{9,10}$/;
    if (!phoneRegex.test(phone)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Số điện thoại không hợp lệ!'
        });
        return false;
    }
    return true;
}

// Check trùng email staff
function validateDuplicateEmail(email) {
    var existsEmail;
    // Gửi yêu cầu AJAX để kiểm tra trùng email
    $.ajax({
        type: "POST",
        url: "/admin/rest/staffs/validateDuplicateEmail",
        contentType: "application/json",
        data: JSON.stringify({email: email}),
        async: false,
        success: function (response) {
            existsEmail = response.existsEmail;
        },
        error: function (error) {
            console.error("Lỗi khi kiểm tra trùng tên Email:", error);
        }
    });
    return existsEmail;
}

function updateStaff(element) {
    //Chỉnh sửa tên modal
    $('.modal-title').text("Chỉnh sửa thông tin nhân viên");

    var staffId = element.getAttribute("data-id");

    // Thêm thuộc tính để kiểm tra xem add hay update
    $('#staffForm').attr('staff-id-update', staffId);
    $.ajax({
        type: 'GET',
        url: '/admin/rest/staffs/formUpdate/' + staffId,
        success: function (staff) {
            // Hiển thị hộp thoại modal
            $('#modalStaff').modal('show');

            // Điền dữ liệu vào các trường biểu mẫu
            $('#name').val(staff.name);
            $('#email').val(staff.email);
            $('#phone').val(staff.phone);
            $('#address').val(staff.address);
            $('#password').val(staff.password);
            $('#role').val(staff.role.id);
            urlImage = staff.avatar;
            writeURL(urlImage);

            // Lắng nghe sự kiện đóng modal
            $('#modalStaff').on('hidden.bs.modal', function () {
                // Xóa thuộc tính brand-id-update khi modal đóng
                $('#staffForm').removeAttr('brand-id-update');

                // Làm mới input
                $('#name').val(null);
                $('#email').val(null);
                $('#phone').val(null);
                $('#address').val(null);
                $('#password').val(null);
                $("#thumbbox").empty();
            });
        },
        error: function (error) {
            console.log('Error fetching brand data:', error);
            // Xử lý lỗi nếu cần
        }
    });
}




