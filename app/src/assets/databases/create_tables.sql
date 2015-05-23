CREATE TABLE `teams` (
	_id INTEGER PRIMARY KEY AUTOINCREMENT,
	`name`	TEXT
);
CREATE TABLE `roles` (
	_id INTEGER PRIMARY KEY AUTOINCREMENT,
	`name`	TEXT,
	`description`	TEXT,
	`team_id`	INTEGER NOT NULL,
	FOREIGN KEY(`team_id`) REFERENCES teams (_id) ON UPDATE CASCADE ON DELETE RESTRICT
);
CREATE TABLE `role_combinations` (
	`id_role1`	INTEGER NOT NULL,
	`id_role2`	INTEGER NOT NULL,
	PRIMARY KEY(id_role1,id_role2),
	FOREIGN KEY(`id_role1`) REFERENCES roles (_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(`id_role2`) REFERENCES roles (_id) ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE `sets` (
	_id INTEGER PRIMARY KEY AUTOINCREMENT,
	`name`	TEXT NOT NULL,
	`count`	INTEGER,
	`description` TEXT,
	`parent`	INTEGER DEFAULT -1
);
CREATE TABLE `set_nogo_roles` (
	`id_set`	INTEGER NOT NULL,
	`id_role`	INTEGER NOT NULL,
	PRIMARY KEY(id_set,id_role),
	FOREIGN KEY(`id_set`) REFERENCES sets (_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(`id_role`) REFERENCES roles (_id) ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE `set_roles` (
	`id_set`	INTEGER NOT NULL,
	`id_role`	INTEGER NOT NULL,
	PRIMARY KEY(id_set,id_role),
	FOREIGN KEY(`id_set`) REFERENCES sets (_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(`id_role`) REFERENCES roles (_id) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO roles VALUES 
	(1, "Engel", "Muss immer die Wahrheit sagen.", 1), 
	(2, "Engel", "Muss immer die Wahrheit sagen.", 2), 
	(3, "Blinder", "Muss immer die Augen geschlossen haben.", 1), 
	(4, "Blinder", "Muss immer die Augen geschlossen haben.", 2),
	(5, "Blaues Team", "Du gehörst dem blauen Team an und gewinnst wenn der Präsident nicht stirbt.", 1),
	(6, "Rotes Team", "Du gehörst dem roten Team an und gewinnst wenn der Präsident stirbt.", 2),
	(7, "Bomber", "Jeder der am Ende in deinem Raum ist stirbt. Das rote Team gewinnt falls der President stirbt.", 2),
	(8, "Präsident", "Das blaue Team gewinnt falls der Präsident überlebt.", 1),
	(9, "Clown", "Du musst die ganze Zeit lächeln.", 1), 
	(10, "Clown", "Du musst die ganze Zeit lächeln.", 2), 
	(11, "Scharfschütze", "Du kannst am Ende des Spieles ein Ziel töten. Du gewinnst wenn du das Ziel tötest.", 3),
	(12, "Köder", "Du gewinnst wenn der Scharfschütze dich tötet.", 3),
	(13, "Ziel", "Du gewinnst wenn der Scharfschütze dich nicht tötet.", 3),
	(14, "Arzt", "Der Präsident kann nur überleben wenn du bis zum Ende mindestens einmal mit ihm geshared hast.", 1),
	(15, "Ingenieur", "Die Bombe kann nur explodieren wenn du bis zum Ende mindestens einmal mit ihr geshared hast.", 2),
	(16, "Zocker", "Am Ende des Spiels bevor die Karten vorgezeigt werden musst du erraten welches Team gewonnen hat. Du gewinnst falls du richtig geraten hats.", 3);
	
	
INSERT INTO role_combinations VALUES
	(1,2),
	(3,4),
	(5,6),
	(7,8),
	(9,10),
	(11, 12),
	(12, 13),
	(13, 11),
	(14, 15);