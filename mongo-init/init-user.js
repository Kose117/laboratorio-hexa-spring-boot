db.createUser({
    user: "root",
    pwd: "rootpassword",
    roles: [
      { role: "readWrite", db: "my_database" },
      { role: "dbAdmin", db: "my_database" }
    ]
  });
  