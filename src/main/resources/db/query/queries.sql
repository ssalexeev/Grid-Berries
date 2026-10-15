--At least one full CRUD set of queries for your domain entity
insert into products (
    name,
    seller_id,
    title,
    description,
    price,
    stock_quantity,
    image_url,
    type
) values (
    'Sample Product',
    1,
    'Sample Product Title',
    'This is a sample product description.',
    19.99,
    100,
    'https://example.com/sample-product.jpg',
    'Sample Type'
);
select * 
from products 
where id = 1;

select * 
from products 
where title like '%Sample%';

update products 
set price = 24.99 
where id = 1;

delete 
from products 
where id = 1;

--Search query with dynamic filters, pagination and sorting
select *
from products
where (:search_title IS NULL OR title like '%' || :search_description || '%')
  and (:min_price IS NULL OR price >= :min_price)
  and (:max_price IS NULL OR price <= :max_price)
order by price desc limit 10 offset 0;

--Search query with joined data for your use-cases 
select 
    p.title, 
    p.price, 
    c.name as category_name
from products p
join product_categories pc on p.id = pc.product_id
join categories c on pc.category_id = c.id;

--Statistic query; can be not related to your use-cases 
select 
    sp.store_name as seller_name, 
    count(*) as product_count
from products
join seller_profiles sp on products.seller_id = sp.id
group by seller_id, sp.store_name
order by product_count desc;

--Top-something query 
select 
    c.name as category_name, 
    count(*) as product_count
from products p
join product_categories pc on p.id = pc.product_id
join categories c on pc.category_id = c.id
group by c.name
order by product_count desc
limit 5;
