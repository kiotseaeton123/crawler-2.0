import urllib.request,urllib.parse
import re
import certifi
import ssl
import throttle

def downloadurl(url,useragent='kiots',numattempts=3):
    print('-:', url)
    header={'User-agent':useragent}
    request=urllib.request.Request(url,headers=header)
    try:
        context=ssl.create_default_context(cafile=certifi.where())
        html=urllib.request.urlopen(request,context=context).read()
    except urllib.error.URLError as e:
        print(e)
        html=None
        if(numattempts>0) and (hasattr(e,'code') and 500<=e.code<600):
            return downloadurl(url,useragent,numattempts-1)

    html=html.decode('utf-8')
    return html

#link crawler, follow links of a particular regex
def crawllinks(seedurl, linkregex):
    q=[] #queue for filtered links
    q.append(seedurl)
    visited=set() #visited links

    while q:
        currurl=q.pop(0) #FIFO pop(0)
        if currurl in visited:
            continue

        t=throttle.throttle(4)
        t.wait(currurl)
        html=downloadurl(currurl)
        visited.add(currurl)
        for link in getlinks(html):
            if re.match(linkregex,link):
                q.append(link)


def getlinks(html):
    regexobject=re.compile(r'<a[^>]+href=["\'](.*?)["\']', re.IGNORECASE)
    return regexobject.findall(html)


#main
url='https://www.geeksforgeeks.org/python-programming-language/'
# regex=r'.*python.*'
regex=r'.*geeksforgeeks.org/(.*python.*|.*java.*)'
crawllinks(url,regex)
# downloadurl(url)