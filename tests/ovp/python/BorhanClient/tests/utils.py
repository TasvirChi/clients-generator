import os, sys, inspect
import unittest
import ConfigParser

from BorhanClient import BorhanClient, BorhanConfiguration
from BorhanClient.Base import BorhanObjectFactory, BorhanEnumsFactory
from BorhanClient.Base import IBorhanLogger

from BorhanClient.Plugins.Core import BorhanSessionType

dir = os.path.dirname(__file__)
filename = os.path.join(dir, 'config.ini')

config = ConfigParser.ConfigParser()
config.read(filename)
PARTNER_ID = config.getint("Test", "partnerId")
SERVICE_URL = config.get("Test", "serviceUrl")
ADMIN_SECRET = config.get("Test", "adminSecret")
USER_NAME = config.get("Test", "userName")

import logging
logging.basicConfig(level = logging.DEBUG,
                    format = '%(asctime)s %(levelname)s %(message)s',
                    stream = sys.stdout)

class BorhanLogger(IBorhanLogger):
    def log(self, msg):
        logging.info(msg)

def GetConfig():
    config = BorhanConfiguration()
    config.serviceUrl = SERVICE_URL
    config.setLogger(BorhanLogger())
    return config

def getTestFile(filename, mode='rb'):
    testFileDir = os.path.dirname(os.path.abspath(inspect.getfile(inspect.currentframe())))
    return file(testFileDir+'/'+filename, mode)
    
    

class BorhanBaseTest(unittest.TestCase):
    """Base class for all Borhan Tests"""
    #TODO  create a client factory as to avoid thrashing borhan with logins...
    
    def setUp(self):
        #(client session is enough when we do operations in a users scope)
        self.config = GetConfig()
        self.client = BorhanClient(self.config)
        self.ks = self.client.generateSession(ADMIN_SECRET, USER_NAME, 
                                             BorhanSessionType.ADMIN, PARTNER_ID, 
                                             86400, "")
        self.client.setKs(self.ks)            
            
            
    def tearDown(self):
        
        #do cleanup first, probably relies on self.client
        self.doCleanups()
        
        del(self.ks)
        del(self.client)
        del(self.config)
        
