create table orders
(
    created_at datetime(6) null,
    id bigint auto_increment
        primary key,
    updated_at datetime(6) null,
    address varchar(255) null,
    order_status enum('CANCELED', 'COMPLETED', 'CREATED', 'PAID') null
);

create table product
(
    price int not null,
    product_id bigint auto_increment
        primary key,
    name varchar(255) null,
    product_status enum('ON_SALE', 'PREPARATION', 'SOLD_OUT') null
);

create table task
(
    created_at datetime(6) null,
    task_id bigint auto_increment
        primary key,
    updated_at datetime(6) null,
    description varchar(255) null,
    title varchar(255) null,
    task_status enum('APPROVED', 'CANCELED', 'FAILED', 'PENDING', 'RUNNING', 'SUCCESS') null,
    task_type enum('LOW_STOCK_REPORT', 'PAYMENT_FAILURE_REPORT', 'PURCHASE_REQUEST_REVIEW', 'RPA_FAILURE_RECOVERY', 'VOC_ANALYSIS_REPORT') null
);

create table stored_file
(
    created_at datetime(6) not null,
    size_bytes bigint not null,
    stored_file_id bigint auto_increment
        primary key,
    content_type varchar(255) null,
    original_filename varchar(255) not null,
    storage_key varchar(255) not null,
    status enum('ATTACHED', 'TEMPORARY') not null,
    constraint uk_stored_file_storage_key
        unique (storage_key)
);

create table notification
(
    created_at datetime(6) null,
    notification_id bigint auto_increment
        primary key,
    task_id bigint not null,
    updated_at datetime(6) null,
    title varchar(100) not null,
    description varchar(255) not null
);



create table payment
(
    amount int not null,
    created_at datetime(6) null,
    id bigint auto_increment
        primary key,
    order_id bigint null,
    paid_at datetime(6) null,
    updated_at datetime(6) null,
    failed_reason varchar(255) null,
    payment_status enum('CANCELED', 'FAILED', 'PENDING', 'SUCCESS') null,
    constraint uk_payment_order_id
        unique (order_id),
    constraint fk_payment_order
        foreign key (order_id) references orders (id)
);



create table inventory
(
    available_quantity bigint not null,
    inventory_id bigint auto_increment
        primary key,
    product_id bigint null,
    reserved_quantity bigint not null,
    constraint uk_inventory_product_id
        unique (product_id),
    constraint fk_inventory_product
        foreign key (product_id) references product (product_id)
);

create table order_item
(
    order_price int not null,
    quantity int not null,
    id bigint auto_increment
        primary key,
    order_id bigint null,
    product_id bigint null,
    order_product_name varchar(255) null,
    constraint fk_order_item_product
        foreign key (product_id) references product (product_id),
    constraint fk_order_item_order
        foreign key (order_id) references orders (id)
);

create table execution
(
    attempt_count int not null,
    created_at datetime(6) null,
    execution_id bigint auto_increment
        primary key,
    task_id bigint null,
    updated_at datetime(6) null,
    error_message varchar(255) null,
    execution_status enum('CANCELED', 'FAILED', 'READY', 'RETRYING', 'RUNNING', 'SUCCESS') null,
    execution_type enum('AI', 'API', 'EMAIL', 'RPA', 'SLACK') null,
    constraint fk_execution_task
        foreign key (task_id) references task (task_id)
);

create table execution_log
(
    created_at datetime(6) null,
    execution_id bigint null,
    execution_log_id bigint auto_increment
        primary key,
    updated_at datetime(6) null,
    message varchar(255) null,
    log_level enum('ERROR', 'INFO', 'WARN') null,
    constraint fk_execution_log_execution
        foreign key (execution_id) references execution (execution_id)
);

