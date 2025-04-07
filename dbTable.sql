/*
Script to create multiple tables in the database.
- Unique tables: 
    state_lookup, district_lookup, mandal_lookup, village_lookup, village_demographics, 
    facilities_lookup, village_facilities, village_proposal, committee_lookup, category_lookup, 
    project_lookup, project_category, village_project, village_project_committee_members, roles, 
    user, user_roles, donar, village_project_donar, village_project_donar_transactions, 
    village_project_photos, expense_category_lookup, village_project_expenses
- Cascade delete and update have been added wherever there is a foreign key reference.
*/


-- state_lookup table
CREATE TABLE state_lookup (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    code VARCHAR(10) NOT NULL,        
    name VARCHAR(255) NOT NULL,       
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100)
);

-- district_lookup table
CREATE TABLE district_lookup (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    code VARCHAR(10) NOT NULL,         
    name VARCHAR(255) NOT NULL,        
    state_id INT NOT NULL,             -- foreign key to state_lookup(id)
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100),
    FOREIGN KEY (state_id) REFERENCES state_lookup(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- mandal_lookup table
CREATE TABLE mandal_lookup (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    code VARCHAR(10) NOT NULL,         
    name VARCHAR(255) NOT NULL,        
    district_id INT NOT NULL,          -- foreign key to district_lookup(id)
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100),
    FOREIGN KEY (district_id) REFERENCES district_lookup(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- village_lookup table
CREATE TABLE village_lookup (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    code VARCHAR(10) NOT NULL,         
    name VARCHAR(255) NOT NULL,        
    mandal_id INT NOT NULL,            -- foreign key to mandal_lookup(id)
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100),
    FOREIGN KEY (mandal_id) REFERENCES mandal_lookup(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- village_demographics table
CREATE TABLE village_demographics (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    village_id INT NOT NULL,           -- foreign key to village_lookup(id)
    no_of_houses INT,
    total_population INT,
    adult_male_population INT,
    adult_female_population INT,
    child_male_population INT,
    child_female_population INT,
    sc_male INT,
    sc_female INT,
    st_male INT,
    st_female INT,
    bc_male INT,
    bc_female INT,
    oc_male INT,
    oc_female INT,
    other_male INT,
    other_female INT,
    area FLOAT,
    latitude DECIMAL(8,6),
    longitude DECIMAL(9,6),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100),
    FOREIGN KEY (village_id) REFERENCES village_lookup(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- facilities_lookup table
CREATE TABLE facilities_lookup (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    name VARCHAR(255) NOT NULL,        -- Facility name
    comments TEXT,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100)
);

-- village_facilities table
CREATE TABLE village_facilities (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    village_id INT NOT NULL,           -- foreign key to village_lookup(id)
    facility_id INT NOT NULL,          -- foreign key to facilities_lookup(id)
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100),
    FOREIGN KEY (village_id) REFERENCES village_lookup(id)
        ON DELETE CASCADE,
    FOREIGN KEY (facility_id) REFERENCES facilities_lookup(id)
        ON DELETE CASCADE
);

-- village_proposal table
CREATE TABLE village_proposal (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    village_id INT NOT NULL,           -- foreign key to village_lookup(id)
    facility_id INT NOT NULL,          -- foreign key to facilities_lookup(id)
    status VARCHAR(50),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100),
    FOREIGN KEY (village_id) REFERENCES village_lookup(id)
        ON DELETE CASCADE,
    FOREIGN KEY (facility_id) REFERENCES facilities_lookup(id)
        ON DELETE CASCADE
);

-- committee_lookup table
CREATE TABLE committee_lookup (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(15),
    address TEXT,
    father_name VARCHAR(100),
    email VARCHAR(100),
    record_type VARCHAR(50),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100)
);

-- category_lookup table
CREATE TABLE category_lookup (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    name VARCHAR(100) NOT NULL,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100)
);

-- project_lookup table
CREATE TABLE project_lookup (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    description TEXT,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100)
);

