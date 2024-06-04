package com.poly.datn.sd18.repository;

import com.poly.datn.sd18.entity.Cart;
import com.poly.datn.sd18.entity.CartDetail;
import com.poly.datn.sd18.entity.ProductDetail;
import com.poly.datn.sd18.model.response.CartDetailRestponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Integer> {
    CartDetail findByCartAndProductDetail(Cart cart, ProductDetail productDetail);

    @Query(value = """
                SELECT
                    cd.*
                FROM
                    cart_details cd
                INNER JOIN
                    carts c ON cd.cart_id = c.id
                INNER JOIN
                    customers cst ON c.customer_id = cst.id
                WHERE
                    cst.id = :customerId
            """, nativeQuery = true)
    List<CartDetail> findCartDetailByCustomer(@Param("customerId") Integer customerId);

    @Modifying
    @Query("delete from CartDetail cd where cd.productDetail.id = :productDetailId and cd.cart.customer.id = :customerId")
    void deleteIdProductDetailAndIdCustomer(@Param("productDetailId") Integer productDetailId,
                                            @Param("customerId") Integer customerId);

    @Modifying
    @Query("delete from CartDetail cd where cd.id = :cartDetailId and cd.cart.customer.id = :customerId")
    void deleteCartDetailByIdCartDetailAndIdCustomer(@Param("cartDetailId") Integer cartDetailId,
                                                     @Param("customerId") Integer customerId);

    boolean existsById(@NonNull Integer id);

    @Query("SELECT SUM(cd.price) FROM CartDetail cd WHERE cd.id = :cartDetailId")
    Float getSumPriceByCartDetailId(@Param("cartDetailId") Integer cartDetailId);

    @Modifying
    @Query(value = "UPDATE [dbo].[cart_details]\n" +
            "   SET [quantity] = [quantity] + 1\n" +
            "FROM [dbo].[cart_details]\n" +
            "   JOIN [dbo].[carts] on [dbo].[cart_details].[cart_id] = [dbo].[carts].[id]\n" +
            " WHERE [dbo].[carts].[customer_id] = :customerId AND \n" +
            "\t   [dbo].[cart_details].[product_detail_id] = :productDetailId", nativeQuery = true)
    void incrementQuantity(@Param("customerId") Integer customerId, @Param("productDetailId") Integer productDetailId);

    @Modifying
    @Query(value = "UPDATE [dbo].[cart_details]\n" +
            "   SET [quantity] = [quantity] - 1\n" +
            "FROM [dbo].[cart_details]\n" +
            "   JOIN [dbo].[carts] on [dbo].[cart_details].[cart_id] = [dbo].[carts].[id]\n" +
            " WHERE [quantity] > 1 AND \n" +
            "\t   [dbo].[carts].[customer_id] = :customerId AND \n" +
            "\t   [dbo].[cart_details].[product_detail_id] = :productDetailId", nativeQuery = true)
    void decrementQuantity(@Param("customerId") Integer customerId, @Param("productDetailId") Integer productDetailId);

    @Query(value = "SELECT dbo.cart_details.id,\n" +
            "       dbo.product_details.id AS productDetailId,\n" +
            "       dbo.products.name AS productName,\n" +
            "       dbo.colors.name AS colorName,\n" +
            "       dbo.sizes.name AS sizeName,\n" +
            "       dbo.cart_details.quantity AS quantity,\n" +
            "       dbo.product_details.price AS price,\n" +
            "    CASE \n" +
            "        WHEN dbo.discounts.status = 1 THEN 0\n" +
            "        WHEN dbo.discounts.start_date > CAST(GETDATE() AS DATE) OR dbo.discounts.end_date < CAST(GETDATE() AS DATE) THEN 0" +
            "        ELSE COALESCE(dbo.discounts.discount, 0)\n" +
            "    END AS discount\n" +
            "FROM   dbo.cart_details\n" +
            "INNER JOIN\n" +
            "       dbo.product_details ON dbo.cart_details.product_detail_id = dbo.product_details.id\n" +
            "INNER JOIN\n" +
            "       dbo.colors ON dbo.product_details.color_id = dbo.colors.id\n" +
            "INNER JOIN\n" +
            "       dbo.sizes ON dbo.product_details.size_id = dbo.sizes.id\n" +
            "INNER JOIN\n" +
            "       dbo.products ON dbo.product_details.product_id = dbo.products.id\n" +
            "LEFT JOIN " +
            "       dbo.discounts ON dbo.products.discount_id = dbo.discounts.id\n" +
            "WHERE dbo.cart_details.id = :cartDetailId",nativeQuery = true)
    CartDetailRestponse findCartDetaiToCheckoutlById(@Param("cartDetailId") Integer cartDetailId);

    @Query(value = """
                SELECT    dbo.cart_details.quantity
                FROM         dbo.cart_details INNER JOIN
                                      dbo.carts ON dbo.cart_details.cart_id = dbo.carts.id INNER JOIN
                                      dbo.customers ON dbo.carts.customer_id = dbo.customers.id
                				where customers.id = :customerId
                				and cart_details.product_detail_id = :productDetailId
            """, nativeQuery = true)
    Integer quantityCartDetail(@Param("customerId") Integer customerId,
                               @Param("productDetailId") Integer productDetailId);

    @Modifying
    @Query(value = "DELETE [dbo].[cart_details]\n" +
            "FROM [dbo].[cart_details]\n" +
            "INNER JOIN [dbo].[carts] ON [dbo].[cart_details].cart_id = [dbo].[carts].id \n" +
            "WHERE [dbo].[carts].customer_id = :customerId AND \n" +
            "\t  [dbo].[cart_details].product_detail_id = :productDetailId",nativeQuery = true)
    void deleteAfterCheckout(@Param("customerId") Integer customerId,@Param("productDetailId") Integer productDetailId);

    @Modifying
    @Transactional
    @Query(value = """
                DELETE FROM cart_details
                WHERE
                    cart_id = :cartId
                    AND (
                        EXISTS (
                            SELECT 1
                            FROM product_details pd
                            INNER JOIN products p ON pd.product_id = p.id
                            WHERE
                                p.status = 1
                                AND pd.id = cart_details.product_detail_id
                        )
                        OR EXISTS (
                            SELECT 1
                            FROM product_details pd
                            WHERE
                                pd.status = 1
                                AND pd.id = cart_details.product_detail_id
                        )
                        OR EXISTS (
                            SELECT 1
                            FROM product_details pd
                            WHERE
                                pd.quantity = 0
                                AND pd.id = cart_details.product_detail_id
                        )
                    )
            """, nativeQuery = true)
    void deleteCartDetailByQuantityAndStatusProduct(@Param("cartId") Integer cartId);
}
