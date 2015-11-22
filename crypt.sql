CREATE TABLE users (user_id SERIAL NOT NULL, login TEXT UNIQUE NOT NULL, password TEXT NOT NULL, is_super BOOLEAN NOT NULL);

CREATE OR REPLACE FUNCTION make_password(p TEXT) 
RETURNS TEXT AS $$

BEGIN
RETURN crypt(p, gen_salt('bf', 8));
END
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION user_insert_trigger()
RETURNS TRIGGER AS $$

BEGIN
NEW.password := make_password(NEW.password);
RETURN NEW;
END
$$ LANGUAGE plpgsql;