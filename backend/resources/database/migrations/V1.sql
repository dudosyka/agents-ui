create table project (
    id serial primary key,
    gptUuid uuid,
    title varchar(256),
    description text,
    trackerId varchar(255) default null,
    closed boolean
);
