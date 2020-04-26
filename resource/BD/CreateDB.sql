--oracle
DROP TABLE FoodCargo;
DROP TABLE ClotherCargo;
DROP TABLE Transportation;

DROP TABLE Carrier;
DROP TABLE Cargo;
DROP TABLE CargoType;
DROP TABLE CarrierType;

CREATE TABLE CargoType(
cargoType VARCHAR(128) PRIMARY KEY
);
CREATE TABLE CarrierType(
    carrierType VARCHAR(128) PRIMARY KEY
);
CREATE TABLE Carrier(
    id INT PRIMARY KEY,
    name VARCHAR(128),
    adress VARCHAR(128),
    carrierType VARCHAR(128),
    CONSTRAINT carrierType_FK FOREIGN KEY(carrierType) REFERENCES CarrierType(carrierType)
);

CREATE TABLE Cargo(
    id INT PRIMARY KEY,
    name VARCHAR(128),
    weight INT NOT NULL,
    cargoType VARCHAR(128),
    CONSTRAINT clotherType_FK FOREIGN KEY(cargoType) REFERENCES CargoType(cargoType)
);
CREATE TABLE ClotherCargo(
    id INT PRIMARY KEY,
    sizeCargo INT,
    material VARCHAR(128),
    CONSTRAINT ClotherCargo_FK FOREIGN KEY(id) REFERENCES Cargo(id)
);
CREATE TABLE FoodCargo(
    id INT PRIMARY KEY,
    expirationDate DATE,
    storeTemperature INT,
    CONSTRAINT FoodCargo_FK FOREIGN KEY(id) REFERENCES Cargo(id)
);

CREATE TABLE Transportation(
    id INT PRIMARY KEY,
    cargoId INT NOT NULL,
    carrierId INT NOT NULL,
    description VARCHAR(128),
    billTo VARCHAR(128),
    transportationBeginDate DATE,
    CONSTRAINT TransportationCargo_FK FOREIGN KEY(cargoId) REFERENCES Cargo(id),
    CONSTRAINT TransportationCarrier_FK FOREIGN KEY(carrierId) REFERENCES Carrier(id)
    
)
