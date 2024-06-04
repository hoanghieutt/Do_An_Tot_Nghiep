package com.poly.datn.sd18.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.poly.datn.sd18.entity.Address;
import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.repository.AddressRepository;
import com.poly.datn.sd18.repository.CustomerRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.springframework.util.StringUtils;

@Controller
@RequestMapping("/admin/customers")
@CrossOrigin("*")
public class CustomersController {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AddressRepository addressRepository;

    @GetMapping("")
    public String customers(Model model) {
        Customer customer = new Customer(); // Tạo một đối tượng Customer mới
        model.addAttribute("customer", customer); // Thêm đối tượng Customer vào model
        Customer newCustomer = new Customer(); // Tạo một đối tượng Customer mới
        model.addAttribute("newCustomer", newCustomer);
        Address newCustomerAdrr = new Address(); // Tạo một đối tượng Customer mới
        model.addAttribute("newCustomerAdrr", newCustomerAdrr);
        // lấy danh sáhc người dùng
        List<Customer> listCus = customerRepository.findAll();
        // Đảo ngược danh sách
        Collections.reverse(listCus);
        // Khởi tạo hai danh sách riêng biệt
        List<Customer> listCusStatus1 = new ArrayList<>();
        List<Customer> listCusStatus2 = new ArrayList<>();

        // Duyệt qua danh sách người dùng và phân loại vào hai danh sách tương ứng
        for (Customer cus : listCus) {
            if (cus.getStatus() == 0) {
                listCusStatus1.add(cus);
            } else if (cus.getStatus() == 1) {
                listCusStatus2.add(cus);
            }
        }

        model.addAttribute("listCus", listCus);
        model.addAttribute("listCusStatus1", listCusStatus1);
        model.addAttribute("listCusStatus2", listCusStatus2);
        for (Customer cus : listCus) {
            List<Address> addresses = cus.getListAddresses();
            if (addresses != null) {
                for (Address addr : addresses) {
                    System.out.println("Dia chi: " + addr.getFullAddress());
                }
            }
        }
        return "admin/customers/index";
    }

