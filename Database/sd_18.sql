use master
go
if EXISTS ( select name
                from sys.databases
                where name = N'sd_18' )
    drop database sd_18
create database sd_18
go
use sd_18
go

create table [roles] (
	[id] int primary key identity(1, 1),
	[name] nvarchar(max) not null,
	[created_date] datetime,
	[updated_date] datetime
);
go

create table [staffs] (
	[id] int primary key identity(1, 1),
	[name] nvarchar(max),
	[email] nvarchar(max),
	[phone] nvarchar(10),
	[avatar] nvarchar(max),
	[address] nvarchar(max),
	[password] nvarchar(max),
	[status] int default 0,
	[created_date] datetime,
	[updated_date] datetime,
	[role_id] int,
	foreign key (role_id) references [roles](id),
);
go

create table [brands] (
	[id] int primary key identity(1, 1),
	[name] nvarchar(max) not null,
	[status] int default 0,
	[created_date] datetime,
	[updated_date] datetime
);
go

create table [colors] (
	[id] int primary key identity(1, 1),
	[name] nvarchar(max) not null,
	[status] int default 0,
	[created_date] datetime,
	[updated_date] datetime
);
go

create table [categories] (
	[id] int primary key identity(1, 1),
	[name] nvarchar(max) not null,
	[status] int default 0,
	[created_date] datetime,
	[updated_date] datetime
);
go

create table [materials] (
	[id] int primary key identity(1, 1),
	[name] nvarchar(max) not null,
	[status] int default 0,
	[created_date] datetime,
	[updated_date] datetime
);
go

create table [sizes] (
	[id] int primary key identity(1, 1),
	[name] nvarchar(max) not null,
	[status] int default 0,
	[shirt_length] int,
	[shirt_width] int,
	[created_date] datetime,
	[updated_date] datetime
);
go

create table [discounts] (
	[id] int primary key identity(1, 1),
	[name] nvarchar(max) not null,
	[status] int default 0,
	[discount] int,
	[start_date] date,
	[end_date] date,
	[created_date] datetime,
	[updated_date] datetime,
);
go

create table [products] (
	[id] int primary key identity(1, 1),
	[name] nvarchar(max),
	[status] int default 0,
	[description] nvarchar(max),
	[created_date] datetime,
	[updated_date] datetime,
	[category_id] int,
	[brand_id] int,
	[material_id] int,
	[discount_id] int,
	foreign key (category_id) references [categories](id),
	foreign key (brand_id) references [brands](id),
	foreign key (material_id) references [materials](id),
	foreign key (discount_id) references [discounts](id),
);
go

create table [product_details] (
	[id] int primary key identity(1, 1),
	[quantity] int,
	[cost] float,
	[price] float,
	[weight] float,
	[status] int default 0,
	[created_date] datetime,
	[updated_date] datetime,
	[product_id] int,
	[size_id] int,
	[color_id] int,
	foreign key (product_id) references [products](id),
	foreign key (size_id) references [sizes](id),
	foreign key (color_id) references [colors](id),
);
go

create table [images] (
	[id] int primary key identity(1, 1),
	[product_id] int,
	[name] nvarchar(max),
	[url_image] nvarchar(max),
	[status] int,
	[created_date] datetime,
	[updated_date] datetime,
	foreign key (product_id) references [products](id),
);
go

create table [customers] (
	[id] int primary key identity(1, 1),
	[name] nvarchar(max),
	[phone] nvarchar(10),
	[avatar] nvarchar(max),
	[password] nvarchar(max),
	[status] int,
	[email] nvarchar(max),
	[created_date] datetime,
	[updated_date] datetime,
);
go

create table [addresses] (
	[id] int primary key identity(1, 1),
	[customer_id] int,
	[ward] nvarchar(max),
	[district] nvarchar(max),
	[city] nvarchar(max),
	[full_address] nvarchar(max),
	[status] int,
	[phone] nvarchar(10),
	[created_date] datetime,
	[updated_date] datetime,
	foreign key (customer_id) references [customers](id),
);
go


