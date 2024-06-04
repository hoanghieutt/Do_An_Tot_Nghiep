// Lấy các tham số từ URL query string
const urlParams = new URLSearchParams(window.location.search);

// Hiển thị các giá trị từ URL query string
document.getElementById("orderInfo").innerText = urlParams.get("vnp_TxnRef");
document.getElementById("bankCode").innerText = urlParams.get("vnp_BankCode");
document.getElementById("cardType").innerText = urlParams.get("vnp_CardType");

// Chuyển đổi giá trị ngày tháng từ URL vào định dạng "HH:mm:ss dd/MM/yyyy"
const payDateValue = urlParams.get("vnp_PayDate");
const formattedPayDate = formatPayDate(payDateValue);
document.getElementById("payDate").innerText = formattedPayDate;

// Chuyển đổi số tiền về định dạng tiền tệ và hiển thị
let amount = parseFloat(urlParams.get("vnp_Amount"));
let formattedAmount = formatCurrency(amount / 100);
document.getElementById("amount").innerText = formattedAmount;

// Hàm chuyển đổi số tiền thành định dạng tiền tệ
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

// Hàm chuyển đổi ngày tháng từ URL vào định dạng "HH:mm:ss dd/MM/yyyy"
function formatPayDate(dateString) {
    const year = dateString.substring(0, 4);
    const month = dateString.substring(4, 6);
    const day = dateString.substring(6, 8);
    const hour = dateString.substring(8, 10);
    const minute = dateString.substring(10, 12);
    const second = dateString.substring(12, 14);
    return `${hour}:${minute}:${second} ${day}/${month}/${year}`;
}