    @PostMapping("/add")
    public String addCustomers(@ModelAttribute("customer") Customer customer,
                               @RequestParam("avatarFile") MultipartFile avatarFile) {
        if (!avatarFile.isEmpty()) {
            // Lấy tên tệp gốc
            String fileName = StringUtils.cleanPath(avatarFile.getOriginalFilename());

            // Đường dẫn đầy đủ đến thư mục static
            String uploadDir = "src/main/resources/static/img/";

            // Tạo đường dẫn lưu trữ tệp hình ảnh
            String filePath = uploadDir + fileName;

            // Lưu tệp hình ảnh vào thư mục static
            try (OutputStream outputStream = new FileOutputStream(filePath)) {
                outputStream.write(avatarFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Thiết lập tên của tệp hình ảnh vào đối tượng Customer
            customer.setCreateDate(new Date());
            customer.setAvatar(fileName);
        }

        // Lưu đối tượng Customer vào cơ sở dữ liệu
        customerRepository.saveAndFlush(customer);

        // Chuyển hướng về trang danh sách khách hàng hoặc trang chính
        return "redirect:/admin/customers";
    }

    @PostMapping("/update")
    public String updateCustomers(@ModelAttribute("newCustomer") Customer customer,
                                  @RequestParam("newAvatarFile") MultipartFile avatarFile) {
        Customer obj = customerRepository.findById(customer.getId()).orElse(customer);
        customer.setCreateDate(obj.getCreateDate());
        customer.setUpdateDate(new Date());
        if (!avatarFile.isEmpty()) {
            // Lấy tên tệp gốc
            String fileName = StringUtils.cleanPath(avatarFile.getOriginalFilename());

            // Đường dẫn đầy đủ đến thư mục static
            String uploadDir = "src/main/resources/static/img/";

            // Tạo đường dẫn lưu trữ tệp hình ảnh
            String filePath = uploadDir + fileName;

            // Lưu tệp hình ảnh vào thư mục static
            try (OutputStream outputStream = new FileOutputStream(filePath)) {
                outputStream.write(avatarFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Thiết lập tên của tệp hình ảnh vào đối tượng Customer
            customer.setAvatar(fileName);
        } else {
            customer.setAvatar(obj.getAvatar());
        }

        customerRepository.saveAndFlush(customer);

        // Chuyển hướng về trang danh sách khách hàng hoặc trang chính
        return "redirect:/admin/customers";
    }

    @GetMapping("/remove/{id}")
    public String removeCustomer(@PathVariable Integer id) {
        // Tìm khách hàng cần xóa trong repository
        Customer obj = customerRepository.findById(id).orElse(null);
        // Nếu khách hàng tồn tại, đổi tt khách hàng và lưu thay đổi vào repository
//        if (obj != null) {
//            obj.setStatus(1);
//            customerRepository.saveAndFlush(obj);
//        }
        if (obj != null) {
            if (obj.getStatus() == 0) {
                obj.setStatus(1);
                customerRepository.saveAndFlush(obj);
            }else {
                obj.setStatus(0);
                customerRepository.saveAndFlush(obj);
            }
        }


        // Chuyển hướng về trang danh sách khách hàng hoặc trang chính
        return "redirect:/admin/customers";
    }

    @GetMapping("/address/{id}")
    public String address(@PathVariable int id, Model model) {
        // Tạo một đối tượng Customer mới
        Customer customer = customerRepository.findById(id).orElse(null);

        // Lấy danh sách địa chỉ từ đối tượng customer
        List<Address> addresses = customer.getListAddresses();

        // Tạo danh sách mới để lưu trữ các phần tử được sắp xếp
        List<Address> sortedAddresses = new ArrayList<>();

        // Duyệt qua danh sách địa chỉ và thêm các phần tử có status = 0 vào đầu danh
        // sách mới
        for (Address address : addresses) {
            if (address.getStatus() == 0) {
                sortedAddresses.add(address);
            }
        }

        // Tiếp tục duyệt qua danh sách địa chỉ và thêm các phần tử còn lại vào cuối
        // danh sách mới
        for (Address address : addresses) {
            if (address.getStatus() != 0) {
                sortedAddresses.add(address);
            }
        }

        // Gán danh sách mới cho thuộc tính getListAddresses của đối tượng customer
        customer.setListAddresses(sortedAddresses);

        model.addAttribute("customer", customer);
        // Tạo một đối tượng địa chỉ mới
        Address newCustomerAdrr = new Address();
        model.addAttribute("newCustomerAdrr", newCustomerAdrr);

        Address editCustomerAdrr = new Address();
        model.addAttribute("editCustomerAdrr", newCustomerAdrr);
        return "admin/customers/address";
    }

    @PostMapping("/addAddress")
    public String addAddress(@ModelAttribute("newCustomerAdrr") Address address) {
        // Tìm khách hàng cần xóa trong repository
        System.out.println("id ne: " + address.getCustomer().getId());
        Customer obj = customerRepository.findById(address.getCustomer().getId()).orElse(null);
        // Nếu khách hàng tồn tại, xóa khách hàng và lưu thay đổi vào repository
        address.setCustomer(obj);
        address.setStatus(1);
        addressRepository.saveAndFlush(address);
        // Chuyển hướng về trang danh sách khách hàng hoặc trang chính
        return "redirect:/admin/customers/address/" + address.getCustomer().getId();
    }

    @PostMapping("/editAddress")
    public String editAddress(@ModelAttribute("editCustomerAdrr") Address address) {
        // Tìm khách hàng cần edit trong repository
        Customer obj = customerRepository.findById(address.getCustomer().getId()).orElse(null);
        Address newObj = addressRepository.findById(address.getId()).orElse(address);
        // cập nhật thay đổi vào repository
        address.setCustomer(obj);
        address.setStatus(newObj.getStatus());
        address.setCreatedAt(newObj.getCreatedAt());
        address.setUpdatedAt(newObj.getCreatedAt());
        addressRepository.saveAndFlush(address);
        // Chuyển hướng về trang danh sách khách hàng hoặc trang chính
        return "redirect:/admin/customers/address/" + address.getCustomer().getId();
    }

    @GetMapping("/removeAddress/{customerId}/{addressId}")
    public String removeAddress(@PathVariable int customerId, @PathVariable int addressId) {
        System.out.println("id ne:" + addressId);
        addressRepository.deleteById(addressId);
        return "redirect:/admin/customers/address/" + customerId;
    }

    @GetMapping("/changeAddress/{customerId}/{id}")
    public String changeAddress(@PathVariable int customerId, @PathVariable int id) {
        Customer obj = customerRepository.findById(customerId).orElse(null);
        if (obj != null) {
            List<Address> addresses = obj.getListAddresses();
            if (addresses != null && !addresses.isEmpty()) {
                for (Address address : addresses) {
                    if (address.getId() == id) {
                        // Tìm thấy địa chỉ cần thay đổi, cập nhật trạng thái và lưu vào cơ sở dữ liệu
                        address.setStatus(0);
                        addressRepository.saveAndFlush(address);
                    } else {
                        address.setStatus(1);
                        addressRepository.saveAndFlush(address);
                    }
                }
            }
            return "redirect:/admin/customers/address/" + customerId;
        }
        return "redirect:/admin/customers"; // Xử lý trường hợp obj == null
    }

}