create table [evaluates] (
	[id] int primary key identity(1, 1),
	[star] int,
	[content] nvarchar(max),
	[created_date] datetime,
	[updated_date] datetime,
	[customer_id] int,
	[product_detail_id] int,
	foreign key (customer_id) references [customers](id),
	foreign key (product_detail_id) references [product_details](id),
);
go

create table [carts] (
	[id] int primary key identity(1, 1),
	[status] int,
	[created_date] datetime,
	[updated_date] datetime,
	[customer_id] int,
	foreign key (customer_id) references [customers](id),
);
go

create table [cart_details] (
	[id] int primary key identity(1, 1),
	[quantity] int,
	[price] float,
	[status] int,
	[created_date] datetime,
	[updated_date] datetime,
	[cart_id] int,
	[product_detail_id] int,
	foreign key (cart_id) references [carts](id),
	foreign key (product_detail_id) references [product_details](id),
);
go

create table [transactions] (
	[id] int primary key identity(1, 1),
	[name] nvarchar(max),
	[description] nvarchar(max),
	[status] int,
	[created_date] datetime,
	[updated_date] datetime,
);
go

create table [orders] (
	[id] int primary key IDENTITY(1,1),
	[phone] nvarchar(10) ,
	[username] nvarchar(max) ,
	[total_price] float ,
	[ship_cost] float,
	[weight] float ,
	[note] nvarchar(max) ,
	[shopping] nvarchar(max) ,
	[address] nvarchar(max) ,
	[status] int ,
	[confirm_date] date ,
	[confirm_wait_date] date ,
	[ship_date] date ,
	[ship_wait_date] date,
	[success_date] date,
	[cancel_date] date ,
	[created_date] date ,
	[updated_date] date ,
	[staff_id] int ,
	[customer_id] int,
	[transaction_id] int,
	[type] bit ,
	foreign key (staff_id) references [staffs](id),
	foreign key (customer_id) references [customers](id),
	foreign key (transaction_id) references [transactions](id),
);
go

create table [order_details] (
	[id] int primary key identity(1, 1),
	[quantity] int,
	[price] float,
	[status] int,
	[created_date] datetime,
	[updated_date] datetime,
	[order_id] int,
	[product_detail_id] int,
	foreign key (order_id) references [orders](id),
	foreign key (product_detail_id) references [product_details](id),
);
go

-- Insert data into [roles]
INSERT INTO [roles] ([name], [created_date], [updated_date]) VALUES
('Admin', '2024-03-02', '2024-03-02'),
('User', '2024-03-02', '2024-03-02'),
('Guest', '2024-03-02', '2024-03-02'),
('Manager', '2024-03-02', '2024-03-02'),
('Staff', '2024-03-02', '2024-03-02');

-- Insert data into [admins]
INSERT INTO [staffs] ([name], [email], [phone], [avatar], [address], [password], [status], [created_date], [updated_date], [role_id]) VALUES
('Admin 1', 'admin1@example.com', '1234567890', '/avatars/admin1.jpg', 'Admin Address 1', 'admin123', 1, '2024-03-02', '2024-03-02', 1),
('Admin 2', 'admin2@example.com', '0987654321', '/avatars/admin2.jpg', 'Admin Address 2', 'admin456', 1, '2024-03-02', '2024-03-02', 2),
('Admin 3', 'admin3@example.com', '9876543210', '/avatars/admin3.jpg', 'Admin Address 3', 'admin789', 1, '2024-03-02', '2024-03-02', 3),
('Admin 4', 'admin4@example.com', '0123456789', '/avatars/admin4.jpg', 'Admin Address 4', 'admin012', 1, '2024-03-02', '2024-03-02', 1),
('Admin 5', 'admin5@example.com', '0123456789', '/avatars/admin5.jpg', 'Admin Address 5', 'admin345', 1, '2024-03-02', '2024-03-02', 2);

