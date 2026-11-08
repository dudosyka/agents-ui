create table project (
    id serial primary key,
    gptUuid uuid,
    title varchar(256),
    description text,
    trackerId varchar(255) default null,
    closed boolean
);

create table task (
    id serial primary key,
    gptUuid uuid,
    projectId int,
    title varchar(256),
    description text,
    trackerId varchar(256) default null,
    status varchar(256) default null,
    closed boolean default false,
    feedback text default null,
    retro_processed boolean default false,
    constraint fk_task_project_id foreign key (projectId)
        references project
        on delete set null
        on update cascade
);

create table feedback_item (
    id serial primary key,
    taskId int,
    gptUuid uuid,
    description text,
    constraint fk_feedback_item_task_id foreign key (taskId)
        references task
        on delete set null
        on update cascade
);