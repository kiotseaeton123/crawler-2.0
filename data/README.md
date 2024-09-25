### About
- crawler project contains geolocation resolver package
- geolocation resolver uses GeoLite2, MaxMind's free geolocation database
- data file too large for version control, download from MaxMind's official site

### load_geodata.py
- GeoLite2 stores network address in CIDR format
- Load into database as network address range `ipstart`-`ipend`
- ipv4 addresses as SQLite INTEGER
- ipv6 as BLOBs, as they exceed size of SQLite INTEGERs
- ip ranges and ip to`geoname_id` indexed for efficient querying

### Convenient Tools for Data Processing
- `sed/awk` for stream handling large files
- `ed` for in-place editing of null data
```
<!-- example -->
> ed datafile
> g/^[^,]*,,$/d
```