-- Insert data into [brands]
INSERT INTO [brands] ([name], [status], [created_date], [updated_date]) VALUES
('Brand 1', 1, '2024-03-02', '2024-03-02'),
('Brand 2', 1, '2024-03-02', '2024-03-02'),
('Brand 3', 1, '2024-03-02', '2024-03-02'),
('Brand 4', 1, '2024-03-02', '2024-03-02'),
('Brand 5', 1, '2024-03-02', '2024-03-02');

-- Insert data into [colors]
INSERT INTO [colors] ([name], [status], [created_date], [updated_date]) VALUES
('Red', 1, '2024-03-02', '2024-03-02'),
('Blue', 1, '2024-03-02', '2024-03-02'),
('Green', 1, '2024-03-02', '2024-03-02'),
('Yellow', 1, '2024-03-02', '2024-03-02'),
('Black', 1, '2024-03-02', '2024-03-02');

-- Insert data into [categories]
INSERT INTO [categories] ([name], [status], [created_date], [updated_date]) VALUES
('Category 1', 1, '2024-03-02', '2024-03-02'),
('Category 2', 1, '2024-03-02', '2024-03-02'),
('Category 3', 1, '2024-03-02', '2024-03-02'),
('Category 4', 1, '2024-03-02', '2024-03-02'),
('Category 5', 1, '2024-03-02', '2024-03-02');

-- Insert data into [materials]
INSERT INTO [materials] ([name], [status], [created_date], [updated_date]) VALUES
('Material 1', 1, '2024-03-02', '2024-03-02'),
('Material 2', 1, '2024-03-02', '2024-03-02'),
('Material 3', 1, '2024-03-02', '2024-03-02'),
('Material 4', 1, '2024-03-02', '2024-03-02'),
('Material 5', 1, '2024-03-02', '2024-03-02');

-- Insert data into [sizes]
INSERT INTO [sizes] ([name], [status], [shirt_length], [shirt_width], [created_date], [updated_date]) VALUES
('Size 1', 1, 30, 20, '2024-03-02', '2024-03-02'),
('Size 2', 1, 32, 22, '2024-03-02', '2024-03-02'),
('Size 3', 1, 34, 24, '2024-03-02', '2024-03-02'),
('Size 4', 1, 36, 26, '2024-03-02', '2024-03-02'),
('Size 5', 1, 38, 28, '2024-03-02', '2024-03-02');

-- Insert data into [discounts]
INSERT INTO [discounts] ([name], [status], [discount], [start_date], [end_date], [created_date], [updated_date]) VALUES
('Discount 1', 1, 10, '2024-03-02', '2024-03-10', '2024-03-02', '2024-03-02'),
('Discount 2', 1, 20, '2024-03-02', '2024-03-15', '2024-03-02', '2024-03-02'),
('Discount 3', 1, 15, '2024-03-02', '2024-03-12', '2024-03-02', '2024-03-02'),
('Discount 4', 1, 25, '2024-03-02', '2024-03-20', '2024-03-02', '2024-03-02'),
('Discount 5', 1, 30, '2024-03-02', '2024-03-25', '2024-03-02', '2024-03-02');

-- Insert data into [products]
INSERT INTO [products] ([name], [status], [description], [created_date], [updated_date], [category_id], [brand_id], [material_id], [discount_id]) VALUES
('Product 1', 1, 'Description for Product 1', '2024-03-02', '2024-03-02', 1, 1, 1, 1),
('Product 2', 1, 'Description for Product 2', '2024-03-02', '2024-03-02', 2, 2, 2, 2),
('Product 3', 1, 'Description for Product 3', '2024-03-02', '2024-03-02', 3, 3, 3, 3),
('Product 4', 1, 'Description for Product 4', '2024-03-02', '2024-03-02', 4, 4, 4, 4),
('Product 5', 1, 'Description for Product 5', '2024-03-02', '2024-03-02', 5, 5, 5, 5);

