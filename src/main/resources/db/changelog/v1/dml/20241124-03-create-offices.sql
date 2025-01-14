--liquibase formatted sql
--changeset Anikeeva:20241124-03-create-offices

INSERT INTO offices VALUES('e00b0425-fa99-48fe-a95a-68bfb83944df', 'г. Ульяновск, ул. Гончарова, д. 2, оф. 12,', 'Тьюринг', false);
INSERT INTO offices VALUES('2de31c5a-3852-4e36-844c-c0eb203ad14f', 'г. Димитровград, ул. Строителей, д. 10, оф. 15', 'Лисков', false);
INSERT INTO offices VALUES('a4a5b5dd-2889-4d0a-be53-b6790903f971', 'г. Самара, ул. Самарская, д. 45, оф. 3', 'Гослинг', false);
INSERT INTO offices VALUES('4b5c93df-5859-4b14-9712-98c158bb189a', 'г. Казань, пр. Победы, д. 55, офис 8', 'Кнут', false);
INSERT INTO offices VALUES('1de24161-1117-4b3b-a382-b3aa30ef0ce7', 'г. Новосибирск, ул. Кирова, д.30, оф. 5', 'Кармак', false)