import dask.dataframe as dd

# file too large for memory, use dask to merge
ipv6=dd.read_csv('country-ipv6.csv',usecols=['geoname_id','network'],assume_missing=True)
ipv4=dd.read_csv('country-ipv4.csv',usecols=['geoname_id','network'],assume_missing=True)
countrycodes=dd.read_csv('countrycodes.csv',usecols=['geoname_id','continent_code','country_iso_code'],dtype={'geoname_id': 'int32'})

ipv6=ipv6.dropna(subset=['geoname_id'])
ipv4=ipv4.dropna(subset=['geoname_id'])
ipv6['geoname_id']=ipv6['geoname_id'].astype('int32')
ipv4['geoname_id']=ipv4['geoname_id'].astype('int32')

print(ipv6.dtypes)
print(ipv4.dtypes)
print(countrycodes.dtypes)

merged=dd.merge(ipv6,ipv4,on='geoname_id',how='outer')
merged=dd.merge(merged,countrycodes,on='geoname_id',how='outer')

numpartitions=merged.npartitions
for i in range(numpartitions):
    partition=merged.get_partition(i)
    partition.to_csv(f'geodata_partition_{i}.csv',index=False,single_file=True)

# merged.to_csv('geodata.csv',index=False)