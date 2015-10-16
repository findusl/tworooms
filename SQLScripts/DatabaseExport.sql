BEGIN;
CREATE TABLE `teams_en` (`_id` INTEGER,`name` TEXT,PRIMARY KEY(_id));
INSERT INTO `teams_en` VALUES (1,'Blue team');
INSERT INTO `teams_en` VALUES (2,'Red team');
INSERT INTO `teams_en` VALUES (3,'Grey team');
INSERT INTO `teams_en` VALUES (4,'Green team');
INSERT INTO `teams_en` VALUES (5,'Yellow team');
INSERT INTO `teams_en` VALUES (6,'Violett team');
INSERT INTO `teams_en` VALUES (7,'Black team');
CREATE TABLE teams_de (
	`_id`	INTEGER AUTO_INCREMENT,
	`name`	TEXT,
	PRIMARY KEY(_id)
);
INSERT INTO `teams_de` VALUES (1,'Blaues Team');
INSERT INTO `teams_de` VALUES (2,'Rotes Team');
INSERT INTO `teams_de` VALUES (3,'Graues Team');
INSERT INTO `teams_de` VALUES (4,'Gr&uuml;nes Team');
INSERT INTO `teams_de` VALUES (5,'Gelbes Team');
CREATE TABLE `categories_en` (
	`_id` INTEGER AUTO_INCREMENT,
	`name` TEXT,
	PRIMARY KEY(_id)
);
INSERT INTO `categories_en` VALUES (1,'Base roles');
INSERT INTO `categories_en` VALUES (2,'Simple grey roles');
INSERT INTO `categories_en` VALUES (3,'Simple team roles');
INSERT INTO `categories_en` VALUES (4,'Extended team roles');
INSERT INTO `categories_en` VALUES (5,'Extended other roles');
INSERT INTO `categories_en` VALUES (6,'Private reveal power roles');
INSERT INTO `categories_en` VALUES (7,'Team based grey roles');
INSERT INTO `categories_en` VALUES (8,'Color share roles');
INSERT INTO `categories_en` VALUES (9,'Card swap power roles');
INSERT INTO `categories_en` VALUES (10,'Public reveal power roles');
INSERT INTO `categories_en` VALUES (11,'Card share power roles');
INSERT INTO `categories_en` VALUES (12,'Exclusive/Instant winner roles');
INSERT INTO `categories_en` VALUES (13,'Burried card roles');
CREATE TABLE categories_de (
	`_id`	INTEGER AUTO_INCREMENT,
	`name`	TEXT,
	PRIMARY KEY(_id)
);
INSERT INTO `categories_de` VALUES (1,'Basis Rollen');
INSERT INTO `categories_de` VALUES (2,'Simple graue Rollen');
INSERT INTO `categories_de` VALUES (3,'Simple Team Rollen');
INSERT INTO `categories_de` VALUES (4,'Erweiterte Team Rollen');
INSERT INTO `categories_de` VALUES (5,'Andere erweiterte Rollen');
INSERT INTO `categories_de` VALUES (6,'Rollen mit private reveal Kr&auml;ften');
INSERT INTO `categories_de` VALUES (7,'Team basierte graue Rollen');
INSERT INTO `categories_de` VALUES (8,'Rollen f&uuml;r color share');
INSERT INTO `categories_de` VALUES (9,'Rollen mit card swap Kr&auml;ften');
INSERT INTO `categories_de` VALUES (10,'Karten mit public reveal Kr&auml;ften');
INSERT INTO `categories_de` VALUES (11,'Karten mit card share Kr&auml;ften');
INSERT INTO `categories_de` VALUES (12,'Instant/Exclusiver Sieger Rollen');
INSERT INTO `categories_de` VALUES (13,'Rollen f&uuml;r buried card');
CREATE TABLE roles_en (
	`_id`	INTEGER AUTO_INCREMENT,
	`name`	TEXT,
	`description` TEXT,
	`team_id`	INTEGER NOT NULL,
	`group`	INTEGER,
	`category`	INTEGER,
	PRIMARY KEY(_id),
	FOREIGN KEY(`team_id`) REFERENCES teams_en ( _id ) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY(`category`) REFERENCES categories_en ( _id ) ON UPDATE CASCADE ON DELETE RESTRICT
);
INSERT INTO `roles_en` VALUES (1,'President','You are a primary character. Blue Team wins if you do not gain the ''dead'' condition.
',1,1,1);
INSERT INTO `roles_en` VALUES (2,'Bomber','You are a primary character. Everyone in the same room as you at the end of the game gains the ''dead'' condition. The Red Team wins if the President gains the ''dead'' condition. Note: if the Bomber receives the ''dead'' condition before the end of the game then the Bomber does not provide the ''dead'' condition to everyone in the same room.
',2,1,1);
INSERT INTO `roles_en` VALUES (3,'Agent','You have the AGENT power: once per round, you may privately reveal your card to a player and force that player to card share with you. You must verbally say to the target player, ''I''m using my AGENT power. You must card share with me.'' Note: The AGENT power works even on characters that normally can''t card share (e.g. Shy Guy).',1,3,6);
INSERT INTO `roles_en` VALUES (4,'Agent','You have the AGENT power: once per round, you may privately reveal your card to a player and force that player to card share with you. You must verbally say to the target player, ''I''m using my AGENT power. You must card share with me.'' Note: The AGENT power works even on characters that normally can''t card share (e.g. Shy Guy).',2,3,6);
INSERT INTO `roles_en` VALUES (5,'Agoraphobe','You win as long as you never leave your initial room.',3,5,2);
INSERT INTO `roles_en` VALUES (6,'Ahab','
You win if Moby is in the same room as the BOMBER at the end of the game and you are not.',3,6,2);
INSERT INTO `roles_en` VALUES (7,'Moby','You win if Ahab is in the same room as the Bomber at the end of the game and you are not.
',3,6,2);
INSERT INTO `roles_en` VALUES (8,'Ambassador','As soon as you are dealt your card, announce ''I am an Ambassador!'' Your card is permanently publicly revealed. You have the ''immune'' condition. Players with the ''immune'' condition are immune to all powers and conditions. Ambassadors can walk freely between the 2 rooms. Ambassadors are never considered a part of a room''s population. Therefore, Ambassadors can never take part in any vote, be hostages, be leaders, and they can never be targeted by abilities. Note: Ambassadors don''t count towards the player count in the game. This means if you have 18 players including the Ambassadors, you should be playing a 16 player game. Ambassadors don''t count for or against Team Zombie''s win objective.
',1,8,4);
INSERT INTO `roles_en` VALUES (9,'Ambassador','As soon as you are dealt your card, announce ''I am an Ambassador!'' Your card is permanently publicly revealed. You have the ''immune'' condition. Players with the ''immune'' condition are immune to all powers and conditions. Ambassadors can walk freely between the 2 rooms. Ambassadors are never considered a part of a room''s population. Therefore, Ambassadors can never take part in any vote, be hostages, be leaders, and they can never be targeted by abilities. Note: Ambassadors don''t count towards the player count in the game. This means if you have 18 players including the Ambassadors, you should be playing a 16 player game. Ambassadors don''t count for or against Team Zombie''s win objective.
',2,8,4);
INSERT INTO `roles_en` VALUES (10,'Anarchist','You win if your vote helped successfully usurp a leader during a majority of the rounds. For example, in a 3 round game, you must have usurped a leader 2 of the 3 rounds.
',3,10,2);
INSERT INTO `roles_en` VALUES (11,'Angel','You begin with the ''honest'' condition. Players with the ''honest'' condition must always verbally tell the truth. This means that you are permitted to lie as long as it is not verbally. Note: If a player with the ''honest'' condition were to acquire the ''liar'' condition, the 2 conditions would cancel one another, leaving the player with neither condition.
',1,11,3);
INSERT INTO `roles_en` VALUES (12,'Angel','You begin with the ''honest'' condition. Players with the ''honest'' condition must always verbally tell the truth. This means that you are permitted to lie as long as it is not verbally. Note: If a player with the ''honest'' condition were to acquire the ''liar'' condition, the 2 conditions would cancel one another, leaving the player with neither condition.
',2,11,3);
INSERT INTO `roles_en` VALUES (13,'Blind','You begin with the ''blind'' condition. Players with the ''blind'' condition must do their best to never open their eyes during the game. Don''t worry, it is a short game.
',1,13,3);
INSERT INTO `roles_en` VALUES (14,'Blind','You begin with the ''blind'' condition. Players with the ''blind'' condition must do their best to never open their eyes during the game. Don''t worry, it is a short game.
',2,13,3);
INSERT INTO `roles_en` VALUES (15,'Bomb-Bot','You win if you are in the same room as the Bomber but the President is not.
',3,15,7);
INSERT INTO `roles_en` VALUES (16,'Queen','You win if you are NOT in the same room as the President or the Bomber at the end of the game.
',3,15,7);
INSERT INTO `roles_en` VALUES (17,'Bouncer','You have the BOUNCER power: if you are in a room that has more players than the other room, you may privately reveal your card to any player and verbally tell them, ''Get out!'' When you do, that player must immediately change rooms. The BOUNCER power does not work during the last round or between rounds.
',1,17,6);
INSERT INTO `roles_en` VALUES (18,'Bouncer','You have the BOUNCER power: if you are in a room that has more players than the other room, you may privately reveal your card to any player and verbally tell them, ''Get out!'' When you do, that player must immediately change rooms. The BOUNCER power does not work during the last round or between rounds.
',2,17,6);
INSERT INTO `roles_en` VALUES (19,'Butler','You win if you are in the same room as the Maid and the President at the end of the game.
',3,19,7);
INSERT INTO `roles_en` VALUES (20,'Maid','You win if you are in the same room as the Butler and the President at the end of the game. 
',3,19,7);
INSERT INTO `roles_en` VALUES (21,'Clone','You win if the first player with whom you card share or color share wins. If you fail to share with any player by the end of the game, you lose. Note: if the first person with whom you share is the Robot, and the Robot''s first share was with you, you both lose.
',3,21,5);
INSERT INTO `roles_en` VALUES (22,'Clown','Do your best to smile at all times.
',1,22,3);
INSERT INTO `roles_en` VALUES (23,'Clown','Do your best to smile at all times.
',2,22,3);
INSERT INTO `roles_en` VALUES (24,'Conman','You have the CONMAN power: when a player agrees to color share with you, private reveal instead. They must private reveal their card too.
',1,24,8);
INSERT INTO `roles_en` VALUES (25,'Conman','You have the CONMAN power: when a player agrees to color share with you, private reveal instead. They must private reveal their card too.
',2,24,8);
INSERT INTO `roles_en` VALUES (26,'Coyboy','You begin with the ''coy'' condition. Players with the ''coy'' condition may ONLY color share unless a character''s power forces otherwise. Note: The ''coy'' condition is a psych condition and can be cured by a Psychologist. Another Note: If a player with the ''coy'' condition were to acquire the ''foolish'' condition, the 2 conditions would cancel one another, leaving the player with neither condition.
',1,26,8);
INSERT INTO `roles_en` VALUES (27,'Coyboy','You begin with the ''coy'' condition. Players with the ''coy'' condition may ONLY color share unless a character''s power forces otherwise. Note: The ''coy'' condition is a psych condition and can be cured by a Psychologist. Another Note: If a player with the ''coy'' condition were to acquire the ''foolish'' condition, the 2 conditions would cancel one another, leaving the player with neither condition.
',2,26,8);
INSERT INTO `roles_en` VALUES (28,'Criminal','You have the CRIMINAL power: any player that card shares with you gains the ''shy'' condition. Players with the ''shy'' condition may not reveal any part of their card to any player. Note: If a player with the ''foolish'' condition were to acquire the ''shy'' condition, the 2 conditions would cancel one another, leaving the player with neither condition.
',1,28,6);
INSERT INTO `roles_en` VALUES (29,'Criminal','You have the CRIMINAL power: any player that card shares with you gains the ''shy'' condition. Players with the ''shy'' condition may not reveal any part of their card to any player. Note: If a player with the ''foolish'' condition were to acquire the ''shy'' condition, the 2 conditions would cancel one another, leaving the player with neither condition.
',2,28,6);
INSERT INTO `roles_en` VALUES (30,'Dealer','You have the DEALER power: any player that card shares with you gains the ''foolish'' condition. Players with the ''foolish'' condition can never turn down an offer to card or color share. Note: If a player with the ''foolish'' condition were to acquire a contradictory condition (e.g. ''shy'' or ''coy''), the 2 conditions would cancel one another, leaving the player with neither condition.
',1,30,6);
INSERT INTO `roles_en` VALUES (31,'Dealer','You have the DEALER power: any player that card shares with you gains the ''foolish'' condition. Players with the ''foolish'' condition can never turn down an offer to card or color share. Note: If a player with the ''foolish'' condition were to acquire a contradictory condition (e.g. ''shy'' or ''coy''), the 2 conditions would cancel one another, leaving the player with neither condition.
',2,30,6);
INSERT INTO `roles_en` VALUES (32,'Demon','You begin with the ''liar'' condition. Players with the ''liar'' condition must always verbally tell lies. This means that are permitted to tell the truth as long as it is not verbally. Note: If a player with the ''liar'' condition were to acquire the ''honest'' condition, the 2 conditions would cancel one another, leaving the player with neither condition.
',1,32,3);
INSERT INTO `roles_en` VALUES (33,'Demon','You begin with the ''liar'' condition. Players with the ''liar'' condition must always verbally tell lies. This means that are permitted to tell the truth as long as it is not verbally. Note: If a player with the ''liar'' condition were to acquire the ''honest'' condition, the 2 conditions would cancel one another, leaving the player with neither condition.
',2,32,3);
INSERT INTO `roles_en` VALUES (34,'Doctor','When playing with the Doctor, the Blue Team has the following additional win condition: the President must card share with the Doctor before the end of the game or the Blue Team loses. At the end of the game, the President will be asked if he/she card shared with the Doctor. At that time both players will verify or deny having card shared. Note: remember that if the President or Bomber character ever switches player control (perhaps due to the Hot Potato), then the new President/Bomber must card share with the Doctor/Engineer again.
',1,34,1);
INSERT INTO `roles_en` VALUES (35,'Engineer','When playing with the Engineer, the Red Team has the following additional win condition: the Bomber must card share with the Engineer before the end of the game or the Red Team loses. At the end of the game, the Bomber will be asked if he/she card shared with the Engineer. At that time both players will verify or deny having card shared. Note: remember that if the President or Bomber character ever switches player control (perhaps due to the Hot Potato), then the new President/Bomber must card share with the Doctor/Engineer again.
',2,34,1);
INSERT INTO `roles_en` VALUES (36,'Drunk','Before characters cards are dealt but after they are shuffled, randomly remove a character card. This is the ''sober'' character card. Place the ''sober'' card facedown in a location easily accessible to all players (usually between the 2 rooms). Then shuffle the Drunk card into the remaining deck of character cards. At the beginning of the last round of the game, the Drunk character should trade their Drunk card with the ''sober'' card. Assume all powers and responsibilities associated with the ''sober'' character card. You lose if you forget or are unable to trade your card for the ''sober'' card. Note: The ''sober'' card is always cleansed when it is first retrieved, meaning it has no acquired conditions. Another Note: If you don''t retrieve the ''sober'' card at the beginning of the last round, you''re still considered to be the ''sober'' character considering the result of the other players.
',6,36,9);
INSERT INTO `roles_en` VALUES (37,'Enforcer','You have the ENFORCER power: once per round, you may privately reveal your card to 2 players. You must verbally tell your target players, ''You must reveal your cards to one another.'' Those 2 players must card share with one another (not to you). You cannot use this power on yourself, but another Enforcer can use their power on you. Note: The ENFORCER power works even on characters that normally can''t card share (e.g. Shy Guy).
',1,37,6);
INSERT INTO `roles_en` VALUES (38,'Enforcer','You have the ENFORCER power: once per round, you may privately reveal your card to 2 players. You must verbally tell your target players, ''You must reveal your cards to one another.'' Those 2 players must card share with one another (not to you). You cannot use this power on yourself, but another Enforcer can use their power on you. Note: The ENFORCER power works even on characters that normally can''t card share (e.g. Shy Guy).
',2,37,6);
INSERT INTO `roles_en` VALUES (39,'Eris','You have the ERIS power: once per game, you may privately reveal your card to 2 players. You must verbally tell your target players, ''You hate each other.'' Those 2 players gain the ''in hate'' condition. Players with the ''in hate'' condition replace their original win objective with the following win objective: be in the opposite room of the player with whom you are ''in hate'' at the end of the game or fail to win. You cannot use this power on yourself. Note: If a player with the ''in hate'' condition were to acquire the ''in love'' condition, the 2 conditions would cancel one another, leaving that specific player with neither condition.
',1,39,6);
INSERT INTO `roles_en` VALUES (40,'Cupid','You have the CUPID power: once per game, you may privately reveal your card to 2 players. You must verbally tell your target players, ''You are in love with each other.'' Those 2 players gain the ''in love'' condition. Players with the ''in love'' condition replace their original win objective with the following win objective: Be in the same room with the player with whom you are ''in love'' at the end of the game or fail to win. The CUPID power cannot be used on yourself. Note: If a player with the ''in love'' condition were to acquire the ''in hate'' condition, the 2 conditions would cancel one another, leaving that specific player with neither condition.
',2,39,6);
INSERT INTO `roles_en` VALUES (41,'Gambler','At the end of the last round, before all players reveal their cards, you must publicly announce which team (Red Team, Blue Team, or neither) you think won the game. Win only if you are correct. Note: You have a pause game number of 10. Only pause the game and make your announcement after all characters with a lower pause game number (e.g. Private Eye) have finished their announcements.
',3,41,2);
INSERT INTO `roles_en` VALUES (42,'Hot Potato','You have the HOT POTATO power: any player that card shares or color shares with you immediately trades cards with you. Both you and the other player assume the powers and the allegiance of the newly acquired cards. The Hot Potato loses at the end of the game. Note: Due to the cleanse rule (see page 13), any previously acquired conditions (e.g. ''cursed'') are lost when receiving a new character card.
',3,42,9);
INSERT INTO `roles_en` VALUES (43,'Immunologist','You begin with the ''immune'' condition. Players with the ''immune'' condition are immune to all powers and conditions without exception. Note: Players should never lie about having the ''immune'' condition.
',1,43,3);
INSERT INTO `roles_en` VALUES (44,'Immunologist','You begin with the ''immune'' condition. Players with the ''immune'' condition are immune to all powers and conditions without exception. Note: Players should never lie about having the ''immune'' condition.
',2,43,3);
INSERT INTO `roles_en` VALUES (45,'Intern','You win if you are in the same room as the President at the end of the game.
',3,45,7);
INSERT INTO `roles_en` VALUES (46,'Victim','You win if you are in the same room as the Bomber at the end of the game.
',3,45,7);
INSERT INTO `roles_en` VALUES (47,'Juliet','You win if you are in the same room as Romeo and the Bomber at the end of the game.
',3,47,7);
INSERT INTO `roles_en` VALUES (48,'Romeo','You win if you are in the same room as Juliet and the same room as the Bomber at the end of the game.
',3,47,7);
INSERT INTO `roles_en` VALUES (49,'Leprechaun','You begin with the ''foolish'' condition. Players with the ''foolish'' condition can never turn down an offer to card or color share. You also have the LEPRECHAUN power: Any player that card shares or even color shares with you immediately trades cards with you. Both you and the other player assume the powers and the allegiance of the newly acquired cards. At the end of the game, the Leprechaun wins. A single player can only ever be the Leprechaun once per game. If a player is about to become the Leprechaun character for the second time, they must communicate that they can''t receive the Leprechaun card. Note: Due to the cleanse rule (see Hot Potato), any previously acquired conditions (especially the share between doctor/president, bomber/engineer) are lost when receiving a new character card. Another Note: If a player with the ''foolish'' condition were to acquire a contradictory condition (e.g. ''shy'' or ''coy''), the 2 conditions would cancel one another, leaving the player with neither condition.
',4,49,9);
INSERT INTO `roles_en` VALUES (50,'Mastermind','You win if you are a room''s leader at the end AND you were the leader of the opposing room at some point during the game.
',3,50,5);
INSERT INTO `roles_en` VALUES (51,'Mayor','If your room has an even number of players, you may publicly reveal your card when attempting to usurp a leader. Your vote to usurp counts as 2 votes instead of 1 unless the opposing Mayor also publicly reveals their card.
',1,51,10);
INSERT INTO `roles_en` VALUES (52,'Mayor','If your room has an even number of players, you may publicly reveal your card when attempting to usurp a leader. Your vote to usurp counts as 2 votes instead of 1 unless the opposing Mayor also publicly reveals their card.
',2,51,10);
INSERT INTO `roles_en` VALUES (53,'Medic','You have the MEDIC power: any player that card shares with you has all ''conditions'' removed. This does not make yourself immune to acquiring conditions. The opposing Medic can remove your acquired conditions.
',1,53,11);
INSERT INTO `roles_en` VALUES (54,'Medic','You have the MEDIC power: any player that card shares with you has all ''conditions'' removed. This does not make yourself immune to acquiring conditions. The opposing Medic can remove your acquired conditions.
',2,53,11);
INSERT INTO `roles_en` VALUES (55,'MI6','You win if you card share with the Bomber and the President before the end of the game.
',3,55,7);
INSERT INTO `roles_en` VALUES (56,'Mime','Do your best to not make any noise.
',1,56,3);
INSERT INTO `roles_en` VALUES (57,'Mime','Do your best to not make any noise.
',2,56,3);
INSERT INTO `roles_en` VALUES (58,'Minion','You win if a leader is never usurped in the same room as you.
',3,58,2);
INSERT INTO `roles_en` VALUES (59,'Mistress','You win if you are in the same room as the President at the end of the game and the Wife is not.
',3,59,7);
INSERT INTO `roles_en` VALUES (60,'Wife','You win if you are in the same room as the president at the end of the game and the mistress is not.
',3,59,7);
INSERT INTO `roles_en` VALUES (61,'Mummy','You have the MUMMY power: any player that card shares with you gains the ''cursed'' condition. Players with the ''cursed'' condition must do their best to not make any noise. Note: Because of the MUMMY power, ''cursed'' players are prevented from using any powers or abilities that requires a verbalization (e.g. Agent, Enforcer, etc.).
',1,61,11);
INSERT INTO `roles_en` VALUES (62,'Mummy','You have the MUMMY power: any player that card shares with you gains the ''cursed'' condition. Players with the ''cursed'' condition must do their best to not make any noise. Note: Because of the MUMMY power, ''cursed'' players are prevented from using any powers or abilities that requires a verbalization (e.g. Agent, Enforcer, etc.).
',2,61,11);
INSERT INTO `roles_en` VALUES (63,'Negotiator','You begin with the ''savvy'' condition. Players with the ''savvy'' condition may only card share. You may not publicly, privately, or color share. Note: It is possible for ''savvy'' players to acquire conditions that prevent card sharing (e.g. the ''coy'' or ''shy'' condition). If this happens, then the ''savvy'' player can''t do anything with their card.
',1,63,8);
INSERT INTO `roles_en` VALUES (64,'Negotiator','You begin with the ''savvy'' condition. Players with the ''savvy'' condition may only card share. You may not publicly, privately, or color share. Note: It is possible for ''savvy'' players to acquire conditions that prevent card sharing (e.g. the ''coy'' or ''shy'' condition). If this happens, then the ''savvy'' player can''t do anything with their card.
',2,63,8);
INSERT INTO `roles_en` VALUES (65,'Nuclear tyrant','At the end of the game, you are asked if you shared your card with both the President and the Bomber. You win if the President and the Bomber did not card share with you by the end of the game. If you win, all other players lose. You begin with the ''foolish'' condition. Players with the ''foolish'' condition can never turn down an offer to card share. Note: If a player with the ''foolish'' condition were to acquire a contradictory condition (e.g. ''shy'' or ''coy''), the 2 conditions would cancel one another, leaving the player with neither condition.
',3,65,12);
INSERT INTO `roles_en` VALUES (66,'Nurse','You are the backup character for the Doctor. If the Doctor card is buried, you must carry out Doctor responsibilities (i.e. card sharing with the President).
',1,66,13);
INSERT INTO `roles_en` VALUES (67,'Tinkerer','You are the backup character for the Engineer. If the Engineer card is buried you must carry out all Engineer responsibilities (i.e. card sharing with the Bomber).
',2,66,13);
INSERT INTO `roles_en` VALUES (68,'Paparazzo','Do your best to make sure there are no private conversations. Be as intrusive and nosy as possible without actually physically manipulating others. If playing with the Privacy Promise rule variant, ignore the rule as long as you publicly reveal your card to prove to others that you are permitted to be invasive.
',1,68,10);
INSERT INTO `roles_en` VALUES (69,'Paparazzo','Do your best to make sure there are no private conversations. Be as intrusive and nosy as possible without actually physically manipulating others. If playing with the Privacy Promise rule variant, ignore the rule as long as you publicly reveal your card to prove to others that you are permitted to be invasive.
',2,68,10);
INSERT INTO `roles_en` VALUES (70,'Paranoid','You begin with the ''paranoid'' condition. Players with the ''paranoid'' condition may only card share. Moreover, they may only card share once per game. Note: If a ''paranoid'' player is forced to card share by a power (e.g. the AGENT power), this does not count as the ''paranoid'' player''s only card share. Another Note: The ''paranoid'' condition is a psych condition and can be cured by a Psychologist. Another Note: If a player with the ''paranoid'' condition were to acquire the ''foolish'' condition, the 2 conditions would cancel one another, leaving the player with neither condition.
',1,70,4);
INSERT INTO `roles_en` VALUES (71,'Paranoid','You begin with the ''paranoid'' condition. Players with the ''paranoid'' condition may only card share. Moreover, they may only card share once per game. Note: If a ''paranoid'' player is forced to card share by a power (e.g. the AGENT power), this does not count as the ''paranoid'' player''s only card share. Another Note: The ''paranoid'' condition is a psych condition and can be cured by a Psychologist. Another Note: If a player with the ''paranoid'' condition were to acquire the ''foolish'' condition, the 2 conditions would cancel one another, leaving the player with neither condition.
',2,70,4);
INSERT INTO `roles_en` VALUES (72,'President''s daughter','You are the backup character for the President. If the President card is buried you must carry out all responsibilities associated with the President.
',1,72,13);
INSERT INTO `roles_en` VALUES (73,'Martyr','You are the backup character for the Bomber. If the Bomber card is buried you must carry out all Bomber responsibilities (i.e. ending the game in the same room as the President, card sharing with the Engineer, etc.).
',2,72,13);
INSERT INTO `roles_en` VALUES (74,'Private eye','At the end of the last round, before all players reveal their character cards, you must publicly announce the identity of the buried card. Win only if you are correct. Note: You have a pause game number of 5. Only pause the game and make your announcement after all characters with a lower pause game number have finished their announcements.
',3,74,13);
INSERT INTO `roles_en` VALUES (75,'Psychologist','When you privately reveal your card to a character with a psych condition (e.g. ''shy'', ''coy'', etc.), that character may then immediately card share with you. If they do, their psych condition is removed.
',1,75,6);
INSERT INTO `roles_en` VALUES (76,'Psychologist','When you privately reveal your card to a character with a psych condition (e.g. ''shy'', ''coy'', etc.), that character may then immediately card share with you. If they do, their psych condition is removed.
',2,75,6);
INSERT INTO `roles_en` VALUES (77,'Rival','You win if you are NOT in the same room as the President at the end of the game.
',3,77,7);
INSERT INTO `roles_en` VALUES (78,'Survivor','You win if you are NOT in the same room as the Bomber at the end of the game.
',3,77,7);
INSERT INTO `roles_en` VALUES (79,'Robot','You win if the first player with whom you card share or color share fails to achieve all of their win objectives. If you fail to share with any players by the end of the game, then you lose. Note: if the first person with whom you share is the Clone, and the Clone''s first share was with you, you both lose.
',3,79,5);
INSERT INTO `roles_en` VALUES (80,'Security','You have the TACKLE power: publicly reveal your card, immediately pick any player in the room (besides yourself), then verbally tell them, ''You''re going nowhere.'' However, your card must permanently remain publicly revealed for the rest of the game. This means that you can only use this power once. The target of your TACKLE power can''t leave as a hostage this round.
',1,80,10);
INSERT INTO `roles_en` VALUES (81,'Security','You have the TACKLE power: publicly reveal your card, immediately pick any player in the room (besides yourself), then verbally tell them, ''You''re going nowhere.'' However, your card must permanently remain publicly revealed for the rest of the game. This means that you can only use this power once. The target of your TACKLE power can''t leave as a hostage this round.
',2,80,10);
INSERT INTO `roles_en` VALUES (82,'Shy guy','You begin with the ''shy'' condition. Players with the ''shy'' condition may not reveal any part of their card to any player. Note: The ''shy'' condition is a psych condition and can be cured by a Psychologist. Another Note: If a player with the ''shy'' condition were to acquire the ''foolish'' condition, the 2 conditions would cancel one another, leaving the player with neither condition.
',1,82,3);
INSERT INTO `roles_en` VALUES (83,'Shy guy','You begin with the ''shy'' condition. Players with the ''shy'' condition may not reveal any part of their card to any player. Note: The ''shy'' condition is a psych condition and can be cured by a Psychologist. Another Note: If a player with the ''shy'' condition were to acquire the ''foolish'' condition, the 2 conditions would cancel one another, leaving the player with neither condition.
',2,82,3);
INSERT INTO `roles_en` VALUES (84,'Sniper','At the end of the last round, before all players reveal their character cards, you must publicly announce which player you are shooting. The selected player does not have to be in the same room as you. You win if the player you selected is the Target. Note: You have a pause game number of 15. Only pause the game and make your announcement after all characters with a lower pause game number (e.g. Private Eye) have finished their announcements.
',3,84,2);
INSERT INTO `roles_en` VALUES (85,'Target','You win if the Sniper does not shoot you at the end of the last round.
',3,84,2);
INSERT INTO `roles_en` VALUES (86,'Decoy','You win if the Sniper shoots you at the end of the last round.
',3,84,2);
INSERT INTO `roles_en` VALUES (87,'Blue Spy','This is a special character card that is the color of the opposite team. This means that the Blue Spy has an allegiance to the Blue Team, but their card is red.
',2,87,8);
INSERT INTO `roles_en` VALUES (88,'Red Spy','This is a special character card that is the color of the opposite team. This means that the Red Spy has an allegiance to the Red Team, but their card is blue.
',1,87,8);
INSERT INTO `roles_en` VALUES (89,'Thug','You have the THUG power: any player that card shares with you acquires the ''coy'' condition. Players with the ''coy'' condition may only color share even when a character''s power might force a card share. Note: If a player with the ''foolish'' condition were to acquire the ''coy'' condition, the 2 conditions would cancel one another, leaving the player with neither condition.
',1,89,8);
INSERT INTO `roles_en` VALUES (90,'Thug','You have the THUG power: any player that card shares with you acquires the ''coy'' condition. Players with the ''coy'' condition may only color share even when a character''s power might force a card share. Note: If a player with the ''foolish'' condition were to acquire the ''coy'' condition, the 2 conditions would cancel one another, leaving the player with neither condition.
',2,89,8);
INSERT INTO `roles_en` VALUES (91,'Traveler','You win if you are sent to a different room as a hostage at the end of MOST rounds. For example, in a 3 round game, you must change rooms twice to win.
',3,91,2);
INSERT INTO `roles_en` VALUES (92,'Tuesday Knight','You have the HUG power: if you card share with the Bomber, everyone in the same room as you, except the President, gains the ''dead'' condition and the game instantly ends. Note: the HUG power never works on the Martyr. If the Bomber is buried, the HUG power is never used.
',1,92,12);
INSERT INTO `roles_en` VALUES (93,'Dr. Boom','You have the BOOM power: if you card share with the President, everyone in the same room as you instantly gains the ''dead'' condition and the game ends. Note: the BOOM power never works on the President''s Daughter. If the President is buried, the BOOM power is never used.
',2,92,12);
INSERT INTO `roles_en` VALUES (94,'Ursurper','You have the USURPER power: during any round but the last, you may publicly reveal your card and become the leader. However, your card must permanently remain publicly revealed for the rest of the game. This means that you can only use this power once. You cannot be usurped during the same round in which you used your USURPER power, not even by another Usurper. If 2 Usurpers use their power in the same room during the same round, whichever Usurper used their power first remains the leader, the other Usurper wasted their power.
',1,94,10);
INSERT INTO `roles_en` VALUES (95,'Ursurper','You have the USURPER power: during any round but the last, you may publicly reveal your card and become the leader. However, your card must permanently remain publicly revealed for the rest of the game. This means that you can only use this power once. You cannot be usurped during the same round in which you used your USURPER power, not even by another Usurper. If 2 Usurpers use their power in the same room during the same round, whichever Usurper used their power first remains the leader, the other Usurper wasted their power.
',2,94,10);
INSERT INTO `roles_en` VALUES (96,'Zombie','You begin with the ''zombie'' condition. The ''zombie'' condition provides a replacement win condition. Players with the ''zombie'' condition win if Team Zombie wins. Team Zombie wins if all players without the ''dead'' condition at the end of the game are on Team Zombie. Any player that card shares or color shares with a player that has the ''zombie'' condition gains the ''zombie'' condition. Note: ''zombie'' players without a Zombie character card must indicate to players with whom they card share or color share that they now also have the ''zombie'' condition. Typically this is done by saying something like, ''I''m a Zombie, and now so are you.''
',4,96,5);
CREATE TABLE roles_de (
	`_id`	INTEGER AUTO_INCREMENT,
	`name`	TEXT,
	`description`	TEXT,
	`team_id`	INTEGER NOT NULL,
	`group`	INTEGER,
	`category`	INTEGER,
	PRIMARY KEY(_id),
	FOREIGN KEY(`team_id`) REFERENCES teams_de ( _id ) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY(`category`) REFERENCES categories_de ( _id ) ON UPDATE CASCADE ON DELETE RESTRICT
);
INSERT INTO `roles_de` VALUES (1,'Pr&auml;sident','Du bist ein prim&auml;rer Charakter. Das blaue Team gewinnt falls du nicht stirbst.
',1,1,1);
INSERT INTO `roles_de` VALUES (2,'Bomber','Jeder der am Ende des Spiels in deinem Raum ist stirbt. Das Rote Team gewinnt falls der Pr&auml;sident stirbt. Notiz: Falls der Bomber vor dem Ende des spiels stirbt, explodiert er nicht.
',2,1,1);
INSERT INTO `roles_de` VALUES (3,'Agent','Einmal pro Runde kannst du deine Karte einem S pieler zeigen und ihn dazu zwingen mit dir einen card share zu vollf&uuml;hren. Du musst ihm explizit sagen das du deine Agenten Kraft nutzt und er mit dir card sharen muss. Notiz: Diese Kraft funktioniert auch bei Charakteren die nicht card sharen d&uuml;rfen (Scheue usw).
',1,3,6);
INSERT INTO `roles_de` VALUES (4,'Agent','Einmal pro Runde kannst du deine Karte einem S pieler zeigen und ihn dazu zwingen mit dir einen card share zu vollf&uuml;hren. Du musst ihm explizit sagen das du deine Agenten Kraft nutzt und er mit dir card sharen muss. Notiz: Diese Kraft funktioniert auch bei Charakteren die nicht card sharen d&uuml;rfen (Scheue usw).
',2,3,6);
INSERT INTO `roles_de` VALUES (5,'Agoraphobe','Du gewinnst nur falls du niemals den Raum wechselst.
',3,5,2);
INSERT INTO `roles_de` VALUES (6,'Ahab','Du gewinnst falls am Ende Moby und der BOMBER im selben Raum sind und du im anderen Raum bist.
',3,6,2);
INSERT INTO `roles_de` VALUES (7,'Moby','Du gewinnst falls Ahab am Ende des spiels im selben Raum ist wie der Bomber du aber nicht.
',3,6,2);
INSERT INTO `roles_de` VALUES (8,'Botschafter','Sobald du deine Karte erh&auml;ltst, zeige sie dauerhaft &ouml;ffentlich vor und verk&uuml;nde ''Ich bin ein Botschafter''. Du bist ''immun'' gegen s&auml;mtliche Kr&auml;fte und Zust&auml;nde. Du kannst frei zwischen den zwei R&auml;umen wechseln. Du z&auml;hlst in keinem Raum zur Bev&ouml;lkerung und kannst weder w&auml;hlen noch Geisel, Anf&uuml;hrer oder Ziel einer F&auml;higkeit sein. Notiz: Das Spiel sollte f&uuml;r 2 Spieler weniger ausgelegt sein da die Botschafter nicht mitz&auml;hlen. Die Botschafter beeinflussen die Siegesbedingung der Zombies nicht.
',1,8,4);
INSERT INTO `roles_de` VALUES (9,'Botschafter','Sobald du deine Karte erh&auml;ltst, zeige sie dauerhaft &ouml;ffentlich vor und verk&uuml;nde ''Ich bin ein Botschafter''. Du bist ''immun'' gegen s&auml;mtliche Kr&auml;fte und Zust&auml;nde. Du kannst frei zwischen den zwei R&auml;umen wechseln. Du z&auml;hlst in keinem Raum zur Bev&ouml;lkerung und kannst weder w&auml;hlen noch Geisel, Anf&uuml;hrer oder Ziel einer F&auml;higkeit sein. Notiz: Das Spiel sollte f&uuml;r 2 Spieler weniger ausgelegt sein da die Botschafter nicht mitz&auml;hlen. Die Botschafter beeinflussen die Siegesbedingung der Zombies nicht.
',2,8,4);
INSERT INTO `roles_de` VALUES (10,'Anarchist','Du gewinnst falls du in mehr als der H&auml;lfte der Runden dabei geholfen hast einen neuen Anf&uuml;hrer zu w&auml;hlen. 
',3,10,2);
INSERT INTO `roles_de` VALUES (11,'Engel','Du musst bei jeder Aussage ehrlich sein, darfst also nicht (verbal) l&uuml;gen. Notiz: Solltest du durch eine F&auml;higkeit zum L&uuml;gner werden, heben sich die beiden Effekte gegenseitig auf.
',1,11,3);
INSERT INTO `roles_de` VALUES (12,'Engel','Du musst bei jeder Aussage ehrlich sein, darfst also nicht (verbal) l&uuml;gen. Notiz: Solltest du durch eine F&auml;higkeit zum L&uuml;gner werden, heben sich die beiden Effekte gegenseitig auf.
',2,11,3);
INSERT INTO `roles_de` VALUES (13,'Blinder','Du bist blind. Du darfst w&auml;hrend des ganzen Spiels deine Augen nicht &ouml;ffnen.
',1,13,3);
INSERT INTO `roles_de` VALUES (14,'Blinder','Du bist blind. Du darfst w&auml;hrend des ganzen Spiels deine Augen nicht &ouml;ffnen.
',2,13,3);
INSERT INTO `roles_de` VALUES (15,'Bombendummy','Du gewinnst falls du mit dem Bomber in einem Raum bist und der Pr&auml;sident im anderen Raum ist.
',3,15,7);
INSERT INTO `roles_de` VALUES (16,'K&ouml;nigin','Du gewinnst falls du am Ende nicht im selben Raum bist wie der Pr&auml;sident und der Bomber.
',3,15,7);
INSERT INTO `roles_de` VALUES (17,'Rausschmei&szlig;er','Wenn du in dem Raum bist der mehr Spieler hat als der andere, kannst du einem Spieler deine Karte privat zeigen und musst ihm sagen ''Raus''. Dieser Spieler muss sofort den Raum wechseln. Diese Kraft funktioniert nicht w&auml;hrend der letzten Runde.
',1,17,6);
INSERT INTO `roles_de` VALUES (18,'Rausschmei&szlig;er','Wenn du in dem Raum bist der mehr Spieler hat als der andere, kannst du einem Spieler deine Karte privat zeigen und musst ihm sagen ''Raus''. Dieser Spieler muss sofort den Raum wechseln. Diese Kraft funktioniert nicht w&auml;hrend der letzten Runde.
',2,17,6);
INSERT INTO `roles_de` VALUES (19,'Butler','Du gewinnst falls du am Ende des Spiels im selben Raum wie Maid und Pr&auml;sident bist.
',3,19,7);
INSERT INTO `roles_de` VALUES (20,'Maid','Du gewinnst falls du am Ende des Spiels im selben Raum wie Butler und Pr&auml;sident bist.
',3,19,7);
INSERT INTO `roles_de` VALUES (21,'Klon','Du gewinnst falls die erste Person gewinnt mit der du card oder color sharest.
',3,21,5);
INSERT INTO `roles_de` VALUES (22,'Clown','Du musst immer l&auml;cheln.
',1,22,3);
INSERT INTO `roles_de` VALUES (23,'Clown','Du musst immer l&auml;cheln.
',2,22,3);
INSERT INTO `roles_de` VALUES (24,'Hochstapler','Wenn ein Spieler mit dir color shared, private reveale stattdessen deine Karte. Er muss seine Karte auch private revealen. Notiz: Die englische version l&auml;sst vermuten das dies nicht optional ist.
',1,24,8);
INSERT INTO `roles_de` VALUES (25,'Hochstapler','Wenn ein Spieler mit dir color shared, private reveale stattdessen deine Karte. Er muss seine Karte auch private revealen. Notiz: Die englische version l&auml;sst vermuten das dies nicht optional ist.
',2,24,8);
INSERT INTO `roles_de` VALUES (26,'Scheue','Du bist scheu. Du darfst lediglich color sharen. Au&szlig;er die Kraft einer Sonderrole zwingt dich zu was anderem. Notiz: Psychologischer effekt Notiz: Falls du leichtsinnig wirst heben sich die Kr&auml;fte auf.
',1,26,8);
INSERT INTO `roles_de` VALUES (27,'Scheue','Du bist scheu. Du darfst lediglich color sharen. Au&szlig;er die Kraft einer Sonderrole zwingt dich zu was anderem. Notiz: Psychologischer effekt Notiz: Falls du leichtsinnig wirst heben sich die Kr&auml;fte auf.
',2,26,8);
INSERT INTO `roles_de` VALUES (28,'Krimineller','Wenn du mit einem Spieler card sharest wird dieser sch&uuml;chtern. Sch&uuml;chterne Spieler d&uuml;rfen keine Teile ihrer Karte anderen Spielern zeigen. Notiz: Sch&uuml;chtern und leichtsinnig heben sich auf.
',1,28,6);
INSERT INTO `roles_de` VALUES (29,'Krimineller','Wenn du mit einem Spieler card sharest wird dieser sch&uuml;chtern. Sch&uuml;chterne Spieler d&uuml;rfen keine Teile ihrer Karte anderen Spielern zeigen. Notiz: Sch&uuml;chtern und leichtsinnig heben sich auf.
',2,28,6);
INSERT INTO `roles_de` VALUES (30,'Dealer','Wenn du mit jemandem card sharest wird dieser Spieler leichtsinnig. Leichtsinnige Personen m&uuml;ssen jeden card share oder color share annehmen. Notiz: Falls ein leichtsinniger Spieler sch&uuml;chtern oder scheu wird heben sich die beiden Bedingungen auf.
',1,30,6);
INSERT INTO `roles_de` VALUES (31,'Dealer','Wenn du mit jemandem card sharest wird dieser Spieler leichtsinnig. Leichtsinnige Personen m&uuml;ssen jeden card share oder color share annehmen. Notiz: Falls ein leichtsinniger Spieler sch&uuml;chtern oder scheu wird heben sich die beiden Bedingungen auf.
',2,30,6);
INSERT INTO `roles_de` VALUES (32,'D&auml;mon','Du musst bei jeder Aussage l&uuml;gen. Non verbal darfst du die Wahrheit sagen. Notiz: Sollte ein D&auml;mon dazu gezwungen werden ehrlich zu sein, heben sich die Bedingungen auf.
',1,32,3);
INSERT INTO `roles_de` VALUES (33,'D&auml;mon','Du musst bei jeder Aussage l&uuml;gen. Non verbal darfst du die Wahrheit sagen. Notiz: Sollte ein D&auml;mon dazu gezwungen werden ehrlich zu sein, heben sich die Bedingungen auf.
',2,32,3);
INSERT INTO `roles_de` VALUES (34,'Arzt','Wenn der Arzt im Spiel ist hat Team Blau eine weitere Siegesbedingung: Der Pr&auml;sident muss mit dem Doktor vor dem Ende des Spiels sharen oder das rote Team verliert. Notiz: Sollte der Pr&auml;sident den Spieler wechseln, muss der Arzt erneut mit ihm tauschen.
',1,34,1);
INSERT INTO `roles_de` VALUES (35,'Ingenieur','Wenn der Ingenieur im Spiel ist hat Team Rot eine weitere Siegesbedingung: Der Bomber muss mit dem Ingenieur vor dem Ende des Spiels sharen oder das rote Team verliert. Notiz: Sollte der Bomber den Spieler wechseln, muss der Ingenieur erneut mit ihm tauschen.
',2,34,1);
INSERT INTO `roles_de` VALUES (36,'Betrunkener','Bevor die Charaktere ausgeteilt werden wird eine Karte zur Seite gelegt. Du musst diese Karte in der letzten Runde gegen deine eigene eintauschen und wirst n&uuml;chtern. Du erh&auml;ltst s&auml;mtliche Kr&auml;fte und Aufgaben der neuen Karte. Vergisst oder kannst du die Karte nicht aufheben hast du verloren. Notiz: Um das Spielergebnis f&uuml;r die anderen Spieler zu berechnen, wirst du als der n&uuml;chterne Charakter gerechnet, auch wenn du die Karte nicht ausgetauscht hast.
',6,36,9);
INSERT INTO `roles_de` VALUES (37,'Erpresser','Einmal pro Runde kannst du deine Karte zwei anderen Spielern private revealen. Tu musst ihnen sagen ''Ihr m&uuml;sst einen card share durchf&uuml;hren''. Die zwei Spieler m&uuml;ssen einen Card share miteinander machen (nicht mit dir). Du kannst deine Kraft nicht auf dich selber einsetzen. Notiz: Funktioniert auch bei charakteren die eigentlich nicht card sharen d&uuml;rfen.
',1,37,6);
INSERT INTO `roles_de` VALUES (38,'Erpresser','Einmal pro Runde kannst du deine Karte zwei anderen Spielern private revealen. Tu musst ihnen sagen ''Ihr m&uuml;sst einen card share durchf&uuml;hren''. Die zwei Spieler m&uuml;ssen einen Card share miteinander machen (nicht mit dir). Du kannst deine Kraft nicht auf dich selber einsetzen. Notiz: Funktioniert auch bei charakteren die eigentlich nicht card sharen d&uuml;rfen.
',2,37,6);
INSERT INTO `roles_de` VALUES (39,'Eris','Einmal im Spiel kannst du zwei Spieler einander hassen lassen. Du musst ihnen deine Karte private revealen und ihnen sagen ''Ihr hasst einander.''. Ihre Siegesbidungung wird durch folgende ersetzt: Du gewinnst falls du am Ende des Spiels NICHT im selben Raum bist wie derjenige den du hasst. Notiz: Falls einer der hassenden einen andere Spieler liebe w&uuml;rde (durch Armor), w&uuml;rden sich die beiden Effekte aufheben.
',1,39,6);
INSERT INTO `roles_de` VALUES (40,'Armor','Einmal im Spiel kannst du zwei Spieler verlieben. Du musst ihnen deine Karte private revealen und ihnen sagen ''Ihr liebt einander.''. Ihre Siegesbidungung wird durch folgende ersetzt: Du gewinnst falls du am Ende des Spiels im selben Raum bist wie derjenige den du liebst. Notiz: Falls einer der Verliebten einen andere Spieler hassen w&uuml;rde (durch Eris), w&uuml;rden sich die beiden Effekte aufheben.
',2,39,6);
INSERT INTO `roles_de` VALUES (41,'Zocker','Am Ende des Spiels musst du verk&uuml;nden welches Team (Rot oder blau oder keins von beiden) du glaubst das gewonnen hat. Du gewinnst falls du richtig liegst. Notiz: Du hast eine ''pause game number'' von 10. Bevor du ansagst m&uuml;ssen alle charaktere mit einer geringeren Nummer ihre Ansagen gemacht haben.
',3,41,2);
INSERT INTO `roles_de` VALUES (42,'Schwarzer Peter','Jeder Spieler der mit dir card oder color shared tauscht seine Karte gegen die deine. Der schwarze Peter verliert am Ende des spiels. Notiz: Der Kartentausch reinigt beide Karten komplett.
',3,42,9);
INSERT INTO `roles_de` VALUES (43,'Immunologist','Du bist immun gegen s&auml;mtliche Kr&auml;fte und Effekte ohne Ausnahme. Notiz: Spieler sollten niemals behaupten immun zu sein wenn sie es nicht sind.
',1,43,3);
INSERT INTO `roles_de` VALUES (44,'Immunologist','Du bist immun gegen s&auml;mtliche Kr&auml;fte und Effekte ohne Ausnahme. Notiz: Spieler sollten niemals behaupten immun zu sein wenn sie es nicht sind.
',2,43,3);
INSERT INTO `roles_de` VALUES (45,'Praktikant','Du gewinnst falls du am Ende im selben Raum bist wie der Pr&auml;sident.
',3,45,7);
INSERT INTO `roles_de` VALUES (46,'Opfer','Du gewinnst falls du am Ende des Spiels im selben Raum bist wie der Bomber.
',3,45,7);
INSERT INTO `roles_de` VALUES (47,'Julia','Du gewinnst falls du am Ende im selben Raum bist wie Romeo und der Bomber.
',3,47,7);
INSERT INTO `roles_de` VALUES (48,'Romeo','Du gewinnst falls du am Ende des Spiels im selben Raum bist wie Julia und der Bomber.
',3,47,7);
INSERT INTO `roles_de` VALUES (49,'Kobold','Du bist leichtsinnig. Leichtsinnige k&ouml;nnen keinen card oder color share ablehnen. Falls du mit einem Spieler card oder color sharest tauscht ihr eure Karten. Der Kobold gewinnt am Ende des Spiels. Jeder spieler kann nur einmal im Spiel Kobold sein. Falls ein Spieler die Kobold Karte zum zweiten Mal erhalten w&uuml;rde muss er klar machen das er dies nicht kann. Notiz: Getauschte Karten werden komplett gereinigt. Notiz: Sollte ein Leichtsinniger sch&uuml;chtern oder scheu werden w&uuml;rden sich die Bedingungen aufheben.
',4,49,9);
INSERT INTO `roles_de` VALUES (50,'Mastermind','Du gewinnst falls du am Ende des Spiels der Anf&uuml;hrer des Raumes bist und w&auml;hrend des Spiels mindestens einmal Anf&uuml;hrer des anderen Raumes warst.
',3,50,5);
INSERT INTO `roles_de` VALUES (51,'B&uuml;rgermeister','Falls dein Raum eine gerade anzahl an Spielern hat, kannst du, w&auml;hrend einer Wahl zum Anf&uuml;hrer des Raumes, deine Karte public revealen und deine Stimme zum st&uuml;rzen des Anf&uuml;hrers z&auml;hlt doppelt, au&szlig;er der andere B&uuml;rgermeister zeigt seine Karte ebenfalls vor.
',1,51,10);
INSERT INTO `roles_de` VALUES (52,'B&uuml;rgermeister','Falls dein Raum eine gerade anzahl an Spielern hat, kannst du, w&auml;hrend einer Wahl zum Anf&uuml;hrer des Raumes, deine Karte public revealen und deine Stimme zum st&uuml;rzen des Anf&uuml;hrers z&auml;hlt doppelt, au&szlig;er der andere B&uuml;rgermeister zeigt seine Karte ebenfalls vor.
',2,51,10);
INSERT INTO `roles_de` VALUES (53,'Mediziner','Wenn du mit einem Spieler card sharest werden s&auml;mtliche Effekte von ihm entfernt (um zu sehen ob etwas ein Effekt ist ist die englische Version empfehlenswert). Dies macht dich nicht immun gegen Effekte. Diese k&ouml;nnen aber vom anderen Mediziner von dir entfernt werden.
',1,53,11);
INSERT INTO `roles_de` VALUES (54,'Mediziner','Wenn du mit einem Spieler card sharest werden s&auml;mtliche Effekte von ihm entfernt (um zu sehen ob etwas ein Effekt ist ist die englische Version empfehlenswert). Dies macht dich nicht immun gegen Effekte. Diese k&ouml;nnen aber vom anderen Mediziner von dir entfernt werden.
',2,53,11);
INSERT INTO `roles_de` VALUES (55,'MI6','Du gewinnst falls du vor Ende des Spiels mit Bomber und Pr&auml;sidenten card sharest.
',3,55,7);
INSERT INTO `roles_de` VALUES (56,'Phantomime','Du musst versuchen keine Ger&auml;usche von dir geben.
',1,56,3);
INSERT INTO `roles_de` VALUES (57,'Phantomime','Du musst versuchen keine Ger&auml;usche von dir geben.
',2,56,3);
INSERT INTO `roles_de` VALUES (58,'G&uuml;nstling','Du gewinnst wenn du niemals in einem Raum warst wenn in diesem ein Anf&uuml;hrer gest&uuml;rzt wurde.
',3,58,2);
INSERT INTO `roles_de` VALUES (59,'Geliebte','Du gewinnst falls du am Ende des Spiels im selben Raum bist wie der Pr&auml;sident aber die Ehefrau im anderen Raum ist.
',3,59,7);
INSERT INTO `roles_de` VALUES (60,'Ehefrau','Du gewinnst falls du am Ende des Spiels im selben Raum bist wie der Pr&auml;sident und die Geliebte im anderen Raum ist.
',3,59,7);
INSERT INTO `roles_de` VALUES (61,'Mumie','Jeder der mit dir card shared wird verflucht. Verfluchte Spieler m&uuml;ssen ihr bestes geben keine Ger&auml;usche zu machen. Notiz: Das bedeutet auch sie k&ouml;nnen keine Kr&auml;fte mehr einsetzen die reden ben&ouml;tigen.
',1,61,11);
INSERT INTO `roles_de` VALUES (62,'Mumie','Jeder der mit dir card shared wird verflucht. Verfluchte Spieler m&uuml;ssen ihr bestes geben keine Ger&auml;usche zu machen. Notiz: Das bedeutet auch sie k&ouml;nnen keine Kr&auml;fte mehr einsetzen die reden ben&ouml;tigen.
',2,61,11);
INSERT INTO `roles_de` VALUES (63,'Vermittler','Du bist klug. Du darfst lediglich card shares machen. Keine private, public reveales oder color shares. Notiz: Falls du einen Effekt erh&auml;ltst der dich am sharen hindert kannst du gar nicht mehr mit deiner Karte machen.
',1,63,8);
INSERT INTO `roles_de` VALUES (64,'Vermittler','Du bist klug. Du darfst lediglich card shares machen. Keine private, public reveales oder color shares. Notiz: Falls du einen Effekt erh&auml;ltst der dich am sharen hindert kannst du gar nicht mehr mit deiner Karte machen.
',2,63,8);
INSERT INTO `roles_de` VALUES (65,'Nuklearer Tyrant','Am Ende des Spiels wirst du gefragt ob du mit Pr&auml;sident oder dem Bomber card geshared hast. Du gewinnst falls du mit keinem von ihnen geshared hast. Falls du gewinnst verlieren alle anderen. Du bist leichtsinnig. Leichtsinnige Spieler m&uuml;ssen jeden card share oder color share annehmen. Notiz: Falls ein Leichtsinniger Spieler einen gegens&auml;tzlichen Effekt erhalten w&uuml;rden, w&uuml;rden sie sich aufheben.
',3,65,12);
INSERT INTO `roles_de` VALUES (66,'Krankenschwester','Du bist der backup Charakter f&uuml;r den Arzt. Falls der Arzt geburied ist musst du seine Aufgaben &uuml;bernehmen (z.B. mit dem Pr&auml;sidenten sharen).
',1,66,13);
INSERT INTO `roles_de` VALUES (67,'Bastler','Du bist der backup Charakter f&uuml;r den Ingenieur. Falls der Ingenieur geburied ist &uuml;bernimmst du seine Aufgaben (z.B. das card sharing mit dem Bomber)
',2,66,13);
INSERT INTO `roles_de` VALUES (68,'Paparazzo','Versuche private Gespr&auml;che zu verhindern. Sei so nervig und laut wie m&ouml;glich ohne andere tats&auml;chlich physisch zu manipulieren. Falls man mit der ''privacy promise'' Regel spielt darfst du diese regel ignorieren solange du deine Karte &ouml;ffentlich vorzeigst um zu beweisen das du die Regel ignorieren darfst.
',1,68,10);
INSERT INTO `roles_de` VALUES (69,'Paparazzo','Versuche private Gespr&auml;che zu verhindern. Sei so nervig und laut wie m&ouml;glich ohne andere tats&auml;chlich physisch zu manipulieren. Falls man mit der ''privacy promise'' Regel spielt darfst du diese regel ignorieren solange du deine Karte &ouml;ffentlich vorzeigst um zu beweisen das du die Regel ignorieren darfst.
',2,68,10);
INSERT INTO `roles_de` VALUES (70,'Paranoid','Du bist paranoid. Du darfst nur card sharen und dar&uuml;ber hinaus nur einmal im ganzen Spiel. Notiz: Falls ein paranoider Spieler zum card share gezwungen wird z&auml;hlt dies nicht als dieser einzige card share. Notiz: Dies ist ein psychologischer Effekt. Notiz: Sollte ein paranoider spieler leichtsinnig werden heben sich die Bedingungen auf.
',1,70,4);
INSERT INTO `roles_de` VALUES (71,'Paranoid','Du bist paranoid. Du darfst nur card sharen und dar&uuml;ber hinaus nur einmal im ganzen Spiel. Notiz: Falls ein paranoider Spieler zum card share gezwungen wird z&auml;hlt dies nicht als dieser einzige card share. Notiz: Dies ist ein psychologischer Effekt. Notiz: Sollte ein paranoider spieler leichtsinnig werden heben sich die Bedingungen auf.
',2,70,4);
INSERT INTO `roles_de` VALUES (72,'Tochter des Pr&auml;sidenten','Du bist der backup Charakter f&uuml;r den Pr&auml;sidenten. Falls der Pr&auml;sident geburied ist musst du alle seine Aufgaben &uuml;bernehmen.
',1,72,13);
INSERT INTO `roles_de` VALUES (73,'M&auml;rtyrer','Du bist die Ersatzkarte f&uuml;r den Bomber. Falls der Bomber geburied ist musst du alle seine Pflichten &uuml;bernehmen (z.B. im selben Raum sein wie der Pr&auml;sident und mit dem Ingineur card sharen).
',2,72,13);
INSERT INTO `roles_de` VALUES (74,'Privater Ermittler','Am ende der letzten Runde, bevor die Spieler ihre Karten vorzeigen musst du &ouml;ffentlich die vergrabene Karte bennenen. Du gewinnst nur falls du korrekt liegst. Notiz: Deine pause Nummer ist 5. Mach deine Ank&uuml;ndigung erst wenn alle Spieler mit einer geringeren Pause Nummer bereits dran waren.
',3,74,13);
INSERT INTO `roles_de` VALUES (75,'Psychologe','Falls du mit einem Spieler mit einem psychischen Effekt card sharest wird dieser von dem psychischen Effekt gereinigt. Falls sein psychischer Effekt ihn am card share hindert kannst du ihm deine Karte private revealen und er darf dann mit dir card sharen wenn er m&ouml;chte.
',1,75,6);
INSERT INTO `roles_de` VALUES (76,'Psychologe','Falls du mit einem Spieler mit einem psychischen Effekt card sharest wird dieser von dem psychischen Effekt gereinigt. Falls sein psychischer Effekt ihn am card share hindert kannst du ihm deine Karte private revealen und er darf dann mit dir card sharen wenn er m&ouml;chte.
',2,75,6);
INSERT INTO `roles_de` VALUES (77,'Rivale','Du gewinnst falls du am Ende nicht im selben Raum bist wie der Pr&auml;sident.
',3,77,7);
INSERT INTO `roles_de` VALUES (78,'&Uuml;berlebender','Du gewinnst falls du am Ende nicht im selben Raum bist wie der Bomber.',3,77,7);
INSERT INTO `roles_de` VALUES (79,'Roboter','Du gewinnst falls der erste Spieler mit dem du card sharest verliert. Falls du es nicht schaffst mit einem Spieler zu sharen bevor das Spiel zuende ist, hast du verloren. Notiz: Falls dein erster card share mit dem Klon ist und der erste card share vom Klon mit dir ist, verliert ihr beide.
',3,79,5);
INSERT INTO `roles_de` VALUES (80,'Security','Einmal pro Spiel kannst du deine Karte &ouml;ffentlich vorzeigen und einen Spieler bestimmen der diese Runde keine Geisel sein darf. Deine Karte bleibt dannach &ouml;ffentlich sichtbar.
',1,80,10);
INSERT INTO `roles_de` VALUES (81,'Security','Einmal pro Spiel kannst du deine Karte &ouml;ffentlich vorzeigen und einen Spieler bestimmen der diese Runde keine Geisel sein darf. Deine Karte bleibt dannach &ouml;ffentlich sichtbar.
',2,80,10);
INSERT INTO `roles_de` VALUES (82,'Sch&uuml;chterner','Du bist sch&uuml;chtern. Sch&uuml;chterne Spieler d&uuml;rfen keine Teile ihrer Karte vorzeigen. Notiz: Dies ist ein psychischer Effekt. Notiz: Solltest ein sch&uuml;chtener Spieler leichtsinnig werden heben sich die Bedingungen auf.
',1,82,3);
INSERT INTO `roles_de` VALUES (83,'Sch&uuml;chtener','Du bist sch&uuml;chtern. Sch&uuml;chterne Spieler d&uuml;rfen keine Teile ihrer Karte vorzeigen. Notiz: Dies ist ein psychischer Effekt. Notiz: Solltest ein sch&uuml;chtener Spieler leichtsinnig werden heben sich die Bedingungen auf.
',2,82,3);
INSERT INTO `roles_de` VALUES (84,'Scharfsch&uuml;tze','Am Ende des Spiels bevor die Spieler ihre Karten vorzeigen musst du einen Spieler bestimmen den du erschie&szlig;t. Der Spieler muss nicht in deinem Raum sein. Du gewinnst falls du das Ziel erschie&szlig;t. Notiz: Deine pause Nummer ist 15. Du bist erst dran wenn alle spieler mit einer geringern pause Nummer ihre Ank&uuml;ndigungen gemacht haben.
',3,84,2);
INSERT INTO `roles_de` VALUES (85,'Ziel','Du gewinnst falls dich der Sniper am Ende des Spiels nicht erschie&szlig;t.
',3,84,2);
INSERT INTO `roles_de` VALUES (86,'K&ouml;der','Du gewinnst falls dich der Scharfsch&uuml;tze am Ende der letzten Runde erschie&szlig;t.
',3,84,2);
INSERT INTO `roles_de` VALUES (87,'Blauer Spion','Dieser Spieler hat die Farbe des anderen Teams. Das hei&szlig;t der Blaue Spion hat eine rote Karte obwohl er f&uuml;r das blaue Team spielt. Dies ist bei einem color share wichtig. Achte darauf nicht zu vergessen in welchem team du wirklich bist.
',2,87,8);
INSERT INTO `roles_de` VALUES (88,'Roter Spion','Dieser Spieler hat die Farbe des anderen Teams. Das hei&szlig;t der rote Spion hat eine blaue Karte obwohl er f&uuml;r das rote Team spielt. Dies ist bei einem color share wichtig. Achte darauf nicht zu vergessen in welchem team du wirklich bist.
',1,87,8);
INSERT INTO `roles_de` VALUES (89,'Schl&auml;ger','Jeder Spieler mit dem du card sharest wird scheu. Scheue Spieler k&ouml;nnen nur color sharen selbst wenn die F&auml;higkeit eines anderen Charakters einen card share erzwingt. Notiz: Falls ein Spieler der scheu ist aus irgendeinem Grund leichtsinnig wird, heben sich die Bedingungen auf.
',1,89,8);
INSERT INTO `roles_de` VALUES (90,'Schl&auml;ger','Jeder Spieler mit dem du card sharest wird scheu. Scheue Spieler k&ouml;nnen nur color sharen selbst wenn die F&auml;higkeit eines anderen Charakters einen card share erzwingt. Notiz: Falls ein Spieler der scheu ist aus irgendeinem Grund leichtsinnig wird, heben sich die Bedingungen auf.
',2,89,8);
INSERT INTO `roles_de` VALUES (91,'Reisender','Du gewinnst falls du in mehr als die H&auml;lfte der Runden als Geisel den Raum wechselst.
',3,91,2);
INSERT INTO `roles_de` VALUES (92,'Tuesday Knight','Falls du mit dem Bomber card sharest stribt jeder in deinem Raum, au&szlig;er dem Pr&auml;sdienten. Au&szlig;erdem endet das Spiel sofort (normalerweise Sieg f&uuml;r das blaue Team. Notiz: Dies funktioniert nicht mit dem M&auml;rtyrer.
',1,92,12);
INSERT INTO `roles_de` VALUES (93,'Dr. Boom','Falls du mit dem Pr&auml;sidenten card sharest stirbt jeder in deinem Raum und das Spiel endet (normalerweise Sieg f&uuml;r team rot). Notiz: Dies funktioniert nicht mit der Tochter des Pr&auml;sidenten.
',2,92,12);
INSERT INTO `roles_de` VALUES (94,'Ursupator','W&auml;hrend jeder Runde au&szlig;er der letzten kannst du deine Karte &ouml;ffentlich vorzeigen und du wirst Anf&uuml;hrer. Deine Karte muss ab dann permanent &ouml;ffentlich vorgezeigt bleiben, das hei&szlig;t diese F&auml;higkeit kann nur einmal genutzt werden. Du kannst in dieser Runde nicht mehr abgew&auml;hlt werden, auch nicht von einem anderen Ursupator.
',1,94,10);
INSERT INTO `roles_de` VALUES (95,'Ursupator','W&auml;hrend jeder Runde au&szlig;er der letzten kannst du deine Karte &ouml;ffentlich vorzeigen und du wirst Anf&uuml;hrer. Deine Karte muss ab dann permanent &ouml;ffentlich vorgezeigt bleiben, das hei&szlig;t diese F&auml;higkeit kann nur einmal genutzt werden. Du kannst in dieser Runde nicht mehr abgew&auml;hlt werden, auch nicht von einem anderen Ursupator.
',2,94,10);
INSERT INTO `roles_de` VALUES (96,'Zombie','Du bist ein Zombie. Jeder Zombie gewinnt falls alle Spieler die am Ende des Spiels nicht tod sind in Team Zombie sind. Jeder Spieler der mit einem Zombie card oder color shared wird zum zombie. Notiz: Zombies die keine Zombie Karte haben m&uuml;ssen einen spieler mit dem sie sharen darauf aufmerksam machen das dieser jetzt auch ein Zombie ist.',4,96,5);
CREATE TABLE sets (
	`_id`	INTEGER AUTO_INCREMENT,
	`name`	TEXT NOT NULL,
	`count`	INTEGER,
	`parent`	INTEGER DEFAULT -1,
	`description`	TEXT,
	`from_server`	INTEGER DEFAULT 0,
	PRIMARY KEY(_id)
);
INSERT INTO `sets` VALUES (1,'Beispiel',4,-1,'Ein sehr simples Set das lediglich als Beispiel dient.',0);
CREATE TABLE set_roles (
	`id_set`	INTEGER NOT NULL,
	`id_role`	INTEGER NOT NULL,
	PRIMARY KEY(id_set,id_role),
	FOREIGN KEY(`id_set`) REFERENCES sets ( _id ) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(`id_role`) REFERENCES roles_de ( _id ) ON UPDATE CASCADE ON DELETE CASCADE
);
INSERT INTO `set_roles` VALUES (1,1);
INSERT INTO `set_roles` VALUES (1,2);
INSERT INTO `set_roles` VALUES (1,34);
INSERT INTO `set_roles` VALUES (1,35);
CREATE TABLE set_nogo_roles (
	`id_set`	INTEGER NOT NULL,
	`id_role`	INTEGER NOT NULL,
	PRIMARY KEY(id_set,id_role),
	FOREIGN KEY(`id_set`) REFERENCES sets ( _id ) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(`id_role`) REFERENCES roles_de ( _id ) ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE `votes` (
	`id_set`	INTEGER NOT NULL ,
	`vote_up`	INTEGER NOT NULL,
	PRIMARY KEY(`id_set`),
	FOREIGN KEY(`id_set`) REFERENCES sets ( _id ) ON UPDATE CASCADE ON DELETE CASCADE
);
ALTER TABLE sets DROP COLUMN from_server;
ALTER TABLE sets ADD last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE categories_en ADD last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE categories_de ADD last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE teams_de ADD last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE teams_en ADD last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE roles_de ADD last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE roles_en ADD last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE set_roles ADD last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
COMMIT;
