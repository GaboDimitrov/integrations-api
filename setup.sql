
CREATE TABLE IF NOT EXISTS app_connector (
    id INT NOT NULL AUTO_INCREMENT,
    name varchar(100) NOT NULL,
    description varchar(500) NOT NULL,
    is_active boolean default 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY ( id )
);

INSERT INTO app_connector(name, description, is_active) VALUES
('Shopify', 'One of the world\'s best', 1),
('Wordpress', 'Lorem ipsum', 1),
('Wix', 'I have seen too much ads on this one', 1),
('SFCC', 'Not so good solution', 1),
('Something else', 'Catch me by suprise', 0);