### About
- geolocation resolver app uses GeoLite2, MaxMind's free geolocation database
- data file too large for version control, download from MaxMind's official site

### Convenient Tools for Data Processing
- `sed/awk` for stream handling large files
- `ed` for in-place editing of null data

```
> ed datafile
> g/^[^,]*,,$/d
```