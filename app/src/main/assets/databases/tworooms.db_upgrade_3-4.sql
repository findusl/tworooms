DELETE FROM set_roles WHERE id_set NOT IN (SELECT _id FROM sets);
INSERT INTO roles_en VALUES (100, "Blue Role",
    "You are a simple blue role. You want the president to survive.",1,100, 1);
INSERT INTO roles_de VALUES (100, "Blaue Rolle",
    "Du bist eine einfache blaue Rolle. Du möchtest das der Präsident überlebst.",1,100, 1);
INSERT INTO roles_en VALUES (101, "Red Role",
    "You are a simple red role. You want the president to die.",2,100, 1);
INSERT INTO roles_de VALUES (101, "Rote Rolle",
    "Du bist eine einfache rote Rolle. Du möchtest das der Präsident stirbt.",2,100, 1);