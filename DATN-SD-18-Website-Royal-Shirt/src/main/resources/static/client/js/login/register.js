
function registerCustomer() {
    var name = $("#name").val().trim();
    var email = $("#email").val().trim();
    var phone = $("#phone").val().trim();
    var password = $("#password").val().trim();

    var dataToSend = {
        name: name,
        email: email,
        phone: phone,
        password: password
    }

    // Check thông tin
    if (!validateInputRegister(name, email, phone, password)) {
        return;
    }
    // Kiểm tra trùng email
    if (!validateDuplicateEmail(email)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Email đã tồn tại!'
        });
        return false;
    }
    // Kiểm tra trùng phone
    if (!validateDuplicatePhone(phone)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Phone đã tồn tại!'
        });
        return false;
    }

    $.ajax({
        type: "POST",
        url: "/rest/register",
        contentType: "application/json",
        data: JSON.stringify(dataToSend),
        success: function (response) {
            console.log("Đăng ký tài khoản thành công!");
            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Đăng ký tài khoản thành công!',
                didClose: function () {
                    window.location.href = "/loginPage";
                }
            });
        },
        error: function (error) {
            console.error("Lỗi khi đăng kí tài khoản:", error);
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi đăng ký tài khoản!'
            });
        }
    });
}

function validateInputRegister(name, email, phone, password) {
    // Kiểm tra xem các trường có rỗng không
    if (name === "" || email === "" || phone === "" || password === "") {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng điền đầy đủ thông tin!'
        });
        return false;
    }
    //Check name
    var nameRegex = /^[a-zA-ZÀ-ỹ\s]+$/;
    if (!nameRegex.test(name)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Tên tài khoản không hợp lệ!'
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
    // Kiểm tra định dạng email
    var regexEmail = /^[^\s@]+@gmail\.com$/;
    if (!regexEmail.test(email)) {
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
    // Kiểm tra định dạng password
    var passwordRegex = /^(?=.*[a-zA-Z])|(?=.*\d)[a-zA-Z0-9]+$/;
    if (!passwordRegex.test(password)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Password không hợp lệ!'
        });
        return false;
    }
    return true;
}

function validateDuplicateEmail(email) {
    var existsEmail;
    // Gửi yêu cầu AJAX để kiểm tra trùng email
    $.ajax({
        type: "POST",
        url: "/rest/validateDuplicateEmail",
        contentType: "application/json",
        data: JSON.stringify({
            email: email
        }),
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

function validateDuplicatePhone(phone) {
    var existsPhone;
    // Gửi yêu cầu AJAX để kiểm tra trùng phone
    $.ajax({
        type: "POST",
        url: "/rest/validateDuplicatePhone",
        contentType: "application/json",
        data: JSON.stringify({
            phone: phone
        }),
        async: false,
        success: function (response) {
            existsPhone = response.existsPhone;
        },
        error: function (error) {
            console.error("Lỗi khi kiểm tra trùng Phone:", error);
        }
    });
    return existsPhone;
}