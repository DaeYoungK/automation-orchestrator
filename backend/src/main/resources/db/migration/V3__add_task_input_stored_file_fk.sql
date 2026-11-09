alter table task_input
    add constraint fk_task_input_stored_file
        foreign key (stored_file_id)
        references stored_file (stored_file_id);