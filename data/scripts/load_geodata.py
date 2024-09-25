import sqlite3
import pandas as pd
import ipaddress
import time

def log(msg):
    print(f"{time.strftime('%Y-%M-%D %H:%M:%S')} - {msg}")

# create connection
connection=sqlite3.connect('../geodata.db')
cursor=connection.cursor()

# create tables
def createtable():
    cursor.execute('''CREATE TABLE IF NOT EXISTS geolocation(
                    geoname_id INTEGER PRIMARY KEY,
                    continent_code TEXT,
                    country_iso_code TEXT);''')
    cursor.execute('''CREATE TABLE IF NOT EXISTS ipv4(
                   id INTEGER PRIMARY KEY AUTOINCREMENT,
                   network TEXT NOT NULL,
                   ipstart INTEGER NOT NULL,
                   ipend INTEGER NOT NULL,
                   geoname_id INTEGER,
                   FOREIGN KEY (geoname_id) REFERENCES geolocation(geoname_id));''')
    cursor.execute('''CREATE TABLE IF NOT EXISTS ipv6(
                   id INTEGER PRIMARY KEY AUTOINCREMENT,
                   network TEXT NOT NULL,
                   ipstart BLOB NOT NULL,
                   ipend BLOB NOT NULL,
                   geoname_id INTEGER,
                   FOREIGN KEY (geoname_id) REFERENCES geolocation(geoname_id));''')
    cursor.execute('''CREATE INDEX IF NOT EXISTS idx_ipv4 ON ipv4(ipstart, ipend);''')
    cursor.execute('''CREATE INDEX IF NOT EXISTS idx_ipv6 ON ipv6(ipstart, ipend);''')
    cursor.execute('''CREATE INDEX IF NOT EXISTS idx_ipv4_geoname_id ON ipv4(geoname_id);''')
    cursor.execute('''CREATE INDEX IF NOT EXISTS idx_ipv6_geoname_id ON ipv6(geoname_id);''')

    
# handle cidr notation
def cidr2ipv4(cidr):
    network=ipaddress.IPv4Network(cidr,strict=False)
    ipstart=int(network.network_address)
    ipend=int(network.broadcast_address)
    # return start and end of ip range
    return ipstart,ipend

# ipv6 values stored as str because values too large to fit SQLite INTEGER
def cidr2ipv6(cidr):
    network=ipaddress.IPv6Network(cidr,strict=False)
    ipstart=network.network_address.packed
    ipend=network.broadcast_address.packed
    return ipstart,ipend

# load data
def load_data():
    geolocation=pd.read_csv('../country-codes.csv')
    geolocation.to_sql('geolocation', connection, if_exists='replace', index=False)

    ipv4=pd.read_csv('../ipv4.csv')
    ipv4['ipstart'],ipv4['ipend']=zip(*ipv4['network'].apply(cidr2ipv4))
    ipv4.to_sql('ipv4',connection,if_exists='replace',index=False)

    ipv6=pd.read_csv('../ipv6.csv')
    ipv6['ipstart'],ipv6['ipend']=zip(*ipv6['network'].apply(cidr2ipv6))
    ipv6.to_sql('ipv6', connection, if_exists='replace', index=False)


# main
log("creating table and loading data...")
createtable()
load_data()
log("finished")

connection.commit()
connection.close()

