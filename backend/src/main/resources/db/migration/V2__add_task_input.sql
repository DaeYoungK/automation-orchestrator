create table task_input
(
    task_input_id bigint auto_increment
        primary key ,
    task_id bigint not null,
    input_key varchar(255) not null,
    task_input_type enum('FILE') not null,
    stored_file_id bigint not null,

    created_at datetime(6) null,
    updated_at datetime(6) null,
    constraint uk_task_input_task_id_input_key
        unique (task_id, input_key),
    constraint fk_task_input_task
        foreign key (task_id) references task (task_id)
)