//db = connect("mongodb://persona_db:persona_db@mongodb:27017/persona_db");

db.createUser({
  user: "persona_db",
  pwd: "persona_db",
  roles: [
    { role: "read", db: "persona_db" },
    { role: "readWrite", db: "persona_db" },
    { role: "dbAdmin", db: "persona_db" }
  ],
  mechanisms: ["SCRAM-SHA-1", "SCRAM-SHA-256"]
});

// Crear las colecciones
db.createCollection("persona");
db.createCollection("profesion");
db.createCollection("telefono");
db.createCollection("estudios");
