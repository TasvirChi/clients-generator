import unittest

from utils import GetConfig
from utils import BorhanBaseTest
from utils import getTestFile

from BorhanClient.Plugins.Core import BorhanMediaListResponse
from BorhanClient.Plugins.Core import BorhanMediaEntry, BorhanMediaType


class MediaTests(BorhanBaseTest):
    
    def test_list(self):
        resp = self.client.media.list()
        self.assertIsInstance(resp, BorhanMediaListResponse)
        
        objs = resp.objects
        self.assertIsInstance(objs, list)
        
        [self.assertIsInstance(o, BorhanMediaEntry) for o in objs]
        

    def test_createRemote(self):
        mediaEntry = BorhanMediaEntry()
        mediaEntry.setName('pytest.MediaTests.test_createRemote')
        mediaEntry.setMediaType(BorhanMediaType(BorhanMediaType.VIDEO))
            
        ulFile = getTestFile('DemoVideo.flv')
        uploadTokenId = self.client.media.upload(ulFile)            
                     
        mediaEntry = self.client.media.addFromUploadedFile(mediaEntry, uploadTokenId)
        
        self.assertIsInstance(mediaEntry.getId(), unicode)
        
        #cleanup
        self.client.media.delete(mediaEntry.id)
        
class Utf8_tests(BorhanBaseTest):
    
    def test_utf8_name(self):
        test_unicode = u'\u03dd\xf5\xf6'  #an odd representation of the word 'FOO'
        mediaEntry = BorhanMediaEntry()
        mediaEntry.setName(u'pytest.MediaTests.test_UTF8_name'+test_unicode)
        mediaEntry.setMediaType(BorhanMediaType(BorhanMediaType.VIDEO))
        ulFile = getTestFile('DemoVideo.flv')
        uploadTokenId = self.client.media.upload(ulFile)
        
        #this will throw an exception if fail.
        mediaEntry = self.client.media.addFromUploadedFile(mediaEntry, uploadTokenId)
            
        self.addCleanup(self.client.media.delete, mediaEntry.getId())
    
    def test_utf8_tags(self):

        test_unicode = u'\u03dd\xf5\xf6'  #an odd representation of the word 'FOO'
        mediaEntry = BorhanMediaEntry()
        mediaEntry.setName('pytest.MediaTests.test_UTF8_tags')
        mediaEntry.setMediaType(BorhanMediaType(BorhanMediaType.VIDEO))
        ulFile = getTestFile('DemoVideo.flv')
        uploadTokenId = self.client.media.upload(ulFile)

        mediaEntry.setTags(test_unicode)
        
        #this will throw an exception if fail.
        mediaEntry = self.client.media.addFromUploadedFile(mediaEntry, uploadTokenId)
            
        self.addCleanup(self.client.media.delete, mediaEntry.getId())
        

def test_suite():
    return unittest.TestSuite((
        unittest.makeSuite(MediaTests),
        unittest.makeSuite(Utf8_tests)
        ))

if __name__ == "__main__":
    suite = test_suite()
    unittest.TextTestRunner(verbosity=2).run(suite)