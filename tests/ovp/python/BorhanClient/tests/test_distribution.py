import unittest

from utils import GetConfig
from utils import BorhanBaseTest

from BorhanClient.Plugins.ContentDistribution import BorhanDistributionProviderListResponse, BorhanDistributionProvider
from BorhanClient.Plugins.ContentDistribution import BorhanDistributionProfileListResponse, BorhanDistributionProfile
from BorhanClient.Plugins.ContentDistribution import BorhanEntryDistributionListResponse, BorhanEntryDistribution

class DistributionProviderTests(BorhanBaseTest):
    
    def test_list(self):
        resp = self.client.contentDistribution.distributionProvider.list()
        self.assertIsInstance(resp, BorhanDistributionProviderListResponse)
    
        objs = resp.objects
        self.assertIsInstance(objs, list)
    
        [self.assertIsInstance(o, BorhanDistributionProvider) for o in objs]
        
        
class DistributionProfileTests(BorhanBaseTest):
    
    def test_list(self):
        resp = self.client.contentDistribution.distributionProfile.list()
        self.assertIsInstance(resp, BorhanDistributionProfileListResponse)   
        
        objs = resp.objects
        self.assertIsInstance(objs, list)
    
        [self.assertIsInstance(o, BorhanDistributionProfile) for o in objs]
        
class entryDistributionTests(BorhanBaseTest):
    
    def test_list(self):
        resp = self.client.contentDistribution.entryDistribution.list()
        self.assertIsInstance(resp, BorhanEntryDistributionListResponse)
    
        objs = resp.objects
        self.assertIsInstance(objs, list)
    
        [self.assertIsInstance(o, BorhanEntryDistribution) for o in objs]
    
def test_suite():
    return unittest.TestSuite((
        unittest.makeSuite(DistributionProviderTests),
        unittest.makeSuite(DistributionProfileTests),
        unittest.makeSuite(entryDistributionTests),
        ))

if __name__ == "__main__":
    suite = test_suite()
    unittest.TextTestRunner(verbosity=2).run(suite)