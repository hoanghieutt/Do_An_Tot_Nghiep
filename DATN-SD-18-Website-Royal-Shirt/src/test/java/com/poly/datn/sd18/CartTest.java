package com.poly.datn.sd18;

import com.poly.datn.sd18.entity.*;
import com.poly.datn.sd18.repository.CartDetailRepository;
import com.poly.datn.sd18.repository.CartRepository;
import com.poly.datn.sd18.repository.ProductDetailRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CartTest {
    @Autowired
    private CartDetailRepository cartDetailRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testAddOneCartItem() {
        Product product = entityManager.find(Product.class, 2);
        Size size = entityManager.find(Size.class, 2);
        Color color = entityManager.find(Color.class, 1);

        ProductDetail productDetail = productDetailRepository
                .findByProductIdAndColorIdAndSizeId(
                        product.getId(),
                        size.getId(),
                        color.getId());

        if (productDetail != null) {
            Cart cart = null;
            Customer customer = entityManager.find(Customer.class, 1);

            if (customer != null) {
                cart = cartRepository.findByCustomerId(customer.getId());
                if (cart == null) {
                    cart = new Cart();
                    cart.setCustomer(customer);
                    cart.setStatus(0);
                    cart = cartRepository.save(cart);
                }
            } else {
                cart = new Cart();
                cart.setStatus(0);
                cart = cartRepository.save(cart);
            }

            CartDetail cartDetail = cartDetailRepository.findByCartAndProductDetail(cart, productDetail);
            if (cartDetail == null) {
                cartDetail = new CartDetail();
                cartDetail.setProductDetail(productDetail);
                cartDetail.setCart(cart);
                cartDetail.setQuantity(1);
                if (productDetail != null) {
                    cartDetail.setPrice(productDetail.getPrice());
                }
                cartDetail.setStatus(0);
            } else {
                cartDetail.setQuantity(cartDetail.getQuantity() + 1);
                if (productDetail != null) {
                    cartDetail.setPrice(productDetail.getPrice() * cartDetail.getQuantity());
                }
            }
            cartDetailRepository.save(cartDetail);
            assertTrue(cartDetail.getId() > 0);
        }
    }

}
