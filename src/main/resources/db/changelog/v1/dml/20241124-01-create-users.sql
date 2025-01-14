--liquibase formatted sql
--changeset Anikeeva:20241124-01-create-users

INSERT INTO users VALUES('f2d52464-2c18-4dc5-9341-7679b80bcca8', 'Кузнецова Анна Дмитриевна', '89012345678', 'kuznecov.anna@mail.ru', 'password', 2400, false, true);
INSERT INTO users VALUES('6f0239c2-3ab8-48e8-a67f-37d753eb5105', 'Лебедев Андрей Павлович', '89123456789', 'lebeded.andrey@gmail.com', 'NewPassword', 2400, false, false);
INSERT INTO users VALUES('aacbe9b8-38cc-49e5-b743-94c9f9a021cd', 'Васильева Мария Сергеевна', '89234567890', 'vasilieva.maria@yandex.ru', 'NewPassword', 2400, false, false);
INSERT INTO users VALUES('445e5201-8886-46d7-a470-a0d36e9a8159', 'Орлов Владимир Юрьевич', '89345678901', 'orlov.vladimir@mail.ru', 'NewPassword', 2400, false, false);
INSERT INTO users VALUES('6d01d01e-0bbf-49d8-99f2-bbd1b24b24d2', 'Федорова Ольга Николаевна', '89456789012', 'fedorova.olga@gmail.com', 'NewPassword', 2400, true, false);
INSERT INTO users VALUES('66d9680b-348b-4d64-8ee8-8adcd42680a4', 'Григорьев Даниил Викторович', '89567890123', 'grigorev.daniil@yandex.ru', 'NewPassword', 2400, false, false);
INSERT INTO users VALUES('3150ef8a-a652-469f-92c4-eba411f853ca', 'Захаров Игорь Евгеньевич', '89678901234', 'zakharaov.igor@mail.ru', 'NewPassword', 2400, false, false);
INSERT INTO users VALUES('c515006c-08b1-4d49-87a4-1910624f8ecf', 'Алексеева Юлия Валерьевна', '89789012345', 'alekseeva.julia@gmail.com', 'password', 2400, false, true);
INSERT INTO users VALUES('e17d2900-9beb-4060-8358-4d380a94360f', 'Миронов Константин Артемович', '89890123456', 'mironov.konstantin@yandex.ru', 'NewPassword', 2400, true, false);
INSERT INTO users VALUES('ff51ed5a-90a3-46bf-88ab-2b8f26fcbc75', 'Панкратова Светлана Геннадьевна', '89901234567', 'pankratova.svetlana@mail.ru', 'NewPassword', 2400, false, false);
INSERT INTO users VALUES('d4727670-0a88-41b0-ba67-2a443c64295e', 'Соловьева Надежда Артемьевна', '89011234567', 'solovyeva.nadezhda@gmail.com', 'NewPassword', 2400, false, false);
INSERT INTO users VALUES('661d2c74-f253-4288-8871-3ab1fcd90ab7', 'Жуковский Илья Данилович', '89122345678', 'zhukovskiy.ilya@yandex.ru', 'password', 2400, false, true);
INSERT INTO users VALUES('c22ba4f8-596a-4342-9d47-b7809794e509', 'Богданова Татьяна Михайловна', '89233456789', 'bogdanova.tatiana@mail.ru', 'NewPassword', 2400, false, false);
INSERT INTO users VALUES('a843e8f1-9b56-4e73-b2a7-f654754adbd1', 'Кириллов Станислав Александрович', '89344567890', 'kirillov.stanislav@gmail.com', 'password', 2400, false, true);
INSERT INTO users VALUES('bba06782-9ef5-4f42-a439-b378cab8f95d', 'Перов Игорь Дмитриевич', '89455678901', 'perov.igor@yandex.ru', 'NewPassword', 2400, true, false)