-- project_category table
CREATE TABLE project_category (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    category_id INT NOT NULL,          -- foreign key to category_lookup(id)
    project_id INT NOT NULL,           -- foreign key to project_lookup(id)
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100),
    FOREIGN KEY (category_id) REFERENCES category_lookup(id)
        ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES project_lookup(id)
        ON DELETE CASCADE
);

-- village_project table
CREATE TABLE village_project (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    village_proposal_id INT NOT NULL,  -- foreign key to village_proposal(id)
    project_category_id INT NOT NULL,  -- foreign key to project_category(id)
    status VARCHAR(50),
    cost FLOAT,
    latitude DECIMAL(8,6),
    longitude DECIMAL(9,6),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100),
    FOREIGN KEY (village_proposal_id) REFERENCES village_proposal(id)
        ON DELETE CASCADE,
    FOREIGN KEY (project_category_id) REFERENCES project_category(id)
        ON DELETE CASCADE
);

-- village_project_committee_members table
CREATE TABLE village_project_committee_members (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    village_project_id INT NOT NULL,   -- foreign key to village_project(id)
    committee_id INT NOT NULL,         -- foreign key to committee_lookup(id)
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100),
    FOREIGN KEY (village_project_id) REFERENCES village_project(id)
        ON DELETE CASCADE,
    FOREIGN KEY (committee_id) REFERENCES committee_lookup(id)
        ON DELETE CASCADE
);

-- roles table
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    name VARCHAR(100) NOT NULL,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100)
);

-- user table
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(15),
    email VARCHAR(100),
    user_name VARCHAR(100),
    password VARCHAR(255),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100)
);

-- user_roles table
CREATE TABLE user_roles (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    user_id INT NOT NULL,              -- foreign key to user(id)
    role_id INT NOT NULL,              -- foreign key to roles(id)
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES user(id)
        ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id)
        ON DELETE CASCADE
);

-- donar table
CREATE TABLE donar (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(15),
    email VARCHAR(100),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100)
);

-- village_project_donar table
CREATE TABLE village_project_donar (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    village_project_id INT NOT NULL,   -- foreign key to village_project(id)
    donar_id INT NOT NULL,             -- foreign key to donar(id)
    amount_of_contribution FLOAT,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100),
    FOREIGN KEY (village_project_id) REFERENCES village_project(id)
        ON DELETE CASCADE,
    FOREIGN KEY (donar_id) REFERENCES donar(id)
        ON DELETE CASCADE
);

-- village_project_donar_transactions table
CREATE TABLE village_project_donar_transactions (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    village_project_donar_id INT NOT NULL, -- foreign key to village_project_donar(id)
    transaction_amount FLOAT,
    payment_mode VARCHAR(50),
    transaction_date DATETIME,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100),
    FOREIGN KEY (village_project_donar_id) REFERENCES village_project_donar(id)
        ON DELETE CASCADE
);

-- village_project_photos table
CREATE TABLE village_project_photos (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    village_project_id INT NOT NULL,   -- foreign key to village_project(id)
    picture BLOB,
    path VARCHAR(255),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100),
    FOREIGN KEY (village_project_id) REFERENCES village_project(id)
        ON DELETE CASCADE
);

-- expense_category_lookup table
CREATE TABLE expense_category_lookup (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    description TEXT,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100)
);

-- village_project_expenses table
CREATE TABLE village_project_expenses (
    id INT AUTO_INCREMENT PRIMARY KEY, -- primary key
    village_project_id INT NOT NULL,   -- foreign key to village_project(id)
    expense_category_id INT NOT NULL, -- foreign key to expense_category_lookup(id)
    transaction_amount FLOAT,
    payment_mode VARCHAR(50),
    transaction_date DATETIME,
    committee_id INT,                 -- foreign key to committee_lookup(id)
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_updated_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    last_updated_by VARCHAR(100),
    FOREIGN KEY (village_project_id) REFERENCES village_project(id)
        ON DELETE CASCADE,
    FOREIGN KEY (expense_category_id) REFERENCES expense_category_lookup(id)
        ON DELETE CASCADE,
    FOREIGN KEY (committee_id) REFERENCES committee_lookup(id)
        ON DELETE CASCADE
);

CREATE TABLE images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    data LONGBLOB NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE
);