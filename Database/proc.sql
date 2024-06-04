CREATE PROCEDURE hotSelling
    @minQuantity INT
AS
BEGIN
    SELECT DISTINCT p.id, p.name, p.status, c.name , pd.price
    FROM products p
    JOIN product_details pd ON p.id = pd.product_id
	JOIN categories c ON p.category_id = c.id
    JOIN (
        SELECT product_detail_id
        FROM order_details
        GROUP BY product_detail_id
        HAVING SUM(quantity) > @minQuantity
    ) AS od ON pd.id = od.product_detail_id;
END

EXEC hotSelling @minQuantity = 2;


CREATE  PROCEDURE thongkedonhang
    @Nam INT
AS
BEGIN
    SET NOCOUNT ON;

    WITH AllMonths AS (
        SELECT 1 AS MonthNumber
        UNION ALL
        SELECT MonthNumber + 1
        FROM AllMonths
        WHERE MonthNumber < 12
    )

    SELECT 
        am.MonthNumber AS Thang, 
        COUNT(o.id) AS SoDonHang
    FROM 
        AllMonths am
    LEFT JOIN 
        [orders] o ON MONTH(o.success_date) = am.MonthNumber AND YEAR(o.success_date) = @Nam
    GROUP BY 
        am.MonthNumber
    ORDER BY 
        am.MonthNumber;

END;

EXEC thongkedonhang 2024

CREATE PROCEDURE thongKeSoSanPham
    @Nam INT
AS
BEGIN
    SET NOCOUNT ON;

    -- Tạo bảng tạm thời chứa tất cả các tháng trong năm
    CREATE TABLE #AllMonths (MonthNumber INT);

    DECLARE @MonthNumber INT = 1;
    WHILE @MonthNumber <= 12
    BEGIN
        INSERT INTO #AllMonths (MonthNumber) VALUES (@MonthNumber);
        SET @MonthNumber = @MonthNumber + 1;
    END;

    -- Thực hiện truy vấn để lấy số sản phẩm được bán cho mỗi tháng
    SELECT 
        am.MonthNumber AS Thang,
        ISNULL(SUM(od.quantity), 0) AS SoSanPhamDaBan
    FROM 
        #AllMonths am
    LEFT JOIN 
        [order_details] od ON MONTH(od.created_date) = am.MonthNumber AND YEAR(od.created_date) = @Nam
    GROUP BY 
        am.MonthNumber
    ORDER BY 
        am.MonthNumber;

    -- Xóa bảng tạm thời
    DROP TABLE #AllMonths;
END;

EXEC thongKeSoSanPham 2024