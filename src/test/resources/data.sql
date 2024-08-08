delete from authority where type = 'ROLE_USER';

insert
into
    authority
(type, id)
values
    ('ROLE_USER', default);