-- Insert data into [product_details]
INSERT INTO [product_details] ([quantity], [cost], [price], [weight], [status], [created_date], [updated_date], [product_id], [size_id], [color_id]) VALUES
(100, 50.00, 100.00, 0.5, 1, '2024-03-02', '2024-03-02', 1, 1, 1),
(150, 60.00, 120.00, 0.6, 1, '2024-03-02', '2024-03-02', 2, 2, 2),
(120, 70.00, 140.00, 0.7, 1, '2024-03-02', '2024-03-02', 3, 3, 3),
(200, 80.00, 160.00, 0.8, 1, '2024-03-02', '2024-03-02', 4, 4, 4),
(180, 90.00, 180.00, 0.9, 1, '2024-03-02', '2024-03-02', 5, 5, 5);

-- Insert data into [images]
INSERT INTO [images] ([product_id], [name], [url_image], [status], [created_date], [updated_date]) VALUES
(1, 'Image 1', '/images/product1.jpg', 1, '2024-03-02', '2024-03-02'),
(2, 'Image 2', '/images/product2.jpg', 1, '2024-03-02', '2024-03-02'),
(3, 'Image 3', '/images/product3.jpg', 1, '2024-03-02', '2024-03-02'),
(4, 'Image 4', '/images/product4.jpg', 1, '2024-03-02', '2024-03-02'),
(5, 'Image 5', '/images/product5.jpg', 1, '2024-03-02', '2024-03-02');

-- Insert data into [customers]
INSERT INTO [customers] ([name], [phone], [avatar], [password], [status], [email], [created_date], [updated_date]) VALUES
('Customer 1', '1234567890', '/avatars/customer1.jpg', 'customer123', 1, 'customer1@example.com', '2024-03-02', '2024-03-02'),
('Customer 2', '0987654321', '/avatars/customer2.jpg', 'customer456', 1, 'customer2@example.com', '2024-03-02', '2024-03-02'),
('Customer 3', '9876543210', '/avatars/customer3.jpg', 'customer789', 1, 'customer3@example.com', '2024-03-02', '2024-03-02'),
('Customer 4', '0123456789', '/avatars/customer4.jpg', 'customer012', 1, 'customer4@example.com', '2024-03-02', '2024-03-02'),
('Customer 5', '0123456789', '/avatars/customer5.jpg', 'customer345', 1, 'customer5@example.com', '2024-03-02', '2024-03-02');

-- Insert data into [addresses]
INSERT INTO [addresses] ([customer_id], [ward], [district], [city], [full_address], [status], [phone], [created_date], [updated_date]) VALUES
(1, 'Ward 1', 'District 1', 'City A', 'Full Address 1', 1, '1234567890', '2024-03-02', '2024-03-02'),
(2, 'Ward 2', 'District 2', 'City B', 'Full Address 2', 1, '0987654321', '2024-03-02', '2024-03-02'),
(3, 'Ward 3', 'District 3', 'City C', 'Full Address 3', 1, '9876543210', '2024-03-02', '2024-03-02'),
(4, 'Ward 4', 'District 4', 'City D', 'Full Address 4', 1, '0123456789', '2024-03-02', '2024-03-02'),
(5, 'Ward 5', 'District 5', 'City E', 'Full Address 5', 1, '0123456789', '2024-03-02', '2024-03-02');

-- Insert data into [evaluates]
INSERT INTO [evaluates] ([star], [content], [created_date], [updated_date], [customer_id], [product_detail_id]) VALUES
(4, 'Good product!', '2024-03-02', '2024-03-02', 1, 1),
(5, 'Excellent service!', '2024-03-02', '2024-03-02', 2, 2),
(3, 'Average quality.', '2024-03-02', '2024-03-02', 3, 3),
(5, 'Highly recommended!', '2024-03-02', '2024-03-02', 4, 4),
(4, 'Satisfied with the purchase.', '2024-03-02', '2024-03-02', 5, 5);

