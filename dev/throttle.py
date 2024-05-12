from datetime import datetime
import time
import urllib.parse

class throttle:
    def __init__(self,delay):
        self.delay=delay
        self.domains={}

    def wait(self,url):
        domain=urllib.parse.urlparse(url).netloc #network location/domain
        accesstime=self.domains.get(domain) #None of not in domains
        if accesstime is not None:
            sleeptime=self.delay-(datetime.now()-accesstime).seconds
            if sleeptime > 0:
                time.sleep(sleeptime)

        self.domains[domain]=datetime.now()