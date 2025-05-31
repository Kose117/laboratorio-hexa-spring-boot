// Connect to the MongoDB database
// const db = connect("mongodb://localhost:27017/prueba_db");
db = db.getSiblingDB("persona_db");
db.persona.insertMany([
	{
		"_id": NumberInt(123456789),
		"nombre": "Pepe",
		"apellido": "Perez",
		"genero": "M",
		"edad": NumberInt(30),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(987654321),
		"nombre": "Pepito",
		"apellido": "Perez",
		"genero": "M",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(321654987),
		"nombre": "Pepa",
		"apellido": "Juarez",
		"genero": "F",
		"edad": NumberInt(30),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(147258369),
		"nombre": "Pepita",
		"apellido": "Juarez",
		"genero": "F",
		"edad": NumberInt(10),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(963852741),
		"nombre": "Fede",
		"apellido": "Perez",
		"genero": "M",
		"edad": NumberInt(18),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	}
], { ordered: false });

db.profesion.insertMany([
	{
		"_id": 1,
		"nom": "Ingeniero",
		"des": "Descripción del Ingeniero"
	},
	{
		"_id": 2,
		"nom": "Medico",
		"des": "Descripción del Médico"
	}
]);

db.telefono.insertMany([
	{
		"num": "3001234567",
		"oper": "Movistar",
		"primaryDuenio": NumberInt(123456789) // Usar el ID real como NumberInt
	},
	{
		"num": "3101234567",
		"oper": "Claro",
		"primaryDuenio": NumberInt(987654321) // Usar el ID real como NumberInt
	}
]);

db.estudios.insertMany([
	{
		"primaryProfesion": NumberInt(1), // Cambiar a NumberInt si el _id de profesión es un entero
		"primaryPersona": NumberInt(123456789), // Cambiar a NumberInt si el _id de persona es un entero
		"fecha": ISODate("2020-01-01T00:00:00Z"),
		"univer": "Universidad Javeriana"
	},
	{
		"primaryProfesion": NumberInt(2), // Cambiar a NumberInt si el _id de profesión es un entero
		"primaryPersona": NumberInt(321654987), // Cambiar a NumberInt si el _id de persona es un entero
		"fecha": ISODate("2019-01-01T00:00:00Z"),
		"univer": "Universidad Nacional"
	}
]);