-- Insert data into [carts]
INSERT INTO [carts] ([status], [created_date], [updated_date], [customer_id]) VALUES
(1, '2024-03-02', '2024-03-02', 1),
(1, '2024-03-02', '2024-03-02', 2),
(1, '2024-03-02', '2024-03-02', 3),
(1, '2024-03-02', '2024-03-02', 4),
(1, '2024-03-02', '2024-03-02', 5);

-- Insert data into [cart_details]
INSERT INTO [cart_details] ([quantity], [price], [status], [created_date], [updated_date], [cart_id], [product_detail_id]) VALUES
(2, 200.00, 1, '2024-03-02', '2024-03-02', 1, 1),
(1, 120.00, 1, '2024-03-02', '2024-03-02', 2, 2),
(3, 270.00, 1, '2024-03-02', '2024-03-02', 3, 3),
(4, 320.00, 1, '2024-03-02', '2024-03-02', 4, 4),
(2, 180.00, 1, '2024-03-02', '2024-03-02', 5, 5);

-- Insert data into [transactions]
INSERT INTO [transactions] ([name], [description], [status], [created_date], [updated_date]) VALUES
('Transaction 1', 'Payment for Order 1', 1, '2024-03-02', '2024-03-02'),
('Transaction 2', 'Payment for Order 2', 1, '2024-03-02', '2024-03-02'),
('Transaction 3', 'Payment for Order 3', 1, '2024-03-02', '2024-03-02'),
('Transaction 4', 'Payment for Order 4', 1, '2024-03-02', '2024-03-02'),
('Transaction 5', 'Payment for Order 5', 1, '2024-03-02', '2024-03-02');

-- Insert data into [orders]
INSERT INTO [orders] ([phone], [username], [total_price], [ship_cost], [weight], [note], [shopping], [address], [status], [confirm_date], [ship_date], [success_date], [created_date], [updated_date], [staff_id], [customer_id], [transaction_id],[type]) VALUES
('0123456789', 'User1', 500.00, 20.00, 5.0, 'Order Note 1', 'Shopping Details 1', 'Address 1', 1, '2024-03-02', '2024-03-03', '2024-03-05', '2024-03-02', '2024-03-02', 1, 1, 1,1),
('0987654321', 'User2', 450.00, 15.00, 4.5, 'Order Note 2', 'Shopping Details 2', 'Address 2', 1, '2024-03-02', '2024-03-03', '2024-03-05', '2024-03-02', '2024-03-02', 2, 2, 2,1),
('9876543210', 'User3', 600.00, 25.00, 6.0, 'Order Note 3', 'Shopping Details 3', 'Address 3', 1, '2024-03-02', '2024-03-03', '2024-03-06', '2024-03-02', '2024-03-02', 3, 3, 3,1),
('0123456789', 'User4', 700.00, 30.00, 7.0, 'Order Note 4', 'Shopping Details 4', 'Address 4', 1, '2024-03-02', '2024-03-03', '2024-03-07', '2024-03-02', '2024-03-02', 4, 4, 4,1),
('0123456789', 'User5', 800.00, 35.00, 8.0, 'Order Note 5', 'Shopping Details 5', 'Address 5', 1, '2024-03-02', '2024-03-03', '2024-03-08', '2024-03-02', '2024-03-02', 5, 5, 5,0);

-- Insert data into [order_details]
INSERT INTO [order_details] ([quantity], [price], [status], [created_date], [updated_date], [order_id], [product_detail_id]) VALUES
(2, 200.00, 1, '2024-03-02', '2024-03-02', 1, 1),
(1, 120.00, 1, '2024-03-02', '2024-03-02', 2, 2),
(3, 270.00, 1, '2024-03-02', '2024-03-02', 3, 3),
(4, 320.00, 1, '2024-03-02', '2024-03-02', 4, 4),
(2, 180.00, 1, '2024-03-02', '2024-03-02', 5, 5);

