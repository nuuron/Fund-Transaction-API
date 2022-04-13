import json
import sys
import psycopg2
from psycopg2.extensions import ISOLATION_LEVEL_AUTOCOMMIT

# get PostgreSQL username and password from command line arguments
db_username = sys.argv[1]
db_password = sys.argv[2]
arg_str = "user=" + db_username + " password='" + db_password + "'"

# optional args
if len(sys.argv) > 3:
    db_host = sys.argv[3]
    arg_str += " host=" + db_host
if len(sys.argv) > 4:
    db_port = sys.argv[4]
    arg_str += " port=" + db_port

# connect to PostgreSQL DBMS
con = psycopg2.connect(arg_str)
con.set_isolation_level(ISOLATION_LEVEL_AUTOCOMMIT)
cursor = con.cursor()

# create database neowise
query_sql = "CREATE DATABASE neowise;"
cursor.execute(query_sql)

# connect to neowise
con.close()
arg_str = "dbname=neowise " + arg_str
con = psycopg2.connect(arg_str)
con.set_isolation_level(ISOLATION_LEVEL_AUTOCOMMIT)
cursor = con.cursor()

# create users table
query_sql = "CREATE EXTENSION IF NOT EXISTS pgcrypto;"
cursor.execute(query_sql)

query_sql = "DROP SCHEMA IF EXISTS bank CASCADE;" \
            "CREATE SCHEMA IF NOT EXISTS bank;"
cursor.execute(query_sql)

query_sql = """ CREATE TABLE bank.users(
                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                    name TEXT,
                    balance NUMERIC(13, 2)
                );
            """
cursor.execute(query_sql)

# load users table
with open('users.json') as file:
    data = json.load(file)
    for user in data:
        query_sql = "INSERT INTO bank.users (id, name, balance) " \
                    "VALUES ('" + user['id'] + "', '" + user['name'] + "', " + str(user['balance']) + ");"
        cursor.execute(query_sql)

# create transactions table
query_sql = """ CREATE TABLE bank.transactions(
                    transaction_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                    details TEXT,
                    amount NUMERIC(13, 2),
                    sender_id UUID,
                    receiver_id UUID,
                    FOREIGN KEY (sender_id) REFERENCES bank.users (id),
                    FOREIGN KEY (receiver_id) REFERENCES bank.users (id)
                );
            """
cursor.execute(query_sql)

# close the connection
con.close()
