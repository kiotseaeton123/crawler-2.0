import sqlite3
import pandas as pd
import time

def log(msg):
    print(f"{time.strftime('%Y-%M-%D %H:%M:%S')} - {msg}")
    
# file too large for memory, use sqlite to merge
connection=sqlite3.connect('geodata.db')

ipv6=pd.read_csv('country-ipv6.csv',usecols=['geoname_id','network'])
ipv4=pd.read_csv('country-ipv4.csv',usecols=['geoname_id','network'])
countrycodes=pd.read_csv('countrycodes.csv',usecols=['geoname_id','continent_code','country_iso_code'],dtype={'geoname_id': 'int32'})

ipv6=ipv6.dropna(subset=['geoname_id'])
ipv4=ipv4.dropna(subset=['geoname_id'])
ipv6['geoname_id']=ipv6['geoname_id'].astype('int32')
ipv4['geoname_id']=ipv4['geoname_id'].astype('int32')

# write dataframes to sqlite
ipv6.to_sql('ipv6', connection, index=False, if_exists='replace')
ipv4.to_sql('ipv4', connection, index=False, if_exists='replace')
countrycodes.to_sql('countrycodes', connection, index=False, if_exists='replace')

# merge query
mergequery="""
SELECT
    ipv6.geoname_id AS geoname_id,
    ipv6.network AS ipv6_network,
    ipv4.network AS ipv4_network,
    countrycodes.continent_code AS continent_code,
    countrycodes.country_iso_code as country_iso_code
FROM ipv6
LEFT JOIN ipv4 ON ipv6.geoname_id=ipv4.geoname_id
LEFT JOIN countrycodes ON ipv6.geoname_id=countrycodes.geoname_id

UNION

SELECT
    ipv4.geoname_id AS geoname_id,
    ipv6.network AS ipv6_network,
    ipv4.network AS ipv4_network,
    countrycodes.continent_code AS continent_code,
    countrycodes.country_iso_code as country_iso_code
FROM ipv4
LEFT JOIN ipv6 on ipv4.geoname_id=ipv6.geoname_id
LEFT JOIN countrycodes ON ipv4.geoname_id=countrycodes.geoname_id
"""

log("wrote dataframes to sqlite, merging...")
mergeddata=pd.read_sql_query(mergequery, connection)
log("merged data, writing to csv...")

mergeddata.to_csv('geodata.csv', index=False)
log("wrote to csv, closing connection...")

connection.close()
