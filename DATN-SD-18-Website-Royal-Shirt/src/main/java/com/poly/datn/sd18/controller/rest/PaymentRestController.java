package com.poly.datn.sd18.controller.rest;

import com.poly.datn.sd18.config.VnpConfig;
import com.poly.datn.sd18.entity.Order;
import com.poly.datn.sd18.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentRestController {
    private final OrderService orderService;

    @GetMapping("/create-payment/{id}")
    public RedirectView createPayment(@PathVariable("id") Integer id) {
        Order order = orderService.findOrderById(id);
        Float totalPrice = orderService.totalPriceByIdOrder(id);
        long amount = (long) (totalPrice * 100);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VnpConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VnpConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", VnpConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", String.valueOf(order.getId()));
        vnp_Params.put("vnp_OrderInfo", "Thanh toán đơn hàng: " + order.getId());
        vnp_Params.put("vnp_OrderType", VnpConfig.orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VnpConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", "192.168.0.149");

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                try {
                    // Build hash data
                    hashData.append(fieldName)
                            .append('=')
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    // Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                            .append('=')
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (fieldNames.indexOf(fieldName) < fieldNames.size() - 1) {
                        query.append('&');
                        hashData.append('&');
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnpConfig.hmacSHA512(VnpConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnpConfig.vnp_PayUrl + "?" + queryUrl;

        return new RedirectView(paymentUrl);
    }

    @GetMapping("/payment-info")
    public String transaction(Model model,
                              @RequestParam(name = "vnp_TxnRef") String vnp_TxnRef,
                              @RequestParam(name = "vnp_Amount") Integer vnp_Amount,
                              @RequestParam(name = "vnp_BankCode", defaultValue = "") String vnp_BankCode,
                              @RequestParam(name = "vnp_CardType") String vnp_CardType,
                              @RequestParam(name = "vnp_PayDate") String vnp_PayDate,
                              @RequestParam(name = "vnp_ResponseCode") String vnp_ResponseCode) {
        if (vnp_ResponseCode.equals("00")) {
            model.addAttribute("vnp_TxnRef", vnp_TxnRef);
            model.addAttribute("vnp_Amount", vnp_Amount);
            model.addAttribute("vnp_BankCode", vnp_BankCode);
            model.addAttribute("vnp_CardType", vnp_CardType);
            model.addAttribute("vnp_PayDate", vnp_PayDate);
            return "client/payment/success";
        }else {
            Integer orderId = Integer.parseInt(vnp_TxnRef);
            orderService.updateOrderStatus(orderId, 6);
            return "client/payment/error";
        }
    }
}