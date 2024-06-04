function showProductModal(productId) {
    $.ajax({
        type: "GET",
        url: "/product/" + productId,
        success: function(data) {
            $("#modalProduct").html(data);
            $("#modalProduct").show();
        },
        error: function(xhr, status, error) {
            console.error(xhr.responseText);
        }
    });
}
