app uses GeoLite2, MaxMind's free geolocation database
data file too large for version control, download from MaxMind's official site

- parse applicable data
- I used `sed/awk` for stream handling large files, and `ed` for in-place editing of null data

```
ed datafile
g/^[^,]*,,$/d
```