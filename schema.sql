CREATE DATABASE rssdata

CREATE TABLE rss_entries (
id INT AUTO_INCREMENT
PRIMARY KEY,
title VARCHAR(255),
link VARCHAR(255),
description TEXT,
published_date TIMESTAMP
);