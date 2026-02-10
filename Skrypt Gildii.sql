-- Tabele główne
CREATE TABLE Player_Class (
class_id INT AUTO_INCREMENT PRIMARY KEY,
class_name VARCHAR(100) NOT NULL
);
CREATE TABLE Guild (
guild_id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100) NOT NULL,
creation_date DATE NOT NULL
);
CREATE TABLE Permissions (
rank_id INT AUTO_INCREMENT PRIMARY KEY,
rank_name VARCHAR(100) NOT NULL,
permission_tier INT NOT NULL
);
CREATE TABLE Guild_Rank (
guild_id INT NOT NULL,
rank_id INT NOT NULL,
PRIMARY KEY (guild_id, rank_id),
FOREIGN KEY (guild_id) REFERENCES Guild(guild_id)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (rank_id) REFERENCES
Permissions(rank_id)
ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE Player (
player_id INT AUTO_INCREMENT PRIMARY KEY,
username VARCHAR(50) NOT NULL,
join_date DATE NOT NULL,

guild_id INT NOT NULL,
class_id INT NOT NULL,
rank_id INT NOT NULL,
FOREIGN KEY (guild_id) REFERENCES Guild(guild_id)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (class_id) REFERENCES
Player_Class(class_id)
ON DELETE RESTRICT ON UPDATE CASCADE,
FOREIGN KEY (rank_id) REFERENCES
Permissions(rank_id)
ON DELETE RESTRICT ON UPDATE CASCADE
);
-- Umiejętności i osiągnięcia
CREATE TABLE Ability (
ability_id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100) NOT NULL,
class_id INT NOT NULL,
FOREIGN KEY (class_id) REFERENCES
Player_Class(class_id)
ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE Achievement (
achievement_id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100) NOT NULL,
date_achieved DATE NOT NULL,
player_id INT NOT NULL,
FOREIGN KEY (player_id) REFERENCES
Player(player_id)
ON DELETE CASCADE ON UPDATE CASCADE
);
-- Gildia i związane z nią zasoby
CREATE TABLE Guild_Fund (

fund_id INT AUTO_INCREMENT PRIMARY KEY,
gold_amount INT NOT NULL DEFAULT 0,
guild_id INT NOT NULL UNIQUE,
FOREIGN KEY (guild_id) REFERENCES Guild(guild_id)
ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE Item (
item_id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100) NOT NULL,
type VARCHAR(100) NOT NULL,
rarity VARCHAR(100) NOT NULL
);
CREATE TABLE Guild_Inventory (
inventory_id INT AUTO_INCREMENT PRIMARY KEY,
quantity INT NOT NULL DEFAULT 1,
guild_id INT NOT NULL,
item_id INT NOT NULL,
FOREIGN KEY (guild_id) REFERENCES Guild(guild_id)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (item_id) REFERENCES Item(item_id)
ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE Guild_Event (
event_id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100) NOT NULL,
event_date DATE NOT NULL,
guild_id INT NOT NULL,
FOREIGN KEY (guild_id) REFERENCES Guild(guild_id)
ON DELETE CASCADE ON UPDATE CASCADE
);
-- Ekwipunek i statystyki gracza
CREATE TABLE Player_Inventory (

inventory_id INT PRIMARY KEY,
quantity INT NOT NULL DEFAULT 1,
player_id INT NOT NULL,
item_id INT NOT NULL,
FOREIGN KEY (player_id) REFERENCES
Player(player_id)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (item_id) REFERENCES Item(item_id)
ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE Lvl_Player (
id INT AUTO_INCREMENT PRIMARY KEY,
level INT NOT NULL,
player_id INT NOT NULL UNIQUE,
FOREIGN KEY (player_id) REFERENCES
Player(player_id)
ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Player_Stats (
stats_id INT AUTO_INCREMENT PRIMARY KEY,
stat_name VARCHAR(100) NOT NULL,
points INT NOT NULL,
lvl_id INT NOT NULL,
FOREIGN KEY (lvl_id) REFERENCES Lvl_Player(id)
ON DELETE CASCADE ON UPDATE CASCADE
);
-- Transakcje
CREATE TABLE donation (
donation_id INT AUTO_INCREMENT PRIMARY KEY,
player_id INT NOT NULL,
gold_amount INT NOT NULL,
date_col DATE NOT NULL,

fund_id INT NOT NULL,
FOREIGN KEY (player_id) REFERENCES
Player(player_id)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (fund_id) REFERENCES
Guild_Fund(fund_id)
ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE Withdraw (
withdraw_id INT AUTO_INCREMENT PRIMARY KEY,
player_id INT NOT NULL,
gold_amount INT NOT NULL,
date_col DATE NOT NULL,
fund_id INT NOT NULL,
FOREIGN KEY (player_id) REFERENCES
Player(player_id)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (fund_id) REFERENCES
Guild_Fund(fund_id)
ON DELETE CASCADE ON UPDATE CASCADE
);
-- Logi aktywności
CREATE TABLE Logs (
log_id INT AUTO_INCREMENT PRIMARY KEY,
action_type VARCHAR(100) NOT NULL,
log_date DATETIME NOT NULL DEFAULT
CURRENT_TIMESTAMP,
guild_id INT NOT NULL,
player_id INT NOT NULL,
FOREIGN KEY (guild_id) REFERENCES Guild(guild_id)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (player_id) REFERENCES
Player(player_id)
ON DELETE CASCADE ON UPDATE CASCADE

);

-- Wstawianie klas graczy
INSERT INTO Player_Class (class_name) VALUES
('Mage'), ('Warrior'), ('Ranger'), ('Cleric'),
('Bard'), ('Barbarian'), ('Monk'), ('Hunter');
-- Wstawianie 5 gildii
INSERT INTO Guild (name, creation_date) VALUES
('Dragon Slayers', '2020-01-15'),
('Shadow Hunters', '2020-03-22'),
('Mystic Order', '2020-05-10'),
('Iron Legion', '2020-07-18'),
('Celestial Guardians', '2020-09-30');
-- Wstawianie rang (Permissions) - 4 to najwyższa
#permisja (Leader)
INSERT INTO Permissions (rank_name, permission_tier)
VALUES
('Leader', 4), -- 1 (najwyższa)
('Co-Leader', 3), -- 2
('Helper', 2), -- 3
('Member', 1); -- 4 (najniższa)
-- Łączenie gildii z rangami (każda gildia ma wszystkie 4 rangi)
INSERT INTO Guild_Rank (guild_id, rank_id) VALUES
(1,1),(1,2),(1,3),(1,4),
(2,1),(2,2),(2,3),(2,4),
(3,1),(3,2),(3,3),(3,4),
(4,1),(4,2),(4,3),(4,4),
(5,1),(5,2),(5,3),(5,4);

-- Gildia 1: 25 graczy

INSERT INTO Player (username, join_date, guild_id,
class_id, rank_id) VALUES
('Aetherius', '2020-02-10', 1, 1, 1), -- Leader
('Brynjar', '2020-02-15', 1, 2, 2), -- Co-Leader
('Caldor', '2020-03-01', 1, 3, 3), -- Helper
('Dagmar', '2020-03-05', 1, 4, 3), -- Helper
('Eirik', '2020-03-10', 1, 5, 4), ('Fenrir', '2020-03-
15', 1, 6, 4),
('Gunnar', '2020-04-01', 1, 7, 4), ('Haldor', '2020-04-
05', 1, 8, 4),
('Ivar', '2020-04-10', 1, 1, 4), ('Jorund', '2020-05-
01', 1, 2, 4),
('Kael', '2020-05-05', 1, 3, 4), ('Loki', '2020-05-10',
1, 4, 4),
('Magni', '2020-06-01', 1, 5, 4), ('Njal', '2020-06-
05', 1, 6, 4),
('Odin', '2020-06-10', 1, 7, 4), ('Prym', '2020-07-01',
1, 8, 4),
('Ragnar', '2020-07-05', 1, 1, 4), ('Sigurd', '2020-07-
10', 1, 2, 4),
('Thor', '2020-08-01', 1, 3, 4), ('Ulf', '2020-08-05',
1, 4, 4),
('Vidar', '2020-08-10', 1, 5, 4), ('Wulf', '2020-09-
01', 1, 6, 4),
('Xerxes', '2020-09-05', 1, 7, 4), ('Ymir', '2020-09-
10', 1, 8, 4);
-- Gildia 2: 30 graczy
INSERT INTO Player (username, join_date, guild_id,
class_id, rank_id) VALUES
('Zephyr', '2020-02-20', 2, 1, 1), -- Leader
('Aria', '2020-02-25', 2, 2, 2), -- Co-Leader
('Breeze', '2020-03-02', 2, 3, 3), -- Helper
('Coral', '2020-03-06', 2, 4, 3), -- Helper
('Dune', '2020-03-11', 2, 5, 4), ('Ember', '2020-03-
16', 2, 6, 4),

('Flare', '2020-04-02', 2, 7, 4), ('Gale', '2020-04-
06', 2, 8, 4),
('Haze', '2020-04-11', 2, 1, 4), ('Ice', '2020-05-02',
2, 2, 4),
('Jade', '2020-05-06', 2, 3, 4), ('Kite', '2020-05-11',
2, 4, 4),
('Luna', '2020-06-02', 2, 5, 4), ('Mist', '2020-06-06',
2, 6, 4),
('Nova', '2020-06-11', 2, 7, 4), ('Orb', '2020-07-02',
2, 8, 4),
('Pebble', '2020-07-06', 2, 1, 4), ('Quill', '2020-07-
11', 2, 2, 4),
('Rime', '2020-08-02', 2, 3, 4), ('Shade', '2020-08-
06', 2, 4, 4),
('Tide', '2020-08-11', 2, 5, 4), ('Umbra', '2020-09-
02', 2, 6, 4),
('Vapor', '2020-09-06', 2, 7, 4), ('Whirl', '2020-09-
11', 2, 8, 4),
('Xylo', '2020-10-01', 2, 1, 4), ('Yarn', '2020-10-05',
2, 2, 4),
('Zinc', '2020-10-10', 2, 3, 4), ('Aero', '2020-11-01',
2, 4, 4);
-- Gildia 3: 15 graczy
INSERT INTO Player (username, join_date, guild_id,
class_id, rank_id) VALUES
('Mystic', '2020-03-01', 3, 1, 1), -- Leader
('Arcane', '2020-03-05', 3, 2, 2), -- Co-Leader
('Rune', '2020-03-10', 3, 3, 3), -- Helper
('Glyph', '2020-03-15', 3, 4, 3), -- Helper
('Spell', '2020-04-01', 3, 5, 4), ('Charm', '2020-04-
05', 3, 6, 4),
('Sigil', '2020-04-10', 3, 7, 4), ('Tome', '2020-05-
01', 3, 8, 4),
('Wand', '2020-05-05', 3, 1, 4), ('Orb', '2020-05-10',
3, 2, 4),

('Scroll', '2020-06-01', 3, 3, 4), ('Potion', '2020-06-
05', 3, 4, 4),
('Crystal', '2020-06-10', 3, 5, 4), ('Amulet', '2020-
07-01', 3, 6, 4);
-- Gildia 4: 20 graczy
INSERT INTO Player (username, join_date, guild_id,
class_id, rank_id) VALUES
('Ironclad', '2020-04-01', 4, 1, 1), -- Leader
('Steel', '2020-04-05', 4, 2, 2), -- Co-Leader
('Forge', '2020-04-10', 4, 3, 3), -- Helper
('Anvil', '2020-05-01', 4, 4, 3), -- Helper
('Hammer', '2020-05-05', 4, 5, 4), ('Nail', '2020-05-
10', 4, 6, 4),
('Rivet', '2020-06-01', 4, 7, 4), ('Plate', '2020-06-
05', 4, 8, 4),
('Gear', '2020-06-10', 4, 1, 4), ('Bolt', '2020-07-01',
4, 2, 4),
('Chain', '2020-07-05', 4, 3, 4), ('Hinge', '2020-07-
10', 4, 4, 4),
('Ingot', '2020-08-01', 4, 5, 4), ('Metal', '2020-08-
05', 4, 6, 4),
('Ore', '2020-08-10', 4, 7, 4), ('Piston', '2020-09-
01', 4, 8, 4),
('Rust', '2020-09-05', 4, 1, 4), ('Spike', '2020-09-
10', 4, 2, 4),
('Wire', '2020-10-01', 4, 3, 4), ('Zinc', '2020-10-05',
4, 4, 4);
-- Gildia 5: 10 graczy
INSERT INTO Player (username, join_date, guild_id,
class_id, rank_id) VALUES
('Celeste', '2020-05-01', 5, 1, 1), -- Leader
('Stella', '2020-05-05', 5, 2, 2), -- Co-Leader
('Luna', '2020-05-10', 5, 3, 3), -- Helper
('Sol', '2020-06-01', 5, 4, 3), -- Helper

('Nova', '2020-06-05', 5, 5, 4), ('Comet', '2020-06-
10', 5, 6, 4),
('Orbit', '2020-07-01', 5, 7, 4), ('Pulsar', '2020-07-
05', 5, 8, 4),
('Quasar', '2020-07-10', 5, 1, 4), ('Zenith', '2020-08-
01', 5, 2, 4);
-- Wstawianie poziomów graczy (Lvl_Player)
INSERT INTO Lvl_Player (player_id, level)
SELECT player_id, FLOOR(1 + RAND() * 100) -- Poziom 1-
FROM Player;
-- Wstawianie statystyk graczy (Player_Stats)
-- Każdy gracz ma 6 podstawowych statystyk powiązanych z jego poziomem
INSERT INTO Player_Stats (stat_name, points, lvl_id)
SELECT 'Strength', FLOOR(l.level * (0.8 + RAND() *
0.4)), l.id -- Strength zależne od poziomu
FROM Lvl_Player l
JOIN Player p ON l.player_id = p.player_id
JOIN Player_Class pc ON p.class_id = pc.class_id
WHERE pc.class_name IN ('Warrior', 'Barbarian');
INSERT INTO Player_Stats (stat_name, points, lvl_id)
SELECT 'Strength', FLOOR(l.level * (0.6 + RAND() *
0.3)), l.id
FROM Lvl_Player l
JOIN Player p ON l.player_id = p.player_id
JOIN Player_Class pc ON p.class_id = pc.class_id
WHERE pc.class_name NOT IN ('Warrior', 'Barbarian');
INSERT INTO Player_Stats (stat_name, points, lvl_id)
SELECT 'Dexterity', FLOOR(l.level * (0.7 + RAND() *
0.6)), l.id
FROM Lvl_Player l
JOIN Player p ON l.player_id = p.player_id

JOIN Player_Class pc ON p.class_id = pc.class_id
WHERE pc.class_name IN ('Ranger', 'Hunter', 'Monk');
INSERT INTO Player_Stats (stat_name, points, lvl_id)
SELECT 'Dexterity', FLOOR(l.level * (0.5 + RAND() *
0.4)), l.id
FROM Lvl_Player l
JOIN Player p ON l.player_id = p.player_id
JOIN Player_Class pc ON p.class_id = pc.class_id
WHERE pc.class_name NOT IN ('Ranger', 'Hunter',
'Monk');
INSERT INTO Player_Stats (stat_name, points, lvl_id)
SELECT 'Intelligence', FLOOR(l.level * (0.8 + RAND() *
0.5)), l.id
FROM Lvl_Player l
JOIN Player p ON l.player_id = p.player_id
JOIN Player_Class pc ON p.class_id = pc.class_id
WHERE pc.class_name IN ('Mage', 'Cleric', 'Bard');
INSERT INTO Player_Stats (stat_name, points, lvl_id)
SELECT 'Intelligence', FLOOR(l.level * (0.4 + RAND() *
0.4)), l.id
FROM Lvl_Player l
JOIN Player p ON l.player_id = p.player_id
JOIN Player_Class pc ON p.class_id = pc.class_id
WHERE pc.class_name NOT IN ('Mage', 'Cleric', 'Bard');
INSERT INTO Player_Stats (stat_name, points, lvl_id)
SELECT 'Constitution', FLOOR(l.level * (0.9 + RAND() *
0.3)), l.id
FROM Lvl_Player l
JOIN Player p ON l.player_id = p.player_id
JOIN Player_Class pc ON p.class_id = pc.class_id
WHERE pc.class_name IN ('Warrior', 'Barbarian',
'Monk');

INSERT INTO Player_Stats (stat_name, points, lvl_id)
SELECT 'Constitution', FLOOR(l.level * (0.7 + RAND() *
0.3)), l.id
FROM Lvl_Player l
JOIN Player p ON l.player_id = p.player_id
JOIN Player_Class pc ON p.class_id = pc.class_id
WHERE pc.class_name NOT IN ('Warrior', 'Barbarian',
'Monk');
INSERT INTO Player_Stats (stat_name, points, lvl_id)
SELECT 'Charisma', FLOOR(l.level * (0.8 + RAND() *
0.5)), l.id
FROM Lvl_Player l
JOIN Player p ON l.player_id = p.player_id
JOIN Player_Class pc ON p.class_id = pc.class_id
WHERE pc.class_name IN ('Bard', 'Cleric');
INSERT INTO Player_Stats (stat_name, points, lvl_id)
SELECT 'Charisma', FLOOR(l.level * (0.5 + RAND() *
0.4)), l.id
FROM Lvl_Player l
JOIN Player p ON l.player_id = p.player_id
JOIN Player_Class pc ON p.class_id = pc.class_id
WHERE pc.class_name NOT IN ('Bard', 'Cleric');
INSERT INTO Player_Stats (stat_name, points, lvl_id)
SELECT 'Wisdom', FLOOR(l.level * (0.8 + RAND() * 0.4)),
l.id
FROM Lvl_Player l
JOIN Player p ON l.player_id = p.player_id
JOIN Player_Class pc ON p.class_id = pc.class_id
WHERE pc.class_name IN ('Cleric', 'Monk', 'Mage');
INSERT INTO Player_Stats (stat_name, points, lvl_id)
SELECT 'Wisdom', FLOOR(l.level * (0.5 + RAND() * 0.4)),
l.id
FROM Lvl_Player l

JOIN Player p ON l.player_id = p.player_id
JOIN Player_Class pc ON p.class_id = pc.class_id
WHERE pc.class_name NOT IN ('Cleric', 'Monk', 'Mage');
-- Wstawianie funduszy gildii
INSERT INTO Guild_Fund (guild_id, gold_amount) VALUES
(1, 10000), (2, 15000), (3, 8000), (4, 12000), (5,
5000);
-- Wstawianie przykładowych przedmiotów
INSERT INTO Item (name, type, rarity) VALUES
('Sword of Flames', 'Weapon', 'Epic'),
('Health Potion', 'Consumable', 'Common'),
('Plate Armor', 'Armor', 'Rare'),
('Amulet of Wisdom', 'Accessory', 'Legendary'),
('Leather Boots', 'Armor', 'Uncommon'),
('Mana Potion', 'Consumable', 'Common'),
('Bow of Precision', 'Weapon', 'Rare'),
('Ring of Power', 'Accessory', 'Epic');
-- Wstawianie ekwipunku gildii
INSERT INTO Guild_Inventory (guild_id, item_id,
quantity) VALUES
(1, 1, 2), (1, 3, 5), (1, 5, 10),
(2, 2, 20), (2, 4, 1), (2, 6, 15),
(3, 7, 3), (3, 2, 10), (3, 8, 2),
(4, 3, 8), (4, 5, 12), (4, 1, 1),
(5, 4, 1), (5, 6, 5), (5, 7, 2);
-- Wstawianie ekwipunku graczy (każdy gracz ma 1-3 losowe przedmioty)
INSERT INTO Player_Inventory (inventory_id, player_id,
item_id, quantity)
SELECT
ROW_NUMBER() OVER () + 1000, -- Unikalne ID
zaczynające od 1001
p.player_id,

FLOOR(1 + RAND() * 8), -- Losowy przedmiot 1-8
FLOOR(1 + RAND() * 3) -- Ilość 1-3
FROM Player p
CROSS JOIN (SELECT 1 AS n UNION SELECT 2 UNION SELECT
3) AS numbers
WHERE RAND() > 0.3; -- 70% szans na przedmiot
-- Wstawianie osiągnięć (każdy gracz ma 0-5 osiągnięć)
INSERT INTO Achievement (name, date_achieved,
player_id)
SELECT
CASE
WHEN a.n = 1 THEN 'First Kill'
WHEN a.n = 2 THEN 'Guild Hero'
WHEN a.n = 3 THEN 'Dungeon Master'
WHEN a.n = 4 THEN 'PvP Champion'
WHEN a.n = 5 THEN 'Legendary Crafter'
END,
DATE_ADD(p.join_date, INTERVAL FLOOR(1 + RAND() *
365) DAY),
p.player_id
FROM Player p
CROSS JOIN (SELECT 1 AS n UNION SELECT 2 UNION SELECT 3
UNION SELECT 4 UNION SELECT 5) AS a
WHERE RAND() > 0.5; -- 50% szans na osiągnięcie
-- Wstawianie umiejętności klas
INSERT INTO Ability (name, class_id) VALUES
-- Mage
('Fireball', 1), ('Ice Nova', 1), ('Arcane Intellect',
1),
-- Warrior
('Whirlwind', 2), ('Shield Bash', 2), ('Battle Shout',
2),
-- Ranger
('Aimed Shot', 3), ('Multi-Shot', 3), ('Trap Mastery',
3),

-- Cleric
('Heal', 4), ('Holy Light', 4), ('Divine Protection',
4),
-- Bard
('Inspire', 5), ('Song of Courage', 5), ('Lullaby', 5),
-- Barbarian
('Berserk', 6), ('Bloodlust', 6), ('Frenzy', 6),
-- Monk
('Flying Kick', 7), ('Meditation', 7), ('Inner Peace',
7),
-- Hunter
('Beast Mastery', 8), ('Tracking', 8), ('Survival
Instinct', 8);
-- Wstawianie wydarzeń gildii
INSERT INTO Guild_Event (name, event_date, guild_id)
VALUES
('Dragon Raid', '2023-01-15', 1),
('Monthly Meeting', '2023-01-20', 2),
('PvP Tournament', '2023-02-05', 3),
('New Member Initiation', '2023-02-10', 4),
('Treasure Hunt', '2023-02-15', 5),
('Boss Night', '2023-03-01', 1),
('Guild Anniversary', '2023-03-22', 2);
-- Wstawianie logów aktywności
INSERT INTO Logs (action_type, log_date, guild_id,
player_id)
SELECT
CASE
WHEN l.n = 1 THEN 'Login'
WHEN l.n = 2 THEN 'Donation'
WHEN l.n = 3 THEN 'Withdraw'
WHEN l.n = 4 THEN 'Item Crafted'
WHEN l.n = 5 THEN 'PvP Win'
END,
DATE_ADD(NOW(), INTERVAL -FLOOR(RAND() * 30) DAY),

p.guild_id,
p.player_id
FROM Player p
CROSS JOIN (SELECT 1 AS n UNION SELECT 2 UNION SELECT 3
UNION SELECT 4 UNION SELECT 5) AS l
WHERE RAND() > 0.7;

-- Widok klas i umiejętności
CREATE OR REPLACE VIEW Class_Abilities AS
SELECT
pc.class_name,
a.name AS ability_name
FROM Player_Class pc
JOIN Ability a ON pc.class_id = a.class_id
ORDER BY pc.class_name;
-- Widok statystyk i osiągnięć graczy
CREATE OR REPLACE VIEW Player_Achievements_Stats AS
SELECT
p.player_id,
p.username,
a.name AS achievement_name,
a.date_achieved,
lp.level,
ps.stat_name,
ps.points
FROM Player p
JOIN Achievement a ON p.player_id = a.player_id
JOIN Lvl_Player lp ON p.player_id = lp.player_id
JOIN Player_Stats ps ON lp.id = ps.lvl_id;
-- 20 dotacji
INSERT INTO Donation (player_id, gold_amount, date_col,
fund_id)
SELECT
p.player_id,

FLOOR(100 + RAND() * 4900) AS gold_amount, -- 100-5000 złota
DATE_ADD(
p.join_date,
INTERVAL FLOOR(RAND() * DATEDIFF(CURDATE(),
p.join_date)) DAY
) AS date_col,
gf.fund_id
FROM Player p
JOIN Guild_Fund gf ON p.guild_id = gf.guild_id
ORDER BY RAND()
LIMIT 20;
-- 20 wyplat
INSERT INTO Withdraw (player_id, gold_amount, date_col,
fund_id)
SELECT
p.player_id,
FLOOR(50 + RAND() * 2950) AS gold_amount, -- 50-3000 złota
DATE_ADD(
p.join_date,
INTERVAL FLOOR(RAND() * DATEDIFF(CURDATE(),
p.join_date)) DAY
) AS date_col,
gf.fund_id
FROM Player p
JOIN Guild_Fund gf ON p.guild_id = gf.guild_id
WHERE p.rank_id IN (1, 2) -- Tylko liderzy i współliderzy
ORDER BY RAND()
LIMIT 20;

-- Logi aktywnosci

INSERT INTO logs (action_type, log_date, guild_id,
player_id)
SELECT
actions.action_type,
DATE_ADD(
p.join_date,
INTERVAL FLOOR(RAND() * DATEDIFF(CURDATE(),
p.join_date)) DAY
) AS log_date,
p.guild_id,
p.player_id
FROM Player p
CROSS JOIN (
SELECT 'Quest Completed' AS action_type UNION ALL
SELECT 'PvP Win' UNION ALL
SELECT 'PvP Loss' UNION ALL
SELECT 'Dungeon Completed' UNION ALL
SELECT 'Boss Defeated' UNION ALL
SELECT 'Boss Failed' UNION ALL
SELECT 'Level Up' UNION ALL
SELECT 'Crafting Success' UNION ALL
SELECT 'Crafting Failure' UNION ALL
SELECT 'Exploration'
) AS actions
ORDER BY RAND()
LIMIT 40;