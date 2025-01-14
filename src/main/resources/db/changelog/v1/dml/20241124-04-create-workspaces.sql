--liquibase formatted sql
--changeset Anikeeva:20241124-04-create-workspaces

INSERT INTO workspaces VALUES('2d3d53dd-7959-4919-a875-4d67bab9e4a4', 'Пиксель', 1, 1, false, 'e00b0425-fa99-48fe-a95a-68bfb83944df');
INSERT INTO workspaces VALUES('a7ce3fc8-880f-415b-8075-1dd6d1fd97ae', 'Тильда', 2, 2, false, 'e00b0425-fa99-48fe-a95a-68bfb83944df');
INSERT INTO workspaces VALUES('83718be9-fb86-49de-ae10-b45864f164ea', 'Эпсилон', 1, 1, false, '2de31c5a-3852-4e36-844c-c0eb203ad14f');
INSERT INTO workspaces VALUES('b897383b-4155-4b24-8df5-4faab1894055', 'Лямбда', 2, 1, false, 'a4a5b5dd-2889-4d0a-be53-b6790903f971');
INSERT INTO workspaces VALUES('0a82c272-678b-455f-902a-b48328121f12', 'Сигма', 2, 2, false, 'a4a5b5dd-2889-4d0a-be53-b6790903f971');
INSERT INTO workspaces VALUES('6890234d-8c6f-494c-ab25-340712b85b9e', 'Омега', 1, 1, false, '4b5c93df-5859-4b14-9712-98c158bb189a');
INSERT INTO workspaces VALUES('607e4c67-e0d8-4ba5-81ea-161ef4a39872', 'Бит', 2, 2, false, '4b5c93df-5859-4b14-9712-98c158bb189a');
INSERT INTO workspaces VALUES('d926c108-29ab-4b18-8c85-be510f030575', 'Фрактал', 3, 1, false, '1de24161-1117-4b3b-a382-b3aa30ef0ce7');
INSERT INTO workspaces VALUES('56ad008d-9877-4d3b-8bf7-995516cf5dc3', 'Квант', 3, 1, false, '1de24161-1117-4b3b-a382-b3aa30ef0ce7')