--oracle

INSERT INTO CargoType(cargoType) VALUES ('FOOD');
INSERT INTO CargoType(cargoType) VALUES ('CLOTHER');

INSERT INTO CarrierType(carrierType) VALUES ('SHIP');
INSERT INTO CarrierType(carrierType) VALUES ('PLANE');


INSERT INTO Carrier(id, name, adress, carrierType) VALUES(1,'YOUR_DELIVERY', 'Spb, Nevskiy 800', 'SHIP');
INSERT INTO Carrier(id, name, adress, carrierType) VALUES(2,'FASTEST EVER', 'Sp, Prosveheniya 900', 'PLANE');

INSERT INTO Cargo(id, name, weight,cargoType) VALUES(1,'APPLE', 100, 'FOOD');
INSERT INTO FoodCargo(id, expirationDate, storeTemperature) VALUES(1,TO_DATE('20.12.2025', 'dd.mm.yyyy' ), -50);

INSERT INTO Cargo(id, name, weight,cargoType) VALUES(2,'JEANS', 200, 'CLOTHER');
INSERT INTO ClotherCargo(id,sizeCargo,material) VALUES(2,'XXL', 'Cotton');

INSERT INTO Cargo(id, name, weight,cargoType) VALUES(3,'ORANGE', 300, 'FOOD');
INSERT INTO FoodCargo(id, expirationDate, storeTemperature) VALUES(3,TO_DATE('11.11.2020', 'dd.mm.yyyy' ), -20);

INSERT INTO Transportation(id, cargoId,carrierId,description,billTo, transportationBeginDate)
VALUES(1,1,1,'Some descript', 'Vasya', TO_DATE('01.03.2010', 'dd.mm.yyyy' ));
INSERT INTO Transportation(id, cargoId,carrierId,description,billTo, transportationBeginDate)
VALUES(2,2,1,'Some descript_2', 'Petya', TO_DATE('08.05.2017', 'dd.mm.yyyy' ));
INSERT INTO Transportation(id, cargoId,carrierId,description,billTo, transportationBeginDate)
VALUES(3,3,1,'Some descript_3', 'Ivan', TO_DATE('04.09.2018', 'dd.mm.yyyy' ));