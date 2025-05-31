db = db.getSiblingDB("persona_db");

db.createUser({
  user: "persona_db",
  pwd: "persona_db",
  roles: [
    { role: "readWrite", db: "persona_db" },
    { role: "dbAdmin", db: "persona_db" },
    { role: "userAdmin", db: "persona_db" }
  ],
  mechanisms: ["SCRAM-SHA-1", "SCRAM-SHA-256"]
});

db.createCollection("persona");
db.createCollection("profesion");
db.createCollection("telefono");
db.createCollection("estudios");
